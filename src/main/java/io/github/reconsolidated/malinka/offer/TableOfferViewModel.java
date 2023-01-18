package io.github.reconsolidated.malinka.offer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableOfferViewModel {
    private Offer offer;
    private String firstProductName;
    private String firstPromotionName;
    private String total;
}