package com.fast.sns.service;

import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.fixture.UserEntityFixture;
import com.fast.sns.model.User;
import com.fast.sns.model.entity.UserEntity;
import com.fast.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 회원가입이_정상적으로_동작하는_경우(){

        String userName = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.fineByUserName(userName)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));

        Assertions.assertDoesNotThrow(()-> userService.join(userName, password));
    }

    @Test
    void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우(){

        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.fineByUserName(userName)).thenReturn(Optional.of(fixture));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        assertThrows(SnsApplicationException.class, ()-> userService.join(userName, password));
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우(){
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.fineByUserName(userName)).thenReturn(Optional.of(fixture));
        Assertions.assertDoesNotThrow(()-> userService.login(userName, password));
    }

    @Test
    void 로그인이_userName으로_회원가입한_유저가_없는_경우(){

        String userName = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.fineByUserName(userName)).thenReturn(Optional.empty());

        assertThrows(SnsApplicationException.class, ()-> userService.join(userName, password));
    }

    @Test
    void 로그인시_password가_틀린_경우(){
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password);
        //mocking
        when(userEntityRepository.fineByUserName(userName)).thenReturn(Optional.of(fixture));

        assertThrows(SnsApplicationException.class, ()-> userService.join(userName, password));
    }
}
