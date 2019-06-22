package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.backup.UserBackUpManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.UserBackUpVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONUserBackUpMultiActionController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONBackupMultiActionController");
	private UserBackUpManager userBackUpManager;

	public void setUserBackUpManager(UserBackUpManager userBackUpManager) {
		this.userBackUpManager = userBackUpManager;
	}

	public ModelAndView getUserBackUpList(HttpServletRequest request, HttpServletResponse response,
			UserBackUpVO BackUpVO) {
		ModelAndView mv = null;

		try {
			this.logger.debug("inside JSONBackupMultiActionController ::  getBackInformation");
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("start value is:::" + Integer.parseInt(request.getParameter("start")));
			this.logger.debug("limit value is::" + Integer.parseInt(request.getParameter("limit")));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			String sortColumnName = request.getParameter("sort");
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			String sortType = request.getParameter("dir");
			mv = new ModelAndView("jsonView");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userId = userBean.getUserName();
			this.logger.debug("UserName:" + userId);
			List<UserBackUpVO> backupList = this.userBackUpManager.getUserBackUpList(userId, sortColumnName, sortType,
					start, limit);
			this.logger.debug("backup List Size::" + backupList.size());
			int count = this.userBackUpManager.getUserBackUpCount(userId);
			this.logger.debug("total BackupList::" + count);
			mv.addObject("backupList", backupList);
			mv.addObject("total", count);
			return mv;
		} catch (CMSException var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		} catch (Exception var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		}
	}
}