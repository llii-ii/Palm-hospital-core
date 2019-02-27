package com.kasite.client.wechat.message.req;

import java.util.Map;


/**
 * 地理位置
 * @author Administrator
 *
 */
public class MapMessage extends BaseMessage<MapMessage>{

	/**地理位置维度*/
	private String locationX;
	/**地理位置经度*/
	private String locationY;
	/**地图缩放大小*/
	private String scale;
	/**地理位置信息*/
	private String label;

	public String getLocationX() {
		return locationX;
	}
	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}
	public String getLocationY() {
		return locationY;
	}
	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public MapMessage parse(Map<String, String> map) throws Exception {
		super.parseBasic(map);
		this.locationX = map.get("Location_X");
		this.locationY = map.get("Location_Y");
		this.scale = map.get("Scale");
		this.label = map.get("Label");
		return this;
	}  
	 
	
	
}
