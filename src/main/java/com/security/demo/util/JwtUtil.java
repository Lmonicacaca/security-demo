package com.security.demo.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
public class JwtUtil {
    public static final String SECRET = "qqskLoginToken";
    /**
     * token 过期时间: 7 天
     */
    public static final int calendarField = Calendar.DATE;

    public static String createToken(Long userId, int failureTime) {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, failureTime);
        Date expiresDate = nowTime.getTime();
        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create().withHeader(map) // header
            .withClaim("iss", "qqsk") // jwt签发这
            .withClaim("aud", "APP").withClaim("userId", null == userId ? null : userId.toString())
            .withIssuedAt(iatDate) // sign time
            .withExpiresAt(expiresDate) // expire time
            .sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }


    /**
     * 获取appid
     *
     * @param token
     * @return
     */
    public static String getAppUID(String token) {
        return tryGetAppId(token, 0);
    }

    /**
     * 重试获取appid
     *
     * @param token
     * @param trycnt
     * @return
     */
    private static String tryGetAppId(String token, int trycnt) {
        if (Objects.isNull(token) || token.trim().length() == 0) {
            log.info("token为空，返回空字符串");
            return "";
        }
        try {
            Map<String, Claim> claims = verifyToken(token);
            Claim userId = claims.get("userId");
            if (null == userId || StringUtils.isEmpty(userId.asString())) {
                // token 校验失败, 抛出Token验证非法异常
                return "";
            }
            return userId.asString();
        } catch (Exception ex) {
            if(ex instanceof TokenExpiredException){
                return "";
            }
            trycnt++;
            if (trycnt < 5) {
                tryGetAppId(token, trycnt);
            }
        }
        return "";
    }

    /**
     * 从request中token获取用户ID
     *
     * @param request
     * @return
     */
    public static String getUserIdByRequestToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token) || "undefined".equals(token)) {
            token = CookieUtil.getCookieValue(request, "jwt-token", true);
        }
        return getUserIdByToken(token);
    }

    /**
     * 从token获取用户ID
     *
     * @param token
     * @return userId
     */
    public static String getUserIdByToken(String token) {
        // 判断token
        if (StringUtils.isEmpty(token) || "undefined".equals(token)) {
            return null;
        }
        try {
            String uid = JwtUtil.getAppUID(token);
            if (uid == null || StringUtils.isEmpty(uid)) {
                String substring = token.substring(token.indexOf("."), token.lastIndexOf("."));
                byte[] bytes = Base64.decodeBase64(substring);
                JSONObject jsonObject = JSON.parseObject(new String(bytes, "utf-8"));
                Object userId = jsonObject.get("userId");
                uid = userId.toString();
            }
            return uid;
        } catch (Exception ex) {
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(createToken(11234263L,7));
    }
}
