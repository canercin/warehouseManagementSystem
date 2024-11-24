package com.warehouse.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A Batch.
 */
@Entity
@Table(name = "batch")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Batch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "batch_number")
    private Integer batchNumber;

    @Column(name = "purchase_quantity")
    private Integer purchaseQuantity;

    @Column(name = "purchase_price")
    private Float purchasePrice;

    @Column(name = "sale_price")
    private Float salePrice;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "warehouses", "batches", "suppliers" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Batch id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getBatchNumber() {
        return this.batchNumber;
    }

    public Batch batchNumber(Integer batchNumber) {
        this.setBatchNumber(batchNumber);
        return this;
    }

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getPurchaseQuantity() {
        return this.purchaseQuantity;
    }

    public Batch purchaseQuantity(Integer purchaseQuantity) {
        this.setPurchaseQuantity(purchaseQuantity);
        return this;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public Float getPurchasePrice() {
        return this.purchasePrice;
    }

    public Batch purchasePrice(Float purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(Float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Float getSalePrice() {
        return this.salePrice;
    }

    public Batch salePrice(Float salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public void setSalePrice(Float salePrice) {
        this.salePrice = salePrice;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public Batch purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Batch product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Batch)) {
            return false;
        }
        return getId() != null && getId().equals(((Batch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Batch{" +
            "id=" + getId() +
            ", batchNumber=" + getBatchNumber() +
            ", purchaseQuantity=" + getPurchaseQuantity() +
            ", purchasePrice=" + getPurchasePrice() +
            ", salePrice=" + getSalePrice() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            "}";
    }
}
