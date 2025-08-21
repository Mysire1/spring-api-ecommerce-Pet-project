package org.example.springapie.mappers;

import org.example.springapie.dtos.ProductDto;
import org.example.springapie.entities.Category;
import org.example.springapie.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);

    default Category mapCategoryIdToCategory(Byte categoryId) {
        if (categoryId == null) return null;
        return new Category(categoryId);
    }

    default Byte mapCategoryToCategoryId(Category category) {
        if (category == null) return null;
        return category.getId();
    }

    @Mapping(target = "id", ignore = true)
    void update(ProductDto productDto,@MappingTarget Product product);
}
