## SkyRoof Spring Application
A client wants to monitor this construction company projects and the tasks that are needed for the project to be completed. 
Each task can have a state (NOT STARTED, IN PROGRESS, COMPLETED). 
Each project will have the attributes: Id, Title, Description, Creation Date 

Each task will have the attributes: Title, Description, State, Creation Date, Start Date, Completed Date.

A project can have many tasks, a task can only be assinged to a single project. A task cannot be assigned to a different project upon creation.
The system should be able to calculate a project state:
- NOT STARTED when all tasks are in NOT STARTED state
- IN PROGRESS when at least one task is in status IN PROGRESS
- COMPLETED when all tasks are in status COMPLETED

A project can be deleted when there are no tasks assigned on it.
A task can be deleted only when it is in state NOT STARTED.


### Requirements
1. Implement the task JPA entity and add the 1-* constraint between project-task
2. Enchance the REST api to perform all crud operations on project and task entities.
3. Implement the business validations and state transition on entities. A user friendly message must be returned.
4. Currently, the system is connected to an on-memory database. Connect with an sql database of your choice (postgres, mysql, oracle etc) and provide the DDL script that creates the schema.
5. Add logging on important functionality (new project/task, status change etc)
6. Perform unit tests on the business functionality and integration tests on the REST api.
 
### TODO
1. Containerize the application (Docker file jar + sql db)
2. Apply user access security per client with roles (Read, Write). Use OAth2, JWT implementation, Spring security or any other security framework of your choice.

### How to start

```{}
   java -jar skyroof-1.0.0.0-SNAPSHOT.jar
```










