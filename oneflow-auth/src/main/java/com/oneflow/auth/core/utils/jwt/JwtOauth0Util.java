package com.oneflow.auth.core.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oneflow.auth.security.entity.LoginUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Oauth0的jwt工具类：https://blog.csdn.net/weixin_57467236/article/details/126473631
 */
public class JwtOauth0Util {
    /**
     * 密钥
     */
    private static final String SECRET = "my_secret";
    /**
     * 过期时间
     **/
    private static final long EXPIRATION = 1000;//单位为秒

    /**
     * 生成用户token,设置token超时时间
     */
    public static String createToken(LoginUser loginUser) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("userId", loginUser.getUserId())//userId
                .withClaim("userName", loginUser.getUsername())//userName
                .withClaim("password", loginUser.getPassword())//password
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }

    /**
     * 校验token
     */
    public static boolean verifyToken(String token) {
        try{
            DecodedJWT jwt=JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    /**
     *  解析token
     * @return
     */
    public static DecodedJWT getIdToken(String token) throws JWTDecodeException{
          return JWT.decode(token);
    }
}

