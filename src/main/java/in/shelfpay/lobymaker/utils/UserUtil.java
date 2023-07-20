package in.shelfpay.lobymaker.utils;

import in.shelfpay.lobymaker.entities.UserEntity;

public class UserUtil {
    public static String username;
    public static Long userId;

    public static void logout() {
        UserUtil.username = null;
//        UserUtil.userId = null;
    }

    public static void login(UserEntity userEntity) {
        UserUtil.username = userEntity.getUsername();
//        UserUtil.userId = userEntity.getId();
    }

}
