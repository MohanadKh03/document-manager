# Document Management System (Google Drive Clone)

A document management system built with Spring Boot that provides functionality similar to Google Drive, allowing users to store, organize, and share documents with role-based access control.

## Features

- **File Management**
  - Upload documents (PDF, Word, Excel)
  - File metadata management (title, description, tags)
  - File size validation (max 10MB)
  - Secure file storage using Cloudinary
  - File download and preview

- **Folder Organization**
  - Create hierarchical folder structures
  - Move files between folders
  - Navigate folder trees

- **Access Control**
  - Role-based access control (View, Edit, Manage permissions)
  - Share files with other users
  - File ownership management

- **Tagging System**
  - Add multiple tags to files
  - Search and filter by tags
  - Organize content with custom taxonomies

## Prerequisites

- Java 21
- Maven 3.8+
- MariaDB 11.7+
- Cloudinary account

## Installation

1. **Clone the repository**
```bash
git clone https://github.com/MohanadKh03/document-manager.git
cd document-manager
```
2. **Configure application.yaml**
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB # configurable
      max-request-size: 20MB #configurable
  datasource:
    url: jdbc:mariadb://localhost:3306/document_manager
    username: YOUR-USER
    password: YOUR-PASSWORD
    driver-class-name: org.mariadb.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    generate-ddl: true
logging:
  level:
    org:
      hibernate:
        SQL=DEBUG:
cloudinary:
  cloud-name: YOUR-CLOUD-NAME
  api-key: YOUR-API-KEY
  api-secret: YOUR-API-SECRET
```
3. **Create the database**
```sql
CREATE DATABASE document_manager;
```
## Building and Running

1. **Build the project**
2. **Run the application**
```bash
java -jar target/document-manager-0.0.1-SNAPSHOT.jar
```
Alternatively, you can run it directly with Maven:
```bash
mvn spring-boot:run
```
## API Documentation

Complete API documentation is available in [Postman Collection](https://www.postman.com/interstellar-comet-438348/workspace/erp-demo/collection/25977966-b724960f-7f29-4af3-88c0-ad56b587e2db?action=share&creator=25977966)
**NOTE: The current way user-id us handled is not secure and just used for demonstration purposes. It should instead use tokens and actual user authentication/authorization**

### Key Endpoints:
- **Authentication**
  - POST `/api/auth/register` - Register new user
  - POST `/api/auth/login` - Login user

- **Files**
  - POST `/api/files` - Upload new file
  - GET `/api/files/{fileId}` - Get file details
  - GET `/api/files/root` - List files in root directory
  - GET `/api/files/folder/{folderId}` - List files in specific folder
  - DELETE `/api/files/{fileId}` - Delete file

- **Folders**
  - POST `/api/folders` - Create new folder
  - GET `/api/folders/{folderId}` - Get folder details
  - PUT `/api/folders/{folderId}` - Update folder
  - DELETE `/api/folders/{folderId}` - Delete folder

- **Permissions**
  - POST `/api/permissions` - Set file permissions
  - GET `/api/permissions/{fileId}` - Get file permissions
  - DELETE `/api/permissions/{permissionId}` - Remove permission
  
## Security
The application implements several security measures:
- File type validation
- Maximum file size restrictions
- Role-based access control
- User authentication
- Secure file storage

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
