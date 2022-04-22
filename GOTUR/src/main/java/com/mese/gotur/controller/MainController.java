package com.mese.gotur.controller;

import com.mese.gotur.dao.ProductDAO;
import com.mese.gotur.entity.Product;
import com.mese.gotur.model.CartInfo;
import com.mese.gotur.model.ProductInfo;
import com.mese.gotur.pagination.PaginationResult;
import com.mese.gotur.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Transactional
public class MainController {

    @Autowired
    private ProductDAO productDAO;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        // Case update quantity in cart
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {

        }

    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "redirect:/productList";
    }

    // Product List
    @RequestMapping({"/productList"})
    public String listProductHandler(Model model,
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 8;
        final int maxNavigationPage = 10;

        PaginationResult<ProductInfo> result = productDAO.queryProducts(page, //
                maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping({"/buyProduct"})
    public String listProductHandler(HttpServletRequest request, Model model, //
                                     @RequestParam(value = "code", defaultValue = "") String code,
                                     @RequestParam(value = "quantity", defaultValue = "") int quantity) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.addProduct(productInfo, quantity);
        }
        return "redirect:/shoppingCart";
    }

    @RequestMapping({"/shoppingCartRemoveProduct"})
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            cartInfo.removeProduct(productInfo);
        }
        return "redirect:/shoppingCart";
        }


        @RequestMapping({"/updateQuantity"})
        public String updateQuantityHandler(HttpServletRequest request, Model model, //
                @RequestParam(value = "code", defaultValue = "") String code,
                @RequestParam(value = "operation") String operation) {
            Product product = null;
            if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            cartInfo.updateQuantity(code, operation);
        }
        return "redirect:/shoppingCart";
    }

    // POST: Update quantity for product in cart
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @ModelAttribute("cartForm") CartInfo cartForm) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        return "redirect:/shoppingCart";
    }

    // GET: Show cart.
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);
        CartInfo cartInfo = Utils.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        model.addAttribute("myCart", cartInfo);
        return "shoppingCart";
    }

    @RequestMapping(value = {"/productImage"}, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = this.productDAO.findProduct(code);
        }
        if (product != null && product.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }

}
