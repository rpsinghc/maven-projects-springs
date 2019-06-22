package com.worldcheck.atlas.sbm;

import com.savvion.common.queue.Queue;
import com.savvion.common.queue.QueueManager;
import com.savvion.common.queue.QueueMember;
import com.savvion.sbm.util.PService;
import com.tdiinc.userManager.JDBCRealm;
import com.tdiinc.userManager.User;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.sbm.UserCreationVO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class SBMUserManager extends SqlMapClientTemplate {
	private final ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.SBMUserManager");

	public long getUserId(String userId) throws CMSException {
		long id = 0L;
		JDBCRealm jdbc = new JDBCRealm();

		try {
			User user = jdbc.getUser(userId);
			if (null != user) {
				id = Long.parseLong(user.getAttribute("userid"));
			} else {
				this.logger.debug("user does not exist..");
			}

			return id;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public String getPassword(String userId) throws CMSException {
		String password = "";
		JDBCRealm jdbc = new JDBCRealm();

		try {
			User user = jdbc.getUser(userId);
			if (null != user) {
				password = user.getAttribute("password");
			} else {
				this.logger.debug("user does not exist..");
			}

			return password;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean isUserAdded(String userName, UserCreationVO userVO) throws CMSException {
		boolean added = false;
		JDBCRealm jdbc = new JDBCRealm();
		Hashtable userAttr = new Hashtable();

		try {
			User user = jdbc.getUser(userName);
			if (null != user) {
				this.logger.debug("The user already exists.");
				return added;
			}
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.getUserAttributes(userVO, userAttr);
		added = jdbc.addUser(userName, userAttr);
		return added;
	}

	private void getUserAttributes(UserCreationVO userVO, Hashtable<String, String> userAttr) throws CMSException {
		try {
			if (null != userVO.getCountry() && !"".equalsIgnoreCase(userVO.getCountry())) {
				userAttr.put("country", userVO.getCountry());
			}

			if (null != userVO.getFirstName() && !"".equalsIgnoreCase(userVO.getFirstName())) {
				userAttr.put("firstname", userVO.getFirstName());
			}

			if (null != userVO.getLastName() && !"".equalsIgnoreCase(userVO.getLastName())) {
				userAttr.put("lastname", userVO.getLastName());
			}

			if (null != userVO.getOrganization() && !"".equalsIgnoreCase(userVO.getOrganization())) {
				userAttr.put("organization", userVO.getOrganization());
			}

			if (null != userVO.getPassword() && !"".equalsIgnoreCase(userVO.getPassword())) {
				userAttr.put("password", PService.self().decrypt(userVO.getPassword()));
			}

			if (null != userVO.getEmail() && !"".equalsIgnoreCase(userVO.getEmail())) {
				userAttr.put("email", userVO.getEmail());
			}

			if (null != userVO.getPhone() && !"".equalsIgnoreCase(userVO.getPhone())) {
				userAttr.put("phone", userVO.getPhone());
			}

			if (null != userVO.getSkills() && !"".equalsIgnoreCase(userVO.getSkills())) {
				userAttr.put("skills", userVO.getSkills());
			}

			if (null != userVO.getState() && !"".equalsIgnoreCase(userVO.getState())) {
				userAttr.put("state", userVO.getState());
			}

			if (null != userVO.getCity() && !"".equalsIgnoreCase(userVO.getCity())) {
				userAttr.put("city", userVO.getCity());
			}

			if (null != userVO.getTenancy() && !"".equalsIgnoreCase(userVO.getTenancy())) {
				userAttr.put("tenancy", userVO.getTenancy());
			}

			if (null != userVO.getTitle() && !"".equalsIgnoreCase(userVO.getTitle())) {
				userAttr.put("title", userVO.getTitle());
			}

			if (null != userVO.getUrl() && !"".equalsIgnoreCase(userVO.getUrl())) {
				userAttr.put("user_url", userVO.getUrl());
			}

			if (null != userVO.getZipCode() && !"".equalsIgnoreCase(userVO.getZipCode())) {
				userAttr.put("zipCode", userVO.getZipCode());
			}

		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String encrypt(String value) throws CMSException {
		String encryptedStr = "";

		try {
			encryptedStr = PService.self().encrypt(value);
			return encryptedStr;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String decrypt(String value) throws CMSException {
		String decryptedStr = "";

		try {
			decryptedStr = PService.self().decrypt(value);
			return decryptedStr;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public boolean isUserRemoved(String userName) throws CMSException {
		boolean deleted = false;

		try {
			JDBCRealm jdbc = new JDBCRealm();
			deleted = jdbc.removeUser(userName);
			return deleted;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public boolean isPassWordUpdated(String userName, String password) throws CMSException {
		boolean updated = false;

		try {
			JDBCRealm jdbc = new JDBCRealm();
			User user = jdbc.getUser(userName);
			if (null != user) {
				updated = user.setAttribute("password", password);
				this.logger.debug("updation Successfully completed");
			} else {
				this.logger.debug("Can't update password.User not found!!");
			}

			return updated;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public String getEmailId(String userName) throws CMSException {
		String email = "";

		try {
			JDBCRealm jdbc = new JDBCRealm();
			User user = jdbc.getUser(userName);
			if (null != user) {
				email = user.getAttribute("email");
				this.logger.debug("email id is " + email);
			} else {
				this.logger.debug("Can't get Email Id.User not found!!");
			}

			return email;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean isEmailUpdated(String userName, String emailId) throws CMSException {
		boolean updated = false;

		try {
			JDBCRealm jdbc = new JDBCRealm();
			User user = jdbc.getUser(userName);
			if (null != user) {
				updated = user.setAttribute("email", emailId);
				this.logger.debug("updation Successfully completed");
			} else {
				this.logger.debug("Can't update email Id.User not found!!");
			}

			return updated;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public boolean isFullNameUpdated(String userName, String fName, String lName) throws CMSException {
		boolean updated = false;

		try {
			JDBCRealm jdbc = new JDBCRealm();
			User user = jdbc.getUser(userName);
			if (null != user) {
				updated = user.setAttribute("firstname", fName);
				if (updated) {
					updated = user.setAttribute("lastname", lName);
				}

				this.logger.debug("updation Successfully completed");
			} else {
				this.logger.debug("Can't update user name.User not found!!");
			}

			return updated;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public boolean isEmailIdExists(String emailId) throws CMSException {
		boolean exists = false;

		try {
			JDBCRealm jdbc = new JDBCRealm();
			String[] users = jdbc.getUserNames("email", emailId);
			if (users != null && users.length > 0) {
				exists = true;
				this.logger.debug("Email exists for user " + users[0]);
			}

			return exists;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Queue getQueue(String queueName) throws CMSException {
		this.logger.debug("getQueue(String queueName) --->" + queueName);
		Queue queue = null;

		try {
			QueueManager qm = new QueueManager(this.getDataSource());
			ArrayList queues = qm.search(queueName);
			this.logger.debug("queues-->" + queues);
			if (null != queues && queues.size() != 0) {
				queue = (Queue) queues.get(0);
			}
		} catch (SQLException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("queue-->" + queue);
		return queue;
	}

	public void createQueue(String queueName) throws CMSException {
		long qid = 0L;

		try {
			QueueManager qm = new QueueManager(this.getDataSource());
			Queue queue = new Queue(queueName);
			queue.setDescription(queueName);
			qid = qm.create(queue);
		} catch (SQLException var6) {
			throw new CMSException(this.logger, var6);
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("queue id is " + qid);
	}

	public boolean isUserAddedToQueue(String userName, String queueName) throws CMSException {
		boolean isAdded = false;
		Queue queue = null;
		QueueMember qMember = null;

		try {
			queue = this.getQueue(queueName);
			if (null == queue) {
				this.createQueue(queueName);
				queue = this.getQueue(queueName);
			}

			if (!this.isMemberExist(queue, userName)) {
				qMember = new QueueMember(userName, false);
				queue.addMember(this.getDataSource(), qMember);
				queue.save(this.getDataSource());
				this.logger.debug("Member " + qMember.getName() + " added to " + queue.getName());
			} else {
				this.logger.debug("Member " + userName + " already exists in " + queue.getName());
			}

			isAdded = true;
			return isAdded;
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public boolean isMemberExist(Queue queue, String memberName) {
		boolean isExist = false;
		ArrayList members = queue.getMembers(this.getDataSource());
		if (null != members) {
			for (int i = 0; i < members.size(); ++i) {
				QueueMember qMember = (QueueMember) members.get(i);
				if (null != qMember && memberName.equals(qMember.getName())) {
					return true;
				}
			}
		}

		return isExist;
	}

	public boolean removeQueueMember(String userName, String queueName) throws CMSException {
		boolean isUserRemoved = false;
		Queue queue = null;
		QueueMember qMember = null;
		if (null != userName && null != queueName && userName.trim().length() > 0 && queueName.trim().length() > 0) {
			queue = this.getQueue(queueName);
			qMember = new QueueMember(userName, false);
			if (this.isMemberExist(queue, userName)) {
				queue.removeMember(this.getDataSource(), qMember);
				queue.save(this.getDataSource());
				this.logger.debug("Member " + qMember.getName() + " removed from " + queue.getName());
				isUserRemoved = true;
			}
		}

		return isUserRemoved;
	}
}