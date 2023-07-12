package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.api.ApiException;
import in.shelfpay.lobymaker.api.LobbyApi;
import in.shelfpay.lobymaker.model.LobbyForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lobby")
@RestController
public class LobbyController {
    @Autowired
    private LobbyApi lobbyApi;

    @PostMapping("/create")
    public void createLobby(@RequestBody LobbyForm lobbyForm){
        lobbyApi.createLobby(lobbyForm);
    }

    @GetMapping("/invite/{lobbyId}")
    public String generateInvite(@PathVariable Long lobbyId, HttpServletRequest servletRequest) throws ApiException {
        return lobbyApi.generateInvite(servletRequest, lobbyId);
    }

    @GetMapping("join/{lobbyId}")
    public String joinInvite(@PathVariable Long lobbyId){
        
    }

    @GetMapping("")
    public String hello(){
        return "hello";
    }
}
