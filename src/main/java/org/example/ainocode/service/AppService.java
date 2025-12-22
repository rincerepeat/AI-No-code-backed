package org.example.ainocode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.example.ainocode.model.dto.app.AppQueryRequest;
import org.example.ainocode.model.entity.App;
import org.example.ainocode.model.vo.AppVO.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/rincerepeat">rincerepeat</a>
 */
public interface AppService extends IService<App> {
//获取应用封装类
     AppVO getAppVO(App app);

//    构造应用查询条件
     QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);
//获取应用封装列表
    List<AppVO> getAppVOList(List<App> appList);
}
