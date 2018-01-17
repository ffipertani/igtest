package com.ff.igtest.service;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.model.Order;
import com.ff.igtest.transport.MessageSender;
import com.ff.igtest.transport.TransportInfo;
import com.ff.igtest.transport.factory.TransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final TransportFactory transportFactory;

    @Autowired
    public OrderService(TransportFactory transportFactory) {
        this.transportFactory = transportFactory;
    }

    public void send(TransportInfo transportInfo, List<Order> orders) throws OrderException {
        LOG.debug("Sending {} orders", orders.size());
        MessageSender<Order> transport = transportFactory.createMessageSender(transportInfo);
        try {
            transport.start();
            for (Order order : orders) {
                transport.send(order);
            }
            LOG.debug("Orders sent");
        } catch (OrderException e) {
            throw e;
        } catch (Exception e) {
            throw new OrderException("Error while sending messages", e);
        } finally {
            transport.stop();
        }
    }
}
