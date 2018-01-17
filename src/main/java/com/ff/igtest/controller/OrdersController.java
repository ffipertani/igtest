package com.ff.igtest.controller;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.factory.OrderFactory;
import com.ff.igtest.model.Order;
import com.ff.igtest.service.OrderService;
import com.ff.igtest.transport.TransportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ff.igtest.ApplicationConstants.*;

@Controller
public class OrdersController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final OrderFactory orderFactory;

    @Autowired
    public OrdersController(OrderService orderService, OrderFactory orderFactory) {
        this.orderService = orderService;
        this.orderFactory = orderFactory;
    }

    @GetMapping("/")
    public String home() {
        return HOME;
    }

    @PostMapping("/")
    public ResponseEntity sendOrders(MultipartFile file,
                                     String username,
                                     String password,
                                     String url,
                                     String name,
                                     String type) {
        try {
            TransportInfo transportInfo = new TransportInfo(username, password, url, name, QUEUE.equals(type), PRODUCER_CLIENTID);
            List<Order> orders = orderFactory.createOrders(file);
            LOG.debug("Sending {} order to {}", orders.size(), transportInfo.getName());
            orderService.send(transportInfo, orders);
            LOG.debug("Request processed successfully");
            return new ResponseEntity(orders.size() + " orders sent", HttpStatus.ACCEPTED);
        } catch (OrderException e) {
            LOG.warn("Error processing request.", e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
