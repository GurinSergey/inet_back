package com.sgurin.inetback;

import com.sgurin.inetback.domain.Role;
import com.sgurin.inetback.repository.RoleRepository;
import com.sgurin.inetback.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@Transactional
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Rollback(value = false)
    public void testCreateRoles() {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");

        roleRepository.saveAll(Arrays.asList(admin, user));

        long count = roleRepository.count();
        assertEquals(2, count);
    }
}
