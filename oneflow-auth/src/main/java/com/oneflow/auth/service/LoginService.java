package com.oneflow.auth.service;

import com.oneflow.auth.core.common.R;
import com.oneflow.auth.entity.User;

public interface LoginService {

    R login(User user);

    R logout();

}
