package com.ff.igtest.transport.factory;

import com.ff.igtest.transport.TransportInfo;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.Arrays;

@Component
public class TransportConnectionFactory {

    public Connection createConnection(TransportInfo transportInfo) throws JMSException {
        ConnectionFactory connectionFactory = createConnectionFactory(transportInfo);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID(transportInfo.getClientId());
        connection.start();
        return connection;
    }

    protected ConnectionFactory createConnectionFactory(TransportInfo transportInfo) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(transportInfo.getUsername(), transportInfo.getPassword(), transportInfo.getUrl());
        factory.setTrustedPackages(Arrays.asList("*"));
        return factory;
    }
}
