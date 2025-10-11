# vmmanager

Manage, start and stop your VMs

## Technologies Used

* Java 21
* Spring Boot
* Maven

## Getting Started

To get started with this project, you will need to have Java 21 and Maven installed.

1. Clone the repository:

```bash
git clone https://github.com/udayraj123/vmmanager.git
```

2. Build the project:

```bash
mvn clean install
```

3. Run the application:

```bash
java -jar target/vmmanager-0.0.1-SNAPSHOT.war
```

## API Endpoints

### Test VM SSH Connection

* **URL:** `/vm/test-connection`
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

* **Success Response:**
  * **Code:** 200
  * **Content:**

```json
{
    "id": "a7a7a7a7-a7a7-a7a7-a7a7-a7a7a7a7a7a7",
    "ipAddress": "your_vm_ip",
    "portNumber": 22,
    "username": "your_username",
    "password": "your_password",
    "osName": "Ubuntu",
    "hostName": "ubuntu-vm",
    "cpuCores": "4",
    "rootPartitionSize": "50G",
    "totalRamSize": "8G",
    "macAddress": "00:11:22:33:44:55"
}
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
