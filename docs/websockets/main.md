## Intro

I'm going to give a quick example here of how I'm documenting websockets.

Overall there will be a base URL. Then each topic will have its own base path.

Base URL: `wss://accord-api.the-hero.dev`

Base path for messages: `/message`

So all topics related to messages will start with `wss://accord-api.the-hero.dev/messages`

**Example endpoint:**

### New Message
- Route: `/new/{serverId}/{channelId}`
- Returns: `Message`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/message/Message.java)


So that means the full URL for a new message will be `wss://accord-api.the-hero.dev/message/new/{serverId}/{channelId}` 

(any {variables} will be replaced with actual values when making the request)


## Our App

Within our application websockets will be used ONLY for receiving updates regarding state changes. This means that the client will not be able to send messages to the server via websockets. Just subscribe to the websockets for updates.

All actions will be done via REST API.

## Message Related Routes
[Messages](docs/websockets/docs/Messages.md)
## Server Related Routes
[Server](docs/websockets/docs/Servers.md)
## Channel Related Routes
[Channel](docs/websockets/docs/Channels.md)
## User Related Routes
[User](docs/websockets/docs/Users.md)
