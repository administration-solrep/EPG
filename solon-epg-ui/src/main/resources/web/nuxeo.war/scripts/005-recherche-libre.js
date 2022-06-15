function reinitRechercheLibre() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/recherche/reinit";
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function getSelectedChkDataId(contentId) {
    var dataId = [];
    $("#" + contentId + " input.form-choice-input__input").each(function () {
        if ($(this).get(0).checked) {
            dataId.push($(this).get(0).getAttribute("data-id"));
        }
    });
    return dataId;
}

function removeFilter(btn) {
    if (btn.id.startsWith("btn-filter-poste")) {
        // set empty poste
        $("#poste-id").val("");
        updateResults();
    } else if (btn.id.startsWith("btn-filter-date")) {
        // set empty date
        var id = btn.id.substr(16);
        $("#dateDebut-" + id).val("");
        $("#dateFin-" + id).val("");
        updateResults();
    } else {
        // Facettes : uncheck checkbox
        var chkId = btn.id.replace("btn-filter-", "chk-");
        $("#" + chkId).removeAttr("checked");
        addOrRemoveItemFacet($("#" + chkId));
    }
}

function addOrRemoveItemFacet(input) {
    var type = $(input).data("type");
    var id = $(input).data("id");
    var value = $(input).data("value");
    var isChecked = $(input).prop("checked");

    var ajaxUrl = $("#ajaxCallPath").val() + "/recherche/facette";
    var myRequest = {
        contentId: null,
        dataToSend: {
            type: type,
            id: id,
            value: value,
            isChecked: isChecked,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function updateResults() {
    var posteId = $("#poste-id").val();
    var posteEnCours = $("#chk-poste-en-cours").get(0).checked;

    var dateDebutCreation = $("#dateDebut-Creation").val();
    var dateFinCreation = $("#dateFin-Creation").val();

    var dateDebutPublication = $("#dateDebut-Publication").val();
    var dateFinPublication = $("#dateFin-Publication").val();

    var dateDebutSignature = $("#dateDebut-Signature").val();
    var dateFinSignature = $("#dateFin-Signature").val();

    var dateDebutPublicationJO = $("#dateDebut-Publication_JO").val();
    var dateFinPublicationJO = $("#dateFin-Publication_JO").val();

    var ajaxUrl = $("#ajaxCallPath").val() + "/recherche/filtre";
    var myRequest = {
        contentId: null,
        dataToSend: {
            posteId: posteId,
            posteEnCours: posteEnCours,
            dateDebutCreation: dateDebutCreation,
            dateFinCreation: dateFinCreation,
            dateDebutPublication: dateDebutPublication,
            dateFinPublication: dateFinPublication,
            dateDebutSignature: dateDebutSignature,
            dateFinSignature: dateFinSignature,
            dateDebutPublicationJO: dateDebutPublicationJO,
            dateFinPublicationJO: dateFinPublicationJO,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doRechercheLibre() {
    var baseProduction = $("#chk_base_production").get(0).checked;
    var baseArchivage = $("#chk_base_archivage").get(0).checked;
    var expressionExacte = $("#chk_expression_exacte").get(0).checked;
    var derniereVersion = $("#chk_derniere_version").get(0).checked;
    var recherche = $("#recherche").val();
    // filtres
    var posteId = $("#poste-id").val();
    var posteEnCours = $("#chk-poste-en-cours").get(0).checked;
    var dateDebutCreation = $("#dateDebut-Creation").val();
    var dateFinCreation = $("#dateFin-Creation").val();
    var dateDebutPublication = $("#dateDebut-Publication").val();
    var dateFinPublication = $("#dateFin-Publication").val();
    var dateDebutSignature = $("#dateDebut-Signature").val();
    var dateFinSignature = $("#dateFin-Signature").val();
    var dateDebutPublicationJO = $("#dateDebut-Publication_JO").val();
    var dateFinPublicationJO = $("#dateFin-Publication_JO").val();

    var ajaxUrl = $("#ajaxCallPath").val() + "/recherche/rechercher";
    var myRequest = {
        contentId: null,
        dataToSend: {
            baseProduction: baseProduction,
            baseArchivage: baseArchivage,
            expressionExacte: expressionExacte,
            derniereVersion: derniereVersion,
            recherche: recherche,
            posteId: posteId,
            posteEnCours: posteEnCours,
            dateDebutCreation: dateDebutCreation,
            dateFinCreation: dateFinCreation,
            dateDebutPublication: dateDebutPublication,
            dateFinPublication: dateFinPublication,
            dateDebutSignature: dateDebutSignature,
            dateFinSignature: dateFinSignature,
            dateDebutPublicationJO: dateDebutPublicationJO,
            dateFinPublicationJO: dateFinPublicationJO,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrFocusResult, displaySimpleErrorMessage);
}

function checkErrorOrFocusResult(contentId, result, caller, extraDatas, xhr) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        window.location.hash = "#recherche-libre-resultats";
        reloadPage();
    }
}

function goToResultSearchPage(page) {
    var sort = $("#select-tri").get(0).selectedOptions[0].getAttribute("value");
    var size = $("#select-elem-page").get(0).selectedOptions[0].getAttribute("value");

    var myRequest = {
        contentId: "recherche-libre-resultats",
        dataToSend: {
            page: page,
            size: size,
            sort: sort,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/recherche/resultat",
        url:
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) + "/recherche/libre",
        isChangeURL: true,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function goToPreviousResultSearchPage() {
    var page = parseInt($(".pagination__item--is-active > a").attr("data-value")) - 1;
    goToResultSearchPage(page);
}

function goToNextResultSearchPage() {
    var page = parseInt($(".pagination__item--is-active > a").attr("data-value")) + 1;
    goToResultSearchPage(page);
}

function applySelectedOptions() {
    goToResultSearchPage(1); // on ramène à la 1ère page lorsqu'on modifie le trie ou le nb de résultats par page
}

$("#recherche").keyup(function (event) {
    if (event.keyCode === 13) {
        $("#btn-rechercher").click();
    }
});

function rechercheLibreToggleSelectionTool(isSelected, id) {
    const url = isSelected ? "retirer" : "ajouter";
    const ajaxUrl = $("#ajaxCallPath").val() + "/selection/" + url;
    let myRequest = {
        contentId: "selection-tool",
        dataToSend: {
            id: id,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            isSelected: isSelected,
        },
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndRefreshLink, displaySimpleErrorMessage);
}

var replaceHtmlFunctionAndRefreshLink = function replaceHtmlFunctionAndRefreshLink(
    containerID,
    result,
    caller,
    extraDatas
) {
    let jsonResponse = JSON.parse(result);
    $("#" + containerID).html(jsonResponse.data.template);

    const messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.successMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.successMessageQueue);
        toggleAddRemoveButton(jsonResponse.data.updatedIds, extraDatas.isSelected);
    } else if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    }
};

function toggleAddRemoveButton(idsToDelete, isSelected) {
    for (var id of idsToDelete) {
        $("#tool-selection-add-" + id).toggle(isSelected);
        $("#tool-selection-remove-" + id).toggle(!isSelected);
    }
}

function sauvegarderFavoriRecherche() {
    if (isValidForm($("#formAjoutFavori"))) {
        let myRequest = {
            contentId: null,
            dataToSend: $("#typeFavori").serialize() + "&" + $("#formAjoutFavori").serialize(),
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + "/recherche/favoris/sauvegarder",
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
    }
}

function exportSearchResult(experte) {
    const url = experte ? "/recherche/experte/exporter" : "/recherche/exporter";
    var ajaxUrl = $("#ajaxCallPath").val() + url;
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(
            experte ? ".ACTION_RECHERCHE_EXPERTE_DOSSIER_EXPORT" : ".ACTION_RECHERCHE_RAPIDE_DOSSIER_EXPORT"
        ),
    };
    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function exportTabDynResult() {
    const url = "/suivi/tableaux/exporter/" + $("#idTabDyn").val();
    var ajaxUrl = $("#ajaxCallPath").val() + url;
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $(".ACTION_TAB_DYN_DOSSIER_EXPORT"),
    };
    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}
