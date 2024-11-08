# Yoga Project

## Description
This project is a Yoga application that includes user management, session scheduling, and authentication features. It consists of a front-end Angular application, a back-end Spring Boot application, and end-to-end tests using Cypress.

## Prerequisites
- Java 17 or higher
- Node.js 14 or higher
- Angular CLI
- MySQL or H2 Database

## Database installation

### MySQL
1. Install MySQL on your machine.
2. Create a new database named `yoga`.
3. Configure your `application.properties` file with the appropriate connection details for your MySQL instance.
4. Run the script from `ressources/sql/script.sql`

### H2 (in memory)
To use H2 :
- No download required, H2 is integrated with Spring Boot.
- The H2 database configuration is already present in `application.properties`.

NB : i'm using a H2 DB for the tests. 

## Installing the application

1. Clone the project from GitHub :
```bash
git clone https://github.com/PlaNeiT/OC-NumDev.git
cd OC-NumDev
 ```

3. Install backend dependencies :
 ```bash
cd back
mvn clean install
```

3. Install dependencies for the frontend :
 ```bash
cd front
npm install
 ```


## Running the application

### Backend
1.  In the `back` directory, run:
 ```bash
mvn spring-boot:run
 ```
### Frontend
1. In the `front` directory, run :
 ```bash
ng serve
 ```

The application will be available at [http://localhost:4200](http://localhost:4200).

## Run the various tests

### Tests Backend
1. In the `back` directory, run:
 ```bash
mvn test
 ```

### Tests Frontend (Jest)
1. In the `front` directory, run :  
 ```bash
 npx jest --coverage 
 ```


### Tests End-to-End (Cypress)
1. In the `front` directory, run :
 ```bash
npm run cypress:run
npm run e2e:coverage   
 ```

Reports will be generated in the `coverage` directory.

## Front coverage : 
### Jest
![jest](https://github.com/user-attachments/assets/13e1e423-c078-4b0b-aea0-977524f5e7d5)

### Cypress
![cypress](https://github.com/user-attachments/assets/b39114fc-db07-4a6d-ab6b-3e0e0f80b654)

## Back coverage :
### Mockito / JUnit
![Mock](https://github.com/user-attachments/assets/4f1952fb-3ea3-42ce-860f-b5352f73112c)

