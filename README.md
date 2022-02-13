<h1 align="center">CodeCharacter Server 2022</h1>

<p align="center">
  <a href="https://github.com/delta/codecharacter-server-2022/actions/workflows/ci.yml">
    <img src="https://github.com/delta/codecharacter-server-2022/actions/workflows/ci.yml/badge.svg"/>
  </a>
  <a href="https://github.com/delta/codecharacter-server-2022/actions/workflows/docs.yml">
    <img src="https://github.com/delta/codecharacter-server-2022/actions/workflows/docs.yml/badge.svg"/>
  </a>
  <a href="https://codecov.io/gh/delta/codecharacter-server-2022">
    <img src="https://codecov.io/gh/delta/codecharacter-server-2022/branch/main/graph/badge.svg?token=DW315MJFHY"/>
  </a>
</p>

### Prerequisites
1. JDK 17 (Ubuntu: install the `openjdk-17-jdk` package)
2. [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/#section=linux)
3. Docker

### Setup

1. Clone the repo and open in IntelliJ Ultimate.
2. Press `Ctrl+Alt+Shift+S` and make sure the JDK version is 17.
3. Press `Ctrl+Alt+S` and go to `Build, Execution, Deployment -> Docker` and make sure docker is configured correctly/
4. Copy server/src/main/resources/application.example.yml to server/src/main/resources/application.yml.
5. The run configurations will be available in the top bar:
   ![Run Configurations](https://i.imgur.com/pO2SrPd.png)

### Run Configurations

1. DB & RabbitMQ: For starting the MongoDB and RabbitMQ containers.
2. Dev: To start the docker development server.
3. Production: To start the docker production server.
4. Tests: To run the tests.
5. Format: To format the project.
6. Assemble: To assemble the project binary.
7. Server: To start the server. (Might require DB & RabbitMQ to be running)
