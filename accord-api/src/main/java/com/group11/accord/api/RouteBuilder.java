package com.group11.accord.api;

import com.group11.accord.api.server.ServerDeletion;
import com.group11.accord.api.server.ServerEdit;
import com.group11.accord.api.server.members.ServerBan;
import com.group11.accord.api.server.members.ServerKick;
import com.group11.accord.api.server.members.UserAdded;
import com.group11.accord.api.server.members.UserLeft;
import com.group11.accord.api.user.friend.FriendRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class RouteBuilder {

    public static final String FORMAT_SERVER_ID = "/{server_id}";
    public static final String FORMAT_CHANNEL_ID = "/{channel_id}";
    public static final String FORMAT_USER_ID = "/{user_id}";

    public static final class MessageRouteBuilder {

        public static final String BASE = "/message";
        public static final String TOPIC_TEMPLATE = "/{serverId}/{channelId}";

        public static String newMessage(Long serverId, Long channelId) {
            String topic = TOPIC_TEMPLATE
                .replace(FORMAT_SERVER_ID, serverId.toString())
                .replace(FORMAT_CHANNEL_ID, channelId.toString());
            return BASE + "/new/" + topic;
        }

        public static String editedMessage(Long serverId, Long channelId) {
            String topic = TOPIC_TEMPLATE
                .replace(FORMAT_SERVER_ID, serverId.toString())
                .replace(FORMAT_CHANNEL_ID, channelId.toString());
            return BASE + "/edit/" + topic;
        }

        public static String deletedMessage(Long serverId, Long channelId) {
            String topic = TOPIC_TEMPLATE
                .replace(FORMAT_SERVER_ID, serverId.toString())
                .replace(FORMAT_CHANNEL_ID, channelId.toString());
            return BASE + "/delete/" + topic;
        }
    }

    public static final class ChannelRouteBuilder {

        public static final String BASE = "/channel";
        public static final String TOPIC_TEMPLATE = "/{serverId}";

        public static String createdChannel(Long serverId) {
            return BASE + "/new/" + serverId;
        }

        public static String editedChannel(Long serverId) {
            return BASE + "/edit/" + serverId;
        }

        public static String deletedChannel(Long serverId) {
            return BASE + "/delete/" + serverId;
        }
    }

    public static final class ServerRouteBuilder {

        public static final String BASE = "/server";

        public static String editServer(ServerEdit server) {
            return BASE + "/edit/" + server.id();
        }

        public static String deleteServer(ServerDeletion server) {
            return BASE + "/delete/" + server.id();
        }

        public static String userAdded(UserAdded user) {
            return BASE + "add-user/" + user.server().id();
        }

        public static String userRemoved(UserLeft user) {
            return BASE + "remove-user/" + user.server().id();
        }

        public static String kickUser(ServerKick serverKick) {
            return BASE + "kick-user/" + serverKick.server().id();
        }

        public static String banUser(ServerBan serverBan) {
            return BASE + "ban-user/" + serverBan.server().id();
        }
    }

    public static final class UserRouteBuilder {

        public static final String BASE = "/user";

        public static String friendRequest(FriendRequest friendRequest) {
            return BASE + "/friend-request/" + friendRequest.recipient().id();
        }

        public static String acceptFriendRequest(Long userId) {
            return BASE + "/accept-friend-request/" + userId;
        }

        public static String declineFriendRequest(Long userId) {
            return BASE + "/decline-friend-request/" + userId;
        }

        public static String removeFriend(Long userId) {
            return BASE + "/remove-friend/" + userId;
        }

        public static String accountEdit(Long userId) {
            return BASE + "/account-edit/" + userId;
        }

        public static String accountDelete(Long userId) {
            return BASE + "/account-delete/" + userId;
        }

        public static String newServerInvite(Long userId) {
            return BASE + "/new-server-invite/" + userId;
        }
    }

}
