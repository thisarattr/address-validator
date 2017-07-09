package org.thisarattr.auspost.addressvalidator.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thisarattr.auspost.addressvalidator.UserException;
import org.thisarattr.auspost.addressvalidator.dtos.AddressObj;
import org.thisarattr.auspost.addressvalidator.dtos.AddressResponse;
import org.thisarattr.auspost.addressvalidator.dtos.Response;
import org.thisarattr.auspost.addressvalidator.models.Address;
import org.thisarattr.auspost.addressvalidator.services.AddressService;

@RestController
public class AddressController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "address/postcode/{postcode}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressResponse> findByPostcode(@PathVariable("postcode") String postcode) {
        List<AddressObj> addressObjList = addressService.findByPostcode(postcode)
                .stream().map(Address::getJsonAddress).collect(Collectors.toList());
        return  ResponseEntity.status(HttpStatus.OK).body(new AddressResponse(true, addressObjList));
    }

    @RequestMapping(value = "address/suburb/{suburb}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressResponse> findBySuburb(@PathVariable("suburb") String suburb) {
        List<AddressObj> addressObjList = addressService.findBySuburb(suburb)
                .stream().map(Address::getJsonAddress).collect(Collectors.toList());
        return  ResponseEntity.status(HttpStatus.OK).body(new AddressResponse(true, addressObjList));
    }

    @RequestMapping(value = "admin/address", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Response> saveAddress(@RequestBody AddressObj addressObj) {
        try {
            addressService.saveAddress(addressObj.getAddress());
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(true, e.getCode(), e.getMessage()));
        }
        return  ResponseEntity.ok(new Response(true));
    }
}
