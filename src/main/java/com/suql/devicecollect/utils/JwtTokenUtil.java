package com.suql.devicecollect.utils;

import com.suql.devicecollect.service.RedisService;
import io.jsonwebtoken.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtTokenUtil {

    final static Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * jwt加密KEY
     */
    public static final String JWT_KEY = "secret_key";

    @Value("${spring.application.name}")
    public static String applicationName;

    /**
     * 生成jwt
     * @param userId
     * @return
     */
    public static String generateToken(Long userId) {
        final Date expirationDate = LocalDate.now().plusMonths(1).toDate();
        final Date createdDate = LocalDateTime.now().toDate();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(expirationDate)
                .setIssuedAt(createdDate)
                .signWith(SignatureAlgorithm.HS256, JWT_KEY)
                .compact();
    }

    public static Claims parseClaimsJws(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            return Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.info("解析jwt错误 {}", e.getMessage());
            return null;
        }
    }

    public static String createToken(Long userId, RedisService redisService) {
        String token = generateToken(userId);
        redisService.set(applicationName + ":token:" + userId, token);
        return token;
    }

    public static boolean verifyJwtToken(String token, RedisService redisService) {
        Claims claims = parseClaimsJws(token);
        if (claims == null) {
            return false;
        }
        String userId = claims.getSubject();
        if (userId == null) {
            return false;
        }
        String redisToken = redisService.get(applicationName + ":token:" + userId);
        return token.equals(redisToken);
    }
}
