package com.team3.ecommerce.jwt;
import com.team3.ecommerce.security.CustomerDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j // cơ chế ghi log của lombok
public class JwtTokenProvider {
    @Value("${com.example.jwt.secret}")
    private String JWT_SECRET; // là chuỗi để làm key mã hóa

    @Value("${com.example.jwt.expiration}")
    private int JWT_EXPIRATION;  // thời gian hiệu lực

    //tạo jwt từ thông tin của user
    public String generateToken(CustomerDetails customerDetails){
        Date now = new Date();
        // ngày hết hạn
        Date dateExpiration = new Date(now.getTime()+JWT_EXPIRATION);

        //tạo chuỗi jwt từ email
        return Jwts.builder().setSubject(customerDetails.getEmail())
                .setIssuedAt(now) // ngày bắt đầu có hiệu lực
                .setExpiration(dateExpiration) // ngày hết hạn
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET).compact();// chữ kí
                                //SignatureAlgorithm.ES512 : gải thuật để mã hóa,  JWT_SECRET: key để mã hóa
    }

    // Lấy thông tin customer từ jwt
    public String getCustomerFromJwt(String token){
        Claims claim =Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token).getBody();
        // trả lại thông tin userName
          return  claim.getSubject();
    }

    //Validate thông tin của JWT
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            log.error("Expired JWT token");
        }catch(UnsupportedJwtException ex){
            log.error("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            log.error("JWT claims String is empty");
        }
        return false;
    }


}
