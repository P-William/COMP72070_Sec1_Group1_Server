package com.group11.accord.app.controllers;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewImageMessage;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.app.services.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/channel")
@Tag(name = "Channel Controller", description = "Handles all operations regarding a users channels")
@RequiredArgsConstructor
public class ChannelController {

    ChannelService channelService;

    //CRUD

    //C
    @PostMapping("/server/{serverId}")
    @Operation(description = "Create a new channel within a server")
    Channel createServerChannel(
            @PathVariable @NotNull(message = "A server ID is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A name for the new channel is required") String channelName
    ) {
        return channelService.createServerChannel(serverId, channelName, accountId, token);
    }

    @PostMapping("/dm/{accountId}")
    @Operation(description = "Create a new dm between users")
    Channel createDmChannel(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "The ID of the user to create a dm with") Long friendID
    ) {
        return channelService.createDmChannel(friendID, accountId, token);
    }

    //R
    @GetMapping("/{accountId}")
    @Operation(description = "Retrieve all dm channels belonging to a user")
    List<Channel> getDmChannels(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return channelService.getDmChannels(accountId, token);
    }

    @GetMapping("/{serverId}")
    @Operation(description = "Retrieve all channels a user can see")
    List<Channel> getServerChannels(
            @PathVariable @NotNull(message = "Account ID is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return channelService.getServerChannels(serverId, accountId, token);
    }

    @GetMapping("/{channelId}/{accountId}")
    @Operation(description = "Get all the messages from a channel")
    List<Message> getChannelMessages(
        @PathVariable @NotNull(message = "The channel ID is required") Long channelId,
        @PathVariable @NotNull(message = "Account ID is required") Long accountId,
        @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return channelService.getChannelMessages(channelId, accountId, token);
    }

    //U
    @PatchMapping("/{channelId}")
    @Operation(description = "Change the name of a channel")
    void changeChannelName(
            @PathVariable @NotNull(message = "The id of the channel to change is needed") Long channelId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A new name for the channel is required") String newName
    ) {
        channelService.changeChannelName(channelId, newName, accountId, token);
    }

    //D
    @DeleteMapping("/{channelId}")
    @Operation(description = "Delete a channel")
    void deleteChannel(
        @PathVariable @NotNull(message = "The ID of the channel is required") Long channelId,
        @RequestParam @NotNull(message = "Account ID is required") Long accountId,
        @RequestParam @NotNull(message = "Token is required") String token
    ) {
        channelService.deleteChannel(channelId, accountId, token);
    }

    //Operations dealing with messages
    @PostMapping("/text/{channelId}")
    @Operation(description = "Send a text based message to a channel")
    void sendTextMessage(
            @PathVariable @NotNull(message = "The ID of the channel is required") Long channelId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewTextMessage newMessage
    ) {
        channelService.sendTextMessage(channelId, newMessage, accountId, token);
    }

    @PostMapping("/image/{channelId}")
    @Operation(description = "Send a image in a message to a channel")
    void sendImageMessage(
            @PathVariable @NotNull(message = "The ID of the channel is required") Long channelId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewImageMessage newMessage
    ) {

    }

}
