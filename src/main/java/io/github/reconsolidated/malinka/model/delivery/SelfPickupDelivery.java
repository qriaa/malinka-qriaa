package io.github.reconsolidated.malinka.model.delivery;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class SelfPickupDelivery extends Delivery{
    private String pickupTime;

    public SelfPickupDelivery() {}
}
