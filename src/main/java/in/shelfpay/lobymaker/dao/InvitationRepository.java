package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {
    // Custom methods for querying lobbies
    InvitationEntity findBySenderIdAndReceiverIdAndLobbyId(Long senderId, Long receiverId, Long lobbyId);

    List<InvitationEntity> findByLobbyIdAndInviteStatus(Long lobbyId, InviteStatus inviteStatus);


    List<InvitationEntity> findByLobbyIdAndReceiverIdAndInviteStatus(Long lobbyId, Long receiverId, InviteStatus inviteStatus);

    List<InvitationEntity> findByReceiverIdAndInviteStatus(Long receiverId, InviteStatus inviteStatus);

    List<InvitationEntity> findByReceiverId(Long userId);

    List<InvitationEntity> findBySenderIdAndInviteStatusOrderByCreatedAtDesc(Long senderId, InviteStatus inviteStatus);

    List<InvitationEntity> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    List<InvitationEntity> findByReceiverIdAndInviteStatusOrderByCreatedAtDesc(Long receiverId, InviteStatus inviteStatus);
}
