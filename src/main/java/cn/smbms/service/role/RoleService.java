package cn.smbms.service.role;

import java.util.List;

import cn.smbms.pojo.Role;

public interface RoleService {
	
	List<Role> getRoleList();

	Role getRoleByRoleCode(String roleCode);

	Boolean saveRole(Role role);

	Integer deleteRole(String id);

	Boolean modifyRole(Role role);

	Role getRoleById(String id);
}
