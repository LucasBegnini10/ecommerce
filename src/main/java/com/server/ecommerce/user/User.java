package com.server.ecommerce.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.ecommerce.infra.BaseEntity;
import com.server.ecommerce.recoveryPassword.RecoveryPassword;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User extends BaseEntity implements UserDetails{


    @Id
    private String id;

    private String name;

    private String phone;

    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "role")
    private RoleType role;

    @Column(name = "account_expired")
    private boolean accountExpired;

    @Column(name = "account_locked")
    private boolean accountLocked;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired;

    @Column(name = "isEnabled")
    private boolean isEnabled;

    @JsonIgnore
    @OneToMany(mappedBy="user")
    private Set<RecoveryPassword> tokensRecovery;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.getRole())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
