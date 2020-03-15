package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.GithubUser;
import com.joezeo.joefgame.common.dto.SteamUser;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.dto.UserDTO;

public interface UserService{
    UserDTO queryUserByEmail(String email);

    UserDTO queryByAccountid(String accountId);

    User queryByUserid(Long userid);

    void signup(User user);

    void loginBaseGithub(String githubID);

    void loginBaseSteam(String steamid);

    void login(User user, boolean isRemember);

    void logout();

    boolean checkEmail(String targetEmail);

    boolean isExistGithubUser(String githubID);

    boolean isExistSteamUser(String steamid);
}
