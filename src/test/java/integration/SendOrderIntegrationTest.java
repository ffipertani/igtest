package integration;

import com.ff.igtest.Application;
import com.ff.igtest.exception.OrderException;
import com.ff.igtest.model.Order;
import com.ff.igtest.transport.MessageReceiver;
import com.ff.igtest.transport.TransportInfo;
import com.ff.igtest.transport.factory.TransportFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransportFactory.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(Application.class)
public class SendOrderIntegrationTest {
    private static String ORDERS_FILE = "orders.xml";
    private static String BROKER_URL = "tcp://localhost:61616";
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static String CLIENT_ID = "consumer";
    private static int EXPECTED_ORDERS = 2;

    private BrokerService broker;

    @LocalServerPort
    private int port;
    @Autowired
    private TransportFactory transportFactory;


    @Before
    public void startUp() throws Exception {
        broker = new BrokerService();
        broker.addConnector(BROKER_URL);
        broker.deleteAllMessages();
        broker.start();
    }

    @After
    public void tearDown() throws Exception {
        broker.stop();
    }

    @Test(timeout = 20000)
    public void queueIntegrationTest() throws Exception {
        boolean queue = true;
        String name = "testQueue";

        sendRestRequest(name, queue);
        //No need to register the consumer before sending messages
        TransportInfo transportInfo = new TransportInfo(USERNAME, PASSWORD, BROKER_URL, name, queue, CLIENT_ID);
        MessageReceiver<Order> transport = transportFactory.createMessageReceiver(transportInfo);
        transport.start();

        receiveMessages(transport);
    }

    @Test(timeout = 20000)
    public void topicIntegrationTest() throws Exception {
        boolean queue = false;
        String name = "testTopic";

        TransportInfo transportInfo = new TransportInfo(USERNAME, PASSWORD, BROKER_URL, name, queue, CLIENT_ID);
        MessageReceiver<Order> transport = transportFactory.createMessageReceiver(transportInfo);

        //Register the durable subscriber before sending messages
        transport.start();
        sendRestRequest(name, queue);
        receiveMessages(transport);
    }


    private void sendRestRequest(String name, boolean queue) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        FileSystemResource value = new FileSystemResource(Thread.currentThread().getContextClassLoader().getResource(ORDERS_FILE).getFile());
        map.add("file", value);
        map.add("username", USERNAME);
        map.add("password", PASSWORD);
        map.add("url", BROKER_URL);
        map.add("name", name);
        map.add("type", queue ? "queue" : "topic");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/", HttpMethod.POST, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }


    private void receiveMessages(MessageReceiver<Order> receiver) throws OrderException {
        int expectedOrders = EXPECTED_ORDERS;
        while (expectedOrders > 0) {
            Order order = receiver.receive();
            System.out.print(order);
            expectedOrders--;
        }

    }

}
