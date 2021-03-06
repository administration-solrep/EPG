package fr.dila.solonepg.core.birt;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.google.common.collect.Lists;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgLifeCycleConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

public class ExportPANStatImpl extends STDomainObjectImpl implements ExportPANStat {

	private static final long serialVersionUID = 1L;

	public ExportPANStatImpl(DocumentModel doc) {
		super(doc);
	}

	@Override
	public String getOwner() {
		return getStringProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_OWNER_PROPERTY);
	}

	@Override
	public void setOwner(String owner) throws ClientException {
		setProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_OWNER_PROPERTY, owner);
	}

	@Override
	public Calendar getDateRequest() {
		return getDateProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_DATE_PROPERTY);
	}

	@Override
	public void setDateRequest(Calendar date) throws ClientException {
		setProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_DATE_PROPERTY, date);
	}

	@Override
	public boolean isExporting() throws ClientException {
		return SolonEpgLifeCycleConstants.EXPORTIING_STATE.equals(document.getCurrentLifeCycleState());
	}

	@Override
	public void setExporting(boolean exporting) throws ClientException {
		if (exporting) {
			document.followTransition(SolonEpgLifeCycleConstants.TO_EXPORTING_TRANSITION);
		} else {
			document.followTransition(SolonEpgLifeCycleConstants.TO_DONE_TRANSITION);
		}
	}

	@Override
	public void setFileContent(Blob xlsContent) throws ClientException {
		setProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_CONTENT_PROPERTY, xlsContent);
	}

	@Override
	public Blob getFileContent() {
		return getBlobProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_CONTENT_PROPERTY);
	}

	@Override
	public String getName() {

		Calendar calendar = this.getDateRequest();
		StringBuilder nameSb = new StringBuilder();
		String legislatures = getStringProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_LEGISLATURE_PROPERTY);

		nameSb.append("PAN-");
		nameSb.append(calendar.get(Calendar.YEAR));
		nameSb.append("-");
		nameSb.append(DateUtil.adjustDatePart(calendar.get(Calendar.MONTH) + 1));
		nameSb.append("-");
		nameSb.append(DateUtil.adjustDatePart(calendar.get(Calendar.DAY_OF_MONTH)));
		nameSb.append("-[");		
		nameSb.append(legislatures);
		nameSb.append("]");
		return nameSb.toString();

	}

	@Override
	public List<String> getExportedLegislatures() {

		String concatLegislatures = getStringProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_LEGISLATURE_PROPERTY);
		if(concatLegislatures != null) {
			return Lists.newArrayList(concatLegislatures.split(","));
		}
		
		return null;

	}

	@Override
	public void setExportedLegislatures(List<String> exportedLegislature) {
		Collections.sort(exportedLegislature);
		String legislatures = StringUtil.join(exportedLegislature, ",");
		setProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_LEGISLATURE_PROPERTY, legislatures);

	}

	@Override
	public boolean isCurLegis(CoreSession session) {

		String legislatures = getStringProperty(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
				ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_LEGISLATURE_PROPERTY);
		
		try {
			ParametrageAN param = SolonEpgServiceLocator.getSolonEpgParametreService().getDocAnParametre(session);
			int dernierElem = param.getLegislatures().indexOf(param.getLegislatureEnCours()) - 1;
			String labelLegisPrec = param.getLegislatures().get(dernierElem);			
			
			return !legislatures.equals(labelLegisPrec);
		} catch (Exception e) {
			
		}
		return true;
	}

}
