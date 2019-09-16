package cn.smbms.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Date;

public class Provider {

	private Integer id;   //id
	//注意包别导错了!!(import org.hibernate.validator.constraints.NotBlank;) NotBlank用于验证String NotNull验证基础数据类型 NotEmpty验证集合等
	@NotBlank(message = "供应商编码不能为空")
	private String proCode; //供应商编码
	@NotBlank(message = "供应商名称不能为空")
	private String proName; //供应商名称
	private String proDesc; //供应商描述
	@NotBlank(message = "供应商联系人不能为空")
	private String proContact; //供应商联系人
	@Size(max = 11,min = 11,message = "请输入11位电话格式")
	private String proPhone; //供应商电话
	private String proAddress; //供应商地址
	private String proFax; //供应商传真
	private Integer createdBy; //创建者
	private Date creationDate; //创建时间
	private Integer modifyBy; //更新者
	private Date modifyDate;//更新时间
	private String companyLicPicPath; //营业执照
	private String orgCodePicPath; //组织机构证书

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProCode() {
		return proCode;
	}
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getProDesc() {
		return proDesc;
	}
	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}
	public String getProContact() {
		return proContact;
	}
	public void setProContact(String proContact) {
		this.proContact = proContact;
	}
	public String getProPhone() {
		return proPhone;
	}
	public void setProPhone(String proPhone) {
		this.proPhone = proPhone;
	}
	public String getProAddress() {
		return proAddress;
	}
	public void setProAddress(String proAddress) {
		this.proAddress = proAddress;
	}
	public String getProFax() {
		return proFax;
	}
	public void setProFax(String proFax) {
		this.proFax = proFax;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Integer getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(Integer modifyBy) {
		this.modifyBy = modifyBy;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCompanyLicPicPath() {
		return companyLicPicPath;
	}

	public void setCompanyLicPicPath(String companyLicPicPath) {
		this.companyLicPicPath = companyLicPicPath;
	}

	public String getOrgCodePicPath() {
		return orgCodePicPath;
	}

	public void setOrgCodePicPath(String orgCodePicPath) {
		this.orgCodePicPath = orgCodePicPath;
	}
}
