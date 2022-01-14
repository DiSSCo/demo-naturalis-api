# Demo Naturalis API

## Description
Demo build to demonstrate data collections / Cordra client usage.
This commandline runner application aims to request data from the naturalis API based on a condition and insert it into Cordra.
- The first step is to gather data from Naturalis. 
This will be done using size and a single condition (multiple conditions not supported).
For example `{"size":100,"conditions":[{"field":"kindOfUnit","operator":"EQUALS_IC","value":"nest"}]}` will retrieve 100 specimen that have as kindOfUnit the value nest.
All fields in Naturalis can be used for the condition. 
Size is restricted to 50.000 records, which for the DiSSCo prepare phase should be enough to get a good sample.
To test conditons it is possible to use https://api.biodiversitydata.nl/scratchpad/.
When the complete Naturalis collection needs to be requested a different API call should be implemented.
If images are part of the object these will be gathered as well.
- Second step is mapping the object from Naturalis (DarwinCore?) to OpenDS objects.
This is purely a mapping from one domain object to another.
The only logic present is to retrieve the ROR code for the institution.
This is done with a request to the API from ror based on the `affiliation` query parameter.
- Last is to insert the data into the Cordra instance
For this the Cordra client is used.
The CordraObject will be created (current type is DigitalSpecimen) and added to the Cordra instance.
The option dryRun is te ensure that we can test the application without creating multiple similar objects in Cordra.

## Parameter explanation
Parameters should be supplied as environmental arguments.
Application is expected to run as a docker container or kubernetes job.
Running als commandline application will require code changes.

### Cordra parameters
`cordra.host` This parameter needs the hostname of the Cordra server, for example `https://nsidr.org/`
`cordra.username` This parameter needs to be the username of a user with sufficient authorization to create objects 
`cordra.password` Password of the user
`cordra.dryrun` With dryrun on `true` object will not be created in Cordra but the complete flow can be tested
`cordra.type` The schema type for the object which should be inserted. Current setup works with `DigitalSpecimen`

`naturalis.requestsize` Amount of object which will be requested from the Naturalis API (and thus will be inserted in Cordra) for example `100`
`naturalis.field` Field which will be used in the Naturalis query as condition, for example `kindOfUnit`
`naturalis.operator` Operator which will be used in the Naturalis query, for example `EQUALS_IC`
`naturalis.value` Value used in the Naturalis query, for example `nest`

## Installation instructions

### IDE
Pull the code from Github and open in your IDE.
Fill in the `application.properties` with the parameters described above.
Run the application.

### Docker
Ensure that parameters are either available as environmental variables are added in the `application.properties`.
Build the Dockerfile with `docker build . -t demo-naturalis-api`
Run the container with `docker run demo-naturalis-api`