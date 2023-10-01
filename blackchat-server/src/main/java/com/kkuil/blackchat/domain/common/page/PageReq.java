package com.kkuil.blackchat.domain.common.page;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @param <DataType>
 * @Description 分页请求携带的参数
 * @Author kkuil
 * @Date 2023/08/11 12:00
 */
@Data
@Accessors(chain = true)
public class PageReq<DataType> {
    /**
     * 当前页
     */
    @NotNull
    @Min(value = 1, message = "最小页码为1")
    private Long current;

    /**
     * 每页大小
     */
    @NotNull
    @Max(value = 50, message = "每页最大数量为50")
    @Min(value = 1, message = "每页最小数量为1")
    private Long pageSize;

    /**
     * 数据
     */
    private DataType data;
}
