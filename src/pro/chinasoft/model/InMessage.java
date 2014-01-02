package pro.chinasoft.model;

import java.util.Date;

public class InMessage {

	private InUser inUser;
	private String content;
	private Date createDate;
	private boolean type;  //type:true message from friends ,false:msg from yourself
	public InUser getInUser() {
		return inUser;
	}
	public void setInUser(InUser inUser) {
		this.inUser = inUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public boolean isType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	
	
}
