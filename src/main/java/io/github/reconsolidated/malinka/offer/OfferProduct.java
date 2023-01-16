package io.github.reconsolidated.malinka.offer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Oferta_Produkt")
public class OfferProduct {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="IdOferty")
    private Long offerId;
    @Column(name="IdProduktu")
    private Long productId;
}
