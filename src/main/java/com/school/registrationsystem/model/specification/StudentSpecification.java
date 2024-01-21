package com.school.registrationsystem.model.specification;

import com.school.registrationsystem.model.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<Student> getSpecification(Integer courseId, Boolean withoutCourses) {
        return Specification.where(hasCourseId(courseId))
                .and(hasNoCourses(withoutCourses));
    }

    private static Specification<Student> hasCourseId(Integer courseId) {
        return (root, query, criteriaBuilder) -> {
            if (courseId != null) {
                return criteriaBuilder.equal(root.join("courseList").get("courseId"), courseId);
            }
            return null;
        };
    }

    private static Specification<Student> hasNoCourses(Boolean withoutCourses) {
        return (root, query, criteriaBuilder) -> {
            if (withoutCourses == null) {
                return null;
            }
            if (!withoutCourses) {
                return null;
            } else {
                return criteriaBuilder.isEmpty(root.get("courseList"));
            }
        };
    }
}