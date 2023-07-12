package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.api.ApiException;
import in.shelfpay.lobymaker.api.LobbyApi;
import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.UserEntity;
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

    @GetMapping("/invite/{lobbyId}/{userId}")
    public void sendInvite(@PathVariable Long lobbyId, @PathVariable Long userId) throws ApiException {
        lobbyApi.sendInvite(lobbyId, userId);
    }

    @GetMapping("/process-invitation/{lobbyId}/{inviteStatus}")
    public void updateInvitation(@PathVariable Long lobbyId, @PathVariable InviteStatus inviteStatus) throws ApiException {
        lobbyApi.updateInvitation(lobbyId, inviteStatus);
    }

    @GetMapping("/memebers/{lobbyId}")
    public List<UserEntity> getLobbyMembers(@PathVariable Long lobbyId) throws ApiException {
        return lobbyApi.getMembers(lobbyId);
    }

    @GetMapping("")
    public String hello(){
        return "hello";
    }
}
