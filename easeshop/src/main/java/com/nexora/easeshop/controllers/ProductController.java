package com.nexora.easeshop.controllers;

import com.nexora.easeshop.dtos.ApiResponse;
import com.nexora.easeshop.dtos.CreateProductDTO;
import com.nexora.easeshop.dtos.ProductDTO;
import com.nexora.easeshop.dtos.UpdateProductDTO;
import com.nexora.easeshop.mappers.ProductMapper;
import com.nexora.easeshop.models.Product;
import com.nexora.easeshop.repositories.ProductRepository;
import com.nexora.easeshop.services.ProductService;
import com.nexora.easeshop.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    //get all products
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, List<ProductDTO>>>> getAllProducts() {
        List<ProductDTO> products = productMapper.toDTOList(productService.getAllProducts());
        Map<String, List<ProductDTO>> data = Map.of("products", products);
        ApiResponse<Map<String, List<ProductDTO>>> response = new ApiResponse<>("List of products", data);
        return ResponseEntity.ok(response);
    }

    //obtain a product by its ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, ProductDTO>>> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productMapper.toDTO(productService.getProductById(id));
        Map<String, ProductDTO> data = Map.of("product", productDTO);
        ApiResponse<Map<String, ProductDTO>> response = new ApiResponse<>("Product found", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchProducts(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "priceRange", required = false) Integer priceRange,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Specification<Product> spec = Specification.where(ProductSpecification.containsTextInNameOrDescription(query)).
                and(ProductSpecification.hasCategory(categoryId)).
                and(ProductSpecification.priceInRange(priceRange));
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        // Mapper les entités en DTO
        List<ProductDTO> productDTOs = productMapper.toDTOList(productPage.getContent());

        // Préparer les données avec produits + pagination
        Map<String, Object> data = Map.of(
                "products", productDTOs,
                "totalElements", productPage.getTotalElements(),
                "totalPages", productPage.getTotalPages(),
                "currentPage", productPage.getNumber(),
                "pageSize", productPage.getSize()
        );

        ApiResponse<Map<String, Object>> response = new ApiResponse<>("List of pagination product", data);

        return ResponseEntity.ok(response);
    }

    //top 10 products new arrivals based on createdAt
    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNewArrivals() {
        Specification<Product> spec = ProductSpecification.isNewArrival();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<ProductDTO> productDTOs = productMapper.toDTOList(productPage.getContent());

        Map<String, Object> data = Map.of(
                "products", productDTOs,
                "totalElements", productPage.getTotalElements(),
                "totalPages", productPage.getTotalPages(),
                "currentPage", productPage.getNumber(),
                "pageSize", productPage.getSize()
        );

        ApiResponse<Map<String, Object>> response = new ApiResponse<>("List of 10 product new arrivals", data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBestSeller() {
        Specification<Product> spec = ProductSpecification.isNewArrival();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<ProductDTO> productDTOs = productMapper.toDTOList(productPage.getContent());

        Map<String, Object> data = Map.of(
                "products", productDTOs,
                "totalElements", productPage.getTotalElements(),
                "totalPages", productPage.getTotalPages(),
                "currentPage", productPage.getNumber(),
                "pageSize", productPage.getSize()
        );

        ApiResponse<Map<String, Object>> response = new ApiResponse<>("List of 10 product new arrivals", data);

        return ResponseEntity.ok(response);
    }
}
