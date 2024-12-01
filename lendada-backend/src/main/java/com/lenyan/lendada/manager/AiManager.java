package com.lenyan.lendada.manager;

import com.github.rholder.retry.*;
import com.lenyan.lendada.common.ErrorCode;
import com.lenyan.lendada.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 通用 AI 调用能力
 */
@Component
public class AiManager {

    @Resource
    private ClientV4 clientV4;

    // 稳定的随机数
    private static final float STABLE_TEMPERATURE = 0.05f;

    // 不稳定的随机数
    private static final float UNSTABLE_TEMPERATURE = 0.99f;


    /**
     * 同步请求重试
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    private String doSyncRequestWithRetry(List<ChatMessage> messages, Boolean stream, Float temperature) {
        Retryer<String> retryer = RetryerBuilder.<String>newBuilder()
                .retryIfException() // 出现异常时进行重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最大重试3次
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS)) // 每次重试间隔2秒
                .build();
        Callable<String> callable = () -> doRequest(messages, stream, temperature);
        try {
            return retryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "多次重试后仍然无法成功调用AI平台，请检查网络或其他问题");
        }
    }


    /**
     * 同步请求（答案不稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, UNSTABLE_TEMPERATURE);
    }

    /**
     * 同步请求（答案较稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }


    /**
     * 同步请求具有重试机会
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncRequestWithRetry(String systemMessage, String userMessage) {
        return doRequestRetry(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }



    /**
     * 同步请求
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }

    /**
     * 通用请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return doRequest(chatMessageList, stream, temperature);
    }


    /**
     * 通用请求重试
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequestRetry(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return doSyncRequestWithRetry(chatMessageList, stream, temperature);
    }

    /**
     * 通用请求 总
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(stream)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }


    /**
     * 流式请求重试
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequestWithRetry(String systemMessage, String userMessage, Float temperature) {
        Retryer<Flowable<ModelData>> retryer = RetryerBuilder.<Flowable<ModelData>>newBuilder()
                .retryIfException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                .build();
        Callable<Flowable<ModelData>> callable = () -> doStreamRequest(systemMessage,userMessage, temperature);
        try {
            return retryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "多次重试后仍然无法成功调用AI平台，请检查网络或其他问题");
        }
    }

    /**
     * 通用流式请求（简化消息传递）
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return doStreamRequest(chatMessageList, temperature);
    }


    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getFlowable();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
