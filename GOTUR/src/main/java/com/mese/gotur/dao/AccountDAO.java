package com.mese.gotur.dao;

import com.mese.gotur.entity.Account;
import com.mese.gotur.form.AccountForm;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AccountDAO {
 
    @Autowired
    private SessionFactory sessionFactory;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
 
    public Account findAccount(String userName) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(Account.class, userName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(AccountForm accountForm) {

        Session session = this.sessionFactory.getCurrentSession();
        Account account = this.findAccount(accountForm.getUserName());

        if (account == null) {
            account = new Account();
            account.setUserName(accountForm.getUserName());
            account.setEncryptedPassword(encoder.encode(accountForm.getPassword()));
            account.setActive(true);
            account.setUserRole(accountForm.getUserRole());
            session.persist(account);
        }

        // If error in DB, Exceptions will be thrown out immediately
        session.flush();
    }
}
