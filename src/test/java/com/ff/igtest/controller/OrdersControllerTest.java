package com.ff.igtest.controller;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.factory.OrderFactory;
import com.ff.igtest.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static com.ff.igtest.ApplicationConstants.HOME;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OrdersControllerTest {
    private static String USERNAME = "username";
    private static String PASSWORD = "password";
    private static String URL = "url";
    private static String NAME = "name";
    private static String TYPE = "type";

    @Mock
    private OrderService orderService;
    @Mock
    private OrderFactory orderFactory;
    @InjectMocks
    private OrdersController ordersController;

    @Test
    public void shouldReturnHomePage() {
        String result = ordersController.home();
        assertThat(result).isEqualTo(HOME);
    }

    @Test
    public void shouldProcessOrdersSuccessfully() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        ResponseEntity result = ordersController.sendOrders(multipartFile, USERNAME, PASSWORD, URL, NAME, TYPE);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void shouldProcessOrdersWithErrorIfSendFails() throws OrderException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Exception exception = new Exception("Error");
        willThrow((new OrderException(exception.getMessage(), exception))).given(orderFactory).createOrders(multipartFile);

        ResponseEntity result = ordersController.sendOrders(multipartFile, USERNAME, PASSWORD, URL, NAME, TYPE);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isEqualTo(exception.getMessage());
    }

    @Test
    public void shouldProcessOrdersWithErrorIfConversionFails() throws OrderException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Exception exception = new Exception("Error");
        willThrow((new OrderException(exception.getMessage(), exception))).given(orderService).send(any(), any());

        ResponseEntity result = ordersController.sendOrders(multipartFile, USERNAME, PASSWORD, URL, NAME, TYPE);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isEqualTo(exception.getMessage());
    }

}