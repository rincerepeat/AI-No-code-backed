package org.example.ainocode.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.example.ainocode.exception.BusinessException;
import org.example.ainocode.exception.ErrorCode;
import org.example.ainocode.model.dto.user.UserQueryRequest;
import org.example.ainocode.model.entity.User;
import org.example.ainocode.mapper.UserMapper;
import org.example.ainocode.model.enums.UserRoleEnum;
import org.example.ainocode.model.vo.LoginUserVO;
import org.example.ainocode.model.vo.UserVO.UserVO;
import org.example.ainocode.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.ainocode.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author <a href="https://github.com/rincerepeat">rincerepeat</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
//     1.校验参数
if(StrUtil.hasBlank(userAccount,userPassword,checkPassword)){
    throw  new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
}
if(userAccount.length()<4){
    throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
}
if(userPassword.length()<8||checkPassword.length()<8){
    throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
}
if(!userPassword.equals(checkPassword)){
    throw  new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致");
}
//        2.查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(User::getUserAccount,userAccount);
   long count = this.mapper.selectCountByQuery(queryWrapper);
   if(count>0){
       throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户已存在");
   }
//      3.加密密码
String encryptPassword = getEncryptPassword(userPassword);
//4.创建用户,插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("匿名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败");
        }
        return user.getId();

    }

    @Override
    public String getEncryptPassword(String userPassword) {
       final  String salt = "rincerepeat";
       return DigestUtils.md5DigestAsHex((userPassword+salt).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
       if(user==null){
           return null;
       }
       LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user,loginUserVO);
        return loginUserVO;
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
//     1.校验参数
        if(StrUtil.hasBlank(userAccount,userPassword)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if(userPassword.length()<8){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
//2.加密
        String encryptPassword = getEncryptPassword(userPassword);
//       3.查询用户是否存在

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(User::getUserAccount,userAccount);
        queryWrapper.eq(User::getUserPassword,encryptPassword);
        User user=this.mapper.selectOneByQuery(queryWrapper);
        if(user==null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
//        4.如果用户存在，记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
//        5.获取脱敏用户信息

        return this.getLoginUserVO(user);

    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }


    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }
    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }



}
