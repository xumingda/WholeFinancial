package com.ciba.wholefinancial.bean;

import java.util.List;

public class RightMenu {

	private String rightTitle;
	
	private List<GoodsBean> rightMenuItem;
	
	public String getRightTitle() {
		return rightTitle;
	}
	public void setRightTitle(String rightTitle) {
		this.rightTitle = rightTitle;
	}
	public List<GoodsBean> getRightMenuItem() {
		return rightMenuItem;
	}
	public void setRightMenuItem(List<GoodsBean> rightMenuItem) {
		this.rightMenuItem = rightMenuItem;
	}
	
	
}
