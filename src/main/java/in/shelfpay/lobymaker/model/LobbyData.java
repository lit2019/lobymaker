package in.shelfpay.lobymaker.model;

import in.shelfpay.lobymaker.entities.LobbyEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LobbyData extends LobbyEntity {

    private String title;
    private String adminUsername;
}
