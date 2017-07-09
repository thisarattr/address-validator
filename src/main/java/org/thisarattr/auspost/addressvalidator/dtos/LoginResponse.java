package org.thisarattr.auspost.addressvalidator.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginResponse extends Response {

    private String jwtToken;

    public LoginResponse() {
        super();
    }

    public LoginResponse(boolean isSuccess, Integer code, String msg) {
        super(isSuccess, code, msg);
    }

    public LoginResponse(boolean isSuccess, String jwtToken) {
        super(isSuccess);
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
