package com.ff.igtest.factory;

import com.ff.igtest.exception.OrderException;
import com.ff.igtest.model.Action;
import com.ff.igtest.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ff.igtest.ApplicationConstants.ACCOUNT_TAG;
import static com.ff.igtest.ApplicationConstants.ACTION_TAG;
import static com.ff.igtest.ApplicationConstants.MARKET_TAG;
import static com.ff.igtest.ApplicationConstants.ORDER_TAG;
import static com.ff.igtest.ApplicationConstants.RECEIVEDAT_TAG;
import static com.ff.igtest.ApplicationConstants.SIZE_TAG;
import static com.ff.igtest.ApplicationConstants.SUBMITTEDAT_TAG;

@Component
public class OrderFactory {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public List<Order> createOrders(MultipartFile multipartFile) throws OrderException {
        try {
            LOG.debug("Start processing multipart file");
            List<Order> orders = new ArrayList<>();
            Order currentOrder = null;
            XMLStreamReader streamReader = createStreamReader(multipartFile.getInputStream());
            streamReader.nextTag();  //Skip root tag

            while (streamReader.hasNext()) {
                if (streamReader.isStartElement() && processTag(streamReader, currentOrder)) {
                    addOrder(currentOrder, orders);
                    currentOrder = new Order();
                }
                streamReader.next();
            }
            addOrder(currentOrder, orders);
            LOG.debug("Read {} orders from input file", orders.size());
            return orders;
        } catch (Exception e) {
            LOG.warn("Read error while converting orders", e);
            throw new OrderException("Error reading orders. Check input xml.", e);
        }
    }

    private void addOrder(Order order, List<Order> orders) {
        if (order != null) {
            orders.add(order);
        }
    }

    private XMLStreamReader createStreamReader(InputStream inputStream) throws IOException, XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(inputStream);
        return streamReader;
    }

    private boolean processTag(XMLStreamReader streamReader, Order currentOrder) throws XMLStreamException {
        switch (streamReader.getLocalName()) {
            case ACCOUNT_TAG: {
                currentOrder.setAccount(streamReader.getElementText());
                break;
            }
            case SUBMITTEDAT_TAG: {
                currentOrder.setSubmittedAt(new Date(Long.parseLong(streamReader.getElementText())));
                break;
            }
            case RECEIVEDAT_TAG: {
                currentOrder.setReceivedAt(new Date(Long.parseLong(streamReader.getElementText())));
                break;
            }
            case MARKET_TAG: {
                currentOrder.setMarket(streamReader.getElementText());
                break;
            }
            case ACTION_TAG: {
                currentOrder.setAction(Action.valueOf(streamReader.getElementText()));
                break;
            }
            case SIZE_TAG: {
                currentOrder.setSize(Double.parseDouble(streamReader.getElementText()));
                break;
            }
            case ORDER_TAG: {
                return true;
            }
        }
        return false;
    }
}
