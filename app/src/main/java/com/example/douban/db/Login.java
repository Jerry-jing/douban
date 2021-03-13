package com.example.douban.db;

import org.litepal.crud.DataSupport;

public class Login extends DataSupport {
    private String account;
    private String pwd;
    private byte[] headshot;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public byte[] getHeadshot() {
        return headshot;
    }

    public void setHeadshot(byte[] headshot) {
        this.headshot = headshot;
    }
}
