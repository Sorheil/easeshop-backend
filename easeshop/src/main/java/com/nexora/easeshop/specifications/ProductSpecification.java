package com.nexora.easeshop.specifications;

import com.nexora.easeshop.models.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductSpecification {

    public static Specification<Product> containsTextInNameOrDescription(String text) {
        if (text == null || text.trim().isEmpty()) {
            // no search text provided, return null to indicate no filtering
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + text.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Product> hasCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> priceInRange(Integer priceRange) {
        if (priceRange == null) {
            return null;
        }

        return (root, query, cb) -> {
            BigDecimal min, max;

            switch (priceRange) {
                case 1:
                    min = BigDecimal.valueOf(0);
                    max = BigDecimal.valueOf(99.99);
                    break;
                case 2:
                    min = BigDecimal.valueOf(100);
                    max = BigDecimal.valueOf(199);
                    break;
                case 3:
                    min = BigDecimal.valueOf(200);
                    max = BigDecimal.valueOf(299);
                    break;
                case 4:
                    min = BigDecimal.valueOf(300);
                    max = BigDecimal.valueOf(399.99);
                    break;
                case 5:
                    min = BigDecimal.valueOf(400);
                    max = null;
                    break;
                default:
                    return null;
            }

            if (max == null) {
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            } else {
                return cb.between(root.get("price"), min, max);
            }
        };
    }

    public static Specification<Product> isNewArrival() {
        return (root, query, cb) -> {
            Instant thirtyDaysAgo = Instant.now().minusSeconds(30L * 24 * 3600);
            return cb.greaterThanOrEqualTo(root.get("createdAt"), thirtyDaysAgo);
        };
    }
}