package com.school.registrationsystem.repository;

import com.school.registrationsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findByCourseIndex(int courseIndex);

    @Query("SELECT c FROM Course c JOIN c.studentList s WHERE s.studentIndex = :studentIndex")
    List<Course> findByStudentIndex(@Param("studentIndex") int studentIndex);

    @Query("SELECT c FROM Course c WHERE c.studentList IS EMPTY")
    List<Course> findByEmptyStudentList();

    @Query("SELECT CASE WHEN COUNT(s) < 50 THEN true ELSE false END " +
            "FROM Course c JOIN c.studentList s WHERE c.courseIndex = :courseIndex")
    boolean isCourseHaveNot50Students(@Param("courseIndex") int courseIndex);

    void deleteByCourseIndex(int courseIndex);
}