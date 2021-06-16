package edu.uw.tjb.account;

import edu.uw.ext.framework.account.Address;

/**
 * A straight forward implementation of the Address interface as a JavaBean.
 *
 * @author Russ Moul
 */
public final class SimpleAddress implements Address {
    /** Version id. */
    private static final long serialVersionUID = -8871477242107876067L;

    /** Street component of the address */
    private String streetAddress;

    /** City component of the address */
    private String city;

    /** State component of the address */
    private String state;

    /** ZIP code component of the address */
    private String zipCode;

    /**
     * Gets the street address.
     *
     * @return the street address
     */
	@Override
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Sets the street address.
     *
     * @param streetAddress the street address
     */
	@Override
    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
	@Override
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the city
     */
	@Override
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
	@Override
    public String getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the state
     */
	@Override
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * Gets the ZIP code.
     *
     * @return the ZIP code
     */
	@Override
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the ZIP code.
     *
     * @param zip the ZIP code
     */
	@Override
    public void setZipCode(final String zip) {
        zipCode = zip;
    }

    /**
     * Concatenates the street, city, state and zip properties into the standard
     * one line postal format.
     *
     * @return the formated address string
     */
	@Override
    public String toString() {
        return String.format("%s, %s, %s %s", streetAddress, city, state, zipCode);
    }
}
