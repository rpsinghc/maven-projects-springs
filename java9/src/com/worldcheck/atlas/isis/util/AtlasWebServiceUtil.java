package com.worldcheck.atlas.isis.util;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.integrascreen.orders.CBDVO;
import com.integrascreen.orders.ErrorDetailObjects;
import com.integrascreen.orders.ErrorObjects;
import com.integrascreen.orders.Ocrs_wsLocator;
import com.integrascreen.orders.Ocrs_wsSoap;
import com.integrascreen.orders.ResponseVO;
import com.integrascreen.orders.UPMasterVO;
import com.integrascreen.orders.UPSubjectREVO;
import com.integrascreen.orders.UPSubjectVO;
import com.integrascreen.orders.USRiskVO;
import com.integrascreen.orders.USSubjectIndustryVO;
import com.integrascreen.orders.USSubjectRiskVO;
import com.integrascreen.orders.USVO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.CaseFileDetailsVO;
import com.worldcheck.atlas.isis.vo.ClientCBDVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.isis.vo.ClientStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientSubjectVO;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.isis.vo.ReDetailsVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.xml.soap.SOAPException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPHeaderElement;

public class AtlasWebServiceUtil {
	static Ocrs_wsSoap ocrs = null;
	private static SqlMapClient sqlMap = null;
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.isis.util.AtlasWebServiceUtil");
	private static ResourceBundle resources = ResourceBundle.getBundle("atlas");

