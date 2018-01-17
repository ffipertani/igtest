package com.ff.igtest.transport.factory;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.transport.TransportInfo;
import com.ff.igtest.transport.factory.TransportConnectionFactory;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class TransportConnectionFactoryTest {

    @Test
    public void shouldCreateConnection() throws JMSException, OrderException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        given(connectionFactory.createConnection()).willReturn(connection);
        TransportConnectionFactory transportConnectionFactory = new TransportConnectionFactory() {
            protected ConnectionFactory createConnectionFactory(TransportInfo transportInfo) {
                return connectionFactory;
            }
        };

        Connection result = transportConnectionFactory.createConnection(mock(TransportInfo.class));

        assertThat(result).isSameAs(connection);
        verify(connection, times(1)).start();
    }

}