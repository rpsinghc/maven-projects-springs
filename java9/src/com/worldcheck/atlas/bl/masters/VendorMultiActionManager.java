package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.IVendorMaster;
import com.worldcheck.atlas.dao.masters.VendorMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.masters.VendorUploadMasterVO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VendorMultiActionManager implements IVendorMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.VendorMultiActionManager");
	VendorMultiActionDAO vendorMultiActionDAO = null;
	PropertyReaderUtil propertyReader = null;

	public void setVendorMultiActionDAO(VendorMultiActionDAO vendorMultiActionDAO) {
		this.vendorMultiActionDAO = vendorMultiActionDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public VendorMasterVO saveVendor(VendorMasterVO vendorMasterVO, boolean isUpdate,
			List<VendorUploadMasterVO> vendorTempUploadsList, List<VendorUploadMasterVO> vendorUploadsToDeleteList)
			throws CMSException {
		Integer vendorMasterId = null;
		if (isUpdate) {
			this.vendorMultiActionDAO.updateVendor(vendorMasterVO);
			vendorMasterId = vendorMasterVO.getVendorMasterId();
		} else {
			vendorMasterId = this.vendorMultiActionDAO.insertVendor(vendorMasterVO);
		}

		vendorMasterVO.setVendorMasterId(vendorMasterId);
		this.vendorMultiActionDAO.insertVendorCountryMappings(vendorMasterVO);
		if (isUpdate) {
			if (vendorUploadsToDeleteList != null && vendorUploadsToDeleteList.size() > 0) {
				List<String> vendorFileNames = this.deleteVendorFilesFromDisk(vendorUploadsToDeleteList);
				this.vendorMultiActionDAO.deleteVendorUploads(vendorMasterVO.getVendorCode(), vendorFileNames);
			}

			if (vendorTempUploadsList != null && vendorTempUploadsList.size() > 0) {
				File dir = this.getVendorUploadDirectory(vendorMasterVO.getVendorCode());
				File tempPathDir = this.getTempVendorUploadDirectory(vendorMasterVO.getUpdatedBy());
				Iterator i$ = vendorTempUploadsList.iterator();

				while (i$.hasNext()) {
					VendorUploadMasterVO vendorUpload = (VendorUploadMasterVO) i$.next();
					File tempFile = new File(tempPathDir, vendorUpload.getFileName());
					File file = new File(dir, vendorUpload.getFileName());
					this.logger.debug("in VendorMultiActionManager::saveVendor::vendorUpload.getFileName() "
							+ vendorUpload.getFileName());
					this.logger
							.debug("in VendorMultiActionManager::saveVendor::tempFile " + tempFile.getAbsolutePath());
					this.logger.debug("in VendorMultiActionManager::saveVendor::file " + file.getAbsolutePath());
					if (tempFile != null && tempFile.exists()) {
						try {
							this.copyFile(tempFile, file);
						} catch (FileNotFoundException var13) {
							throw new CMSException(this.logger, var13);
						} catch (IOException var14) {
							throw new CMSException(this.logger, var14);
						}

						this.vendorMultiActionDAO.addVendorUpload(vendorUpload);
					}
				}
			}
		}

		return this.vendorMultiActionDAO.getVendorInfo(String.valueOf(vendorMasterId));
	}

	private List<String> deleteVendorFilesFromDisk(List<VendorUploadMasterVO> vendorUploadsToDeleteList) {
		List<String> vendorFileNames = new ArrayList();
		String path = this.propertyReader.getVendorUploadsPath();

		VendorUploadMasterVO vendorUploadToDelete;
		for (Iterator i$ = vendorUploadsToDeleteList.iterator(); i$.hasNext(); vendorFileNames
				.add(vendorUploadToDelete.getFileName())) {
			vendorUploadToDelete = (VendorUploadMasterVO) i$.next();
			path = path + File.separator + vendorUploadToDelete.getVendorCode();
			File file = new File(path + File.separator + vendorUploadToDelete.getFileName());
			if (file.exists()) {
				file.delete();
			}
		}

		return vendorFileNames;
	}

	private File getVendorUploadDirectory(String vendorCode) {
		String path = this.propertyReader.getVendorUploadsPath();
		path = path + File.separator + vendorCode;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return dir;
	}

	private File getTempVendorUploadDirectory(String currentUser) {
		String tempPath = this.propertyReader.getVendorTempUploadsPath();
		tempPath = tempPath + File.separator + currentUser;
		File dir = new File(tempPath);
		return dir;
	}

	private void copyFile(File tempFile, File file) throws FileNotFoundException, IOException {
		FileReader in = new FileReader(tempFile);
		FileWriter out = new FileWriter(file);

		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}

		in.close();
		out.close();
	}

	public VendorMasterVO getVendorInfo(String vendorMasterId) throws CMSException {
		return this.vendorMultiActionDAO.getVendorInfo(vendorMasterId);
	}

	public List<VendorMasterVO> getVendorTypes() throws CMSException {
		return this.vendorMultiActionDAO.getVendorTypes();
	}

	public List<VendorMasterVO> searchVendor(HashMap<String, Object> paramMap) throws CMSException {
		return this.vendorMultiActionDAO.searchVendor(paramMap);
	}

	public int getTotalCount(HashMap<String, Object> paramMap) throws CMSException {
		return this.vendorMultiActionDAO.getTotalCount(paramMap);
	}

	public List<VendorMasterVO> getDataForExport(Map<String, Object> excelParamMap) throws CMSException {
		return this.vendorMultiActionDAO.getDataForExport(excelParamMap);
	}

	public void changeVendorStatus(String selectedVendorId, String vendorStatus, String updatedBy) throws CMSException {
		this.vendorMultiActionDAO.changeVendorStatus(selectedVendorId, vendorStatus, updatedBy);
	}

	public List<String> getAssociatedWIPCasesList(String selectedVendorId) throws CMSException {
		return this.vendorMultiActionDAO.getAssociatedWIPCasesList(selectedVendorId);
	}

	public void addVendorUpload(VendorUploadMasterVO vendorUploadMasterVO) throws CMSException {
		this.vendorMultiActionDAO.addVendorUpload(vendorUploadMasterVO);
	}

	public List<VendorUploadMasterVO> getVendorUploads(String vendorCode,
			List<VendorUploadMasterVO> vendorTempUploadsList, List<VendorUploadMasterVO> vendorUploadsToDeleteList)
			throws CMSException {
		List<VendorUploadMasterVO> vendorUploadsList = this.vendorMultiActionDAO.getVendorUploads(vendorCode);
		List<Integer> vendorIndexesToDelete = new ArrayList();
		if (vendorUploadsToDeleteList != null && vendorUploadsToDeleteList.size() > 0) {
			Iterator i$ = vendorUploadsToDeleteList.iterator();

			while (i$.hasNext()) {
				VendorUploadMasterVO vendorUploadToDelete = (VendorUploadMasterVO) i$.next();

				for (int i = 0; i < vendorUploadsList.size(); ++i) {
					if (((VendorUploadMasterVO) vendorUploadsList.get(i)).getFileName()
							.equals(vendorUploadToDelete.getFileName())) {
						vendorIndexesToDelete.add(i);
					}
				}
			}

			i$ = vendorIndexesToDelete.iterator();

			while (i$.hasNext()) {
				int index = (Integer) i$.next();
				vendorUploadsList.remove(index);
			}
		}

		if (vendorTempUploadsList != null && vendorTempUploadsList.size() > 0) {
			vendorUploadsList.addAll(vendorTempUploadsList);
		}

		return vendorUploadsList;
	}

	public List<CountryMasterVO> getAllCountries(String vendorMasterId) throws CMSException {
		return this.vendorMultiActionDAO.getAllCountries(vendorMasterId);
	}

	public List<CountryMasterVO> getSelectedCountries(String vendorMasterId) throws CMSException {
		return this.vendorMultiActionDAO.getSelectedCountries(vendorMasterId);
	}

	public String vendorExists(String vendorName) throws CMSException {
		return this.vendorMultiActionDAO.vendorExists(vendorName);
	}

	public List<VendorUploadMasterVO> tempDeleteVendorUploads(List<String> vendorFileNames,
			List<VendorUploadMasterVO> vendorTempUploadsList, List<VendorUploadMasterVO> vendorUploadsToDeleteList,
			String uploadedBy, String vendorCode) {
		if (vendorUploadsToDeleteList == null) {
			vendorUploadsToDeleteList = new ArrayList();
		}

		List<Integer> vendorIndexesToDelete = new ArrayList();
		Iterator i$ = vendorFileNames.iterator();

		while (i$.hasNext()) {
			String fileName = (String) i$.next();
			String tempPath;
			if (vendorTempUploadsList != null) {
				tempPath = this.propertyReader.getVendorTempUploadsPath();
				tempPath = tempPath + File.separator + uploadedBy;

				for (int i = 0; i < vendorTempUploadsList.size(); ++i) {
					if (((VendorUploadMasterVO) vendorTempUploadsList.get(i)).getFileName().equals(fileName)) {
						File file = new File(tempPath + File.separator + fileName);
						if (file.exists()) {
							file.delete();
						}

						vendorIndexesToDelete.add(i);
					}
				}
			}

			tempPath = this.propertyReader.getVendorUploadsPath();
			tempPath = tempPath + File.separator + vendorCode;
			File file = new File(tempPath + File.separator + fileName);
			if (file.exists()) {
				this.logger.debug("VendorMultiActionManager::tempDeleteVendorUploads::fileName to delete " + fileName);
				VendorUploadMasterVO vendorUploadToDelete = new VendorUploadMasterVO();
				vendorUploadToDelete.setFileName(fileName);
				vendorUploadToDelete.setVendorCode(vendorCode);
				((List) vendorUploadsToDeleteList).add(vendorUploadToDelete);
			}
		}

		this.logger.debug("VendorMultiActionManager::tempDeleteVendorUploads::vendorUploadsToDeleteList.size() "
				+ ((List) vendorUploadsToDeleteList).size());
		if (vendorTempUploadsList != null) {
			i$ = vendorIndexesToDelete.iterator();

			while (i$.hasNext()) {
				int index = (Integer) i$.next();
				vendorTempUploadsList.remove(index);
			}
		}

		return (List) vendorUploadsToDeleteList;
	}

	public boolean vendorFileExists(String vendorCode, String fileName, String uploadedBy) {
		if (fileName.contains("\\")) {
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}

		String path = this.propertyReader.getVendorUploadsPath();
		path = path + File.separator + vendorCode + File.separator + fileName;
		File file = new File(path);
		String tempPath = this.propertyReader.getVendorTempUploadsPath();
		tempPath = tempPath + File.separator + uploadedBy + File.separator + fileName;
		File tempFile = new File(tempPath);
		return file.exists() || tempFile.exists();
	}
}