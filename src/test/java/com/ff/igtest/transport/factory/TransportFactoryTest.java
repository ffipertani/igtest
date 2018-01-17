package com.ff.igtest.transport.factory;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.transport.MessageReceiver;
import com.ff.igtest.transport.MessageSender;
import com.ff.igtest.transport.TransportInfo;
import com.ff.igtest.transport.factory.MessageConsumerFactory;
import com.ff.igtest.transport.factory.MessageProducerFactory;
import com.ff.igtest.transport.factory.TransportConnectionFactory;
import com.ff.igtest.transport.factory.TransportFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TransportFactoryTest {
    @Mock
    private TransportConnectionFactory transportConnectionFactory;
    @Mock
    private MessageProducerFactory messageProducerFactory;
    @Mock
    private MessageConsumerFactory messageConsumerFactory;
    @InjectMocks
    private TransportFactory transportFactory;


    @Test
    public void shouldCreateMessageSender() throws JMSException, OrderException {
        TransportInfo transportInfo = createTransportInfo(false);

        MessageSender result = transportFactory.createMessageSender(transportInfo);

        assertThat(result).isNotNull();
    }

    @Test
    public void shouldCreateMessageReceiver() throws JMSException, OrderException {
        TransportInfo transportInfo = createTransportInfo(false);

        MessageReceiver result = transportFactory.createMessageReceiver(transportInfo);

        assertThat(result).isNotNull();
    }


    private static TransportInfo createTransportInfo(boolean queue) {
        return new TransportInfo("username", "password", "url", "name", queue, "clientId", true);
    }

}