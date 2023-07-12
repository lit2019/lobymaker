package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.LobbyEntity;
import in.shelfpay.lobymaker.entities.LobbyPlayerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {
    // Custom methods for querying lobbies
    LobbyPlayerMappingEntity findByIdAndAdminId(Long id, Long adminId);
}