package ${packageName}.model.dto.${dataKey};

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新${dataName}请求
 *
 * @author <a href="https://github.com/lenyanjgk">程序员冷颜</a>
 */
@Data
public class ${upperDataKey}UpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}