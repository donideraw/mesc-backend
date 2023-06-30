package com.pertamina.backend.service;

import com.pertamina.backend.model.entity.AppUser;

import java.util.List;

public interface UserService {

    List<AppUser> findAllUser();

    AppUser deactivateUser(Long id);
    AppUser activateUser(Long id);

}
