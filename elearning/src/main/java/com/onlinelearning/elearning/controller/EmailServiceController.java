package com.onlinelearning.elearning.controller;

import java.security.Principal;
import java.util.Random;

import javax.servlet.http.HttpSession;

import com.onlinelearning.elearning.Dao.EmailRepository;
import com.onlinelearning.elearning.Dao.StudentRepository;
import com.onlinelearning.elearning.entities.EmailRequest;
import com.onlinelearning.elearning.entities.Student;
import com.onlinelearning.elearning.helper.Message;
import com.onlinelearning.elearning.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailServiceController {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/forget")
    public String forget(Model m) {
        m.addAttribute("title", "forget password");

        m.addAttribute("emailrequest", new EmailRequest());

        return "forgetpassword";
    }

    // code method

    @GetMapping("/code/{id}")
    public String code(Model m, @PathVariable("id") Integer id) {
        m.addAttribute("title", "Code");

        EmailRequest emailrequest = this.emailRepository.findByEid(id);
        m.addAttribute("emailrequest", emailrequest);
        return "code";
    }

    //////

    ////////////////////
    // set password handlar

    @GetMapping("/setpassword")
    public String setpassword(Model m) {
        m.addAttribute("title", "Set-Password");

        // Student student = this.studentRepository.findByEmail(id);

        m.addAttribute("student", new Student());
        return "setpassword";
    }

    ///////////////////
    // foget handlar

    @PostMapping("/forget-password")
    public String forgetHandalr(@ModelAttribute("emailrequest") EmailRequest emailrequest, HttpSession session) {

        Student student = this.studentRepository.findByEmail(emailrequest.getTo());
        EmailRequest emailRequest1 = this.emailRepository.findByTo(emailrequest.getTo());

        Random r = new Random();
        int m = r.nextInt(100000);

        String message = "" + "<div style='border:1px soild #e2e2e2; padding:20px'>" + "<h1>" + "OTP is " + "<b>" + m
                + "</n>" + "</h1>" + "</div>";
        if (student != null) {

            if (emailRequest1 == null) {

                emailrequest.setSubject("Foget Password");
                emailrequest.setMessage(message);

                System.out.println("code : " + emailrequest.getMessage());

                boolean result = this.emailService.sendEmail(emailrequest.getSubject(), emailrequest.getMessage(),
                        emailrequest.getTo());

                if (result) {

                    session.setAttribute("message", new Message("Email is sent successfully...", "alert-success"));

                    this.emailRepository.save(emailrequest);

                    return "redirect:/code/" + emailrequest.getEid();
                } else {
                    session.setAttribute("message", new Message("Email is not sent..", "alert-danger"));

                    return "redirect:/forget";
                }

            } else {

                emailRequest1.setSubject("Foget Password");
                emailRequest1.setMessage(message);
                System.out.println("code : " + emailRequest1.getMessage());

                boolean result = this.emailService.sendEmail(emailRequest1.getSubject(), emailRequest1.getMessage(),
                        emailRequest1.getTo());

                if (result) {

                    session.setAttribute("message", new Message("Email is sent successfully...", "alert-success"));

                    this.emailRepository.save(emailRequest1);
                    return "redirect:/code/" + emailRequest1.getEid();
                } else {
                    session.setAttribute("message", new Message("Email is not sent..", "alert-danger"));

                    return "redirect:/forget";
                }

            }

            //////////////////////////

        } else {
            session.setAttribute("message",
                    new Message("This mail is not exist please enter valid mail !!", "alert-danger"));
            return "redirect:/forget";

        }

    }

    ////////////////
    // forget code handlar

    @PostMapping("/forget-code")
    public String MatchCode(@ModelAttribute EmailRequest emailrequest, HttpSession session) {

        EmailRequest emailRequest = this.emailRepository.findByEid(emailrequest.getEid());

        // Student student = this.studentRepository.findByEmail(emailrequest.getTo());

        // String message = "Your Online learning forget password code : " +
        // emailrequest.getMessage();

        String message = "" + "<div style='border:1px soild #e2e2e2; padding:20px'>" + "<h1>" + "OTP is " + "<b>"
                + emailrequest.getMessage() + "</n>" + "</h1>" + "</div>";

        System.out.println("MESSAGE : " + message);

        if (emailRequest.getMessage().equals(message)) {

            session.setAttribute("message", new Message("code is match create new password !!", "alert-success"));

            session.setAttribute("email", emailRequest.getTo());
            session.setAttribute("id", emailrequest.getEid());
            return "setpassword";

        } else {

            session.setAttribute("message",
                    new Message("code is note match please enter right code !!", "alert-danger"));

            return "redirect:/code/" + emailRequest.getEid();

        }

    }

    ///////////////////
    // set passsword handler

    @PostMapping("/set-password")
    public String setpassword(@ModelAttribute("student") Student student, HttpSession session, Principal principal) {

        String email = (String) session.getAttribute("email");
        Student student1 = this.studentRepository.findByEmail(email);

        if (student.getPassword().matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")) {

            System.out.println("email : " + student1.getEmail());
            // student1.setPassword(student.getPassword());
            student1.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
            this.studentRepository.save(student1);

            session.setAttribute("message", new Message("Your password is successfully change !!", "alert-success"));

            return "login";
        } else {

            session.setAttribute("message", new Message(
                    "Minimum eight characters, at least one letter, one number and one special character requried !!",
                    "alert-danger"));

            return "redirect:/setpassword";
        }

    }

    ////////////////////
}
