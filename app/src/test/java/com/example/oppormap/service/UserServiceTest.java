package com.example.oppormap.service;

import com.example.oppormap.GenericUnitTest;
import com.example.oppormap.model.entity.User;
import com.example.oppormap.service.impl.DefaultUserService;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends GenericUnitTest {

    private UserService userService = new DefaultUserService();
    private static User user = new User();

    @BeforeClass
    public static void beforeClass() {
        user.setName("Erfan");
        user.setAddress("Kebnekaisevagen 5");
        user.setLatitude(59.3293);
        user.setLongitude(18.0686);
    }

    @Test
    public void a_create_new_user() {
        User saved = userService.create(user);
        assertEquals(saved.getName(), user.getName());
        assertEquals(saved.getLatitude(), user.getLatitude());
        assertEquals(saved.getAddress(), user.getAddress());
        assertNotNull(saved.getId());
        assertNotNull(saved.getRegistered());
        assertNotNull(saved.getUpdated());
        user = saved;
    }

    @Test
    public void b_get_user() {
        User fetched = userService.get(user.getId());
        assertEquals(fetched.getName(), user.getName());
        assertEquals(fetched.getLatitude(), user.getLatitude());
        assertEquals(fetched.getAddress(), user.getAddress());
        assertEquals(fetched.getId(), user.getId());
        assertEquals(fetched.getRegistered(), user.getRegistered());
        assertEquals(fetched.getUpdated(), user.getUpdated());
        user = fetched;
    }

    @Test
    public void c_update_user() {
        user.setName("Tester");
        user.setAddress("Kebnekaisevagen 6");
        user.setLatitude(54.22);
        User fetched = userService.update(user);
        assertEquals(fetched.getName(), user.getName());
        assertEquals(fetched.getLatitude(), user.getLatitude());
        assertEquals(fetched.getAddress(), user.getAddress());
        assertEquals(fetched.getId(), user.getId());
        assertEquals(fetched.getRegistered(), user.getRegistered());
        assertNotEquals(fetched.getUpdated(), user.getUpdated());
    }

    @Test
    public void d_delete_user() {
        userService.delete(user.getId());
        User deleted = userService.get(user.getId());
        assertNull(deleted);
    }
}
