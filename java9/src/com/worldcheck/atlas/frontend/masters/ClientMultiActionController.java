package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.IClientMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientContactVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;

public class ClientMultiActionController extends JSONMultiActionController {
	private static final String CLIENT_ID = "clientId";
	private static final String CLIENT_ADD = "client_add";
	private static final String EXCEL_FILE_NAME = "Client Master Search";
	private static final String UPDATE_ACTION = "update";
	private static final String ADD_ACTION = "add";
	private static final String ACTION_PARAM = "actionParam";
	private String RESULT = "result";
	private static final String CLIENT_DATA = "ClientData";
	private static final String COUNTRY_MASTER_LIST = "countryMasterList";
	private static final String CLIENT_NAME2 = "clientName";
	private static final String CLIENT_CODE2 = "clientCode";
	private static final String CL_CODE = "clCode";
	private static final String CL_MASTER_ID = "clMasterId";
	private static final String SUCCESS = "success";
	private static final String MESSAGE2 = "message";
	private static final String CREATE_CASE = "createCase";
	private static final String FAX2 = "fax";
	private static final String PHONE2 = "phone";
	private static final String REMARK = "remark";
	private static final String NAME = "name";
	private static final String CLIENT_SINCE2 = "clientSince";
	private static final String EMAIL = "email";
	private static final String BRANCH_OFFICE_ID = "branchOfficeId";
	private static final String CLIENT_GROUP_ID = "clientGroupId";
	private static final String HCLIENT_STATUS_CMB = "hclientStatusCmb";
	private static final String HBACKUP1_CMB = "hbackup1Cmb";
	private static final String HCOUNTRY_CMB = "hcountryCmb";
	private static final String CLIENT_STATUS = "Client Status";
	private static final String INVOICE_INSTRUCTION = "Invoice Instruction";
	private static final String BRANCH = "Branch";
	private static final String ASSOCIATED_BDM = "Associated BDM";
	private static final String ASSOCIATED_CM = "Associated CM";
	private static final String EMAIL_ADDRESS = "Email Address";
	private static final String FAX = "Fax";
	private static final String PHONE = "Phone";
	private static final String LOCATION = "Location";
	private static final String COUNTRY2 = "Country";
	private static final String INVOICE_ADDRESS = "Invoice Address";
	private static final String INVOICE_TO = "Invoice To";
	private static final String CLIENT_SINCE = "Client Since";
	private static final String CLIENT_NAME = "Client Name";
	private static final String CLIENT_CODE = "Client Code";
	private static final String CLIENT_GROUP_NAME = " Main Client Group";
	private static final String CLIENT_CONTACT_NAME = "Client's Contacts_Name";
	private static final String CLIENT_PHONE = "Client's Contacts_Phone";
	private static final String CLIENT_FAX = "Client's Contacts_Fax";
	private static final String CLIENT_EMAIL = "Client's Contacts_Email Address";
	private static final String CLIENT_REMARK = "Client's Contacts_Remark";
	private static final String CLIENT_ORDER_DATE = "Case Last Ordered Date";
	private static final String USER_LIST = "userList";
	private static final String ROLE_ID = "roleId";
	private ModelAndView mv = null;
	private String clientName = "clientList";
	private String searchName = "searchResult";
	private static final String GET_CLIENT = "getClient";
	private static final String ACTION = "action";
	private static final String CONTACT_LIST = "contactList";
	private static final String UPDATE_DEACTIVE = "updateDeactive";
	private static final String COUNTRY = "Country";
	private static final String CLIENT_CUSTOM_JSP = "client";
	private static final String REDIRECT_CLIENT_SEARCH = "redirect:clientSearch.do";
	private static final String CLIENT_ADD_CUSTOM_JSP = "client_add";
	private static final String IS_SUBREPORT_REQ = "subreportTypeReq";
	private static final String SubReportTypeAtSubjectLevel = "Subreport_Type_At_Subject_Level";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.ClientMultiActionController");
	IClientMaster clientMultiActionManager = null;

