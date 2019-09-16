package cn.smbms.service.bill;

import java.util.List;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("billService")
public class BillServiceImpl implements BillService {
	@Resource
	private BillMapper billMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(Bill bill) {
		try {
			return billMapper.add(bill) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Bill> getBillList(String productName, Integer providerId, Integer isPayment,
			Integer currentPage, Integer pageSize) {
		try {
			return billMapper.getBillList(productName, providerId, isPayment, (currentPage-1)*pageSize, pageSize);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public int getBillCount(Bill bill) {
		try {
			return billMapper.getBillCount(bill);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteBillById(String delId) {
		try {
			return billMapper.deleteBillById(delId) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Bill getBillById(String id) {
		try {
			return billMapper.getBillById(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean modify(Bill bill) {
		try {
			return billMapper.modify(bill) > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
