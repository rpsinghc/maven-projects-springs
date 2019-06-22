package com.worldcheck.atlas.listener;

import com.worldcheck.atlas.cache.service.CacheService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.subject.SubjectController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class AtlasSessionListener implements HttpSessionListener {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.listener.AtlasSessionListener");
	private List sessions = new ArrayList();

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		this.sessions.add(session.getId());
		session.setAttribute("loginUsercounter", String.valueOf(this.sessions.size()));
		session.setAttribute("_syncToken", SubjectController.nextToken());
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		this.logger.debug("Session Expired ");
		HttpSession session = se.getSession();
		String comment = "Either user closed the browser or left machine/browser inactive for 30 minutes";
		if (session.getAttribute("isLogoutLinkClicked") != null) {
			Boolean isLogoutLinkClicked = (Boolean) session.getAttribute("isLogoutLinkClicked");
			if (isLogoutLinkClicked) {
				comment = "Proper logout";
			}
		}

		try {
			if (session != null) {
				UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
				if (userDetailsBean != null) {
					ResourceLocator locator = ResourceLocator.self();
					UserMasterVO masterVO = locator.getUserService().getUserInfo(userDetailsBean.getLoginUserId());
					BufferedWriter writer = null;

					try {
						SimpleDateFormat dt1 = new SimpleDateFormat("MM-dd-yyyy");
						String userAgent = session.getAttribute("userAgent").toString();
						File file = new File("D:\\LoginLogs\\AtlasLogin-" + dt1.format(new Date()) + ".csv");
						if (file.exists()) {
							writer = new BufferedWriter(new FileWriter(file, true));
						} else {
							writer = new BufferedWriter(new FileWriter(file));
							writer.write("UserID,UserName,LoginTime,LogoutTime,MachineInfo,Comment");
							writer.newLine();
						}

						writer.write(userDetailsBean.getLoginUserId() + "," + masterVO.getUsername() + ",," + new Date()
								+ ",\"" + userAgent + "\"," + comment);
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (Exception var20) {
						;
					} finally {
						try {
							if (writer != null) {
								writer.close();
							}
						} catch (Exception var19) {
							;
						}

					}

					CacheService cacheService = ResourceLocator.self().getCacheService();
					HashMap<String, String> userTokenMap = cacheService.getACLTokenCache();
					this.logger.debug("userTokenMap before removing" + userTokenMap.size());
					if (userDetailsBean.getToken().equals(userTokenMap.get(userDetailsBean.getLoginUserId()))) {
						cacheService.removeACLTokenCache(userDetailsBean.getLoginUserId());
					}

					this.logger.debug("userTokenMap after removing" + userTokenMap.size());
				}
			}

			this.sessions.remove(session.getId());
			session.setAttribute("loginUsercounter", String.valueOf(this.sessions.size()));
		} catch (Exception var22) {
			new CMSException(this.logger, var22);
		}

	}
}