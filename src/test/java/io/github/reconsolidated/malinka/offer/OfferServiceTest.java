package io.github.reconsolidated.malinka.offer;

import io.github.reconsolidated.malinka.mainPage.Product;
import io.github.reconsolidated.malinka.mainPage.ProductsService;
import io.github.reconsolidated.malinka.promotion.Promotion;
import io.github.reconsolidated.malinka.promotion.PromotionService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {


    // Services
    private static final ProductsService productsService = Mockito.mock(ProductsService.class);
    private static final PromotionService promotionService = Mockito.mock(PromotionService.class);

    // Repositories
    private static final OfferRepository offerRepository = Mockito.mock(OfferRepository.class);
    private static final OfferProductRepository offerProductRepository = Mockito.mock(OfferProductRepository.class);
    private static final OfferPromotionRepository offerPromotionRepository = Mockito.mock(OfferPromotionRepository.class);

    static Offer mockOffer;
    static List<Product> mockProducts;
    static List<Promotion> mockPromotions;
    static List<OfferProduct> mockOfferProducts;
    static List<OfferPromotion> mockOfferPromotions;

    @BeforeAll
    static void prepareMock() {
        // prepare mock data
        mockOffer = new Offer();
        mockOffer.setId(1L);

        mockProducts = new ArrayList<>();
        Product productToAdd = new Product("Mleko", "/images/mleko.jpg","dairy", "1,99 zł/szt", 1.99, false);
        productToAdd.setGeneratedId(1L);
        mockProducts.add(productToAdd);
        productToAdd = new Product("Jajka", "data:image/jpeg;base64,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","dairy", "1,99 zł/szt", 1.99, true);
        productToAdd.setGeneratedId(2L);
        mockProducts.add(productToAdd);
        productToAdd = new Product("Ser", "https://mlekovita.com.pl/uploads/products/622/6379ser-gouda-z-czarnuszka-jpg.jpg","dairy", "3,99 zł/kg", 3.99, true);
        productToAdd.setGeneratedId(3L);
        mockProducts.add(productToAdd);
        productToAdd = new Product("Pomarańcze 5kg", "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQ8P8Jvk1KvaF8GUWleLZq6E6Q9EMW1qb5QYC82w3z45LbKq2ztp_-O31H8eC7aoZUrzUwAjzJ8aBZYqTM0qAR1gYXZ574zi1GIumTN-WezUuLsd-lJ49KM&usqp=CAE","fruit", "25,99 zł/kg", 25.99, true);
        productToAdd.setGeneratedId(20L);
        mockProducts.add(productToAdd);

        mockPromotions = new ArrayList<Promotion>(Arrays.asList(
                new Promotion(1L, "Mleko 10%", 0.9f, false, 0, true),
                new Promotion(2L, "Jajka 20%", 0.8f, false, 0, true),
                new Promotion(10L, "Kasza 60%", 0.6f, false, 0, true)
        ));

        mockOfferProducts = new ArrayList<OfferProduct>();
        OfferProduct offerProductToAdd = new OfferProduct();
        offerProductToAdd.setOfferId(1L);
        offerProductToAdd.setProductId(1L);
        mockOfferProducts.add(offerProductToAdd);

        offerProductToAdd = new OfferProduct();
        offerProductToAdd.setOfferId(1L);
        offerProductToAdd.setProductId(2L);
        mockOfferProducts.add(offerProductToAdd);

        offerProductToAdd = new OfferProduct();
        offerProductToAdd.setOfferId(1L);
        offerProductToAdd.setProductId(3L);
        mockOfferProducts.add(offerProductToAdd);

        mockOfferPromotions = new ArrayList<OfferPromotion>();
        OfferPromotion offerPromotionToAdd = new OfferPromotion();
        offerPromotionToAdd.setOfferId(1L);
        offerPromotionToAdd.setPromotionId(1L);
        mockOfferPromotions.add(offerPromotionToAdd);

        offerPromotionToAdd = new OfferPromotion();
        offerPromotionToAdd.setOfferId(1L);
        offerPromotionToAdd.setPromotionId(2L);
        mockOfferPromotions.add(offerPromotionToAdd);

        //mock productsService data
        Mockito.when(productsService.getById(1L)).thenReturn(mockProducts.get(0));
        Mockito.when(productsService.getById(2L)).thenReturn(mockProducts.get(1));
        Mockito.when(productsService.getById(3L)).thenReturn(mockProducts.get(2));

        //mock promotionService data
        Mockito.when(promotionService.getById(1L)).thenReturn(mockPromotions.get(0));
        Mockito.when(promotionService.getById(2L)).thenReturn(mockPromotions.get(1));

        //mock offerProductRepository
        Mockito.when(offerProductRepository.findAll()).thenReturn(mockOfferProducts);

        //mock offerPromotionRepository
        Mockito.when(offerPromotionRepository.findAll()).thenReturn(mockOfferPromotions);

    }

    @Test
    void getProductsByOfferTest() {
        OfferService offerService = new OfferService(productsService, promotionService, offerRepository, offerProductRepository, offerPromotionRepository);
        List<Product> expectedResult = new ArrayList<Product>();
        expectedResult.add(mockProducts.get(0));
        expectedResult.add(mockProducts.get(1));
        expectedResult.add(mockProducts.get(2));
        List<Product> actualResult = offerService.getProductsByOffer(mockOffer);
        assertEquals(expectedResult, actualResult);
    }
    @Test
    void getPromotionsByOfferTest() {
        OfferService offerService = new OfferService(productsService, promotionService, offerRepository, offerProductRepository, offerPromotionRepository);
        List<Promotion> expectedResult = new ArrayList<Promotion>();
        expectedResult.add(mockPromotions.get(0));
        expectedResult.add(mockPromotions.get(1));
        List<Promotion> actualResult = offerService.getPromotionsByOffer(mockOffer);
        assertEquals(expectedResult, actualResult);
    }
    @Test
    @DisplayName("Total calculation test")
    void getTotalTest() {
        OfferService offerService = new OfferService(productsService, promotionService, offerRepository, offerProductRepository, offerPromotionRepository);

        double actualTotal = offerService.getTotal(mockOffer);
        assertEquals(1.99*0.9f + 1.99*0.8f + 3.99, actualTotal);
    }

    @Test
    void getTotalForEmptyOfferTest() {
        OfferService offerService = new OfferService(productsService, promotionService, offerRepository, offerProductRepository, offerPromotionRepository);

        Offer emptyOffer = new Offer();
        emptyOffer.setId(10000L);

        double actualTotal = offerService.getTotal(emptyOffer);

        assertEquals(0, actualTotal);
    }
}