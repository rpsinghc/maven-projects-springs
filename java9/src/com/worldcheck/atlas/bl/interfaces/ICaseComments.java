package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.comment.CommentsDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.CommentDetails;
import java.util.List;

public interface ICaseComments {
	void setCommentsDAO(CommentsDAO var1);

	void insertComments(String var1, String var2, String var3, String var4, boolean var5) throws CMSException;

	int insertComments(CommentDetails var1) throws CMSException;

	List<CommentDetails> getComments(String var1, int var2, String var3, String var4, int var5) throws CMSException;

	List<CommentDetails> getAllCommentsListForCRN(String var1) throws CMSException;

	int getCommentsCountForCRN(String var1) throws CMSException;

	void deleteCommentsForCRN(List<String> var1, String var2, String var3) throws CMSException;

	Integer getCaseStatus(String var1) throws CMSException;
}