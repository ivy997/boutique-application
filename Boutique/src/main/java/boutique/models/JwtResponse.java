package boutique.models;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String name;
    private String surname;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String name, String surname, String email, List<String> roles) {
        this.token = accessToken;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
