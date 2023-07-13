package in.shelfpay.lobymaker.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "invitations",
        uniqueConstraints = {@UniqueConstraint(name = "lobby-sender-receiver", columnNames = {"lobbyId", "senderId", "receiverId"})}
)
@EntityListeners(AuditingEntityListener.class)
public class InvitationEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lobbyId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private Long senderId;

    @Column
    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

}
