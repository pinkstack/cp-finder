# Lightning fast search query

Help us create a web service, that will support different queries on the COVID-19 test results data. What we miss is an API, that would allow us to query really fast on big, live data set.


## Task description
###  Description
In the current pandemic, counting the number of infected individuals has become a worldwide phenomenon. To make the picture and the state of current affairs visually clearer, we started to implement a visualization platform, which would show data related to infection rate across different demographics (gender, age, country, etc.).


Your challenge is to help us create a (REST or SOAP) service, that will support different queries on the covid-19 test results data.  What is missing is an API, that would allow us to query required results.  And for this, we ask for your help.

##  The development stack requirements
- Java 8+
- JPA

##  Prerequisite instructions
- From the provided CSV file create a relational database and organize your data
- The rest service deals with the database you have created. The CSV file serves only for initial data.
- The data filtering and retrieval should be implemented on the service layer
- The whole solution should be uploaded in a zip file.

## Task

Implement as many functionalities as you manage.
You are expected to write unit and integration tests, 
which demonstrate the performance of the particular task (query) bellow. 
The final solution should require as little configuration as possible. 
Ideally, one would just run the service.

### CRUD Operations

- Implement support for basic CRUD operations on your entities through your API

### Positive test cases by gender

- Create an endpoint that provides number of positive test cases
- Create an endpoint that provides number positive cases grouped by gender
- Create an endpoint that provides number of positive test cases grouped by gender and their current state (quarantine, medical care, hospitalized)

### Positive test cases by country

- Create an endpoint that will provide the number of positive cases by date for all data
- Improve the previous endpoint to include a subset of countries and return the number of total cases for all of the countries
- Improve the previous endpoint  to group the results by country

### Individuals in quarantine

- Create an endpoint to provide the number of individuals currently in the quarantine
- Improve the previous endpoint to provide results grouped by country
- Improve the previous endpoint to support filter the data based on the amount left in quarantine. 
  So the endpoint should include the minimum number of days left in quarantine.

> 14 dni?

### Evaluation criteria
- Size of the solution
- Database layer architecture
- Coding principles used
- Unit and Integration tests with performance measures


## Extras

- COVIDPeople data: https://drive.google.com/file/d/1yM23HHHfwbjvZKW3-xjUSUbPQMEhaUmd/view?usp=sharing
