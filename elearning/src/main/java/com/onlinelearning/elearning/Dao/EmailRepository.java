package com.onlinelearning.elearning.Dao;

import com.onlinelearning.elearning.entities.EmailRequest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailRequest, Integer> {

    public EmailRequest findByTo(String email);

    public EmailRequest findByEid(Integer eid);

}
