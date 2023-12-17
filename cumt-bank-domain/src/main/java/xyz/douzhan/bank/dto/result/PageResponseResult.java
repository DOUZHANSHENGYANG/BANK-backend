package xyz.douzhan.bank.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * date: 2023/12/17 12:57
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true, chain = true)
@Schema(description = "统一返回列表结果")
public class PageResponseResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "状态码")
    private Integer code;
    @Schema(description = "分页列表数据")
    private List records;
    @Schema(description = "数据总数")
    private Long totals;
    @Schema(description = "当前页码")
    private Long currentPage;
    @Schema(description = "每页大小")
    private Long size;
    private PageResponseResult(){};
    public static <T> PageResponseResult success(List<T> records,Long totals,Long currentPage,Long size) {
        PageResponseResult<T> pageResponseResult = new PageResponseResult<>();
        pageResponseResult.code = 200;
        pageResponseResult.records = records;
        pageResponseResult.totals = totals;
        pageResponseResult.currentPage=currentPage;
        pageResponseResult.size=size;
        return pageResponseResult;
    }
    public static <T> PageResponseResult error() {
        PageResponseResult<T> responseResult = new PageResponseResult<>();
        responseResult.code = 400;
        return responseResult;
    }


}
