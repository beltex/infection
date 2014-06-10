infection
====

A simulator for studying infection algorithms for distributed agents with
variable graph topologies.

This project is under the **MIT License**. See ```LICENSE``` file.

### Problem

You want to send _n_ robots (agents) to Mars. For certain tasks, synchronization
among the robots is needed, and this often requires a designated leader. How do
these distributed autonomous robots go about doing this? The number which will
survive the trip, or how long any will last on the surface of the planet is not
clear, thus hardcoding a leader before hand is not reliable. As a result the
robots need a means to choose a leader autonomously and efficiently. This is
known as **leader election**, a problem in distributed computing. It can also be
viewed as, for example, an infection spreading through a population, like a
zombie apocalypse.

### Background

- 4th year CS project at York University supervised by professors
  **Patrick Dymond** & **Michael Jenkin**

This work is based on the paper, [_Infection algorithms for distributed
agents_](http://beltex.github.io/infection/paper-1.pdf), by
**Rahul Chaturvedi**, **Patrick Dymond**, and **Michael Jenkin**. In the paper,
infection algorithms for leader election were presented for a collection of
numbered agents with the ability to determine probabilistically when election
was completed.

We extend the work by studying leader election using these infection algorithms,
but now with variable graph topologies. To do so, a simulator is needed to test
and experiment.

### Tech Stack

- Java (7)
- [GraphStream](http://graphstream-project.org) (1.2)
- [tinylog](http://www.tinylog.org) (0.9.1)
- Google Guava (16.0.1)
- JUnit (4.11)
- Gradle (1.10)

### Setup & Use

**Gradle**

```bash
# To build and run tests
# Test results can be found in - build/reports/tests/index.html
gradle build

# To run project (runs src/main/java/client/SampleClient.java)
gradle run

# To create Eclipse project files
gradle eclipse

# To create Javadoc
# Docs found in - build/docs/javadoc/index.html
gradle javadoc
```

**Creating Your Own Simulation**

Sample client classes can be found inside `src/main/java/client/`

### References

- [_Infection algorithms for distributed agents_](http://beltex.github.io/infection/paper-1.pdf)
- [_Efficient leader election among numbered agents_](http://beltex.github.io/infection/paper-2.pdf)
