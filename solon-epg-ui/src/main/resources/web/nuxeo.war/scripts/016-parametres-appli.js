function saveParametresApplication() {
    if (isValidForm($("#formParamAppli"))) {
        var targetURL = "admin/param/enregistrer";
        var paramAppliForm = $("#formParamAppli").serialize();
        var myRequest = {
            contentId: null,
            dataToSend: paramAppliForm,
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#basePath").val() + targetURL,
            isChangeURL: false,
            overlay: null,
            caller: this,
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}

function reloadMgppParam() {
    $("#mgppParamForm")[0].reset();
    cleanAlerts();
    constructAlertContainer([constructSuccessAlert("Saisie abandonnée")]);
}
