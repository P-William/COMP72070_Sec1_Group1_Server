## User Related Routes
Base path: `/user`

### WARNING
**Keep in mind that the user id is that of the client receiving the event. 
For example, if you get an `account-edit` event that is the id of the user receiving the event. NOT the id of the user that was edited. The user that was edited will be in the body of the event.**

### Friend Request
- Description: The user has received a friend request.
- Route: `/friend-request`
- Returns: `FriendRequest`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/user/friend/FriendRequest.java)

### Friend Request Accepted
- Description: The user's friend request has been accepted by the other user.
- Route: `/friend-request-accepted`
- Returns: `Account`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/user/Account.java)

### Friend Request Rejected
- Description: The user's friend request has been rejected by the other user.
- Route: `/friend-request-rejected`
- Returns: `Account`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/user/Account.java)

### Friend Removed
- Description: The user has been removed from the other user's friend list.
- Route: `/friend-removed`
- Returns: `Account`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/user/Account.java)

### Account Edit
- Description: Another user has edited their account and the client has received the updated account. Their information will need to be updated in the client.
- Route: `/account-edit`
- Returns: `Account`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/user/Account.java)

### Account Deleted
- Description: Another user has deleted their account. The client should remove all information related to this user.
- Route: `/account-delete`
- Returns: `Account`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/user/Account.java)

### New Server Invite
- Description: The user has received a new server invite.
- Route: `/new-server-invite`
- Returns: `ServerInvite`
- Return Definition: [here](https://github.com/P-William/COMP72070_Sec1_Group1_Server/tree/main/accord-api/src/main/java/com/group11/accord/api/server/members/ServerInvite.java)
