package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.Gouvernement;
import fr.sword.xsd.solon.epp.ObjetType;

/**
 * Implémentation de l'objet métier gouvernement.
 *
 * @author sly
 */
public class GouvernementNodeImpl extends TableReferenceEppNodeImpl implements GouvernementNode {
    private String identifiant;
    private String appellation;
    private final String typeValue;

    public GouvernementNodeImpl() {
        super();
        this.typeValue = ObjetType.GOUVERNEMENT.value();
    }

    public void remapField(Gouvernement gouvernement) {
        this.identifiant = gouvernement.getId();
        this.appellation = gouvernement.getAppellation();
        if (gouvernement.getDateDebut() != null) {
            setDateDebut(DateUtil.xmlGregorianCalendarToDate(gouvernement.getDateDebut()));
        }
        if (gouvernement.getDateFin() != null) {
            setDateFin(DateUtil.xmlGregorianCalendarToDate(gouvernement.getDateFin()));
        }
        setHasChildren(gouvernement.isMinistereAttache());
    }

    public Gouvernement toGouvernementXsd() {
        Gouvernement result = new Gouvernement();
        result.setId(this.getIdentifiant());
        result.setAppellation(this.getAppellation());
        result.setDateDebut(DateUtil.dateToXmlGregorianCalendar(this.getDateDebut()));
        result.setDateFin(DateUtil.dateToXmlGregorianCalendar(this.getDateFin()));
        return result;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getTypeValue() {
        return this.typeValue;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getLabel() {
        return getAppellation();
    }
}
