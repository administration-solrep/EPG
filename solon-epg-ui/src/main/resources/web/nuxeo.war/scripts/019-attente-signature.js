function chargeOngletAttenteSignature(me, type) {
    var targetURL = "travail/attenteSignature/" + type;
    var myRequest = {
        contentId: $(me).attr("aria-controls"),
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/" + targetURL,
        isChangeURL: false,
        overlay: null,
        caller: me,
    };
    $("#currentTab").val(type);
    history.pushState(null, "", $("#basePath").val() + targetURL + "#main_content");

    callAjaxRequest(myRequest, replaceHtmlFunction, tabLoadError);
}

function chargerTexteAttenteSignature(itemId) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/travail/attenteSignature/charger/" + itemId;

    var modale = $("#attenteSignatureModaleEdit");

    var myRequest = {
        contentId: modale.find(".interstitial__content > div").first().attr("id"),
        dataToSend: { currentTab: $("#currentTab").val() },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function editerTexteAttenteSignature() {
    var tab = $("#currentTab").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/travail/attenteSignature/" + tab + "/editer";
    var form = $("#content-attenteSignatureModaleEdit").find("form");

    var myRequest = {
        contentId: "attente_signature_" + tab + "_content",
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndConstructAlert, displaySimpleErrorMessage);
}

function retirerDossiersAttenteSignature() {
    var tab = $("#currentTab").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/travail/attenteSignature/" + tab + "/retirer";
    var myRequest = {
        contentId: "attente_signature_" + tab + "_content",
        dataToSend: {
            ids: getSelectedContent(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndConstructAlert, displaySimpleErrorMessage);
}

var replaceHtmlFunctionAndConstructAlert = function replaceHtmlFunctionAndConstructAlert(containerID, result) {
    let jsonResponse = JSON.parse(result);
    let $target = $("#" + containerID);

    $target.html(jsonResponse.data.template);

    const messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.successMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.successMessageQueue);
    } else if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    }
};

function exportListeAttenteSignature(type) {
    var tab = $("#currentTab").val();
    let downloadUrl =
        $("#ajaxCallPath")
            .val()
            .substring(0, $("#ajaxCallPath").val().length - 5) +
        "/travail/attenteSignature/telecharger/" +
        type +
        "?tab=" +
        tab;
    window.open(downloadUrl, "_blank");
}

function addDossierAttenteSignature(data) {
    const ajaxUrl = $("#ajaxCallPath").val() + "/travail/attenteSignature/ajouter";
    var myRequest = {
        contentId: null,
        dataToSend: data,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, showModalAjoutAttenteSignatureOrErrorOrReload, displaySimpleErrorMessage);
}

function addDossierAttenteSignatureWithoutType() {
    addDossierAttenteSignature({ dossierId: $("#dossierId").val() });
}

function addDossierAttenteSignatureWithType() {
    addDossierAttenteSignature({
        dossierId: $("#dossierId").val(),
        type: $("#form-select-type-attente-signature").val(),
    });
}

function showModalAjoutAttenteSignatureOrErrorOrReload(containerID, result, caller, extraDatas) {
    const jsonResponse = JSON.parse(result);
    const messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else if (messagesContaineur.successMessageQueue.length > 0) {
        reloadPage();
    } else {
        doInitModal("#init-modal-ajout-attente-signature");
        const idModal = $("#init-modal-ajout-attente-signature").data("controls");
        const modal = $("#" + idModal);
        modal.addClass("interstitial-overlay__content--visible");
        modal.find("select")[0].focus();
    }
}
