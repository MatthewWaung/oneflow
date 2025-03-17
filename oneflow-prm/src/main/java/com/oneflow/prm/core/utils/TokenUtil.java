package com.oneflow.prm.core.utils;

import cn.hutool.core.util.IdUtil;
import com.oneflow.prm.core.constant.Constants;
import com.oneflow.prm.entity.dao.sys.SysRole;
import com.oneflow.prm.entity.dao.sys.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * TokenUtil中的方法（如getLoginUser）被用在WebLogAspect、DataScopeAspect、WhiteUrlHandler、ContextUtil等需要用到当前用户登录信息（如工号、手机号）的地方
 */
@Slf4j
@Component
public class TokenUtil {

    private static final String HEADER = "Authorization";
    private static final String SECRET = "abcdefghklmn";

    public static String getHEADER() {
        return HEADER;
    }

    public static String getSECRET() {
        return SECRET;
    }

    /**
     * 获取当前请求线程的用户
     *
     * @return
     */
    public static SysUser getLoginUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return getLoginUser(request);
    }

    /**
     * 获取用户身份信息
     *
     * @param request
     * @return
     */
    public static SysUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            if (Objects.nonNull(claims)) {
                //解析对应的权限及用户信息
                Integer userId = (Integer) claims.get(Constants.LOGIN_USER_ID);
                String userName = (String) claims.get(Constants.LOGIN_USER_NAME);
                String realName = (String) claims.get(Constants.LOGIN_USER_REAL_NAME);
                String deptId = (String) claims.get(Constants.LOGIN_USER_DEPT_ID);
                String deptName = (String) claims.get(Constants.LOGIN_USER_DEPT_NAME);
                List<SysRole> roles = (List<SysRole>) claims.get(Constants.LOGIN_USER_ROLE);
                SysUser user = new SysUser();
                user.setUserId(Long.valueOf(userId));
                user.setUserName(userName);
                user.setDeptId(deptId);
                user.setDeptName(deptName);
                user.setRoles(roles);
                return user;
            }
        }
        return null;
    }

    /**
     * 获取请求中的token
     *
     * @param request
     * @return
     */
    private static String getToken(HttpServletRequest request) {
        String token = "";
        if (Objects.nonNull(request)) {
            token = request.getHeader(getHEADER());
        }
        if (StringUtils.isBlank(token) && StringUtils.isNotBlank(MDC.get(getHEADER()))) {
            token = MDC.get(getHEADER());
        }
        if (StringUtils.isNotBlank(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token
     * @return
     */
    private static Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSECRET())
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
            log.warn("访问令牌不合法或已过期！");
        }
        return null;
    }

    /**
     * 数据声明生成令牌
     *
     * @param claims
     * @return
     */
    private static String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, getSECRET())
                .compact();
        return token;
    }

    public static String createTempToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, IdUtil.fastUUID());
        claims.put(Constants.LOGIN_USER_ID, 1L);
        claims.put(Constants.LOGIN_USER_NAME, "admin");
        claims.put(Constants.LOGIN_USER_REAL_NAME, "管理员");
        claims.put(Constants.LOGIN_USER_DEPT_ID, 999L);
        claims.put(Constants.LOGIN_USER_DEPT_NAME, "");
        SysRole sysRole = new SysRole();
        sysRole.setRoleId(1L);
        sysRole.setRoleName("管理员");
        sysRole.setRoleKey("admin");
        sysRole.setDataScope("1");
        sysRole.setStatus("0");
        List<SysRole> roles = new ArrayList<>();
        roles.add(sysRole);
        claims.put(Constants.LOGIN_USER_ROLE, roles);
        String token = createToken(claims);
        return Constants.TOKEN_PREFIX + token;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token
     * @return
     */
    public String getTokenUsername(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

}
