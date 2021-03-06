# Java Quick Start Project -- AR Data Management Platform

This repository contains code samples for the Quick Start blog post series.

You can read more about the Quick Start series on the [MongoDB Developer Hub](https://developer.mongodb.com/learn/).

- [MongoDB & Java - CRUD Operations Tutorial](https://developer.mongodb.com/quickstart/java-setup-crud-operations)
- [Java - Aggregation Pipeline](https://developer.mongodb.com/quickstart/java-aggregation-pipeline)
- [Java - Change Streams](https://developer.mongodb.com/quickstart/java-change-streams)
- [Java - Client Side Field Level Encryption](https://developer.mongodb.com/quickstart/java-client-side-field-level-encryption/)

# MongoDB Cluster

To get started with MongoDB Atlas and get a free cluster read [this blog post](https://developer.mongodb.com/quickstart/free-atlas-cluster).

# Requirements

- Java JDK 8 to 15.
- Maven 3.6.3.

# Command lines

- Compile: 

```sh
mvn clean compile
```

- Run the `HelloMongoDB` class: 

```sh
mvn compile exec:java -Dexec.mainClass="HelloMongoDB"
```
- Run the `Connection` class: 

```sh
mvn compile exec:java -Dexec.mainClass="Connection" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Create` class:

```sh
mvn compile exec:java -Dexec.mainClass="Create" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Read` class:

```sh
mvn compile exec:java -Dexec.mainClass="Read" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Update` class:

```sh
mvn compile exec:java -Dexec.mainClass="Update" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Delete` class:

```sh
mvn compile exec:java -Dexec.mainClass="Delete" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

Sample JSON document showing how files are stored
![image](https://user-images.githubusercontent.com/48060956/125744258-74a06c12-286a-499d-8547-e210989c421a.png)
