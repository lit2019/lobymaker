package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.LobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {
    // Custom methods for querying lobbies
    LobbyEntity findByIdAndAdminId(Long id, Long adminId);

    List<LobbyEntity> findByAdminId(Long userId);


    List<LobbyEntity> findByIdIn(List<Long> lobbyIds);
}