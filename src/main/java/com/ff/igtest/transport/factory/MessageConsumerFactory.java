package com.ff.igtest.transport.factory;

import com.ff.igtest.transport.TransportInfo;
import org.springframework.stereotype.Component;

import javax.jms.*;

import static com.ff.igtest.ApplicationConstants.SUBSCRIBER_NAME;

@Component
public class MessageConsumerFactory {

    public MessageConsumer createMessageConsumer(Session session, TransportInfo transportInfo) throws JMSException {
        if (transportInfo.isQueue()) {
            Queue queue = session.createQueue(transportInfo.getName());
            return session.createConsumer(queue);
        } else {
            Topic topic = session.createTopic(transportInfo.getName());
            return session.createDurableSubscriber(topic, SUBSCRIBER_NAME);
        }
    }
}
