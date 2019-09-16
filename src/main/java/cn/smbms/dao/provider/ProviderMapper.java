package cn.smbms.dao.provider;

import java.sql.Connection;
import java.util.List;
import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

public interface ProviderMapper {
	
	/**
	 * 增加供应商
	 * @param provider
	 * @return
	 * @throws Exception
	 */
	int add(Provider provider)throws Exception;


	/**
	 * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
	 * @param proName
	 * @return
	 * @throws Exception
	 */
	List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode)throws Exception;
	
	/**
	 * 通过proId删除Provider
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	int deleteProviderById(@Param("id") String delId)throws Exception;

	
	/**
	 * 通过proId获取Provider
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Provider getProviderById(@Param("id") String id)throws Exception;
	
	/**
	 * 修改用户信息
	 * @return
	 * @throws Exception
	 */
	int modify(Provider provider)throws Exception;
}
