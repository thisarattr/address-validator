package org.thisarattr.auspost.addressvalidator.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thisarattr.auspost.addressvalidator.models.User;
import org.thisarattr.auspost.addressvalidator.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> find(String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }
}
