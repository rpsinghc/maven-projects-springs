package com.worldcheck.atlas.frontend;

import com.worldcheck.atlas.frontend.JSONUpdateCTDataController.1;
import com.worldcheck.atlas.frontend.JSONUpdateCTDataController.2;
import com.worldcheck.atlas.frontend.JSONUpdateCTDataController.3;
import com.worldcheck.atlas.frontend.JSONUpdateCTDataController.4;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.UpdateCTDataExcelVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONUpdateCTDataController extends JSONMultiActionController {
	private static final String UPDATEDCTDATASTATUS = "UpdatedCTDataStatus";
	private static final String RESULTLIST = "resultList";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.JSONUpdateCTDataController");

	public ModelAndView getUpdatedCTDataStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
      this.logger.debug("In JSONUpdateCTDataController : getUpdatedCTDataStatus");
      ModelAndView mv = new ModelAndView("jsonView");
      List<UpdateCTDataExcelVO> resultList = new ArrayList();
      List<UpdateCTDataExcelVO> sendItemList = new ArrayList();
      int start = Integer.parseInt(StringUtils.checkPaginationParams("start", request.getParameter("start")));
      int limit = Integer.parseInt(StringUtils.checkPaginationParams("limit", request.getParameter("limit")));
      String direction = request.getParameter("dir");
      String sortType = request.getParameter("sort");

      try {
         if (request.getSession().getAttribute("UpdatedCTDataStatus") != null) {
            resultList = (List)request.getSession().getAttribute("UpdatedCTDataStatus");
            Object comparator;
            if (null != sortType && !"null".equalsIgnoreCase(sortType) && !"".equalsIgnoreCase(sortType) && "status".equalsIgnoreCase(sortType)) {
               comparator = null;
               if (null != direction && !"null".equalsIgnoreCase(direction) && !"".equalsIgnoreCase(direction) && !"ASC".equalsIgnoreCase(direction)) {
                  comparator = new 1(this);
               } else {
                  comparator = new 2(this);
               }

               Collections.sort((List)resultList, (Comparator)comparator);
            } else {
               comparator = null;
               if (null != direction && !"null".equalsIgnoreCase(direction) && !"".equalsIgnoreCase(direction) && !"ASC".equalsIgnoreCase(direction)) {
                  comparator = new 3(this);
               } else {
                  comparator = new 4(this);
               }

               Collections.sort((List)resultList, (Comparator)comparator);
            }

            this.logger.debug("resultList.size() is " + ((List)resultList).size());
            limit += start;
            if (((List)resultList).size() >= limit) {
               sendItemList = ((List)resultList).subList(start, limit);
            } else {
               sendItemList = ((List)resultList).subList(start, ((List)resultList).size());
            }
         }
      } catch (Exception var11) {
         return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
      }

      mv.addObject("resultList", sendItemList);
      mv.addObject("total", ((List)resultList).size());
      return mv;
   }
}