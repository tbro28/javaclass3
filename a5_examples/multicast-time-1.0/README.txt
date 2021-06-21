To build:
mvn clean compile

Start the server:
mvn exec:java -Dexec.mainClass=demo.TimeServerDemo

In a separate window start the client:
mvn exec:java -Dexec.mainClass=demo.TimeClientDemo
