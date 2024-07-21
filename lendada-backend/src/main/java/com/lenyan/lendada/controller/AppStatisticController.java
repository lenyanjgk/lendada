package com.lenyan.lendada.controller;


import com.lenyan.lendada.common.BaseResponse;
import com.lenyan.lendada.common.ErrorCode;
import com.lenyan.lendada.common.ResultUtils;
import com.lenyan.lendada.exception.ThrowUtils;
import com.lenyan.lendada.mapper.UserAnswerMapper;
import com.lenyan.lendada.model.dto.statistic.AppAnswerCountDTO;
import com.lenyan.lendada.model.dto.statistic.AppAnswerResultCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * App 统计分析接口
 *
 * @author <a href="https://github.com/lenyanjgk">程序员冷颜</a>
 * @from <a href="https://lenyan.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 热门应用及回答数统计（top 10）
     *
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }

    /**
     * 某应用回答结果分布统计
     *
     * @param appId
     * @return
     */
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }
}
