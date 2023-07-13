package in.shelfpay.lobymaker.controller;

import in.shelfpay.lobymaker.model.InfoData;
import in.shelfpay.lobymaker.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UIController {

    @Autowired
    private InfoData info;

    @RequestMapping("/ui/invites")
    public ModelAndView invitesPage() {
        return mav("invites");
    }
    @RequestMapping("/ui/lobbies")
    public ModelAndView lobbiesPage() {
        return mav("lobbies");
    }


    protected ModelAndView mav(String page) {
        // Get current user

        info.setUsername(UserUtil.username == null ? "" : UserUtil.username);
        info.setUserId(UserUtil.userId);

        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", info);
        return mav;
    }
}
