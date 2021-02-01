# cp-finder

Ultra-fast search and analytics engine purposely built for
[Žejn GROUP - Codemania (TL - Hack) - hackathon, January 2021](https://www.zejn.si/).

Detailed competition requirements and instructions can be found in [INSTRUCTIONS.md](INSTRUCTIONS.md).

## Usage 🚀

### Requirements

The service can run as [fat JAR](https://dzone.com/articles/the-skinny-on-fat-thin-hollow-and-uber)
on top of any modern JRE or via pre-packaged Docker Image.

> 🐇 Although out of the scope of the assigment; this project can easily be compiled with GraalVM to also run as "native-image"; that would further reduce memory footprint and improve boot-up time and possibly performance.

### REST API

#### CRUD

##### Creating a person

```http request
POST http://127.0.0.1:8080/people
Content-Type: application/json

{
  "id": 42,
  "gender": "M",
  "isoCountry": "svn",
  "testResult": "P",
  "intervention": "quarantine",
  "birthDate": "23.06.1953",
  "testDate": "31.12.2020"
}
```

##### Reading a person

```http request
GET http://127.0.0.1:8080/people/42
```

##### Updating a person

```http request
PATCH http://127.0.0.1:8080/people/42
Content-Type: application/json

{
  "gender": "M",
  "isoCountry": "svn",
  "testResult": "N",
  "intervention": "quarantine",
  "birthDate": "23.06.1953",
  "testDate": "31.12.2020"
}
```

##### Deleting a person

```http request
DELETE http://127.0.0.1:8080/people/42
```

#### Analytics

##### Number of positive test cases

```http request
GET http://127.0.0.1:8080/analytics/positive
```

```json
{
  "count": 14
}
```

##### Number positive cases grouped by gender

```http request
GET http://127.0.0.1:8080/analytics/positiveByGender
```

```json
{
  "female": 3,
  "male": 11
}
```

##### Number of positive test cases grouped by the gender, and their current state (quarantine, medical care, hospitalized)

```http request
GET http://127.0.0.1:8080/analytics/positiveByGenderAndState
```

```json
{
  "male": {
    "quarantine": 11
  },
  "female": {
    "hospitalized": 1
  }
}
```

##### Number of positive cases by date for all data

```http request
GET http://127.0.0.1:8080/analytics/positiveByDates
```

```json
{
  "dates": {
    "1.01.2021": 200,
    "5.01.2021": 200,
    "12.01.2021": 600
  }
}
```

## Development 🏗

### Requirements

- Install any modern JDK, although it is suggested to use [SDKMAN](https://sdkman.io/) with OpenJDK (14).

    - Development was done on `openjdk version "14.0.2" 2020-07-14`

- [Install Scala](https://docs.scala-lang.org/getting-started/index.html) with
  [Scala Built Tool (SBT)](https://www.scala-sbt.org/download.html)

    - Scala version used `2.13.4`
    - SBT version used `1.4.6`

### Running 🏃‍

To compile and run the server please use the following SBT commands, that will spawn the server
on [http://127.0.0.1:8080](http://127.0.0.1:8080) and put everything online.

$ sbt run

### Testsuite

To run the full testsuite including integration tests please run

$ sbt test

## Author

- [Oto Brglez](https://github.com/otobrglez) / [@otobrglez](https://twitter.com/otobrglez)
