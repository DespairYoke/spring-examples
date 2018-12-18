package com.zwd.example.bean.life.domain;

/**
 * @author zwd
 * @date 2018/12/17 16:29
 * @Email stephen.zwd@gmail.com
 */
public class User1 {

    private String id;

    private String name;

    public void setBeanName(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User1{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
