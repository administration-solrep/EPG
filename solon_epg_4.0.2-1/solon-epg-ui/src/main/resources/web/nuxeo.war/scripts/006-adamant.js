function saveParamAdamant() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/adamant/sauvegarde";
    var myRequest = {
        contentId: null,
        dataToSend: $("#param-adamant-form").serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function resetParamAdamant() {
    var isFormModified = !checkParamAdamantForm();
    if (isFormModified) {
        var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/adamant/reinitialiser";
        var myRequest = {
            contentId: "param-adamant-body",
            dataToSend: null,
            method: "GET",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndShowToast, displaySimpleErrorMessage);
    }
}

const paramAdamant = [
    "numEntreeSolon",
    "originatingAgencyId",
    "submissionAgencyId",
    "archivalProfile",
    "archivalProfileSpecific",
    "originatingAgencyId2",
    "submissionAgencyId2",
    "delaiEligibilite",
    "nbDossier",
];

function checkParamAdamantForm() {
    const modale = $("#modale-sauvegarde-param-adamant");
    const textOk = checkTextInputModaleSauvegarde(paramAdamant, modale);
    const autoOk = checkAutocompleteInputModaleSauvegarde(["vecteurPublication", "typeActe"], modale);
    return textOk && autoOk;
}

function initControlParamAdamant() {
    const modale = $("#modale-sauvegarde-param-adamant");
    insertTextInputModaleSauvegarde(paramAdamant, modale);
    insertAutocompleteInputModaleSauvegarde(["vecteurPublication", "typeActe"], modale);
}

function demandeExtraction() {
    if (isValidForm($("#archivage-adamant-form"))) {
        var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/adamant/demande";
        var myRequest = {
            contentId: "message-validation-dialog-modal",
            dataToSend: $("#archivage-adamant-form").serialize(),
            method: "GET",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
            loadingButton: $(".ACTION_DEMANDE_EXTRACTION"),
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndShowValidationDialog, displaySimpleErrorMessage);
    }
}

var replaceHtmlFunctionAndShowValidationDialog = function replaceHtmlFunctionAndShowValidationDialog(
    containerID,
    result
) {
    replaceHtmlFunction(containerID, result);
    doInitValidationModal();
};

function doInitValidationModal() {
    var idModal = "validation-dialog-modal";
    var title = $("#dialogTitle-" + idModal);
    var btnconfirm = $("#btn-confirm-" + idModal);

    title.text("Confirmation");
    btnconfirm.attr("onclick", "extraireDocument()");

    $("#" + idModal).addClass("interstitial-overlay__content--visible");
}

function extraireDocument() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/adamant/extraire";
    var myRequest = {
        contentId: null,
        dataToSend: $("#archivage-adamant-form").serialize(),
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_DEMANDE_EXTRACTION"),
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}
