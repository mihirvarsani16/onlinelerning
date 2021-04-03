package com.onlinelearning.elearning.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.onlinelearning.elearning.Dao.AddCourseRepository;
import com.onlinelearning.elearning.Dao.CourseRepository;
import com.onlinelearning.elearning.Dao.StudentRepository;
import com.onlinelearning.elearning.entities.AddCourse;
import com.onlinelearning.elearning.entities.Course;
import com.onlinelearning.elearning.entities.Student;
import com.onlinelearning.elearning.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddCourseRepository addCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void addCommondata(Model m, Principal principal) {

        String name = principal.getName();
        System.out.println("NAME : " + name);
        Student student = this.studentRepository.getUserByUserName(name);
        System.out.println("STUDENT : " + student);

        m.addAttribute("student", student);
    }

    /////////////////
    // home controller
    @GetMapping("/home")
    public String cheking(Principal principal, Model m) {
        m.addAttribute("title", "Home");

        List<AddCourse> addCourse = this.addCourseRepository.findAll();
        m.addAttribute("addcourse", addCourse);

        return "user/home";
    }

    ////////////////////////
    // course detaile method

    @GetMapping("course-detail/{id}")
    public String courseDetaile(@PathVariable("id") Integer id, Model m, Principal principal) {

        AddCourse addcourse = this.addCourseRepository.findByAid(id);
        m.addAttribute("addcourse", addcourse);
        return "user/course-detaile";
    }

    /////////////////////
    // your course method
    @GetMapping("/your-course")
    public String yourcourse(Model m, Principal principal) {
        m.addAttribute("title", "Your Course");

        Student student = this.studentRepository.getUserByUserName(principal.getName());
        List<Course> courses = this.courseRepository.findCourseByStudent(student.getSid());

        m.addAttribute("courses", courses);
        return "user/ourcourses";
    }

    //////////
    // enroll-course handler

    @GetMapping("/enroll/{id}")
    public String enroll(@PathVariable("id") Integer id, Model m) {

        m.addAttribute("title", "Enroll-course");
        AddCourse addcourse = this.addCourseRepository.findByAid(id);
        m.addAttribute("addcourse", addcourse);
        return "user/enroll-page";
    }

    ///////////////////////////
    // enroll couse process

    @PostMapping("/enroll-course")
    public String enrollcourse(@ModelAttribute("course") Course course, @ModelAttribute AddCourse addcourse,
            HttpSession session, Principal principal) {

        try {

            Student student = this.studentRepository.getUserByUserName(principal.getName());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            course.setJtime(dtf.format(now));

            course.setStudent(student);
            student.getCourse().add(course);
            this.studentRepository.save(student);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something is wrong please try !!", "danger"));
            return "redirect:/user/enroll/" + addcourse.getAid();
        }
        return "redirect:/user/your-course";
    }
    ///////////////////////////

    /// open setting handler
    @GetMapping("/settings")
    public String openSetting() {
        return "user/settings";
    }

    ///////////////////////
    // change password handler

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, HttpSession session, Principal principal) {

        System.out.println("OLD PASSWORD " + oldPassword);
        System.out.println("NEW PASSWORD " + newPassword);

        Student currentstudent = this.studentRepository.getUserByUserName(principal.getName());

        System.out.println("current password" + currentstudent.getPassword());

        if (bCryptPasswordEncoder.matches(oldPassword, currentstudent.getPassword())) {

            // change password

            if (newPassword.matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")) {

                currentstudent.setPassword(bCryptPasswordEncoder.encode(newPassword));
                this.studentRepository.save(currentstudent);

            } else {

                session.setAttribute("message", new Message(
                        "Minimum eight characters, at least one letter, one number and one special character requried !!",
                        "danger"));
                return "redirect:/user/settings";
            }

            //

            session.setAttribute("message", new Message("Your password is successfully changed..", "success"));
        } else {
            session.setAttribute("message", new Message("Wrong old password !!", "danger"));
            return "redirect:/user/settings";
        }
        return "redirect:/user/settings";

    }
    ///////////////////////////
    /// unenroll course handlar

    @GetMapping("/delete/{cid}")
    public String delete(@PathVariable("cid") Integer cid, Principal principal, HttpSession session) {

        Course course = this.courseRepository.findById(cid).get();

        Student student = this.studentRepository.getUserByUserName(principal.getName());
        student.getCourse().remove(course);
        this.studentRepository.save(student);
        session.setAttribute("message", new Message("Course is deleted successfully", "success"));

        return "redirect:/user/your-course";
    }

}
