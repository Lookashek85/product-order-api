# Product Order Api
Spring Boot Java simple application serving as base service for products order management

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













