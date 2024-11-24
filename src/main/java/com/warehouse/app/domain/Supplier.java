package com.warehouse.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A Supplier.
 */
@Entity
@Table(name = "supplier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "address")
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_supplier__products",
        joinColumns = @JoinColumn(name = "supplier_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @JsonIgnoreProperties(value = { "warehouses", "batches", "suppliers" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
    @JsonIgnoreProperties(value = { "supplier" }, allowSetters = true)
    private Set<SupplierContact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Supplier id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Supplier name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public Supplier contactInfo(String contactInfo) {
        this.setContactInfo(contactInfo);
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return this.address;
    }

    public Supplier address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Supplier products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Supplier addProducts(Product product) {
        this.products.add(product);
        return this;
    }

    public Supplier removeProducts(Product product) {
        this.products.remove(product);
        return this;
    }

    public Set<SupplierContact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<SupplierContact> supplierContacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setSupplier(null));
        }
        if (supplierContacts != null) {
            supplierContacts.forEach(i -> i.setSupplier(this));
        }
        this.contacts = supplierContacts;
    }

    public Supplier contacts(Set<SupplierContact> supplierContacts) {
        this.setContacts(supplierContacts);
        return this;
    }

    public Supplier addContacts(SupplierContact supplierContact) {
        this.contacts.add(supplierContact);
        supplierContact.setSupplier(this);
        return this;
    }

    public Supplier removeContacts(SupplierContact supplierContact) {
        this.contacts.remove(supplierContact);
        supplierContact.setSupplier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return getId() != null && getId().equals(((Supplier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
