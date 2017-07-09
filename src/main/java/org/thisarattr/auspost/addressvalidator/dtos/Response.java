package org.thisarattr.auspost.addressvalidator.dtos;

public class Response {

    private boolean isSuccess;
    private Integer status;
    private String message;

    public Response() {
    }

    public Response(boolean isSuccess, Integer status, String message) {
        this.isSuccess = isSuccess;
        this.status = status;
        this.message = message;
    }

    public Response(boolean isSuccess) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            this.status = 200;
            this.message = "Successfully completed.";
        } else {
            this.status = 400;
            this.message = "Failed to completed the request.";
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
