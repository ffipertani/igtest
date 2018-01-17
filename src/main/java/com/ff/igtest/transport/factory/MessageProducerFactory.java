package com.ff.igtest.transport.factory;

import com.ff.igtest.transport.TransportInfo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

@Component
public class MessageProducerFactory {

    public MessageProducer createMessageProducer(Session session, TransportInfo transportInfo) throws JMSException {
        if (transportInfo.isQueue()) {
            return session.createProducer(session.createQueue(transportInfo.getName()));
        } else {
            return session.createProducer(session.createTopic(transportInfo.getName()));
        }
    }
}
