package br.com.meli.projetointegrador;

import br.com.meli.projetointegrador.dto.CartDTO;
import br.com.meli.projetointegrador.dto.CartWithStatusDTO;
import br.com.meli.projetointegrador.dto.InboundOrderDTO;

import br.com.meli.projetointegrador.model.Cart;
import br.com.meli.projetointegrador.repository.CartRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartIntegrationTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepository cartRepository;

    private static boolean init = false;

    private String getStandardInboundOrder() {
        return "{\n" +
                "    \"orderNumber\": 10,\n" +
                "    \"orderDate\": \"2022-04-27\",\n" +
                "    \"section\": {\n" +
                "        \"sectionCode\": 1,\n" +
                "        \"warehouseCode\": 1\n" +
                "    },\n" +
                "    \"batchStock\": [\n" +
                "        {\n" +
                "            \"productId\": 1,\n" +
                "            \"currentTemperature\": 25.5,\n" +
                "            \"minTemperature\": -10.5,\n" +
                "            \"initialQuantity\": 20,\n" +
                "            \"currentQuantity\": 20,\n" +
                "            \"manufacturingDate\": \"2022-05-01\",\n" +
                "            \"manufacturingTime\": \"2022-05-01T00:00:00\",\n" +
                "            \"expirationDate\": \"2022-09-09\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"productId\": 2,\n" +
                "            \"currentTemperature\": 25.5,\n" +
                "            \"minTemperature\": -10.5,\n" +
                "            \"initialQuantity\": 20,\n" +
                "            \"currentQuantity\": 20,\n" +
                "             \"manufacturingDate\": \"2022-05-01\",\n" +
                "            \"manufacturingTime\": \"2022-05-01T00:00:00\",\n" +
                "            \"expirationDate\": \"2022-09-09\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"productId\": 3,\n" +
                "            \"currentTemperature\": 25.5,\n" +
                "            \"minTemperature\": -10.5,\n" +
                "            \"initialQuantity\": 20,\n" +
                "            \"currentQuantity\": 20,\n" +
                "             \"manufacturingDate\": \"2022-05-01\",\n" +
                "            \"manufacturingTime\": \"2022-05-01T00:00:00\",\n" +
                "            \"expirationDate\": \"2022-09-09\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"productId\": 3,\n" +
                "            \"currentTemperature\": 25.5,\n" +
                "            \"minTemperature\": -10.5,\n" +
                "            \"initialQuantity\": 20,\n" +
                "            \"currentQuantity\": 20,\n" +
                "             \"manufacturingDate\": \"2022-05-01\",\n" +
                "            \"manufacturingTime\": \"2022-05-01T00:00:00\",\n" +
                "            \"expirationDate\": \"2022-09-09\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public String postPurchaseOrder(){
     return   "{\n" +
                "    \"orderDate\": \"2022-02-02\",\n" +
                "    \"customerId\": 1,\n" +
                "    \"orderStatus\": {\n" +
                "        \"statusCode\": \"CART\"\n" +
                "    },\n" +
                "    \"items\": [\n" +
                "        {\n" +
                "            \"advertisementId\": 1,\n" +
                "            \"quantity\": 14\n" +
                "        },\n" +
                "        {\n" +
                "            \"advertisementId\": 2,\n" +
                "            \"quantity\": 14\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private String postInboundOrder(InboundOrderDTO inboundOrderDTO, ResultMatcher resultMatcher) throws Exception {

        MvcResult response = mockmvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inboundOrderDTO)))
                .andExpect(resultMatcher)
                .andReturn();

        return response.getResponse().getContentAsString();
    }
    private String postPurchaseOrder(CartDTO cartDTO, ResultMatcher resultMatcher) throws Exception {

        MvcResult response = mockmvc.perform(post("/api/v1/fresh-products/orders")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cartDTO)))
                .andExpect(resultMatcher)
                .andReturn();

        return response.getResponse().getContentAsString();
    }
    private String putPurchaseOrder() throws Exception {

        MvcResult response = mockmvc.perform(put("/api/v1/fresh-products/orders/1")).andReturn();

        return response.getResponse().getContentAsString();
    }
    private String getPurchaseOrder() throws Exception {

        MvcResult response = mockmvc.perform(get("/api/v1/fresh-products/orders/1")).andReturn();

        return response.getResponse().getContentAsString();
    }

    @BeforeEach
    void initialSetup() throws Exception {

        if (!init) {
            String inboundOrderString = getStandardInboundOrder();
            String purchaseOrderString = postPurchaseOrder();
            CartDTO cartDTO = objectMapper.readValue(purchaseOrderString, new TypeReference<CartDTO>() {
            });

            InboundOrderDTO inboundOrderDTO = objectMapper.readValue(inboundOrderString, new TypeReference<>() {
            });

            postInboundOrder(inboundOrderDTO, status().isCreated());
            postPurchaseOrder(cartDTO, status().isCreated());
            init = true;
        }
    }

    @Test
    void registerValidPurchaseOrder() throws Exception {

        Cart cart = cartRepository.findById(1L).orElse(new Cart());

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(840).setScale(2), cart.getTotalCart()),
                () -> assertEquals(1, cart.getCustomer().getId())
        );

    }
    @Test
    void registerInvalidPurchaseOrder() throws Exception {

        Cart cart = cartRepository.findById(1L).orElse(new Cart());

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(840).setScale(2), cart.getTotalCart()),
                () -> assertEquals(1, cart.getCustomer().getId())
        );

    }

    @Test
    void updatePurchaseOrder() throws Exception {

        putPurchaseOrder();
        Cart cart = cartRepository.findById(1L).orElse(new Cart());

        assertEquals("PURCHASE", cart.getOrderStatus().getStatusCode().name());

    }

    @Test
    void validPurchaseOrder() throws  Exception{

        CartWithStatusDTO cartWithStatusDTO = objectMapper.readValue(getPurchaseOrder(), new TypeReference<CartWithStatusDTO>() {
        });

        Cart cart = cartRepository.findById(1L).orElse(new Cart());

        assertEquals( cartWithStatusDTO.getStatusCode(),cart.getOrderStatus().getStatusCode());
    }


}
