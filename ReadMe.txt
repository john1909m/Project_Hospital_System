# Hospital Management System

A web-based **Hospital Management System** that allows **patients, doctors, and admins** to interact through separate dashboards. The system is secured using **JWT token authentication**.

## Features

### Patient Dashboard
- Register and manage patient profiles
- Book and view appointments
- Access prescriptions
- Secure login with JWT authentication

### Doctor Dashboard
- View and manage appointments
- Access patient information
- Secure login with JWT authentication

### Admin Dashboard
- Manage doctors and patients
- Approve or cancel appointments
- Secure login with JWT authentication

## Authentication
- **JWT-based authentication** ensures secure access to dashboards
- Separate roles for **patients, doctors, and admins**
- Token expiration and refresh mechanism implemented

## Technology Stack
- **Frontend:** React.js + vite + Tailwind.css
- **Backend:**  Java / Spring Boot
- **Database:** Oracle Db
- **Authentication:** JWT (JSON Web Tokens)
- **Version Control:** Git & GitHub

## Installation

1. Clone the repository:
   ```bash
   git clone <https://github.com/john1909m/Project_Hospital_System/tree/main>
   cd Project_Hospital_System

2.Run FrontEnd:
download and install node.js from : https://nodejs.org/en/download/current
   ```bash
	npm install
	npm run dev

3.Run Backend
	Java JDK 11 or higher installed
	Maven installed
	Oracle Db must be installed
	
	In application.yml:
		spring.datasource.username={username on db}
		spring.datasource.password={password on db}
	```bash
		mvn clean install
		mvn spring-boot:run
