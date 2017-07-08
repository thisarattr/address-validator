package org.thisarattr.auspost.addressvalidator.controllers;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thisarattr.auspost.addressvalidator.models.Address;

@RestController
public class AddressController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "address/postcode/{postcode}", method = RequestMethod.GET)
    public ResponseEntity<List<Address>> checkStatus(@PathVariable("postcode") Long postcode, HttpServletRequest request, HttpServletResponse response) {

        Address address = new Address();
        address.setState("name");
        address.setSuburb("Clayton");
        return  ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(address));
    }
}
