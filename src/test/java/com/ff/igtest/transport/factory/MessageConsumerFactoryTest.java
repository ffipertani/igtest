package com.ff.igtest.transport.factory;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.transport.TransportInfo;
import com.ff.igtest.transport.factory.MessageConsumerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessageConsumerFactoryTest {
    @Captor
    private ArgumentCaptor<Queue> queueCaptor;
    @Captor
    private ArgumentCaptor<Topic> topicCaptor;
    @Mock
    private Queue queue;
    @Mock
    private Topic topic;
    @Mock
    private Session session;
    @InjectMocks
    private MessageConsumerFactory messageConsumerFactory;

    private static TransportInfo createTransportInfo(boolean queue) {
        return new TransportInfo("username", "password", "url", "name", queue, "clientID",true);
    }

    @Test
    public void shouldCreateTopic() throws JMSException, OrderException {
        TransportInfo transportInfo = createTransportInfo(false);
        given(session.createTopic(transportInfo.getName())).willReturn(topic);

        messageConsumerFactory.createMessageConsumer(session, transportInfo);

        verify(session, times(1)).createDurableSubscriber(topicCaptor.capture(),any());
        assertThat(topicCaptor.getValue()).isSameAs(topic);
    }

    @Test
    public void shouldCreateQueue() throws JMSException, OrderException {
        TransportInfo transportInfo = createTransportInfo(true);
        given(session.createQueue(transportInfo.getName())).willReturn(queue);

        messageConsumerFactory.createMessageConsumer(session, transportInfo);

        verify(session, times(1)).createConsumer(queueCaptor.capture());
        assertThat(queueCaptor.getValue()).isSameAs(queue);
    }

}