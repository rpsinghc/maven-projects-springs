package com.worldcheck.atlas.dao.vendordetail;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.VendorDetailVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class VendorDetailDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.vendordetail.VendorDetailDAO");

	public VendorDetailVO searchVendorInformation(String vendorId, String crn) throws CMSException {
		try {
			new VendorDetailVO();
			HashMap<String, String> map = new HashMap();
			map.put("vendorId", vendorId);
			map.put("crn", crn);
			this.logger.debug("Inside the VendorDetailDAO searchVendorInformation VendorId  :" + vendorId);
			VendorDetailVO vendorDetailVO = (VendorDetailVO) this.queryForObject("VendorDetail.searchVendorInformation",
					map);
			this.logger.debug("vendorDetailVo :" + vendorDetailVO);
			return vendorDetailVO;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int getCountExistVendorDetail(String vendorId, String crn) throws CMSException {
		try {
			HashMap<String, String> map = new HashMap();
			map.put("vendorId", vendorId);
			map.put("crn", crn);
			int count = Integer.parseInt(this.queryForObject("VendorDetail.getCountExistVendorDetail", map).toString());
			this.logger.debug("Vendor Detail Name Found:" + count);
			return count;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<VendorDetailVO> getVendorNameList(VendorDetailVO vendorDetailVO) throws CMSException {
		try {
			new ArrayList();
			List<VendorDetailVO> vendorNameList = this.queryForList("VendorDetail.getVendorNameList", vendorDetailVO);
			return vendorNameList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int saveVendorDetail(VendorDetailVO vendorDetailVO) throws CMSException {
		try {
			int vendorInvoiceId = (Integer) this.insert("VendorDetail.insertVendorDetail", vendorDetailVO);
			return vendorInvoiceId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<VendorDetailVO> getVendorDetailList(String crn, String visiableToAnalyst, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		try {
			this.logger.debug("Inside the VendorDetailDAO getVendorGridcrn" + crn + "start:" + start + "limit:" + limit
					+ "sortColumn   :" + sortColumnName + "sortType :" + sortType);
			HashMap<String, String> map = new HashMap();
			map.put("visiableToAnalyst", visiableToAnalyst);
			map.put("crn", crn);
			map.put("start", start + "");
			map.put("limit", limit + "");
			map.put("sortColumnName", sortColumnName);
			map.put("sortType", sortType);
			new ArrayList();
			List<VendorDetailVO> vendorDetailVOList = this.queryForList("VendorDetail.getVendorListGrid", map);
			this.logger.debug("countryGridList size :" + vendorDetailVOList.size());
			return vendorDetailVOList;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public int getVendorDetailSize(String crn, String visiableToAnalyst) throws CMSException {
		try {
			this.logger.debug("Inside the VendorDetailDAO getVendorDetailSizecrn" + crn);
			HashMap<String, String> map = new HashMap();
			map.put("visiableToAnalyst", visiableToAnalyst);
			map.put("crn", crn);
			int count = (Integer) this.queryForObject("VendorDetail.getVendorListSize", map);
			this.logger.debug("countryGridList size :" + count);
			return count;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public VendorDetailVO getVendorInformation(String vendorInvoiceId) throws CMSException {
		this.logger.debug("vendorInvoiceId:" + vendorInvoiceId);
		VendorDetailVO vendorDetailVO = null;

		try {
			this.logger.debug("Inside the VendorDetailDAO getVendorInformationvendorInvoiceId::" + vendorInvoiceId);
			new VendorDetailVO();
			vendorDetailVO = (VendorDetailVO) this.queryForObject("VendorDetail.getVendorInformation", vendorInvoiceId);
			this.logger.debug("" + vendorDetailVO.getVendorId());
			return vendorDetailVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deAssociatedVendorDetail(String vendorInvoiceId) throws CMSException {
		try {
			this.logger.debug("Inside the VendorDetailDAO getVendorInformationvendorInvoiceId::" + vendorInvoiceId);
			int count = Integer.valueOf(this.delete("VendorDetail.deAssociatedVendorDetail", vendorInvoiceId));
			this.logger.debug("row Deleted::" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateVendorDetail(VendorDetailVO vendorDetailVO) throws CMSException {
		try {
			int vendorInvoiceId = Integer.valueOf(this.update("VendorDetail.updateVendorDetail", vendorDetailVO));
			return vendorInvoiceId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public VendorDetailVO getVendorEmailInformation(String vendorInvoiceId) throws CMSException {
		this.logger.debug("vendorId:" + vendorInvoiceId);
		VendorDetailVO vendorDetailVO = null;

		try {
			this.logger
					.debug("Inside the VendorDetailDAO getVendorEmailInformationvendorInvoiceId::" + vendorInvoiceId);
			new VendorDetailVO();
			vendorDetailVO = (VendorDetailVO) this.queryForObject("VendorDetail.getVendorEmailInformation",
					vendorInvoiceId);
			return vendorDetailVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<VendorDetailVO> getVendorSubjectREInformation(String crn, String manager) throws CMSException {
		this.logger.debug("CRN:" + crn);
		HashMap<String, String> map = null;
		List vendorDetailVOList = null;

		try {
			map = new HashMap();
			map.put("crn", crn);
			map.put("manager", manager);
			this.logger.debug("Inside the VendorDetailDAO getVendorSUbjectREInformationCRN::" + crn);
			new ArrayList();
			vendorDetailVOList = this.queryForList("VendorDetail.getVendor_SUbject_RE_Information", map);
			return vendorDetailVOList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}
}