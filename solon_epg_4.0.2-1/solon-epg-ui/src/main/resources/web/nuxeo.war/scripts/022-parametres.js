function doAddRubrique() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/rubrique/ajouter";

    const myRequest = {
        contentId: "input-list-rubriques",
        dataToSend: {
            rubriques: $("#form_input_liste_rubriques").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, displaySimpleErrorMessage);
}

function doRemoveRubrique() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/rubrique/supprimer";

    const myRequest = {
        contentId: "input-list-rubriques",
        dataToSend: {
            id: $("#idRubrique").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, displaySimpleErrorMessage);
}

function initCreationMotsClesModale() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/creer/init";

    const myRequest = {
        contentId: "modalAjoutListeMotsClesContent",
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, initMotsClesModaleCallBack, displaySimpleErrorMessage);
}

function doCreateListMotsCles() {
    const modal = $("#modal-ajout-liste-mots-cles");
    if (!isValidForm(modal)) return false;

    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/creer";
    const typeSelectionMinistere = $("input[name='type-selection']:checked").val();

    const ministereId = typeSelectionMinistere === "Tous" ? "Tous" : $("#selection-ministere").val();

    const myRequest = {
        contentId: "input-list-rubriques",
        dataToSend: {
            label: $("#modal_params_indexation_mots_cles").val(),
            ministereId: ministereId,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    closeModal(modal.get(0));

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function initReattributionMotsClesModale(listeId) {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/" + listeId + "/charger";

    const myRequest = {
        contentId: "modalAjoutListeMotsClesContent",
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, initMotsClesModaleCallBack, displaySimpleErrorMessage);
}

function initMotsClesModaleCallBack(containerID, result, extraDatas) {
    replaceHtmlFunction(containerID, result, extraDatas);
    initAsyncSelect();
}

function doRemoveListeMotsCles() {
    const listeId = $("#listeId").val();
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/" + listeId + "/supprimer";

    const myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doAddMotsCles() {
    const listeId = $("#listeId").val();
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/" + listeId + "/ajouter";

    const myRequest = {
        contentId: "input-list-listeMotsCles",
        dataToSend: {
            motsCles: $("#form_input_liste_listeMotsCles").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doRemoveMotsCles() {
    const listeId = $("#listeId").val();
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/" + listeId + "/retirer";

    const myRequest = {
        contentId: "input-list-listeMotsCles",
        dataToSend: {
            motsCles: $("#motsCles").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function changeSelectionMinistere(input) {
    const select = $("#select-autocomplete-ministeres");
    const enableSelect = input === "un" ? true : false;
    toggleOrganigrammeSelectAutocomplete(select, enableSelect);
}

function doReattributionMinistereListMotsCles() {
    const modal = $("#modal-ajout-liste-mots-cles");
    if (!isValidForm(modal)) return false;

    const listeId = $("#listeId").val();
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/param/indexation/liste/" + listeId + "/reattribuer";
    const typeSelectionMinistere = $("input[name='type-selection']:checked").val();

    const ministereId = typeSelectionMinistere === "Tous" ? "Tous" : $("#selection-ministere").val();

    const myRequest = {
        contentId: null,
        dataToSend: {
            ministereId: ministereId,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    closeModal(modal.get(0));

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}
