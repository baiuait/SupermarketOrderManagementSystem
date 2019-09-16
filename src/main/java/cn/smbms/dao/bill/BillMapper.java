package cn.smbms.dao.bill;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.Bill;
import org.apache.ibatis.annotations.Param;

public interface BillMapper {
	/**
	 * 增加订单
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	int add(Bill bill)throws Exception;


	/**
	 * 通过查询条件获取供应商列表-模糊查询-getBillList
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	List<Bill> getBillList(@Param("productName") String productName, @Param("providerId") Integer providerId, @Param("isPayment")Integer isPayment,
						   @Param("currentPage")Integer currentPage, @Param("pageSize")Integer pageSize)throws Exception;
	
	/**
	 * 通过delId删除Bill
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	int deleteBillById(@Param("id") String delId)throws Exception;
	
	
	/**
	 * 通过billId获取Bill
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Bill getBillById(@Param("id") String id)throws Exception;
	
	/**
	 * 修改订单信息
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	int modify(Bill bill)throws Exception;

	/**
	 * 根据供应商ID查询订单数量
	 * @param providerId
	 * @return
	 * @throws Exception
	 */
	int getBillCountByProviderId(@Param("providerId")String providerId)throws Exception;

	int getBillCount(Bill bill);
}
