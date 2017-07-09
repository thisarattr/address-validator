package org.thisarattr.auspost.addressvalidator.dtos;


import java.util.List;

public class AddressResponse extends Response {

    private List<AddressObj> addresses;

    public AddressResponse() {
        super();
    }

    public AddressResponse(boolean isSuccess, Integer code, String msg) {
        super(isSuccess, code, msg);
    }

    public AddressResponse(boolean isSuccess, List<AddressObj> addresses) {
        super(isSuccess);
        this.addresses = addresses;
    }

    public List<AddressObj> getAddresses() {
        return addresses;
    }
}

