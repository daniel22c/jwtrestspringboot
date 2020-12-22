# Name : Myungho Sim
# Requirements
REST endpoints that reads the above JSON feed using HTTP. 
The service should perform the following tasks: 
API - JWT 
Create a JWT creation API using secret key. Keys can be hardcoded in backend for authentication.
JWT should be using timestamp to generate the JWT and have expiration date data inside the JWT token.
This should be callable from curl or postman.

API Count endpoint 
JWT is required on the header to authenticate the api.
Tally the number of unique user Ids in the JSON and return in a JSON response. 
This should be callable from curl or postman.
Updated User List endpoint 
Modify the 4th JSON array item, changing the title and body of the object to “Encora”. 
Return the modified JSON object to the controller class from the service then in the endpoint JSON response. 
Unit Test: 
Write a JUNIT/Mockito Unit tests which should be runnable via “mvn test” 
Should be written to test all aspects of the application (include mock of the feed). 
Requirements 
Use Java 1.8+ 
Use Spring Boot 
Use Maven 
Use JUnit and Mockito 
All dependencies should be publicly available or properly included with the project and referenced within the POM 

#How to Test
 * Make sure to Authenticate and receive Jwt token. 
 * Include Jwt token in the header authorization bearer when sending requests
 * Use postman to get all posts from http://localhost:8080/posts/all
 * To get counts of unique user ids : http://localhost:8080/posts/count_unique_users
 * To update the 4th post and get the updated list of posts, send put request to http://localhost:8080/posts/update_4th_to_encora
 * To authenticate and retrieve jwt token 
   POST http://localhost:8080/authenticate
   with body 
{
    "username": "admin",
    "password": "password"
}
 * Server monitoring using Spring Actuator
 Get request to http://localhost:8080/actuator/health
 Include Bearer JWT token string in the header
 
# Technologies used
Use Java 1.8+ 
Use Spring Boot 
Use Maven 
Use JUnit and Mockito/MockMvc 
Logger
Swagger
Jacoco for code coverage
Spring Actuator for server monitoring 
#references
https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html
https://mkyong.com/spring-boot/spring-rest-integration-test-example/