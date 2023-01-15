package io.github.reconsolidated.malinka.basket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProduct, Long> {
}
