//Put SWORD specific scripts for appli here
$(document).ready(function () {
    initMgpp();
    initPan();
});

function getAppliName() {
    return "solon-epg";
}

function onChangeMailboxTri(tri) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/corbeille/mailbox";
    var myRequest = {
        contentId: "mailboxListTree",
        dataToSend: "tri=" + tri,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: $("#mailboxListTree").find(".overlay").first().attr("id"),
        caller: this,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction);
}

function doSearch() {
    if (window.location.toString().includes("mgpp")) {
        doSearchMGPP();
    } else if (window.location.toString().includes("suivi/pan")) {
        doRapidFiltrePan();
    } else {
        doRapidFiltre();
    }
}

function onClickOpenSuiviNode(rootTreeId, key, targetId) {
    var target = document.getElementById(targetId);
    var targetStatus = target.getAttribute("aria-hidden");
    onOpenSuiviNode(rootTreeId, key, targetStatus);
}

function onOpenSuiviNode(rootTreeId, key, isOpening) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/corbeille/suivi";
    var myRequest = {
        contentId: rootTreeId,
        dataToSend: "rootTreeId=" + rootTreeId + "&key=" + key + "&isOpening=" + isOpening,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: $("#" + rootTreeId)
            .find(".overlay")
            .first()
            .attr("id"),
        caller: $(event.target).is(":button") ? $(event.target) : $(event.target).parent(),
        ignoreChronology: true,
    };

    callAjaxRequest(myRequest, loadTreeData);
}

function refreshTreesSuivi() {
    $(".tree-navigation__list button[data-tree-reload-id]").each(function () {
        const rootTreeNodeId = $(this).attr("data-tree-reload-id");
        onOpenSuiviNode(rootTreeNodeId, "", false);
    });
}

var replaceHtmlFunctionAndUpdateNbResults = function replaceHtmlFunctionAndUpdateNbResults(containerID, result) {
    replaceHtmlFunction(containerID, result);
    const titre = $("#nbResultInput").val();
    if (titre) {
        $("#nbResultTitre").text(titre);
    }
};

var replaceHtmlFunctionAndShowToast = function replaceHtmlFunctionAndShowToast(containerID, result) {
    replaceHtmlFunction(containerID, result);
    initSelectAutocomplete();
    initAsyncSelect();
    checkErrorOrToast;
};

var replaceHtmlFunctionAndInitAutoSelect = function replaceHtmlFunctionAndInitAutoSelect(containerID, result) {
    replaceHtmlFunction(containerID, result);
    initSelectAutocomplete();
    initAsyncSelect();
};

function genererRapportBirt() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/stats/displayStat";
    const birtReportForm = $("#birtReportForm");
    if (!isValidForm(birtReportForm)) return false;
    var myRequest = {
        contentId: "birt-fragment",
        dataToSend: $("#idStat").serialize() + "&" + birtReportForm.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

var iloadEspace = setInterval(reloadCorbeille, $("#refreshEspaceTravailDelay").val());

function reloadCorbeille() {
    if (window.location.toString().includes("/travail")) {
        let tri = "PAR_TYPE_ACTE";
        var radioChecked = $('input[name="form-radio-button-column"]:checked').attr("id");
        let keySelected = $("input[name=typeActe]").val();
        if (radioChecked.indexOf("poste") !== -1) {
            tri = "PAR_POSTE";
            keySelected = $("input[name=posteId]").val();
        } else if (radioChecked.indexOf("etape") !== -1) {
            tri = "PAR_TYPE_ETAPE";
            keySelected = $("input[name=typeEtape]").val();
        }

        var ajaxUrl = $("#ajaxCallPath").val() + "/corbeille/mailbox";
        var myRequest = {
            contentId: "mailboxListTree",
            dataToSend: "tri=" + tri + "&key=" + keySelected,
            method: "GET",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: $("#mailboxListTree").find(".overlay").first().attr("id"),
            caller: this,
            ignoreChronology: true,
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndReloadMain);
    }
}

var replaceHtmlFunctionAndReloadMain = function replaceAndReloadMain(containerID, result) {
    replaceHtmlFunction(containerID, result);
    if (!window.location.toString().includes("/attenteSignature") && !window.location.toString().endsWith("/travail")) {
        doRapidFiltre(true);
    }
};

var replaceHtmlFunctionAndUpdateNbResults = function replaceHtmlFunctionAndUpdateNbResults(containerID, result) {
    replaceHtmlFunction(containerID, result);
    const titre = $("#nbResultInput").val();
    if (titre) {
        $("#nbResultTitre").text(titre);
    }
};

function doReinitialiserFiltres() {
    var target, dataToSend;
    if (window.location.toString().includes("mgpp")) {
        target = "/mgpp/dossier/reinitialiser";
        dataToSend = { idCorbeille: $("#idCorbeille").val() };

        let myRequest = {
            contentId: null,
            dataToSend: dataToSend,
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + target,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    } else if (window.location.toString().includes("pan")) {
        let typeForm = $(event.srcElement).closest("#searchForm")[0];
        var map = new Map();

        $(typeForm).find("input[data-isForm!='true'], select[data-isForm!='true']").val("");

        $(typeForm)
            .find("input[data-isForm='true'], select[data-isForm='true']")
            .each(function () {
                if (this.value !== undefined) {
                    map.set(this.name, this.value);
                }
            });

        var myRequest = {
            contentId: $($(event.srcElement).closest(".tabulation__content")[0].querySelector(".tableForm")).attr("id"),
            dataToSend: {
                search: JSON.stringify([...map]),
                currentSection: $("#currentSection").val(),
                currentPanTab: $("#currentPanTab").val(),
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + "/suivi/pan/filtrer",
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, replaceWithHtmlFunction, tabLoadError);
    } else {
        target = "/mgpp/dossier/reinitialiser";
        dataToSend = { idCorbeille: $("#idCorbeille").val() };

        let myRequest = {
            contentId: null,
            dataToSend: dataToSend,
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + target,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}

function launchRechercheRapide(event) {
    doLaunchRechercheRapide(event, "nor");
}
