# instancio-petclinic-jpa-sample

This project is an example to generate a fake data based on instancio in Spring boot application with JPA.

The application example used is the popular Spring pet clinic [spring pet clinic](https://github.com/spring-projects/spring-petclinic)

## Generated data

### Steps for demo
1. checkout spring pet clinic application from fork repos
```bash
git clone https://github.com/anasoid/spring-petclinic.git
```

2. Install spring petclinic in local maven repo
```bash
cd spring-petclinic
./mvnw package
./mvnw install:install-file -Dfile=./target/spring-petclinic-3.4.0-SNAPSHOT.jar.original -DgroupId=org.springframework.samples -DartifactId=spring-petclinic -Dversion=3.4.0-SNAPSHOT -Dpackaging=jar
```

3. Checkout this repo in different folder
```bash
git clone https://github.com/anasoid/instancio-petclinic-jpa-example.git
```

4. Run sring boot petclinic app
```bash
./gradlew bootRun
```

5. Visit url http://localhost:8080