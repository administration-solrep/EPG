package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.Ministere;
import fr.sword.xsd.solon.epp.ObjetType;

/**
 * Implémentation de l'objet métier mandat.
 *
 * @author sly
 */
public class MinistereNodeImpl extends TableReferenceEppNodeImpl implements MinistereNode {
    private String identifiant;
    private final String typeValue;
    private String nom;
    private String libelle;
    private String appellation;
    private String edition;
    private String gouvernement;

    public void remapField(Ministere ministere) {
        this.identifiant = ministere.getId();
        this.nom = ministere.getNom();
        this.libelle = ministere.getLibelle();
        this.appellation = ministere.getAppellation();
        this.edition = ministere.getEdition();
        this.gouvernement = ministere.getIdGouvernement();
        if (ministere.getDateDebut() != null) {
            setDateDebut(DateUtil.xmlGregorianCalendarToDate(ministere.getDateDebut()));
        }
        if (ministere.getDateFin() != null) {
            setDateFin(DateUtil.xmlGregorianCalendarToDate(ministere.getDateFin()));
        }
        setHasChildren(ministere.isMandatAttache());
    }

    public MinistereNodeImpl() {
        super();
        this.typeValue = ObjetType.MINISTERE.value();
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getGouvernement() {
        return gouvernement;
    }

    public void setGouvernement(String gouvernement) {
        this.gouvernement = gouvernement;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getLabel() {
        return getNom();
    }

    public Ministere toMinistereXsd() {
        Ministere result = new Ministere();
        result.setId(this.getIdentifiant());
        result.setAppellation(this.getAppellation());
        result.setLibelle(this.getLibelle());
        result.setNom(this.getNom());
        result.setDateDebut(DateUtil.dateToXmlGregorianCalendar(this.getDateDebut()));
        result.setDateFin(DateUtil.dateToXmlGregorianCalendar(this.getDateFin()));
        result.setEdition(this.getEdition());
        result.setIdGouvernement(this.getGouvernement());
        return result;
    }
}
