package com.example.product_service;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        // set mongodb uri from application.properties
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
	@Test
	void shouldCreateProduct() throws JsonProcessingException,Exception {
        ProductDto productDto = getProductRequest();
        String productDtoString = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productDtoString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(6, productRepository.findAll().size());
	}

    private ProductDto getProductRequest() {
        return ProductDto.builder()
                .name("Iphone13")
                .description("mobile phone")
                .price(BigDecimal.valueOf(79000.00))
                .build();
    }

}
