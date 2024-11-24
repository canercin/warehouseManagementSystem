package com.warehouse.app.service.mapper;

import com.warehouse.app.domain.Product;
import com.warehouse.app.domain.Supplier;
import com.warehouse.app.service.dto.ProductDTO;
import com.warehouse.app.service.dto.SupplierDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productNameSet")
    SupplierDTO toDto(Supplier s);

    @Mapping(target = "removeProducts", ignore = true)
    Supplier toEntity(SupplierDTO supplierDTO);

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
