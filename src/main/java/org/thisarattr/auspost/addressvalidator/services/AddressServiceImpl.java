package org.thisarattr.auspost.addressvalidator.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thisarattr.auspost.addressvalidator.models.Address;
import org.thisarattr.auspost.addressvalidator.repositories.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> findBySuburb(String suburb) {
        return addressRepository.findBySuburbStartingWithOrderBySuburbAsc(suburb);
    }

    @Override
    public List<Address> findByPostcode(String postcode) {
        return addressRepository.findByPostcodeStartingWithOrderByPostcodeAsc(postcode);
    }

    @Override
    public void createAddress() {

    }
}
