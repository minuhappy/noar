package com.noar.webapp.entity;

import com.noar.dbist.annotation.Column;
import com.noar.dbist.annotation.GenerationRule;
import com.noar.dbist.annotation.PrimaryKey;
import com.noar.dbist.annotation.Table;

@Table(name = "entities", idStrategy = GenerationRule.UUID)
public class Resource {

	@PrimaryKey
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "bundle")
	private String bundle;

	@Column(name = "table_name")
	private String tableName;

	@Column(name = "search_url", length = 64)
	private String searchUrl;

	@Column(name = "multi_save_url", length = 64)
	private String multiSaveUrl;

	@Column(name = "id_type", length = 15)
	private String idType;

	@Column(name = "id_field")
	private String idField;

	@Column(name = "title_field")
	private String titleField;

	@Column(name = "master_id")
	private String masterId;

	@Column(name = "association", length = 15)
	private String association;

	@Column(name = "data_prop")
	private String dataProp;

	@Column(name = "ref_field")
	private String refField;

	@Column(name = "del_strategy", length = 20)
	private String delStrategy;

	@Column(name = "fixed_columns")
	private Integer fixedColumns;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "ext_entity")
	private Boolean extEntity;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the bundle
	 */
	public String getBundle() {
		return bundle;
	}

	/**
	 * @param bundle the bundle to set
	 */
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the searchUrl
	 */
	public String getSearchUrl() {
		return searchUrl;
	}

	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	/**
	 * @return the multiSaveUrl
	 */
	public String getMultiSaveUrl() {
		return multiSaveUrl;
	}

	/**
	 * @param multiSaveUrl the multiSaveUrl to set
	 */
	public void setMultiSaveUrl(String multiSaveUrl) {
		this.multiSaveUrl = multiSaveUrl;
	}

	/**
	 * @return the idField
	 */
	public String getIdField() {
		return idField;
	}

	/**
	 * @param idField the idField to set
	 */
	public void setIdField(String idField) {
		this.idField = idField;
	}

	/**
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}

	/**
	 * @param idType the idType to set
	 */
	public void setIdType(String idType) {
		this.idType = idType;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the titleField
	 */
	public String getTitleField() {
		return titleField;
	}

	/**
	 * @param titleField the titleField to set
	 */
	public void setTitleField(String titleField) {
		this.titleField = titleField;
	}

	/**
	 * @return the masterId
	 */
	public String getMasterId() {
		return masterId;
	}

	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	/**
	 * @return the refField
	 */
	public String getRefField() {
		return refField;
	}

	/**
	 * @param refField the refField to set
	 */
	public void setRefField(String refField) {
		this.refField = refField;
	}

	/**
	 * @return the delStrategy
	 */
	public String getDelStrategy() {
		return delStrategy;
	}

	/**
	 * @param delStrategy the delStrategy to set
	 */
	public void setDelStrategy(String delStrategy) {
		this.delStrategy = delStrategy;
	}

	/**
	 * @return the association
	 */
	public String getAssociation() {
		return association;
	}

	/**
	 * @param association the association to set
	 */
	public void setAssociation(String association) {
		this.association = association;
	}

	/**
	 * @return the dataProp
	 */
	public String getDataProp() {
		return dataProp;
	}

	/**
	 * @param dataProp the dataProp to set
	 */
	public void setDataProp(String dataProp) {
		this.dataProp = dataProp;
	}

	/**
	 * @return the fixedColumns
	 */
	public Integer getFixedColumns() {
		return fixedColumns;
	}

	/**
	 * @param fixedColumns the fixedColumns to set
	 */
	public void setFixedColumns(Integer fixedColumns) {
		this.fixedColumns = fixedColumns;
	}
}