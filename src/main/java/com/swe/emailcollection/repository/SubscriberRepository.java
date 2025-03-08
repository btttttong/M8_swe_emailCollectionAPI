package com.swe.emailcollection.repository;

import com.swe.emailcollection.model.Subscriber;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// @Repository
// public class SubscriberRepository {
//     private final Map<String, Subscriber> subscribers = new HashMap<>();

//     public boolean save(Subscriber subscriber) {
//         if (subscribers.containsKey(subscriber.getEmail())) {
//             return false; // check if Email already exists in the repository
//         }
//         subscribers.put(subscriber.getEmail(), subscriber);
//         return true;
//     }

//     public Collection<Subscriber> findAll() {
//         return subscribers.values();
//     }

//     public Subscriber findByEmail(String email) {
//         return subscribers.get(email);
//     }
// }
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Optional<Subscriber> findByEmail(String email);
    Page<Subscriber> findAll(Pageable pageable);
}