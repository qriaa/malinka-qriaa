package io.github.reconsolidated.malinka.offer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="Oferta")
public class Offer {
    @Id
    @GeneratedValue
    private Long id;
}
