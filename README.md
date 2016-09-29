JDBC Employee database Viewer - CA 1
========================
 by Anton Krug 20062210

**Features:**

* Proper use of Java8
 
    * Utilization of **streams** and **lambda expressions**, to iterate all Components and disable/enable them in one line 
    `Arrays.asList(prevnext.getComponents()).stream().forEach((item)->item.setEnabled(areEnabled));`
    
    * Method referencing and **runnables**. To handle differently each mouse event action it's possible quickly reference a regular method as a Runnable argument. Removes all the syntax sugar for creating Functions, Runnables or Threads or other workarounds to handle simple actions. 
    `this::actionAddEmployee()`
    This syntax is very trivial and yet very powerful (removes lot of sugar). This allows very simplistic eventListener and lowers cyclomatic complexity (more below).

* Separation of concerns, all the database configuration is removed from code and kept in separate **config.properties** file.

* Separation of concerns and Modularity, using Interface to separate the database access implementations from concept. AddEmployee in the UI should be independ of the specific implementation of the database. Or given database access implementation shouldn't dependant on specific GUI. All UIs implementation and DAOs depend on 1 interface and 1 class, Employee which is entity containting the values and the DataAccess interface which is used to describle the interface for the implementations. This allows for the UI have many different DAOs implementations and no need to changed anything in the UI. And it's the same other direction as well, the specific DAO can be used in many different UIs. This allows to have console based UI, or just better JUnit tests. And it allows to swap implementations in runtime if needed:
`DataAccess dao = new DataAccessJdbc();`
Because the dao is DataAccess we don't care with which specific implementation we initialised it at the moment, in the example it was Jdbc but for the UI there wouldn't any difference if there would be different implementation. In the diagram below it's show how the interface is used:
![uml diagram of the interface](https://raw.githubusercontent.com/truhlikfredy/assignments-y4-jdbcViewer/master/images/uml.png?token=ABC5if-GPFhyJOJ-UsvbRhHRwsFTlCK3ks5X9l2bwA%3D%3D)

    
* Externalized Strings, all texts which comunicate with enduser are exported into **messagess.properties** allows faster proof reading, or easy multilangual support.

* Embedded resources, icons and graphics are embedded into jar as internal resources (only 1 jar needed to redistribute).

* Handcrafted GUI, avoided WindowBuilder tool on purpose, because for this type of simple GUI a WindowBuilder is overkill tool and polutes project with a lot of garbage and generated code, which can't be touched or refactored without breaking things. By hand tailoring the GUI the source code is smaller, cleaner and free to refactoring and future changes.

* Using libary [faker](https://github.com/blocoio/faker), to generate fake employees.

<kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>C</kbd>:

code metrics

test coverage

  heading
  ===================
  
