function nextBordereauSimilaire() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossiersimilaire/bordereau/suivant";
    var myRequest = {
        contentId: "contentBordereauSimilaire",
        dataToSend: {
            dossierId: $("#dossierId").val(),
            indexCourant: $("#indexCourant").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndenableButton, displaySimpleErrorMessage);
}

function previousBordereauSimilaire() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossiersimilaire/bordereau/precedent";
    var myRequest = {
        contentId: "contentBordereauSimilaire",
        dataToSend: {
            dossierId: $("#dossierId").val(),
            indexCourant: $("#indexCourant").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndenableButton, displaySimpleErrorMessage);
}

function copyBordereauSimilaire() {
    const dossierId = $("#dossierId").val();
    let fields = [];
    $("#dossierFormBordereau")
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            fields.push($(this).data("name"));
        });
    const dossierIdSource = $("#dossierSourceId").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/dossiersimilaire/copier/" + dossierIdSource + "/" + dossierId;
    var myRequest = {
        contentId: null,
        dataToSend: {
            champs: fields,
        },
        extraDatas: {
            redirectUrl: $("#basePath").val() + "dossier/" + dossierIdSource + "/bordereau",
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoToUrl, displaySimpleErrorMessage);
}

const replaceHtmlFunctionAndenableButton = function (containerID, result, caller, extraDatas, xhr) {
    replaceHtmlFunction(containerID, result);
    enableNextPrevious();
};

function enableNextPrevious() {
    const hasNext = $("#hasButtonNext").val() == "true";
    const hasPrevious = $("#hasButtonPrevious").val() == "true";

    $(".ACTION_SUIVANT_COPY_BORDEREAU").attr("disabled", !hasNext);
    $(".ACTION_PRECEDENT_COPY_BORDEREAU").attr("disabled", !hasPrevious);
}

function selectBordereauCheckbox(elem) {
    updateCheckboxLabel(elem, $(elem).is(":checked"));
    var someChecked = $("#dossierFormBordereau").find(":checkbox:checked.js-custom-table-line-check").length > 0;
    enableTableSelectionActions(someChecked); // activer (ou non) les actions
}
