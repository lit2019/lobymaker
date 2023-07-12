package in.shelfpay.lobymaker.api;

import in.shelfpay.lobymaker.dao.UserRepository;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.model.UserForm;
import in.shelfpay.lobymaker.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

//    TODO: return mav for homepage instead

@Service
public class UserApi {

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

    public ResponseEntity<String> login(@RequestBody UserForm userForm) {
        UserEntity existingUser = userRepository.findByUsername(userForm.getUsername());

        // Check if the user exists and the password matches
        if (existingUser == null || !existingUser.getPassword().equals(userForm.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        UserUtil.username = userForm.getUsername();
        UserUtil.userId = existingUser.getId();

        return ResponseEntity.ok("Login successful");
    }

}
