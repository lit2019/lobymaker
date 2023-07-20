package in.shelfpay.lobymaker.dao;

import in.shelfpay.lobymaker.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    public List<UserEntity> findByIdIn(List<Long> ids);


    UserEntity getById(Long adminId);
}
