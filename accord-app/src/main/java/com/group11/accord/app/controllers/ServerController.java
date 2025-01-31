package com.group11.accord.app.controllers;

import com.group11.accord.api.server.Server;
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

    ServerService serverService;

    //CRUD

    //Create
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{accountId}")
    @Operation(description = "Create a new server")
    void createServer(
            @PathVariable @NotNull(message = "Account of the server owner ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "The name of the new server is required") String serverName
    ) {
        serverService.createServer(accountId, token, serverName);
    }

    //Read
    @GetMapping("/{accountId}")
    @Operation(description = "Retrieve all servers that a user is a member of")
    List<Server> getServers(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return serverService.getServers(accountId, token);
    }


    //Update
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/name/{serverId}")
    @Operation(description = "Change the name of a server")
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
    @Operation(description = "Delete a server")
    void deleteServer(
            @PathVariable @NotNull(message = "ID of the server to delete is required") Long serverId,
            @RequestParam @NotNull(message = "Account ID of the server owner is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        serverService.deleteServer(serverId, accountId, token);
    }

    //Owner Operations dealing with members of a server
    @PostMapping("/kick/{serverId}")
    @Operation(description = "Kick a user from a server")
    void kickFromServer(
            @PathVariable @NotNull(message = "ID of the server is required") Long serverId,
            @RequestParam @NotNull(message = "Owner's account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewServerKick kickUpload
    ) {
        serverService.kickFromServer(serverId, accountId, token, kickUpload);
    }

    @PostMapping("/ban/{serverId}")
    @Operation(description = "Ban a user from a server")
    void banFromServer(
            @PathVariable @NotNull(message = "ID of the server is required") Long serverId,
            @RequestParam @NotNull(message = "Owner's account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @Valid NewServerBan banUpload
            ) {
        serverService.banFromServer(serverId, accountId, token, banUpload);
    }

    @PostMapping("/{serverId}/{accountId}")
    @Operation(description = "Leave a server")
    void leaveServer(
            @PathVariable @NotNull(message = "ID of the server is required") Long serverId,
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        serverService.leaveServer(serverId, accountId, token);
    }
}
