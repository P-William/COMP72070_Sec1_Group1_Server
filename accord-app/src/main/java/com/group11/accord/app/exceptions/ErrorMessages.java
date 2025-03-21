package com.group11.accord.app.exceptions;

public class ErrorMessages {

    public static final String INVALID_CREDENTIALS = "Invalid Login Credentials";
    public static final String MISSING_ACCOUNT_WITH_ID = "Unknown account with ID %s";
    public static final String MISSING_ACCOUNT_WITH_USERNAME = "Unknown account with username %s";
    public static final String FRIEND_REQUEST_ALREADY_EXISTS = "A friend request to %s has already been sent";
    public static final String MISSING_FRIEND_REQUEST_WITH_ID = "Unknown friend request with ID %s";
    public static final String ALREADY_FRIENDS = "User is already friends with %s";
    public static final String NOT_FRIENDS = "User %s is not friends with the given user";
    public static final String MISSING_SERVER_INVITE_WITH_ID = "Unknown server invite with ID %s";
    public static final String ALREADY_MEMBER = "User is already of member of the server %s";
    public static final String NOT_MEMBER = "User is not a member of server with ID %s";
    public static final String INVALID_SERVER_AUTHORITY = "User %s is not authorized to make changes to this server";
    public static final String INVALID_CHANNEL_AUTHORITY = "User %s is not authorized to make changes to this channel";
    public static final String MISSING_SERVER_WITH_ID = "Unknown server with ID %s";
    public static final String MISSING_CHANNEL_WITH_ID = "Unknown channel with ID %s";
    public static final String CHANNEL_NOT_BOUND = "Channel is not bound to a server or direct message";
    public static final String CHANNEL_NOT_OF_TYPE = "Channel is not of type %s and cannot have operations given type applied to it";
}
