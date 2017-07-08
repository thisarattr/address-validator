package org.thisarattr.auspost.addressvalidator.services;

import java.util.List;

import org.thisarattr.auspost.addressvalidator.models.Address;

public interface AddressService {
    List<Address> findBySuburb(String suburb);
    List<Address> findByPostcode(String postcode);
    void createAddress();
}
