package com.kkuil.blackchat.domain.vo.response;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/10/1 13:07
 * @Description 游标响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursorPageBaseResp<T, C> {

    /**
     * 游标（下次翻页带上这参数）
     */
    private C cursor;

    /**
     * 是否最后一页
     */
    private Boolean isLast = Boolean.FALSE;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 额外字段
     */
    private Object extraInfo;

    public static <T, C> CursorPageBaseResp<T, C> init(CursorPageBaseResp<?, C> cursorPage, List<T> list) {
        CursorPageBaseResp<T, C> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp.setIsLast(cursorPage.getIsLast());
        cursorPageBaseResp.setList(list);
        cursorPageBaseResp.setCursor(cursorPage.getCursor());
        return cursorPageBaseResp;
    }

    @JsonIgnore
    public Boolean isEmpty() {
        return CollectionUtil.isEmpty(list);
    }

    public static <T, C> CursorPageBaseResp<T, C> empty() {
        CursorPageBaseResp<T, C> cursorPageBaseResp = new CursorPageBaseResp<T, C>();
        cursorPageBaseResp.setIsLast(true);
        cursorPageBaseResp.setList(new ArrayList<T>());
        return cursorPageBaseResp;
    }

}
