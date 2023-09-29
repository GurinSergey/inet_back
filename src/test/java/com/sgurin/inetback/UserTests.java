package com.sgurin.inetback;

import com.sgurin.inetback.domain.Role;
import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.repository.RoleRepository;
import com.sgurin.inetback.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.junit.Assert.assertEquals;


@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@Transactional
public class UserTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Rollback(value = false)
    public void name() {
        User user = userRepository.findByEmail("sgurin.dev@gmail.com").orElse(null);
        if (Objects.nonNull(user)) {
            Role role = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            if (Objects.nonNull(role)) {
                user.addRole(role);

                user = userRepository.save(user);
            }
        }
        assertEquals(user == null ? 0 : user.getRoles().size(), 1);
    }
}
