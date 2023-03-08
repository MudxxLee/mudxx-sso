package com.mudxx.sso.system;

import com.mudxx.sso.system.mapper.SSOUserMapper;
import com.mudxx.sso.system.pojo.SSOUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author laiw
 * @date 2023/3/8 10:57
 */
@SpringBootTest
public class SSOUserMapperTest {

    @Autowired
    private SSOUserMapper ssoUserMapper;

    @Test
    public void test1() {
        SSOUser ssoUser = ssoUserMapper.selectUserByUsername("admin");
        System.out.println(ssoUser);
    }

    @Test
    protected void test2() {
        List<String> permissions = ssoUserMapper.selectUserPermissions(1L);
        System.out.println(permissions);
    }

}
