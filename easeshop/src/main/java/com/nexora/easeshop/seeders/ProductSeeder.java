package com.nexora.easeshop.seeders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexora.easeshop.dtos.ProductDTO;
import com.nexora.easeshop.models.Category;
import com.nexora.easeshop.models.Product;
import com.nexora.easeshop.repositories.CategoryRepository;
import com.nexora.easeshop.repositories.ProductRepository;
import com.nexora.easeshop.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper; // On utilise le mapper

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() > 0) {
            System.out.println("📦 Produits déjà présents, seeder ignoré.");
            return;
        }

        System.out.println("🚀 Lancement du seeder...");

        // Lecture du JSON
        var resource = new ClassPathResource("products.json");
        String json = Files.readString(resource.getFile().toPath());

        // Conversion en liste de ProductDTO
        List<ProductDTO> productDTOList = objectMapper.readValue(json, new TypeReference<>() {});

        for (ProductDTO dto : productDTOList) {
            // Trouver ou créer la catégorie
            Category category = categoryRepository
                    .findByName(dto.getCategory().getName())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(dto.getCategory().getName());
                        return categoryRepository.save(newCategory);
                    });

            // Mapper le DTO vers l'entité
            Product product = productMapper.toEntity(dto);
            product.setCategory(category);

            // Sauvegarde
            productRepository.save(product);
        }

        System.out.println("✅ Seeder terminé avec succès !");
    }
}
