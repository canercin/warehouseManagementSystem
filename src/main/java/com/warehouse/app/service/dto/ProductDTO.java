package com.warehouse.app.service.dto;

import com.warehouse.app.domain.enumeration.QuantityTypes;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.warehouse.app.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private UUID id;

    private String name;

    private String description;

    private LocalDate createdAt;

    private QuantityTypes quantityType;

    private LocalDate updatedAt;

    private Set<WarehouseDTO> warehouses = new HashSet<>();

    private Set<SupplierDTO> suppliers = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public QuantityTypes getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(QuantityTypes quantityType) {
        this.quantityType = quantityType;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<WarehouseDTO> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Set<WarehouseDTO> warehouses) {
        this.warehouses = warehouses;
    }

    public Set<SupplierDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<SupplierDTO> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", quantityType='" + getQuantityType() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", warehouses=" + getWarehouses() +
            ", suppliers=" + getSuppliers() +
            "}";
    }
}
