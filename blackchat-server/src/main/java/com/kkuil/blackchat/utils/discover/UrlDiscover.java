package com.kkuil.blackchat.utils.discover;

import com.kkuil.blackchat.utils.discover.domain.UrlInfo;
import org.jsoup.nodes.Document;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @Author Kkuil
 * @Date 2023/9/26
 * @Description url发现
 */
public interface UrlDiscover {

    @Nullable
    Map<String, UrlInfo> getUrlContentMap(String content);

    @Nullable
    UrlInfo getContent(String url);

    @Nullable
    String getTitle(Document document);

    @Nullable
    String getDescription(Document document);

    @Nullable
    String getImage(String url, Document document);
}
