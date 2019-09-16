package cn.smbms.dao.role;

import java.util.List;
import cn.smbms.pojo.Role;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
	
	List<Role> getRoleList()throws Exception;

	Role getRoleByRoleCode(@Param("roleCode")String roleCode);

	Integer saveRole(Role role);

	Integer deleteRole(@Param("id")String id);

	Integer modifyRole(Role role);

	Role getRoleById(@Param("id")String id);
}
