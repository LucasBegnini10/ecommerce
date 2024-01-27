package com.server.ecommerce.recoveryPassword;

import com.server.ecommerce.infra.BaseEntity;
import com.server.ecommerce.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "recovery_password")
@Getter
@Setter
public class RecoveryPassword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false, name = "expires_in")
    private LocalDateTime expiresIn;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private boolean used;

    @Column(name = "used_in")
    private LocalDateTime usedIn;

    @Column(name = "is_enabled")
    private boolean isEnabled;

}
