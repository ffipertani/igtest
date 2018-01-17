package com.ff.igtest.factory;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.model.Action;
import com.ff.igtest.model.Order;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class OrderFactoryTest {

    @Test
    public void shouldCreateOrders() throws IOException, OrderException {
        OrderFactory factory = new OrderFactory();
        MultipartFile file = mock(MultipartFile.class);
        given(file.getInputStream()).willReturn(Thread.currentThread().getContextClassLoader().getResourceAsStream("orders.xml"));

        List<Order> result = factory.createOrders(file);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactly(
                new Order.OrderBuilder()
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
                        .build()
        );

    }
}