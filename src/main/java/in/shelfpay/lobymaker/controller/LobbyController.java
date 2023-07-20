package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.api.ApiException;
import in.shelfpay.lobymaker.api.LobbyApi;
import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.LobbyData;
import in.shelfpay.lobymaker.model.LobbyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/lobby")
@RestController
public class LobbyController {

    @Autowired
    private LobbyApi lobbyApi;

    @PostMapping("/create")
    public void createLobby(HttpServletRequest request, @RequestBody LobbyForm lobbyForm) throws ApiException {
        lobbyApi.createLobby(lobbyForm, request);
    }

    @GetMapping("/get/{isAdmin}") //gets lobbies
    public List<LobbyData> getAllLobbies(HttpServletRequest request, @PathVariable String isAdmin) throws IllegalAccessException, InstantiationException, ApiException {
        return lobbyApi.getAllLobbies(request, Boolean.parseBoolean(isAdmin));
    }

    @GetMapping("/invite/{lobbyId}/{username}")
    public void sendInvite(HttpServletRequest request, @PathVariable Long lobbyId, @PathVariable String username) throws ApiException {
        lobbyApi.sendInvite(request, lobbyId, username);
    }

    @PutMapping("/invitation/update/{inviteId}/{inviteStatus}")
    public void updateInvitation(HttpServletRequest request, @PathVariable Long inviteId, @PathVariable InviteStatus inviteStatus) throws ApiException {
        lobbyApi.updateInvitation(request, inviteId, inviteStatus);
    }

    @PutMapping("/invitation/revoke/{inviteId}")
    public void revokeInvitation(HttpServletRequest request, @PathVariable Long inviteId) throws ApiException {
        lobbyApi.revokeInvitation(request, inviteId);
    }

    @GetMapping("/invitation/received")
    public List<InvitationData> getReceivedInvitations(HttpServletRequest request) throws IllegalAccessException, InstantiationException, ApiException {
        return lobbyApi.getReceivedInvitations(request);
    }

    @GetMapping("/invitation/sent")
    public List<InvitationData> getSentInvitations(HttpServletRequest request, @RequestParam(name = "status", required = false) InviteStatus status) throws IllegalAccessException, InstantiationException, ApiException {
        return lobbyApi.getSentInvitations(request, status);
    }

    @GetMapping("/members/{lobbyId}")
    public List<UserEntity> getLobbyMembers(HttpServletRequest request, @PathVariable Long lobbyId) throws ApiException {
        return lobbyApi.getMembers(request, lobbyId);
    }

}
