package com.example.linebook.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class JwtTokenUtil {

    // key:token value:userId , check duplicate login
//    private static ConcurrentHashMap<Long, String>  tokenUserMap = new ConcurrentHashMap<>();

    private final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final static long expiration = 1000 * 60 * 60; // 1 hour

    public static String generateToken(Long userId, List<String> permissionList) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("permissionList", permissionList);

        String token= Jwts.builder()
                .addClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();

        // prevent duplicate login, clear first login
//        String oldToken = tokenUserMap.get(userId);
//        if (oldToken != null  && !oldToken.isEmpty()) {
//            getAllClaims(oldToken).clear();
//        }
//
//        tokenUserMap.put(userId, token);

        return token;
    }

    public static long getUserId(String token) {
        return ((Number) getAllClaims(token).get("userId")).longValue();
    }

    public static String getUsername(String token) {
        return (String) getAllClaims(token).get("username");
    }

    public static List<String> getUserPermissionList(String token) {
        return (List<String>) getAllClaims(token).get("permissionList");
    }


    // === Validate token ===
    public static boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // === Private helper ===
    private static Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }


}
