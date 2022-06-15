function doAddDocumentParapheur() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/ajout/fichier/parapheur";
    var groupId = DOMPurify.sanitize($("#groupId").val());

    var filesCtl = getFilesFromFileInput("documentFileAdd");

    var formData = new FormData();

    formData.append("dossierId", DOMPurify.sanitize($("#dossierId").val()));
    formData.append("groupId", groupId);

    filesCtl.forEach(function (inp) {
        formData.append("documentFile", inp);
    });

    var myRequest = {
        contentId: null,
        dataToSend: formData,
        processData: false,
        contentType: false,
        enctype: "multipart/form-data",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    closeModal($("#modal-add-document").get(0));
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function initAddDocModale(acceptedFileTypes) {
    if (acceptedFileTypes !== undefined && acceptedFileTypes.length > 0) {
        $("#fileTypes").text(acceptedFileTypes.replace("[", "").replace("]", "").concat("."));
    } else {
        $("#fileTypes").text("Tous");
    }
}

function initHistoryDocModale(idDoc) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/historique/fichier/contenu";
    var myRequest = {
        contentId: "modalHistoryVersionDocContent",
        dataToSend: { idDocument: idDoc },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function addFddFolder(btn) {
    addFddFolderCommon("/dossier/ajout/repertoire/fdd", $(btn).closest("tr").data("id"), $("#dossierId").val());
}

function addFddFolderCommon(url, groupId, dossierId) {
    var ajaxUrl = $("#ajaxCallPath").val() + url;

    var myRequest = {
        contentId: null,
        dataToSend: {
            groupId: groupId,
            dossierId: dossierId,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function initEditFolderModale(name) {
    $("#newFolderName").val(name);
}

function editFddFolder(btn) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/edition/repertoire/fdd";

    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            groupId: $("#folderId").val(),
            dossierNom: $("#newFolderName").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-edition"),
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function initDeleteFolderModale(name) {
    $("#message").text($("#message").text().replace("{0}", name));
}

function deleteFddFolder() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/suppression/repertoire/fdd";

    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            groupId: $("#folderIdToDelete").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-delete"),
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function initRenameModale(params) {
    const arrParams = params.split(";");
    const name = arrParams[0];
    const extension = arrParams[1];

    $("#documentExtension").text(extension);
    $("#newDocumentName").val(name);
}

function doRenameDocumentFondDeDossier() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/renommer/fichier";

    var formData = new FormData();
    formData.append("documentId", DOMPurify.sanitize($("#documentId").val()));
    formData.append("documentName", DOMPurify.sanitize($("#newDocumentName").val()));

    var myRequest = {
        contentId: null,
        dataToSend: formData,
        processData: false,
        contentType: false,
        enctype: "multipart/form-data",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-rename-doc"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doDeleteDocumentFondDeDossier(deleteAllVersions) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/suppression/fichier/fdd";
    var myRequest = {
        contentId: null,
        dataToSend: {
            documentIdToDelete: $("#documentIdToDelete").val(),
            dossierId: $("#dossierId").val(),
            deleteAllVersions: deleteAllVersions,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-deletion-doc"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doDeleteDocumentParapheur(deleteAllVersions) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/suppression/fichier/parapheur";
    var myRequest = {
        contentId: null,
        dataToSend: {
            documentIdToDelete: $("#documentIdToDelete").val(),
            dossierId: $("#dossierId").val(),
            deleteAllVersions: deleteAllVersions,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-deletion-doc"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doNewVersionDocument() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/modification/fichier";
    if (validInputFile("documentFile", "documentFile")) {
        var formData = new FormData();
        formData.append("groupId", DOMPurify.sanitize($("#folderId").val()));
        formData.append("documentId", DOMPurify.sanitize($("#documentId").val()));
        formData.append("origineSource", DOMPurify.sanitize($("#origineSource").val()));
        formData.append("documentFile", getFilesFromFileInput("documentFile")[0]);
        var myRequest = {
            contentId: null,
            dataToSend: formData,
            processData: false,
            contentType: false,
            enctype: "multipart/form-data",
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
            loadingButton: $("#btn-confirm-edit-doc"),
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}

function openEdit(event, id, nom) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/token";

    const myRequest = {
        contentId: null,
        dataToSend: {},
        method: "GET",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        caller: event.target,
        extraDatas: {
            idFichier: id,
            nomFichier: nom,
        },
        async: false,
    };

    callAjaxRequest(myRequest, constructFolderEditLinkAndOpen, displayEditLinkError);
}

window.createEditLinkFolderSuccess = function (link, caller) {
    caller.href = link;
    caller.removeAttribute("onclick");
};

window.constructFolderEditLinkAndOpen = function (elementId, result, caller, extraDatas, myXhr) {
    const token = result;
    const id = extraDatas.idFichier;
    const filename = extraDatas.nomFichier;
    if (id != null && token != null) {
        const specificArgs = [
            { name: "id", value: id },
            { name: "filename", value: filename },
        ];
        updateLinkEdition(token, "/dossier", "/edition", specificArgs, createEditLinkFolderSuccess, caller);
    } else {
        displayEditLinkError();
    }
};

function doValiderEtape() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/etape";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_VALIDER_ETAPE_DOSSIER"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function checkChargeMissionBeforePubDila() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/etape/check/publication";
    var myRequest = {
        contentId: "message-validation-dialog-modal",
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionIfChargeMissionPubDilaKo, displaySimpleErrorMessage);
}

var replaceHtmlFunctionIfChargeMissionPubDilaKo = function replaceHtmlFunctionIfChargeMissionPubDilaKo(
    containerID,
    result
) {
    let jsonResponse = JSON.parse(result);
    let $target = $("#" + containerID);

    if (jsonResponse.data.isChargeMissionConseillePMValide == false) {
        $target.html(jsonResponse.data.template);
        $target.find("[id^=selection-tool_link-copy__link_").first().focus();
    }
};

function deleteApplication(id) {
    $("#" + id).remove();
}

function addTranspositionDirective() {
    let $directives = $(".directive-fieldset");
    let $index = 1;
    let $idToReplace = "add-directive";
    let $div = $(".add-directive");
    if ($directives.length > 0) {
        $index = $directives.length + 1;
        $idToReplace = $directives.last().attr("id");
    }

    $div.find(".alert").remove();

    if (isValidForm($div)) {
        const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/add/transposition/directive";
        let myRequest = {
            contentId: $idToReplace,
            dataToSend: {
                index: $index,
                annee: $("#annee-directive_ajout").val(),
                reference: $("#reference-directive_ajout").val(),
                titre: $("#titre-directive_ajout").val(),
                numeroArticle: $("#numeroArticle-directive_ajout").val(),
                commentaire: $("#commentaire-directive_ajout").val(),
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
            loadingButton: $("#transposition-directive-add-button"),
            caller: this,
        };
        callAjaxRequest(myRequest, checkErrorOrAppendHtmlFunction, displaySimpleErrorMessage);
    }
}

function addApplicationLoiOrdonnance() {
    let $loiOrdonnances = $(".loi-ordonnance-fieldset");
    let $index = 1;
    let $idToReplace = "add-loi-ordonnance";
    if ($loiOrdonnances.length > 0) {
        $index = $loiOrdonnances.length + 1;
        $idToReplace = $loiOrdonnances.last().attr("id");
    }
    if (isValidForm($(".add-loi-ordonnance"))) {
        const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/add/transposition/loi-ordonnance";
        let myRequest = {
            contentId: $idToReplace,
            dataToSend: {
                index: $index,
                reference: $("#reference-loi-ordonnance_ajout").val(),
                titre: $("#titre-loi-ordonnance_ajout").val(),
                numeroOrdre: $("#numeroOrdre-loi-ordonnance_ajout").val(),
                numeroArticle: $("#numeroArticle-loi-ordonnance_ajout").val(),
                commentaire: $("#commentaire-loi-ordonnance_ajout").val(),
                type: $("input[name='type_ajout']:checked").val(),
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
            loadingButton: $("#transposition-loi-ordonnance-add-button"),
            caller: this,
        };
        callAjaxRequest(myRequest, appendHtmlFunction, displaySimpleErrorMessage);
    }
}

function isNotValidFormBordereau(onglet) {
    var form = $("#dossierFormBordereau");
    return onglet === "bordereau" && !isValidForm(form.find("fieldset"));
}

function saveDossier(
    callback = checkErrorOrHardReload,
    selectedTab = $(".tabulation__item--active[name=tab-up]").attr("data-name"),
    loadingButton = $("#SAUVEGARDER_DOSSIER")
) {
    if (isNotValidFormBordereau(selectedTab) || isSousOngletBordereau(selectedTab)) {
        return;
    }
    var tabCapitalize = selectedTab.charAt(0).toUpperCase() + selectedTab.slice(1);
    var $data =
        $("form#dossierForm" + tabCapitalize).serialize() +
        "&" +
        $("#dossierId").serialize() +
        "&" +
        $("#dossierLinkId").serialize();
    var tabFunction = window["save" + tabCapitalize];
    if (typeof tabFunction !== "undefined" && tabFunction !== null) {
        $data = tabFunction($data);
    }
    if (selectedTab === "fiche") {
        return;
    }
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/save/" + selectedTab;
    if (selectedTab === "traitement") {
        // on ajoute le sous-onglet
        var selectedSubtab = "/papier/" + $(".tabulation__item--active[name=subtab]").attr("data-name");
        ajaxUrl += selectedSubtab;
    }
    var myRequest = {
        contentId: null,
        dataToSend: $data,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, callback, displaySimpleErrorMessage);
}

function doValiderRetourModification() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/etape";
    var dataToSend;
    if ($("#dossierLinkId").val() != null) {
        dataToSend = {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        };
    } else {
        dataToSend = {
            dossierId: $("#dossierId").val(),
        };
    }
    var myRequest = {
        contentId: null,
        dataToSend: dataToSend,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_DOSSIER_VALIDER_RETOUR_MODIFICATION_DOSSIER"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function saveBordereau() {
    const bordereauFields = $("#dossierLinkId, #dossierId, .bordereauFieldset").serializeArray();
    let bordereauForm = serializedArrayToObject(bordereauFields);
    let transpositionDirectives = [];
    let applicationLoiOrdonnances = [];
    $("fieldset.directive-item").each(function () {
        const directives = $(this).serializeArray();
        let directiveForm = serializedArrayToObject(directives);

        transpositionDirectives.push(JSON.stringify(directiveForm));
    });

    $("fieldset.loi-ordonnance-item").each(function () {
        let loiOrdonnances = $(this).serializeArray();
        loiOrdonnances.forEach(function (item) {
            if (item["name"].startsWith("type_")) {
                item["name"] = "type";
            }
        });
        let loiOrdonnanceForm = serializedArrayToObject(loiOrdonnances);

        applicationLoiOrdonnances.push(JSON.stringify(loiOrdonnanceForm));
    });

    return $.extend(
        bordereauForm,
        { transpositionDirectives: transpositionDirectives },
        { applicationLoiOrdonnances: applicationLoiOrdonnances }
    );
}

function serializedArrayToObject(serializedObject) {
    let result = {};
    for (let s in serializedObject) {
        const name = serializedObject[s]["name"];
        const value = serializedObject[s]["value"];
        if (result[name] != undefined) {
            result[name].push(value);
        } else if (name !== undefined && name.indexOf("[]") > -1) {
            result[name] = new Array(value);
        } else {
            result[name] = value;
        }
    }
    return result;
}

window.checkErrorOrAppendHtmlFunction = function (contentId, result, caller, extraDatas, xhr) {
    var jsonResponse = JSON.parse(result);
    var messagesContainer = jsonResponse.messages;
    if (messagesContainer.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContainer.dangerMessageQueue);
        if (messagesContainer.infoMessageQueue.length > 0) {
            constructAlertContainer(messagesContainer.infoMessageQueue);
        }
    } else {
        appendHtmlFunction(contentId, jsonResponse.data, caller, extraDatas, xhr);
    }
};

function terminerSansPublication() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/terminer/sans/publication";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doValiderNorAttribuer() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/nor/attribuer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doValiderAvecCorrectionAvis() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/etape/correction";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_VALIDER_CORRECTION_AVIS"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doValiderAvecCorrectionBonATirer() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/etape/correction";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_VALIDER_CORRECTION_BON_A_TIRER"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function validerNonConcerne(addStep) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/non/concerne";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            addStep: addStep,
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_VALIDER_NON_CONCERNE"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doRetourModification(addStep) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/retour/modification";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
            idPosteDan: $("input:radio[name ='poste-retour-dan-input']:checked").val(),
            addStep: addStep,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_RETOUR_POUR_MODIFICATION"),
    };

    callAjaxRequest(myRequest, showErrorModalRetourModifOrGoPrevious, displaySimpleErrorMessage);
}

function initRetourDanModal() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/init/retour/dan";
    var myRequest = {
        contentId: "modalRetourModificationRetourDanContent",
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function doRefusValidationAvis() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/refus";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_VALIDER_REFUS_AVIS"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doRefusValidationRetourModif() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/refus";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_VALIDER_REFUS_RETOUR_MODIF"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doRefusValidation() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/refus";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_VALIDER_REFUS"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doInitialisation() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/valider/etape/initialisation";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_LANCER_DOSSIER"),
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function openModalSuppressionDossierCreation() {
    const myTable = $("[id^=SUPPRIMER_DOSSIER_CREATION]").closest(".tableForm");
    let data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/massAction/suppression";
    const myRequest = {
        contentId: "suppression-dossiers-summary-content",
        dataToSend: {
            idDossiers: data,
            deleteUrlAjax: "/dossier/massAction/supprimer",
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndSetupModal, displaySimpleErrorMessage);
}

function doRemoveFromDossiersSuivis() {
    const myTable = $("#REMOVE_DOSSIER_SUIVI").closest(".tableForm");
    let data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/travail/dossiersSuivis/remove";
    const myRequest = {
        contentId: null,
        dataToSend: {
            idDossiers: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function doEmptyDossiersSuivis() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/travail/dossiersSuivis/empty";
    const myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function replaceHtmlFunctionAndSetupModal(containerID, result) {
    replaceHtmlFunction(containerID, result);
    $("#" + containerID)
        .parent()
        .removeClass("initialized");
    if ($("#suppression-dossiers-modal").length) {
        $("#" + containerID)
            .parent()
            .addClass("interstitial-overlay__content--chained");
    } else {
        $("#" + containerID)
            .parent()
            .removeClass("interstitial-overlay__content--chained");
    }
    initInterstitial();
}

function doDeleteDossiers(event, button, isSummary) {
    if (isSummary && $(button).text() === "Suivant") return;
    var modal = $("#modal-suppression-dossiers");
    if (!isValidForm(modal)) {
        event.stopImmediatePropagation();
        return;
    }

    const ajaxUrl = $("#ajaxCallPath").val() + $("#urlDeleteAjax").val();
    const myRequest = {
        contentId: null,
        dataToSend: {
            username: $("#form_login_username").val(),
            data: $("#form_login_password").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function removeUrgenceDossier() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/supprimer/urgence";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function addUrgenceDossier() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/ajouter/urgence";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function showErrorModalRetourModifOrGoPrevious(containerID, result, caller, extraDatas) {
    const jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        const modal = $("#modal-error-retour-modification");
        modal.addClass("interstitial-overlay__content--visible");
        modal.find("button")[0].focus();
    } else {
        goPreviousPage();
    }
}

$(document).ready(function () {
    initAutoCompleteTypeActe();
    if ($("#sidebar__content").find(".quick-access__link--active").length !== 0) {
        $("#sidebar__content")
            .find(".tree-navigation__item--active")
            .closest("li")
            .removeClass("tree-navigation__item--active");
    }
});

function initAutoCompleteTypeActe() {
    var oldTypeActe = "";
    // change event on input
    $("#typeActe")
        .next("div")
        .find("input")
        .on("blur", function () {
            var typeActe = $("#typeActe").val();
            if (typeActe !== oldTypeActe) {
                updateFormFromTypeActe(typeActe);
                oldTypeActe = typeActe;
            }
        });
}

function showTypeActeContent(typeActe) {
    if (typeActe === "Rectificatif") {
        if ($("#rectificatif").hasClass("hide-element")) {
            $("#rectificatif").removeClass("hide-element");
        }
        if (!$("#type-acte-content").hasClass("hide-element")) {
            $("#type-acte-content").addClass("hide-element");
        }
        $("#btn-create-dossier").text("Consulter");
        // add alert info "Veuillez consulter le SGG avant de créer un rectificatif"
        constructAlert(warningMessageType, ["Veuillez consulter le SGG avant de créer un rectificatif"]);
    } else {
        if (!$("#rectificatif").hasClass("hide-element")) {
            $("#rectificatif").addClass("hide-element");
        }
        if ($("#type-acte-content").hasClass("hide-element")) {
            $("#type-acte-content").removeClass("hide-element");
        }
        $("#btn-create-dossier").text("Créer le dossier");
        // clean alerts
        cleanAlerts();
    }
}

function updateFormFromTypeActe(typeActe) {
    // show content div
    showTypeActeContent(typeActe);

    if (typeActe != null) {
        $("#btn-create-dossier").attr("disabled", false);
    } else {
        $("#btn-create-dossier").attr("disabled", true);
    }

    if (typeActe == null || !isSpecificTypeActe(typeActe)) {
        $("#dynamic-type-acte-content").empty();
    } else {
        var ajaxUrl = $("#ajaxCallPath").val() + "/travail/appliquer";
        var myRequest = {
            contentId: "dynamic-type-acte-content",
            dataToSend: {
                typeActe: typeActe,
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndInitFormValidation, displaySimpleErrorMessage);
    }
}

var replaceHtmlFunctionAndInitFormValidation = function replaceHtmlFunctionAndInitFormValidation(containerID, result) {
    replaceHtmlFunction(containerID, result);
    initFormValidation();
};

function enableMinistereEtDirection(enableMinDir = true) {
    // Ministère et Direction
    // (des)activer bouton d'ouverture modale
    $("#min-dir").find("fieldset").find(".interstitial-overlay__trigger").prop("disabled", !enableMinDir);
    // (des)activer input
    $("#min-dir").find("fieldset").find("input").prop("disabled", !enableMinDir);

    // NOR PRM
    // (des)activer select
    if (!enableMinDir && $("#norPrm").hasClass("form-select-input__field--disabled")) {
        $("#norPrm").removeClass("form-select-input__field--disabled");
    }
    $("#norPrm").prop("disabled", enableMinDir);
}

function isSpecificTypeActe(typeActe) {
    return (
        (typeActe.startsWith("Décret") && !typeActe.endsWith("(individuel)")) ||
        (typeActe.startsWith("Arrêté") && !typeActe.endsWith("(individuel)") && !typeActe.endsWith("Conseil d'Etat")) ||
        typeActe.startsWith("Ordonnance") ||
        typeActe.startsWith("Loi")
    );
}

function onChangeMesureTransposition() {
    if ($("input[name=mesureTransposition]:checked").val() === "true") {
        $("#mesure-transposition-europeenne").removeClass("hide-element");
    } else {
        $("#mesure-transposition-europeenne").addClass("hide-element");
    }
}

function onChangeMesureApplication() {
    if ($("input[name=mesureApplication]:checked").val() !== "false") {
        $("#mesure-application").removeClass("hide-element");
    } else {
        $("#mesure-application").addClass("hide-element");
    }
}

function onChangeArticle() {
    if ($("input[name=article]:checked").val() === "true") {
        $("#ordonnance").removeClass("hide-element");
    } else {
        $("#ordonnance").addClass("hide-element");
    }
}

function addDirectiveMesureApplicationLoi() {
    if (isValidForm($("#mesure-application-form"))) {
        var ajaxUrl = $("#ajaxCallPath").val() + "/travail/application";
        var myRequest = {
            contentId: "directives-mesure-application",
            dataToSend: $("#mesure-application-form").serialize(),
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, addDirectiveInContainer, displaySimpleErrorMessage);
    }
}

function addDirectiveMesureTransposition() {
    if (isValidForm($("#mesure-transposition-europeenne-form"))) {
        var ajaxUrl = $("#ajaxCallPath").val() + "/travail/transposition";
        var myRequest = {
            contentId: "directives-mesure-transposition",
            dataToSend: $("#mesure-transposition-europeenne-form").serialize(),
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, addDirectiveInContainer, displaySimpleErrorMessage);
    }
}

function addDirectiveInContainer(containerID, result) {
    const container = $("#" + containerID);
    container.append(result);
    $("#mesure-transposition-europeenne-form").trigger("reset");
}

function removeDirective(button) {
    $(button).parent().remove(); // remove parent div
}

function isValidPoste() {
    if ($("#rbt-poste").prop("checked") === true) {
        return isValidInput($("#poste"));
    } else if ($("#rbt-poste-defaut").prop("checked") === true) {
        return isValidInput($("#poste-defaut"));
    }
    return false;
}

function setPosteAndCreateFolder() {
    if (isValidPoste()) {
        var poste;
        if ($("#rbt-poste").prop("checked") === true) {
            poste = $("#poste").val();
        } else if ($("#rbt-poste-defaut").prop("checked") === true) {
            poste = $("#poste-defaut").val();
        }
        createFolder(poste);
    }
}

function isValidFolder() {
    var isValidTypeActe;
    if (getSelectedTypeActe() === "Rectificatif") {
        isValidTypeActe = isValidInput($("#typeActe")) && isValidInput($("#norRectificatif"));
    } else {
        isValidTypeActe = isValidInput($("#typeActe")) && isValidForm($("#type-acte-form"));
    }

    var isValidOrdonnance = true;
    if (isOrdonnance()) {
        isValidOrdonnance = isValidForm($("#ordonnance-form"));
    }

    return isValidTypeActe && isValidOrdonnance;
}

function createFolder(poste = "") {
    if (isValidFolder()) {
        var typeActe = getSelectedTypeActe();
        if (typeActe === "Rectificatif") {
            consulterDossier();
        } else {
            var entite = $("#rbt-ministere-direction").prop("checked") === true ? $("#entite").val() : "";
            var direction = $("#rbt-ministere-direction").prop("checked") === true ? $("#dirConcernee").val() : "";
            var norPrm = $("#rbt-nor").prop("checked") === true ? $("#norPrm").val() : "";
            var norCopie = $("#norCopie").val();
            var norRectificatif = $("#norRectificatif").val();
            // Données des autres formulaires
            var mesuresTransposition = getMesuresTransposition();
            var mesuresApplication = getMesuresApplication();
            var ordonnance = getOrdonnance();

            var ajaxUrl = $("#ajaxCallPath").val() + "/travail/creer";
            var myRequest = {
                contentId: null,
                dataToSend: {
                    poste: poste,
                    typeActe: typeActe,
                    entite: entite,
                    direction: direction,
                    norPrm: norPrm,
                    norCopie: norCopie,
                    norRectificatif: norRectificatif,
                    mesuresTransposition: mesuresTransposition,
                    mesuresApplication: mesuresApplication,
                    ordonnance: ordonnance,
                },
                method: "POST",
                dataType: "html",
                ajaxUrl: ajaxUrl,
                isChangeURL: false,
                loadingButton: poste === "" ? $("#btn-create-dossier") : null,
                overlay: poste === "" ? null : "reload-loader",
            };

            window.scrollTo(0, 0);
            callAjaxRequest(myRequest, checkErrorOrShowModalOrGoDossier, displaySimpleErrorMessage);
        }
    }
}

function createRectificatif(poste = "") {
    var ajaxUrl = $("#ajaxCallPath").val() + "/travail/creer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            poste: poste,
            typeActe: "Rectificatif",
            norRectificatif: $("#dossierNor").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        loadingButton: null,
        overlay: "reload-loader",
    };

    window.scrollTo(0, 0);
    callAjaxRequest(myRequest, checkErrorOrShowModalOrGoDossier, displaySimpleErrorMessage);
}

function setPosteAndCreateRectificatif() {
    if (isValidPoste()) {
        var poste;
        if ($("#rbt-poste").prop("checked") === true) {
            poste = $("#poste").val();
        } else if ($("#rbt-poste-defaut").prop("checked") === true) {
            poste = $("#poste-defaut").val();
        }
        createRectificatif(poste);
    }
}

function consulterDossier() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/travail/rectificatif";
    var myRequest = {
        contentId: null,
        dataToSend: {
            nor: $("#norRectificatif").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoToDossier, displaySimpleErrorMessage);
}

window.checkErrorOrGoToDossier = function (contentId, result, caller, extraDatas, xhr) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        var data = jsonResponse.data;
        var dataJson = JSON.parse(data);
        goToDossier(dataJson.idDossier);
    }
};

function checkErrorOrShowModalOrGoDossier(contentId, result, caller, extraDatas, xhr) {
    var jsonResponse = JSON.parse(result);
    var data = jsonResponse.data;
    var dataJson = JSON.parse(data);
    if (dataJson && dataJson.isValidPoste) {
        var messagesContaineur = jsonResponse.messages;
        if (messagesContaineur.dangerMessageQueue.length > 0) {
            constructAlertContainer(messagesContaineur.dangerMessageQueue);
        } else {
            goToDossier(dataJson.idDossier);
        }
    } else {
        // si data vaut false : afficher la modale de choix du poste
        if (dataJson.isRectificatif == true) {
            initModalChoixPosteRectificatif();
        } else {
            initModalChoixPoste();
        }

        var modal = $("#modal-choix-poste");
        openModal(modal[0]);
        modal.find("button")[0].focus();
    }
}

function initModalChoixPoste() {
    var btnconfirm = $("#btn-create-dossier-choix-poste");
    btnconfirm.attr("onclick", "setPosteAndCreateFolder()");
}

function initModalChoixPosteRectificatif() {
    var btnconfirm = $("#btn-create-dossier-choix-poste");
    btnconfirm.attr("onclick", "setPosteAndCreateRectificatif()");
}

function goToDossier(id) {
    showReloadLoader();
    window.location.href = $("#basePath").val() + "dossier/" + id + "/bordereau?dossierLinkId=#main_content";
}

function enablePoste(enable = true) {
    // (des)activer bouton d'ouverture modale
    $("#poste-div").find("fieldset").find(".interstitial-overlay__trigger").prop("disabled", !enable);
    // (des)activer input
    $("#poste-div").find("fieldset").find("input").prop("disabled", !enable);
    // (dés)activer le poste par défaut
    $("#poste-defaut").prop("disabled", enable);

    // clean messages
    cleanPreviousErrorOrSuccess($("#poste"));
    cleanPreviousErrorOrSuccess($("#poste-defaut"));
}

function getSelectedTypeActe() {
    return $("#typeActe").val();
}

function isMesureTransposition() {
    return isSpecificTypeActe(getSelectedTypeActe()) && $("#mesureTransposition-true").prop("checked") === true;
}

function getMesuresTransposition() {
    // mesures transposition d'une directive européenne
    var mesuresTransposition = [];

    if (isMesureTransposition()) {
        $("#directives-mesure-transposition")
            .find("form")
            .each(function () {
                var serializeObject = $(this).serializeArray();
                mesuresTransposition.push(convertToJson(serializeObject));
            });
    }

    return mesuresTransposition;
}

function isMesureApplication() {
    var typeActe = getSelectedTypeActe();
    return (
        typeActe.startsWith("Décret") &&
        !typeActe.endsWith("(individuel)") &&
        ($("#mesureApplication-loi").prop("checked") === true ||
            $("#mesureApplication-ordonnance").prop("checked") === true)
    );
}

function getMesuresApplication() {
    // mesures loi ou ordonnance
    var mesuresApplication = [];

    if (isMesureApplication()) {
        $("#directives-mesure-application")
            .find("form")
            .each(function () {
                var serializeObject = $(this).serializeArray();
                serializeObject.push({
                    name: "loi",
                    value: $("#mesureApplication-loi").prop("checked"),
                });
                serializeObject.push({
                    name: "ordonnance",
                    value: $("#mesureApplication-ordonnance").prop("checked"),
                });
                mesuresApplication.push(convertToJson(serializeObject));
            });
    }

    return mesuresApplication;
}

function isOrdonnance() {
    return getSelectedTypeActe().startsWith("Ordonnance") && $("input[name=article]:checked").val() === "true";
}

function getOrdonnance() {
    var ordonnance;
    if (isOrdonnance()) {
        ordonnance = convertToJson($("#ordonnance-form").serializeArray());
    }
    return ordonnance;
}

function convertToJson(serializeObject) {
    var objectToJson = "{";
    for (i = 0; i < serializeObject.length; i++) {
        var name = serializeObject[i]["name"].split("-")[0];
        var value = serializeObject[i]["value"];
        if (i != 0) {
            objectToJson += '",';
        }
        objectToJson += '"';
        objectToJson += name;
        objectToJson += '" : "';
        objectToJson += value;
    }
    objectToJson += '"}';

    return objectToJson;
}

function bordereauShowHiddenDelay(select) {
    const divContent = $("#datePreciseeContent");
    if (select.value == "1") {
        divContent.removeClass("invisible").addClass("visible");
    } else {
        divContent.removeClass("visible").addClass("invisible");
    }
}

function changeTypeActe() {
    doInitModal("#init-modal-changement-acte");
    const idModal = $("#init-modal-changement-acte").data("controls");
    const modal = $("#" + idModal);
    openModal(modal[0]);
    modal.find("button")[0].focus();
}

function closeChangeTypeActeModale(event) {
    event.stopImmediatePropagation();
    let modal = $(event.target).closest(".interstitial-overlay__content")[0];
    closeModal(modal);
    let $typeActeSelect = $("#typeActe");
    const $actualTypeActe = $("#actualTypeActe").val();
    $typeActeSelect.val($actualTypeActe);
    $typeActeSelect[0].focus();
}

function changePeriodiciteRapport() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/periodicite/show/modal";
    const myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
            periodicite: $("#periodiciteRapport").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, showModalSubstitutionPeriodiciteRapport, displaySimpleErrorMessage);
}

var showModalSubstitutionPeriodiciteRapport = function showModalSubstitutionPeriodiciteRapport(containerID, result) {
    let jsonResponse = JSON.parse(result);
    if (jsonResponse.data == true) {
        doInitModal("#init-modal-changement-periodicite");
        const idModal = $("#init-modal-changement-periodicite").data("controls");
        const modal = $("#" + idModal);
        openModal(modal[0]);
        modal.find("button")[0].focus();
    }
};

function closePeriodiciteRapportModale(event) {
    event.stopImmediatePropagation();
    let modal = $(event.target).closest(".interstitial-overlay__content")[0];
    closeModal(modal);
    $("#periodiciteRapport")[0].focus();
}

function loadModeleFdrSubstitutionRapportParlement() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/periodicite/load/modele";
    const myRequest = {
        contentId: "select-modele-fdr",
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function doSubstitutionFdrModalPeriodiciteRapport() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/substitution/valider";
    var $data =
        $("form#dossierFormBordereau").serialize() +
        "&dossierId=" +
        $("#dossierId").val() +
        "&idModele=" +
        $("#selectModeleFdr").val();

    var myRequest = {
        contentId: null,
        dataToSend: $data,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-modal-changement-periodicite"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doSubstitutionFromNor(btnCopier) {
    event.preventDefault();
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/" + $("#dossierId").val() + "/substitution/copie/fdr/nor";

    var myRequest = {
        contentId: null,
        dataToSend: {
            norToCopie: $(btnCopier).closest(".table-form").find("#fdr_nor_to_copy").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function annulerMesureNominative() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/remove/mesure/nominative";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_ANNULER_MESURE_NOMINATIVE"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function addSelectedDossierToFavoris() {
    var myTable = $("#listeDossiers");
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    var ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/dossiers/ajouter";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossiersIds: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function redonnerMesureNominative() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/add/mesure/nominative";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $(".ACTION_DOSSIER_REDONNER_MESURE_NOMINATIVE"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doFiltreFdrSubstitution() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/" + $("#dossierId").val() + "/substitution/liste";
    var myRequest = {
        contentId: "listeModeles",
        dataToSend: {
            filtreTypeActe: $("input:radio[name=filtreTypeActe]").filter(":checked").val(),
            filtreMinistere: $("input:radio[name=filtreMinistere]").filter(":checked").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: $("#listeModeles").find(".overlay").first().attr("id"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function doResetFiltreFdrSubstitution() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/" + $("#dossierId").val() + "/substitution/reset/filter";
    var myRequest = {
        contentId: "listeModeles",
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".btn-reset-filtre-substitution"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doRapidFiltre(isReload) {
    var map = buildSearchMap();
    var formContent = "listeDossiers";
    if (event && $(event.srcElement).closest(".tabulation__content").length > 0) {
        // cas où on a des filtres rapides sur plusieurs onglets
        formContent = $($(event.srcElement).closest(".tabulation__content")[0].querySelector(".tableForm")).attr("id");
    }
    let $formContent = $("#" + formContent);
    $formContent.find("[data-field]").each(function () {
        if ($(this).data("value") !== undefined) {
            map.set($(this).data("field"), $(this).data("value"));
        }
        if ($(this).data("order") !== undefined) {
            map.set($(this).data("field") + "Order", "'" + $(this).data("order") + "'");
        }
    });
    $formContent.find("input[data-isForm='true'], select[data-isForm='true']").each(function () {
        if (this.value !== undefined) {
            map.set(this.name, this.value);
        }
    });

    if (map.size !== 0) {
        var myRequest = {
            contentId: formContent,
            dataToSend: { search: JSON.stringify([...map]) },
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + "/dossier/filtrer",
            isChangeURL: false,
            overlay: isReload ? "reload-loader" : null,
            loadingButton: isReload ? null : $("#btnRecherche"),
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndUpdateNbResults, tabLoadError);
    }
}

function doAddDossierSuivi() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/ajouter/suivis";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(event.target),
    };

    callAjaxRequest(myRequest, showMessage, displaySimpleErrorMessage);
}

function doEditerDossierPdf() {
    var urlPdf = $("#basePath").val() + "dossier/editerPdf/" + $("#dossierId").val();
    window.open(urlPdf, "_blank");
}

function doExportZip() {
    constructAlertContainer([constructInfoAlert("Export Zip demandé")]);

    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/export/zip";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
    };
    callAjaxRequest(myRequest, downloadFile, displaySimpleErrorMessage);
}

function loadDossierMailContent() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/mail/contenu";
    const myRequest = {
        contentId: "modal-dossier-mail-content",
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, onLoadContentModal, displaySimpleErrorMessage);
}

function gotoSaisine(dossierId) {
    window.location.replace($("#basePath").val() + "dossier/" + dossierId + "/saisine/rectificative#main_content");
}

function envoyerSaisineRectificative() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/saisine/rectificative/envoyer/saisine";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(event.target),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function saisineRectificativePiecesComplementaires() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/saisine/rectificative/envoyer/pieces/complementaires";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(event.target),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doCreateListSignatureFromMailbox() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/travail/creer/liste/signature";
    const actionClass = ".ACTION_CREATE_LIST_MISE_EN_SIGNATURE_MAILBOX";
    const myTable = $(actionClass).closest(".tableForm");
    let data = [];

    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    const myRequest = {
        contentId: null,
        dataToSend: {
            idDossiers: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(actionClass),
    };

    callAjaxRequest(myRequest, callbackCreationListeSignature, displaySimpleErrorMessage);
}

function doCreateListSignatureFromFolder() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/dossier/creer/liste/signature";
    const actionClass = ".ACTION_CREATE_LIST_MISE_EN_SIGNATURE_DOSSIER";
    const myRequest = {
        contentId: null,
        dataToSend: {
            idDossier: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(actionClass),
    };

    callAjaxRequest(myRequest, callbackCreationListeSignature, displaySimpleErrorMessage);
}

function callbackCreationListeSignature(containerID, result, caller, extraDatas) {
    const jsonResponse = JSON.parse(result);
    const messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length == 0) {
        loadContentModalListeSignature(jsonResponse.data.idListe);
        const modal = $("#modal-success-create-list-signature");
        openModal(modal[0]);
    } else {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    }
}

function doSearchStatsArchivesDossiers() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/stats/archivage/dossiers/rechercher";
    const urlPath = $("#basePath").val() + "stats/archivage/dossiers";
    const form = $("#searchForm");
    if (!isValidForm(form)) return false;
    var myRequest = {
        contentId: "listeResultats",
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        url: urlPath,
        isChangeURL: true,
        overlay: null,
        loadingButton: $(".ACTION_STATUT_ARCHIVAGE_DOSSIER"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function doExportDossiersArchives() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/stats/archivage/dossiers/exporter";
    var myTable = $("#listeDossiersArchives").first();
    var dossiers = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            dossiers.push($(this).closest("tr").attr("data-id"));
        });
    var myRequest = {
        contentId: null,
        dataToSend: { idDossiers: dossiers },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function changeTypeCreationEpreuve() {
    $("#add-etape-button").prop("disabled", true);
    const fdrId = $("#d_fdr_content").length > 0 ? $("#d_fdr_content").attr("fdr-id") : $("#idModele").val();

    const ajaxUrl = $("#ajaxCallPath").val() + "/etape/ajouter/epreuve";

    const myRequest = {
        contentId: "etapeFdrContainer-etape",
        dataToSend: {
            fdrId: fdrId,
        },

        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitOrganigramme, displaySimpleErrorMessage);
}

function doRelancerDossierAbandonne() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/relancer/abandonne";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_RELANCER_DOSSIER_ABANDONNE"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function redemarrerDossier() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossier/redemarrer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doCreerDossier() {
    if (window.location.href.endsWith($("#basePath").val() + "travail/dossier/creation#main_content")) {
        reloadPage();
    } else {
        showReloadLoader();
        window.location.href = $("#basePath").val() + "travail/dossier/creation#main_content";
    }
}

function goToDossierSimilaire() {
    showReloadLoader();
    window.location.href =
        $("#basePath").val() + "dossiersimilaire?dossierId=" + $("#dossierId").val() + "#main_content";
}

var loadOngletData = function loadOnglet(containerID, result, caller, extraDatas) {
    replaceHtmlFunction(containerID, result);

    var dossierLocked = extraDatas.dossierLocked;
    var isDossierLock = $("#LEVER_VERROU_DOSSIER").length > 0 ? true : false;
    if (dossierLocked != isDossierLock) {
        updateStatutLockDossierAria(isDossierLock);
    }

    var triggerBtn = extraDatas.triggerBtn;
    $("#" + triggerBtn.attr("id")).focus();

    initAsyncSelect();
    initModaleSauvegarde("dossier");
};

function updateCurrentTab() {
    const $this = $(".dossier-onglet.tabulation__item--active");
    if ($this) {
        var dataName =
            $this.attr("data-name") === "traitement" ? $this.attr("data-name") + "/papier" : $this.attr("data-name");
        var targetURL = "/dossier/" + $("#dossierId").val() + "/" + dataName + "?" + $("#dossierLinkId").serialize();

        $this.on("click", function (evt) {
            return updateOngletData(evt, targetURL);
        });
    }
}

function initDossierSauvegardeModale() {
    const modale = $("#modale-sauvegarde-dossier");
    const url = window.location.pathname.split("/");

    var avantDernier = url[url.length - 2];
    modale.attr("data-onglet", url[url.length - 1]);
    modale.attr("data-onglet-parent", avantDernier);
    if (avantDernier == "papier") {
        // quand on clique sur un sous-onglet de l'onglet "traitement papier", le path est de type ".../traitement/papier/subtab"
        modale.attr("data-onglet-parent", "traitement");
    }
    if (avantDernier == "traitement") {
        // quand on clique sur l'onglet "traitement", l'url ne contient pas le nom du sous-onglet, et c'est le sous-onglet par défaut (='références') qui est chargé,
        modale.attr("data-onglet", "references");
    }

    // onglet traitement papier
    // references
    insertTextInputModaleSauvegarde(["dateArriveePapier", "commentaire"], modale);
    insertCheckboxModaleSauvegarde(["texteAPublier", "texteSoumisAValidation"], modale);
    insertOptinInputModaleSauvegarde(["signataire"], modale);

    // bordereau
    insertTextInputModaleSauvegarde(["modeleCourrier"], modale);
    insertAutocompleteInputModaleSauvegarde(["auteur", "coAuteur"], modale);

    // communication
    ["cabinetPM", "chargeMission", "autres", "cabinetSG"].forEach(function (name) {
        insertTableModaleSauvegarde(
            "table-" + name,
            ["p-destinataire", "p-dateEnvoi", "p-dateRetour", "p-dateRelance", "p-sensAvis", "p-objet"].map(
                (it) => it + "-" + name
            ),
            modale
        );
    });
    insertTextInputModaleSauvegarde(["personne"], modale);
    insertOptinInputModaleSauvegarde(["prioriteNormal"], modale);

    if ($("#communicationRbtSignataireParametre").is(":checked")) {
        insertTextInputModaleSauvegarde(["communicationSignataireParametre"], modale);
    } else {
        insertTextInputModaleSauvegarde(["communicationSignataireAutre"], modale);
    }

    // signature
    insertCheckboxModaleSauvegarde(["cheminCroix"], modale);
    insertTextInputModaleSauvegarde(["sgg", "cabinetPmSignature"], modale);

    // retour
    insertTextInputModaleSauvegarde(["retourA", "date", "motif"], modale);
    if ($("#retourRbtSignataireParametre").is(":checked")) {
        insertTextInputModaleSauvegarde(["retourSignataireParametre"], modale);
    } else {
        insertTextInputModaleSauvegarde(["retourSignataireAutre"], modale);
    }

    // epreuves
    insertTextInputModaleSauvegarde(
        [
            "epreuvePremiereDemandeDateEpreuvePourLe",
            "epreuvePremiereDemandeDestinataire",
            "epreuveNouvelleDemandeDateEpreuvePourLe",
            "epreuveNouvelleDemandeDestinataire",
            "dateRetourBon",
        ],
        modale
    );

    if ($("#epreuvePremiereDemandeRbtSignataireParametre").is(":checked")) {
        insertTextInputModaleSauvegarde(["epreuvePremiereDemandeSignataireParametre"], modale);
    } else {
        insertTextInputModaleSauvegarde(["epreuvePremiereDemandeSignataireAutre"], modale);
    }

    if ($("#epreuveNouvelleDemandeRbtSignataireParametre").is(":checked")) {
        insertTextInputModaleSauvegarde(["epreuveNouvelleeDemandeSignataireParametre"], modale);
    } else {
        insertTextInputModaleSauvegarde(["epreuveNouvelleDemandeSignataireAutre"], modale);
    }

    // publication
    insertTextInputModaleSauvegarde(
        [
            "dateEnvoiJO",
            "vecteurPublication-trtPapier",
            "modeParution-trtPapier",
            "delaiPublication-trtPapier",
            "datePublication-trtPapier",
        ],
        modale
    );
    insertOptinInputModaleSauvegarde(["optin-epreuvesRetour"], modale);

    // ampliation
    insertAutocompleteInputModaleSauvegarde(["destinataire", "destinataireLibre"], modale);
    insertOptinInputModaleSauvegarde(["optin-dossierPapierArchive"], modale);

    // onglet bordereau
    insertOptinInputModaleSauvegarde(
        ["optin-publicationRapportPresentation", "optin-isTexteRubriqueEntreprise"],
        modale
    );

    insertTextInputModaleSauvegarde(
        [
            "typeActe",
            "titreActe",
            "categorieActe",
            "datePublicationSouhaitee",
            "dateSignature",
            "nomResponsable",
            "prenomResponsable",
            "qualiteResponsable",
            "telResponsable",
            "melResponsable",
            "prioriteCE",
            "sectionCE",
            "rapporteur",
            "dateTransmissionSectionCE",
            "datePrevisionnelleSectionCe",
            "datePrevisionnelleAgCe",
            "numeroISA",
            "dateFournitureEpreuve",
            "publicationsConjointes",
            "integraleOuExtraitPublication",
            "datePreciseePublication",
            "modeParution",
            "delaiPublication",
            "zoneCommentaireSggDila",
            "baseLegale",
            "datesEffetRubriqueEntreprise",
            "periodiciteRapport",
            "natureTexte",
            "numeroTexte",
            "dateBaseLegale",
        ],
        modale
    );

    $("#accordion__content__epgBordereauTranspositionApplication input[type!=hidden]").each(function () {
        addHiddenSpanForMultipleInputValueToModale(
            "modale-sauvegarde-dossier-transpo-application-" + DOMPurify.sanitize($(this).attr("id")),
            $(this).val(),
            modale
        );
    });

    $("#accordion__content__epgBordereauDonneesIndexation .multiple-date input").each(function (nb) {
        addHiddenSpanForMultipleInputValueToModale(
            "modale-sauvegarde-dossier-datesEffetRubriqueEntreprise-" + nb,
            $(this).val(),
            modale
        );
    });

    insertAutocompleteInputModaleSauvegarde(["vecteurPublication", "rubriques", "motsCles", "champsLibres"], modale);
}

function addHiddenSpanForMultipleInputValueToModale(id, formValue, modale) {
    const text = DOMPurify.sanitize(formValue);
    const input = $("#" + id);
    if (input.length) {
        input.text(text);
    } else {
        modale.append(
            $("<span>", {
                hidden: true,
                id: id,
                text: text,
            })
        );
    }
}

function checkDossierSauvegardeModale() {
    const modale = $("#modale-sauvegarde-dossier");
    let onglet = modale.attr("data-onglet");
    const ongletParent = modale.attr("data-onglet-parent");
    let sousOnglet = "";

    if (ongletParent === "traitement") {
        sousOnglet = onglet;
        onglet = "traitementPapier";
    }

    if (onglet === "traitementPapier") {
        if (sousOnglet === "references") {
            const textRefOk = checkTextInputModaleSauvegarde(["dateArriveePapier", "commentaire"], modale);
            const checkboxRefOk = checkCheckboxInputModaleSauvegarde(
                ["texteAPublier", "texteSoumisAValidation"],
                modale
            );
            const optinRefOk = checkOptinInputModaleSauvegarde(["signataire"], modale);

            return textRefOk && checkboxRefOk && optinRefOk;
        } else if (sousOnglet === "bordereau") {
            const textBordOk = checkTextInputModaleSauvegarde(["modeleCourrier"], modale);
            const autoBordOk = checkAutocompleteInputModaleSauvegarde(["auteur", "coAuteur"], modale);

            return textBordOk && autoBordOk;
        } else if (sousOnglet === "communication") {
            const persComOk = checkTextInputModaleSauvegarde(["personne"], modale);
            const optinComOk = checkOptinInputModaleSauvegarde(["prioriteNormal"], modale);

            let tabsComOk = true;

            let signComOk = true;

            ["cabinetPM", "chargeMission", "autres", "cabinetSG"].some(function (name) {
                if (
                    !checkTableModaleSauvegarde(
                        "table-" + name,
                        ["p-destinataire", "p-dateEnvoi", "p-dateRetour", "p-dateRelance", "p-sensAvis", "p-objet"].map(
                            (it) => it + "-" + name
                        ),
                        modale
                    )
                ) {
                    tabsComOk = false;
                    return true;
                }
            });

            if ($("#communicationRbtSignataireParametre").is(":checked")) {
                signComOk = checkTextInputModaleSauvegarde(["communicationSignataireParametre"], modale);
            } else {
                signComOk = checkTextInputModaleSauvegarde(["communicationSignataireAutre"], modale);
            }

            return persComOk && optinComOk && tabsComOk && signComOk;
        } else if (sousOnglet === "signature") {
            const chkSignOk = checkCheckboxInputModaleSauvegarde(["cheminCroix"], modale);
            const txtSignOk = checkTextInputModaleSauvegarde(["cabinetPmSignature", "sgg"], modale);

            return chkSignOk && txtSignOk;
        } else if (sousOnglet === "retour") {
            const textRetOk = checkTextInputModaleSauvegarde(["retourA", "date", "motif"], modale);
            let optRetOk = true;

            if ($("#retourRbtSignataireParametre").is(":checked")) {
                optRetOk = checkTextInputModaleSauvegarde(["retourSignataireParametre"], modale);
            } else {
                optRetOk = checkTextInputModaleSauvegarde(["retourSignataireAutre"], modale);
            }

            return textRetOk && optRetOk;
        } else if (sousOnglet === "epreuves") {
            const textEpOk = checkTextInputModaleSauvegarde(
                [
                    "epreuvePremiereDemandeDateEpreuvePourLe",
                    "epreuvePremiereDemandeDestinataire",
                    "epreuveNouvelleDemandeDateEpreuvePourLe",
                    "epreuveNouvelleDemandeDestinataire",
                    "dateRetourBon",
                ],
                modale
            );

            let optEpPrOk = true;
            let optEpNvOk = true;

            if ($("#epreuvePremiereDemandeRbtSignataireParametre").is(":checked")) {
                optEpPrOk = checkTextInputModaleSauvegarde(["epreuvePremiereDemandeSignataireParametre"], modale);
            } else {
                optEpPrOk = checkTextInputModaleSauvegarde(["epreuvePremiereDemandeSignataireAutre"], modale);
            }

            if ($("#epreuveNouvelleDemandeRbtSignataireParametre").is(":checked")) {
                optEpNvOk = checkTextInputModaleSauvegarde(["epreuveNouvelleeDemandeSignataireParametre"], modale);
            } else {
                optEpNvOk = checkTextInputModaleSauvegarde(["epreuveNouvelleDemandeSignataireAutre"], modale);
            }

            return textEpOk && optEpPrOk && optEpNvOk;
        } else if (sousOnglet === "publication") {
            const textPubOk = checkTextInputModaleSauvegarde(
                [
                    "dateEnvoiJO",
                    "vecteurPublication-trtPapier",
                    "modeParution-trtPapier",
                    "delaiPublication-trtPapier",
                    "datePublication-trtPapier",
                ],
                modale
            );
            const optinPubOk = checkOptinInputModaleSauvegarde(["optin-epreuvesRetour"], modale);

            return textPubOk && optinPubOk;
        } else if (sousOnglet === "ampliation") {
            const autoAmpOk = checkAutocompleteInputModaleSauvegarde(["destinataire", "destinataireLibre"], modale);
            const optinAmpOk = checkOptinInputModaleSauvegarde(["optin-dossierPapierArchive"], modale);

            return autoAmpOk && optinAmpOk;
        }
    } else if (onglet === "bordereau") {
        const textBordereauOk = checkTextInputModaleSauvegarde(
            [
                "typeActe",
                "titreActe",
                "categorieActe",
                "datePublicationSouhaitee",
                "dateSignature",
                "nomResponsable",
                "prenomResponsable",
                "qualiteResponsable",
                "telResponsable",
                "melResponsable",
                "prioriteCE",
                "sectionCE",
                "rapporteur",
                "dateTransmissionSectionCE",
                "datePrevisionnelleSectionCe",
                "datePrevisionnelleAgCe",
                "numeroISA",
                "dateFournitureEpreuve",
                "publicationsConjointes",
                "integraleOuExtraitPublication",
                "datePreciseePublication",
                "modeParution",
                "delaiPublication",
                "zoneCommentaireSggDila",
                "baseLegale",
                "datesEffetRubriqueEntreprise",
                "periodiciteRapport",
                "natureTexte",
                "numeroTexte",
                "dateBaseLegale",
            ],
            modale
        );

        const optinBordereauOk = checkOptinInputModaleSauvegarde(
            ["optin-publicationRapportPresentation", "optin-isTexteRubriqueEntreprise"],
            modale
        );

        let bordereauOk = true;
        const transpList = $("#accordion__content__epgBordereauTranspositionApplication input[type!=hidden]");
        const transpModList = $("[id^='modale-sauvegarde-dossier-transpo-application-']");

        if (transpList.length !== transpModList.length) {
            bordereauOk = false;
        }

        transpList.each(function () {
            if ($(this).val() !== $("#modale-sauvegarde-dossier-transpo-application-" + $(this).attr("id")).text()) {
                bordereauOk = false;
            }
        });

        const datesList = $("#accordion__content__epgBordereauDonneesIndexation .multiple-date input");
        const datesModList = $("[id^='modale-sauvegarde-dossier-datesEffetRubriqueEntreprise-']");

        if (datesList.length !== datesModList.length) {
            bordereauOk = false;
        }

        datesModList.each(function () {
            const index = $(this).attr("id").substr("modale-sauvegarde-dossier-datesEffetRubriqueEntreprise-".length);
            if ($(this).text() !== $(datesList[index]).val()) {
                bordereauOk = false;
            }
        });

        const autoBordereauOk = checkAutocompleteInputModaleSauvegarde(
            ["vecteurPublication", "rubriques", "motsCles", "champsLibres"],
            modale
        );

        return bordereauOk && textBordereauOk && optinBordereauOk && autoBordereauOk;
    }
    return true;
}

function dossierCallbackModaleSauvegarde() {
    callbackModaleSauvegarde("dossier");
    constructAlertContainer([constructSuccessAlert("L'onglet a été sauvegardé")]);
}

function saveDossierFromModale() {
    var onglet = $("#modale-sauvegarde-dossier").attr("data-onglet");
    var ongletParent = $("#modale-sauvegarde-dossier").attr("data-onglet-parent");

    saveDossier(dossierCallbackModaleSauvegarde, ongletParent === "traitement" ? "traitement" : onglet, null);
}

function initDeleteDocumentModale(deleteFunction) {
    $("#btn-confirm-all-version-modal-suppression-document").attr("onclick", deleteFunction);
}

function cutFolderFile(input) {
    const myRequest = {
        contentId: null,
        dataToSend: {
            fileId: $(input).parents("tr.table-line").data("id"),
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/dossier/couper",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function pasteFolderFile(input) {
    const myRequest = {
        contentId: null,
        dataToSend: {
            folderId: $(input).parents("tr.table-line").data("id"),
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/dossier/coller",
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}
