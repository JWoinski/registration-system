# School Registration System

## Course Controller API

This api provides endpoints Create, read, update and delete for courses.

## Endpoints


### Save Course

*Endpoint* `POST /courses` <br><br>
*Description* Create and save a new course. <br><br>
*Request Body* json:

Example: <br>
{ <br>
"name": "Course Name", <br>
"courseIndex": 123,<br>
"startDate": "2023-01-01",<br>
"endDate": "2023-06-30",<br>
"description": "Course description"<br>
}


Response that you get is:

{<br>
"message": "Course saved correctly"<br>
}

Status codes that you can get:

201 Created: Successful creation of the course.
400 Bad Request: Invalid request format or missing required fields.

### Delete Course

*Endpoint* `DELETE /courses`<br>
*Description* delete a cours by its index<br>
*Request Body* json :

{<br>
"index" : 123<br>
}

Response that you get is:<br>
{<br>
"message": "Course deleted correctly"<br>
}

Status codes that you can get:

200 OK: Successful deletion of the course.
404 Not Found: Course not found.

### Modify Course
*Endpoint* 'PUT /courses"
*Description* Modify an existing course via courseIndex
*Request Body* json:

*Endpoint* `PUT /courses`<br>
*Description* Modify an existing course via courseIndex<br>
*Request Body* json:<br>

Example:<br>
{<br>
"name": "Updated Course Name",<br>
"courseIndex": 123,<br>
"startDate": "2023-01-01",<br>
"endDate": "2023-12-31",<br>
"description": "Updated course description"<br>
}

Respone that you get is:

{<br>
"message": "Course modified correctly"<br>
}

Status codes that you can get:

200 OK: Successful modification of the course.
404 Not Found: Course not found.


### Get Courses

*Endpoint* `GET /courses`<br>
*Description* Retereive a list of all courses<br>


Response that you can get is :

{<br>
"courses": [<br>
{<br>
"name": "Course Name",<br>
"courseIndex": 123,<br>
"startDate": "2023-01-01",<br>
"endDate": "2023-12-31",<br>
"description": "Course description"<br>
},<br>
    // ... more courses ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of the courses.
404 Not Found: No courses found.

# Filter Controller API

This API provides endpoints for filtering students and courses based on various criteria

## Endpoints

### Get Students by Course Index

*Endpoint:* `GET /filters/courses/{courseIndex}`<br><br>
*Description:* Retrieve a list of students enrolled in a specific course.<br><br>
*Path Variable:*
`courseIndex` (integer): The index of the course.<br>
*Response:*<br>

Example:<br>
{<br>
    "students": [
{<br>
"studentId": 1,<br>
"name": "John",<br>
"surname": "Doe",<br>
"studentIndex": 12345<br>
},<br>
      // ... more students ...
    ]
  }

Status codes that you can get:<br>
200 OK: Successful retrieval of students.<br>
404 Not Found: Course not found or no students enrolled.


### Get Courses by Student Index

*Endpoint* `GET /filters/students/{studentIndex}`<br>
*Description* Retrieve a list of courses in which a specific student is enrolled.<br>
*Path Variable* `studentIndex` (integer): The index of the student.

*Response*

Example:
{
  "courses": [
{<br>
"courseId": 1,<br>
"name": "Math",<br>
"courseIndex": 101,<br>
"startDate": "2023-01-01",<br>
"endDate": "2023-06-30"<br>
    },
    // ... more courses ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of courses.<br>
404 Not Found: Student not found or no courses enrolled.

### Get Students with No Courses

*Endpoint* `GET /filters/studentsWithNoCourses`<br>
*Description* Retrieve a list of students not enrolled in any course.<br>
*Response example*<br><br>
{<br>
"students": [{<br><br>
"studentId": 2,<br>
"name": "Jane",<br>
"surname": "Smith",<br>
"studentIndex": 67890<br>
},<br><br>
    // ... more students ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of students with no courses.<br>
404 Not Found: No students without courses found.

### Get Courses with No Students

*Endpoint* `GET /filters/coursesWithNoStudents`<br><br>
*Description* Retrieve a list of courses with no enrolled students.<br><br>
*Response json example:*<br>

{<br>
  "courses": [
{<br><br>
"courseId": 2,<br>
"name": "Physics",<br>
"courseIndex": 102,<br>
"startDate": "2023-02-01",<br>
"endDate": "2023-07-31"<br>
},<br><br>
    // ... more courses ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of courses with no students.
404 Not Found: No courses without students found.

# Register API

This API provides endpoints for registering students to courses.

## Endpoints

### Register Student to Course

*Endpoint:* `POST /register`<br>
*Description* Register a student to a course.<br>
*Request Body json example:*<br>
{<br>
"studentIndex": 12345,<br>
"courseIndex": 101<br>
  }
Response that you get is:

{<br>
"message": "Student registered correctly"<br>
}

Http Status Code that you can get:

200 OK : Succesful registration of the student to the course<br>
400 Bad Request: Main reason is occupied Student Index

# Student Controller API

This api provides endpoints Create, read, update and delete for students.

## Endpoints


### Save Student

*Endpoint* `POST /students`<br><br>
*Description* Create and save a new student.
*Request Body* json:

Example:<br>
{<br>
"name": "Name",<br>
"surname": "surname",<br>
"studentIndex" : 1234<br>
}


Response that you get is:

{<br>
"message": "Student saved correctly"<br>
}

Status codes that you can get:

200 Created: Successful creation of the student.<br>
400 Bad Request: Invalid request format or missing required fields.

### Delete Student

*Endpoint* `DELETE /students`<br>
*Description* delete a student by its index<br>
*Request Body json* :

{<br>
"index" : 1234<br>
}

Response that you get is:<br>
{<br>
"message": "Student deleted correctly"<br>
}

Status codes that you can get:

200 OK: Successful deletion of the student.<br>
404 Not Found: Student not found.

### Modify Student

*Endpoint* `PUT /student`<br>
*Description* Modify an existing student via studentIndex<br>
*Request Body json:*

Example:<br>
{<br>
"name": "new Name",<br>
"surname": "New surname",<br>
"studentIndex" : 1234<br>
}

Respone that you get is:

{<br>
"message": "Student modified correctly"<br>
}

Status codes that you can get:

200 OK: Successful modification of the student.<br>
404 Not Found: student not found.


### Get Students

*Endpoint* `GET /students`<br>
*Description* Retereive a list of all students

Response that you can get is :
Example:<br>
{<br>
  "students": [
{<br>
"name": "new  Name",<br>
"surname": "New surname",<br>
"studentIndex" : 1234<br>
},<br>
    // ... more students ...
  ]
}

Status codes that you can get:

200 OK: Successful retrieval of the courses.<br>
404 Not Found: No courses found.

## How to Run

Clone the repository.<br>
Navigate to the project directory.<br>
The application will be accessible at http://localhost:8080.

Technologies Used in project:<br>
Java<br>
Maven<br>
Liquibase<br>
Spring Boot<br>
Docker<br>
JUnit 5<br>
Mockito<br>
MySQL<br>
Hibernate<br>
