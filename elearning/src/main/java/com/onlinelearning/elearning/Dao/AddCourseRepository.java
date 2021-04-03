package com.onlinelearning.elearning.Dao;

import java.util.List;
import com.onlinelearning.elearning.entities.AddCourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddCourseRepository extends JpaRepository<AddCourse, Integer> {

    @Query("from AddCourse as c where c.student.sid =:studentSid")
    public List<AddCourse> findAddCourseByStudent(@Param("studentSid") Integer studentSid);

    public AddCourse findByAid(int aid);

}
