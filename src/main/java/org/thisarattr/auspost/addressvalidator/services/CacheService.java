package org.thisarattr.auspost.addressvalidator.services;

import java.util.Optional;

import org.thisarattr.auspost.addressvalidator.models.User;

public interface CacheService {

    void put(String key, User user);

    Optional<User> get(String key);
}
