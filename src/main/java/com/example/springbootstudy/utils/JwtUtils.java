package com.example.springbootstudy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    //7天过期
    private static long expire = 604800;
    //32位秘钥
    private static String secret ="abcdfghiabcdfghiabcdfghiabcdfghi";

    //生成token
    public static String generateToken(String username){
        Date now = new Date();
        Date expiration = new Date(now.getTime () + 1000*expire);
        return Jwts.builder()
                .setHeaderParam( "type","JWT") //设置头部信息，固定JWT
                .setSubject(username)		//设置载荷，存储用户信息
                .setIssuedAt(now)	//设置生效时间
                .setExpiration(expiration) //设置过期时间
                //指定签名算法，加入密钥
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }
    //解析token
    public static Claims getClaimsByToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
