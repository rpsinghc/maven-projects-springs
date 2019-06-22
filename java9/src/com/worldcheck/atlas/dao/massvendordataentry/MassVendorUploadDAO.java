package com.worldcheck.atlas.dao.massvendordataentry;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.massvendordataentry.MassVendorUploadVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class MassVendorUploadDAO extends SqlMapClientTemplate {
	private static final String UPDATE_MASSVENDOR_DATA = "AdminConsole.updateMassVendorData";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.massvendordataentry.MassVendorUploadDAO");
	private static final String INSERT_MASSVENDOR_DATA = "AdminConsole.insertMassVendorData";
	private static final String GET_VENDORINVOICEID_DATA = "AdminConsole.getVendorInvoiceId";
	private static final String INSERT_MASSVENDORMAP_DATA = "AdminConsole.insertMassVendorMapData";
	private static final String GET_CRN_DATA = "AdminConsole.getCRNData";
	private static final String GET_SUB_COUNT = "AdminConsole.getSubCount";
	private static final String GET_CRN_PID_DATA = "AdminConsole.getCRNPidData";

	public int insertData(HashMap dataMap) throws CMSException {
		boolean var2 = false;

		try {
			int vendorInvoiceId = (Integer) this.insert("AdminConsole.insertMassVendorData", dataMap);
			return vendorInvoiceId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateData(HashMap dataMap) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("AdminConsole.updateMassVendorData", dataMap);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<Integer> getExistingIds(HashMap parameterMap) throws CMSException {
		new ArrayList();

		try {
			List<Integer> idList = this.queryForList("AdminConsole.getVendorInvoiceId", parameterMap);
			return idList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int insertMapData(HashMap hashMap) throws CMSException {
		boolean var2 = false;

		try {
			int vendorInvoiceMapId = (Integer) this.insert("AdminConsole.insertMassVendorMapData", hashMap);
			return vendorInvoiceMapId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<MassVendorUploadVO> getCRNData(List<String> crnNumbers) throws CMSException {
		new ArrayList();

		try {
			List<MassVendorUploadVO> crnDataList = this.queryForList("AdminConsole.getCRNData", crnNumbers);
			return crnDataList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getSubjectCount(HashMap rowData) {
		int count = (Integer) this.queryForObject("AdminConsole.getSubCount", rowData);
		return count;
	}

	public List<MassVendorUploadVO> getCRNPIDData(List<String> crnNumbers) throws CMSException {
		new ArrayList();

		try {
			List<MassVendorUploadVO> crnDataList = this.queryForList("AdminConsole.getCRNPidData", crnNumbers);
			return crnDataList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}