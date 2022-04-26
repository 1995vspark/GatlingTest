**About Gatling:**

Gatling is an open source load testing framework based on Scala and is a very powerful tool for Load Testing.

**Software Versions Used**
1. JDK8
2. Maven Compiler version : 1.8
3. Gatling Version : 3.6.1
4. Gatling Maven plugin versions : 3.1.2

**Application Under Test:**

We will test performance of Linio.cl website.

**Performance Test scenarios:**

Scenario 1: 
1. User will open Linio.cl and browse the page.
2. User will select a random product and open the product's page

Scenario 2:
1. User will open Linio.cl and browse the page.
2. User will click on search bar on Linio.cl homepage and search for random string.

**Load Pattern:**

Load pattern can be decided by the user running the test using command line arguments.

Default Load Pattern for both scenarios : 10 users with 10 seconds of rampup time for a duration of 900 seconds.

Linio.cl homepage is included in both scenarios since the homepage is expected to have highest load compared to other pages.

**Prerequisites to run the test:**

1. JDK 8
2. Maven

**Steps to run the test**

1. Clone the repo from 'https://github.com/GondaliaKaran/falabella-gatling.git'.
2. cd falabella-gatling
3. mvn clean gatling:test -Dgatling.simulationClass=Simulations.LinioHomePage -DUSERS=10 -DRAMP_DURATION=1 -DDURATION=60

*User can define their own scenario by changing the values for -DUSERS , -DRAMP_DURATION , -DDURATION.*

