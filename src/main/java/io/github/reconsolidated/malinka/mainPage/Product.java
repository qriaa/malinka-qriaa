package io.github.reconsolidated.malinka.mainPage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String name = "default";
    private String fileName = "default";
    private String category = "default";
    private String priceTag = "default";
    private double price = 0.01;
}
