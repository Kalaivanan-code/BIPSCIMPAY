package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_FLOW_TABLE")
public class IPSFlowTable {

	@Id
	@Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false)
    private long id;

    
    private String sequence_unique_id;
    private String msg_type;
    private String menu;
    private String action;
    private Date action_time;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSequence_unique_id() {
		return sequence_unique_id;
	}
	public void setSequence_unique_id(String sequence_unique_id) {
		this.sequence_unique_id = sequence_unique_id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getAction_time() {
		return action_time;
	}
	public void setAction_time(Date action_time) {
		this.action_time = action_time;
	}
	public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
    
    
}
