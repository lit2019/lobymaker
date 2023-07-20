package in.shelfpay.lobymaker.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
//TODO remove unique constraint
@Table(name = "lobbies", uniqueConstraints  = {@UniqueConstraint(name = "lobby-admin", columnNames = {"id", "adminId"})})
@EntityListeners(AuditingEntityListener.class)

public class LobbyEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long adminId;  //here admin id is the user's id who has created the loby

}
//id, admindi
//1, 1
//1, 2

