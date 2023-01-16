package io.github.reconsolidated.malinka.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class OfferController {
    @Getter
    @Setter
    private class TableOfferViewModel {
        private Offer offer;
        private String firstProductName;
        private String firstPromotionName;
        private double total;
    }

    private OfferService offerService;

    @GetMapping("/manager/offer")
    public String offerPage(Model model){
        List<Offer> allOffers = offerService.getAllOffers();
        List<TableOfferViewModel> tableOffers = new ArrayList<>();
        for(Offer offer: allOffers){
            TableOfferViewModel newTableOffer = new TableOfferViewModel();
            newTableOffer.setOffer(offer);
            newTableOffer.setFirstProductName(offerService.getProductsByOffer(offer).get(0).getName());
            newTableOffer.setFirstPromotionName(offerService.getPromotionsByOffer(offer).get(0).getName());
            newTableOffer.setTotal(offerService.getTotal(offer));
            tableOffers.add(newTableOffer);
        }
        model.addAttribute("tableOffers", tableOffers);
        return "manager_offer";
    }

    @GetMapping("/manager/offer/add")
    public String addOfferPage(
            @RequestParam(name="offer") Offer offer,
            Model model){
        return "manager_offer_add";
    }

    @GetMapping("/manager/offer/edit")
    public String editOfferPage(
            @RequestParam(name="offer") Offer offer,
            Model model){
        return "manager_offer_edit";
    }
}
