package com.mese.gotur.validator;

import com.mese.gotur.dao.AccountDAO;
import com.mese.gotur.entity.Account;
import com.mese.gotur.form.AccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountFormValidator implements Validator {

    @Autowired
    private AccountDAO accountDAO;

    // This validator only checks for the CustomerForm.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AccountForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountForm accountForm = (AccountForm) target;

        // Check the fields of ProductForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.accountForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.accountForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rePassword", "NotEmpty.accountForm.rePassword");

        if(accountForm.getPassword().equals(accountForm.getRePassword())) {
            errors.rejectValue("rePassword", "Match.accountForm.password");
        }

        String userName = accountForm.getUserName();
        if (userName != null && userName.length() > 0) {
            Account account = accountDAO.findAccount(userName);
            if (account != null) {
                errors.rejectValue("userName", "Duplicate.accountForm.userName");
            }
        }
    }
}
