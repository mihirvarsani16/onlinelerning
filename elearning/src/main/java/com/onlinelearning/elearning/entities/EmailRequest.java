package com.onlinelearning.elearning.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emailrequest")
public class EmailRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eid;

    @Column(name = "to")
    private String to;
    @Column(name = "subject")
    private String subject;
    @Column(name = "message")
    private String message;

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailRequest [eid=" + eid + ", message=" + message + ", subject=" + subject + ", to=" + to + "]";
    }

}
