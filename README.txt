Supporting 25 persistent client connections to the server at once, we can change the number of threads in config.properties file.
Property name is ${messaging.server.threadpool}.

Currently, I'm running the client and server both on localhost and on port 7777. You can change the host and port for the server in properties file(src/main/resources/config.properties).

ClientRequestObject[originId, destinationId, messageToBeSend] is an entity send by the client A to client B.
When the server accepts the request from client A, it will populate the Map<id, ClientResponseObject>.
ClientResponseObject contains socket details[host and port of id] and list of messages to be send by different clients.

When the client comes online, i.e., made a persistent socket connection to server, all the messages stored in the list will be send to the client.

Created a maven project. For compilation, execute command "mvn clean install". It'll create a jar in the target folder. jar contains all the dependencies, so no need to download any dependency.
After creating the jar, run the server followed by number of clients to be executed.
java -cp path-to-jar com.app.mobile.messaging.server.Main (for executing server)
java -cp path-to-jar com.app.mobile.messaging.server.ClientApp "clientId1"
java -cp path-to-jar com.app.mobile.messaging.server.ClientApp "clientId2"
java -cp path-to-jar com.app.mobile.messaging.server.ClientApp "clientId2"
.
.

for sending messages from clientId1 to clientId2, message should be in the format "clientId2,${message}"   
