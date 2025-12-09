package com.example.laundry.dto;

import com.example.laundry.model.ServiceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
// import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderRequestTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeServicesRequested() throws Exception {
        String json = "{" +
                "\"studentId\": \"student-1\"," +
                "\"servicesRequested\": [\"WASH\", \"IRON\"]" +
                "}";

        CreateOrderRequest req = objectMapper.readValue(json, CreateOrderRequest.class);

        assertNotNull(req.getServicesRequested(), "servicesRequested should not be null");
        assertEquals(2, req.getServicesRequested().size(), "Should have 2 services");
        assertTrue(req.getServicesRequested().contains(ServiceType.WASH));
        assertTrue(req.getServicesRequested().contains(ServiceType.IRON));
    }
}
