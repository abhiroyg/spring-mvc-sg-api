# spring-mvc-sg-api
Trying my hands for first time at Spring MVC to create REST-ful APIs using Societe Generale's sample data.

## Running Locally

### Dependencies

```
1. Java 1.8
2. Gradle 2.0
3. MySQL
```

### Running

```
$ git clone https://github.com/abhiroyg/spring-mvc-sg-api.git
$ cd spring-mvc-sg-api
$ ./gradlew build
$ java -jar build/libs/societe-generale-api-0.1.0.jar
```

Create database `societegenerale` in MySQL.
Use `create_table.sql` file to create the table `SGMembers`.
Run `MemberStoring.java` to put data into the table.

Open `http://localhost:8080` to start using it.

### End-points

```
GET /greetings
GET /count
GET /search?query=
GET /search?status=<>&id=<>&race=<>&weight=<>&height=<>
```

Query parameters are optional in the last two APIs. In the last API, any one (or more than one) can be mentioned.

### Heroku deployment

Sample heroku api is in: https://sheltered-inlet-8466.herokuapp.com
Due to lack of funds, could not get MySQL db.

### TODO
- Unit tests
- Support start and end values for `id`, `height`, `weight` and `is_veg`.
- Documentation
- Use credit card and verify profile to install MySQL (or) change code to use PostgreSQL.
- Create good frontend.
