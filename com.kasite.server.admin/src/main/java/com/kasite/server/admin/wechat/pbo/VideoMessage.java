package com.kasite.server.admin.wechat.pbo;  
  
/**
 * 视频消息
 * @author 無
 *
 */
public class VideoMessage extends BaseMessage {  
    //视频
    private Video video ;
    //否     表示是否是保密消息，0表示否，1表示是，默认0
    private int safe;
    
    public Video getVideo() {
        return video;
    }
    public void setVideo(Video video) {
        this.video = video;
    }
    public int getSafe() {
        return safe;
    }
    public void setSafe(int safe) {
        this.safe = safe;
    }

}  