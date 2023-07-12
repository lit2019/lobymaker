package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.LobbyPlayerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyPlayerMappingRepository extends JpaRepository<LobbyPlayerMappingEntity, Long> {
    // Custom methods for querying lobbies
}
