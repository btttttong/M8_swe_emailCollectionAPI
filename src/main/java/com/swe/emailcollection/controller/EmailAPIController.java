package com.swe.emailcollection.controller;

import com.swe.emailcollection.service.SubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class EmailAPIController {

    private final SubscriberService subscriberService;

    @GetMapping("/")
    public String home() {
        return "emailcollection";
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@Valid @RequestBody Map<String, String> requestBody,
                                            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp) {
        return subscriberService.subscribe(requestBody.get("email"), clientIp);
    }
}