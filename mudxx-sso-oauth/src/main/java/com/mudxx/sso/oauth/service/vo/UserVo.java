package com.mudxx.sso.oauth.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author laiwen
 */
@Data
public class UserVo implements Serializable, UserDetails{
    private static final long serialVersionUID = 6900814554605029354L;

    private String userId;
    private String username;
    private String password;
    private boolean enabled;
    private List<String> permissions;

    public UserVo() {
        // TODO Auto-generated constructor stub
    }

    public UserVo(String userId, String username, String password, boolean enabled, List<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.permissions = permissions;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() { // 帐户是否过期
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() { // 帐户是否被冻结
        return true;
    }

    // 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {  // 帐号是否可用
        return enabled;
    }

    @Override
    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(permissions != null) {
            for (String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}