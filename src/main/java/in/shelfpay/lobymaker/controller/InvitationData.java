package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.entities.InvitationEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationData extends InvitationEntity {
    private String receiverUsername;
    private String senderUsername;
    private String lobbyTitle;
}
