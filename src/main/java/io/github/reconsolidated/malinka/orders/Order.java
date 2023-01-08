package io.github.reconsolidated.malinka.orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private String address;
    private String city;
    private String street;
    private String localNumber;
    private String selectParcelLocker;
    private String pickupTime;
    private String paymentMethod;
    private boolean paymentSuccessful;
}
