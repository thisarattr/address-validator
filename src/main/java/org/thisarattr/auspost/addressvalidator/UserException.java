package org.thisarattr.auspost.addressvalidator;

public class UserException extends RuntimeException {

    private int code;

    public UserException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
