package org.example.springapie.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springapie.dtos.ProductDto;
import org.example.springapie.dtos.SearchTextDto;
import org.example.springapie.entities.Category;
import org.example.springapie.entities.Product;
import org.example.springapie.repositories.CategoryRepository;
import org.example.springapie.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        testCategory = new Category("Electronics");
        testCategory = categoryRepository.save(testCategory);

        testProduct = Product.builder()
                .name("Test Laptop")
                .description("High-performance laptop")
                .price(new BigDecimal("999.99"))
                .category(testCategory)
                .build();
        testProduct = productRepository.save(testProduct);
    }

    @Test
    @WithMockUser
    public void testGetAllProducts_Success() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Test Laptop")))
                .andExpect(jsonPath("$[0].description", is("High-performance laptop")))
                .andExpect(jsonPath("$[0].price", is(999.99)))
                .andExpect(jsonPath("$[0].categoryId", is(testCategory.getId().intValue())));
    }

    @Test
    @WithMockUser
    public void testGetAllProducts_FilterByCategory() throws Exception {
        Category category2 = new Category("Books");
        category2 = categoryRepository.save(category2);

        Product product2 = Product.builder()
                .name("Java Programming")
                .description("Learn Java")
                .price(new BigDecimal("49.99"))
                .category(category2)
                .build();
        productRepository.save(product2);

        mockMvc.perform(get("/products")
                        .param("categoryId", testCategory.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Laptop")))
                .andExpect(jsonPath("$[0].categoryId", is(testCategory.getId().intValue())));
    }

    @Test
    @WithMockUser
    public void testCreateProduct_Success() throws Exception {
        ProductDto newProductDto = new ProductDto();
        newProductDto.setName("Smartphone");
        newProductDto.setDescription("Latest model smartphone");
        newProductDto.setPrice(new BigDecimal("799.99"));
        newProductDto.setCategoryId(testCategory.getId());

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Smartphone")))
                .andExpect(jsonPath("$.description", is("Latest model smartphone")))
                .andExpect(jsonPath("$.price", is(799.99)))
                .andExpect(jsonPath("$.categoryId", is(testCategory.getId().intValue())));
    }

    @Test
    @WithMockUser
    public void testUpdateProduct_Success() throws Exception {
        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Laptop");
        updateDto.setDescription("Updated description");
        updateDto.setPrice(new BigDecimal("1099.99"));
        updateDto.setCategoryId(testCategory.getId());

        mockMvc.perform(put("/products/update/{id}", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Laptop")))
                .andExpect(jsonPath("$.description", is("Updated description")))
                .andExpect(jsonPath("$.price", is(1099.99)))
                .andExpect(jsonPath("$.id", is(testProduct.getId().intValue())));
    }

    @Test
    @WithMockUser
    public void testUpdateProduct_NotFound() throws Exception {
        ProductDto updateDto = new ProductDto();
        updateDto.setName("Non-existent Product");
        updateDto.setDescription("This product does not exist");
        updateDto.setPrice(new BigDecimal("999.99"));
        updateDto.setCategoryId(testCategory.getId());

        mockMvc.perform(put("/products/update/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void testDeleteProduct_Success() throws Exception {
        mockMvc.perform(delete("/products/delete/{id}", testProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
    public void testDeleteProduct_NotFound() throws Exception {
        mockMvc.perform(delete("/products/delete/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testSearchProduct_Success() throws Exception {
        Product product2 = Product.builder()
                .name("Gaming Laptop")
                .description("High-end gaming laptop")
                .price(new BigDecimal("1499.99"))
                .category(testCategory)
                .build();
        productRepository.save(product2);

        SearchTextDto searchDto = new SearchTextDto();
        searchDto.setName("Laptop");

        mockMvc.perform(get("/products/search")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser
    public void testCreateMultipleProductsAndRetrieve() throws Exception {
        for (int i = 1; i <= 3; i++) {
            ProductDto productDto = new ProductDto();
            productDto.setName("Product " + i);
            productDto.setDescription("Description " + i);
            productDto.setPrice(new BigDecimal(String.valueOf(100 * i)));
            productDto.setCategoryId(testCategory.getId());

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(productDto)))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}
