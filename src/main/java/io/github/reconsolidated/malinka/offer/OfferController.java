package io.github.reconsolidated.malinka.offer;

import io.github.reconsolidated.malinka.mainPage.Product;
import io.github.reconsolidated.malinka.mainPage.ProductsService;
import io.github.reconsolidated.malinka.promotion.Promotion;
import io.github.reconsolidated.malinka.promotion.PromotionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Getter
    @Setter
    private class TempOffer {
        private Offer offer;
        private List<Product> products;
        private List<Promotion> promotions;

        public void removeProductById(Long id) {
            for (int i=0; i<products.size(); i++) {
                if(products.get(i).getGeneratedId().equals(id)) {
                    products.remove(i);
                    return;
                }
            }
        }
        public void removePromotionById(Long id) {
            for (int i=0; i<promotions.size(); i++) {
                if(promotions.get(i).getId().equals(id)) {
                    promotions.remove(i);
                    return;
                }
            }
        }

        public List<Promotion> findCandidatePromotions() {
            List<Promotion> candidates = new ArrayList<>();
            for(Product product: products) {
                candidates.addAll(promotionService.getByProductId(product.getGeneratedId()));
            }
            return candidates;
        }
    }
    private static TempOffer tempOffer;

    private OfferService offerService;

    private ProductsService productsService;

    private PromotionService promotionService;

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
    public String addOfferPage(Model model){
        OfferAddEditViewModel viewModel = new OfferAddEditViewModel();
        tempOffer = new TempOffer();
        tempOffer.setOffer(offerService.getNewOffer());
        tempOffer.setProducts(new ArrayList<>());
        tempOffer.setPromotions(new ArrayList<>());
        viewModel.setOfferId(tempOffer.getOffer().getId());
        List<Product> allProducts = productsService.getAll();
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("allProducts", allProducts);
        return "manager_offer_add";
    }

    @GetMapping("/manager/offer/add/addProduct")
    public String addOfferProductGetRedirect(){
        return "redirect:/manager/offer/add";
    }
    @PostMapping("/manager/offer/add/addProduct")
    public String addOfferProduct(Model model, OfferAddEditViewModel viewModel) {
        tempOffer.getProducts().add(productsService.getById(viewModel.getProductId()));
        List<Product> allProducts = productsService.getAll();
        List<Promotion> candidatePromotions = tempOffer.findCandidatePromotions();
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("addedProducts", tempOffer.getProducts());
        model.addAttribute("addedPromotions", tempOffer.getPromotions());
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("candidatePromotions", candidatePromotions);
        return "manager_offer_add";
    }

    @PostMapping("/manager/offer/add/removeProduct")
    public String removeOfferProduct(Model model ,OfferAddEditViewModel viewModel) {
        tempOffer.removeProductById(viewModel.getProductId());
        List<Product> allProducts = productsService.getAll();
        List<Promotion> candidatePromotions = tempOffer.findCandidatePromotions();
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("addedProducts", tempOffer.getProducts());
        model.addAttribute("addedPromotions", tempOffer.getPromotions());
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("candidatePromotions", candidatePromotions);
        return "manager_offer_add";
    }

    @PostMapping("/manager/offer/add/addPromotion")
    public String addOfferPromotion(Model model, OfferAddEditViewModel viewModel) {
        Long promoId = viewModel.getPromotionId();
        if (promoId != null) {
            tempOffer.getPromotions().add(promotionService.getById(promoId));
        }
        List<Product> allProducts = productsService.getAll();
        List<Promotion> candidatePromotions = tempOffer.findCandidatePromotions();
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("addedProducts", tempOffer.getProducts());
        model.addAttribute("addedPromotions", tempOffer.getPromotions());
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("candidatePromotions", candidatePromotions);
        return "manager_offer_add";
    }

    @PostMapping("/manager/offer/add/removePromotion")
    public String removeOfferPromotion(Model model ,OfferAddEditViewModel viewModel) {
        tempOffer.removePromotionById(viewModel.getPromotionId());
        List<Product> allProducts = productsService.getAll();
        List<Promotion> candidatePromotions = tempOffer.findCandidatePromotions();
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("addedProducts", tempOffer.getProducts());
        model.addAttribute("addedPromotions", tempOffer.getPromotions());
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("candidatePromotions", candidatePromotions);
        return "manager_offer_add";
    }

    @GetMapping("/manager/offer/edit")
    public String editOfferPage(
            @RequestParam(name="offer") Offer offer,
            Model model){
        return "manager_offer_edit";
    }
}
