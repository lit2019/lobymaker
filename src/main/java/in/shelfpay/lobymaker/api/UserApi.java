package in.shelfpay.lobymaker.api;

import in.shelfpay.lobymaker.dao.UserRepository;
import in.shelfpay.lobymaker.entities.UserEntity;
import in.shelfpay.lobymaker.jwt.JwtTokenUtil;
import in.shelfpay.lobymaker.model.InfoData;
import in.shelfpay.lobymaker.model.UserForm;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


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

    public Long getUserIdFromToken(String jwtToken) throws ApiException {
        if(!jwtTokenUtil.isValid(jwtToken)) throw new ApiException("Invalid user");

        UserEntity userEntity = getCheckByUsername(jwtTokenUtil.getUsernameFromToken(jwtToken));
        return userEntity.getId();

    }
}
