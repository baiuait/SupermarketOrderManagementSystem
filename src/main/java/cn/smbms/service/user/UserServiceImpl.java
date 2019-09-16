package cn.smbms.service.user;

import java.util.List;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 * @author Administrator
 *
 */
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService{
	@Resource
	private UserMapper userMapper;
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(User user) {
		try {
			return userMapper.add(user) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		boolean flag = false;
//		Connection connection = null;
//		try {
//			connection = BaseDao.getConnection();
//			connection.setAutoCommit(false);//开启JDBC事务管理
//			int updateRows = userMapper.add(connection,user);
//			connection.commit();
//			if(updateRows > 0){
//				flag = true;
//				System.out.println("add success!");
//			}else{
//				System.out.println("add failed!");
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			try {
//				System.out.println("rollback==================");
//				connection.rollback();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}finally{
//			//在service层进行connection连接的关闭
//			BaseDao.closeResource(connection, null, null);
//		}
//		return flag;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public User login(String userCode, String userPassword) {
		try {
			User user = userMapper.getLoginUser(userCode);
			//匹配密码
			if(null != user){
				if(!user.getUserPassword().equals(userPassword))
					user = null;
			}
			return user;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo, int pageSize) {
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		System.out.println("currentPageNo ---- > " + currentPageNo);
		System.out.println("pageSize ---- > " + pageSize);
		try {
			List<User> userList = userMapper.getUserList(queryUserName,queryUserRole,(currentPageNo-1)*pageSize,pageSize);
			return userList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public User selectUserCodeExist(String userCode) {
		User user = null;
		try {
			user = userMapper.getLoginUser(userCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteUserById(Integer delId) {
		try {
			return userMapper.deleteUserById(delId) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		Connection connection = null;
//		boolean flag = false;
//		try {
//			connection = BaseDao.getConnection();
//			if(userMapper.deleteUserById(connection,delId) > 0)
//				flag = true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			BaseDao.closeResource(connection, null, null);
//		}
//		return flag;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public User getUserById(String id) {
		try {
			User user = userMapper.getUserById(id);
			return user;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		Connection connection = null;
//		try{
//			connection = BaseDao.getConnection();
//			user = userMapper.getUserById(id);
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			user = null;
//		}finally{
//			BaseDao.closeResource(connection, null, null);
//		}
//		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean modify(User user) {
		try {
			return userMapper.modify(user) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

//		Connection connection = null;
//		boolean flag = false;
//		try {
//			connection = BaseDao.getConnection();
//			if(userMapper.modify(connection,user) > 0)
//				flag = true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			BaseDao.closeResource(connection, null, null);
//		}
//		return flag;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updatePwd(int id, String pwd) {
		try {
			int updatePwd = userMapper.updatePwd(id, pwd);
			return updatePwd > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		boolean flag = false;
//		Connection connection = null;
//		try{
//			connection = BaseDao.getConnection();
//			if(userMapper.updatePwd(connection,id,pwd) > 0)
//				flag = true;
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally{
//			BaseDao.closeResource(connection, null, null);
//		}
//		return flag;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public int getUserCount(String queryUserName, int queryUserRole) {
		try {
			System.out.println("queryUserName ---- > " + queryUserName);
			System.out.println("queryUserRole ---- > " + queryUserRole);
			return userMapper.getUserCount(queryUserName,queryUserRole);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		Connection connection = null;
//		int count = 0;
//		System.out.println("queryUserName ---- > " + queryUserName);
//		System.out.println("queryUserRole ---- > " + queryUserRole);
//		try {
//			connection = BaseDao.getConnection();
//			count = userMapper.getUserCount(connection, queryUserName,queryUserRole);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			BaseDao.closeResource(connection, null, null);
//		}
//		return count;
	}
}
