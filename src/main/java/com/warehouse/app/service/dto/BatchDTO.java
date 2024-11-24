package com.warehouse.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.warehouse.app.domain.Batch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BatchDTO implements Serializable {

    private UUID id;

    private Integer batchNumber;

    private Integer purchaseQuantity;

    private Float purchasePrice;

    private Float salePrice;

    private LocalDate purchaseDate;

    private ProductDTO product;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public Float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Float salePrice) {
        this.salePrice = salePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatchDTO)) {
            return false;
        }

        BatchDTO batchDTO = (BatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, batchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BatchDTO{" +
            "id='" + getId() + "'" +
            ", batchNumber=" + getBatchNumber() +
            ", purchaseQuantity=" + getPurchaseQuantity() +
            ", purchasePrice=" + getPurchasePrice() +
            ", salePrice=" + getSalePrice() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}
