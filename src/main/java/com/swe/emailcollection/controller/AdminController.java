package com.swe.emailcollection.controller;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class AdminController {

    private final SubscriberRepository subscriberRepository;

    public AdminController(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model, 
                            @RequestParam(defaultValue = "0") int page, 
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Subscriber> subscriberPage = subscriberRepository.findAll(pageable);

        model.addAttribute("subscribers", subscriberPage.getContent());  // Current page data
        model.addAttribute("currentPage", subscriberPage.getNumber());
        model.addAttribute("totalPages", subscriberPage.getTotalPages());

        return "admin";  
    }
}