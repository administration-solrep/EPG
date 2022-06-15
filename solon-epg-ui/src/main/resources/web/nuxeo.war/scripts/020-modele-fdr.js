function doExportResultListModele() {
    var urlCsv = $("#ajaxCallPath").val() + "/fdr/rechercher/resultats/export";
    window.open(urlCsv, "_blank");
}

function reinitCreationMasseModelesForm() {
    cleanAllPreviousErrorOrSuccess();
    $("input.aria-autocomplete__input").val("");
}

function showLoaderOnFormSubmit(form) {
    if (!isValidForm($(form))) return;
    showReloadLoader();
}

function doDeleteModeleMass() {
    var myTable = $(".tableForm").first();
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/fdr/modeles/massAction/suppression";
    const myRequest = {
        contentId: null,
        dataToSend: {
            idModeles: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}
