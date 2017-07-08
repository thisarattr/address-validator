package org.thisarattr.auspost.addressvalidator.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thisarattr.auspost.addressvalidator.models.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findBySuburbStartingWithOrderBySuburbAsc(String suburb);
    List<Address> findByPostcodeStartingWithOrderByPostcodeAsc(String postcode);
}
