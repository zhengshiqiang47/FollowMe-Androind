package com.example.coderqiang.followme.IMAbout.Model;

import com.hyphenate.easeui.domain.EaseUser;

/**
 * Created by CoderQiang on 2016/12/1.
 */

public class RobotUser extends EaseUser {

    public RobotUser(String username) {
        super(username.toLowerCase());
    }
}
