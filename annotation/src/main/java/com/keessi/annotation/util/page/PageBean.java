package com.keessi.annotation.util.page;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 扩展PageHelper的PageInfo类，添加查询条件
 */
public class PageBean<T> extends PageInfo<T> {
    private Map<String, Object> keys;
    private String keyString;

    public PageBean(List<T> list) {
        super(list);
    }

    public boolean hasKey() {
        return keys != null;
    }

    public Map<String, Object> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, Object> keys) {
        this.keys = keys;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : keys.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (sb.length() > 0) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        this.keyString = sb.toString();
    }

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }
}
