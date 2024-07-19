package com.lenyan.lendada.model.dto.question;

import lombok.Data;

/**
 * 题目答案封装类（用于 AI 评分）
 *
 * @author <a href="https://github.com/lenyanjgk">程序员冷颜</a>
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 用户答案
     */
    private String userAnswer;
}
