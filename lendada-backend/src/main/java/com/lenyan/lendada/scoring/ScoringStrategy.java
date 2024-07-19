package com.lenyan.lendada.scoring;


import com.lenyan.lendada.model.entity.App;
import com.lenyan.lendada.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 *
 * @author <a href="https://github.com/lenyanjgk">程序员冷颜</a>
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     *
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}