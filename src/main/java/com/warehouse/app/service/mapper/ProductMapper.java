package com.warehouse.app.service.mapper;

import com.warehouse.app.domain.Product;
import com.warehouse.app.domain.Supplier;
import com.warehouse.app.domain.Warehouse;
import com.warehouse.app.service.dto.ProductDTO;
import com.warehouse.app.service.dto.SupplierDTO;
import com.warehouse.app.service.dto.WarehouseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "warehouses", source = "warehouses", qualifiedByName = "warehouseIdSet")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierIdSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeWarehouses", ignore = true)
    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("warehouseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarehouseDTO toDtoWarehouseId(Warehouse warehouse);

    @Named("warehouseIdSet")
    default Set<WarehouseDTO> toDtoWarehouseIdSet(Set<Warehouse> warehouse) {
        return warehouse.stream().map(this::toDtoWarehouseId).collect(Collectors.toSet());
    }

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierDTO toDtoSupplierId(Supplier supplier);

    @Named("supplierIdSet")
    default Set<SupplierDTO> toDtoSupplierIdSet(Set<Supplier> supplier) {
        return supplier.stream().map(this::toDtoSupplierId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
