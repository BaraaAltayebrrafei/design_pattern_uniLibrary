# University Media Library

A Java Swing desktop application for managing educational media (documents, videos, quizzes) in a university library, organised by specialty and course. Built as a design-patterns project.

## Features
- Login for an administrator and for students
- Admin panel: add specialties, courses (matières) and media
- Student view: browse and search media for their own courses
- Search by title, course, specialty or media ID
- Export results to CSV or XML
- Statistics view with simple charts
- Data stored in XML (students) and Java serialization (media, courses, specialties)

## Design patterns
- **Strategy**: interchangeable search algorithms (`SearchStrategy`) and export formats (`ExportStrategy`)
- **Factory**: `MediaFactory` creates `Document`, `Video` or `Quiz` objects
- **Composite / Observer / Visitor**: interfaces are defined (`LibraryComponent`, `Observer`), but these patterns are not fully implemented yet

## Requirements
- Java 21 or later
- [JDOM 2.0.6.1](http://www.jdom.org/downloads/) jar, placed in a `lib/` folder

## Run
From the project root:

```
javac -cp lib/jdom-2.0.6.1.jar -d out src/java4project/*.java
java -cp "out;lib/jdom-2.0.6.1.jar" java4project.Java4Project
```

On Linux/macOS, use `:` instead of `;` in the classpath. Run from the project root, because data paths such as `data/universite.xml` are relative.

You can also open the project in NetBeans.

## Login
- Admin: `admin` / `admin`
- Students: any username and password from `data/universite.xml` (sample data only)

## Project structure
```
src/java4project/   source code
data/               sample student data and exports
nbproject/          NetBeans project files
```

## Known limitations
- Passwords are stored in plain text (demo only)
- Observer and Visitor patterns are not implemented yet
- No automated tests
