package io.github.reconsolidated.malinka.offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferPromotionRepository extends JpaRepository<OfferPromotion, Long> {
}
