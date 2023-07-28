package com.team3.ecommerce.payload.resp;

public class MessageResp {
    private String message;

    public MessageResp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
