package com.ff.igtest.transport.factory;

import com.ff.igtest.transport.MessageReceiver;
import com.ff.igtest.transport.MessageSender;
import com.ff.igtest.transport.TransportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportFactory {
    private final TransportConnectionFactory transportConnectionFactory;
    private final MessageProducerFactory messageProducerFactory;
    private final MessageConsumerFactory messageConsumerFactory;

    @Autowired
    public TransportFactory(TransportConnectionFactory transportConnectionFactory, MessageProducerFactory messageProducerFactory, MessageConsumerFactory messageConsumerFactory) {
        this.transportConnectionFactory = transportConnectionFactory;
        this.messageProducerFactory = messageProducerFactory;
        this.messageConsumerFactory = messageConsumerFactory;
    }

    public MessageSender createMessageSender(TransportInfo transportInfo) {
        return new MessageSender(transportInfo, transportConnectionFactory, messageProducerFactory);
    }

    public MessageReceiver createMessageReceiver(TransportInfo transportInfo) {
        return new MessageReceiver(transportInfo, transportConnectionFactory, messageConsumerFactory);
    }
}
