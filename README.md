# React + Spring Boot API for account management

This project is an example of a react front-end application that consumes a rest api built with Java Spring Boot.

Features:

* User registration
* User login
* User logout
* Profile update
* User deletion

## Motivation

My main goal with this project is to learn about Java Spring Boot and implement authentication using OAuth 2.0 through Firebase

## React App

![app login page](./docs/login-page-app.png)

### Setup

The app was developed with Node.js 20.10.0

1. Clone the repository
2. Install dependencies with `npm install`
3. Run the app with `npm run dev`

Don't forget to set the environment variables in a `.env` file. Use the `.env.example` file as a template.

## Spring Boot Web API

### Setup

The API was developed with Java 17 and Spring Boot 3.2.4

1. Clone the repository
2. Open the project with your favorite IDE, if posible IntelliJ IDEA
3. Resolve dependencies with Maven
4. Run the application through the IDE

Don't forget to set the environment variables in a `.env` file. Use the `.env.example` file as a template.

For IntelliJ IDEA there is a `.run` folder with the configuration to run the application or tests. Install the plugin [EnvFile](https://plugins.jetbrains.com/plugin/7861-envfile) to load the environment variables from the `.env` file.


If you plan to use the Firebase emulator you need to create a `.firebaserc` file inside the root of the java project with the following content:

```json
{
  "projects": {
    "default": "<project-name>"
  }
}
```

To avoid any issues, it is recommended that the name of the project is the same as the one mentioned in the Google credentials environment variable.