package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.vo.masters.GradingPoint;
import com.worldcheck.atlas.vo.masters.ScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.masters.ScoreSheetSubCategoryVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class CategoryXmlParser {
	static HashMap<Object, Object> keyValueMap = new HashMap();
	static HashMap<String, ScoreSheetCategoryVO> catMap = new HashMap();
	static List userRoleTypeList = new ArrayList();
	static List<ScoreSheetCategoryVO> catList = new ArrayList();
	static Document doc = null;

	private static void parseSubCategory(List<Element> elementList, ScoreSheetCategoryVO scoreSheetCategoryVO) {
		for (Iterator i$ = elementList.iterator(); i$.hasNext(); keyValueMap.put("UserRoleType", userRoleTypeList)) {
			Element key = (Element) i$.next();
			List<Element> valueOfElements = key.elements();
			if (valueOfElements.size() > 0) {
				Element subCatElement = (Element) valueOfElements.get(0);
				ScoreSheetSubCategoryVO scoreSheetSubCategoryVO = new ScoreSheetSubCategoryVO();
				scoreSheetSubCategoryVO.setScoreSheetSubCategoryName(subCatElement.getText());
				scoreSheetCategoryVO.getScoreSheetSubCategory().add(scoreSheetSubCategoryVO);
				valueOfElements.remove(subCatElement);
				parseErrorAndCount(valueOfElements, scoreSheetCategoryVO, scoreSheetSubCategoryVO);
			}
		}

	}

	private static void parseErrorAndCount(List<Element> elementList, ScoreSheetCategoryVO scoreSheetCategoryVO,
			ScoreSheetSubCategoryVO scoreSheetSubCategoryVO) {
		if (elementList.size() > 0) {
			GradingPoint gradingPoint = new GradingPoint();
			Element key = (Element) elementList.get(0);
			String keyString = key.getName();
			String valueString = key.getText();
			gradingPoint.setCountOfError(key.getText());
			Element key1 = (Element) elementList.get(1);
			keyString = key1.getName();
			valueString = key1.getText();
			gradingPoint.setPoint(key1.getText());
			elementList.remove(key);
			elementList.remove(key1);
			scoreSheetSubCategoryVO.getGradingPoint().add(gradingPoint);
			parseErrorAndCount(elementList, scoreSheetCategoryVO, scoreSheetSubCategoryVO);
		}

	}

	public static List<ScoreSheetCategoryVO> parseXml(String xml) {
		try {
			catList = new ArrayList();
			Document document = DocumentHelper.parseText(xml);
			List entries = document.getRootElement().selectNodes("/scoreSheet");
			Iterator i$ = entries.iterator();

			while (i$.hasNext()) {
				Object node = i$.next();

				try {
					Element annotationElement = (Element) node;
					List<Element> scoreElements = annotationElement.elements();
					Iterator i$ = scoreElements.iterator();

					while (i$.hasNext()) {
						Element key = (Element) i$.next();
						List<Element> valueOfElements = key.elements();
						if (valueOfElements.size() > 0) {
							Element category = (Element) valueOfElements.get(0);
							ScoreSheetCategoryVO scoreSheetCategoryVO = new ScoreSheetCategoryVO();
							String catName = category.getText();
							scoreSheetCategoryVO.setCategoryName(category.getText());
							valueOfElements.remove(category);
							parseSubCategory(valueOfElements, scoreSheetCategoryVO);
							catList.add(scoreSheetCategoryVO);
						}
					}
				} catch (Exception var13) {
					HashMap<String, String> keyValueMap = new HashMap();
					keyValueMap.put("name", "XML Note Failed");
					keyValueMap.put("notes", var13.getMessage());
				}
			}
		} catch (Exception var14) {
			var14.printStackTrace();
		}

		return catList;
	}

	public HashMap<Object, Object> getElements() {
		return keyValueMap;
	}

	public static void main(String[] args) {
	}
}