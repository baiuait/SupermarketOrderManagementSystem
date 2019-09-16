package cn.smbms.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Calendar;
import java.util.Date;

public class User {
	private Integer id; //id
	@NotBlank(message = "用户编码不能为空")
	private String userCode; //用户编码
	@NotBlank(message = "用户名称不能为空")
	private String userName; //用户名称
	@NotBlank(message = "用户密码不能为空")
	@Length(min = 6,max = 10,message = "用户密码长度为6-10")
	private String userPassword; //用户密码
	private Integer gender;  //性别
	@Past(message = "必须是过去的一个时间")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")  //解决JSON数据传递的日期格式问题
	private Date birthday;  //出生日期
	private String phone;   //电话
	private String address; //地址
	private Integer userRole;    //用户角色
	private Integer createdBy;   //创建者
	private Date creationDate; //创建时间
	private Integer modifyBy;     //更新者
	private Date modifyDate;   //更新时间
	private String idPicPath; //照片路径
	private String workPicPath; //工作证照片路径
	
	private Integer age;//年龄
	
	private String userRoleName;    //用户角色名称
	
	
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public Integer getAge() {
		/*long time = System.currentTimeMillis()-birthday.getTime();
		Integer age = Long.valueOf(time/365/24/60/60/1000).IntegerValue();*/
		Calendar now = Calendar.getInstance();
		Calendar birthday = Calendar.getInstance();
		birthday.setTime(this.birthday);
		return now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
	}

	public String getWorkPicPath() {
		return workPicPath;
	}

	public void setWorkPicPath(String workPicPath) {
		this.workPicPath = workPicPath;
	}

	public String getIdPicPath() {
		return idPicPath;
	}

	public void setIdPicPath(String idPicPath) {
		this.idPicPath = idPicPath;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getUserRole() {
		return userRole;
	}
	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
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
}
