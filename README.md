# ORDER MANAGER APPLICATION - JAKARTA EE 9.1

API where users can create and manage orders. Items can be ordered and orders are automatically fulfilled as soon as the item stock allows it.

<img width="1307" alt="Screenshot 2024-09-09 at 1 46 39 AM" src="https://github.com/user-attachments/assets/e1666e41-caa2-4469-a78d-866a236184ce">

---
## Specifications or Functionalities

The system should be able to provide the following features:

- [x] create, read, update and delete and list all entities;
- [x] when an order is created, it should try to satisfy it with the current stock.;
- [x] when a stock movement is created, the system should try to attribute it to an order that isn't complete;
- [x] when an order is complete, send a notification by email to the user who created it;
- [x] trace the list of stock movements that were used to complete the order, and vice versa;
- [x] show the current completion of each order;
- [x] write a log file with orders completed, stock movements, emails sent, and errors.

---

---
## Additional Functionalities

- [x] junit and mockito - unit and integration test;
- [x] Docker - containerization - create application image and pull all dependent containers - postgres, pgadmin, start all with docker compose;
- [ ] customizer exception to protect application default logs with sensitive information;
- [ ] security - protect all endpoints - using spring security to authentication and authorization;
- [ ] monitoring - integration logs application with → Logstash → elasticksearch → kibana;
- [ ] CI/CD - using jenkins to automatize all the main processes;
- [ ] deploy application;

---
## CLASS DIAGRAM
Expand Class Diagram below to see full Diagram

<details>
<summary>Class Diagram - Mermeid plugin</summary>

```mermaid
---
title: Class Diagram
---
classDiagram
    Item -->"1..n" Stock
    Item -->"1..n" Order
    User -->"1..n" Order
    
        
    class User{
        -Long id
        -String name
        -String email
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        
        +createUser()
        +getUserById()
    }
    
    class Item{
        -Long id
        -String name
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        
        +createItem()
    }

    class Order{
        -Long id
        -LocalDateTime creationDate
        -Item item
        -int quantity
        -int fulfilledQuantity
        -User user
        
        +createOrder()
        -trySatisfyOrder()
        -orderCompleted()
        -sendOrderCompletionEmail()
    }

    class Stock{
        -Long id
        -LocalDateTime creationDate
        -Item item
        -int quantity
        
        +createStock()
        +reduceStockQuantity(Item item, int quantity)
        -trySatisfyIncompleteOrder()
    }

```
</details>

---
## Authors

---
Fernando Piedade


## Installation

To use this project, only need to make sure those tools below are installed.

| Lib                | Description             |
|--------------------|-------------------------|
| `jdk-17`           | Java Development Kit 17 |
| `jakarta 9.1`      | Enterprise Edition      |
| `JBoss/WildFly 33` | Webserver               |
| `maven`            | Dependencies Manager    |
| `docker`           | Container Management    |

---

## Technologies

- [Jakarta EE 9.1](https://jakarta.ee/release/9.1/)
- [Maven](https://maven.apache.org/)
- [JBoss/Wildfly](https://www.wildfly.org/)
- [PostgresSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)

## How to Run the Application

### Local
- Clone the project from GitHUB repository
```
 git clone git@github.com:fjpiedade/order-manager-jee.git
```

- Inside project folder run
```
 mvn clean package
```

- Start Build Docker Image - JBoss/WildFly - apply the project inside start on standalone mode
```
 docker build -t api-order .
```

- Start Docker Container - with JBoss/WildFly - apply the project inside start on standalone mode
```
 docker run -d -p 8881:8080 --name api-order-container api-order 
```

---

---

- Or execute using docker-compose
```
 docker compose up -d
```

- Stop run the Project
```
 docker compose down
```

after run the project

Localhost can access the API [localhost:8881](http://localhost:8881).

--- 

The Endpoint can be found on a Postman document

[localhost:8881](http://localhost:8881).


## Examples
<img width="783" alt="Screenshot 2024-09-09 at 1 59 09 AM" src="https://github.com/user-attachments/assets/0dafdf45-4d6c-4cca-af86-5532a10ebaf8">



HTTP requests [httpie](https://httpie.io):

- POST /api/v1/item

```
http POST :9090/api/v1/user name="User" state="State"

HTTP/1.1 200 OK
Content-Length: 129
Content-Type: application/json

{
    "email": "noname.joao@gmail.com",
    "name": "noname"
}
```