	public void setClientMultiActionManager(IClientMaster clientMultiActionManager) {
		this.clientMultiActionManager = clientMultiActionManager;
	}

	public ModelAndView client(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String clientStatus = request.getParameter("status");
			String isSubreportRequire = request.getParameter("isSubreportRequired");
			clientMasterVO.setClientStatus(clientStatus);
			if (isSubreportRequire != null && isSubreportRequire.equalsIgnoreCase("1")) {
				clientMasterVO.setIsSubreportRequired(isSubreportRequire);
			} else {
				clientMasterVO.setIsSubreportRequired("");
			}

			new ArrayList();
			ArrayList<ClientMasterVO> clientList = (ArrayList) this.clientMultiActionManager
					.selectClientInfo(clientMasterVO);
			modelAndView.addObject(this.clientName, clientList);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView clientSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("client");
		String cr_case;
		if (request.getSession().getAttribute("actionParam") != null) {
			cr_case = request.getSession().getAttribute("actionParam").toString();
			modelAndView.addObject("actionParam", cr_case);
			request.getSession().removeAttribute("actionParam");
		}

		if (request.getSession().getAttribute("message") != null) {
			cr_case = request.getSession().getAttribute("message").toString();
			modelAndView.addObject("message", cr_case);
			request.getSession().removeAttribute("message");
		}

		if (request.getSession().getAttribute("createCase") != null) {
			cr_case = request.getSession().getAttribute("createCase").toString();
			modelAndView.addObject("createCase", cr_case);
			request.getSession().removeAttribute("createCase");
		}

		return modelAndView;
	}

