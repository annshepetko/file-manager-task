
## How to run it
First of all you should run  
docker compose file (there is  a redis )

1. Open terminal and move to the root of project
2. Run ``docker compose up``
3. Wait until container will run
4. Start the application using intellij or maven : ``mvn spring-boot:run`` in terminal

## Features 
I have implemented modifying file and cache it in Redis, also I have add documentation to some classes for you

## How to ensure that it`s work

### **POST**  
`http://localhost:8080/api/v1/enrich`

Uploads a file for enrichment and returns the processed file.

#### **Request**
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/v1/enrich`
- **Headers:**
    - `Content-Type: multipart/form-data`
- **Body:**
    - `file`: The file to be uploaded.

#### **Usage Examples**

### **Linux & macOS** 
```sh
curl -X POST "http://localhost:8080/api/v1/enrich" \
     -H "Content-Type: multipart/form-data" \
     -F "file=@/path/to/file.csv" \
     -o output_file
```
### **Windows**
```sh
curl -X POST "http://localhost:8080/api/v1/enrich" ^
     -H "Content-Type: multipart/form-data" ^
     -F "file=@C:\path\to\file.csv" ^
     -o output_file
```


## Or we can run a request through postman

### Steps:
1. **Open Postman**
2. **Set the method to POST**
3. **Enter the URL: http://localhost:8080/api/v1/enrich**
4. **Go to the Body tab.**
5. **Select form-data.**
6. **Add a new key-value pair:**
  * **Key: file**
  * **Value: Choose your file.**
7.  **Click Send**

## What I`d done if I have more time

**I'd done these things**

1. **I`d done addition file formats such as JSON & XML**
2. **I`d done faster file processing because, csv library which I have used for the first time**
3. **I`d set adding new products in database and implement API for managing data in database**
