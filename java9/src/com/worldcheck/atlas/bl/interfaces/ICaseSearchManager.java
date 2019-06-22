package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.List;

public interface ICaseSearchManager {
	List<UserMasterVO> getCaseCreatorMaster() throws CMSException;
}