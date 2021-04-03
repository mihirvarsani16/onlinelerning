package com.onlinelearning.elearning.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.onlinelearning.elearning.Dao.AddCourseRepository;
import com.onlinelearning.elearning.Dao.StudentRepository;
import com.onlinelearning.elearning.entities.AddCourse;
import com.onlinelearning.elearning.entities.Student;
import com.onlinelearning.elearning.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class AddCourseController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddCourseRepository addCourseRepository;

    @ModelAttribute
    public void addCommondata(Model m, Principal principal) {

        String name = principal.getName();
        System.out.println("NAME : " + name);
        Student student = this.studentRepository.getUserByUserName(name);
        System.out.println("STUDENT : " + student);

        m.addAttribute("student", student);
    }

    // add course home page
    @GetMapping("/addhome")
    public String addCourseHome(Model m, Principal principal) {

        m.addAttribute("title", "your added course");
        Student student = this.studentRepository.getUserByUserName(principal.getName());
        // // m.addAttribute("addcourse", new A);
        List<AddCourse> addcourse = this.addCourseRepository.findAddCourseByStudent(student.getSid());
        m.addAttribute("addcourse", addcourse);
        return "courses/home";
    }

    // add course form controller

    @GetMapping("/addcourse")
    public String addCourse(Model m, @ModelAttribute("addcourse") AddCourse addcourse) {

        m.addAttribute("title", "add your course");
        m.addAttribute("addcourse", new AddCourse());
        return "courses/addcourseform";
    }

    /// addcourse handler

    @PostMapping("/process-addcourse")
    public String addCourseHandler(@ModelAttribute("addcourse") AddCourse addcourse,
            @RequestParam("profileimage") MultipartFile file, Model m, HttpSession session, Principal principal) {

        try {

            String name = principal.getName();
            Student student = this.studentRepository.getUserByUserName(name);

            addcourse.setImage(file.getOriginalFilename());

            File savefile = new ClassPathResource("static/image").getFile();
            Path path = Paths.get(savefile.getAbsolutePath(), File.separator, file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("file is uploaded");

            ////////
            addcourse.setStudent(student);
            this.addCourseRepository.save(addcourse);
            System.out.println(addcourse);

            // return "courses/home";

            return "redirect:/user/addhome";

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something want wrong !! Try again...", "danger"));
            return "courses/addcourseform";
        }

    }

    ////////////////////////////////
    // update page
    @GetMapping("/update/{id}")
    public String update(Model m, @PathVariable("id") Integer id) {

        m.addAttribute("title", "update form");
        AddCourse addcourse = this.addCourseRepository.findByAid(id);
        m.addAttribute("addcourse", addcourse);

        return "courses/updatecourse";
    }

    ///////////////
    // update handler

    @PostMapping("/update-process")
    public String updateform(@ModelAttribute("addcourse") AddCourse addcourse, HttpSession session, Model m,
            @RequestParam("profileimage") MultipartFile file, Principal principal) {

        try {

            Student student = this.studentRepository.getUserByUserName(principal.getName());

            AddCourse oldaddcourse = this.addCourseRepository.findByAid(addcourse.getAid());
            if (!file.isEmpty()) {
                addcourse.setImage(file.getOriginalFilename());
                // old file delete
                File oldfile = new ClassPathResource("static/image").getFile();
                File file1 = new File(oldfile, oldaddcourse.getImage());
                file1.delete();

                // add new file
                File newfile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(newfile.getAbsolutePath(), File.separator, file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            } else {
                addcourse.setImage(oldaddcourse.getImage());

            }

            addcourse.setStudent(student);

            System.out.println(addcourse);

            this.addCourseRepository.save(addcourse);
            session.setAttribute("message", new Message("your course is successfully uploaded !!", "success"));
            return "courses/home";
        } catch (Exception e) {

            e.printStackTrace();
            session.setAttribute("message", new Message("Something is wrong !!", "danger"));

            return "redirect:/user/addcourse";
        }

    }

    //////////////////
    // delet course

    @GetMapping("/delet/{id}")
    public String delete(@PathVariable("id") Integer id, Model m, Principal principal, HttpSession session) {
        this.addCourseRepository.deleteById(id);
        session.setAttribute("message", new Message("Course is deleted successfully", "success"));
        return "redirect:/user/addhome";
    }
    ////////////////////

}
