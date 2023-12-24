### School Registration System
                                     

#Course Controller API

This api provides endpoints Create, read, update and delete for courses.

## Endpoints


### Save Course

*Endpoint* ' POST /courses'
*Description* Create and save a new course.
*Request Body* json:

Example:
{
    "name": "Course Name",
    "courseIndex": 123,
    "startDate": "2023-01-01",
    "endDate": "2023-06-30",
    "description": "Course description"
}


Response that you get is:

{
  "message": "Course saved correctly"
}

Status codes that you can get:

201 Created: Successful creation of the course.
400 Bad Request: Invalid request format or missing required fields.

### Delete Course

*Endpoint* 'DELETE /courses'
*Description* delete a cours by its index
*Request Body* json :

"123"

Response that you get is:
{
  "message": "Course deleted correctly"
}

Status codes that you can get:

200 OK: Successful deletion of the course.
404 Not Found: Course not found.

### Modify Course

*Endpoint* 'PUT /courses"
*Description* Modify an existing course
*Request Body* json:

Example:
{
  "name": "Updated Course Name",
  "courseIndex": 123,
  "startDate": "2023-01-01",
  "endDate": "2023-12-31",
  "description": "Updated course description"
}

Respone that you get is:

{
  "message": "Course modified correctly"
}

Status codes that you can get:

200 OK: Successful modification of the course.
404 Not Found: Course not found.


### Get Courses

*Endpoint* 'GET /courses'
*Description* Retereive a list of all courses


Response that you can get is :

{
  "courses": [
    {
      "name": "Course Name",
      "courseIndex": 123,
      "startDate": "2023-01-01",
      "endDate": "2023-12-31",
      "description": "Course description"
    },
    // ... more courses ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of the courses.
404 Not Found: No courses found.

### Filter Controller API

This API provides endpoints for filtering students and courses based on various criteria

## Endpoints

### Get Students by Course Index

*Endpoint:* `GET /filters/{courseIndex}`
*Description:* Retrieve a list of students enrolled in a specific course.
*Path Variable:*
   `courseIndex` (integer): The index of the course.
 *Response:*

Example:
  {
    "students": [
      {
        "studentId": 1,
        "name": "John",
        "surname": "Doe",
        "studentIndex": 12345
      },
      // ... more students ...
    ]
  }


Status codes that you can get:
200 OK: Successful retrieval of students.
404 Not Found: Course not found or no students enrolled.


### Get Courses by Student Index

*Endpoint* GET /filters/{studentIndex}
*Description* Retrieve a list of courses in which a specific student is enrolled.
*Path Variable*
studentIndex (integer): The index of the student.

*Response*

Example:
{
  "courses": [
    {
      "courseId": 1,
      "name": "Math",
      "courseIndex": 101,
      "startDate": "2023-01-01",
      "endDate": "2023-06-30"
    },
    // ... more courses ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of courses.
404 Not Found: Student not found or no courses enrolled.


###Get Students with No Courses

*Endpoint* GET /filters/studentsWithNoCourses
*Description* Retrieve a list of students not enrolled in any course.
*Response*
Example:
{
  "students": [
    {
      "studentId": 2,
      "name": "Jane",
      "surname": "Smith",
      "studentIndex": 67890
    },
    // ... more students ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of students with no courses.
404 Not Found: No students without courses found.

### Get Courses with No Students
*Endpoint* GET /filters/coursesWithNoStudents
*Description* Retrieve a list of courses with no enrolled students.
*Response*
Example: json

{
  "courses": [
    {
      "courseId": 2,
      "name": "Physics",
      "courseIndex": 102,
      "startDate": "2023-02-01",
      "endDate": "2023-07-31"
    },
    // ... more courses ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of courses with no students.
404 Not Found: No courses without students found.


### Register API


This API provides endpoints for registering students to courses.

## Endpoints

### Register Student to Course

*Endpoint:* `POST /register`
*Description* Register a student to a course.
*Request Body*: json
Example:
  {
    "studentIndex": 12345,
    "courseIndex": 101
  }
Response that you get is:

{
  "message": "Student registered correctly"
}

Http Status Code that you can get:

200 OK : Succesful registration of the student to the course
400 Bad Request: Main reason is occupied Student Index

### Student Controller API

This api provides endpoints Create, read, update and delete for students.

## Endpoints


### Save Student

*Endpoint* ' POST /students'
*Description* Create and save a new student.
*Request Body* json:

Example:
{
    "name": "Name",
    "surname": "surname",
    "studentIndex" : 1234
}


Response that you get is:

{
  "message": "Student saved correctly"
}

Status codes that you can get:

201 Created: Successful creation of the student.
400 Bad Request: Invalid request format or missing required fields.

### Delete Student

*Endpoint* 'DELETE /students'
*Description* delete a student by its index
*Request Body* json :

"1234"

Response that you get is:
{
  "message": "Student deleted correctly"
}

Status codes that you can get:

200 OK: Successful deletion of the student.
404 Not Found: Student not found.

### Modify Student

*Endpoint* 'PUT /student"
*Description* Modify an existing student
*Request Body* json:

Example:
{
    "name": "new  Name",
    "surname": "New surname",
    "studentIndex" : 1234
}

Respone that you get is:

{
  "message": "Student modified correctly"
}

Status codes that you can get:

200 OK: Successful modification of the student.
404 Not Found: student not found.


### Get Students

*Endpoint* 'GET /students'
*Description* Retereive a list of all students


Response that you can get is :
Example:
{
  "students": [
    {
      "name": "new  Name",
    "surname": "New surname",
    "studentIndex" : 1234
    },
    // ... more students ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of the courses.
404 Not Found: No courses found.

How to Run
Clone the repository.
Navigate to the project directory.

// Wait for docker

The application will be accessible at http://localhost:8080.


Technologies Used in project:
Java
Mavan
Spring Boot
Docker
JUnit 5
MySQL
Hibernate
