package com.warehouse.app.repository;

import com.warehouse.app.domain.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierRepositoryWithBagRelationshipsImpl implements SupplierRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERS_PARAMETER = "suppliers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Supplier> fetchBagRelationships(Optional<Supplier> supplier) {
        return supplier.map(this::fetchProducts);
    }

    @Override
    public Page<Supplier> fetchBagRelationships(Page<Supplier> suppliers) {
        return new PageImpl<>(fetchBagRelationships(suppliers.getContent()), suppliers.getPageable(), suppliers.getTotalElements());
    }

    @Override
    public List<Supplier> fetchBagRelationships(List<Supplier> suppliers) {
        return Optional.of(suppliers).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    Supplier fetchProducts(Supplier result) {
        return entityManager
            .createQuery("select supplier from Supplier supplier left join fetch supplier.products where supplier.id = :id", Supplier.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Supplier> fetchProducts(List<Supplier> suppliers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, suppliers.size()).forEach(index -> order.put(suppliers.get(index).getId(), index));
        List<Supplier> result = entityManager
            .createQuery(
                "select supplier from Supplier supplier left join fetch supplier.products where supplier in :suppliers",
                Supplier.class
            )
            .setParameter(SUPPLIERS_PARAMETER, suppliers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
