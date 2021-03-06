package com.slabs.exchange.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final String SECRET_KEY = "jFNLmzRlGBtPCivnzogYJtYG070HR890";

    public static String encode(String userId) {
        Integer ept = 10080;  // 一周
        return JWTUtil.encode(userId, ept);
    }

    // 加密Token
    public static String encode(String userId, Integer exceptionTime) {
        Map<String, Object> claims = new HashMap<>();
        long nowMillis = System.currentTimeMillis();
        long expirationMillis = nowMillis + exceptionTime * 60000L;

        claims.put("sub", userId);
        return Jwts.builder()
                .setSubject("subValue")
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationMillis))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()).compact();
    }

    // 解密Token
    public static String decode(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(accessToken).getBody();
            return claims.get("sub").toString();
        } catch (Exception e) {  // 解密失败，返回null
            System.out.println(e.getMessage());
            return null;
        }
    }
}