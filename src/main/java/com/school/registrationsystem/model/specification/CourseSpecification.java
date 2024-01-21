package com.school.registrationsystem.model.specification;

import com.school.registrationsystem.model.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {
    public static Specification<Course> getSpecification(Integer studentId, Boolean withoutStudents) {
        return Specification.where(hasStudentId(studentId))
                .and(hasStudents(withoutStudents));
    }

    private static Specification<Course> hasStudentId(Integer studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId != null) {
                return criteriaBuilder.equal(root.join("studentList").get("studentId"), studentId);
            }
            return null;
        };
    }

    private static Specification<Course> hasStudents(Boolean withoutStudents) {
        return (root, query, criteriaBuilder) -> {
            if (withoutStudents == null) {
                return null;
            }
            if (!withoutStudents) {
                return null;
            } else {
                return criteriaBuilder.isEmpty(root.get("studentList"));
            }
        };
    }
}