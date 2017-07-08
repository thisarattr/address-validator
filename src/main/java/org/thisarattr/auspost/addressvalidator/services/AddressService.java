package org.thisarattr.auspost.addressvalidator.services;

import java.util.List;

import org.thisarattr.auspost.addressvalidator.models.Address;

public interface AddressService {

    /**
     * Returns list of address objects order by suburb, based on the suburb string provided. Provided suburb string
     * will be matched against the begining of suburd database field.
     *
     * @param suburb String value
     * @return List of Address objects
     */
    List<Address> findBySuburb(String suburb);

    /**
     * Returns list of address objects order by postcode, based on the postcode string provided. Provided postcode
     * string will be matched against the begining of postcode database field.
     * @param postcode String value
     * @return List of Address objects
     */
    List<Address> findByPostcode(String postcode);

    /**
     * Creates a adddress with privided values.
     */
    void createAddress();
}
