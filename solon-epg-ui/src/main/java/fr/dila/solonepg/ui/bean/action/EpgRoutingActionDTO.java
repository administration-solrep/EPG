package fr.dila.solonepg.ui.bean.action;

import fr.dila.ss.ui.bean.actions.RoutingActionDTO;

public class EpgRoutingActionDTO extends RoutingActionDTO {

    public EpgRoutingActionDTO() {
        super();
    }

    private boolean isStepRetourModification;

    private boolean isStepAvis;

    private boolean isStepInitialisation;

    private boolean isStepPourBonATirer;

    private boolean isStepContreseign;

    private boolean canRetourModification;

    private boolean canValideNonConcerne;

    private boolean canRefusValidation;

    private boolean isDossierLance;

    private boolean isActeTexteNonPublie;

    private boolean isEtapePourAvisCE;

    private boolean isSqueletteFeuilleRoute;

    private boolean hasRunningStep;

    private boolean isFeuilleRouteRestartable;

    private boolean isRapportAuParlement;

    private boolean isStepExecutable;

    public boolean getIsStepRetourModification() {
        return isStepRetourModification;
    }

    public void setStepRetourModification(boolean isStepRetourModification) {
        this.isStepRetourModification = isStepRetourModification;
    }

    public boolean getIsStepAvis() {
        return isStepAvis;
    }

    public void setStepAvis(boolean isStepAvis) {
        this.isStepAvis = isStepAvis;
    }

    public boolean getIsStepInitialisation() {
        return isStepInitialisation;
    }

    public void setStepInitialisation(boolean isStepInitialisation) {
        this.isStepInitialisation = isStepInitialisation;
    }

    public boolean getIsStepPourBonATirer() {
        return isStepPourBonATirer;
    }

    public void setStepPourBonATirer(boolean isStepPourBonATirer) {
        this.isStepPourBonATirer = isStepPourBonATirer;
    }

    public boolean getIsStepContreseign() {
        return isStepContreseign;
    }

    public void setStepContreseign(boolean isStepContreseign) {
        this.isStepContreseign = isStepContreseign;
    }

    public boolean getCanRetourModification() {
        return canRetourModification;
    }

    public void setCanRetourModification(boolean canRetourModification) {
        this.canRetourModification = canRetourModification;
    }

    public boolean getCanValideNonConcerne() {
        return canValideNonConcerne;
    }

    public void setCanValideNonConcerne(boolean canValideNonConcerne) {
        this.canValideNonConcerne = canValideNonConcerne;
    }

    public boolean getCanRefusValidation() {
        return canRefusValidation;
    }

    public void setCanRefusValidation(boolean canRefusValidation) {
        this.canRefusValidation = canRefusValidation;
    }

    public boolean getIsDossierLance() {
        return isDossierLance;
    }

    public void setDossierLance(boolean isDossierLance) {
        this.isDossierLance = isDossierLance;
    }

    public boolean getIsActeTexteNonPublie() {
        return isActeTexteNonPublie;
    }

    public void setActeTexteNonPublie(boolean isActeTexteNonPublie) {
        this.isActeTexteNonPublie = isActeTexteNonPublie;
    }

    public boolean getIsEtapePourAvisCE() {
        return isEtapePourAvisCE;
    }

    public void setIsEtapePourAvisCE(boolean isEtapePourAvisCE) {
        this.isEtapePourAvisCE = isEtapePourAvisCE;
    }

    public boolean getIsSqueletteFeuilleRoute() {
        return isSqueletteFeuilleRoute;
    }

    public void setIsSqueletteFeuilleRoute(boolean isSqueletteFeuilleRoute) {
        this.isSqueletteFeuilleRoute = isSqueletteFeuilleRoute;
    }

    public boolean getHasRunningStep() {
        return hasRunningStep;
    }

    public void setHasRunningStep(boolean hasRunningStep) {
        this.hasRunningStep = hasRunningStep;
    }

    public boolean getIsFeuilleRouteRestartable() {
        return isFeuilleRouteRestartable;
    }

    public void setFeuilleRouteRestartable(boolean isFeuilleRouteRestartable) {
        this.isFeuilleRouteRestartable = isFeuilleRouteRestartable;
    }

    public boolean getIsRapportAuParlement() {
        return this.isRapportAuParlement;
    }

    public void setIsRapportAuParlement(boolean isRapportAuParlement) {
        this.isRapportAuParlement = isRapportAuParlement;
    }

    public boolean getIsStepExecutable() {
        return isStepExecutable;
    }

    public void setIsStepExecutable(boolean isStepExecutable) {
        this.isStepExecutable = isStepExecutable;
    }
}
