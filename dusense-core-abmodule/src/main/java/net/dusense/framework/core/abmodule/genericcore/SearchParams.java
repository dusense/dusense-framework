package net.dusense.framework.core.abmodule.genericcore;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 对象查询参数,包含分页 = value. Attribute is like value. =~eq~ value. Attribute equals value.
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/09
 */
@Data
public class SearchParams extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    // 当前页码
    private int page = 1;
    // 每页条数
    private int limit = 10;

    private String orderbyasc;
    private String orderbydesc;

    /** sorts=~asc~name,~desc~age */
    private String sorts;

    private List<String> includeFields;
    private List<String> excludeFields;

    public SearchParams() {}

    public SearchParams(Map<String, Object> params) {
        this.putAll(params);
        // 分页参数
        if (Objects.nonNull(params.get("page"))) {
            this.page = Integer.parseInt(params.get("page").toString());
            this.remove("page");
        }
        if (Objects.nonNull(params.get("limit"))) {
            this.limit = Integer.parseInt(params.get("limit").toString());
            this.remove("limit");
        }

        if (Objects.nonNull(params.get("sorts"))) {
            this.sorts = (String) params.get("sorts");
            this.remove("sorts");
        }

        // 字段过滤等
        if (Objects.nonNull(params.get("includeFields"))) {
            includeFields = (List<String>) params.get("includeFields");
            this.remove("includeFields");
        } else if (Objects.nonNull(params.get("excludeFields"))) {
            excludeFields = (List<String>) params.get("excludeFields");
            this.remove("excludeFields");
        }
    }
}
