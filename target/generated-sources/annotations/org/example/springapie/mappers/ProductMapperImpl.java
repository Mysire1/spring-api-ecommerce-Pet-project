package org.example.springapie.mappers;

import javax.annotation.processing.Generated;
import org.example.springapie.dtos.ProductDto;
import org.example.springapie.entities.Category;
import org.example.springapie.entities.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-21T19:47:39+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setCategoryId( productCategoryId( product ) );
        productDto.setId( product.getId() );
        productDto.setName( product.getName() );
        productDto.setDescription( product.getDescription() );
        productDto.setPrice( product.getPrice() );

        return productDto;
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productDto.getId() );
        product.name( productDto.getName() );
        product.description( productDto.getDescription() );
        product.price( productDto.getPrice() );

        return product.build();
    }

    @Override
    public void update(ProductDto productDto, Product product) {
        if ( productDto == null ) {
            return;
        }

        product.setName( productDto.getName() );
        product.setDescription( productDto.getDescription() );
        product.setPrice( productDto.getPrice() );
    }

    private Byte productCategoryId(Product product) {
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
