package com.warehouse.app.service.mapper;

import com.warehouse.app.domain.Supplier;
import com.warehouse.app.domain.SupplierContact;
import com.warehouse.app.service.dto.SupplierContactDTO;
import com.warehouse.app.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupplierContact} and its DTO {@link SupplierContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierContactMapper extends EntityMapper<SupplierContactDTO, SupplierContact> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierName")
    SupplierContactDTO toDto(SupplierContact s);

    @Named("supplierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SupplierDTO toDtoSupplierName(Supplier supplier);
}
