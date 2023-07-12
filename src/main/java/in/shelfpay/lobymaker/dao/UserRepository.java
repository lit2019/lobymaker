package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    // Custom methods for querying lobbies
}
