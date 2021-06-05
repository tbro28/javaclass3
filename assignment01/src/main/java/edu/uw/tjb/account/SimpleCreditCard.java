package edu.uw.tjb.account;

import edu.uw.ext.framework.account.CreditCard;

/**
 * Class for creating a credit card.
 *
 * @author Tim
 */
public class SimpleCreditCard implements CreditCard {

    private String issuer = "";
    private String type = "";
    private String holder = "";
    private String accountNumber = "";
    private String expirationDate = "";


/*    public SimpleCreditCard() {
    }

    public SimpleCreditCard(String issuer, String type, String holder, String accountNumber) {
        this.issuer = issuer;
        this.type = type;
        this.holder = holder;
        this.accountNumber = accountNumber;
    }*/


    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public void setIssuer(String s) {
        issuer = s;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String s) {
        type = s;
    }

    @Override
    public String getHolder() {
        return holder;
    }

    @Override
    public void setHolder(String s) {
        holder = s;
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void setAccountNumber(String s) {
        accountNumber = s;
    }

    @Override
    public String getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(String s) {
        expirationDate = s;
    }
}
