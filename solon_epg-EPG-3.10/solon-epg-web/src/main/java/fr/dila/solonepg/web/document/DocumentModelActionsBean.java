package fr.dila.solonepg.web.document;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.domain.comment.Comment;

/**
 * Ce WebBean permet d'injecter les classes des objets métiers pour les rendre disponible dans le contexte
 * Seam.
 * 
 * @author jtremeaux
 */
@Name("documentModelActions")
@Scope(ScopeType.APPLICATION)
@Install(precedence = FRAMEWORK + 2)
public class DocumentModelActionsBean extends fr.dila.st.web.document.DocumentModelActionsBean implements Serializable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne l'interface de l'objet métier Comment.
     * 
     * @return Interface de l'objet métier
     */
    @Factory(value = "Comment", scope = ScopeType.APPLICATION)
    public Class<Comment> getComment() {
        return Comment.class;
    }

    /**
     * Retourne l'interface de l'objet métier Dossier.
     * 
     * @return Interface de l'objet métier
     */
    @Factory(value = "Dossier", scope = ScopeType.APPLICATION)
    public Class<Dossier> getDossier() {
        return Dossier.class;
    }
    
    /**
     * Retourne l'interface de l'objet métier BulletinOfficiel.
     * 
     * @return Interface de l'objet métier
     */
    @Factory(value = "BulletinOfficiel", scope = ScopeType.APPLICATION)
    public Class<BulletinOfficiel> getBulletinOfficiel() {
        return BulletinOfficiel.class;
    }
    
    /**
     * Retourne l'interface de l'objet métier IndexationMotCle.
     * 
     * @return Interface de l'objet métier
     */
    @Factory(value = "IndexationMotCle", scope = ScopeType.APPLICATION)
    public Class<IndexationMotCle> getIndexationMotCle() {
        return IndexationMotCle.class;
    }
    
    /**
     * Retourne l'interface de l'objet métier IndexationRubrique.
     * 
     * @return Interface de l'objet métier
     */
    @Factory(value = "IndexationRubrique", scope = ScopeType.APPLICATION)
    public Class<IndexationRubrique> getIndexationRubrique() {
        return IndexationRubrique.class;
    }
    
    
}
