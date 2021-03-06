package com.mese.gotur.controller;

import com.mese.gotur.dao.AccountDAO;
import com.mese.gotur.dao.OrderDAO;
import com.mese.gotur.dao.ProductDAO;
import com.mese.gotur.entity.Product;
import com.mese.gotur.form.AccountForm;
import com.mese.gotur.form.ProductForm;
import com.mese.gotur.model.OrderDetailInfo;
import com.mese.gotur.model.OrderInfo;
import com.mese.gotur.pagination.PaginationResult;
import com.mese.gotur.validator.AccountFormValidator;
import com.mese.gotur.validator.ProductFormValidator;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Transactional
public class AdminController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ProductFormValidator productFormValidator;

    @Autowired
    private AccountFormValidator accountFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == ProductForm.class) {
            dataBinder.setValidator(productFormValidator);
        }
        else if(target.getClass() == AccountForm.class) {
            dataBinder.setValidator(accountFormValidator);
        }

    }

    // GET: Show Login Page
    @RequestMapping(value = {"/admin/login"}, method = RequestMethod.GET)
    public String login(Model model) {

        return "login";
    }

    // GET: Show Register Page
    @RequestMapping(value = {"/admin/register"}, method = RequestMethod.GET)
    public String retrieveAccountForm(Model model) {
        model.addAttribute("accountForm", new AccountForm());
        return "register";
    }

    // POST: Save Account From Register Page
    @RequestMapping(value = {"/admin/register"}, method = RequestMethod.POST)
    public String registerAccount(Model model, //
                              @ModelAttribute("accountForm") @Validated AccountForm accountForm, //
                              BindingResult result, //
                              final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }
        try {
            accountDAO.save(accountForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            // Show register form.
            return "register";
        }

        return "redirect:/admin/login";
    }

    @RequestMapping(value = {"/admin/orderList"}, method = RequestMethod.GET)
    public String orderList(Model model, //
                            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;

        PaginationResult<OrderInfo> paginationResult //
                = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }

    // GET: Show product.
    @RequestMapping(value = {"/admin/product"}, method = RequestMethod.GET)
    public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ProductForm productForm = null;

        if (code != null && code.length() > 0) {
            Product product = productDAO.findProduct(code);
            if (product != null) {
                productForm = new ProductForm(product);
            }
        }
        if (productForm == null) {
            productForm = new ProductForm();
            productForm.setNewProduct(true);
        }
        model.addAttribute("productForm", productForm);
        return "product";
    }

    // POST: Save product
    @RequestMapping(value = {"/admin/product"}, method = RequestMethod.POST)
    public String productSave(Model model, //
                              @ModelAttribute("productForm") @Validated ProductForm productForm, //
                              BindingResult result, //
                              final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "product";
        }
        try {
            productDAO.save(productForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            // Show product form.
            return "product";
        }

        return "redirect:/productList";
    }

    @RequestMapping({"/admin/productRemove"})
    public String productDelete(Model model, //
                                @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            try {
                productDAO.delete(code);
            } catch (Exception e) {
                Throwable rootCause = ExceptionUtils.getRootCause(e);
                String message = rootCause.getMessage();
                model.addAttribute("errorMessage", message);
                // Show product form.
                return "productList";
            }
        }
        return "redirect:/productList";
    }

    @RequestMapping(value = {"/admin/order"}, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);

        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }

    @RequestMapping(value = {"/admin/fulfillOrder"}, method = RequestMethod.GET)
    public String fulfillOrder(Model model, @RequestParam("orderId") String orderId) {
        return handleOrderStatusChange(orderId, 1);
    }

    @RequestMapping(value = {"/admin/cancelOrder"}, method = RequestMethod.GET)
    public String cancelOrder(Model model, @RequestParam("orderId") String orderId) {
        return handleOrderStatusChange(orderId, 2);
    }

    private String handleOrderStatusChange(String orderId, int orderStatusToBeUpdated) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        this.orderDAO.fulfillOrder(orderId, orderStatusToBeUpdated);

        return "redirect:/admin/orderList";
    }

}
