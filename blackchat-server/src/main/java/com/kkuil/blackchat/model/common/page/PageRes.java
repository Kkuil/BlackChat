package com.kkuil.blackchat.model.common.page;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @param <DataType>
 * @Description 分页响应返回的数据
 * @Author kkuil
 * @Date 2023/08/11 12:00
 */
@Data
@Accessors(chain = true)
public class PageRes<DataType> {
    /**
     * 当前页
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long pageSize;

    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private DataType data;
}
