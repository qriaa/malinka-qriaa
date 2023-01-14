package io.github.reconsolidated.malinka.model.delivery;

public enum DeliveryMethod {
    //name of delivery and price
    STANDARD(5.20),
    PARCEL(1.2),
    SELF_PICKUP(2.3);

    DeliveryMethod(double price) {
    }
}