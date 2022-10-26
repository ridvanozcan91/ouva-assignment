# Ouva Tehnical Interview Assignment

Thank you for your interest in Ouva. We enjoyed talking to and learning about you. Our team found your background to be applicable to our current work and looking forward to better understanding your technical skill set and if it matches what we need.
To evaluate that, we have a mini assignment for you to complete on your own time.

## Why Take-Home Assignment?

We want to make sure that the people we bring into our team can start contributing from day 1 (with our help, of course). We also know that, your degree, and ability to solve tricky algorithm questions under stressful environments do not accurately reflect your abilities as a Software Engineer.
This particular project is taken from a slice of work that we will ask you to do when you join Ouva. We made sure that it is not something we can use within the product, however, so that we are not asking you to do free work for us. Also, you own your work - we will never keep or use it.

## How Long Should It Take?

We respect your time and know that you may be working full time, interviewing other companies and taking care of your family. We did our best to come up with a project that can be done in ~4 hours.
Read the description below. If you believe the assignment will take longer, let us know before you begin.

We recommend you focus on the core requirements first, then work on any additional features if you have the time. By 4 hours in, please feel free to stop working and explain what refactors / code organization / enhancements you would have made with more time.

> You have 2 week to complete the task. We think that gives you enough time if you need to split the work throughout the week. Let us know if you have a situation that requires you to have more time.

## The Task

Your task, if you accept it, is to write a *SpringBoot* application that consumes and processes a person's heartbeat data via *Apache Kafka*. This backend will then store & expose heartbeat anomalies found for any given patient via *GraphQL*. 
We also expect you to think about what happens if there are multiple instances of the application and how effective it can perform under high heartbeat data frequency with high number of patients.

So the assignment consists of 3 core requirements:

### 1. Consume Heartbeat Messages

The application should be able to consume JSON-formatted heartbeat messages from a Kafka topic named `heartbeat`.
Each message contains 3 fields: `Red_Signal` (heartbeat value), `Time`, and `room_id` (unique identifier for a patient). The message frequency is *0.25* seconds and each message has also a *partition key* set as its room id. 

Here is an example message:

```json
{
    "Red_Signal": "705",
    "Time": "12:54:40.369052",
    "room_id": "4c309b0c-afeb-4240-8465-d5e3798b700a"
}
```

### 2. Find & Store Heartbeat Anomalies

The application should check for anomalies while consuming the heartbeat messages. An anomaly for a patient's heartbeat means just a higher `Red_Signal` (i.e. heartbeat) value than the *mean* `Red_Signal` values consumed for that patient so far. 

Here is an example anomaly:
```js
// Assume that the application received 3 messages for a patient (i.e. room_id) so far and they have these Red_Signal values: 
8, 7, 7

// The mean Red_Signal value for that patient would be (8 + 7 + 7) / 3 = 7.33
// And if the application consumes another message for the same patient with Red_Signal = 8, then this will be an anomaly because it will be higher than the mean value (8 > 7.33)
```

The application should store any necessary data in a Postgres database named `heartbeat_db`. We expect you to design the database tables as you'd see fit and if you use an ORM, you can also use DDL-auto generation to easily create/update the tables.

### 3. Serve Heartbeat Anomalies

The application should be able to expose these anomalies for a given patient (i.e. `room_id`) through a GraphQL endpoint. So the GraphQL query should have a `room_id` argument and should return a list of anomalies detected for that patient.
An anomaly JSON should at least include `redSignal` and `time` values.

## Before You Begin

Before you begin, make sure you have these dependencies installed. You will need them to develop, run and test the application:

* JDK17 or newer
* Maven
* Docker
* Docker Compose

To help limit the time required to complete this assignment, we have provided you with a ready-to-use development setup in this repository.
You can find a `docker-compose.yml` file that contains all the necessary components (Postgres, Apache Kafka, Zookeeper, Ouva Heartbeat Publisher) and `docker-inits` subdirectory that configures Postgres `heartbeat_db` database.
There is also an `assignment` subdirectory which is just a template for the SpringBoot application. It just contains the minimum boilerplate code, configuration and dependencies *without any task-specific business logic*.

```
.
├── assignment // Template for the SpringBoot application.
├── docker-compose.yml // Contains all the necessary components
├── docker-inits // Some config for the database.
└── README.md
```

> You can use any files/directories provided in this repository as they are or modify them in any way you need. They are just here to help you start development right away and, thus, let you focus on what is important.

## Test Your Code

Test your code with your framework of choice and show coverage report. We want to know that you know how to test, but you do not need to have a high coverage. Let us know what you thought was reasonable to demonstrate your testing capability. We will not judge by the amount but your demonstrated thinking.

## Deploy

Save your code base to a public GitHub or GitLab repository with a `Dockerfile`. We will build your application with the `Dockerfile` you provided in your project, and will run your image according to your directions. Do your best to minimize your docker image.

## Documentation

In general, use as much inline comments on your code as possible. If you brought in an entire unchanged code from outside, make sure to document the source. 

* General challenges, and how you overcame them,
* Which additional frameworks, third party libraries you needed and why,
* How did you organize your code and why,
* What coding convention did you follow,
* What are open questions that still needs to be resolved and briefly where would you start to solve them.

## What's Next?

When you submit your code, we will:

* Run your code and make sure it displays anomaly data for any given patient (i.e. `room_id`),
* Review your code and documentation. We focus on the functionality, reliability and maintainability during this step. We also take [12-factor methodology](https://12factor.net/) very seriously.

If you have completed the task, or have a solid discussion at 4 hour mark for what is done and what can be done, we will invite you to meet our team and have another interview where we discuss your code, discuss our projects in technical details. Because we have a project that you demonstrated your capabilities with, we will keep the second meeting less about coding and more about technical discussion.
If our team thinks that your skills are not suitable for the current position, we will give you as clear feedback as possible for why that is.

## If You Have Questions

If you have any questions or concerns, please feel free to mail us any time you want. We love technical discussion and are always happy to help to resolve any issue.

> Happy coding! :beers: We think we are a fun team to work with, and hope to see you soon!
