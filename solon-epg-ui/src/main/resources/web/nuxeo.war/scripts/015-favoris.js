function removeDossierToFavorite() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/dossiers/supprimer";
    const myRequest = {
        dataToSend: {
            ids: getSelectedContent(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function removeFdrToFavorite() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/fdr/supprimer";
    var myTable = $(".tableForm").first();
    var fdrs = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            fdrs.push($(this).closest("tr").attr("data-id"));
        });
    const myRequest = {
        dataToSend: {
            ids: fdrs,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function addFdrToFavoriRecherche() {
    var myTable = $(".tableForm").first();
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    var ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/fdr/ajouter";
    var myRequest = {
        contentId: null,
        dataToSend: {
            ids: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function removeUserToFavorite() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/utilisateurs/supprimer";
    var myTable = $(".tableForm").first();
    var users = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            users.push($(this).closest("tr").attr("data-id"));
        });
    const myRequest = {
        dataToSend: {
            ids: users,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function doSupprimerFavorisRecherche() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/supprimer";
    var myTable = $("#listeFavorisRecherche").first();
    var favoris = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            favoris.push($(this).closest("tr").attr("data-id"));
        });
    const myRequest = {
        dataToSend: {
            idFavoris: favoris,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function addFavoriRechercheLibre() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/recherche/favoris/dossiers/enregistrer/criteres/libre";
    const baseProduction = $("#chk_base_production").get(0).checked;
    const baseArchivage = $("#chk_base_archivage").get(0).checked;
    const expressionExacte = $("#chk_expression_exacte").get(0).checked;
    const derniereVersion = $("#chk_derniere_version").get(0).checked;
    const recherche = $("#recherche").val();

    const myRequest = {
        contentId: null,
        dataToSend: {
            baseProduction: baseProduction,
            baseArchivage: baseArchivage,
            expressionExacte: expressionExacte,
            derniereVersion: derniereVersion,
            recherche: recherche,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            type: "libre",
        },
    };
    callAjaxRequest(myRequest, callBackAddFavoriRecherche, displaySimpleErrorMessage);
}
function addSearchUserToFavoriRecherche() {
    const path = "/recherche/favoris/utilisateurs/enregistrer/criteres";
    saveCriteresFavorisRecherche(path, "#searchUserForm", "utilisateur");
}

function addSearchFdrToFavoriRecherche() {
    const path = "/recherche/favoris/fdr/enregistrer/criteres";
    saveCriteresFavorisRecherche(path, "#searchModeleForm", "fdr");
}

function saveCriteresFavorisRecherche(context, id, type) {
    const ajaxUrl = $("#ajaxCallPath").val() + context;
    const myRequest = {
        contentId: null,
        dataToSend: $(id).serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            type: type,
        },
    };

    callAjaxRequest(myRequest, callBackAddFavoriRecherche, displaySimpleErrorMessage);
}
const callBackAddFavoriRecherche = function (contentId, result, caller, extraDatas, xhr) {
    ariaLoader($("#reload-loader"), false);
    window.location.href = $("#basePath").val() + "recherche/favoris/ajouter?type=" + extraDatas.type + "#main_content";
};
