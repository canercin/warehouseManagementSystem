package com.warehouse.app.repository;

import com.warehouse.app.domain.SupplierContact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupplierContact entity.
 */
@Repository
public interface SupplierContactRepository extends JpaRepository<SupplierContact, Long> {
    default Optional<SupplierContact> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SupplierContact> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SupplierContact> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select supplierContact from SupplierContact supplierContact left join fetch supplierContact.supplier",
        countQuery = "select count(supplierContact) from SupplierContact supplierContact"
    )
    Page<SupplierContact> findAllWithToOneRelationships(Pageable pageable);

    @Query("select supplierContact from SupplierContact supplierContact left join fetch supplierContact.supplier")
    List<SupplierContact> findAllWithToOneRelationships();

    @Query(
        "select supplierContact from SupplierContact supplierContact left join fetch supplierContact.supplier where supplierContact.id =:id"
    )
    Optional<SupplierContact> findOneWithToOneRelationships(@Param("id") Long id);
}
