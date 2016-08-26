
package com.moretv.entity;

import java.util.Date;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 收藏
 * 
 * @author WangZx
 * 
 * Date 2012-12-29 下午05:03:02
 * 
 */
@Document(collection = "mcs_collect")
public class Collect {
	
	@Id
	private ObjectId id;
	
	@Field(value="user_type")
	private String userType;
	
	private String uid;
	
	private String sid;
	
	@Field(value="create_time")
	private Date createTime;
	
	@Field(value="update_time")
	private Date updateTime;
	
	//收藏节目的历史记录
	private Integer episode;
	
	private Integer second;
	
	@Field(value="play_over")
	private Boolean playOver;
	
	/**热播，如华语热播、动漫最热、少儿热播、综艺热播*/
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public Boolean getPlayOver() {
		return playOver;
	}

	public void setPlayOver(Boolean playOver) {
		this.playOver = playOver;
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
