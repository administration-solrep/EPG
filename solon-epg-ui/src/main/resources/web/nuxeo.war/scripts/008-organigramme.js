function loadSelectOrganigrammeWithFiltre(id, type, activatePosteFilter, selectedNode, isMulti, idTypeEtape) {
    var orgaID = "orga-" + id;

    var ajaxUrl = $("#ajaxCallPath").val() + "/organigramme/selectarbre/filtrePoste";
    var myRequest = {
        contentId: orgaID,
        dataToSend:
            buildTypesSelectionQueryParam(type) +
            type +
            "&selectID=" +
            id +
            "&activatePosteFilter=" +
            activatePosteFilter +
            "&selectedNode=" +
            selectedNode +
            "&dtoAttribute=" +
            selectedNode +
            "&isMulti=" +
            isMulti +
            "&filterCE=" +
            true +
            "&typeEtape=" +
            $("#" + idTypeEtape).val(),
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "overlay-" + id,
        caller: this,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction);
}

function importGouvernement() {
    event.preventDefault();
    var form = $("#modalImportGourvenementForm");
    const fileInput = $('input[name="gouvernementFile"]');
    if (fileInput.val()) {
        var data = new FormData(form[0]);
        var ajaxUrl = $("#ajaxCallPath").val() + "/organigramme/import/file";
        var myRequest = {
            contentId: null,
            enctype: "multipart/form-data",
            dataToSend: data,
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "reload-loader",
        };
        callAjaxRequest(myRequest, checkErrorOrReload, tabLoadError);
    } else {
        constructAlert(errorMessageType, ["Un fichier est requis"], "modalImportGourvenementForm");
    }
}

function doImporterGouvernementEpp() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/organigramme/importer/epp";
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, goPreviousOrReloadIfError, displaySimpleErrorMessage);
}

function appliquerNouveauGouvernementEpg() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/organigramme/appliquer/gouvernement";
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrGoToImportGouvernement, displaySimpleErrorMessage);
}

function appliquerNouveauGouvernementEpp() {
    showReloadLoader();
    window.location.href = $("#basePath").val() + "admin/organigramme/gouvernement/import/epp";
}

var checkErrorOrGoToImportGouvernement = function (contentId, result) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        window.location.href = $("#basePath").val() + "admin/organigramme/gouvernement/import/epg";
    }
};

function exportInjectionNewGouv() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/organigramme/import/exporter";
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#INJECTION_GOUV_EXPORT"),
    };
    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}
