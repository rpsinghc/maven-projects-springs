package com.worldcheck.atlas.validation.bl;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.NoSingleSectionException;
import org.apache.poi.hpsf.Property;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.Section;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;

public class AtlasPOIFSReaderListener implements POIFSReaderListener {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.validation.bl.MyPOIFSReaderListener");
	public static String excelFileCategory = "";

	public void processPOIFSReaderEvent(POIFSReaderEvent event) {
		this.logger.debug("In processPOIFSReaderEvent");
		DocumentSummaryInformation si = null;
		Object var3 = null;

		try {
			si = (DocumentSummaryInformation) PropertySetFactory.create(event.getStream());
			excelFileCategory = si.getCategory();
		} catch (NoSingleSectionException var7) {
			this.logger.debug("NoSingleSectionException for DocumentSummaryInformation.getCategory()");
			Property[] p = ((Section) ((Section) si.getSections().get(0))).getProperties();

			for (int i = 0; i < p.length; ++i) {
				if (p[i].getType() == 30L) {
					excelFileCategory = p[i].getValue().toString();
					break;
				}
			}
		} catch (Exception var8) {
			throw new RuntimeException("Property set stream \"" + event.getPath() + event.getName() + "\": " + var8);
		}

		if (excelFileCategory != null) {
			this.logger.debug("Category: \"" + excelFileCategory + "\"");
		} else {
			this.logger.debug("Document has no category.");
		}

	}
}