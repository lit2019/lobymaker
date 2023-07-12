package in.shelfpay.lobymaker.api;


import in.shelfpay.lobymaker.dao.LobbyPlayerMappingRepository;
import in.shelfpay.lobymaker.dao.LobbyRepository;
import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.LobbyEntity;
import in.shelfpay.lobymaker.entities.LobbyPlayerMappingEntity;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.LobbyForm;
import in.shelfpay.lobymaker.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LobbyApi {
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserApi userApi;

    @Autowired
    private LobbyPlayerMappingRepository lobbyPlayerMappingRepository;

    public void createLobby(LobbyForm lobbyForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();
        lobbyEntity.setTitle(lobbyForm.getTitle());
        lobbyEntity.setAdminId(UserUtil.userId);
        lobbyRepository.save(lobbyEntity);
    }

    public void sendInvite(Long lobbyId, String username) throws ApiException {
        if (Objects.isNull(lobbyRepository.findByIdAndAdminId(lobbyId, UserUtil.userId))){
            throw new ApiException("lobby not found");
        }
        UserEntity invitee = userApi.getByUsername(username);
        LobbyPlayerMappingEntity lobbyPlayerMappingEntity = lobbyPlayerMappingRepository.findByLobbyIdAndPlayerId(lobbyId, invitee.getId());
        if (Objects.nonNull(lobbyPlayerMappingEntity)){
            throw new ApiException("invite already sent");
        }
        lobbyPlayerMappingEntity = new LobbyPlayerMappingEntity();
        lobbyPlayerMappingEntity.setLobbyId(lobbyId);
        lobbyPlayerMappingEntity.setPlayerId(invitee.getId());
        lobbyPlayerMappingEntity.setInviteStatus(InviteStatus.SENT);
        lobbyPlayerMappingRepository.save(lobbyPlayerMappingEntity);
    }

    public void updateInvitation(Long lobbyId, InviteStatus inviteStatus) throws ApiException {
        LobbyEntity lobbyEntity = getCheckLobbyById(lobbyId);

        LobbyPlayerMappingEntity lobbyPlayerMappingEntity = lobbyPlayerMappingRepository.findByLobbyIdAndPlayerId(lobbyId, UserUtil.userId);
        if (Objects.isNull(lobbyPlayerMappingEntity)){
            throw new ApiException("no invite was initialy sent to you for this lobby");
        }
        lobbyPlayerMappingEntity.setInviteStatus(inviteStatus);

        lobbyPlayerMappingRepository.save(lobbyPlayerMappingEntity);
    }

    public List<UserEntity> getMembers(Long lobbyId) throws ApiException {
        LobbyEntity lobbyEntity = getCheckLobbyById(lobbyId);
        checkIfMember(lobbyId, UserUtil.userId);

        List<LobbyPlayerMappingEntity> lobbyPlayerMappingEntities = lobbyPlayerMappingRepository.findByLobbyIdAndInviteStatus(lobbyId, InviteStatus.ACCEPTED);
        List<Long> userIds = lobbyPlayerMappingEntities.stream().map(LobbyPlayerMappingEntity::getPlayerId).collect(Collectors.toList());
        userIds.add(lobbyEntity.getAdminId());

        List<UserEntity> userEntities = userApi.findByIdIn(userIds);
        return userEntities;
    }

    private LobbyEntity getCheckLobbyById(Long lobbyId) throws ApiException {
        LobbyEntity lobbyEntity = lobbyRepository.getById(lobbyId);
        if (Objects.isNull(lobbyEntity)){
            throw new ApiException("lobby not found");
        }
        return lobbyEntity;
    }

    //    throws exception if not member or admin of the lobby
    private void checkIfMember(Long lobbyId, Long userId) throws ApiException {
        if((Objects.isNull(lobbyRepository.findByIdAndAdminId(lobbyId, userId))) && Objects.isNull(lobbyPlayerMappingRepository.findByLobbyIdAndPlayerId(lobbyId, userId))){
            throw new ApiException("you're not a member");
        }
    }

    public List<LobbyEntity> getAllLobbies(Boolean isAdmin) {
        List<LobbyEntity> lobbyEntities = lobbyRepository.findByAdminIdIn(UserUtil.userId);
        if (isAdmin){
            return lobbyEntities;
        }
        List<LobbyPlayerMappingEntity> lobbyMappings = lobbyPlayerMappingRepository.findByPlayerIdIn(UserUtil.userId);
        List<Long> lobbyIds = lobbyMappings.stream().map(LobbyPlayerMappingEntity::getLobbyId).collect(Collectors.toList());
        lobbyEntities.addAll(lobbyRepository.findByIdIn(lobbyIds));
        return lobbyEntities;
    }
}
