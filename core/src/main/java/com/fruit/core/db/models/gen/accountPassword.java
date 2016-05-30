package com.fruit.core.db.models.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ACCOUNT_PASSWORD.
 */
public class accountPassword {

    private Long id;
    private String account;
    private String password;
    private Boolean lastLogin;

    public accountPassword() {
    }

    public accountPassword(Long id) {
        this.id = id;
    }

    public accountPassword(Long id, String account, String password, Boolean lastLogin) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Boolean lastLogin) {
        this.lastLogin = lastLogin;
    }

}