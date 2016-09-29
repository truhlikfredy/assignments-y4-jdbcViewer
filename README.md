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

* Modularity, using Interface xxxxxxxxxxxxxxxxxxxxxxx finish
    
* Separation of concenrs, all the database configuration is removed from code and kept in separate **config.properties** file.

* Externalized Strings, all texts which comunicate with enduser are exported into **messagess.properties** allows faster proof reading, or easy multilangual support.

* Embedded resources, icons and graphics are embedded into jar as internal resources (only 1 jar needed to redistribute).

* Handcrafted GUI, avoided WindowBuilder tool on purpose, because for this type of simple GUI a WindowBuilder is overkill tool and polutes project with a lot of garbage and generated code, which can't be touched or refactored without breaking things. By hand tailoring the GUI the source code is smaller, cleaner and free to refactoring and future changes.

* Using libary [faker](https://github.com/blocoio/faker), to generate fake employees.

<kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>C</kbd>:

code metrics

test coverage

  heading
  ===================
  
