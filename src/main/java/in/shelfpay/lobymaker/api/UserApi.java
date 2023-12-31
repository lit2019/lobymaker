package in.shelfpay.lobymaker.api;

import in.shelfpay.lobymaker.dao.UserRepository;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.InfoData;
import in.shelfpay.lobymaker.model.UserForm;
import in.shelfpay.lobymaker.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

import static in.shelfpay.lobymaker.controller.UIController.mav;

//    TODO: return mav for homepage instead

@Service
public class UserApi {

    @Autowired
    private InfoData info;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> signup(UserForm userForm) {
        // Check if the username already exists
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userForm.getUsername());
        userEntity.setPassword(userForm.getPassword());

        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        // Save the user in the database
        userRepository.save(userEntity);
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<String> login(UserForm userForm) throws ApiException {
        UserEntity existingUser = userRepository.findByUsername(userForm.getUsername());

        // Check if the user exists and the password matches
        if (existingUser == null || !existingUser.getPassword().equals(userForm.getPassword())) {
            throw new ApiException("Invalid username or password");
        }
        UserUtil.login(existingUser);
        return ResponseEntity.ok("login successfully");

    }

    public ModelAndView logout() {
        UserUtil.logout();
        return mav("login", info);

    }

    public List<UserEntity> findByIdIn(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }


    public UserEntity getById(Long adminId) {
        return userRepository.getById(adminId);
    }

    public UserEntity getCheckByUsername(String username) throws ApiException {
        UserEntity user = userRepository.findByUsername(username);
        if (Objects.isNull(user)){
            throw new ApiException(username+" not found");
        }
        return user;
    }
}
