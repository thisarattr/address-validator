package org.thisarattr.auspost.addressvalidator.services;

import java.util.Optional;

import org.thisarattr.auspost.addressvalidator.models.User;

public interface CacheService {


    /**
     * Add key and user object in to the cache. Each objects in cache will expire on configured time.
     * @param key key for the value object
     * @param user value object User
     */
    void put(String key, User user);

    /**
     * Retrieves the user object for the cache if its still valid token, else returns empty optional object.
     * @param key key for the object to be retrieved
     * @return Optional of User object
     */
    Optional<User> get(String key);
}
