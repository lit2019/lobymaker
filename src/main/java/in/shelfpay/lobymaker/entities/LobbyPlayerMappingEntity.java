package in.shelfpay.lobymaker.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
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
    private Long lobbyId;

    @Column(nullable = false)
    private Long playerId;

    @Column
    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

}
