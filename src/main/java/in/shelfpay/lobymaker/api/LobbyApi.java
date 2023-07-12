package in.shelfpay.lobymaker.api;


import in.shelfpay.lobymaker.dao.LobbyRepository;
import in.shelfpay.lobymaker.entities.LobbyEntity;
import in.shelfpay.lobymaker.model.LobbyForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LobbyApi {
    @Autowired
    private LobbyRepository lobbyRepository;

    public void createLobby(LobbyForm lobbyForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();
        lobbyEntity.setTitle(lobbyForm.getTitle());
        lobbyEntity.setAdminId(lobbyForm.getAdminId());
        lobbyRepository.save(lobbyEntity);
    }

    public String generateInvite(HttpServletRequest request, Long lobbyId) throws ApiException {
        String baseUrl = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();

        if (Objects.isNull(lobbyRepository.getById(lobbyId))){
            throw new ApiException("lobby not found");
        }

        return baseUrl.replace(requestURI, "")+"/join/"+lobbyId;
    }
}
