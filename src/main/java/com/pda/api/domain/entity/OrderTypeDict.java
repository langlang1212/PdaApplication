package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-09-04
 */
@TableName("order_type_dict")
public class OrderTypeDict implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String text;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "OrderTypeDict{" +
            "code=" + code +
            ", name=" + name +
            ", text=" + text +
        "}";
    }
}
