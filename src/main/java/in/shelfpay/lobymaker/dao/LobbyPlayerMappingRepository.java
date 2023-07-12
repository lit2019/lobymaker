package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.LobbyPlayerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LobbyPlayerMappingRepository extends JpaRepository<LobbyPlayerMappingEntity, Long> {
    // Custom methods for querying lobbies
    LobbyPlayerMappingEntity findByLobbyIdAndPlayerId(Long lobbyId, Long playerId);
    List<LobbyPlayerMappingEntity> findByLobbyId(Long lobbyId);

    List<LobbyPlayerMappingEntity> findByLobbyIdAndInviteStatus(Long lobbyId, InviteStatus inviteStatus);

    List<LobbyPlayerMappingEntity> findByPlayerIdIn(Long playerId);
}
