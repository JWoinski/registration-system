package com.school.registrationsystem.service;

import com.school.registrationsystem.exception.CapacityException;
import com.school.registrationsystem.exception.DuplicateEnrollmentException;
import com.school.registrationsystem.exception.IndexOccupiedException;
import com.school.registrationsystem.exception.TimeOutCourseRegisterException;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.repository.CourseRepository;
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
    private final CourseRepository courseRepostiory;

    /**
     * Saves a new student based on the information provided in the StudentDto, ensuring the student index is not already in use.
     *
     * @param studentDto The StudentDto containing the details of the student to be saved.
     * @throws IndexOccupiedException Thrown if the student index is already in use.
     */
    public void saveStudent(StudentDto studentDto) {
        if (studentRepository.findByStudentIndex(studentDto.getStudentIndex()) == null) {
            //Save a new student if the student index is not in use
            studentRepository.save(
                    Student.builder()
                            .name(studentDto.getName())
                            .surname(studentDto.getSurname())
                            .studentIndex(studentDto.getStudentIndex())
                            .build());
        } else {
            //Throw an exception if the student index is already in use
            throw new IndexOccupiedException(studentDto.getStudentIndex() + "is in use!");
        }
    }

    /**
     * Saves a student to the repository, ensuring that the student index is not already in use.
     *
     * @param student The Student object to be saved.
     * @throws IndexOccupiedException Thrown if the student index is already in use.
     */
    public void saveStudent(Student student) {
        //Check if the student index is not already in use
        Student byStudentIndex = studentRepository.findByStudentIndex(student.getStudentIndex());
        if (byStudentIndex == null) {
            //Save the student to the repository
            studentRepository.save(student);
        } else {
            //Throw and exception if the student inde is already in use
            throw new IndexOccupiedException(student.getStudentIndex() + " is in use!");
        }
    }

    /**
     * Deletes a student by their student index, ensuring proper handling of associated courses' student lists.
     * Uses a transaction to maintain data consistency.
     *
     * @param studentIndex The index of the student to be deleted.
     */
    @Transactional
    public void deleteStudent(int studentIndex) {
        //Retrieve the existing student by their index
        Student byStudentIndex = studentRepository.findByStudentIndex(studentIndex);
        if (byStudentIndex != null) {
            //Update associated courses' student lists
            for (Course course : byStudentIndex.getCourseList()) {
                course.getStudentList().remove(byStudentIndex);
                courseRepostiory.save(course);
            }
        }
        studentRepository.deleteByStudentIndex(studentIndex);
    }

    /**
     * Modifies a student based on the information provided in the StudentDto.
     *
     * @param studentDto The StudentDto containing the modified student details.
     * @throws NullPointerException    Thrown if the modification form is incorrect.
     * @throws EntityNotFoundException Thrown if the student with the specified index is not found.
     */
    public void modifyStudent(StudentDto studentDto) {
        //Check if the modification form is correct
        if (studentDto.getName() == null || studentDto.getSurname() == null || studentDto.getStudentIndex() == 0) {
            throw new NullPointerException("Modification form is incorrect.");
        }

        //Retrieve the existing student by their index
        Student student = studentRepository.findByStudentIndex(studentDto.getStudentIndex());
        if (student == null) {
            throw new EntityNotFoundException("Student has been not found.");
        }

        //Update the values of the existing student with those from studentDto
        updateStudentValues(student, studentDto);
    }

    /**
     * Updates the values of an existing Student object with the information provided in the StudentDto.
     *
     * @param student    The existing Student object to be updated.
     * @param studentDto The StudentDto containing the modified student details.
     */
    private void updateStudentValues(Student student, StudentDto studentDto) {
        //Update the name and surname of the existing student
        student.setName(studentDto.getName());
        student.setSurname(studentDto.getSurname());

        //Save the updated student to the repoistory
        studentRepository.save(student);
    }

    /**
     * Retrieves a list of all students from the repository.
     *
     * @return A List of Student objects representing all students in the repository.
     */
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    /**
     * Registers a student to a course based on the information provided in the RegisterDto.
     * Uses a transaction to ensure data consistency.
     *
     * @param registerDto The RegisterDto containing the details for registering a student to a course.
     */
    @Transactional
    public void registerStudentToCourse(RegisterDto registerDto) {
        //Retrieve the student and crouse based on the provided indices
        Student student = studentRepository.findByStudentIndex(registerDto.getStudentIndex());
        Course course = courseRepostiory.findByCourseIndex(registerDto.getCourseIndex());

        //Validate the registration details
        if (validateRegisterStudentToCourse(registerDto)) {
            //Add the student to the course's student list and vice versa
            student.getCourseList().add(course);
            course.getStudentList().add(student);

            //Save the updated student infromation to the repostiory
            studentRepository.save(student);
        }
    }

    /**
     * Validates the registration details for enrolling a student to a course.
     *
     * @param registerDto The RegisterDto containing the details for registering a student to a course.
     * @return true if the registration details are valid, false otherwise.
     * @throws EntityNotFoundException        Thrown if the student or course is not found.
     * @throws CapacityException              Thrown if the course is at maximum capacity or the student exceeds the course limit.
     * @throws TimeOutCourseRegisterException Thrown if the course registration period has ended.
     * @throws DuplicateEnrollmentException   Thrown if the student is already enrolled in the course.
     */
    private boolean validateRegisterStudentToCourse(RegisterDto registerDto) {
        //Retrieve the student and course based on the provided
        Student student = studentRepository.findByStudentIndex(registerDto.getStudentIndex());
        Course course = courseRepostiory.findByCourseIndex(registerDto.getCourseIndex());

        //Check if the student or course is not found
        if (student == null || course == null) {
            throw new EntityNotFoundException("Student or Course not found");
        }

        //Check if the course is at maximum capacity
        else if (!isCourseHaveNot50Students(registerDto.getCourseIndex())) {
            throw new CapacityException("Course is overflowing.");
        }

        //Check if the student exceeds the course limit
        else if (!isStudentHaveNot5Courses(registerDto.getStudentIndex())) {
            throw new CapacityException("Student have too many courses.");
        }

        //Check if the course registration period has ended
        else if (course.getEndDate().isBefore(LocalDate.now())) {
            throw new TimeOutCourseRegisterException("Course has been closed.");
        }

        //Check if the student is already enrolled in the course
        else if (student.getCourseList().contains(course)) {
            throw new DuplicateEnrollmentException("Student is already on this course.");
        }
        return true;
    }

    /**
     * Checks if a course has less than 50 students enrolled.
     *
     * @param studentIndex The index of the student for whom the course enrollment is checked.
     * @return true if the course has less than 50 students, false otherwise.
     */
    private boolean isCourseHaveNot50Students(int studentIndex) {
        return courseRepostiory.isCourseHaveNot50Students(studentIndex);
    }

    /**
     * Checks if a student is enrolled in less than 5 courses.
     *
     * @param courseIndex The index of the course for which the student's course enrollment is checked.
     * @return true if the student is enrolled in less than 5 courses, false otherwise.
     */
    private boolean isStudentHaveNot5Courses(int courseIndex) {
        return studentRepository.isStudentHaveNot5Courses(courseIndex);
    }

    /**
     * Retrieves a list of students enrolled in a course based on the provided course index.
     *
     * @param courseIndex The index of the course for which the student list is to be retrieved.
     * @return A List of Student objects representing the students enrolled in the specified course.
     */
    public List<Student> getStudentListByCourseIndex(int courseIndex) {
        return studentRepository.findByCourseIndex(courseIndex);
    }

    /**
     * Retrieves a list of students with an empty course enrollment list.
     *
     * @return A List of Student objects representing students without any enrolled courses.
     */
    public List<Student> getStudentByEmptyCourseList() {
        return studentRepository.findByEmptyCourseList();
    }

    /**
     * Retrieves a student based on the provided student index.
     *
     * @param studentIndex The index of the student to be retrieved.
     * @return The Student object representing the student with the specified index, or null if not found.
     */
    public Student getStudentByStudentIndex(int studentIndex) {
        return studentRepository.findByStudentIndex(studentIndex);
    }

    /**
     * Saves a list of students to the repository.
     *
     * @param studentList The List of Student objects to be saved.
     */
    public void saveAll(List<Student> studentList) {
        studentRepository.saveAll(studentList);
    }
}