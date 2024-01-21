package com.school.registrationsystem.repository;

import com.school.registrationsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {
    @Query("SELECT CASE WHEN COUNT(s) < :courseCapacity" +
            " THEN true ELSE false END " +
            "FROM Course c JOIN c.studentList s WHERE c.courseId = :courseId")
    boolean isCourseHaveNotSpecifiedStudents(@Param("courseId") int courseId, @Param("courseCapacity") int courseCapacity);
}