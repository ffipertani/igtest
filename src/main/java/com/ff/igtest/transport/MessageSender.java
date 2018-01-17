package com.ff.igtest.transport;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.transport.factory.MessageProducerFactory;
import com.ff.igtest.transport.factory.TransportConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.io.Serializable;

public class MessageSender<T extends Serializable> {
    private final TransportInfo transportInfo;
    private final boolean transactional = true;
    private final MessageProducerFactory messageProducerFactory;
    private final TransportConnectionFactory transportConnectionFactory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public MessageSender(TransportInfo transportInfo,
                         TransportConnectionFactory transportConnectionFactory,
                         MessageProducerFactory messageProducerFactory) {
        this.transportInfo = transportInfo;
        this.transportConnectionFactory = transportConnectionFactory;
        this.messageProducerFactory = messageProducerFactory;
    }

    public void start() throws OrderException {
        try {
            connection = transportConnectionFactory.createConnection(transportInfo);
            session = connection.createSession(transactional, Session.AUTO_ACKNOWLEDGE);
            producer = messageProducerFactory.createMessageProducer(session, transportInfo);
        } catch (JMSException e) {
            throw new OrderException("Error while starting", e);
        }
    }

    public void stop() throws OrderException {
        try {
            if (transactional) {
                session.commit();
            }
            session.close();
            connection.close();
            producer = null;
        } catch (JMSException e) {
            throw new OrderException("Error while stopping", e);
        }
    }

    public void send(T o) throws OrderException {
        try {
            if (producer == null) {
                throw new OrderException("Transport not started", null);
            }
            producer.send(session.createObjectMessage(o));
        } catch (JMSException e) {
            throw new OrderException("Error while sending message", e);
        }
    }

}
