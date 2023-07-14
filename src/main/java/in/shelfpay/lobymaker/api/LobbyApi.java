package in.shelfpay.lobymaker.api;


import in.shelfpay.lobymaker.controller.InvitationData;
import in.shelfpay.lobymaker.dao.InvitationRepository;
import in.shelfpay.lobymaker.dao.LobbyRepository;
import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.LobbyEntity;
import in.shelfpay.lobymaker.entities.InvitationEntity;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.LobbyData;
import in.shelfpay.lobymaker.model.LobbyForm;
import in.shelfpay.lobymaker.utils.ConvertUtils;
import in.shelfpay.lobymaker.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private InvitationRepository invitationRepository;

    public void createLobby(LobbyForm lobbyForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();
        lobbyEntity.setTitle(lobbyForm.getTitle());
        lobbyEntity.setAdminId(UserUtil.userId);
        lobbyRepository.save(lobbyEntity);
    }

    public void sendInvite(Long lobbyId, String username) throws ApiException {
        checkIfLobbyMember(lobbyId, UserUtil.userId);
        UserEntity receiver = userApi.getByUsername(username);
        checkNotIfLobbyMember(lobbyId, receiver.getId());

        InvitationEntity invitationEntity = invitationRepository.findBySenderIdAndReceiverIdAndLobbyId(UserUtil.userId, receiver.getId(), lobbyId);
        if (Objects.nonNull(invitationEntity)){
            throw new ApiException(String.format("invite already sent to %s", username));
        }

        invitationEntity = new InvitationEntity();
        invitationEntity.setLobbyId(lobbyId);
        invitationEntity.setReceiverId(receiver.getId());
        invitationEntity.setInviteStatus(InviteStatus.SENT);
        invitationEntity.setSenderId(UserUtil.userId);
        invitationRepository.save(invitationEntity);
    }

    public void updateInvitation(Long inviteId, InviteStatus inviteStatus) throws ApiException {
//        check the max limit
//        if status is sent then he/she can accept or decline
//        if status is declined/revoke/accepted he can't do anything

        InvitationEntity invitationEntity = getCheckInvitationById(inviteId);
        if(!invitationEntity.getReceiverId().equals(UserUtil.userId)) throw new ApiException("not invited");

        LobbyEntity lobbyEntity = getCheckLobbyById(invitationEntity.getLobbyId());
        checkNotIfLobbyMember(lobbyEntity.getId(), UserUtil.userId);

        if(invitationEntity.getInviteStatus().equals(InviteStatus.SENT)){
            if(getMembers(lobbyEntity.getId()).size()<3){
                invitationEntity.setInviteStatus(inviteStatus);
            }else {
                throw new ApiException("lobby is full, try again later");
            }
        }
        invitationEntity.setInviteStatus(inviteStatus);
        invitationRepository.save(invitationEntity);
    }
//TODO add an end point to revoke invitiation
    private InvitationEntity getCheckInvitationById(Long inviteId) throws ApiException {
        InvitationEntity invite = invitationRepository.getById(inviteId);
        if(Objects.isNull(invite)){
            throw new ApiException("invitation not found");
        }
        return invite;
    }


    public List<UserEntity> getMembers(Long lobbyId) throws ApiException {
        LobbyEntity lobbyEntity = getCheckLobbyById(lobbyId);
        checkIfLobbyMember(lobbyId, UserUtil.userId);

        List<InvitationEntity> lobbyPlayerMappingEntities = invitationRepository.findByLobbyIdAndInviteStatus(lobbyId, InviteStatus.ACCEPTED);
        List<Long> userIds = lobbyPlayerMappingEntities.stream().map(InvitationEntity::getReceiverId).collect(Collectors.toList());
        userIds.add(lobbyEntity.getAdminId());

        List<UserEntity> userEntities = userApi.findByIdIn(userIds);
        return userEntities;
    }

    public LobbyEntity getCheckLobbyById(Long lobbyId) throws ApiException {
        LobbyEntity lobbyEntity = lobbyRepository.getById(lobbyId);
        if (Objects.isNull(lobbyEntity)){
            throw new ApiException("lobby not found");
        }
        return lobbyEntity;
    }

    //    throws exception if not member or admin of the lobby
    public List<LobbyData> getAllLobbies(Boolean isAdmin) throws IllegalAccessException, InstantiationException {
        List<LobbyEntity> lobbyEntities = lobbyRepository.findByAdminId(UserUtil.userId);
        if (isAdmin){
            return convert(lobbyEntities);
        }
        List<InvitationEntity> lobbyMappings = invitationRepository.findByReceiverIdAndInviteStatus(UserUtil.userId, InviteStatus.ACCEPTED);
        List<Long> lobbyIds = lobbyMappings.stream().map(InvitationEntity::getLobbyId).collect(Collectors.toList());
        lobbyEntities.addAll(lobbyRepository.findByIdIn(lobbyIds));

        return convert(lobbyEntities);
    }
    private void checkIfLobbyMember(Long lobbyId, Long userId) throws ApiException {
        if((Objects.isNull(lobbyRepository.findByIdAndAdminId(lobbyId, userId)))
                && Objects.isNull(invitationRepository.findByLobbyIdAndReceiverIdAndInviteStatus(lobbyId, userId, InviteStatus.ACCEPTED))){
            throw new ApiException("not a member");
        }
    }

    private void checkNotIfLobbyMember(Long lobbyId, Long userId) throws ApiException {
        if((Objects.nonNull(lobbyRepository.findByIdAndAdminId(lobbyId, userId)))
                || !CollectionUtils.isEmpty(invitationRepository.findByLobbyIdAndReceiverIdAndInviteStatus(lobbyId, userId, InviteStatus.ACCEPTED))){
            throw new ApiException("you're a member");
        }
    }

    private List<LobbyData> convert(List<LobbyEntity> lobbyEntities) throws IllegalAccessException, InstantiationException {
        List<LobbyData> lobbyDataList = ConvertUtils.convertList(lobbyEntities, LobbyData.class);
        for (LobbyData lobbyData : lobbyDataList) {
            Long adminId = lobbyData.getAdminId();
            lobbyData.setAdminUsername(userApi.getById(adminId).getUsername());
        }
        return lobbyDataList;
    }

    public List<InvitationData> getAllInvitations() throws IllegalAccessException, InstantiationException, ApiException {
        List<InvitationEntity> invitationEntities = invitationRepository.findByReceiverIdAndInviteStatus(UserUtil.userId, InviteStatus.SENT);

        List<InvitationData> invitations = new ArrayList<>();
        for (InvitationEntity invitationEntity : invitationEntities) {
            checkNotIfLobbyMember(invitationEntity.getLobbyId(), UserUtil.userId);

            InvitationData invitation = ConvertUtils.convert(invitationEntity, InvitationData.class);
            invitation.setSenderUsername(userApi.getById(invitationEntity.getSenderId()).getUsername());
            invitation.setLobbyTitle(getCheckLobbyById(invitation.getLobbyId()).getTitle());
            invitations.add(invitation);
        }
        return invitations;
    }

    public void revokeInvitation(Long inviteId) {
        
    }
}
