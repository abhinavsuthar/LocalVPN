package com.vpn.packet

import java.nio.ByteBuffer

class Packet(buffer: ByteBuffer) {

    val IP4_HEADER_SIZE = 20
    val TCP_HEADER_SIZE = 20
    val UDP_HEADER_SIZE = 8

    // var ip4Header: IP4Header
    // var tcpHeader: TCPHeader
    // var udpHeader: UDPHeader
    // var backingBuffer: ByteBuffer
}