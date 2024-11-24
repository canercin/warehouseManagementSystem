package com.warehouse.app.service.mapper;

import com.warehouse.app.domain.Product;
import com.warehouse.app.domain.Warehouse;
import com.warehouse.app.service.dto.ProductDTO;
import com.warehouse.app.service.dto.WarehouseDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Warehouse} and its DTO {@link WarehouseDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productNameSet")
    WarehouseDTO toDto(Warehouse s);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProducts", ignore = true)
    Warehouse toEntity(WarehouseDTO warehouseDTO);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("productNameSet")
    default Set<ProductDTO> toDtoProductNameSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductName).collect(Collectors.toSet());
    }
}
