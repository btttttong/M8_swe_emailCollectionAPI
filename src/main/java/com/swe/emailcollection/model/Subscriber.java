package com.swe.emailcollection.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Auto-increment ID
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private String ipAddress;

    public Subscriber() {} // Required for JPA

    public Subscriber(String email, String ipAddress) {
        this.email = email;
        this.createdAt = ZonedDateTime.now();
        this.ipAddress = ipAddress;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public String getIpAddress() { return ipAddress; }
}