package server;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class WebsocketConfigTest {

    @Test
    void testRegisterStompEndpoints() {
        WebsocketConfig config = new WebsocketConfig();
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);

        config.registerStompEndpoints(registry);

        verify(registry, times(1)).addEndpoint("/websocket");
    }

    @Test
    void testConfigureMessageBroker() {
        WebsocketConfig config = new WebsocketConfig();
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);

        config.configureMessageBroker(registry);

        verify(registry, times(1)).enableSimpleBroker("/topic");
        verify(registry, times(1)).setApplicationDestinationPrefixes("/app");
    }

    @Test
    void testConfigureMessageConverters() {
        WebsocketConfig config = new WebsocketConfig();
        List<MessageConverter> messageConverters = new ArrayList<>();

        boolean addDefaultConverters = config.configureMessageConverters(messageConverters);

        assertFalse(addDefaultConverters);
        assertEquals(1, messageConverters.size());
    }
}