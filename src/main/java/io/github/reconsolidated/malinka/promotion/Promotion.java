package io.github.reconsolidated.malinka.promotion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Promocja")
public class Promotion {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="IdProduktu")
    private Long productId;
    @Column(name="Nazwa")
    private String name;
    @Column(name="Znizka")
    private float sale;
    @Column(name="CzyZaPunkty")
    private boolean isForPoints;
    @Column(name="ilePunktow")
    private int points;
    @Column(name="CzyAktywna")
    private boolean isActive;
    @Column(name="CreatedAt")
    private Long createdAtTimestamp;
    @Column(name="UpdatedAt")
    private Long updatedAtTimestamp;
}
