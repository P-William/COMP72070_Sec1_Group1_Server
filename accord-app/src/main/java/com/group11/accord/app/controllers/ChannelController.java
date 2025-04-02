package com.group11.accord.app.controllers;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.app.services.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/channel")
@Tag(name = "Channel Controller", description = "Handles all operations regarding a users channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    //CRUD

    //C
    @PostMapping("/server/{serverId}")
    @Operation(summary = "Create a new channel within a server")
    Channel createServerChannel(
            @PathVariable @NotNull(message = "A server ID is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A name for the new channel is required") String channelName
    ) {
        return channelService.createServerChannel(serverId, channelName, accountId, token);
    }

    //R
    @GetMapping("/dm/{accountId}")
    @Operation(summary= "Retrieve all dm channels belonging to a user")
    List<Channel> getDmChannels(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return channelService.getDmChannels(accountId, token);
    }

    @GetMapping("/server/{serverId}")
    @Operation(summary = "Retrieve all channels a user can see")
    List<Channel> getServerChannels(
            @PathVariable @NotNull(message = "Account ID is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return channelService.getServerChannels(serverId, accountId, token);
    }

    @GetMapping("/{channelId}/{accountId}")
    @Operation(summary = "Get all the messages from a channel")
    List<Message> getChannelMessages(
        @PathVariable @NotNull(message = "The channel ID is required") Long channelId,
        @PathVariable @NotNull(message = "Account ID is required") Long accountId,
        @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return channelService.getChannelMessages(channelId, accountId, token);
    }

    //U
    @PatchMapping("/{channelId}")
    @Operation(summary = "Change the name of a channel")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a channel")
    void deleteChannel(
        @PathVariable @NotNull(message = "The ID of the channel is required") Long channelId,
        @RequestParam @NotNull(message = "Account ID is required") Long accountId,
        @RequestParam @NotNull(message = "Token is required") String token
    ) {
        channelService.deleteServerChannel(channelId, accountId, token);
    }

    //Operations dealing with messages
    @PostMapping("/{channelId}/text")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Send a text based message to a channel")
    void sendTextMessage(
            @PathVariable @NotNull(message = "The ID of the channel is required") Long channelId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewTextMessage newMessage
    ) {
        channelService.sendTextMessage(channelId, newMessage, accountId, token);
    }

    @PostMapping(value="/{channelId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Send a image in a message to a channel")
    void sendImageMessage(
            @PathVariable @NotNull(message = "The ID of the channel is required") Long channelId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid MultipartFile image
            ) {
        channelService.sendImageMessage(channelId, image, accountId, token);
    }

}
