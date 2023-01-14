package io.github.reconsolidated.malinka.model.delivery;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
public class ParcelDelivery extends Delivery{
    private String selectParcelLocker;

    public ParcelDelivery() {}
}
