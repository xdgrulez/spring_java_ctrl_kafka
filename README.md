# Spring javactrl-kafka

Minimal test for using javactrl-kafka in conjunction with Spring Boot.

Sets up a minimal workflow consisting of two steps.

# Setup

1. Set up a local Kafka cluster e.g. using the Confluent Platform Quick Start `docker-compose.yml` (https://docs.confluent.io/platform/current/get-started/platform-quickstart.html).
2. Install kafi (https://github.com/xdgrulez/kafi).
3. Run the Python script `init.py` to (re-)create the three Kafka topics used by the app.
4. Run the app (`./gradlew bootRun`).
5. Run the Python script `produce.py` to produce some messages to the `workflow-resume` topic to execute the workflow.
6. Run the Python script `list.py` to see the lengths of the topics used by the app, or
7. Run the Python script `result.py` to show the content of the result topic.

# REST endpoints

The app has two REST GET endpoints:

* `localhost:8080/workflows` returns the list of workflow IDs (`test.py` creates IDs based on timestamps)
* `localhost:8080/workflows/{id}` returns the workflow with ID `id`

# Issues

In some environments (e.g. IntelliJ + Gradle Runner; it works if you use IntelliJ + IntelliJ IDEA Runner) the app does not work and returns `not available thread {ID}` after the second message was sent in `produce.py`.