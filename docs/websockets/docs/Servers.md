## Server Related Routes
Base path: `/server`

### New Server
Note there is no websocket for new server. This is because the server is created via the REST API and there is no need to inform the clients via websocket.

### Edited Server
- Description: A server has been edited.
- Route: `/edit/{serverId}`
- Returns: `ServerEdit`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/ServerEdit.java)

### Deleted Server
- Description: A server has been deleted.
- Route: `/delete/{serverId}`
- Returns: `ServerDeletion`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/ServerDeletion.java)

### User Added
- Description: A user has been added to the server.
- Route: `/add-user/{serverId}`
- Returns: `UserAdded`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/members/UserAdded.java)

### User Left
- Description: A user has left from the server.
- Route: `/remove-user/{serverId}`
- Returns: `UserLeft`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/members/UserLeft.java)

### User Kicked
- Description: A user has been kicked from the server.
- Route: `/kick-user/{serverId}`
- Returns: `ServerKick`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/members/ServerKick.java)

### User Banned
- Description: A user has been banned from the server.
- Route: `/ban-user/{serverId}`
- Returns: `ServerBan`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/members/ServerBan.java)