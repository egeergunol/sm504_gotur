package com.mese.gotur.utils;

import com.mese.gotur.model.CartInfo;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    // Products in the cart, stored in Session.
    public static CartInfo getCartInSession(HttpServletRequest request) {
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
        if (cartInfo == null) {
            cartInfo = new CartInfo();
            request.getSession().setAttribute("myCart", cartInfo);
        }
        return cartInfo;
    }

}
