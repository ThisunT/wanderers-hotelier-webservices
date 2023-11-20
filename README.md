# wanderers-hotelier-webservices
Webservices platform of Wanderers

To launch the application, execute `docker-compose up` in the root directory of the repository. If the Docker Gradle build fails, a pre-built artifact has been provided. Please follow the instructions in the Dockerfile to make the necessary modifications and retry.

The OpenAPI Spec is located at `/src/main/resources/static/hotelier-webservices-v1.yaml`. This specification was used to generate API interfaces and models. Once the server is running it can be visualized at [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/).

A sample postman collection can be found out at `/postman-collection/wanderers-hotelier-webservice.postman_collection.json`. 

****Introduced Validation Scenarios****
* **Hotelier-Id**: Introduced to identify a hotelier. Similar to a bearer token, it is required in the header for accommodation creation, update, and deletion. Only the original hotelier can manipulate their own accommodation resources, others can only view them.
* **Customer**: Introduced to represent the actor submitting a booking. The customer should include the token (**Is-Valid-Token**) in the header as a representation for the authenticity.

As per the database schema at `psql/init.sql` two tables exist for hotelier and customer, yet no endpoints have been exposed to create the entities. They are expected to be created using SQL.

**Caching strategy**:
A cache has been implemented to enhance the retrieval of accommodations. It features a TTL of 15 seconds, meaning that the response from an accommodation fetch will remain unchanged for 15 seconds, even if the underlying state has been modified during that time.

**Note** : 
Main branch consists of the Java code, a separate branch(kotlin-migration) was created and was carried out partially and under migration.