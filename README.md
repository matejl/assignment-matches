# Assignment - matches

This repository contains a solution for matches assignment.

## Configuring
The database credentials are already set up for running through the `docker compose`. If you want to configure it to
use your own Postgres data source, you can specify the credentials in `src/main/java/resources/db.properties`.
The loading of those variables is not fully extensible or production-ready yet, as they should be injected from other
sources like environment variables, command line parameters or external parameter store provider.

## Building
To build the project, run
```bash
$ ./gradlew :build
```

## Running
If you want to run it using the provided defaults, you can simply start the database using
```bash
$ docker compose up -d
```
After the project is built, you can run the jar. Additionally, source file needs to be provided as an argument.
```bash
$ java -jar ./build/libs/assignment-matches-1.0.jar <path_to_fo_random.txt>
```
