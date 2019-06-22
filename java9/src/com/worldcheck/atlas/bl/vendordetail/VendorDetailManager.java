package com.worldcheck.atlas.bl.vendordetail;

import com.worldcheck.atlas.dao.vendordetail.VendorDetailDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.VendorDetailVO;
import java.util.Iterator;
import java.util.List;

public class VendorDetailManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.vendordetail.VendorDetailManager");
	private VendorDetailDAO vendorDetailDAO;

	public void setVendorDetailDAO(VendorDetailDAO vendorDetailDAO) {
		this.vendorDetailDAO = vendorDetailDAO;
	}

	public VendorDetailVO searchVendorInformation(String vendorId, String crn) throws CMSException {
		return this.vendorDetailDAO.searchVendorInformation(vendorId, crn);
	}

	public List<VendorDetailVO> getVendorNameList(VendorDetailVO vendorDetailVO) throws CMSException {
		String[] roleArray = vendorDetailVO.getVisiableToAnalyst().split(",");
		vendorDetailVO.setVisiableToAnalyst("y");

		for (int i = 0; i < roleArray.length; ++i) {
			if (roleArray[i].equals("R1") || roleArray[i].equals("R2") || roleArray[i].equals("R3")
					|| roleArray[i].equals("R6") || roleArray[i].equals("R7") || roleArray[i].equals("R9")
					|| roleArray[i].equals("R12")) {
				vendorDetailVO.setVisiableToAnalyst("n");
			}
		}

		return this.vendorDetailDAO.getVendorNameList(vendorDetailVO);
	}

	public int saveVendorDetail(VendorDetailVO vendorDetailVO) throws CMSException {
		if (vendorDetailVO.getImmediateAttention().equalsIgnoreCase("true")) {
			vendorDetailVO.setImmediateAttention("1");
		} else {
			vendorDetailVO.setImmediateAttention("0");
		}

		return this.vendorDetailDAO.saveVendorDetail(vendorDetailVO);
	}

	public List<VendorDetailVO> getVendorDetailList(String crn, String visiableToAnalyst, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		String[] roleArray = visiableToAnalyst.split(",");
		visiableToAnalyst = "y";

		for (int i = 0; i < roleArray.length; ++i) {
			if (roleArray[i].equals("R1") || roleArray[i].equals("R2") || roleArray[i].equals("R3")
					|| roleArray[i].equals("R6") || roleArray[i].equals("R7") || roleArray[i].equals("R9")
					|| roleArray[i].equals("R12")) {
				visiableToAnalyst = "n";
			}
		}

		List<VendorDetailVO> vendorDetailList = this.vendorDetailDAO.getVendorDetailList(crn, visiableToAnalyst,
				start + 1, limit + start, sortColumnName, sortType);
		return vendorDetailList;
	}

	public int getVendorDetailSize(String crn, String visiableToAnalyst) throws CMSException {
		String[] roleArray = visiableToAnalyst.split(",");
		visiableToAnalyst = "y";

		for (int i = 0; i < roleArray.length; ++i) {
			if (roleArray[i].equals("R1") || roleArray[i].equals("R2") || roleArray[i].equals("R3")
					|| roleArray[i].equals("R6") || roleArray[i].equals("R7") || roleArray[i].equals("R9")
					|| roleArray[i].equals("R12")) {
				visiableToAnalyst = "n";
			}
		}

		return this.vendorDetailDAO.getVendorDetailSize(crn, visiableToAnalyst);
	}

	public VendorDetailVO getVendorInformation(String reqForVendorInvoiceId) throws CMSException {
		return this.vendorDetailDAO.getVendorInformation(reqForVendorInvoiceId);
	}

	public int deAssociatedVendorDetail(String vendorInvoiceId) throws CMSException {
		return this.vendorDetailDAO.deAssociatedVendorDetail(vendorInvoiceId);
	}

	public int updateVendorDetail(VendorDetailVO vendorDetailVO) throws CMSException {
		if (vendorDetailVO.getImmediateAttention().equalsIgnoreCase("true")) {
			vendorDetailVO.setImmediateAttention("1");
		} else {
			vendorDetailVO.setImmediateAttention("0");
		}

		return this.vendorDetailDAO.updateVendorDetail(vendorDetailVO);
	}

	public VendorDetailVO sendEmailTemplate(String vendorInvoiceId, String crn, String manager) throws CMSException {
		new VendorDetailVO();
		List<VendorDetailVO> vendorSubjectREInformation = null;
		String mailSubject = "IntegraScreen EDD has a new assignment for you";
		String primary = "";
		String mailBody = "Hello, %0D %0DScope of the assignment: %0D";
		VendorDetailVO vendorEmailInformation = this.vendorDetailDAO.getVendorEmailInformation(vendorInvoiceId);
		vendorSubjectREInformation = this.vendorDetailDAO.getVendorSubjectREInformation(crn, manager);

		VendorDetailVO vendorDetailVO;
		for (Iterator i$ = vendorSubjectREInformation.iterator(); i$.hasNext(); mailBody = mailBody + "Subject Name:"
				+ vendorDetailVO.getSubjectName() + " " + primary + ";  Country:" + vendorDetailVO.getCountry()
				+ "; %0DResearch Element:" + vendorDetailVO.getReNamesList() + "%0D") {
			vendorDetailVO = (VendorDetailVO) i$.next();
			primary = "";
			if (vendorDetailVO.getIsPrimary().equals("1")) {
				primary = "(primary)";
			} else {
				primary = "";
			}
		}

		if (null == vendorEmailInformation.getCost() || vendorEmailInformation.getCost().equals("")) {
			vendorEmailInformation.setCost("0.00");
		}

		if (null == vendorEmailInformation.getVendorDueDate() || "".equals(vendorEmailInformation.getVendorDueDate())) {
			vendorEmailInformation.setVendorDueDate(" - ");
		}

		if (null == vendorEmailInformation.getVendorMgrMesToVendor()
				|| "".equals(vendorEmailInformation.getVendorMgrMesToVendor())) {
			vendorEmailInformation.setVendorMgrMesToVendor(" - No Information");
		}

		mailBody = mailBody + "%0DAdditional Information:%0D";
		mailBody = mailBody + "%0D" + vendorEmailInformation.getVendorMgrMesToVendor() + "%0D";
		mailBody = mailBody + "%0DDue Date:" + vendorEmailInformation.getVendorDueDate() + "%0D";
		mailBody = mailBody + "%0DBudget:" + vendorEmailInformation.getCurrency() + " "
				+ vendorEmailInformation.getCost() + "%0D";
		mailBody = mailBody + "%0DPlease email or call " + vendorEmailInformation.getVendorManager()
				+ " and confirm your acceptance of this assignment.%0D";
		mailBody = mailBody + "%0DRegards,%0D%0D" + vendorEmailInformation.getVendorManager() + "%0D%0D";
		mailBody = mailBody
				+ "Note: Please insure confidentiality is maintained at all times. Always include the CRN in your invoice to IntegraScreen.";
		this.logger.debug(mailBody);
		vendorEmailInformation.setMailSubject(mailSubject);
		vendorEmailInformation.setMailBody(mailBody);
		return vendorEmailInformation;
	}
}