	public ModelAndView setupAddClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("client_add");
		return modelAndView;
	}

	public ModelAndView addClientAndNew(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("redirect:clientSearch.do");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			this.logger.debug("ClientMultiActionController.addClientAndNew()" + request.getParameter("hcountryCmb"));
			this.logger.debug(
					"ClientMultiActionController.addClientAndNew:: Client Name::" + request.getParameter("clientName"));
			clientMasterVO.setCountryMasterId(request.getParameter("hcountryCmb"));
			clientMasterVO.setAssociatedBDM(request.getParameter("hbackup1Cmb"));
			clientMasterVO.setClientStatus(request.getParameter("hclientStatusCmb"));
			clientMasterVO.setBranchOfficeId(request.getParameter("branchOfficeId"));
			clientMasterVO.setClientGroupId(request.getParameter("clientGroupId"));
			clientMasterVO.setEmailaddress(request.getParameter("email"));
			clientMasterVO.setClientSince(request.getParameter("clientSince"));
			clientMasterVO.setIsSubreportRequired(request.getParameter("subreportTypeReq"));
			this.logger.debug("ClientMultiActionController.addClientAndNew()  " + clientMasterVO.getEmailaddress()
					+ "  county master id   " + clientMasterVO.getCountryMasterId());
			this.logger.debug("ClientMultiActionController.addClientAndNew()  " + clientMasterVO.getClientSince()
					+ "  addres  " + clientMasterVO.getInvoiceAddress());
			String clientCode = this.clientMultiActionManager.getClientCode(clientMasterVO.getClientName());
			clientMasterVO.setClientCode(clientCode);
			clientMasterVO.setUpdatedBy(userName);
			this.clientMultiActionManager.addClient(clientMasterVO);
			this.logger.info("client added successfully");
			List<ClientContactVO> listVO = new ArrayList();
			String[] modifiedRecords = clientMasterVO.getModifiedRecords();
			ClientContactVO clientContactVO;
			if (modifiedRecords != null && modifiedRecords.length > 0) {
				for (int i = 0; i < modifiedRecords.length; ++i) {
					clientContactVO = new ClientContactVO();
					String JSONstring = modifiedRecords[i];
					Map jsonObject = (Map) JSONValue.parse(JSONstring);
					clientContactVO.setName((String) jsonObject.get("name"));
					clientContactVO.setEmail((String) jsonObject.get("email"));
					clientContactVO.setRemark((String) jsonObject.get("remark"));
					String phone = (String) jsonObject.get("phone");
					clientContactVO.setPhone(phone);
					String fax = (String) jsonObject.get("fax");
					clientContactVO.setFax(fax);
					clientContactVO.setClientCode(clientMasterVO.getClientCode());
					clientContactVO.setUserName(userName);
					listVO.add(clientContactVO);
				}
			}

			Iterator iterator = listVO.iterator();

			while (iterator.hasNext()) {
				clientContactVO = (ClientContactVO) iterator.next();
				this.clientMultiActionManager.addClientContact(clientContactVO);
			}

			this.logger.info("client contact added successfully");

			try {
				ResourceLocator.self().getCacheService().addToCacheRunTime("CLIENT_MASTER");
			} catch (Exception var16) {
				throw new CMSException(this.logger, var16);
			}

			request.getSession().setAttribute("actionParam", "add");
			String message = "Client < " + clientMasterVO.getClientName() + " > has been successfully added.";
			request.getSession().setAttribute("message", message);
			request.getSession().setAttribute("createCase", request.getParameter("createCase"));
			return modelAndView;
		} catch (CMSException var17) {
			return AtlasUtils.getExceptionView(this.logger, var17);
		} catch (Exception var18) {
			return AtlasUtils.getExceptionView(this.logger, var18);
		}
	}

	public ModelAndView addClientAndBack(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String message = "Add Client and more Client Method";
			this.logger.debug(message);
			clientMasterVO.setUpdatedBy(userBean.getUserName());
			this.clientMultiActionManager.addClient(clientMasterVO);
			this.mv = new ModelAndView("client_custom");
			this.mv.addObject("message", message);
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}

		return this.mv;
	}

	public ModelAndView updateClientStatus(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String message = "Status Of Client Updated Successfully";
			this.clientMultiActionManager.updateClientStatus(clientMasterVO);
			this.logger.debug(message);
			ResourceLocator.self().getCacheService().addToCacheRunTime("CLIENT_MASTER");
			modelAndView.addObject("success", "true");
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView searchClient(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("Inside Search" + clientMasterVO.getClientStatus() + " client code >>>   "
					+ clientMasterVO.getClientCode() + " client name " + clientMasterVO.getClientName()
					+ " client group id=" + clientMasterVO.getClientGroupId());
			new ArrayList();
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			int count = false;
			clientMasterVO.setMinRecord(start + 1);
			clientMasterVO.setMaxRecord(start + limit);
			clientMasterVO.setSortColumnName(request.getParameter("sort"));
			clientMasterVO.setSortType(request.getParameter("dir"));
			clientMasterVO.setClientGroupId(request.getParameter("clientGroupId"));
			List<ClientMasterVO> searchResult = this.clientMultiActionManager.searchClient(clientMasterVO);
			int count = this.clientMultiActionManager.searchClientCount(clientMasterVO);
			modelAndView.addObject(this.searchName, searchResult);
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getClientInfo(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("client_add");

		try {
			String clientId = request.getParameter("clientId");
			int idVal = false;
			if (clientId != null && !clientId.equals("")) {
				int idVal;
				try {
					idVal = Integer.parseInt(clientId);
				} catch (NumberFormatException var8) {
					modelAndView = new ModelAndView("redirect:clientSearch.do");
					return modelAndView;
				}

				clientMasterVO = this.clientMultiActionManager.getClientInfo(idVal);
				if (clientMasterVO == null) {
					modelAndView = new ModelAndView("redirect:clientSearch.do");
					return modelAndView;
				} else {
					if (clientMasterVO.getClientStatus() != null && "0".equals(clientMasterVO.getClientStatus())) {
						modelAndView.addObject("updateDeactive", "updateDeactive");
					}

					this.logger
							.debug("ClientMultiActionController.getClientInfo()" + clientMasterVO.getCountryMasterId());
					modelAndView.addObject("getClient", clientMasterVO);
					modelAndView.addObject("action", "update");
					modelAndView.addObject("createCase", request.getParameter("createCase"));
					return modelAndView;
				}
			} else {
				modelAndView = new ModelAndView("redirect:clientSearch.do");
				return modelAndView;
			}
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView updateClient(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			this.logger.debug(" request parameters map " + request.getParameterMap());
			this.logger.debug("ClientMultiActionController.updateClient" + request.getParameter("hcountryCmb"));
			clientMasterVO.setUpdatedBy(userName);
			this.logger.debug(
					"ClientMultiActionController.updateClient: Client Name::" + request.getParameter("clientName"));
			clientMasterVO.setCountryMasterId(request.getParameter("hcountryCmb"));
			clientMasterVO.setAssociatedBDM(request.getParameter("hbackup1Cmb"));
			clientMasterVO.setClientStatus(request.getParameter("hclientStatusCmb"));
			clientMasterVO.setBranchOfficeId(request.getParameter("branchOfficeId"));
			clientMasterVO.setEmailaddress(request.getParameter("email"));
			clientMasterVO.setClientSince(request.getParameter("clientSince"));
			clientMasterVO.setClientMasterId(request.getParameter("clMasterId"));
			this.logger
					.debug("ClientMultiActionController.updateClient() clCode ::::  " + request.getParameter("clCode"));
			clientMasterVO.setClientCode(request.getParameter("clCode"));
			clientMasterVO.setClientGroupId(request.getParameter("clientGroupId"));
			clientMasterVO.setIsSubreportRequired(request.getParameter("subreportTypeReq"));
			this.logger.debug("ClientMultiActionController.addClientAndNew()  getClientMasterId   "
					+ clientMasterVO.getClientMasterId() + "  clientMasterVO.getClientCode() >>>>>>>>   "
					+ clientMasterVO.getClientCode());
			this.logger.debug("ClientMultiActionController.addClientAndNew()  " + clientMasterVO.getClientSince()
					+ "  addres  " + clientMasterVO.getInvoiceAddress());
			String updateDeactive = request.getParameter("updateDeactive");
			if (updateDeactive != null && !updateDeactive.equals("")) {
				this.clientMultiActionManager.updateDeactiveClient(clientMasterVO.getClientCode());
				request.setAttribute("clientId", clientMasterVO.getClientMasterId());
				this.mv = new ModelAndView("redirect:getClientInfo.do?clientId=" + clientMasterVO.getClientMasterId());
				return this.mv;
			} else {
				this.clientMultiActionManager.updateClient(clientMasterVO);
				this.clientMultiActionManager.delete(clientMasterVO.getClientCode());
				List<ClientContactVO> listVO = new ArrayList();
				String[] modifiedRecords = clientMasterVO.getModifiedRecords();
				ClientContactVO clientContactVO;
				if (modifiedRecords != null && modifiedRecords.length > 0) {
					for (int i = 0; i < modifiedRecords.length; ++i) {
						clientContactVO = new ClientContactVO();
						String JSONstring = modifiedRecords[i];
						Map jsonObject = (Map) JSONValue.parse(JSONstring);
						clientContactVO.setName((String) jsonObject.get("name"));
						clientContactVO.setEmail((String) jsonObject.get("email"));
						clientContactVO.setRemark((String) jsonObject.get("remark"));
						String phone = (String) jsonObject.get("phone");
						clientContactVO.setPhone(phone);
						String fax = (String) jsonObject.get("fax");
						clientContactVO.setFax(fax);
						clientContactVO.setClientCode(clientMasterVO.getClientCode());
						clientContactVO.setUserName(userName);
						listVO.add(clientContactVO);
					}
				}

				Iterator iterator = listVO.iterator();

				while (iterator.hasNext()) {
					clientContactVO = (ClientContactVO) iterator.next();
					this.clientMultiActionManager.addClientContact(clientContactVO);
				}

				try {
					ResourceLocator.self().getCacheService().addToCacheRunTime("CLIENT_MASTER");
				} catch (Exception var15) {
					throw new CMSException(this.logger, var15);
				}

				this.mv = new ModelAndView("redirect:clientSearch.do");
				request.getSession().setAttribute("actionParam", "update");
				String message = "Client < " + clientMasterVO.getClientName() + " > has been successfully Updated.";
				request.getSession().setAttribute("message", message);
				return this.mv;
			}
		} catch (CMSException var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		} catch (Exception var17) {
			return AtlasUtils.getExceptionView(this.logger, var17);
		}
	}

	public ModelAndView getClientContact(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String clientCode = request.getParameter("clientCode");
			new ArrayList();
			List<ClientContactVO> listResult = this.clientMultiActionManager.getContactDetail(clientCode);
			this.logger.debug("ClientMultiActionController.getClientContact()" + listResult);
			modelAndView.addObject("contactList", listResult);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView checkClientIfExist(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String clientName = request.getParameter("clientName");
			String clientCode = "";
			clientCode = this.clientMultiActionManager.checkClientName(clientName);
			if (clientCode != null && !clientCode.equals("")) {
				modelAndView.addObject("success", clientCode);
			} else {
				modelAndView.addObject("success", "false");
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public List getCountryMaster() throws CMSException {
		this.logger.debug("Inside getCountryMaster method of SubjectManager class");
		List countryList = ResourceLocator.self().getCacheService().getCacheItemsList("COUNTRY_MASTER");
		return countryList;
	}

	public ModelAndView popualteCountryList(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryMasterVO> topCountry = this.clientMultiActionManager.getTopCountry();
			List<CountryMasterVO> finalList = this.setCountryList(this.getCountryMaster(), topCountry);
			modelAndView.addObject("countryMasterList", finalList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView popualteCountryListForRiskHBD(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryMasterVO> finalList = this.setCountryListHBD(this.getCountryMaster());
			modelAndView.addObject("countryMasterList", finalList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getUserForRole(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String roleId = request.getParameter("roleId");
			List<UserMasterVO> userList = ResourceLocator.self().getUserService().getUsersForRole(roleId);
			modelAndView.addObject("userList", userList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	private List<CountryMasterVO> setCountryList(List<CountryMasterVO> cacheList, List<CountryMasterVO> topTen)
			throws CMSException {
		ArrayList finalList = new ArrayList();

		try {
			Map<String, String> map = new LinkedHashMap();
			Iterator it = topTen.iterator();

			CountryMasterVO vo;
			while (it.hasNext()) {
				vo = (CountryMasterVO) it.next();
				map.put(vo.getCountry(), vo.getCountryCode());
			}

			it = cacheList.iterator();

			while (it.hasNext()) {
				vo = (CountryMasterVO) it.next();
				map.put(vo.getCountry(), vo.getCountryCode());
			}

			it = map.entrySet().iterator();

			while (it.hasNext()) {
				vo = new CountryMasterVO();
				Entry pairs = (Entry) it.next();
				vo.setCountry((String) pairs.getKey());
				vo.setCountryCode((String) pairs.getValue());
				finalList.add(vo);
			}

			return finalList;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private List<CountryMasterVO> setCountryListHBD(List<CountryMasterVO> cacheList) throws CMSException {
		ArrayList finalList = new ArrayList();

		try {
			Map<String, Integer> map = new LinkedHashMap();
			Iterator it = cacheList.iterator();

			CountryMasterVO vo;
			while (it.hasNext()) {
				vo = (CountryMasterVO) it.next();
				map.put(vo.getCountry(), vo.getCountryMasterId());
			}

			it = map.entrySet().iterator();

			while (it.hasNext()) {
				vo = new CountryMasterVO();
				Entry pairs = (Entry) it.next();
				vo.setCountry((String) pairs.getKey());
				vo.setCountryMasterId((Integer) pairs.getValue());
				finalList.add(vo);
			}

			return finalList;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public ModelAndView exportToExcelClientMaster(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		this.logger.debug("in export to excel");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("parameter " + excelParamMap);
			new ArrayList();
			List<ClientMasterVO> searchResult = this.clientMultiActionManager.searchClientForExport(excelParamMap);
			this.logger.debug("number of records " + searchResult.size());
			Map<String, Object> resultMap = this.writeToExcel(searchResult, response);
			modelandview = new ModelAndView("excelDownloadPopup");
			modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
			modelandview.addObject("fileName", resultMap.get("fileName"));
			return modelandview;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	private Map<String, Object> writeToExcel(List<ClientMasterVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		ClientMasterVO clientMasterVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				clientMasterVO = (ClientMasterVO) iterator.next();
				this.populateDataMap(clientMasterVO, datamap);
				dataList.add(datamap);
			}
		} catch (UnsupportedOperationException var8) {
			throw new CMSException(this.logger, var8);
		} catch (ClassCastException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IllegalArgumentException var11) {
			throw new CMSException(this.logger, var11);
		}

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "ClientData", (short) 0, (short) 1, response,
				"Client Master Search");
	}

	private void populateDataMap(ClientMasterVO clientMasterVO, LinkedHashMap<String, String> datamap) {
		datamap.put("Client Code", String.valueOf(clientMasterVO.getClientCode()));
		datamap.put("Client Name", String.valueOf(clientMasterVO.getClientName()));
		datamap.put(" Main Client Group", String.valueOf(clientMasterVO.getGroupName()));
		datamap.put("Client Status", String.valueOf(clientMasterVO.getClientStatus()));
		datamap.put("Client Since", String.valueOf(clientMasterVO.getClientSince()));
		datamap.put("Branch", String.valueOf(clientMasterVO.getBranch()));
		datamap.put("Associated CM", String.valueOf(clientMasterVO.getAssociatedCM()));
		datamap.put("Associated BDM", String.valueOf(clientMasterVO.getAssociatedBDM()));
		datamap.put("Invoice To", String.valueOf(clientMasterVO.getInvoiceTo()));
		datamap.put("Country", String.valueOf(clientMasterVO.getCountry()));
		datamap.put("Invoice Address", String.valueOf(clientMasterVO.getInvoiceAddress()));
		datamap.put("Invoice Instruction", String.valueOf(clientMasterVO.getInvoiceInstruction()));
		datamap.put("Location", String.valueOf(clientMasterVO.getLocation()));
		datamap.put("Phone", String.valueOf(clientMasterVO.getPhone()));
		datamap.put("Email Address", String.valueOf(clientMasterVO.getEmailaddress()));
		datamap.put("Fax", String.valueOf(clientMasterVO.getFax()));
		datamap.put("Client's Contacts_Name", String.valueOf(clientMasterVO.getClientContact()));
		datamap.put("Client's Contacts_Phone", String.valueOf(clientMasterVO.getClientPhone()));
		datamap.put("Client's Contacts_Fax", String.valueOf(clientMasterVO.getClientFax()));
		datamap.put("Client's Contacts_Email Address", String.valueOf(clientMasterVO.getClientEmail()));
		datamap.put("Client's Contacts_Remark", String.valueOf(clientMasterVO.getClientRemark()));
		datamap.put("Case Last Ordered Date", String.valueOf(clientMasterVO.getCaseOrdaerDate()));
		datamap.put("Subreport_Type_At_Subject_Level", String.valueOf(clientMasterVO.getIsSubreportRequired()));
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Client Code");
			lstHeader.add("Client Name");
			lstHeader.add(" Main Client Group");
			lstHeader.add("Client Status");
			lstHeader.add("Client Since");
			lstHeader.add("Branch");
			lstHeader.add("Associated CM");
			lstHeader.add("Associated BDM");
			lstHeader.add("Invoice To");
			lstHeader.add("Country");
			lstHeader.add("Invoice Address");
			lstHeader.add("Invoice Instruction");
			lstHeader.add("Location");
			lstHeader.add("Phone");
			lstHeader.add("Email Address");
			lstHeader.add("Fax");
			lstHeader.add("Client's Contacts_Name");
			lstHeader.add("Client's Contacts_Phone");
			lstHeader.add("Client's Contacts_Fax");
			lstHeader.add("Client's Contacts_Email Address");
			lstHeader.add("Client's Contacts_Remark");
			lstHeader.add("Case Last Ordered Date");
			lstHeader.add("Subreport_Type_At_Subject_Level");
			return lstHeader;
		} catch (UnsupportedOperationException var3) {
			throw new CMSException(this.logger, var3);
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}