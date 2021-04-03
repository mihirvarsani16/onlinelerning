package com.onlinelearning.elearning.Dao;

import java.util.List;

import com.onlinelearning.elearning.entities.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("from Course as c where c.student.sid =:courseSid")
    public List<Course> findCourseByStudent(@Param("courseSid") Integer courseSid);

}
