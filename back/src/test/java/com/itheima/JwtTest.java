package com.itheima;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

//    测试JWT生成
    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "张三");
        //生成jwt的代码
        String token = JWT.create()
                .withClaim("user", claims)//添加载荷
                    // System.currentTimeMillis() 指的是当前毫秒时
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1000))//添加过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*12))//添加过期时间为12小时
                .sign(Algorithm.HMAC256("itheima"));//指定加密算法,配置秘钥

        System.out.println(token);

    }

    //    测试JWT验证
    @Test
    public void testParse() {
        //定义字符串,模拟用户传递过来的token
//        TODO: 执行testParse要先通过testGen获取token，再将token替换下面的token，记住删除后面的\n
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3MjM5Mzk4NTh9.0T9hpvOZUpA37xjPK9T-ofb7-qn2cs4mDvtypHrRsFk";
//
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("itheima")).build();
//
//        DecodedJWT decodedJWT = jwtVerifier.verify(token);//验证token,生成一个解析后的JWT对象
//        Map<String, Claim> claims = decodedJWT.getClaims();
//        System.out.println(claims.get("user"));

        //如果篡改了头部和载荷部分的数  据,那么验证失败
        //如果秘钥改了,验证失败
        //token过期
    }
}
