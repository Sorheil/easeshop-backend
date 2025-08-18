package com.nexora.easeshop.mappers;

import com.nexora.easeshop.dtos.CategoryDTO;
import com.nexora.easeshop.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}
