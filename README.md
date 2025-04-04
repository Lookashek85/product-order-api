# Product Order Api
Spring Boot Java simple application serving as base service for products order management

## Running instructions 

Prerequisites - docker compose 

Application can be started locally using docker-compos in root project of `product-order-service`.
Clone or download this repository, navigate to `product-order-service` -> `docker-compose` and type in terminal 
`% docker-compose up --build ` to build docker image then `% docker-compose up -d` to run. 
To shut down, type `% docker-compose down` 
Application can be acceseed on localhost:8080 , to see the API as Swagger UI, 
go to http://localhost:8080/swagger-ui.html or if you need JSON with API, go to  http://localhost:8080/api-docs 



## Developer Journal
### Stage 1 - Discovery and analysis  
Technical instructions about tech stack required and final outcome were clear at the beginning, 
when reading requirements for web service,  I noticed business relationship such that Order will have Product/s 
then went through rest of requirements and decided that I’ll go for DDD approach, it says tiny service, so 
I will build one service that should be easy to decompose into smaller services... 

I decided to take below assumptions based on spec:
- product inventory is not mentioned, just pricing so - assuming no stock control required.
- Product price without currencies  is mentioned so one currency for now.
- No other product details such as weight, size, type etc is not mentioned - assuming simple product domain model.
- Orders management is not mentioned - assuming only add and get operations for orders have to be implemented.
- No global state management - assuming stateless api, no Http Session support.
- Assuming basic error handling 
- Assuming no security required
- Assuming no build pipeline in repository
- Assuming no fancy styling of this README ;)  

After these, I was ready to plan my domain model, came up with:

- Product(productId:ProductId, name: ProductName, price: Price)

- Order(orderId: OrderId, products : Product[] , buyerEmail: Email, ), orderTimestamp: OrderTimestamp)

- Buyer(buyerId: BuyerId, email: Email)

At this point I've had a solid idea of what I am going to do : 
build Rest API using spring boot and mysql as storage, I will use docker compose and since I'm a big fan of kotlin coroutines 
and not such a big fun of Java Rx , I decided I will try to utilise Virtual Threads,
I also realised that it's been a good while since
I've used Java, it's been mainly Kotlin in past 2 years, 
hence I need to focus more on working code rather than syntactic sugars, etc.

I setup required dependencies and added first class to project... 

### Stage 2 - Create domain 

I will create required entities and service definitions that will form my domain. 
Later I will implement domain services and write unit tests to verify my design.
At this stage I should be able to build the app but it won't start yet...

### Stage 3 - Implement domain services 
Development went ok, I made few minor changes to my model, I tested domain code and found out there is an
issue with resolving order dates, I will look into it tomorrow. I'm done for today, Java switch was a bit harder 
than I thought, I reminded to myself why I loved Kotlin so much when first introduced to it ;)

### Day 2 - Stage 4 - Add persistence, go onion ;) 
First, fixed dates bug, I didn't round up days properly and was comparing by millis accuracy :/ 
Since my domain tests were passing all business requirements, I decided its time to add persistence layer, 
say hello to a good old friend MySQL since recently Postgres has been my SQL buddy.
Started with refactoring domain to be more complaint with onion approach, lets see how it goes...

## Stage 4 retro 
I haven't completely finished what I planned, even though I have working app, when running the build, I get some weird 
hikari connections timeout, will need to look into test containers to fix this. 
I've stomped into serious issues when setting up Wildfly for migrations, this took definitely too log, I should have choosen Postgres
because majority of issues with persistence layer was caused by my lack of mysql knowledge... 


### Day 3 - http layer, first integration tests
I didn't manage to complete everything, I haven't got that much time as expected, managed to add http layer and tests for it
but noticed some problems with repo tests, will look into them tomorrow, its late and I'm tired ;),
Tomorrow is deadline so I need to get the app to work for required features... 
In worst case scenario, I woudld expect app to start up and expose API, maybe it wont be fully working but lets see....

### Day 4 - Final 
Application is working, I can cover every test case from spec via Postman however I noticed automated tests can randomly
fail sometimes so I have to fix test containers settings. 

I still need to add open api and dockerize my app, otherwise app is not finished.. :/ 

TODO list  (Tech debt)
- Tune tests containers timeout settings to reduce build time
- Investigate and get rid of flaky tests 
- more tests to cover some edge cases particularly around time ranges -> this hasn't been looked well enough yet

In overall, I would rate work as 75% at the moment... 

### update
I managed to add swagger open API and dockerize the application :) Kinda done but not yet, lets talk... ? 






