package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-08-06
 */
@TableName("view_password")
public class ViewPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ViewPassword{" +
            "userName=" + userName +
            ", password=" + password +
        "}";
    }
}
