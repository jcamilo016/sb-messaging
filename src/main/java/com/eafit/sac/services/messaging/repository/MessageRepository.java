package com.eafit.sac.services.messaging.repository;

import com.eafit.sac.services.messaging.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByOrderBySentDateDesc();
    List<Message> findBySendTo(String sentTo);
}
