package com.group11.accord.app.controllers;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import com.group11.accord.app.services.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO-Connor add ApiResponses after service is completed

@Slf4j
@Validated
@RestController
@RequestMapping("/server")
@Tag(name = "Server Controller", description = "Handles all operations regarding Servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    //CRUD

    //Create
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create/{accountId}")
    @Operation(summary = "Create a new server")
    void createServer(
            @PathVariable @NotNull(message = "Account of the server owner ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "The name of the new server is required") String serverName
    ) {
        serverService.createServer(accountId, token, serverName);
    }

    //Read
    @GetMapping("/{accountId}")
    @Operation(summary = "Retrieve all servers that a user is a member of")
    List<BasicServer> getServers(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return serverService.getServers(accountId, token);
    }

    //TODO add endpoint for getting all the members of a server

    //Update
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{serverId}/name")
    @Operation(summary = "Change the name of a server")
    void changeServerName(
            @PathVariable @NotNull(message = "Server ID is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID of the server owner is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A new name for the server is required") String newName
    ) {
        serverService.changeServerName(serverId, accountId, token, newName);
    }

    //Delete
    @DeleteMapping("/{serverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a server")
    void deleteServer(
            @PathVariable @NotNull(message = "ID of the server to delete is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID of the server owner is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        serverService.deleteServer(serverId, accountId, token);
    }

    @PostMapping("/{serverId}/invite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Invite a user to a server")
    void inviteToServer(
            @PathVariable @NotNull(message = "ID of the server to invite someone to") Long serverId,
            @RequestParam @NotNull(message = "Account ID of a server member is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {

    }

    //Owner Operations dealing with members of a server
    @PostMapping("/{serverId}/kick")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Kick a user from a server")
    void kickFromServer(
            @PathVariable @NotNull(message = "ID of the server is required") Long serverId,
            @RequestParam @NotNull(message = "Owner's account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewServerKick kickUpload
    ) {
        serverService.kickFromServer(serverId, accountId, token, kickUpload);
    }

    @PostMapping("/{serverId}/ban")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ban a user from a server")
    void banFromServer(
            @PathVariable @NotNull(message = "ID of the server is required") Long serverId,
            @RequestParam @NotNull(message = "Owner's account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewServerBan banUpload
            ) {
        serverService.banFromServer(serverId, accountId, token, banUpload);
    }

    @PostMapping("/leave/{serverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Leave a server")
    void leaveServer(
            @PathVariable @NotNull(message = "ID of the server is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        serverService.leaveServer(serverId, accountId, token);
    }
}
