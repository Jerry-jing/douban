package com.example.douban.bean;

public class Poetry {

    /**
     * content : 记得武陵相见日，六年往事堪惊。
     * source : 周紫芝《临江仙·送光州曾使君》
     */

    private String content;
    private String source;

    public Poetry(String content, String source) {
        this.content = content;
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
