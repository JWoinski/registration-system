package com.school.registrationsystem.service;

import com.school.registrationsystem.exception.CapacityException;
import com.school.registrationsystem.exception.DuplicateEnrollmentException;
import com.school.registrationsystem.exception.IndexOccupiedException;
import com.school.registrationsystem.exception.TimeOutCourseRegisterException;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.repository.CourseRepostiory;
import com.school.registrationsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepostiory courseRepostiory;

    public void saveStudent(StudentDto studentDto) {
        if (studentRepository.findByStudentIndex(studentDto.getStudentIndex()) == null) {
            studentRepository.save(
                    Student.builder()
                            .name(studentDto.getName())
                            .surname(studentDto.getSurname())
                            .studentIndex(studentDto.getStudentIndex())
                            .build());
        } else {
            throw new IndexOccupiedException(studentDto.getStudentIndex() + "is in use!");
        }
    }

    public void saveStudent(Student student) {
        Student byStudentIndex = studentRepository.findByStudentIndex(student.getStudentIndex());
        if (byStudentIndex == null) {
            studentRepository.save(student);
        } else {
            throw new IndexOccupiedException(student.getStudentIndex() + " is in use!");
        }
    }

    @Transactional
    public void deleteStudent(int studentIndex) {
        Student byStudentIndex = studentRepository.findByStudentIndex(studentIndex);
        if (byStudentIndex != null) {
            for (Course course : byStudentIndex.getCourseList()) {
                course.getStudentList().remove(byStudentIndex);
                courseRepostiory.save(course);
            }
        }
        studentRepository.deleteByStudentIndex(studentIndex);
    }

    public void modifyStudent(StudentDto studentDto) {
        if (studentDto.getName() == null || studentDto.getSurname() == null || studentDto.getStudentIndex() == 0) {
            throw new NullPointerException("Modification form is incorrect.");
        }
        Student student = studentRepository.findByStudentIndex(studentDto.getStudentIndex());
        if (student == null) {
            throw new EntityNotFoundException("Student has been not found.");
        }
        updateStudentValues(student, studentDto);
    }

    private void updateStudentValues(Student student, StudentDto studentDto) {
        student.setName(studentDto.getName());
        student.setSurname(studentDto.getSurname());
        studentRepository.save(student);
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public void registerStudentToCourse(RegisterDto registerDto) {
        Student student = studentRepository.findByStudentIndex(registerDto.getStudentIndex());
        Course course = courseRepostiory.findByCourseIndex(registerDto.getCourseIndex());

        if (validateRegisterStudentToCourse(registerDto)) {
            student.getCourseList().add(course);
            course.getStudentList().add(student);
            studentRepository.save(student);
        }
    }

    private boolean validateRegisterStudentToCourse(RegisterDto registerDto) {
        Student student = studentRepository.findByStudentIndex(registerDto.getStudentIndex());
        Course course = courseRepostiory.findByCourseIndex(registerDto.getCourseIndex());
        if (student == null || course == null) {
            throw new EntityNotFoundException("Student or Course not found");
        } else if (!isCourseHaveNot50Students(registerDto.getCourseIndex())) {
            throw new CapacityException("Course is overflowing.");
        } else if (!isStudentHaveNot5Courses(registerDto.getStudentIndex())) {
            throw new CapacityException("Student have too many courses.");
        } else if (course.getEndDate().isBefore(LocalDate.now())) {
            throw new TimeOutCourseRegisterException("Course has been closed.");
        } else if (student.getCourseList().contains(course)) {
            throw new DuplicateEnrollmentException("Student is already on this course.");
        }
        return true;
    }

    private boolean isCourseHaveNot50Students(int studentIndex) {
        return courseRepostiory.isCourseHaveNot50Students(studentIndex);
    }

    private boolean isStudentHaveNot5Courses(int courseIndex) {
        return studentRepository.isStudentHaveNot5Courses(courseIndex);
    }

    public List<Student> getStudentListByCourseIndex(int courseIndex) {
        return studentRepository.findByCourseIndex(courseIndex);
    }

    public List<Student> getStudentByEmptyCourseList() {
        return studentRepository.findByEmptyCourseList();
    }

    public Student getStudentByStudentIndex(int studentIndex) {
        return studentRepository.findByStudentIndex(studentIndex);
    }

    public Student testExpectedStudent(Student testStudent) {
        testStudent.setName("newName");
        testStudent.setSurname("newSurname");
        return testStudent;
    }

    public void saveAll(List<Student> studentList) {
        studentRepository.saveAll(studentList);
    }
}