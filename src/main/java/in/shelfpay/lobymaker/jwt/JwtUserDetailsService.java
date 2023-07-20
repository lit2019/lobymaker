package in.shelfpay.lobymaker.jwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import in.shelfpay.lobymaker.dao.UserRepository;
import in.shelfpay.lobymaker.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private HashMap<String,String> userPWdMap = new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username);
        if (Objects.nonNull(userEntity)){
            return new User(username,
                    passwordEncoder.encode(userEntity.getPassword()),
                    new ArrayList<>());

        } else {

            throw new UsernameNotFoundException("User not found with username: " + username);

        }
    }
}
