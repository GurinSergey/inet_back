package com.sgurin.inetback;

import com.sgurin.inetback.domain.Role;
import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.repository.RoleRepository;
import com.sgurin.inetback.repository.UserRepository;
import com.sgurin.inetback.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@Transactional
@Profile("test")
public class UserTests {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userServiceMock;

    @Test
    @Rollback(value = false)
    public void testCreateUser() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");

        User newUser = new User("sgurin.dev@gmail.com", password);
        User savedUser = userService.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    public void testRoles() {
        User user = userService.findByEmail("sgurin.dev@gmail.com");
        if (Objects.nonNull(user)) {
            Role role = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            if (Objects.nonNull(role)) {
                user.addRole(role);

                user = userService.save(user);
            }
        }
        assertEquals(user == null ? 0 : user.getRoles().size(), 1);
    }

    @Test
    public void mockUserService() {
        User expectedUser = new User("sgurin.dev@gmail.com", "test_password");

        UserService mockUserService = Mockito.mock(UserService.class);
        Mockito.when(mockUserService.findByEmail(Mockito.anyString())).thenReturn(expectedUser);

        User userByEmail = mockUserService.findByEmail("sgurin.dev@gmail.com");

        assertEquals("test_password", userByEmail.getPassword());
        Mockito.verify(mockUserService).findByEmail(Mockito.eq("sgurin.dev@gmail.com"));

        Mockito.doNothing().when(mockUserService).print(userByEmail);
        mockUserService.print(userByEmail);

        Mockito.verify(mockUserService).print(userByEmail);
        //Mockito.verify(mockUserService, Mockito.times(1)).print(userByEmail);
        //Mockito.verify(mockUserService, Mockito.never()).print(userByEmail);
    }

    @Test
    public void mockUserService2() {
        User expectedUser = new User("sgurin.dev@gmail.com", "test_password");

        Mockito.when(userRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(expectedUser));

        User userByEmail = userServiceMock.findByEmail("sgurin.dev@gmail.com");

        assertNotNull(userByEmail);
        assertEquals(expectedUser, userByEmail);
        Mockito.verify(userRepositoryMock).findByEmail(Mockito.eq("sgurin.dev@gmail.com"));
    }

    @Test
    public void spyUserService() {
        ArrayList<User> spyUserList = Mockito.spy(new ArrayList<>());
        Mockito.when(spyUserList.size()).thenReturn(10);

        int size = spyUserList.size();

        assertEquals(10, size);
        Mockito.verify(spyUserList).size();
    }
}
