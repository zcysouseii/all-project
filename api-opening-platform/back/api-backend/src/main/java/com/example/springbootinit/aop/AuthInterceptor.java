package com.example.springbootinit.aop;

import com.example.springbootinit.annotation.AuthCheck;
import com.example.springbootinit.common.ErrorCode;
import com.example.springbootinit.exception.BusinessException;
import com.example.springbootinit.model.entity.User;
import com.example.springbootinit.model.enums.UserRoleEnum;
import com.example.springbootinit.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 权限校验 AOP
 *
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 拿到的是通过@authcheck注解传进来的值，看了一下代码都是写死的，反正拿到的就是admin的字符串
        String mustRole = authCheck.mustRole();
        // 下面就是通过request拿到存储在session中的登录用户的信息呗
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 必须有该权限才通过
        // 这里的代码说实话挺神秘的，mustrole就是传进来的admin字符串的，它怎么会为空呢？
        if (StringUtils.isNotBlank(mustRole)) {
            // 通过字符串拿到枚举类对象。反正怎么拿都是admin这个枚举类对象，迷惑
            // 改一下把它改对，mustrole换成loginUser.geitUserRole()，获得的枚举类对象就是当前登录用户的权限
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
            // 同样这里是不可能为空的，改了之后就可以为空，但是理论上不可能为空，因为用户信息也是逻辑限制过的
            if (mustUserRoleEnum == null) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            // 这里是正常的，拿到登录用户的权限
            String userRole = loginUser.getUserRole();
            // 如果被封号，直接拒绝
            // 这里的比较很明显出错了，拿刚才取出的枚举类对象，也就是admin作比较，比较个鸡毛
            if (UserRoleEnum.BAN.equals(mustUserRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            // 必须有管理员权限
            if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!mustRole.equals(userRole)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

