package com.ff.igtest.transport;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.transport.factory.MessageConsumerFactory;
import com.ff.igtest.transport.factory.TransportConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

public class MessageReceiver<T extends Serializable> {
    private final TransportInfo transportInfo;
    private final MessageConsumerFactory messageConsumerFactory;
    private final TransportConnectionFactory transportConnectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public MessageReceiver(TransportInfo transportInfo,
                           TransportConnectionFactory transportConnectionFactory,
                           MessageConsumerFactory messageConsumerFactory) {
        this.transportInfo = transportInfo;
        this.transportConnectionFactory = transportConnectionFactory;
        this.messageConsumerFactory = messageConsumerFactory;
    }

    public void start() throws OrderException {
        try {
            connection = transportConnectionFactory.createConnection(transportInfo);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = messageConsumerFactory.createMessageConsumer(session, transportInfo);
        } catch (JMSException e) {
            throw new OrderException("Error while starting", e);
        }
    }

    public void stop() throws OrderException {
        try {
            session.close();
            connection.close();
            consumer = null;
        } catch (JMSException e) {
            throw new OrderException("Error while stopping", e);
        }
    }

    public T receive() throws OrderException {
        try {
            if (consumer == null) {
                throw new OrderException("Transport not started", null);
            }
            ObjectMessage message = (ObjectMessage) consumer.receive();
            return (T) message.getObject();
        } catch (JMSException e) {
            throw new OrderException("Error while receiving message", e);
        }
    }

}
