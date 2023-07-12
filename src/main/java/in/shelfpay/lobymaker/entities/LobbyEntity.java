package in.shelfpay.lobymaker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "lobbies", uniqueConstraints  = {@UniqueConstraint(name = "lobby-admin", columnNames = {"id", "adminId"})})
@EntityListeners(AuditingEntityListener.class)

public class LobbyEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long adminId;

}
