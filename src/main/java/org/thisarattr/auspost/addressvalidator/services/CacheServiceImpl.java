package org.thisarattr.auspost.addressvalidator.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thisarattr.auspost.addressvalidator.models.User;

/**
 * This class provides basic cache implementation for this exercise. But this needs to be replace with more appropriate
 * solution like Redis.
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Value("${security.user.cache.expire.in.sec}")
    private Long expiresInSec;
    Map<String, User> cache = new HashMap<>();

    @Override
    public void put(String key, User user) {
        /*This is bad practice to reuse field for totally different purpose. But this is just a workaround. Whole cache
        service need to be replace with proper cache.*/
        user.setCreatedOn(LocalDateTime.now().plusSeconds(expiresInSec));
        cache.put(key, user);
    }

    @Override
    public Optional<User> get(String key) {
        User user = cache.get(key);
        if (user != null) {
            if (LocalDateTime.now().isBefore(user.getCreatedOn())) {
                return Optional.of(user);
            } else {
                cache.remove(key);
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
