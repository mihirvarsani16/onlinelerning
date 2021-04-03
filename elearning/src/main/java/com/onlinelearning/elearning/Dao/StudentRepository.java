package com.onlinelearning.elearning.Dao;

import com.onlinelearning.elearning.entities.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select u from Student u where u.email = :email")
    public Student getUserByUserName(@Param("email") String email);

    public Student findByEmailIgnoreCase(String email);

    public Student findByEmail(String email);

}
