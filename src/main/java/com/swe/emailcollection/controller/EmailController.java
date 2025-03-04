package com.swe.emailcollection.controller;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmailController {
    private final SubscriberRepository repository;

    public EmailController(SubscriberRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("subscriber", new Subscriber("", ""));
        return "emailcollection";
    }

    @PostMapping("/submit")
    public String submitEmail(@Valid @ModelAttribute("subscriber") Subscriber subscriber,
                              BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "emailcollection";
        }
        String userIp = request.getRemoteAddr();
        subscriber = new Subscriber(subscriber.getEmail(), userIp);
        System.out.println("Received email: " + subscriber.getEmail() + " from IP: " + subscriber.getIpAddress() + " at " + subscriber.getCreatedAt());

        if (!repository.save(subscriber)) {
            model.addAttribute("error", "This email is already registered. Please use a different email.");
            return "emailcollection";
        }

        model.addAttribute("message", "Email successfully submitted!");
        model.addAttribute("createdAt", subscriber.getCreatedAt()); 
        return "emailcollection";
    }
}