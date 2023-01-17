package io.github.reconsolidated.malinka.promotion;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    //mock
    private final ArrayList<Promotion> promotions;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        this.promotions = new ArrayList<>();
    }

    public Promotion getNewPromotion(){
        Promotion newPromotion = new Promotion();
        return promotionRepository.save(newPromotion);
    }

    public Promotion addPromotion(Promotion promotion){
        return promotionRepository.save(promotion);
    }
    public Promotion getById(Long id){
        return promotionRepository.getReferenceById(id);
    }
    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }
}
