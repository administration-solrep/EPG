function createOrModifyModeleCourrier(event, isCreation) {
    var formData = new FormData();
    let creationUrl = "/mgpp/modele/courrier/creer";

    if (!isCreation) {
        creationUrl = "/mgpp/modele/courrier/modifier";
        formData.append("idModele", $("#modele-courrier-form").data("id"));
    }

    if (!isValidForm($("#modele-courrier-form"))) return false;

    var file = getFilesFromFileInput("modeleFile")[0];

    if (!file && isCreation) {
        constructAlert(errorMessageType, ["Un fichier est requis"], "modele-courrier-form");
    } else {
        var fileName = file ? file.name : "";
        formData.append("modeleName", DOMPurify.sanitize($("#modeleName").val()));
        formData.append("typesCommunication", DOMPurify.sanitize($("#typesCommunication").val()));
        formData.append("modeleFile", file);
        formData.append("fileName", fileName);
        let myRequest = {
            contentId: null,
            dataToSend: formData,
            processData: false,
            contentType: false,
            enctype: "multipart/form-data",
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + creationUrl,
            isChangeURL: false,
            overlay: null,
            loadingButton: event.target,
        };

        callAjaxRequest(myRequest, checkErrorOrGoModeleCourrierList, displaySimpleErrorMessage);
    }
}

window.checkErrorOrGoModeleCourrierList = function (contentId, result, caller, extraDatas, xhr) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        goModeleCourrierList();
    }
};

function goModeleCourrierList() {
    window.location.replace($("#basePath").val() + "admin/mgpp/courriers#main_content");
}

function goModificationModeleCourrier() {
    window.location.replace(
        $("#basePath").val() +
            "admin/mgpp/courriers/modifier/" +
            $("#modele-courrier-form").data("name") +
            "#main_content"
    );
}

function doDeleteModeleCourrier() {
    var formData = new FormData();
    formData.append("idModele", $("#modele-courrier-form").data("id"));
    let myRequest = {
        contentId: null,
        processData: false,
        contentType: false,
        enctype: "multipart/form-data",
        dataToSend: formData,
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/modele/courrier/supprimer",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoModeleCourrierList, displaySimpleErrorMessage);
}
