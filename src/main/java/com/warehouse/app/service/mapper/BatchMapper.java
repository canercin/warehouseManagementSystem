package com.warehouse.app.service.mapper;

import com.warehouse.app.domain.Batch;
import com.warehouse.app.domain.Product;
import com.warehouse.app.service.dto.BatchDTO;
import com.warehouse.app.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Batch} and its DTO {@link BatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface BatchMapper extends EntityMapper<BatchDTO, Batch> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    BatchDTO toDto(Batch s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
