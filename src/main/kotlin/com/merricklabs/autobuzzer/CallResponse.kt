package com.merricklabs.autobuzzer

data class CallResponse(val message: String, val input: Map<String, Any>) : Response()
