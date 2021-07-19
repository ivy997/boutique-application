package boutique.models;

import com.sun.istack.NotNull;

import java.util.Set;

public class RegisterRequest {
    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    private String address;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    private Set<String> role;

    public RegisterRequest(String email, String name, String surname, String password, String confirmPassword) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public RegisterRequest(String email, String name, String surname, String address, String password, String confirmPassword) {
        this(email, name, surname, password, confirmPassword);

        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
