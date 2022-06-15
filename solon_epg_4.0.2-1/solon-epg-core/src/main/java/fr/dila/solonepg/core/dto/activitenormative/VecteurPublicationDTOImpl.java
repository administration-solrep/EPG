package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Date;

public class VecteurPublicationDTOImpl extends AbstractMapDTO implements VecteurPublicationDTO {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public VecteurPublicationDTOImpl() {
        //Ren à faire ici
    }

    // Utilisé pour créer un bulletin officiel
    public VecteurPublicationDTOImpl(String libelleBulletinOfficiel) {
        setId(libelleBulletinOfficiel);
        setIntitule(libelleBulletinOfficiel);
    }

    public VecteurPublicationDTOImpl(VecteurPublication vecteur) {
        if (vecteur != null) {
            setId(vecteur.getDocument().getId());

            if (vecteur.getDateDebut() != null) {
                setDateDebut(vecteur.getDateDebut().getTime());
            }
            if (vecteur.getDateFin() != null) {
                setDateFin(vecteur.getDateFin().getTime());
            }
            setIntitule(vecteur.getIntitule());
        }
    }

    @Override
    public String getId() {
        return getString(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_ID);
    }

    @Override
    public void setId(String id) {
        put(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_ID, id);
    }

    @Override
    public String getIntitule() {
        return getString(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_INTITULE);
    }

    @Override
    public void setIntitule(String intitule) {
        put(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_INTITULE, intitule);
    }

    @Override
    public Date getDateDebut() {
        return getDate(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DEBUT);
    }

    @Override
    public void setDateDebut(Date debut) {
        put(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DEBUT, debut);
    }

    @Override
    public Date getDateFin() {
        return getDate(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_FIN);
    }

    @Override
    public void setDateFin(Date fin) {
        put(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_FIN, fin);
    }

    @Override
    public String getType() {
        return SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE;
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }

    // Très important: ce sera utilisé par le nxu:joinCollection qui construit la liste des vecteurs/bulletins en mode 'view'
    @Override
    public String toString() {
        return getIntitule();
    }
}
