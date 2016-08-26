
package com.moretv.entity;

import java.util.Date;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 历史播放记录
 * 
 * @author WangZx
 * 
 * Date 2013-01-28 下午05:03:02
 * 
 */
@Document(collection = "mcs_history")
public class History {
	
	@Id
	private ObjectId id;
	
	@Field(value="user_type")
	private String userType;
	
	private String uid;
	
	@Field(value="sid")
	private String sid;
	
	@Field(value="episode_sid")
	private String episodeSid;
	
	private Integer episode;
	
	private Integer second;
	
	@Field(value="total_second")
	private Integer totalSecond;
	
	@Field(value="play_over")
	private Boolean playOver;
	
	@Field(value="create_time")
	private Date createTime;
	
	@Field(value="update_time")
	private Date updateTime;
	
	/**节目观看的次数*/
	@Field(value="views")
	private Integer Views;
	
	/**热播*/
	@Field(value="code")
	private String code;

	@Field("browse_episode")
	private String browseEpisode;
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEpisodeSid() {
		return episodeSid;
	}

	public void setEpisodeSid(String episodeSid) {
		this.episodeSid = episodeSid;
	}

	public Integer getEpisode() {
		return episode;
	}

	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public Integer getTotalSecond() {
		return totalSecond;
	}

	public void setTotalSecond(Integer totalSecond) {
		this.totalSecond = totalSecond;
	}

	public Boolean getPlayOver() {
		return playOver;
	}

	public void setPlayOver(Boolean playOver) {
		this.playOver = playOver;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getViews() {
		return Views;
	}

	public void setViews(Integer views) {
		Views = views;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBrowseEpisode() {
		return browseEpisode;
	}

	public void setBrowseEpisode(String browseEpisode) {
		this.browseEpisode = browseEpisode;
	}

}
