package io.github.reconsolidated.malinka.mainPage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsDbRepo extends JpaRepository<Product, Long> {
}
