package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.api.ApiException;
import in.shelfpay.lobymaker.api.UserApi;
import in.shelfpay.lobymaker.dao.UserRepository;
import in.shelfpay.lobymaker.model.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserApi userApi;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserForm userForm) {
        return userApi.signup(userForm);
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestBody UserForm userForm) throws ApiException {
        return userApi.login(userForm);
    }

    @PostMapping("/logout")
    public ModelAndView logout() {
        return userApi.logout();
    }
}
