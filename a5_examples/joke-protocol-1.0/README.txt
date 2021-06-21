To compile:
mvn clean compile

To run local main class:
mvn exec:java -Dexec.mainClass=edu.uw.exemplar.joke.KnockKnockJoke

To run traditional or state patter server implementation:
Start the server in one window (and select implementation at prompt):
mvn exec:java -Dexec.mainClass=edu.uw.exemplar.protocol.net.Server

And the client in another:
mvn exec:java -Dexec.mainClass=edu.uw.exemplar.protocol.net.Client

To run the client proxy implementation, start either server implementation in one window
and execute the proxy client in the other:
mvn exec:java -Dexec.mainClass=edu.uw.exemplar.protocol.proxy.ProxyClient
