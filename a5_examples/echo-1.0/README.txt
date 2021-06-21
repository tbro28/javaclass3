mvn clean compile

Start the server:
mvn exec:java -Dexec.mainClass=demo.DemoServer

In a separate shell start the client
mvn exec:java -Dexec.mainClass=demo.DemoClient


Separately, you can run the DemoServer class and connect to it using telnet.  The server will simply write anything typed
on the client console to the server console.  To execute the EchoDemo:
mvn exec:java -Dexec.mainClass=demo.DemoServer

