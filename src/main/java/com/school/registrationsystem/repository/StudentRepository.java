package com.school.registrationsystem.repository;

import com.school.registrationsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
    @Query("SELECT CASE WHEN COUNT(c) < :studentCapacity THEN true ELSE false END " +
            "FROM Student s JOIN s.courseList c WHERE s.studentId = :studentId")
    boolean isStudentHaveNotSpecifiedCourses(@Param("studentId") int studentId, @Param("studentCapacity") int studentCapacity);

    @Query("SELECT s FROM Student s join s.courseList  c WHERE s.studentId = :studentId AND c.courseId = :courseId")
    boolean existByCourseId(@Param("studentId") int studentId, @Param("courseId") int courseId);
}