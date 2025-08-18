package com.nexora.easeshop.integration;

import com.nexora.easeshop.models.Cart;
import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.models.Product;
import com.nexora.easeshop.repositories.CustomerRepository;
import com.nexora.easeshop.repositories.OrderRepository;
import com.nexora.easeshop.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ModelIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCreateAndSaveProduct() {
//        Product product = new Product();
//        product.setName("Test Product");
//        product.setPrice(19.99);
//        product.setDescription("Produit de test");
//        product.setImage_product_url("http://image.jpg");
//
//        Product saved = productRepository.save(product);
//
//        assertNotNull(saved.getId());
//        assertEquals("Test Product", saved.getName());
    }

    @Test
    void testCreateCustomerWithCart() {
//        Cart cart = new Cart();
//
//        Customer customer = new Customer();
//        customer.setUsername("johndoe");
//        customer.setEmail("john@example.com");
//        customer.setPassword("password123");
//        customer.setCart(cart);
//
//        Customer saved = customerRepository.save(customer);
//
//        assertNotNull(saved.getId());
//        assertNotNull(saved.getCart());
    }
}
