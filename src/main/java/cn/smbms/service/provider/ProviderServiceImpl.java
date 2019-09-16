package cn.smbms.service.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {
	@Resource
	private ProviderMapper providerMapper;
	@Resource
	private BillMapper billMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(Provider provider) {
		try {
			return providerMapper.add(provider) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Provider> getProviderList(String proName,String proCode) {
		try {
			return providerMapper.getProviderList(proName,proCode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
	 * 若订单表中无该供应商的订单数据，则可以删除
	 * 若有该供应商的订单数据，则不可以删除
	 * 返回值billCount
	 * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
	 * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
	 * 
	 * ---判断
	 * 如果billCount = -1 失败
	 * 若billCount >= 0 成功
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteProviderById(String delId) {
		int billCount;
		try {
			billCount = billMapper.getBillCountByProviderId(delId);
			if (billCount == 0){
				providerMapper.deleteProviderById(delId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			billCount = -1;
		}
		return billCount;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Provider getProviderById(String id) {
		try {
			return providerMapper.getProviderById(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean modify(Provider provider) {
		try {
			return providerMapper.modify(provider) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
