package io.github.reconsolidated.malinka.offer;

import io.github.reconsolidated.malinka.mainPage.Product;
import io.github.reconsolidated.malinka.mainPage.ProductsService;
import io.github.reconsolidated.malinka.promotion.Promotion;
import io.github.reconsolidated.malinka.promotion.PromotionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService {
    // Services
    private final ProductsService productsService;
    private final PromotionService promotionService;

    // Repositories
    private final OfferRepository offerRepository;
    private final OfferProductRepository offerProductRepository;
    private final OfferPromotionRepository offerPromotionRepository;

    // in case of crunch - add cache

    public OfferService(ProductsService productsService, PromotionService promotionService, OfferRepository offerRepository, OfferProductRepository offerProductRepository, OfferPromotionRepository offerPromotionRepository){
        this.productsService = productsService;
        this.promotionService = promotionService;
        this.offerRepository = offerRepository;
        this.offerProductRepository = offerProductRepository;
        this.offerPromotionRepository = offerPromotionRepository;
    }

    /**
     * Creates new offer with an assigned id
     * @return Offer with autoassigned id
     */
    public Offer getNewOffer(){
        Offer newOffer = new Offer();
        return this.offerRepository.save(newOffer); //i really need that generated value - i'm sorry
    }

    /**
     * adds Offer to the database
     * @param newOffer offer object to add
     * @param products list of products to be connected with the offer
     * @param promotions list of promotions to be connected with the offer
     */
    public void addOffer(Offer newOffer, List<Product> products, List<Promotion> promotions) {

        List<OfferProduct> offerProductList = new ArrayList<>();
        for (Product product: products){
            OfferProduct offerProduct = new OfferProduct();
            offerProduct.setOfferId(newOffer.getId());
            offerProduct.setProductId(product.getGeneratedId());
            offerProductList.add(offerProduct);
        }

        List<OfferPromotion> offerPromotionList = new ArrayList<>();
        for(Promotion promotion: promotions){
            OfferPromotion offerPromotion = new OfferPromotion();
            offerPromotion.setOfferId(newOffer.getId());
            offerPromotion.setPromotionId(promotion.getId());
            offerPromotionList.add(offerPromotion);
        }
        offerRepository.save(newOffer);
        offerProductRepository.saveAll(offerProductList);
        offerPromotionRepository.saveAll(offerPromotionList);
    }

    /**
     * updates an existing offer
     * @param offer offer to be updated
     * @param products new list of products
     * @param promotions new list of promotions
     */
    public void updateOffer(Offer offer, List<Product> products, List<Promotion> promotions) {
        List<OfferProduct> offerProductsToDelete = getOfferProducts(offer);
        List<OfferPromotion> offerPromotionsToDelete = getOfferPromotions(offer);

        offerProductRepository.deleteAll(offerProductsToDelete);
        offerPromotionRepository.deleteAll(offerPromotionsToDelete);


        List<OfferProduct> offerProductList = new ArrayList<>();
        for (Product product: products){
            OfferProduct offerProduct = new OfferProduct();
            offerProduct.setOfferId(offer.getId());
            offerProduct.setProductId(product.getGeneratedId());
            offerProductList.add(offerProduct);
        }

        List<OfferPromotion> offerPromotionList = new ArrayList<>();
        for(Promotion promotion: promotions){
            OfferPromotion offerPromotion = new OfferPromotion();
            offerPromotion.setOfferId(offer.getId());
            offerPromotion.setPromotionId(promotion.getId());
            offerPromotionList.add(offerPromotion);
        }
        offerProductRepository.saveAll(offerProductList);
        offerPromotionRepository.saveAll(offerPromotionList);
    }

    /**
     * Gets all offers
     * @return list of offers
     */
    public List<Offer> getAllOffers(){
        return offerRepository.findAll();
    }

    /**
     * gets an offer from repository by id
     * @param id id to be looked for
     * @return found offer
     */
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    /**
     * Gets OfferProducts connected to the offer
     * @param offer host offer
     * @return list of OfferProducts that are connected to that offer
     */
    public List<OfferProduct> getOfferProducts(Offer offer) {
        List<OfferProduct> offerProducts = offerProductRepository.findAll();
        offerProducts = offerProducts.stream().filter(op -> op.getOfferId().equals(offer.getId())).toList();
        return offerProducts;
    }

    /**
     * Gets OfferPromotions connected to the offer
     * @param offer host offer
     * @return list of OfferPromotions that are connected to that offer
     */
    public List<OfferPromotion> getOfferPromotions(Offer offer) {
        List<OfferPromotion> offerPromotions = offerPromotionRepository.findAll();
        offerPromotions = offerPromotions.stream().filter(op -> op.getOfferId().equals(offer.getId())).toList();
        return offerPromotions;
    }

    /**
     * Gets a list of Products connected to that offer
     * @param offer host offer
     * @return List of products connected to that offer
     */
    public List<Product> getProductsByOffer(Offer offer) {
        List<OfferProduct> offerProducts = getOfferProducts(offer);
        List<Product> products = new ArrayList<>();
        for (OfferProduct offerProduct: offerProducts) {
            products.add(productsService.getById(offerProduct.getProductId()));
        }
        return products;
    }
    /**
     * Gets a list of Promotions connected to that offer
     * @param offer host offer
     * @return List of promotions connected to that offer
     */
    public List<Promotion> getPromotionsByOffer(Offer offer) {
        List<OfferPromotion> offerPromotions = getOfferPromotions(offer);
        List<Promotion> promotions = new ArrayList<>();
        for (OfferPromotion offerPromotion: offerPromotions) {
            promotions.add(promotionService.getById(offerPromotion.getPromotionId()));
        }
        return promotions;
    }

    /**
     * Removes offer from the repository
     * @param offer offer to be removed
     */
    public void removeOffer(Offer offer) {
        List<OfferProduct> offerProductsToDelete = getOfferProducts(offer);
        List<OfferPromotion> offerPromotionsToDelete = getOfferPromotions(offer);

        offerProductRepository.deleteAll(offerProductsToDelete);
        offerPromotionRepository.deleteAll(offerPromotionsToDelete);
        offerRepository.delete(offer);
    }

    /**
     * Gets a total of an offer
     * A total is the sum of prices of products (in case there is a promotion in the offer, the promotion is applied
     * to its product)
     * @param offer the offer for which to calculate sum
     * @return The total
     */
    public double getTotal(Offer offer) {
        List<Product> products = getProductsByOffer(offer);
        List<Promotion> promotions = getPromotionsByOffer(offer);

        double sum = 0;

        for(Product product: products){
            List<Promotion> applicablePromotions = promotions.stream().filter(p -> p.getProductId().equals(product.getGeneratedId())).toList();
            double price = product.getPrice();
            for(Promotion promotion: applicablePromotions){
                price *= promotion.getSale();
            }
            sum += price;
        }

        return sum;
    }
    /**
     * Gets a total of an offer
     * A total is the sum of prices of products (in case there is a promotion in the offer, the promotion is applied
     * to its product)
     * @param products the products for which to calculate sum
     * @param promotions the promotions for the products
     * @return The total
     */
    public double getTotal(List<Product> products, List<Promotion> promotions) {
        double sum = 0;

        for(Product product: products){
            List<Promotion> applicablePromotions = promotions.stream().filter(p -> p.getProductId().equals(product.getGeneratedId())).toList();
            double price = product.getPrice();
            for(Promotion promotion: applicablePromotions){
                price *= promotion.getSale();
            }
            sum += price;
        }

        return sum;
    }
}
