package cn.smbms.service.role;

import java.sql.Connection;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.role.RoleMapper;
import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("roleService")
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private UserMapper userMapper;
	
	@Override
	public List<Role> getRoleList() {
		try {
			return roleMapper.getRoleList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Role getRoleByRoleCode(String roleCode) {
		try {
			return roleMapper.getRoleByRoleCode(roleCode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Boolean saveRole(Role role) {
		try {
			return roleMapper.saveRole(role) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer deleteRole(String id) {
		try {
			Integer count = userMapper.getUserCountByRole(id);
			if(count == 0)
				roleMapper.deleteRole(id);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Boolean modifyRole(Role role) {
		try {
			return roleMapper.modifyRole(role) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Role getRoleById(String id) {
		try {
			return roleMapper.getRoleById(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
