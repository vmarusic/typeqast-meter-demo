# Meter readings interview assignment
This is a demo project for Typeqast job interview purposes. 
Check the provided *Assignment* file for more details

## Instructions
App build as a self-contained web-server app. It can be run by either:

* Using Spring Boot Maven Run plugin i.e. mvn spring-boot:run
* Building a 'Fat Jar' via mvn spring-boot:repackage and running it via java -jar
* pre-built Jar is provided in the /bin folder

App usage is documented via Swagger interface located at 
*hostname:port/swagger-ui.html* (assuming app is run locally this would resolve to *localhost:8080/swagger-ui.html*)

**Note** that input data for ratios/readings can be provided as either the CSV or JSON:

### CSV

```
JAN,B,0.1
FEB,B,0.1
MAR,B,0.1
APR,B,0.1
MAY,B,0.1
JUN,B,0.05
JUL,B,0.05
AUG,B,0.05
SEP,B,0.05
OCT,B,0.1
NOV,B,0.1
DEC,B,0.1
```

### JSON
```
[
      {
        "month": "DEC",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "JUL",
        "profileName": "B",
        "ratio": 0.05
      },
      {
        "month": "JUN",
        "profileName": "B",
        "ratio": 0.05
      },
      {
        "month": "APR",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "FEB",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "MAY",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "AUG",
        "profileName": "B",
        "ratio": 0.05
      },
      {
        "month": "NOV",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "JAN",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "SEP",
        "profileName": "B",
        "ratio": 0.05
      },
      {
        "month": "MAR",
        "profileName": "B",
        "ratio": 0.1
      },
      {
        "month": "OCT",
        "profileName": "B",
        "ratio": 0.1
      }
    ]
```
