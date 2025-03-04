package com.swe.emailcollection.repository;

import com.swe.emailcollection.model.Subscriber;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class SubscriberRepository {
    private final Map<String, Subscriber> subscribers = new HashMap<>();

    public boolean save(Subscriber subscriber) {
        if (subscribers.containsKey(subscriber.getEmail())) {
            return false; // check if Email already exists in the repository
        }
        subscribers.put(subscriber.getEmail(), subscriber);
        return true;
    }

    public Collection<Subscriber> findAll() {
        return subscribers.values();
    }

    public Subscriber findByEmail(String email) {
        return subscribers.get(email);
    }
}