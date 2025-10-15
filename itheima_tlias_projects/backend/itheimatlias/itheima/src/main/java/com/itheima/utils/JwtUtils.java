package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 设置过期时间为1小时
    private static final long EXPIRATION_TIME = 3600000; // 1小时 = 3600000毫秒

    // 密钥 (WGlhb2ZlbmcgaXMgdGhlIG1vc3QgYW1hemluZw== 是 "xiaofeng is the most amazing" 的Base64编码)
    private static final String SECRET_KEY = "WGlhb2ZlbmcgaXMgdGhlIG1vc3QgYW1hemluZw==";

    // 创建SecretKey实例
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * 生成JWT令牌
     * @param claims 要存储在令牌中的信息
     * @return 生成的JWT令牌字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)  // 添加声明信息
                .issuedAt(now)   // 签发时间
                .expiration(expirationDate)  // 过期时间
                .signWith(key)   // 使用密钥签名
                .compact();      // 生成JWT字符串
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌字符串
     * @return 解析后的Claims对象，包含令牌中的信息
     */
    public static Claims parseJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(key)  // 验证密钥
                .build()
                .parseSignedClaims(jwt)  // 解析JWT
                .getPayload();  // 获取载荷信息
    }
}
