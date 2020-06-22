package com.googleinterns.smb;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(OrderDocumentsController.class)
public class OrderTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private FireStoreInstance fireStoreInstance;

    @InjectMocks
    private OrderDocumentsController odc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(odc).build();
    }

    @Test
    public void placeOrderTest() throws Exception {
        OrderGeneration sampleOrderRequest = new OrderGeneration();
        sampleOrderRequest.setOrderId("1592747922:23456");
        sampleOrderRequest.setUserId("MockUserID");
        List<Item> items = new ArrayList<>();
        items.add(new Item("Surf Excel 1kg", 9, 80, "8009098989765"));
        sampleOrderRequest.setItemDetails(items);
        mvc.perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(toJson(sampleOrderRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemsInAnOrderTest() throws Exception {

        Item firstItem = new Item("Surf Excel 1kg", 9, 80, "8009098989765");
        Item secondItem = new Item("Britannia Good Day 20gm", 5, 20, "8009123989765");

        List<Item> mockItems = new ArrayList<>();
        mockItems.add(firstItem);
        mockItems.add(secondItem);

        Mockito.when(odc.getItemDetails("1592747922:23456")).thenReturn(mockItems);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getItemDetailsForOrderID?orderId=1592747922:23456")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        String expected = "[{\"quantity\":9," + "\"ean\":\"8009098989765\"," + "\"item_name\":\"Surf Excel 1kg\","
                + "\"unit_price\":80.0," + "\"EAN\":\"8009098989765\"}," + "{\"quantity\":5,"
                + "\"ean\":\"8009123989765\"," + "\"item_name\":\"Britannia Good Day 20gm\"," + "\"unit_price\":20.0,"
                + "\"EAN\":\"8009123989765\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    private String toJson(OrderGeneration sampleOrderRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(sampleOrderRequest);
    }

}