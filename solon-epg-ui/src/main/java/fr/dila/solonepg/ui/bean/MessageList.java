package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageList implements MgppExportable {
    private final List<ColonneInfo> listeColonnes = new ArrayList<>();

    private List<MgppMessageDTO> liste = new ArrayList<>();

    private static final String COLUMN_ID_DOSSIER = "idDossierTri";
    private static final String COLUMN_OBJET_DOSSIER = "objetDossierTri";
    private static final String COLUMN_LECTURE = "nivLectureTri";
    private static final String COLUMN_EMETTEUR = "emetteurTri";
    private static final String COLUMN_DESTINATAIRE = "destinataireTri";
    private static final String COLUMN_COPIE = "copieTri";
    private static final String COLUMN_COMMUNICATION = "communicationTri";
    private static final String COLUMN_DATE = "dateTri";
    private static final String COLUMN_ID_COMMUNICATION = "idCommunicationTri";

    private Integer nbTotal;

    private String titre;

    private String sousTitre;

    public MessageList() {
        this.nbTotal = 0;
    }

    public List<MgppMessageDTO> getListe() {
        return liste;
    }

    public void setListe(List<MgppMessageDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    @Override
    public List<ColonneInfo> getListeColonnes() {
        return listeColonnes;
    }

    public List<ColonneInfo> getListeColonnes(MessageListForm form) {
        buildColonnes(form);
        return listeColonnes;
    }

    public void buildColonnes(MessageListForm form) {
        listeColonnes.clear();

        if (form != null) {
            listeColonnes.add(new ColonneInfo(null, false, true, false, true));
            listeColonnes.add(
                new ColonneInfo("label.mgpp.evenement.idDossier", true, COLUMN_ID_DOSSIER, form.getIdDossier())
            );
            listeColonnes.add(
                new ColonneInfo("label.mgpp.evenement.objetdossier", true, COLUMN_OBJET_DOSSIER, form.getObjetDossier())
            );
            listeColonnes.add(
                new ColonneInfo("label.mgpp.evenement.niveauLecture", true, COLUMN_LECTURE, form.getNivLecture())
            );
            listeColonnes.add(
                new ColonneInfo("label.mgpp.evenement.emetteur", true, COLUMN_EMETTEUR, form.getEmetteur())
            );
            listeColonnes.add(
                new ColonneInfo("label.mgpp.evenement.destinataire", true, COLUMN_DESTINATAIRE, form.getDestinataire())
            );
            listeColonnes.add(new ColonneInfo("label.mgpp.evenement.copie", true, COLUMN_COPIE, form.getCopie()));
            listeColonnes.add(
                new ColonneInfo(
                    "label.mgpp.evenement.typeEvenement",
                    true,
                    COLUMN_COMMUNICATION,
                    form.getCommunication()
                )
            );
            listeColonnes.add(new ColonneInfo("label.mgpp.evenement.date", true, COLUMN_DATE, form.getDate()));
            listeColonnes.add(
                new ColonneInfo(
                    "label.mgpp.evenement.idEvenement",
                    true,
                    COLUMN_ID_COMMUNICATION,
                    form.getIdCommunication()
                )
            );
        }
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSousTitre() {
        return sousTitre;
    }

    public void setSousTitre(String sousTitre) {
        this.sousTitre = sousTitre;
    }

    @Override
    public String getExportName() {
        return titre;
    }

    @Override
    public List<List<String>> getDataForExport() {
        return liste
            .stream()
            .map(
                item ->
                    Arrays.asList(
                        item.getIdDossier(),
                        item.getObjetDossier(),
                        item.getNivLecture(),
                        item.getEmetteur(),
                        item.getDestinataire(),
                        item.getCopie(),
                        item.getCommunication(),
                        item.getDate(),
                        item.getId()
                    )
            )
            .collect(Collectors.toList());
    }
}
