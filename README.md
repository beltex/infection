infection
====

A simulator for studying infection algorithms for distributed agents with
variable graph topologies.

![alt text](http://beltex.github.io/infection/vis-1.gif)


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
- [Google Guava](https://code.google.com/p/guava-libraries/) (16.0.1)
- [Google Gson](https://code.google.com/p/google-gson/) (2.2.4)
- [JUnit](http://junit.org) (4.11)
- [Gradle](http://www.gradle.org) (2.0)


## Setup & Use

**Download Project**

```bash
git clone https://github.com/beltex/infection.git
cd infection/
```

**Gradle**

Gradle is used for build automation. It can be installed via Homebrew for
OS X users.

```bash
# Install Gradle
brew update
brew install gradle

# Update Gradle version (if already installed)
brew update
brew upgrade gradle
```

For more see:

- http://www.gradle.org/installation

To use Gradle (must be inside the project directory):

```bash
# To build and run tests
# Test results can be found in - build/reports/tests/index.html
gradle build

# To run project (runs src/main/java/client/ClientVis.java)
gradle run

# To run a specific client class
# This assumes that the class is located inside the client package
# (src/main/java/client)
gradle -Pclient=<NAME_OF_CLASS> run

# To create Eclipse project files
gradle eclipse

# To create Javadoc
# Docs found in - build/docs/javadoc/index.html
gradle javadoc
```

**Creating Your Own Simulation**

Sample client classes can be found inside `src/main/java/client/`


## Logs

Logs from the simulation can be found in the `logs` directory which is
created at run time. Each simulation run creates it's own timestamped
directory inside containing logs, simulation data in JSON format, and charts.

- Logging is only really useful for single simulation runs, adding of new feautres, etc
- When running large tests, logs are both repetitive and carry a performance and space overhead.

**metadata.json**

The metadata file contains overview information about the completed simulation.

```json
{
  "date": "Jul 14, 2014 11:15:03 PM",
  "description": "Description about the test",
  "duration": "9 hour, 43 min, 32 sec",
  "graphType": "CUSTOM",
  "nodeSelection": "WEIGHTED",
  "agentDistribution": "SINGLE",
  "numNodes": 2,
  "interactProbability": "50.0%",
  "traversalProbability": "50.0%",
  "numAgents": {
    "lowerBound": {
      "endpoint": 1000
    },
    "upperBound": {
      "endpoint": 10001
    }
  },
  "termA": 4,
  "termB": 0,
  "maxTimeSteps": 600000,
  "runsPerPopulation": 10,
  "totalRuns": 90010,
  "avg_infectionLevel": "87.97%",
  "avg_leaderError": "10.14%"
}
```

**data.json**

- Data file is optional - off by default
- Have to turn it on via Simulator class method `saveSimData()`
- Contains the data points used in the markers chart
- The JSON is minified (not pretty printed as the files get large quickly)

```json
[
  {
    "numAgents": 1000,
    "infections": 1000,
    "electionCompleteCount": 5,
    "interactions": 25086,
    "traversals": 74914,
    "infectionCompleteStep": 26320,
    "leaderElectionCompleteStep": 95610,
    "allElectionCompleteStep": 0,
    "infectionCompleteInteractions": 6509,
    "leaderElectionCompleteInteractions": 23981,
    "allElectionCompleteInteractions": 0,
    "stepInfectionsMap": {}
  },
  {
    "numAgents": 1001,
    "infections": 1001,
    "electionCompleteCount": 0,
    "interactions": 24967,
    "traversals": 75033,
    "infectionCompleteStep": 31285,
    "leaderElectionCompleteStep": 0,
    "allElectionCompleteStep": 0,
    "infectionCompleteInteractions": 7839,
    "leaderElectionCompleteInteractions": 0,
    "allElectionCompleteInteractions": 0,
    "stepInfectionsMap": {}
  },
  ....
]
```

**Markers Chart - chart.png**

![alt text](http://beltex.github.io/infection/chart-1.png)


### References

- [_Infection algorithms for distributed agents_](http://beltex.github.io/infection/paper-1.pdf)
- [_Efficient leader election among numbered agents_](http://beltex.github.io/infection/paper-2.pdf)


### License

This project is under the **GNU General Public License v2.0**.
