package in.shelfpay.lobymaker.api;


import in.shelfpay.lobymaker.controller.InvitationData;
import in.shelfpay.lobymaker.dao.InvitationRepository;
import in.shelfpay.lobymaker.dao.LobbyRepository;
import in.shelfpay.lobymaker.entities.InvitationEntity;
import in.shelfpay.lobymaker.entities.InviteStatus;
import in.shelfpay.lobymaker.entities.LobbyEntity;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.LobbyData;
import in.shelfpay.lobymaker.model.LobbyForm;
import in.shelfpay.lobymaker.utils.ConvertUtils;
import in.shelfpay.lobymaker.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
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

    public void createLobby(LobbyForm lobbyForm, HttpServletRequest request) throws ApiException {
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));

        LobbyEntity lobbyEntity = new LobbyEntity();
        lobbyEntity.setTitle(lobbyForm.getTitle());
        lobbyEntity.setAdminId(userId);
        lobbyRepository.save(lobbyEntity);
    }

    public void sendInvite(HttpServletRequest request, Long lobbyId, String username) throws ApiException {
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));

        checkIfLobbyMember(lobbyId, userId);
        UserEntity receiver = userApi.getCheckByUsername(username);
        checkIfNotLobbyMember(lobbyId, receiver.getId());

        InvitationEntity invitationEntity = invitationRepository.findBySenderIdAndReceiverIdAndLobbyId(userId, receiver.getId(), lobbyId);
        if (Objects.nonNull(invitationEntity)){
            throw new ApiException(String.format("invite already sent to %s", username));
        }

        invitationEntity = new InvitationEntity();
        invitationEntity.setLobbyId(lobbyId);
        invitationEntity.setReceiverId(receiver.getId());
        invitationEntity.setInviteStatus(InviteStatus.SENT);
        invitationEntity.setSenderId(userId);
        invitationRepository.save(invitationEntity);
    }

    public void updateInvitation(HttpServletRequest request, Long inviteId, InviteStatus inviteStatus) throws ApiException {
//        check the max limit
//        if status is sent then he/she can accept or decline
//        if status is declined/revoke/accepted he can't do anything
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));

        InvitationEntity invitationEntity = getCheckInvitationById(inviteId);
        if(!invitationEntity.getReceiverId().equals(userId)) throw new ApiException("not invited");

        LobbyEntity lobbyEntity = getCheckLobbyById(invitationEntity.getLobbyId());

        if(invitationEntity.getInviteStatus().equals(InviteStatus.SENT)){
            if(getMembers(request, lobbyEntity.getId()).size()<3){
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


    public List<UserEntity> getMembers(HttpServletRequest request, Long lobbyId) throws ApiException {
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        checkIfLobbyMember(lobbyId, userId);
        LobbyEntity lobbyEntity = getCheckLobbyById(lobbyId);

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
    public List<LobbyData> getAllLobbies(HttpServletRequest request, Boolean isAdmin) throws IllegalAccessException, InstantiationException, ApiException {

        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        List<LobbyEntity> lobbyEntities = lobbyRepository.findByAdminId(userId);
        if (isAdmin){
            lobbyEntities.sort(Comparator.comparing(LobbyEntity::getCreatedAt).reversed());
            return convert(lobbyEntities);
        }
        List<InvitationEntity> lobbyMappings = invitationRepository.findByReceiverIdAndInviteStatus(userId, InviteStatus.ACCEPTED);
        List<Long> lobbyIds = lobbyMappings.stream().map(InvitationEntity::getLobbyId).collect(Collectors.toList());
        lobbyEntities.addAll(lobbyRepository.findByIdIn(lobbyIds));

        lobbyEntities.sort(Comparator.comparing(LobbyEntity::getCreatedAt).reversed());
        return convert(lobbyEntities);
    }
    private void checkIfLobbyMember(Long lobbyId, Long userId) throws ApiException {
        if (!isLobbyMember(lobbyId, userId)){
            throw new ApiException("not a member of this lobby");
        }
    }

    private void checkIfNotLobbyMember(Long lobbyId, Long userId) throws ApiException {
        if(isLobbyMember(lobbyId, userId)){
            throw new ApiException("present in lobby");
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

    public List<InvitationData> getSentInvitations(HttpServletRequest request, InviteStatus status) throws IllegalAccessException, InstantiationException, ApiException {
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));

        List<InvitationEntity> invitationEntities;
        if(Objects.isNull(status)){
            invitationEntities = invitationRepository.findBySenderIdOrderByCreatedAtDesc(userId);
        }else {
            invitationEntities = invitationRepository.findBySenderIdAndInviteStatusOrderByCreatedAtDesc(userId, status);
        }

        List<InvitationData> invitations = new ArrayList<>();
        for (InvitationEntity invitationEntity : invitationEntities) {
            InvitationData invitation = ConvertUtils.convert(invitationEntity, InvitationData.class);
            invitation.setSenderUsername(userApi.getById(invitationEntity.getSenderId()).getUsername());
            invitation.setReceiverUsername(userApi.getById(invitationEntity.getReceiverId()).getUsername());
            invitation.setLobbyTitle(getCheckLobbyById(invitation.getLobbyId()).getTitle());
            invitations.add(invitation);
        }
        return invitations;
    }

    public List<InvitationData> getReceivedInvitations(HttpServletRequest request) throws IllegalAccessException, InstantiationException, ApiException {
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));

        List<InvitationEntity> invitationEntities = invitationRepository.findByReceiverIdAndInviteStatusOrderByCreatedAtDesc(userId, InviteStatus.SENT);

        List<InvitationData> invitations = new ArrayList<>();
        for (InvitationEntity invitationEntity : invitationEntities) {
            if(isLobbyMember(invitationEntity.getLobbyId(), userId)){
                continue;
            }
            InvitationData invitation = ConvertUtils.convert(invitationEntity, InvitationData.class);
            invitation.setSenderUsername(userApi.getById(invitationEntity.getSenderId()).getUsername());
            invitation.setReceiverUsername(userApi.getById(invitationEntity.getReceiverId()).getUsername());
            invitation.setLobbyTitle(getCheckLobbyById(invitation.getLobbyId()).getTitle());
            invitations.add(invitation);
        }
        return invitations;
    }

    private boolean isLobbyMember(Long lobbyId, Long userId) {
        return ((Objects.nonNull(lobbyRepository.findByIdAndAdminId(lobbyId, userId))) ||
                !CollectionUtils.isEmpty(invitationRepository.findByLobbyIdAndReceiverIdAndInviteStatus(lobbyId, userId, InviteStatus.ACCEPTED)));
    }

    public void revokeInvitation(HttpServletRequest request, Long inviteId) throws ApiException {
        Long userId = userApi.getUserIdFromToken(request.getHeader("Authorization").substring(7));

        InvitationEntity invitationEntity = getCheckInvitationById(inviteId);
        LobbyEntity lobbyEntity = getCheckLobbyById(invitationEntity.getLobbyId());

        if(invitationEntity.getSenderId()!=userId){
            throw new ApiException("you've not sent this invite");
        }
        if(invitationEntity.getInviteStatus()==InviteStatus.SENT){
            invitationEntity.setInviteStatus(InviteStatus.REVOKED);
        }
        invitationRepository.save(invitationEntity);
    }
}
