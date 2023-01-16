package io.github.reconsolidated.malinka.offer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Oferta_Promocja")
public class OfferPromotion {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="IdOferty")
    private Long offerId;
    @Column(name="IdPromocji")
    private Long promotionId;
}
