package com.example.framework.gson;

/**
 * FileName: TokenBean
 * Founder: LiuGuiLin
 * Profile: Token
 */
public class TokenBean {

    /**
     * code : 200
     * userId : c7a9b4794f
     * token : ZGMoS+CnH8p/Mq+IlDp14RQP/A1PxatKhEzbrIZl/JYmCYOPx6pM/FH4h2uHFHHMPL5wNhw//ixisf0XbIl5CpjtoZG+9gjw
     */
    /**
     * "code":200,
     * "userId":"a1d8a72b60",
     * "token":"G/h9ySNW93/QLZLFH9Zom00WPUxjCG7/6y/vcDsWdrk=@ynjp.cn.rongnav.com;ynjp.cn.rongcfg.com"
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
