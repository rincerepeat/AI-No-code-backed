package org.example.ainocode.service;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.example.ainocode.model.dto.chathistory.ChatHistoryQueryRequest;
import org.example.ainocode.model.entity.ChatHistory;
import org.example.ainocode.model.entity.User;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/rincerepeat">rincerepeat</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {


    /**
     * 添加对话消息
     *
     * @param appId 应用id
     * @param message 消息内容
     * @param messageType 消息类型
     * @param userId 用户id
     * @return 是否添加成功
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 删除指定应用的对话消息
     *
     * @param appId 应用id
     * @return 是否删除成功
     */
    boolean deleteByAppId(Long appId);


    /**
     * 获取指定应用的对话消息（分页）
     *
     * @param appId 应用id
     * @param pageSize 页大小
     * @param lastCreateTime 最后创建时间
     * @param loginUser 登录用户
     * @return 对话消息列表
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 构造查询条件
     *
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
