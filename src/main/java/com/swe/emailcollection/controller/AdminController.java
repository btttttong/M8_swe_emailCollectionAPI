package com.swe.emailcollection.controller;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final SubscriberRepository subscriberRepository;

    public AdminController(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<Subscriber> subscribers = subscriberRepository.findAll();  
        model.addAttribute("subscribers", subscribers);
        return "admin";  
    }
}