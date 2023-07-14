package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.api.ApiException;
import in.shelfpay.lobymaker.api.LobbyApi;
import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.LobbyData;
import in.shelfpay.lobymaker.model.LobbyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/lobby")
@RestController
public class LobbyController {

    @Autowired
    private LobbyApi lobbyApi;

    @PostMapping("/create")
    public void createLobby(@RequestBody LobbyForm lobbyForm){
        lobbyApi.createLobby(lobbyForm);
    }

    @GetMapping("/get/{isAdmin}") //gets lobbies
    public List<LobbyData> getAllLobbies(@PathVariable String isAdmin) throws IllegalAccessException, InstantiationException {
        return lobbyApi.getAllLobbies(Boolean.parseBoolean(isAdmin));
    }

    @GetMapping("/invite/{lobbyId}/{username}")
    public void sendInvite(@PathVariable Long lobbyId, @PathVariable String username) throws ApiException {
        lobbyApi.sendInvite(lobbyId, username);
    }

    @PutMapping("/invitation/update/{inviteId}/{inviteStatus}")
    public void updateInvitation(@PathVariable Long inviteId, @PathVariable InviteStatus inviteStatus) throws ApiException {
        lobbyApi.updateInvitation(inviteId, inviteStatus);
    }

    @PutMapping("/invitation/revoke/{inviteId}")
    public void revokeInvitation(@PathVariable Long inviteId) throws ApiException {
        lobbyApi.revokeInvitation(inviteId);
    }

    @GetMapping("/invitation/get")
    public List<InvitationData> getInvitations() throws IllegalAccessException, InstantiationException, ApiException {
        return lobbyApi.getAllInvitations();
    }

    @GetMapping("/members/{lobbyId}")
    public List<UserEntity> getLobbyMembers(@PathVariable Long lobbyId) throws ApiException {
        return lobbyApi.getMembers(lobbyId);
    }

}
