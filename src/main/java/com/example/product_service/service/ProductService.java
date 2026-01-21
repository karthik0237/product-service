package com.example.product_service.service;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository,ModelMapper modelMapper){
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public ProductDto createProduct(ProductDto productDto){
        Product product = Product.builder()
                                .name(productDto.getName())
                                .price(productDto.getPrice())
                                .description(productDto.getDescription())
                                .build();
        Product savedProduct = productRepository.save(product);
        log.info("Product created with id: {}",product.getId());
        ProductDto newProductDto = modelMapper.map(savedProduct,ProductDto.class);
        return newProductDto;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());
        log.info("Getting all products");
        return productDtos;
    }
}
