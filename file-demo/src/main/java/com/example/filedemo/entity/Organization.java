package com.example.filedemo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organization_")
public class Organization {

	@Id
	private long organizationId;
	private long ctCollectionId;
	private String uuid_;
	private String externalReferenceCode;
	private long companyId;
	private long userId;
	private String userName;
	private Date createDate;
	private Date modifiedDate;
	private long parentOrganizationId;
	private String treePath;
	private String name;
	private String type_;
	private boolean recursable;
	private long regionId;
	private long countryId;
	private long statusId;
	private String comments;
	private long logoId;
	private long mvccVersion;

	public Organization() {
		super();
	}

	public Organization(long ctCollectionId, long organizationId, String uuid_, String externalReferenceCode,
			long companyId, long userId, String userName, Date createDate, Date modifiedDate, long parentOrganizationId,
			String treePath, String name, String type_, boolean recursable, long regionId, long countryId,
			long statusId, String comments, long logoId, long mvccVersion) {
		super();
		this.ctCollectionId = ctCollectionId;
		this.organizationId = organizationId;
		this.uuid_ = uuid_;
		this.externalReferenceCode = externalReferenceCode;
		this.companyId = companyId;
		this.userId = userId;
		this.userName = userName;
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.parentOrganizationId = parentOrganizationId;
		this.treePath = treePath;
		this.name = name;
		this.type_ = type_;
		this.recursable = recursable;
		this.regionId = regionId;
		this.countryId = countryId;
		this.statusId = statusId;
		this.comments = comments;
		this.logoId = logoId;
		this.mvccVersion = mvccVersion;
	}

	public long getMvccVersion() {
		return mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	public long getCtCollectionId() {
		return ctCollectionId;
	}

	public void setCtCollectionId(long ctCollectionId) {
		this.ctCollectionId = ctCollectionId;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getUuid_() {
		return uuid_;
	}

	public void setUuid_(String uuid_) {
		this.uuid_ = uuid_;
	}

	public String getExternalReferenceCode() {
		return externalReferenceCode;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		this.externalReferenceCode = externalReferenceCode;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public long getParentOrganizationId() {
		return parentOrganizationId;
	}

	public void setParentOrganizationId(long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType_() {
		return type_;
	}

	public void setType_(String type_) {
		this.type_ = type_;
	}

	public boolean isRecursable() {
		return recursable;
	}

	public void setRecursable(boolean recursable) {
		this.recursable = recursable;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public long getLogoId() {
		return logoId;
	}

	public void setLogoId(long logoId) {
		this.logoId = logoId;
	}

}
