package com.onlinelearning.elearning.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.onlinelearning.elearning.Dao.StudentRepository;
import com.onlinelearning.elearning.entities.Student;
import com.onlinelearning.elearning.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class homecontroller {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model m) {
        m.addAttribute("title", "Home - online learning");
        return "home";
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute("student") Student student, Model m) {
        m.addAttribute("title", "Signup Page");
        m.addAttribute("student", new Student());
        return "singup";
    }

    @GetMapping("/signin")
    public String signin(Model m) {

        m.addAttribute("title", "Login Page");
        return "login";
    }

    ///////////////////////////////////////
    // handler for signup page

    @PostMapping("/do_register")
    public String handalerSignup(@Valid @ModelAttribute("student") Student student, BindingResult result,
            HttpSession session, Model m,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement) {

        try {
            // for agreement
            if (!agreement) {
                System.out.println("You have not agreed the terms and condition");
                throw new Exception("You have not agreed the terms and condition");
            }

            // for error
            if (result.hasErrors()) {

                System.out.println("ERROR : " + result.toString());
                m.addAttribute("student", student);
                return "singup";

            }

            Student existuser = this.studentRepository.findByEmailIgnoreCase(student.getEmail());

            if (existuser != null) {

                session.setAttribute("message",
                        new Message("This mail is alredy exist  please enter another mail!!", "alert-danger"));
                m.addAttribute("student", student);
                return "singup";

            } else {

                student.setRole("ROLE_USER");
                student.setPassword(passwordEncoder.encode(student.getPassword()));
                this.studentRepository.save(student);
                System.out.println(student);

                m.addAttribute("student", new Student());
                session.setAttribute("message", new Message("Successfully Registered !! ", "alert-success"));

            }
            return "singup";
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("student", student);
            session.setAttribute("message", new Message("Something is wrong !!" + e.getMessage(), "alert-danger"));
            return "singup";
        }

    }

    //////////////
}
