package pro.chinasoft.model;

import java.util.Date;

public class InMessage {

	private InUser inUser;
	private String content;
	private Date reviceDate;
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
	public Date getReviceDate() {
		return reviceDate;
	}
	public void setReviceDate(Date reviceDate) {
		this.reviceDate = reviceDate;
	}
	
}
