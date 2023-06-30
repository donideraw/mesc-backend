package com.pertamina.backend.service.impl;

import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.entity.AppUser;
import com.pertamina.backend.repository.AppUserRepository;
import com.pertamina.backend.service.UserService;
import com.pertamina.backend.utils.SecurityUtil;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;

    public UserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<AppUser> findAllUser() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return appUserRepository.findAll(sort);
    }

    @Override
    public AppUser deactivateUser(Long id) {
        AppAuth auth = SecurityUtil.getAuth();
        AppUser user = appUserRepository.findById(id).orElse(null);

        if (user == null) {
            throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
        }

        user.setIsDeleted(Boolean.TRUE);
        user.setUpdatedBy(auth.getUsername());
        user.setUpdatedAt(LocalDateTime.now());
        return appUserRepository.save(user);
    }

    @Override
    public AppUser activateUser(Long id) {
        AppAuth auth = SecurityUtil.getAuth();
        AppUser user = appUserRepository.findById(id).orElse(null);

        if (user == null) {
            throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
        }

        user.setIsDeleted(Boolean.FALSE);
        user.setUpdatedBy(auth.getUsername());
        user.setUpdatedAt(LocalDateTime.now());
        return appUserRepository.save(user);
    }
}
