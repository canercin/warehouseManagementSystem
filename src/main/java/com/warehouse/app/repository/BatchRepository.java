package com.warehouse.app.repository;

import com.warehouse.app.domain.Batch;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Batch entity.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {
    default Optional<Batch> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Batch> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Batch> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select batch from Batch batch left join fetch batch.product", countQuery = "select count(batch) from Batch batch")
    Page<Batch> findAllWithToOneRelationships(Pageable pageable);

    @Query("select batch from Batch batch left join fetch batch.product")
    List<Batch> findAllWithToOneRelationships();

    @Query("select batch from Batch batch left join fetch batch.product where batch.id =:id")
    Optional<Batch> findOneWithToOneRelationships(@Param("id") UUID id);
}
