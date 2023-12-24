package com.school.registrationsystem.repository;

import com.school.registrationsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    void deleteByStudentIndex(int studentIndex);

    Student findByStudentIndex(int studentIndex);

    @Query("SELECT s FROM Student s JOIN s.courseList c WHERE c.courseIndex = :courseIndex")
    List<Student> findByCourseIndex(@Param("courseIndex") int courseIndex);

    @Query("SELECT s FROM Student s WHERE s.courseList IS EMPTY")
    List<Student> findByEmptyCourseList();

    @Query("SELECT CASE WHEN COUNT(c) < 5 THEN true ELSE false END " +
            "FROM Student s JOIN s.courseList c WHERE s.studentIndex = :studentIndex")
    boolean isStudentHaveNot5Courses(@Param("studentIndex") int studentIndex);
}