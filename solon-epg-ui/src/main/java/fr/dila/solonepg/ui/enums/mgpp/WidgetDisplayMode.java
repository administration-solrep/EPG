package fr.dila.solonepg.ui.enums.mgpp;

/**
 * Conditions d'affichage d'un widget
 */
public enum WidgetDisplayMode {
    /** Toujours affiché sitôt qu'il est dans l'événement */
    ALWAYS,
    /** Affiché seulement si le contenu est non vide */
    IF_NOT_EMPTY,
    /**
     * Mode d'affichage spécifique au widget, à préciser dans
     * fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum.getFilterViewableMetadonnee()
     */
    WIDGET_SPECIFIC,
    /** Jamais affiché */
    NEVER
}
