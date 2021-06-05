package edu.uw.tjb.account;

import edu.uw.ext.framework.account.Address;

/**
 * Class for creating an address.
 *
 * @author Tim
 */
public class SimpleAddress implements Address {

    private String streetAddress = "";
    private String city = "";
    private String state = "";
    private String zipCode = "";

/*    public SimpleAddress() {
    }

    public SimpleAddress(String streetAddress, String city, String state, String zipCode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }*/

    @Override
    public String getStreetAddress() {
        return streetAddress;
    }

    @Override
    public void setStreetAddress(String s) {
        streetAddress = s;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String s) {
        city = s;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String s) {
        state = s;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public void setZipCode(String s) {
        zipCode = s;
    }
}
