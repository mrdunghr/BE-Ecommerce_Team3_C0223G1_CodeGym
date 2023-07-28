package com.team3.ecommerce.payload.resp;

public class JwtResp {
    private String type = "Bearer";
    private String token;
    private String email;

    public JwtResp( String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
