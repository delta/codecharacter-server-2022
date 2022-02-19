package delta.codecharacter.server.notifications

import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import java.lang.reflect.Type
import java.util.function.Consumer

class ClientFrameHandler(frameHandler: Consumer<String>) : StompFrameHandler {
    private val frameHandler: Consumer<String>

    init {
        this.frameHandler = frameHandler
    }

    override fun getPayloadType(headers: StompHeaders): Type {
        return String::class.java
    }

    override fun handleFrame(headers: StompHeaders, payload: Any?) {
        frameHandler.accept(payload.toString())
    }
}
