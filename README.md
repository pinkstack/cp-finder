# cp-finder

Ultra-fast search and analytics engine purposely built for
[Žejn GROUP](https://www.zejn.si/)

- [Codemania (TL - Hack) - hackathon, January 2021](https://tl-hack.incubatehub.com/p/codemania-tl-hack).

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
  "count": 5457
}
```

##### Number positive cases grouped by gender

```http request
GET http://127.0.0.1:8080/analytics/positiveByGender
```

```json
{
  "female": 2779,
  "male": 2678
}
```

##### Number of positive test cases grouped by the gender, and their current state (quarantine, medical care, hospitalized)

```http request
GET http://127.0.0.1:8080/analytics/positiveByGenderAndState
```

```json
{
  "female": {
    "hospitalized": 919,
    "medical care": 865,
    "quarantine": 995
  },
  "male": {
    "hospitalized": 898,
    "medical care": 916,
    "quarantine": 864
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
    "8.12.2020": 20,
    "9.01.2021": 12,
    "9.03.2020": 14,
    "9.04.2020": 18,
    "9.05.2020": 20,
    "9.06.2020": 14,
    "9.07.2020": 15,
    "9.08.2020": 14,
    "9.09.2020": 16,
    "9.10.2020": 19,
    "9.11.2020": 12,
    "9.12.2020": 14
    ...
```

##### Number of positive cases by date\*

This endpoint is to be used for following requirements:

- The number of positive cases by date for all data.
- Filtering subset of countries and return the number of total cases for all the countries
- Endpoint groups results per country

```http request
GET http://127.0.0.1:8080/analytics/positiveByCountryAndDates
```

```json
{
  "countries": {
    "aus": {
      "1.03.2020": 3,
      "2.03.2020": 3,
      "3.03.2020": 3,
      "4.03.2020": 9,
      "5.03.2020": 5,
      "6.03.2020": 2,
      "7.03.2020": 9,
      "8.03.2020": 4,
      "9.03.2020": 2,
      "10.03.2020": 6,
      "11.03.2020": 5,
      "12.03.2020": 6,
      "13.03.2020": 1,
      ...
```

With the help of `country` query parameters the results can be further filtered.

```http request
GET http://127.0.0.1:8080/analytics/positiveByCountryAndDates?country=svn&country=aus
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

### Tooling

To populate the service with seed data from CSV the script [bin/feed-csv.rb](bin/feed-csv.rb) can be used like so:

```bash
./bin/feed-csv.rb data/covidPeople.csv
```

## Author

- [Oto Brglez](https://github.com/otobrglez) / [@otobrglez](https://twitter.com/otobrglez)
