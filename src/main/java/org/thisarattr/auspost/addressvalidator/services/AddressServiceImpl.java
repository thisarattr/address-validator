package org.thisarattr.auspost.addressvalidator.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.thisarattr.auspost.addressvalidator.UserException;
import org.thisarattr.auspost.addressvalidator.models.Address;
import org.thisarattr.auspost.addressvalidator.repositories.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> findBySuburb(String suburb) {
        return addressRepository.findBySuburbStartingWithIgnoreCaseOrderBySuburbAsc(suburb);
    }

    @Override
    public List<Address> findByPostcode(String postcode) {
        return addressRepository.findByPostcodeStartingWithIgnoreCaseOrderByPostcodeAsc(postcode);
    }

    @Override
    public void saveAddress(Address address) {
        if (address.getPostcode() == null || address.getSuburb() == null || address.getState() == null) {
            throw new UserException(400, "Postcode, suburb and state are mandatory fields");
        }
        address.setCreatedOn(LocalDateTime.now());
        addressRepository.save(address);
    }
}
