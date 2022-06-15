function doSupprimerSquelette() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/fdr/squelette/supprimer";
    const myRequest = buildRequest(ajaxUrl, $("#idSquelette").val());

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doUnlockSquelette() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/fdr/squelette/unlock";
    const myRequest = buildRequest(ajaxUrl, $("#idSquelette").val());

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doRetourListSquelettes() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/fdr/squelette/retourList";
    const myRequest = buildRequest(ajaxUrl, $("#idSquelette").val());

    callAjaxRequest(myRequest, goPreviousOrReloadIfError, displaySimpleErrorMessage);
}

function doValiderSquelette() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/fdr/squelette/valider";
    let myRequest;

    if ($("#editSqueletteFDR").length) {
        myRequest = {
            contentId: null,
            dataToSend: $("#editSqueletteFDR").serialize(),
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };
    } else {
        myRequest = buildRequest(ajaxUrl, $("#idSquelette").val());
    }

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doModifierSquelette() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/fdr/squelette/modifier";
    const myRequest = buildRequest(ajaxUrl, $("#idSquelette").val());

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doSaveEditEtapeSquelette() {
    var modal = $("#editEtape");
    if (!isValidForm(modal)) return false;

    var ajaxUrl = $("#ajaxCallPath").val() + "/etape/editerSquelette";
    var myRequest = {
        contentId: $("#stepId").val(),
        dataToSend: {
            dossierLinkId: $("#dossierLinkId").val(),
            typeEtape: $("#typeEtape-editStep").val(),
            typeDestinataire: $("#typeDestinataire-editStep").val(),
            destinataire: $("#destinataire-editStep option[selected]").val(),
            echeance: $("#echeance-editStep").val(),
            valAuto: $("#form_select_stage_val_auto_serie_editStep").is(":checked"),
            obligatoire: $("input:radio[name ='obligatoire-editStep']:checked").val(),
            totalNbLevel: $("#totalNbLevel").val(),
            stepId: $("#stepId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        extraDatas: {
            idContentModal: "contentEditEtapeFdr",
            idModal: "editEtape",
        },
    };

    callAjaxRequest(myRequest, resultEditStep, modalErrorMessage);
}

function enableDestinataire(select) {
    const destinataireBloc = $(select).closest(".grid__col").next();
    const enable = select.value === "ORGANIGRAMME";

    toggleOrganigrammeSelectAutocomplete(destinataireBloc, enable);
}
