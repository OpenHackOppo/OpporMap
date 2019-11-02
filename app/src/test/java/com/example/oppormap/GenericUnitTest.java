package com.example.oppormap;

import com.example.oppormap.service.UserService;
import com.example.oppormap.service.impl.DefaultUserService;

import org.junit.Test;

public class GenericUnitTest {

    @Test
    public void test() {
        UserService userService = new DefaultUserService();
        userService.delete("c3088a5a-c14b-4db4-991d-9f12c682711e");
    }

}
