function archivageRetirerList() {
    const form = $("#archivage-retirer-modal-form");
    if (isValidForm(form)) {
        var myTable = $(".tableForm").first();
        var data = [];
        $(myTable)
            .find(":checkbox:checked.js-custom-table-line-check")
            .each(function () {
                data.push($(this).closest("tr").attr("data-id"));
            });
        const nbMois = $("#form_dossier_delais");
        var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/intermediaire/retirer";
        var myRequest = {
            contentId: null,
            dataToSend: {
                idDossiers: data,
                nombreMois: nbMois.val(),
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}

function archivageValiderList() {
    const form = $("#authentification_modal_form");
    if (isValidForm(form)) {
        var myTable = $(".tableForm").first();
        var dossiers = [];
        $(myTable)
            .find(":checkbox:checked.js-custom-table-line-check")
            .each(function () {
                dossiers.push($(this).closest("tr").attr("data-id"));
            });
        var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/intermediaire/valider";
        var myRequest = {
            contentId: null,
            method: "POST",
            dataToSend: {
                username: $("#form_login_username").val(),
                data: $("#form_login_password").val(),
                idDossiers: dossiers,
            },
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}

function exportDossierArchivage() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/archivage/intermediaire/exporter";
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        caller: this,
    };
    callAjaxRequest(myRequest, displaySuccessOrMessage, displaySimpleErrorMessage);
}
