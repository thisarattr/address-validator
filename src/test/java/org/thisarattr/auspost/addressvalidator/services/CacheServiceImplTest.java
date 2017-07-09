package org.thisarattr.auspost.addressvalidator.services;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.thisarattr.auspost.addressvalidator.models.User;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest {

    CacheService cacheService;

    @Before
    public void setUp() throws Exception {
        cacheService = new CacheServiceImpl();
    }

    @Test
    public void shouldPutIntoCache() throws Exception {
        ReflectionTestUtils.setField(cacheService, "expiresInSec", 6000L);
        User user = new User();
        user.setUsername("test");
        cacheService.put("123", user);

        Optional<User> retrievedUser = cacheService.get("123");
        assertTrue(retrievedUser.isPresent());
        assertThat(retrievedUser.get().getUsername(), is("test"));
    }

    @Test
    public void getNotGetObjWhenCacheExpire() throws Exception {
        ReflectionTestUtils.setField(cacheService, "expiresInSec", 1L);
        User user = new User();
        user.setUsername("test2");
        cacheService.put("456", user);
        Thread.sleep(2000);

        Optional<User> retrievedUser = cacheService.get("456");
        assertFalse(retrievedUser.isPresent());
    }

}