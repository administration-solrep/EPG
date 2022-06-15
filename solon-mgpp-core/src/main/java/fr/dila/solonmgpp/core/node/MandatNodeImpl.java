package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.ObjetType;
import fr.sword.xsd.solon.epp.TypeMandat;

public class MandatNodeImpl extends TableReferenceEppNodeImpl implements MandatNode {
    private final String typeValue;
    private String identifiant;
    private String proprietaire;
    private String typeMandat;
    private Long ordreProtocolaire;
    private String nor;
    private String titre;
    private IdentiteNode identite;
    private String ministere;
    private String circonscription;
    private String appellation;
    private String label;

    public MandatNodeImpl() {
        super();
        this.identite = new IdentiteNodeImpl();
        this.typeValue = ObjetType.MANDAT.value();
    }

    public void remapField(Mandat mandat) {
        setIdentifiant(mandat.getId());
        setProprietaire(mandat.getProprietaire() == null ? null : mandat.getProprietaire().value());
        setOrdreProtocolaire(
            mandat.getOrdreProtocolaire() == null ? null : Long.valueOf(mandat.getOrdreProtocolaire())
        );
        setTitre(mandat.getTitre());
        setCirconscription(mandat.getIdCirconscription());
        setAppellation(mandat.getAppellation());
        setMinistere(mandat.getIdMinistere());
        setTypeMandat(mandat.getType().value());
        if (mandat.getDateDebut() != null) {
            setDateDebut(DateUtil.xmlGregorianCalendarToDate(mandat.getDateDebut()));
        }
        if (mandat.getDateFin() != null) {
            setDateFin(DateUtil.xmlGregorianCalendarToDate(mandat.getDateFin()));
        }
        setNor(mandat.getNor());
        this.identite.setIdentifiant(mandat.getIdIdentite());
        if (mandat.getIdentiteDenormalise() != null) {
            StringBuilder label = new StringBuilder(mandat.getIdentiteDenormalise().getCivilite().value())
                .append(" ")
                .append(mandat.getIdentiteDenormalise().getPrenom())
                .append(" ")
                .append(mandat.getIdentiteDenormalise().getNom());
            setLabel(label.toString());
        }
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getTypeMandat() {
        return typeMandat;
    }

    public void setTypeMandat(String typeMandat) {
        this.typeMandat = typeMandat;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMinistere() {
        return ministere;
    }

    public void setMinistere(String ministere) {
        this.ministere = ministere;
    }

    public String getCirconscription() {
        return circonscription;
    }

    public void setCirconscription(String circonscription) {
        this.circonscription = circonscription;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public IdentiteNode getIdentite() {
        return identite;
    }

    public void setIdentite(IdentiteNode identite) {
        this.identite = identite;
    }

    public Long getOrdreProtocolaire() {
        return ordreProtocolaire;
    }

    public void setOrdreProtocolaire(Long ordreProtocolaire) {
        this.ordreProtocolaire = ordreProtocolaire;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNor() {
        return this.nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public Mandat toMandatXsd() {
        Mandat result = new Mandat();
        result.setId(this.getIdentifiant());
        result.setDateDebut(DateUtil.dateToXmlGregorianCalendar(this.getDateDebut()));
        result.setDateFin(DateUtil.dateToXmlGregorianCalendar(this.getDateFin()));
        if (this.getOrdreProtocolaire() != null) {
            result.setOrdreProtocolaire(this.getOrdreProtocolaire().intValue());
        }

        Institution institution = null;
        if (this.getProprietaire() != null) {
            institution = Institution.valueOf(this.getProprietaire());
        }

        result.setProprietaire(institution);
        result.setTitre(this.getTitre());

        TypeMandat typeMandat = null;
        if (this.getTypeMandat() != null) {
            typeMandat = TypeMandat.valueOf(this.getTypeMandat());
        }
        result.setType(typeMandat);
        result.setIdCirconscription(this.getCirconscription());
        result.setIdIdentite(this.getIdentite().getIdentifiant());
        result.setIdMinistere(this.getMinistere());
        result.setAppellation(this.getAppellation());
        result.setIdIdentite(this.getIdentite().getIdentifiant());
        result.setProprietaire(Institution.GOUVERNEMENT);
        result.setNor(this.getNor());
        return result;
    }
}
