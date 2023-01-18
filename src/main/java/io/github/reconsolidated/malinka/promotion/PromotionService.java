package io.github.reconsolidated.malinka.promotion;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        ArrayList<Promotion> initData = new ArrayList<Promotion>(Arrays.asList(
                new Promotion(1L, "Mleko 10%", 0.9f, false, 0, true),
                new Promotion(6L, "Kefir 20%", 0.8f, false, 0, true),
                new Promotion(4L, "Masło 40%", 0.6f, false, 0, true),
                new Promotion(12L, "Makaron 15%", 0.85f, false, 0, true),
                new Promotion(13L, "Kurczak 30%", 0.7f, false, 0, true),
                new Promotion(2L, "Ser 5%", 0.95f, false, 0, true)
        ));
        promotionRepository.saveAll(initData);
    }

    public Promotion getNewPromotion(){
        Promotion newPromotion = new Promotion();
        return promotionRepository.save(newPromotion);
    }

    public Promotion addPromotion(Promotion promotion){
        return promotionRepository.save(promotion);
    }
    public Promotion getById(Long id){
        Optional<Promotion> optPromo = promotionRepository.findById(id);
        return optPromo.orElse(null);
    }
    public List<Promotion> getByProductId(Long productId) {
        List<Promotion> outputList = new ArrayList<>();
        for(Promotion promo : promotionRepository.findAll()) {
            if(promo.getProductId() == productId)
                outputList.add(promo);
        }
        return outputList;
    }
    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }
}
