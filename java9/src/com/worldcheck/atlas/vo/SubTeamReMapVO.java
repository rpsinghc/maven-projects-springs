package com.worldcheck.atlas.vo;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SubTeamReMapVO {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.vo.SubTeamReMapVO");
	private int subTeamReMapId;
	private String crn;
	private String subjectId;
	private String subject;
	private String reId;
	private List<String> reIds;
	private String reName;
	private String revStatus;
	private int teamId;
	private String performer;
	private String performerFullName;
	private String updatedBy;
	private String jlpPoints;
	private int analystCount;
	private String country;
	private String teamName;
	private List<SubTeamReMapVO> reElementByTeam;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private String rejectREInfo;
	private String rejectREInfoForHistory;
	private String rejectionReason;
	private String processCycle;
	private String manager;
	private String reportsTo;
	private String teamTypeId;
	private String entityName;
	private String countryName;
	private String teamDisplayName;
	private String rejectedTaskStatus;
	private String rejectedFrom;
	private String officeId;
	private String office;
	private String reportsToFullName;
	private String managerId;
	private String managerFullName;
	private String mainAnalyst;
	private String analystFullName;

	public int hashCode() {
		this.logger.debug("subjectId :: " + this.subjectId + " :: reid :: " + this.reId + " :: teamId :: " + this.teamId
				+ " :: performer :: " + this.performer);
		String myString = this.subjectId + ":" + this.reId + ":" + this.teamId;
		if (null != this.performer && !this.performer.trim().isEmpty()) {
			myString = myString + ":" + this.performer;
		}

		try {
			byte[] bytesOfMessage = myString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public int hashCodeForPST() {
		this.logger
				.debug("subjectId :: " + this.subjectId + " :: reid :: " + this.reId + " :: teamId :: " + this.teamId);
		String myString = this.subjectId + ":" + this.reId + ":" + this.teamId;

		try {
			byte[] bytesOfMessage = myString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public int hashCodeWithJLP() {
		this.logger.debug("subjectId :: " + this.subjectId + " :: reid :: " + this.reId + " :: teamId :: " + this.teamId
				+ " :: jlpPoints :: " + this.jlpPoints + " :: performer :: " + this.performer);
		String jlp = "";
		if (null != this.jlpPoints) {
			if (this.jlpPoints.indexOf(".") == -1) {
				jlp = this.jlpPoints + ".0000";
			} else {
				if (this.jlpPoints.indexOf(".") == 0) {
					this.jlpPoints = "0" + this.jlpPoints;
				}

				int seperation = this.jlpPoints.length() - (this.jlpPoints.indexOf(".") + 1);
				if (seperation >= 4) {
					jlp = this.jlpPoints.substring(0, this.jlpPoints.length() - seperation + 4);
				} else if (seperation == 3) {
					jlp = this.jlpPoints + "0";
				} else if (seperation == 2) {
					jlp = this.jlpPoints + "00";
				} else if (seperation == 1) {
					jlp = this.jlpPoints + "000";
				}
			}
		}

		this.logger.debug(" jlpPoints :: " + this.jlpPoints + " :: jlp :: " + jlp);
		String myString = this.subjectId + ":" + this.reId + ":" + this.teamId;
		if (null != this.performer && !this.performer.trim().isEmpty()) {
			myString = myString + ":" + this.performer;
		}

		if (null != this.jlpPoints && !this.jlpPoints.trim().isEmpty()) {
			myString = myString + ":" + this.jlpPoints;
		}

		try {
			byte[] bytesOfMessage = myString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var7) {
			new CMSException(this.logger, var7);
			return 0;
		} catch (NoSuchAlgorithmException var8) {
			new CMSException(this.logger, var8);
			return 0;
		}
	}

	public int hashCodeForRE() {
		String myString = String.valueOf(this.reId);

		try {
			byte[] bytesOfMessage = myString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getReName() {
		return this.reName;
	}

	public void setReName(String reName) {
		this.reName = reName;
	}

	public void setRevStatus(String revStatus) {
		this.revStatus = revStatus;
	}

	public String getRevStatus() {
		return this.revStatus;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public String getPerformerFullName() {
		return this.performerFullName;
	}

	public void setPerformerFullName(String performerFullName) {
		this.performerFullName = performerFullName;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getReId() {
		return this.reId;
	}

	public void setReId(String reId) {
		this.reId = reId;
	}

	public List<String> getReIds() {
		return this.reIds;
	}

	public void setReIds(List<String> reIds) {
		this.reIds = reIds;
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getJlpPoints() {
		return this.jlpPoints;
	}

	public void setJlpPoints(String jlpPoints) {
		this.jlpPoints = jlpPoints;
	}

	public int getSubTeamReMapId() {
		return this.subTeamReMapId;
	}

	public void setSubTeamReMapId(int subTeamReMapId) {
		this.subTeamReMapId = subTeamReMapId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<SubTeamReMapVO> getReElementByTeam() {
		return this.reElementByTeam;
	}

	public void setReElementByTeam(List<SubTeamReMapVO> reElementByTeam) {
		this.reElementByTeam = reElementByTeam;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getRejectREInfo() {
		return this.rejectREInfo;
	}

	public void setRejectREInfo(String rejectREInfo) {
		this.rejectREInfo = rejectREInfo;
	}

	public String getRejectionReason() {
		return this.rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getRejectREInfoForHistory() {
		return this.rejectREInfoForHistory;
	}

	public void setRejectREInfoForHistory(String rejectREInfoForHistory) {
		this.rejectREInfoForHistory = rejectREInfoForHistory;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getReportsTo() {
		return this.reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getTeamTypeId() {
		return this.teamTypeId;
	}

	public void setTeamTypeId(String teamTypeId) {
		this.teamTypeId = teamTypeId;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getAnalystCount() {
		return this.analystCount;
	}

	public void setAnalystCount(int analystCount) {
		this.analystCount = analystCount;
	}

	public String getTeamDisplayName() {
		return this.teamDisplayName;
	}

	public void setTeamDisplayName(String teamDisplayName) {
		this.teamDisplayName = teamDisplayName;
	}

	public String getRejectedTaskStatus() {
		return this.rejectedTaskStatus;
	}

	public void setRejectedTaskStatus(String rejectedTaskStatus) {
		this.rejectedTaskStatus = rejectedTaskStatus;
	}

	public String getRejectedFrom() {
		return this.rejectedFrom;
	}

	public void setRejectedFrom(String rejectedFrom) {
		this.rejectedFrom = rejectedFrom;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getReportsToFullName() {
		return this.reportsToFullName;
	}

	public void setReportsToFullName(String reportsToFullName) {
		this.reportsToFullName = reportsToFullName;
	}

	public String getManagerId() {
		return this.managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManagerFullName() {
		return this.managerFullName;
	}

	public void setManagerFullName(String managerFullName) {
		this.managerFullName = managerFullName;
	}

	public String getMainAnalyst() {
		return this.mainAnalyst;
	}

	public void setMainAnalyst(String mainAnalyst) {
		this.mainAnalyst = mainAnalyst;
	}

	public String getAnalystFullName() {
		return this.analystFullName;
	}

	public void setAnalystFullName(String analystFullName) {
		this.analystFullName = analystFullName;
	}
}