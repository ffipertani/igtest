package com.ff.igtest.service;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.model.Action;
import com.ff.igtest.model.Order;
import com.ff.igtest.transport.MessageSender;
import com.ff.igtest.transport.TransportInfo;
import com.ff.igtest.transport.factory.TransportFactory;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;
    @Mock
    private TransportFactory transportFactory;
    @InjectMocks
    private OrderService orderService;
    @Mock
    private TransportInfo transportInfo;
    @Mock
    private MessageSender messageSender;

    @Test
    public void shouldSendOrders() throws OrderException {
        given(transportFactory.createMessageSender(transportInfo)).willReturn(messageSender);
        List<Order> orders = Lists.newArrayList( new Order.OrderBuilder()
                        .account("AX001")
                        .submittedAt(new Date(1507060723641L))
                        .receivedAt(new Date(1507060723642L))
                        .action(Action.BUY)
                        .market("VOD1.L")
                        .size(100d)
                        .build(),

                new Order.OrderBuilder()
                        .account("AX002")
                        .submittedAt(new Date(1507060723651L))
                        .receivedAt(new Date(1507060723652L))
                        .action(Action.SELL)
                        .market("VOD2.L")
                        .size(200d)
                        .build());

        orderService.send(transportInfo,orders);

        verify(messageSender,times(2)).send(orderArgumentCaptor.capture());
        assertThat(orderArgumentCaptor.getAllValues()).isEqualTo(orders);
    }

}