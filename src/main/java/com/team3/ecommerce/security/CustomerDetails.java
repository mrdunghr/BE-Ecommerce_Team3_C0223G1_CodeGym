package com.team3.ecommerce.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team3.ecommerce.entity.AuthenticationType;
import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
// lấy thông tin từ userDetails map với bảng cơ sở dữ liệu
public class CustomerDetails implements UserDetails {
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String state;
    private Country country;
    private String phoneNumber;
    private boolean enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        return  role;
    }

    public static CustomerDetails mapUserToUserDetail(Customer customer) {
        // Trả về đối tượng CustomerDetails
        return new CustomerDetails(
                customer.getEmail(),
                customer.getPassword(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getState(),
                customer.getCountry(),
                customer.getPhoneNumber(),
                customer.isEnabled()
        );
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
