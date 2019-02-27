package com.vpn.services

import android.content.Context
import android.net.VpnService
import android.net.wifi.WifiManager
import android.os.ParcelFileDescriptor
import android.text.format.Formatter
import android.util.Log
import com.vpn.ByteBufferPool
import com.vpn.Packet
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.Selector
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors


class MyVpnService : VpnService() {

    private var tag = "suthar"
    private val serverAddress = "172.30.2.245"

    override fun onCreate() {
        super.onCreate()
        startThread()
    }

    private fun connectVPN() {

        val mInterface = Builder().setSession("MyVpnService")
            .setMtu(1500)
            .addAddress(serverAddress, 32)
            .addRoute("0.0.0.0", 0)
            .establish()

        // Packets to be sent are queued in this input stream
        val mIn = FileInputStream(mInterface.fileDescriptor)
        // Packets received need to be written to this output stream
        val mOut = FileOutputStream(mInterface.fileDescriptor)
        // The UDP channel can be used to pass/get ip package to/from server
        val tunnel = DatagramChannel.open()
        // Connect to the server, localhost is used for demonstration only
        tunnel.connect(InetSocketAddress("127.0.0.1", 8087))
        // Protect this socket, so package send by it will not be feedback to the vpn service
        protect(tunnel.socket())
        val buffer = ByteBuffer.allocate(32767)


        // Use a loop to pass packets
        while (true) {
            // Here in this loop the packets are coming in and out...


        }
    }

    private fun startThread() {
        Thread {
            try {
                connectVPN2()
            } catch (e: Exception) {
                Log.d(tag, e.toString())
            }
        }.start()
    }

    private fun connectVPN2() {

        val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

        val builder = Builder()

        val localTunnel: ParcelFileDescriptor = builder
            .addAddress(ip, 24)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8")
            .setMtu(1500)
            .establish()


        val vpnFileDescriptor = localTunnel.fileDescriptor

        test(vpnFileDescriptor)
    }

    private fun test(descriptor: FileDescriptor?) {
        val mIn = FileInputStream(descriptor)
        val mOut = FileOutputStream(descriptor)

        val tunnel = DatagramChannel.open()
        tunnel.connect(InetSocketAddress("10.0.0.1", 8080))
        tunnel.configureBlocking(false)
        protect(tunnel.socket())

        while (true) {

            val packet = ByteBuffer.allocate(65535)

            val length = mIn.read(packet.array())

            if (length != -1 && length > 0) {
                packet.limit(length) // filled till that pos

                // Do read from packet buffer
                // then, packet.clear() or packet.compact() to read again.

                // packet.flip()   // No more writes, make it ready for reading
                tunnel.write(packet)
                packet.clear()

                val x = tunnel.receive(packet)
                Log.d(tag, x?.toString() ?: "")

                mOut.write(packet.array(), 0, length)
                packet.clear()

                /*val x = Packet(packet)
                Log.d(tag, x.toString())
                packet.clear()*/
            }

            Thread.sleep(100)
        }

    }

    private val deviceToNetworkUDPQueue by lazy { ConcurrentLinkedQueue<Packet>() }
    private val deviceToNetworkTCPQueue by lazy { ConcurrentLinkedQueue<Packet>() }
    private val networkToDeviceQueue by lazy { ConcurrentLinkedQueue<Packet>() }
    private val executorService by lazy { Executors.newFixedThreadPool(5) }

    private val udpSelector by lazy { Selector.open() }
    private val tcpSelector by lazy { Selector.open() }

    private fun test_(vpnFileDescriptor: FileDescriptor) {
        Thread {

            val vpnInput = FileInputStream(vpnFileDescriptor).channel
            val vpnOutput = FileOutputStream(vpnFileDescriptor).channel

            try {
                var bufferToNetwork: ByteBuffer? = null
                var dataSent = true
                var dataReceived: Boolean = true
                while (!Thread.interrupted()) {
                    if (dataSent)
                        bufferToNetwork = ByteBufferPool.acquire()
                    else
                        bufferToNetwork!!.clear()

                    // TODO: Block when not connected
                    val readBytes = vpnInput.read(bufferToNetwork)
                    if (readBytes > 0) {
                        dataSent = true
                        bufferToNetwork?.flip()
                        val packet = Packet(bufferToNetwork)
                        when {
                            packet.isUDP -> {
                                Log.d(tag, "UDP packet")
                                deviceToNetworkUDPQueue.offer(packet)
                            }
                            packet.isTCP -> {
                                Log.d(tag, "TCP packet")
                                deviceToNetworkTCPQueue.offer(packet)
                            }
                            else -> {
                                Log.w(tag, "Unknown packet type")
                                Log.w(tag, packet.ip4Header.toString())
                                dataSent = false
                            }
                        }
                        Log.w(tag, packet.ip4Header.toString())
                    } else {
                        dataSent = false
                    }

                    val bufferFromNetwork = networkToDeviceQueue.poll().backingBuffer
                    if (bufferFromNetwork != null) {
                        bufferFromNetwork.flip()
                        while (bufferFromNetwork.hasRemaining())
                            vpnOutput.write(bufferFromNetwork)
                        dataReceived = true

                        ByteBufferPool.release(bufferFromNetwork)
                    } else {
                        dataReceived = false
                    }

                    // TODO: Sleep-looping is not very battery-friendly, consider blocking instead
                    // Confirm if throughput with ConcurrentQueue is really higher compared to BlockingQueue
                    if (!dataSent && !dataReceived)
                        Thread.sleep(10)
                }
            } catch (e: InterruptedException) {
                Log.i(tag, "Stopping")
            } catch (e: IOException) {
                Log.w(tag, e.toString(), e)
            } finally {
                // closeResources(vpnInput, vpnOutput)
            }

        }.start()
    }
}