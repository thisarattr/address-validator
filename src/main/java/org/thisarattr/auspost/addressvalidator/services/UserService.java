package org.thisarattr.auspost.addressvalidator.services;

import java.util.Optional;

import org.thisarattr.auspost.addressvalidator.models.User;

public interface UserService {

    /**
     * Returns User base on the provided username. Search is case insensitive,
     * because all usernames are saved in lower case.
     *
     * @param username String value
     * @return Optional User object
     */
    Optional<User> find(String username);

}
