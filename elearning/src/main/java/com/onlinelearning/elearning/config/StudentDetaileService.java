package com.onlinelearning.elearning.config;

import com.onlinelearning.elearning.Dao.StudentRepository;
import com.onlinelearning.elearning.entities.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class StudentDetaileService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student student = this.studentRepository.getUserByUserName(username);

        if (student == null) {

            throw new UsernameNotFoundException("could not found user !!");
        }

        StudentDetails studentDetails = new StudentDetails(student);
        return studentDetails;
    }

}
