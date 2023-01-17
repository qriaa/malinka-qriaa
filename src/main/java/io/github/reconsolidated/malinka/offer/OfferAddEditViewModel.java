package io.github.reconsolidated.malinka.offer;

import io.github.reconsolidated.malinka.mainPage.Product;
import io.github.reconsolidated.malinka.promotion.Promotion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class OfferAddEditViewModel {
    private Long offerId;
    private Long productId;
    private Long promotionId;
    private double sum = 0;
}
