package com.warehouse.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.app.domain.enumeration.QuantityTypes;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_type")
    private QuantityTypes quantityType;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_product__warehouses",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "warehouses_id")
    )
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Warehouse> warehouses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @JsonIgnoreProperties(value = { "products", "contacts" }, allowSetters = true)
    private Set<Supplier> suppliers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Batch> batches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Product id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public QuantityTypes getQuantityType() {
        return this.quantityType;
    }

    public Product quantityType(QuantityTypes quantityType) {
        this.setQuantityType(quantityType);
        return this;
    }

    public void setQuantityType(QuantityTypes quantityType) {
        this.quantityType = quantityType;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public Product updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Warehouse> getWarehouses() {
        return this.warehouses;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Product warehouses(Set<Warehouse> warehouses) {
        this.setWarehouses(warehouses);
        return this;
    }

    public Product addWarehouses(Warehouse warehouse) {
        this.warehouses.add(warehouse);
        return this;
    }

    public Product removeWarehouses(Warehouse warehouse) {
        this.warehouses.remove(warehouse);
        return this;
    }

    public Set<Supplier> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        if (this.suppliers != null) {
            this.suppliers.forEach(i -> i.removeProducts(this));
        }
        if (suppliers != null) {
            suppliers.forEach(i -> i.addProducts(this));
        }
        this.suppliers = suppliers;
    }

    public Product suppliers(Set<Supplier> suppliers) {
        this.setSuppliers(suppliers);
        return this;
    }

    public Product addSuppliers(Supplier supplier) {
        this.suppliers.add(supplier);
        supplier.getProducts().add(this);
        return this;
    }

    public Product removeSuppliers(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.getProducts().remove(this);
        return this;
    }

    public Set<Batch> getBatches() {
        return this.batches;
    }

    public void setBatches(Set<Batch> batches) {
        if (this.batches != null) {
            this.batches.forEach(i -> i.setProduct(null));
        }
        if (batches != null) {
            batches.forEach(i -> i.setProduct(this));
        }
        this.batches = batches;
    }

    public Product batches(Set<Batch> batches) {
        this.setBatches(batches);
        return this;
    }

    public Product addBatches(Batch batch) {
        this.batches.add(batch);
        batch.setProduct(this);
        return this;
    }

    public Product removeBatches(Batch batch) {
        this.batches.remove(batch);
        batch.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", quantityType='" + getQuantityType() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
