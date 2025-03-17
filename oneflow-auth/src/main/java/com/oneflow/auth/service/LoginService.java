package com.oneflow.auth.service;

import com.oneflow.auth.security.core.common.R;
import com.oneflow.auth.security.entity.User;

public interface LoginService {

    R login(User user);

    R logout();

}
