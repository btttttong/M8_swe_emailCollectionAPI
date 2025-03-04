package com.swe.emailcollection.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Subscriber {
    @NotBlank
    @Email
    private String email;
    private ZonedDateTime createdAt;
    private String ipAddress;

    public Subscriber(String email, String ipAddress) {
        this.email = email;
        this.createdAt = ZonedDateTime.now(ZoneId.systemDefault());
        this.ipAddress = ipAddress;
    }

    public String getEmail() { return email; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public String getIpAddress() { return ipAddress; }
}