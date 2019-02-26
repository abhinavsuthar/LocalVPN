package com.vpn.packet


private enum class TransportProtocol(val number: Int) {
    TCP(6),
    UDP(17),
    Other(0xFF);

    private fun numberToEnum(protocolNumber: Int): TransportProtocol {
        return when (protocolNumber) {
            6 -> TCP
            17 -> UDP
            else -> Other
        }
    }
}