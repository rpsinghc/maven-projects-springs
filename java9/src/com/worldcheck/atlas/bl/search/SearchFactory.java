package com.worldcheck.atlas.bl.search;

import com.worldcheck.atlas.bl.interfaces.ISearch;

public class SearchFactory {
	public static final String SUBJECT_SEARCH = "SubjectSearch";
	public static final String KEYWORD_SEARCH = "KeywordSearch";
	public static final String MESSAGE_SEARCH = "MessageSearch";
	public static final String CASE_SEARCH = "CaseSearch";
	public static final String TEAM_SEARCH = "TeamSearch";
	TeamSearchManager teamSearch;
	CaseSearchManager caseSearch;
	MessageSearchManager messageSearch;
	KeywordSearch keywordSearch;
	SubjectSearchManager subjectSearch;

	public void setCaseSearch(CaseSearchManager caseSearch) {
		this.caseSearch = caseSearch;
	}

	public void setMessageSearch(MessageSearchManager messageSearch) {
		this.messageSearch = messageSearch;
	}

	public void setKeywordSearch(KeywordSearch keywordSearch) {
		this.keywordSearch = keywordSearch;
	}

	public void setSubjectSearch(SubjectSearchManager subjectSearch) {
		this.subjectSearch = subjectSearch;
	}

	public void setTeamSearch(TeamSearchManager teamSearch) {
		this.teamSearch = teamSearch;
	}

	public ISearch getSearchImpl(String searchType) {
		if (searchType.equals("TeamSearch")) {
			return this.teamSearch;
		} else if (searchType.equals("CaseSearch")) {
			return this.caseSearch;
		} else if (searchType.equals("MessageSearch")) {
			return this.messageSearch;
		} else if (searchType.equals("KeywordSearch")) {
			return this.keywordSearch;
		} else {
			return searchType.equals("SubjectSearch") ? this.subjectSearch : null;
		}
	}
}