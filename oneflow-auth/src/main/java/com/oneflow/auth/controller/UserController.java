package com.oneflow.auth.controller;

import com.oneflow.auth.security.entity.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usr")
public class UserController {

//    @Autowired
//    private UserService userService;

    @GetMapping("/getUser")
    public SysUser getUser(Integer id) {
//        return userService.getUser(id);
        return new SysUser();
    }

    @PostMapping("/addUser")
    public void addUser(SysUser user) {
//        userService.addUser(user);
    }

}
