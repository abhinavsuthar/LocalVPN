package com.vpn

import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentLinkedQueue

class ByteBufferPool {
    companion object {
        private const val BUFFER_SIZE = 16384 // XXX: Is this ideal?
        private val pool = ConcurrentLinkedQueue<ByteBuffer>()
        fun acquire(): ByteBuffer? = pool.poll() ?: ByteBuffer.allocate(BUFFER_SIZE)
        fun release(buffer: ByteBuffer) {
            buffer.clear()
            pool.offer(buffer)
        }

        fun clear() {
            pool.clear()
        }
    }
}