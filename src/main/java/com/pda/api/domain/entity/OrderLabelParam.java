package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-08-09
 */
@TableName("order_label_param")
public class OrderLabelParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String text;

    private String label;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "OrderLabelParam{" +
            "id=" + id +
            ", text=" + text +
            ", label=" + label +
        "}";
    }
}