	static {
		String isisWebServiceAddress;
		try {
			isisWebServiceAddress = resources.getString("isis.webservice.url");
			URL endpoint = new URL(isisWebServiceAddress);
			Ocrs_wsLocator locator = new Ocrs_wsLocator();
			ocrs = locator.getocrs_wsSoap(endpoint);
			Stub stub = (Stub) ocrs;
			SOAPHeaderElement header = getCredentialToHeader();
			logger.debug("going to set soap header..........");
			stub.setHeader(header);
			logger.debug("going to set timeout property");
		} catch (Exception var6) {
			logger.error("Some error occured...");
			logger.error("Some error occured for getting web service insatnce..." + getStackTraceAsString(var6));
			logger.error(var6);
		}

		isisWebServiceAddress = null;

		try {
			Reader reader = Resources.getResourceAsReader("SqlMapConfig1.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (Exception var5) {
			logger.error("Some error occured...");
			logger.error("Some error occured for getting sqlMap insatnce..." + getStackTraceAsString(var5));
			logger.error(var5.getMessage());
		}

	}

	public static ArrayList convertFromArrayObjectToArrayList(Object[] objectArray, String ObjectType) {
		logger.debug("inside convertFromArrayObjectToArrayList method of AtlasWebServiceUtil class");
		ArrayList arrayList = new ArrayList();
		if (objectArray != null && objectArray.length > 0) {
			for (int i = 0; i < objectArray.length; ++i) {
				if (ObjectType.equalsIgnoreCase("CaseFileDetailsVO")) {
					arrayList.add((CaseFileDetailsVO) objectArray[i]);
				} else if (ObjectType.equalsIgnoreCase("SubjectDetailsVO")) {
					arrayList.add((SubjectDetailsVO) objectArray[i]);
				} else if (ObjectType.equalsIgnoreCase("ReDetailsVO")) {
					arrayList.add((ReDetailsVO) objectArray[i]);
				}
			}
		}

		return arrayList;
	}

	public static Object[] convertCommaStringToObjectArray(String commaSeparatedString) {
		logger.debug("inside convertCommaStringToObjectArray method of AtlasWebServiceUtil class");
		Object[] reObj = (Object[]) null;
		if (!commaSeparatedString.equals("") && commaSeparatedString != null) {
			StringTokenizer reTokenizer = new StringTokenizer(commaSeparatedString, ",");
			reObj = new Object[reTokenizer.countTokens()];

			for (int index = 0; reTokenizer.hasMoreElements(); ++index) {
				reObj[index] = reTokenizer.nextElement();
			}
		}

		return reObj;
	}

	public static Map callISISWebService(Map dataMap) {
		logger.debug("inside callISISWebService method of AtlasWebServiceUtil class");
		Map resultMap = new HashMap();
		ResponseVO responseVO = null;
		String OperationType = (String) dataMap.get("OperationType");
		long seqId = (Long) dataMap.get("QueueRecordId");
		String errorCode;
		if (OperationType.equalsIgnoreCase("ConfirmBudgetAndDueDateOperation")) {
			logger.debug(" CBD operation Request....");
			ClientCBDVO clientCBDVO = (ClientCBDVO) dataMap.get("ReqObject");
			CBDVO cbdVO = new CBDVO();
			cbdVO.setBudget(clientCBDVO.getBudget());
			logger.debug(" CBD operation Request....clientCBDVO.getBudget():" + clientCBDVO.getBudget());
			cbdVO.setCRN(clientCBDVO.getCRN());
			logger.debug(" CBD operation Request....clientCBDVO.getCRN():" + clientCBDVO.getCRN());
			cbdVO.setCurrencyCode(clientCBDVO.getCurrencyCode());
			logger.debug(" CBD operation Request....clientCBDVO.getCurrencyCode():" + clientCBDVO.getCurrencyCode());
			cbdVO.setDueDate(clientCBDVO.getDueDate());
			logger.debug(" CBD operation Request....clientCBDVO.getDueDate():" + clientCBDVO.getDueDate());
			cbdVO.setStatus(clientCBDVO.getStatus());
			logger.debug(" CBD operation Request....clientCBDVO.getStatus():" + clientCBDVO.getStatus());
			cbdVO.setSubjectList(clientCBDVO.getSubjectList());
			logger.debug(" CBD operation Request....clientCBDVO.getSubjectList():" + clientCBDVO.getSubjectList());

			try {
				logger.debug("going to call isis webservice for CBD..");
				responseVO = ocrs.CBD(cbdVO);
			} catch (Exception var19) {
				logger.error("inside exception block of CBD webservice.");
				resultMap.put("successFlag", 0);
				resultMap.put("communicationErrorFlag", 0);
				logger.error("inside exception block of CBD webservice." + var19.getMessage());
				logger.error("inside exception block of CBD webservice." + getStackTraceAsString(var19));
			}

			if (responseVO != null) {
				errorCode = responseVO.getErrorCode();
				if (errorCode.equalsIgnoreCase("CBD_0")) {
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("                                   SUCCESS                                         ");
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("Inside success of CBD... got success flag");
					logger.info("CBD request successfully sent to ISIS for crn: " + cbdVO.getCRN());
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 1);
					resultMap.put("communicationErrorFlag", 1);
				} else if (errorCode.equalsIgnoreCase("CBD_2")) {
					logger.debug("got error code of communication failure from ISIS for CBD..");
					logger.debug("got error code for failure from webservice for CBD operation" + errorCode);
					logger.debug(
							"got error code for failure from webservice for CBD operation" + responseVO.getMessage());
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 0);
				} else {
					logger.debug("got error code of failure from ISIS for CBD..");
					logger.debug("got error code for failure from webservice for CBD operation" + errorCode);
					logger.debug(
							"got error code for failure from webservice for CBD operation" + responseVO.getMessage());
					updateStausForFailureErrorCode(seqId);
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 1);
				}
			}
		} else if (OperationType.equalsIgnoreCase("SubjectOperation")) {
			String updateType = "";
			int AtlasSubjectId = false;
			errorCode = "";
			UPSubjectVO upSubjectVO = new UPSubjectVO();

			try {
				logger.debug(" Subject operation Request............:::::");
				ClientSubjectVO clientSubjectVO = (ClientSubjectVO) dataMap.get("ReqObject");
				int AtlasSubjectId = clientSubjectVO.getAtlasSubjectID();
				logger.debug("AtlasSubjectId is : " + AtlasSubjectId);
				upSubjectVO.setAtlasSubjectID(clientSubjectVO.getAtlasSubjectID());
				upSubjectVO.setCountry(clientSubjectVO.getCountry());
				errorCode = clientSubjectVO.getCRN();
				upSubjectVO.setCRN(clientSubjectVO.getCRN());
				upSubjectVO.setIsPrimary(clientSubjectVO.isPrimary());
				upSubjectVO.setSubjectID(clientSubjectVO.getSubjectID());
				upSubjectVO.setSubjectName(clientSubjectVO.getSubjectName());
				upSubjectVO.setSubjectPosition(clientSubjectVO.getSubjectPosition());
				logger.debug("befor.." + clientSubjectVO.getSubjectREs());
				upSubjectVO.setSubjectRE(convertCommaStringToREObjectArray(clientSubjectVO.getSubjectREs()));
				logger.debug("after..");
				updateType = clientSubjectVO.getUpdateType();
				upSubjectVO.setUpdateType(clientSubjectVO.getUpdateType());
				upSubjectVO.setOtherDetails(clientSubjectVO.getOtherDetails());
				upSubjectVO.setSubjectType(clientSubjectVO.getSubjectType());
				if (clientSubjectVO.getSlSubreportID() > 0) {
					upSubjectVO.setSlSubreportCode("SPT" + clientSubjectVO.getSlSubreportID());
				}

				upSubjectVO.setSlCurrency(clientSubjectVO.getSlCurrency());
				upSubjectVO.setSlBudget((double) clientSubjectVO.getSlBudget());
				logger.debug("going to call ISIS webservice for Subject request...");
				responseVO = ocrs.UPSubject(upSubjectVO);
			} catch (Exception var18) {
				logger.error(
						"Error occured while calling ISIS webservice for subject operation.." + var18.getMessage());
				logger.error("Exception is::::" + getStackTraceAsString(var18));
				resultMap.put("successFlag", 0);
				resultMap.put("communicationErrorFlag", 0);
			}

			if (responseVO != null) {
				logger.debug("response vo is not null....");
				String errorCode = responseVO.getErrorCode();
				logger.debug("error code is::::::::::::" + responseVO.getErrorCode());
				if (errorCode.equalsIgnoreCase("UPS_0")) {
					logger.debug("Success !!!!!!");
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("                                   SUCCESS                                         ");
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug(
							"got success for ISIS webservice for subject operation......" + responseVO.getMessage());
					logger.info("Subject operation request sent successfully to ISIS for operation: "
							+ upSubjectVO.getUpdateType() + " and atlas subject id: "
							+ upSubjectVO.getAtlasSubjectID());
					if (updateType.equalsIgnoreCase("Insert") && responseVO.getResponseSubjectVO() != null) {
						logger.debug("Going to update....");
						resultMap.put("isisSubjectId", responseVO.getResponseSubjectVO().getISISSubjectID());
					}

					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("message", responseVO.getMessage());
					resultMap.put("successFlag", 1);
					resultMap.put("communicationErrorFlag", 1);
				} else if (errorCode.equalsIgnoreCase("UPS_2")) {
					logger.debug("got error code of communication failure from ISIS for subject..");
					logger.debug("got error code for failure from webservice for subject operation" + errorCode);
					logger.debug("got error code for failure from webservice for subject operation"
							+ responseVO.getMessage());
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("message", responseVO.getMessage());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 0);
				} else {
					logger.debug("got error code for failure from webservice for subject operation");
					logger.debug("got error code for failure from webservice for subject operation" + errorCode);
					logger.debug("got error code for failure from webservice for subject operation"
							+ responseVO.getMessage());
					updateStausForFailureErrorCode(seqId);
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("message", responseVO.getMessage());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 1);
				}
			}
		} else if (OperationType.equalsIgnoreCase("UpdateStatusOperation")) {
			logger.debug(" update status Request....");
			USVO usVO = new USVO();

			try {
				ClientCaseStatusVO clientCaseStatusVO = (ClientCaseStatusVO) dataMap.get("ReqObject");
				usVO.setCRN(clientCaseStatusVO.getCRN());
				usVO.setExpressCase(clientCaseStatusVO.getExpressCase());
				usVO.setFileName(clientCaseStatusVO.getFileName());
				usVO.setStatus(clientCaseStatusVO.getStatus());
				usVO.setVersion(clientCaseStatusVO.getVersion());
				usVO.setUpdateType(clientCaseStatusVO.getUpdateType());
				logger.debug("status code to be updated in ISIS :: " + clientCaseStatusVO.getStatus());
				logger.debug("going to set subject industry....");
				ClientCaseStatusIndustryVO[] subjectIndustryArray = clientCaseStatusVO.getSubjectIndustry();
				USSubjectIndustryVO[] usSubjectIndustryVOArray = (USSubjectIndustryVO[]) null;
				if (subjectIndustryArray != null && subjectIndustryArray.length > 0) {
					usSubjectIndustryVOArray = new USSubjectIndustryVO[subjectIndustryArray.length];

					for (int i = 0; i < subjectIndustryArray.length; ++i) {
						USSubjectIndustryVO usSubjectIndustryVO = new USSubjectIndustryVO();
						usSubjectIndustryVO.setAtlasSubjectID(subjectIndustryArray[i].getAtlasSubjectID());
						usSubjectIndustryVO.setISISSubjectID(subjectIndustryArray[i].getISISSubjectID());
						usSubjectIndustryVO.setIndustryName(subjectIndustryArray[i].getIndustryName());
						usSubjectIndustryVO.setIndustryID(subjectIndustryArray[i].getIndustryID());
						usSubjectIndustryVOArray[i] = usSubjectIndustryVO;
					}
				}

				usVO.setSubjectIndustry(usSubjectIndustryVOArray);
				logger.debug("going to set subject Risk....");
				ClientCaseStatusRiskVO[] subjectRiskArray = clientCaseStatusVO.getSubjectRisk();
				USSubjectRiskVO[] usSubjectRiskVOArray = (USSubjectRiskVO[]) null;
				if (subjectRiskArray != null && subjectRiskArray.length > 0) {
					usSubjectRiskVOArray = new USSubjectRiskVO[subjectRiskArray.length];

					for (int i = 0; i < subjectRiskArray.length; ++i) {
						logger.debug("subjectRiskArray.length is:::::::::::::" + subjectRiskArray.length);
						USSubjectRiskVO usSubjectRiskVO = new USSubjectRiskVO();
						usSubjectRiskVO.setAtlasSubjectID(subjectRiskArray[i].getAtlasSubjectID());
						usSubjectRiskVO.setISISSubjectID(subjectRiskArray[i].getISISSubjectID());
						ClientStatusRiskVO[] clientStatusRiskVOArray = subjectRiskArray[i].getRisk();
						USRiskVO[] usRiskVOArray = (USRiskVO[]) null;
						if (clientStatusRiskVOArray != null && clientStatusRiskVOArray.length > 0) {
							logger.debug("clientStatusRiskVOArray.length is:::::::" + clientStatusRiskVOArray.length);
							usRiskVOArray = new USRiskVO[clientStatusRiskVOArray.length];

							for (int j = 0; j < clientStatusRiskVOArray.length; ++j) {
								logger.debug("j is::::::::::" + j);
								logger.debug("clientStatusRiskVOArray[j] is::::" + clientStatusRiskVOArray[j]);
								USRiskVO usRiskVO = new USRiskVO();
								usRiskVO.setRiskName(clientStatusRiskVOArray[j].getRiskName());
								usRiskVO.setRiskID(clientStatusRiskVOArray[j].getRiskID());
								usRiskVO.setPossibleRisk(clientStatusRiskVOArray[j].getPossibleRisk());
								usRiskVOArray[j] = usRiskVO;
							}
						}

						usSubjectRiskVO.setRisk(usRiskVOArray);
						usSubjectRiskVOArray[i] = usSubjectRiskVO;
					}
				}

				usVO.setSubjectRisk(usSubjectRiskVOArray);
				usVO.setOtherinformation(clientCaseStatusVO.getOtherinformation());
				logger.debug("going to call ISIS webservice for update status..");
				responseVO = ocrs.US(usVO);
			} catch (Exception var20) {
				logger.error("Error occured while calling ISIS webservice for update status operation."
						+ var20.getMessage());
				logger.error("Error is::::" + getStackTraceAsString(var20));
				resultMap.put("successFlag", 0);
				resultMap.put("communicationErrorFlag", 0);
			}

			if (responseVO != null) {
				String errorCode = responseVO.getErrorCode();
				logger.debug("errorCode from ISIS is :: " + errorCode);
				if (errorCode.equalsIgnoreCase("US_0")) {
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("                                   SUCCESS                                         ");
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("got success from ISIS webservice for update status operation");
					logger.info("updated case status sent successfully to ISIS for update type: " + usVO.getUpdateType()
							+ " and crn: " + usVO.getCRN());
					updateStausForSuccess(seqId);
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 1);
					resultMap.put("communicationErrorFlag", 1);
				} else if (errorCode.equalsIgnoreCase("US_2")) {
					logger.debug("got error code of communication failure from ISIS for update status operation..");
					logger.debug("got error code for failure from webservice for update status operation" + errorCode);
					logger.debug("got error code for failure from webservice for update status operation"
							+ responseVO.getMessage());
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 0);
				} else if (errorCode.equalsIgnoreCase("US_1")) {
					logger.debug("got error code of communication failure from ISIS for update status operation..");
					logger.debug("got error code for failure from webservice for update status operation" + errorCode);
					logger.debug("got error code for failure from webservice for update status operation"
							+ responseVO.getMessage());
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 0);
					logger.debug("Response Details VO---" + responseVO.getResponseDetailsVo());
					if (responseVO.getResponseDetailsVo() != null) {
						ErrorDetailObjects errorDetailsObject = responseVO.getResponseDetailsVo()
								.getErrorDetailObjects();
						logger.debug("Error Details Object is----" + errorDetailsObject);
						if (errorDetailsObject != null) {
							String msg = "";
							logger.debug("Got error details object for failure from webservice");
							ErrorObjects orderErrObj;
							String[] orderArr;
							String orderId;
							int s;
							if (errorDetailsObject.getCategory() != null) {
								orderErrObj = errorDetailsObject.getCategory();
								logger.debug("got error object for category");
								if (orderErrObj.getCategoryID() != null) {
									orderArr = orderErrObj.getCategoryID().getCodes();
									orderId = "";
									logger.debug("category Ids----" + orderArr.length);

									for (s = 0; s < orderArr.length; ++s) {
										if (s == orderArr.length - 1) {
											orderId = orderId + orderArr[s];
										} else {
											orderId = orderId + orderArr[s] + ",";
										}
									}

									msg = orderId + "-" + orderErrObj.getErrorMessage();
								}
							} else if (errorDetailsObject.getAttribute() != null) {
								orderErrObj = errorDetailsObject.getAttribute();
								logger.debug("got error object for Attribute");
								if (orderErrObj.getAttribute() != null) {
									orderArr = orderErrObj.getAttribute().getCodes();
									orderId = "";
									logger.debug("Attribute Ids----" + orderArr.length);

									for (s = 0; s < orderArr.length; ++s) {
										if (s == orderArr.length - 1) {
											orderId = orderId + orderArr[s];
										} else {
											orderId = orderId + orderArr[s] + ",";
										}
									}

									msg = orderId + "-" + orderErrObj.getErrorMessage();
								}
							} else if (errorDetailsObject.getRisk() != null) {
								orderErrObj = errorDetailsObject.getRisk();
								logger.debug("got error object for Risk");
								if (orderErrObj.getRiskCode() != null) {
									orderArr = orderErrObj.getRiskCode().getCodes();
									orderId = "";
									logger.debug("risk Codes----" + orderArr.length);

									for (s = 0; s < orderArr.length; ++s) {
										if (s == orderArr.length - 1) {
											orderId = orderId + orderArr[s];
										} else {
											orderId = orderId + orderArr[s] + ",";
										}
									}

									msg = orderId + "-" + orderErrObj.getErrorMessage();
								}
							} else if (errorDetailsObject.getOrder() != null) {
								orderErrObj = errorDetailsObject.getOrder();
								logger.debug("got error object for Order");
								if (orderErrObj.getOrder() != null) {
									orderArr = orderErrObj.getOrder().getCodes();
									orderId = "";
									logger.debug("Order Array Length----" + orderArr.length);

									for (s = 0; s < orderArr.length; ++s) {
										if (s == orderArr.length - 1) {
											orderId = orderId + orderArr[s];
										} else {
											orderId = orderId + orderArr[s] + ",";
										}
									}

									msg = orderId + "-" + orderErrObj.getErrorMessage();
								}
							}

							logger.debug("AtlasWebServiceUtil::: Got Error Message For Risk Profile:::" + msg);
						}
					}
				} else {
					logger.debug("got error code of failure for update status operation");
					logger.debug("got error code for failure from webservice for update status operation" + errorCode);
					logger.debug("got error code for failure from webservice for update status operation"
							+ responseVO.getMessage());
					updateStausForFailureErrorCode(seqId);
					resultMap.put("errorCode", responseVO.getErrorCode());
					resultMap.put("successFlag", 0);
					resultMap.put("communicationErrorFlag", 1);
				}
			}
		}

		return resultMap;
	}

	public static ISISResponseVO updateISISMaster(UPMasterVO upMasterVO) throws CMSException {
		logger.debug("inside updateISISMaster method of AtlasWebServiceUtil class");
		ResponseVO responseVO = null;

		try {
			responseVO = ocrs.UPMaster(upMasterVO);
			ISISResponseVO isisResponseVo;
			if (responseVO != null) {
				if ("UPM_0".equalsIgnoreCase(responseVO.getErrorCode())) {
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("                                   SUCCESS                                         ");
					logger.debug("-----------------------------------------------------------------------------------");
					logger.debug("Got success flag........");
					logger.info("Master request sent successfully to ISIS ");
					isisResponseVo = new ISISResponseVO();
					isisResponseVo.setSuccess(true);
					isisResponseVo.setResponseVO(responseVO);
					return isisResponseVo;
				} else {
					logger.debug("Got Exception....");
					logger.debug("got error code for failure from webservice for subject operation"
							+ responseVO.getErrorCode());
					logger.debug("got error code for failure from webservice for subject operation"
							+ responseVO.getMessage());
					isisResponseVo = new ISISResponseVO();
					isisResponseVo.setSuccess(false);
					if (upMasterVO.getREMaster() != null) {
						responseVO.setMessage(
								"Error occured while updating EDDO for RE " + upMasterVO.getREMaster().getDescription()
										+ ".\n\r <br/>                                   Error description is:"
										+ responseVO.getMessage());
					} else {
						responseVO.setMessage(
								"Error occured while updating EDDO. Error description is:" + responseVO.getMessage());
					}

					isisResponseVo.setResponseVO(responseVO);
					return isisResponseVo;
				}
			} else {
				isisResponseVo = new ISISResponseVO();
				ResponseVO response = new ResponseVO();
				response.setErrorCode("UPM_5");
				response.setMessage("Error occured while updating EDDO");
				isisResponseVo.setSuccess(false);
				isisResponseVo.setResponseVO(response);
				return isisResponseVo;
			}
		} catch (Exception var5) {
			logger.error("Got Exception...." + getStackTraceAsString(var5));
			ISISResponseVO isisResponseVo = new ISISResponseVO();
			ResponseVO response = new ResponseVO();
			response.setErrorCode("UPM_6");
			response.setMessage("Communication Error with EDDO");
			isisResponseVo.setSuccess(false);
			isisResponseVo.setResponseVO(response);
			return isisResponseVo;
		}
	}

	public static boolean pingISIS() {
		logger.debug("inside pingISIS method of AtlasWebServiceUtil class");
		boolean responseFlag = false;

		try {
			String response = ocrs.pingISIS();
			if (response.equalsIgnoreCase("Ping_0")) {
				responseFlag = true;
			} else {
				responseFlag = false;
			}
		} catch (Exception var2) {
			responseFlag = false;
			logger.error("Inside Catch Block...");
			logger.error("Error is:::" + getStackTraceAsString(var2));
		}

		logger.debug("after call..." + responseFlag);
		return responseFlag;
	}

	public static void updateStausForSuccess(long sequenceId) {
		logger.debug("inside updateStausForSuccess method");

		try {
			sqlMap.update("AtlasWebServiceClient.updateForSuccess", sequenceId);
		} catch (SQLException var3) {
			logger.error("Some Error occured " + getStackTraceAsString(var3));
			logger.error("Error is" + var3.getMessage());
		} catch (Exception var4) {
			logger.debug("Some Error occured " + getStackTraceAsString(var4));
		}

	}

	public static void updateStausForManualSuccess(long sequenceId) {
		logger.debug("inside updateStausForSuccess method");

		try {
			sqlMap.update("AtlasWebServiceClient.updateForManualSuccess", sequenceId);
		} catch (SQLException var3) {
			logger.debug("Some Error occured " + getStackTraceAsString(var3));
			logger.error("Error is" + var3.getMessage());
		} catch (Exception var4) {
			logger.error("Some Error occured " + getStackTraceAsString(var4));
		}

	}

	public static void updateStausForFailureErrorCode(long sequenceId) {
		logger.debug("inside updateStausForFailureErrorCode method");

		try {
			sqlMap.update("AtlasWebServiceClient.updateForFailureErrorCode", sequenceId);
		} catch (SQLException var3) {
			logger.error("Some Error occured " + getStackTraceAsString(var3));
		} catch (Exception var4) {
			logger.error("Some Error occured " + getStackTraceAsString(var4));
		}

	}

	public static int getSameUnprocessedCRNCount(Map dataMap) throws SQLException {
		logger.debug("inside getSameUnprocessedCRNCount method");
		int count = false;
		int count = (Integer) sqlMap.queryForObject("AtlasWebServiceClient.getSameCRNCOunt", dataMap);
		logger.debug("unprocessed same crn count is:::" + count);
		return count;
	}

	public static String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}

	private static UPSubjectREVO[] convertCommaStringToREObjectArray(String commaSeparatedString) {
		logger.debug("----->>>>>>>>>>>>>>");
		logger.debug("inside convertCommaStringToREObjectArray method of AtlasWebServiceUtil class::::"
				+ commaSeparatedString);
		UPSubjectREVO[] reObj = (UPSubjectREVO[]) null;

		try {
			if (commaSeparatedString != null && !commaSeparatedString.equals("")) {
				StringTokenizer reTokenizer = new StringTokenizer(commaSeparatedString, ",");
				reObj = new UPSubjectREVO[reTokenizer.countTokens()];

				for (int index = 0; reTokenizer.hasMoreElements(); ++index) {
					UPSubjectREVO upSubjectREVO = new UPSubjectREVO();
					upSubjectREVO.setREID((String) reTokenizer.nextElement());
					reObj[index] = upSubjectREVO;
				}
			}
		} catch (Exception var5) {
			logger.error("Inside Error::::::::::::" + getStackTraceAsString(var5));
		}

		return reObj;
	}

	public static void updateISISSubjectId(int atlasSubjectId, String isisSubjectId, String crn) {
		logger.debug("atlasSubjectId is :: " + atlasSubjectId);

		try {
			Map subjectMap = new HashMap();
			subjectMap.put("isisSubjectId", isisSubjectId);
			subjectMap.put("updatedBy", resources.getString("atlas.webservice.case.creator.id"));
			subjectMap.put("atlasSubjectId", atlasSubjectId);
			subjectMap.put("crn", crn);
			logger.debug("subjectMap is:::::::::::::::::::::" + subjectMap);
			int count = sqlMap.update("AtlasWebServiceClient.UpdateSubjectForISISSubjectId", subjectMap);
			logger.debug("count value is ::" + count);
		} catch (SQLException var5) {
			logger.error("Some Error occured " + getStackTraceAsString(var5));
		} catch (Exception var6) {
			logger.error("Some Error occured " + getStackTraceAsString(var6));
		}

	}

	public static Call addUserCredentialForISISClientStub(Call _call) {
		logger.debug("Inside addUserCredentialForISISClientStub method of AtlasWebServiceutil");

		try {
			String user = resources.getString("isis.webservice.auth.user");
			String pwd = resources.getString("isis.webservice.auth.pwd");
			logger.debug("User is:::::::::::::::::::" + user);
			logger.debug("pwd is::::::::::::::::::::" + pwd);
			SOAPHeaderElement AuthSoapHd = new SOAPHeaderElement("http://orders.integrascreen.com", "AuthSoapHd");
			SOAPHeaderElement UserName = new SOAPHeaderElement("http://orders.integrascreen.com", "UserName", user);
			SOAPHeaderElement Password = new SOAPHeaderElement("http://orders.integrascreen.com", "Password", pwd);
			AuthSoapHd.addChild(UserName);
			AuthSoapHd.addChild(Password);
			_call.addHeader(AuthSoapHd);
		} catch (SOAPException var6) {
			logger.error("inside Error::::::::" + getStackTraceAsString(var6));
		}

		return _call;
	}

	private static SOAPHeaderElement getCredentialToHeader() {
		logger.debug("Inside addUserCredentialForISISClientStub method of AtlasWebServiceutil");
		SOAPHeaderElement AuthSoapHd = null;

		try {
			String user = resources.getString("isis.webservice.auth.user");
			String pwd = resources.getString("isis.webservice.auth.pwd");
			logger.debug("User is:::::::::::::::::::" + user);
			logger.debug("pwd is::::::::::::::::::::" + pwd);
			AuthSoapHd = new SOAPHeaderElement("http://orders.integrascreen.com", "AuthSoapHd");
			SOAPHeaderElement UserName = new SOAPHeaderElement("http://orders.integrascreen.com", "UserName", user);
			SOAPHeaderElement Password = new SOAPHeaderElement("http://orders.integrascreen.com", "Password", pwd);
			AuthSoapHd.addChild(UserName);
			AuthSoapHd.addChild(Password);
		} catch (SOAPException var5) {
			logger.error("inside Error::::::::" + getStackTraceAsString(var5));
		}

		return AuthSoapHd;
	}

	private static int getOverDueTimeForTrWaitQ() {
		logger.debug("Inside getOverDueTimeForTrWaitQ method of AtlasWebServiceUtil class");
		int overDueTime = 0;

		try {
			String trOverDueTime = resources.getString("atlas.messages.transcation.queue.wait.time.second");
			overDueTime = Integer.parseInt(trOverDueTime);
		} catch (Exception var2) {
			logger.error("inside Error::::::::" + getStackTraceAsString(var2));
		}

		logger.debug("overDueTime value is::::::" + overDueTime);
		return overDueTime;
	}

	private static int getMessageRetryMaxCount() {
		logger.debug("Inside getMessageRetryMaxCount method of AtlasWebServiceUtil class");
		int retryCount = 0;

		try {
			String count = resources.getString("atlas.messages.retry.count");
			retryCount = Integer.parseInt(count);
		} catch (Exception var2) {
			logger.error("inside Error::::::::" + getStackTraceAsString(var2));
		}

		logger.debug("retryCount value is::::::" + retryCount);
		return retryCount;
	}

	private static int getMessageRetryGapTime() {
		logger.debug("Inside getMessageRetryGapTime method of AtlasWebServiceUtil class");
		int retryGapTime = 0;

		try {
			String gapTime = resources.getString("atlas.messages.retry.gap.time.second");
			retryGapTime = Integer.parseInt(gapTime);
		} catch (Exception var2) {
			logger.error("inside Error::::::::" + getStackTraceAsString(var2));
		}

		logger.debug("retryGapTime value is::::::" + retryGapTime);
		return retryGapTime;
	}

	public static HashMap getPropertiesForProcess() {
		HashMap dataMap = new HashMap();
		dataMap.put("transcationOverDueTime", getOverDueTimeForTrWaitQ());
		dataMap.put("maxRetryCount", getMessageRetryMaxCount());
		dataMap.put("retryGapCount", getMessageRetryGapTime());
		return dataMap;
	}
}