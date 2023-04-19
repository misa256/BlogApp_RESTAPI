package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    //@Value : プロパティファイルから指定したプロパティの値をインジェクトしてくる
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //generate JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                //Signs the constructed JWT with the specified key using the key's recommended signature algorithm, producing a JWS.
                .signWith(key())
                .compact();

        return token;

    }

    private Key key(){
        //Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //get username from JWT token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                //Parses the specified compact serialized JWS string based on the builder's current configuration state and returns the resulting Claims JWS instance.
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        return  username;
    }

    //validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    //Parses the specified compact serialized JWT string based on the builder's current configuration state and returns the resulting JWT or JWS instance.
                    //parse : 解析する
                    .parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Expired JWT token");
        } catch (MalformedJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
        } catch (UnsupportedJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"JWT claims string is empty");
        }

    }

}
