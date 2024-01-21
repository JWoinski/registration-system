package com.school.registrationsystem.service;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.model.specification.StudentSpecification;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepostiory;

    /**
     * Saves a new student based on the information provided in the StudentDto, ensuring the student index is not already in use.
     *
     * @param studentDto The StudentDto containing the details of the student to be saved.
     */
    public void saveStudent(StudentDto studentDto) {
        studentRepository.save(
                Student.builder()
                        .name(studentDto.getName())
                        .surname(studentDto.getSurname())
                        .build());
    }

    /**
     * Deletes a student by their student index, ensuring proper handling of associated courses' student lists.
     * Uses a transaction to maintain data consistency.
     *
     * @param studentId The id of the student to be deleted.
     */
    @Transactional
    public void deleteStudent(int studentId) {
        //Retrieve the existing studentOptional by their index
        Optional<Student> studentOptional = Optional.ofNullable(studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student has been not found.")));
        if (studentOptional.isPresent()) {
            //Update associated courses' studentOptional lists
            Student student = studentOptional.get();
            for (Course course : student.getCourseList()) {
                course.getStudentList().remove(student);
                courseRepostiory.save(course);
            }
        }
        studentRepository.deleteById(studentId);
    }

    /**
     * Modifies a student based on the information provided in the StudentDto.
     *
     * @param studentDto The StudentDto containing the modified student details.
     * @throws NullPointerException    Thrown if the modification form is incorrect.
     * @throws EntityNotFoundException Thrown if the student with the specified index is not found.
     */
    public void modifyStudent(StudentDto studentDto, int studentId) {
        Optional<Student> studentOptional = Optional.ofNullable(
                studentRepository.findById(studentId)
                        .orElseThrow(() -> new EntityNotFoundException("Student has been not found.")));
        studentOptional.ifPresent(student -> updateStudentValues(student, studentDto));
    }

    /**
     * Updates the values of an existing Student object with the information provided in the StudentDto.
     *
     * @param student    The existing Student object to be updated.
     * @param studentDto The StudentDto containing the modified student details.
     */
    private void updateStudentValues(Student student, StudentDto studentDto) {
        student.setName(studentDto.getName());
        student.setSurname(studentDto.getSurname());

        studentRepository.save(student);
    }

    /**
     * Retrieves a list of all students from the repository.
     *
     * @param courseId       The id of the course to get filtered students who are enrolled by course id.
     * @param withoutCourses If true, filters out students who are not enrolled in any courses.
     * @param pageable       Object used for pagination, specifying the page size, current page, etc.
     * @return A Page of Student objects representing students in the repository based on the specified filters.
     */
    public Page<Student> getAll(Integer courseId, Boolean withoutCourses, Pageable pageable) {
        Specification<Student> specification = StudentSpecification.getSpecification(courseId, withoutCourses);
        return studentRepository.findAll(specification, pageable);
    }


}