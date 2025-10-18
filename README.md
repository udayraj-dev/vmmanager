# Virtual Machine Manager

A Spring Boot application to manage, start, and stop your Virtual Machines.

## Technologies Used

* **Java:** 21
* **Framework:** Spring Boot 3
* **Build Tool:** Maven
* **Database:** H2 (default), SQLite
* **SSH:** JCraft (jsch)
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)

## Getting Started

### Prerequisites

* Java 21
* Maven

### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/udayraj123/vmmanager.git
    ```

2. **Build the project:**

    ```bash
    mvn clean install
    ```

3. **Run the application:**

    ```bash
    java -jar target/vmmanager-0.0.1-SNAPSHOT.war
    ```

4. **Access the application:**

    The web interface will be available at http://localhost:8080.

## Web Interface

The application includes a web interface for managing VMs. You can view, add, and see details for your virtual machines.

## API

API documentation is available through Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### API Endpoints

The base URL for all API endpoints is `/api/vms`.

| Method | Endpoint           | Description                                                |
| :----- | :----------------- | :--------------------------------------------------------- |
| `POST` | `/test-connection` | Test SSH connection and retrieve VM details.               |
| `POST` | `/`                | Save a new VM's information.                               |
| `PUT`  | `/{id}`            | Update an existing VM.                                     |
| `DELETE`| `/{id}`            | Soft-delete a VM.                                          |
| `GET`  | `/`                | Get a list of all VMs.                                     |
| `GET`  | `/{id}`            | Get a single VM by its ID.                                 |

### Example: Test VM SSH Connection

* **URL:** `/api/vms/test-connection`
* **Method:** `POST`
* **Request Body:**

    ```json
    {
        "ipAddress": "your_vm_ip",
        "portNumber": 22,
        "username": "your_username",
        "password": "your_password"
    }
    ```

## Database Configuration

This application uses a file-based database. You can switch between H2 and SQLite by activating the corresponding Spring profile.

* **H2 (default):** No configuration needed. The database is stored in memory.
* **SQLite:** To use SQLite, set the following Spring profile:

    ```
    spring.profiles.active=sqlite
    ```

## License

This project is licensed under the MIT License.
