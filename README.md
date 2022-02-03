# Demo Naturalis API

## Description
Demo build to demonstrate data collections / Cordra client usage.
It has two flavours one the `Digital Object Interface Protocol` and one for the `Rest API`.
Both have three options to run `create`, `update` and `delete`.
They can be configured further by setting the amount of objects to use and how many runs it should do.
Create is limited by the API of Naturalis which return a maximum of 50000 results.
Updated is limited by the amount of objects in the Cordra instance (will not reduce each run as it will only update).
Delete is limited by the amount of objects in the Cordra instance (will reduce each run as it deletes items).

### Create
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

### Update
With update it will first call the Cordra instance and collect the specified amount of objects.
It will then update a field of each object and insert a new created copy to Cordra.
This is just a simple counter for which the midsLevel field is used.
Keep in mind that the logic of the counter and the object modification is included in the timing.

### Delete
For delete it will first call the Cordra instance and collect the ids of the specified amount of objects.
It will then delete each item based on their id.

## Parameter explanation
Parameters should be supplied as environmental arguments.
Application is expected to run as a docker container or kubernetes job.
Running als commandline application will require code changes.

### Application properties
`spring.profiles.active` Feature toggle to determine whether to use `REST` or `DOIP`  
`application.action` Feature toggle to determine whether to use `create`, `update` or `delete`  
`application.number-of-objects` Number of objects to use in a single run, for example `1000000`  
`application.number-of-runs` Number of runs to execute, for example `10`  

### Cordra parameters
`cordra.username` This parameter needs to be the username of a user with sufficient authorization to create objects   
`cordra.password` Password of the user  
`cordra.type` The schema type for the object which should be inserted. Current setup works with `DigitalSpecimen`  

### Cordra Rest parameters
`cordra.rest.host` This paremeters contains the hostname for the Rest client, for example `https://localhost:8443`

### Cordra DOIP parameters
`cordra.doip.doip-port` The DOIP port, for example `9000`  
`cordra.doip.service-id` The DOIP service id for example `test/service`  
`cordra.doip.ip` The DOIP ip address, for example `127.0.0.1`  

### Naturalis parameters
`naturalis.requestsize` Amount of object which will be requested from the Naturalis API (and thus will be inserted in Cordra) for example `100`    
`naturalis.field` Field which will be used in the Naturalis query as condition, for example `kindOfUnit`    
`naturalis.operator` Operator which will be used in the Naturalis query, for example `EQUALS_IC`  
`naturalis.value` Value used in the Naturalis query, for example `nest`  

### Installation instructions

### IDE
Pull the code from Github and open in your IDE.
Fill in the `application.properties` with the parameters described above.
Run the application.

### Docker
Ensure that parameters are either available as environmental variables are added in the `application.properties`.
Build the Dockerfile with `docker build . -t demo-naturalis-api`
Run the container with `docker run demo-naturalis-api`

# Results
We tested the application in two setups:
- Remote cordra (https://nsidr.org/) backed by ES and MongoDB
- Local setup with Docker container with a data directory see: https://www.cordra.org/documentation/configuration/single-instance-deployment.html#docker-instance
All results are based on 10 runs of the application where possible with 10000 objects.
We experienced difficulties with the DOIP so we scaled down to 1000 objects to remote Cordra.
Delete was particularly slow in the remote setup so we scaled down to a 100 objects
See the added csv with the metrics (cordra-performance.csv)
