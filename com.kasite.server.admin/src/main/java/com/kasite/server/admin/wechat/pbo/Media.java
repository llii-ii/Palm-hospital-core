package com.kasite.server.admin.wechat.pbo;

/**
 * 图片、语音、文件
 * @author 無
 *
 */
public class Media {
    //是     图片/语音/文件 媒体文件id，可以调用上传临时素材接口获取
    private String media_id;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }    
    
    
}