package org.example.ainocode.aop;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.ainocode.annotation.AuthCheck;
import org.example.ainocode.exception.BusinessException;
import org.example.ainocode.exception.ErrorCode;
import org.example.ainocode.model.entity.User;
import org.example.ainocode.model.enums.UserRoleEnum;
import org.example.ainocode.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
    String mustRole=authCheck.mustRole();
        RequestAttributes requestAttributes= RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request=((ServletRequestAttributes)requestAttributes).getRequest();
        //获取当前登录用户
        User loginUser=userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum=UserRoleEnum.getEnumByValue(mustRole);
        //不需要权限，直接放行
        if(mustRoleEnum==null){
            return joinPoint.proceed();
        }
        //以下的代码，必须要有权限才能放行
        UserRoleEnum userRoleEnum=UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        //没有权限，直接拒绝
        if(userRoleEnum==null){
            throw  new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须要有管理员权限
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw  new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();
    }
}
