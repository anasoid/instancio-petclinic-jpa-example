# instancio-petclinic-jpa-sample

This project is an example to generate a fake data based on instancio in Spring boot application with JPA.

The application example used is the popular Spring pet
clinic [spring pet clinic](https://github.com/spring-projects/spring-petclinic)

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

### Description of application

#### Applications

1. There is two spring boot application present in this repos:
    1. __InstancioPetClinicJpaExampleApplication:__ Console application that generate data in database, should be used with
       mysql or other server database to be visible.
    1. __PetClinicWithDataApplication:__ Star the original spring pet clinic and generated data in h2 in memory database.

#### Code

The code to generate petclinic data is presents in package "org.anasoid.instancio.petclinic.jpa.example.petclinic" other
class form "org.anasoid.instancio.petclinic.jpa.example.core" can be resused in different applications.

#### Generated Data

The config file by entity is present in [entity-config.yaml](src/main/resources/entity-config.yaml)


| Entity    | Remarque                                                                                |   
|:----------|:----------------------------------------------------------------------------------------|
| Vet       | generate 10 vets, with one or two specialities                                          |  
| Owner     | generate minimum 100 owners, with one or two pets                                       |  
| Specialty | generate 5 specialities based on csv file [specialty.csv](src/main/resources/data/specialty.csv) |   
| Pet       | generate one or two pets by owners                                                      |  
| Pettype   | generate 5 pettype based on csv file [pettype.csv](src/main/    resources/data/pettype.csv)          |    
| Visit     | generate zero to fives visits by pets                                                   |   
