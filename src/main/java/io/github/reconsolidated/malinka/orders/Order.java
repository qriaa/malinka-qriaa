package io.github.reconsolidated.malinka.orders;

import io.github.reconsolidated.malinka.basket.BasketProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Order {
    private static long lastId = 0;
    private long id;
    private String address;
    private String city;
    private String street;
    private String localNumber;
    private String selectParcelLocker;
    private String pickupTime;
    private String paymentMethod;
    private boolean paymentSuccessful;
    private String cost;
    private int amount;
    private LocalDateTime date;
    private List<BasketProduct> products = new ArrayList<>();

    public Order() {
        id = lastId++;
    }
}
