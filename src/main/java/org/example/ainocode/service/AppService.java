package org.example.ainocode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.example.ainocode.model.dto.app.AppQueryRequest;
import org.example.ainocode.model.entity.App;
import org.example.ainocode.model.entity.User;
import org.example.ainocode.model.vo.AppVO.AppVO;
import reactor.core.publisher.Flux;

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
//通过聊天生成代码
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);
//应用部署
    String deployApp(Long appId, User loginUser);

    /**
     * 异步生成应用截图
     *
     * @param appId 应用ID
     * @param appUrl 应用URL
     */
    public void generateAppScreenshotAsync(Long appId, String appUrl);
}
