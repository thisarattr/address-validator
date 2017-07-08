package org.thisarattr.auspost.addressvalidator.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thisarattr.auspost.addressvalidator.models.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findBySuburbStartingWithIgnoreCaseOrderBySuburbAsc(String suburb);

    List<Address> findByPostcodeStartingWithIgnoreCaseOrderByPostcodeAsc(String postcode);
}
