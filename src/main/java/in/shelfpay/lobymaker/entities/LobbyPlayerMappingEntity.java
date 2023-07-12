package in.shelfpay.lobymaker.entities;


import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "lobby_player_mappings",
        uniqueConstraints = {@UniqueConstraint(name = "lobby-player", columnNames = {"lobbyId", "playerId"})}
)
@EntityListeners(AuditingEntityListener.class)
public class LobbyPlayerMappingEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer lobbyId;

    @Column(nullable = false)
    private Integer playerId;

//    @Column(nullable = false)
//    private InviteStatus inviteStatus;

}
