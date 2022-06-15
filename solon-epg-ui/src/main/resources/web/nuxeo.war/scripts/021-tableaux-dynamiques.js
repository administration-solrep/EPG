function doEditTd(input) {
    var idTab = $(input).parents(".quick-access-dropdown").find("input[name=idTab]").val();
    window.location.href = $("#basePath").val() + "suivi/tableaux/modifier?idTab=" + idTab + "#main_content";
}

function saveTableauDynamique() {
    var $form = $("#td-form");
    if (!isValidForm($form)) return false;
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/tableaux/sauvegarder";
    var myRequest = {
        contentId: null,
        dataToSend: $form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        caller: this,
    };
    callAjaxRequest(myRequest, checkErrorOrGoToBlocTd, displaySimpleErrorMessage);
}
function doDeleteTd() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/suivi/tableaux/supprimer";
    const idTab = $("#idTab").val();
    const myRequest = {
        contentId: null,
        dataToSend: { id: idTab },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        caller: this,
    };
    callAjaxRequest(myRequest, callBackSuppressionTD, displaySimpleErrorMessage);
}
const checkErrorOrGoToBlocTd = function (contentId, result) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        ariaLoader($("#reload-loader"), false);
        window.location.href = $("#basePath").val() + "suivi#suivi_menu_td";
    }
};

const callBackSuppressionTD = function (contentId, result) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else if ($("#urlPreviousPage").val()) {
        goPreviousPage();
    } else {
        window.location.replace($("#basePath").val() + "suivi#suivi_menu_td");
        reloadPage();
    }
};

function creerTDRechercheLibre() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/suivi/tableaux/enregistrer/criteres/libre";
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
        caller: this,
    };
    callAjaxRequest(myRequest, callBackTDRechercheLibre, displaySimpleErrorMessage);
}

const callBackTDRechercheLibre = () => {
    ariaLoader($("#reload-loader"), false);
    window.location.href = $("#basePath").val() + "suivi/tableaux/creer?type=libre#main_content";
};
