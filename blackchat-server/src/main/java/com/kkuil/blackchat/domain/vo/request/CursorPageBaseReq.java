package com.kkuil.blackchat.domain.vo.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Author Kkuil
 * @Date 2023/10/1 13:10
 * @Description 游标翻页请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseReq<C> {

    /**
     * 页面大小
     */
    @Min(0)
    @Max(100)
    private Integer pageSize = 10;

    /**
     * 游标（初始为null，后续请求附带上次翻页的游标）
     */
    private C cursor;

    public Page plusPage() {
        return new Page(1, this.pageSize);
    }
}
