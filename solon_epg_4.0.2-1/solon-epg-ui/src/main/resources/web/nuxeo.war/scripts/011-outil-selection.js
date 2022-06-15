function doEmptySelectionTool() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/vider";
    let myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_EMPTY_SELECTION_TOOL"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function doValidateStepSelectionTool() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/valider/etape";
    let myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_VALIDATE_STEP_SELECTION_TOOL"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function addSelectedDossierToSelectionTool() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/ajouter/dossiers";
    const myRequest = {
        contentId: "selection-tool",
        dataToSend: {
            ids: getSelectedContent(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function addSelectedFdrToSelectionTool() {
    var myTable = $(".tableForm").first();
    var fdrs = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            fdrs.push($(this).closest("tr").attr("data-id"));
        });
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/ajouter/fdr";
    const myRequest = {
        contentId: "selection-tool",
        dataToSend: {
            ids: fdrs,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

var replaceHtmlFunctionFromResponseAndDisplayToast = function replaceHtmlFunctionFromResponseAndDisplayToast(
    containerID,
    result
) {
    let jsonResponse = JSON.parse(result);
    let $target = $("#" + containerID);

    $target.html(jsonResponse.data.template);
    $target.find("[id^=selection-tool_link-copy__link_").first().focus();

    const messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.successMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.successMessageQueue);
        toggleAddRemoveButton(jsonResponse.data.updatedIds, true);
    } else if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    }
};

function doDeleteElement(id) {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/retirer";
    let myRequest = {
        contentId: "selection-tool",
        dataToSend: {
            id: id,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        extraDatas: {
            id: id,
            isSelected: true,
        },
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndRefreshLink, displaySimpleErrorMessage);
}

function doCreateListEpreuve() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/create/list/epreuve";
    var myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_CREATE_LIST_DEMANDE_EPREUVE"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function doCreateListPublication() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/create/list/publication";
    var myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_CREATE_LIST_DEMANDE_PUBLICATION"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function doAddDossiersSuivis() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/add/dossiers/suivis";
    var myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ADD_CORBEILLE_DOSSIER_SUIVI"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function doAbandonSelection() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/abandon";
    var myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_ABANDON_SELECTION_TOOL"),
    };

    callAjaxRequest(
        myRequest,
        replaceHtmlFunctionFromResponseRefreshCorbeilleAndDisplayToast,
        displaySimpleErrorMessage
    );
}

function doCreateListSignature() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/create/list/signature";
    var myRequest = {
        contentId: "selection-tool",
        dataToSend: "",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_CREATE_LIST_MISE_EN_SIGNATURE"),
    };

    callAjaxRequest(myRequest, callbackListSignature, displaySimpleErrorMessage);
}

function callbackListSignature(containerID, result, caller, extraDatas) {
    const jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    replaceHtmlFunctionFromResponseAndDisplayToast(containerID, result);
    if (messagesContaineur.dangerMessageQueue.length == 0) {
        toggleAddRemoveButton(jsonResponse.data.updatedIds, true);
        loadContentModalListeSignature(jsonResponse.data.idListe, jsonResponse.data.messageError);
        const modal = $("#modal-success-create-list-signature");
        openModal(modal[0]);
    }
}

function loadContentModalListeSignature(idListe, messageError) {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/create/list/signature/load/content";
    var myRequest = {
        contentId: "modal-success-create-list-signature-content",
        dataToSend: {
            idListe: idListe,
            messageError: messageError,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function doDeleteFromListSignature() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/list/signature/supprimer";

    var myTable = $("#tableListeSignatureModal");
    var dossierIds = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            dossierIds.push($(this).closest("tr").data("id"));
        });

    var myRequest = {
        contentId: "modal-success-create-list-signature-content",
        dataToSend: {
            ids: dossierIds,
            idListe: $("#idListeTraitementListe").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndSetupModal, displaySimpleErrorMessage);
}

function doExportListeSignatureModal() {
    let downloadUrl =
        $("#ajaxCallPath").val() + "/selection/list/signature/export/" + $("#idListeTraitementListe").val();
    window.open(downloadUrl, "_blank");
}

function checkReattributionDossiers(event) {
    if (!isValidForm($("#reattribution-modal-form"))) {
        event.stopImmediatePropagation();
        return;
    }
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/verifier/reattribution";
    const myRequest = {
        contentId: "reattribution-nor-summary",
        dataToSend: {
            ministere: $("#ministere-cible-reattribution").val(),
            direction: $("#direction-cible-reattribution").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, openSummaryModal, displaySimpleErrorMessage);
}

var openSummaryModal = function openSummaryModal(containerID, result) {
    let $target = $("#" + containerID);
    $target.html(result);
    openModal($target.find(".interstitial-overlay__content")[0]);
};

function doReattributionDossiers() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/reattribuer";
    const myRequest = {
        contentId: null,
        dataToSend: {
            ministere: $("#ministere-reattribution").val(),
            direction: $("#direction-reattribution").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function closeReattributionModal(event) {
    event.stopImmediatePropagation();
    let modal = $(event.target).closest(".interstitial-overlay__content")[0];
    closeModal(modal);
    $(".ACTION_REATTRIBUTION_NOR_SELECTION")[0].focus();
}

function openModalSubstitutionMass() {
    let data = [];
    $("#selection-tool")
        .find("div.folder-selected")
        .each(function () {
            data.push($(this).attr("data-id"));
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/massAction/substitutionMass/modal";
    const myRequest = {
        contentId: "substitution-mass-summary-content",
        dataToSend: {
            idDossiers: data,
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndSetupModal, displaySimpleErrorMessage);
}

function openModalSuppressionDossiersSelection() {
    let data = [];
    $("#selection-tool")
        .find("div.folder-selected")
        .each(function () {
            data.push($(this).attr("data-id"));
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/massAction/suppression";
    const myRequest = {
        contentId: "suppression-dossiers-summary-content",
        dataToSend: {
            idDossiers: data,
            deleteUrlAjax: "/selection/supprimerMasse",
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndSetupModal, displaySimpleErrorMessage);
}

function doSubstitutionPoste() {
    if (!isValidForm($("#modal-substitution-poste-form"))) {
        event.stopImmediatePropagation();
        return;
    }
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/substituer/postes";
    const myRequest = {
        contentId: "selection-tool",
        dataToSend: $("#modal-substitution-poste-form").serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-valider-substitution-poste"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionFromResponseAndDisplayToast, displaySimpleErrorMessage);
}

function checkTransfertDossiers() {
    let $form = $("#transfert-modal-form");
    if (!isValidForm($form)) {
        event.stopImmediatePropagation();
        return;
    }
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/verifier/transfert";
    const myRequest = {
        contentId: "transfert-dossier-summary",
        dataToSend: $form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, openSummaryModal, displaySimpleErrorMessage);
}

function doTransfertDossiers() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/transferer";
    const myRequest = {
        contentId: null,
        dataToSend: {
            ministere: $("#ministere-transfert").val(),
            direction: $("#direction-transfert").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function closeTransfertModal(event) {
    event.stopImmediatePropagation();
    let modal = $(event.target).closest(".interstitial-overlay__content")[0];
    closeModal(modal);
    $(".ACTION_TRANSFERT_SELECTION")[0].focus();
}

var replaceHtmlFunctionFromResponseRefreshCorbeilleAndDisplayToast = function replaceHtmlFunctionFromResponseRefreshCorbeilleAndDisplayToast(
    containerID,
    result
) {
    replaceHtmlFunctionFromResponseAndDisplayToast(containerID, result);
    reloadCorbeille();
};
