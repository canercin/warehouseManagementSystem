package com.warehouse.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.warehouse.app.domain.SupplierContact} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContactDTO implements Serializable {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private SupplierDTO supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierContactDTO)) {
            return false;
        }

        SupplierContactDTO supplierContactDTO = (SupplierContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, supplierContactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierContactDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", supplier=" + getSupplier() +
            "}";
    }
}
