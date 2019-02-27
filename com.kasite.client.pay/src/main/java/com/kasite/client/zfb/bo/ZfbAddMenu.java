package com.kasite.client.zfb.bo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="zfb_menu")
public class ZfbAddMenu {
	@Id
	@KeySql(useGeneratedKeys=true)
	//新增自定义菜单的实体类
	@Column(name="id")
	private long id;//id
	@Column(name="zfb_appId")
	private String zfbAppId;//公众号appid
	@Column(name="menu_type")
	private String menuType;//菜单类型
	@Column(name="menu_url")
	private String menuUrl;//菜单url
	@Column(name="menu_first_id")
	private int menuFirstId;//父级菜单id
	@Column(name="menu_first_name")
	private String menuFirstName;//父级菜单名字
	@Column(name="menu_second_id")
	private int menuSecondId;//子级菜单id
	@Column(name="menu_second_name")
	private String menuSecondName;//子级菜单名字
	@Column(name="menu_location")
	private String menuLocation;//菜单位置

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getZfbAppId() {
		return zfbAppId;
	}

	public void setZfbAppId(String zfbAppId) {
		this.zfbAppId = zfbAppId;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public int getMenuFirstId() {
		return menuFirstId;
	}

	public void setMenuFirstId(int menuFirstId) {
		this.menuFirstId = menuFirstId;
	}

	public String getMenuFirstName() {
		return menuFirstName;
	}

	public void setMenuFirstName(String menuFirstName) {
		this.menuFirstName = menuFirstName;
	}

	public int getMenuSecondId() {
		return menuSecondId;
	}

	public void setMenuSecondId(int menuSecondId) {
		this.menuSecondId = menuSecondId;
	}

	public String getMenuSecondName() {
		return menuSecondName;
	}

	public void setMenuSecondName(String menuSecondName) {
		this.menuSecondName = menuSecondName;
	}

	public String getMenuLocation() {
		return menuLocation;
	}

	public void setMenuLocation(String menuLocation) {
		this.menuLocation = menuLocation;
	}

	
	


	

}
