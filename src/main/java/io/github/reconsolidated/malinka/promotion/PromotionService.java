package io.github.reconsolidated.malinka.promotion;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
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
