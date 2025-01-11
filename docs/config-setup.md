### Configuration Setup
Under `drive-guard-app/src/main/resources` create a file called `application-dev.yaml`.
This will be your development configuration file.

Within `application-dev.yaml` you'll want to specify your database, user, and the user's password. Change the port and database name in the url as needed. Eg:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/drive-guard
    username: fred
    password: 1234
```
Note indentation is important for yaml.

You'll likely also want to specify a port for the webserver:
```yaml
server:
  port: 6002
```

Now you have something like this
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/drive-guard
    username: fred
    password: 1234
server:
  port: 6002