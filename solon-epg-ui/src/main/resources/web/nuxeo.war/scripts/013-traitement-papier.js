function chargeSousOngletTraitementPapier(sousOnglet, me) {
    const modale = $("#modale-sauvegarde-dossier");
    modale.attr("data-confirmed", "yes");

    replaceHtmlFunction($(me).attr("aria-controls"), "Chargement");

    const url = new URL(window.location.href);
    const isMgpp = url.pathname.includes("mgpp/dossier") || url.searchParams.get("isMgpp") === "true";

    var targetURL = "dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet;
    var myRequest = {
        contentId: isMgpp ? "traitement_content" : "consult_dossier_content",
        dataToSend: $("#dossierLinkId").serialize() + "&isMgpp=" + isMgpp,
        method: "GET",
        dataType: "html",
        url: $("#basePath").val() + targetURL,
        ajaxUrl: $("#ajaxCallPath").val() + "/" + targetURL,
        isChangeURL: true,
        overlay: null,
        caller: me,
        extraDatas: {
            triggerBtn: $(me),
            dossierLocked: $("#LEVER_VERROU_DOSSIER").length > 0 ? true : false,
        },
    };

    callAjaxRequest(myRequest, loadOngletData, tabLoadErrorExtraDataTarget);
}

function isSousOngletBordereau(onglet) {
    return onglet === "traitement" && $(".tabulation__item--active[name=subtab]").data("name") === "bordereau";
}

function saveTraitement() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    if (sousOnglet === "references") {
        return getSubTabReferenceForm();
    } else if (sousOnglet === "communication") {
        return getSubTabCommunicationForm();
    } else if (sousOnglet === "signature") {
        return getSubTabSignatureForm();
    } else if (sousOnglet === "retour") {
        return getSubTabRetourForm();
    } else if (sousOnglet === "epreuves") {
        return getSubTabEpreuvesForm();
    } else if (sousOnglet === "publication") {
        return getSubTabPublicationForm();
    } else if (sousOnglet === "ampliation") {
        return getSubTabAmpliationForm();
    }
    return;
}

function getSubTabReferenceForm() {
    return {
        dossierId: $("#dossierId").val(),
        dateArriveePapier: $("#dateArriveePapier").val(),
        commentaire: $("#commentaire").val(),
        texteAPublier: $("#texteAPublier").prop("checked"),
        texteSoumisAValidation: $("#texteSoumisAValidation").prop("checked"),
        signataire: $("input[name='signataire']:checked").val(),
    };
}

function getSignataire(onglet) {
    var inputName = onglet + "RbtSignataire";
    var inputParametreId = onglet + "SignataireParametre";
    var inputAutreId = onglet + "SignataireAutre";
    var signataire;
    if ($("input[name=" + inputName + "]:checked").val() === "parametre") {
        signataire = $("#" + inputParametreId + " option:selected").val();
    } else if ($("input[name=" + inputName + "]:checked").val() === "autre") {
        signataire = $("#" + inputAutreId).val();
    }
    return signataire;
}

function getSubTabCommunicationForm() {
    var priorite = $("input[name=priorite]:checked").val();
    var personne = $("#personne").val();
    var signataire = getSignataire("communication");
    var selectedCabinetPM = getSelectedTableRow("table-cabinetPM");
    var selectedChargeMission = getSelectedTableRow("table-chargeMission");
    var selectedAutres = getSelectedTableRow("table-autres");
    var selectedCabinetSG = getSelectedTableRow("table-cabinetSG");

    return {
        dossierId: $("#dossierId").val(),
        priorite: priorite,
        personne: personne,
        signataire: signataire,
    };
}

function getSubTabSignatureForm() {
    return {
        dossierId: $("#dossierId").val(),
        cheminCroix: $("#cheminCroix").prop("checked"),
        sgg: $("#sgg").val(),
        cabinetPM: $("#cabinetPM").val(),
        signataire: $("#signataire").val(),
        dateEnvoi: $("#dateEnvoi").val(),
        dateRetour: $("#dateRetour").val(),
        commentaire: $("#commentaire").val(),
    };
}

function getSubTabRetourForm() {
    return {
        dossierId: $("#dossierId").val(),
        retourA: $("#retourA").val(),
        date: $("#date").val(),
        motif: $("#motif").val(),
        nomsSignataires: getSignataire("retour"),
    };
}

function getSubTabEpreuvesForm() {
    return {
        dossierId: $("#dossierId").val(),
        dateEpreuve: $("#epreuvePremiereDemandeDateEpreuve").val(),
        dateEpreuvePourLe: $("#epreuvePremiereDemandeDateEpreuvePourLe").val(),
        numeroListe: $("#epreuvePremiereDemandeNumeroListe").val(),
        dateEnvoiRelecture: $("#epreuvePremiereDemandeDateEnvoiRelecture").val(),
        destinataire: $("#epreuvePremiereDemandeDestinataire").val(),
        signataire: getSignataire("epreuvePremiereDemande"),
        nouvelleDateEpreuve: $("#epreuveNouvelleDemandeDateEpreuve").val(),
        nouvelleDateEpreuvePourLe: $("#epreuveNouvelleDemandeDateEpreuvePourLe").val(),
        nouveauNumeroListe: $("#epreuveNouvelleDemandeNumeroListe").val(),
        nouvelleDateEnvoiRelecture: $("#epreuveNouvelleDemandeDateEnvoiRelecture").val(),
        nouveauDestinataire: $("#epreuveNouvelleDemandeDestinataire").val(),
        nouveauSignataire: getSignataire("epreuveNouvelleDemande"),
        dateRetourBon: $("#dateRetourBon").val(),
    };
}

function getSubTabPublicationForm() {
    return {
        dossierId: $("#dossierId").val(),
        dateEnvoiJO: $("#dateEnvoiJO").val(),
        vecteurPublication: $("#vecteurPublication-trtPapier option:selected").val(),
        modeParution: $("#modeParution-trtPapier option:selected").val(),
        epreuvesRetour: $("input[name='epreuvesRetour']:checked").val(),
        delaiPublication: $("#delaiPublication-trtPapier option:selected").val(),
        datePublication:
            $("#delaiPublication-trtPapier option:selected").text() === "A date précisée"
                ? $("#datePublication-trtPapier").val()
                : "",
        numeroListe: $("#numeroListe").val(),
        dateParutionJORF: $("#dateParutionJORF").val(),
    };
}

function getSubTabAmpliationForm() {
    return {
        destinataire: JSON.stringify(
            $("#destinataire")
                .find("option")
                .toArray()
                .map((opt) => opt.value)
        ),
        destinataireLibre: JSON.stringify(
            $("#destinataireLibre")
                .find("option")
                .toArray()
                .map((opt) => opt.value)
        ),
        dossierId: $("#dossierId").val(),
        dossierPapierArchive: $("input[name='dossierPapierArchive']:checked").val(),
    };
}

function doFilterListsByDate(event) {
    event.preventDefault();
    if (isValidForm($("#gestionListForm"))) {
        const ajaxUrl = $("#ajaxCallPath").val() + "/suivi/gestionListes";
        const myRequest = {
            contentId: "tables-gestion-listes",
            dataToSend: "date=" + $("#dateCreation").val(),
            method: "GET",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, replaceWithHtmlFunction, displaySimpleErrorMessage);
    }
}

function getCheckedLinesFromDetailList() {
    let data = [];
    $("#tableListeSignatureModal")
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    return data;
}

function doRemoveFoldersFromList() {
    const listId = $("#idListeTraitementListe").val();

    const ajaxUrl = $("#ajaxCallPath").val() + "/suivi/liste/" + listId + "/retirer";
    const myRequest = {
        contentId: null,
        dataToSend: {
            foldersId: getCheckedLinesFromDetailList(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, reloadOrGoPreviousIfInfoMessage, displaySimpleErrorMessage);
}

function doPrintList() {
    let downloadUrl = $("#ajaxCallPath").val() + "/suivi/liste/" + $("#idListeTraitementListe").val() + "/imprimer";
    window.open(downloadUrl, "_blank");
}

function doTraitementSerie() {
    const modal = $("#modal-traitement-serie-list");
    if (!isValidForm(modal)) return false;

    let dates = {};
    modal.find("input.form-date-picker-input__form-input").each(function () {
        dates[$(this).attr("name")] = $(this).val();
    });

    const dataToSend = Object.assign(dates, { foldersId: getCheckedLinesFromDetailList() });

    const listId = $("#idListeTraitementListe").val();
    const ajaxUrl = $("#ajaxCallPath").val() + "/suivi/liste/" + listId + "/traiter";
    const myRequest = {
        contentId: null,
        dataToSend: dataToSend,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    closeModal(modal.get(0));
    callAjaxRequest(myRequest, displaySuccessOrMessage, displaySimpleErrorMessage);
}

function initModalAddCabinetPM() {
    initModalAdd("-add-cabinetPM");
}

function initModalAddChargeMission() {
    initModalAdd("-add-chargeMission");
}

function initModalAddAutres() {
    initModalAdd("-add-autres");
}

function initModalAddCabinetSG() {
    initModalAdd("-add-cabinetSG");
}

function initModalAdd(contentId) {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/initialiser";

    var myRequest = {
        contentId: contentId,
        dataToSend: {
            dossierId: $("#dossierId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "result_overlay",
    };

    callAjaxRequest(myRequest, replaceModalDestinataireFields, displaySimpleErrorMessage);
}

window.replaceModalDestinataireFields = function (contentId, result) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        $("#dateEnvoi" + contentId).val(jsonResponse.data.dateEnvoi);
        $("#dateRetour" + contentId).val(jsonResponse.data.dateRetour);
        $("#dateRelance" + contentId).val(jsonResponse.data.dateRelance);
        $("#objet" + contentId).val(jsonResponse.data.objet);
    }
};

function initModalDestinataireEdit(paramInitJs) {
    var paramArray = paramInitJs.split(";");
    var uniqueId = paramArray[0].split("=")[1];
    var id = paramArray[1].split("=")[1];

    $("#modal-destinataire-edit-" + id).data("uniqueId", uniqueId);

    for (var i = 2; i < paramArray.length; i++) {
        var elts = paramArray[i].split("=");
        var paramName = elts[0];
        var paramValue = elts[1];
        // set values
        if (paramName === "destinataire" || paramName === "sensAvis") {
            if (id === "autres") {
                $("#" + paramName + "-edit-" + id).val(paramValue);
            } else {
                $("#" + paramName + "-edit-" + id + " option")
                    .filter(function () {
                        return $.trim($(this).val()) == paramValue;
                    })
                    .prop("selected", true);
            }
        } else if (paramValue === "null") {
            $("#" + paramName + "-edit-" + id).val("");
        } else {
            $("#" + paramName + "-edit-" + id).val(paramValue);
        }
    }
}

function onChangeSensAvis(select) {
    var suffixeId = select.id.substring(8, select.id.length); // 8 correspond à "sensAvis"
    // seulement pour la modale d'edition et si le sens de l'avis change (sauf l'index 0)
    if (suffixeId.includes("-edit-") && select.selectedIndex !== 0) {
        // date de retour = date du jour
        $("#dateRetour" + suffixeId).val(getDateToday());
    }
}

function addDestinataire(id) {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/ajouter";

    var destinataire;
    if (id === "autres") {
        destinataire = $("#destinataire-add-" + id).val();
    } else {
        destinataire = $("#destinataire-add-" + id + " option:selected").val();
    }

    var myRequest = {
        contentId: id,
        dataToSend: {
            id: id,
            uniqueId: randomId(),
            destinataire: destinataire,
            dateEnvoi: $("#dateEnvoi-add-" + id).val(),
            dateRetour: $("#dateRetour-add-" + id).val(),
            dateRelance: $("#dateRelance-add-" + id).val(),
            sensAvis: $("#sensAvis-add-" + id + " option:selected").val(),
            objet: $("#objet-add-" + id).val(),
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "result_overlay_" + id,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function editDestinataire(id) {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer";

    var uniqueId = $("#modal-destinataire-edit-" + id).data("uniqueId");

    var destinataire;
    if (id === "autres") {
        destinataire = $("#destinataire-edit-" + id).val();
    } else {
        destinataire = $("#destinataire-edit-" + id + " option:selected").val();
    }

    var myRequest = {
        contentId: id,
        dataToSend: {
            id: id,
            uniqueId: uniqueId,
            destinataire: destinataire,
            dateEnvoi: $("#dateEnvoi-edit-" + id).val(),
            dateRetour: $("#dateRetour-edit-" + id).val(),
            dateRelance: $("#dateRelance-edit-" + id).val(),
            sensAvis: $("#sensAvis-edit-" + id + " option:selected").val(),
            objet: $("#objet-edit-" + id).val(),
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "result_overlay_" + id,
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function removeDestinataire(id) {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/supprimer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            id: id,
            idDestinataire: $("#idDestinataire").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "result_overlay_" + id,
        extraDatas: {
            id: id,
            idDestinataire: $("#idDestinataire").val(),
        },
    };

    callAjaxRequest(myRequest, removeRowDestinataire, displaySimpleErrorMessage);
}

var removeRowDestinataire = function removeRowDestinataire(containerID, result, caller, extraDatas) {
    $("#table-" + extraDatas.id)
        .children("tbody")
        .children()
        .each(function () {
            if ($(this).attr("data-id") === extraDatas.idDestinataire) {
                $(this).remove();
            }
        });
};

function enableIntitule(input) {
    var inputName = $(input).prop("name");
    var inputParametreId = $(input).data("id") + "SignataireParametre";
    var inputAutreId = $(input).data("id") + "SignataireAutre";
    if ($("input[name=" + inputName + "]:checked").val() === "parametre") {
        $("#" + inputParametreId).attr("disabled", false);
        if ($("#" + inputParametreId).hasClass("form-select-input__field--disabled")) {
            $("#" + inputParametreId).removeClass("form-select-input__field--disabled");
        }
        $("#" + inputAutreId).attr("disabled", true);
        if (!$("#" + inputAutreId).hasClass("form-input__field--disabled")) {
            $("#" + inputAutreId).addClass("form-input__field--disabled");
        }
    } else if ($("input[name=" + inputName + "]:checked").val() === "autre") {
        $("#" + inputParametreId).attr("disabled", true);
        if (!$("#" + inputParametreId).hasClass("form-select-input__field--disabled")) {
            $("#" + inputParametreId).addClass("form-select-input__field--disabled");
        }
        $("#" + inputAutreId).attr("disabled", false);
        if ($("#" + inputAutreId).hasClass("form-input__field--disabled")) {
            $("#" + inputAutreId).removeClass("form-input__field--disabled");
        }
    }
}

function getSelectedTableRow(idTable) {
    var selectedRow = [];
    $("#" + idTable)
        .find("tbody tr")
        .each(function () {
            var cac = $(this).find("input");
            if (cac.length > 0 && cac[0].checked) {
                selectedRow.push($(this).data("id"));
            }
        });
    return selectedRow;
}

function editerBordereauEnvoiCabinetSG() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL =
        "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer/bordereau/cabinetsg";

    var priorite = $("input[name=priorite]:checked").val();
    var personne = $("#personne").val();
    var signataire = getSignataire("communication");
    var selectedCabinetPM = getSelectedTableRow("table-cabinetPM");
    var selectedChargeMission = getSelectedTableRow("table-chargeMission");
    var selectedAutres = getSelectedTableRow("table-autres");
    var selectedCabinetSG = getSelectedTableRow("table-cabinetSG");

    var myRequest = {
        contentId: null,
        dataToSend: {
            priorite: priorite,
            personne: personne,
            signataire: signataire,
            cabinetPM: selectedCabinetPM,
            chargeMission: selectedChargeMission,
            autres: selectedAutres,
            cabinetSG: selectedCabinetSG,
        },
        method: "POST",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btnEditerBordereauEnvoiCabinetSG"),
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function editerBordereauRelance() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL =
        "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer/bordereau/relance";

    var priorite = $("input[name=priorite]:checked").val();
    var personne = $("#personne").val();
    var signataire = getSignataire("communication");
    var selectedCabinetPM = getSelectedTableRow("table-cabinetPM");
    var selectedChargeMission = getSelectedTableRow("table-chargeMission");
    var selectedAutres = getSelectedTableRow("table-autres");
    var selectedCabinetSG = getSelectedTableRow("table-cabinetSG");

    var myRequest = {
        contentId: null,
        dataToSend: {
            priorite: priorite,
            personne: personne,
            signataire: signataire,
            cabinetPM: selectedCabinetPM,
            chargeMission: selectedChargeMission,
            autres: selectedAutres,
            cabinetSG: selectedCabinetSG,
        },
        method: "POST",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btnEditerBordereauRelance"),
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function editerBordereauEnvoi() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL =
        "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer/bordereau/envoi";

    var priorite = $("input[name=priorite]:checked").val();
    var personne = $("#personne").val();
    var signataire = getSignataire("communication");
    var selectedCabinetPM = getSelectedTableRow("table-cabinetPM");
    var selectedChargeMission = getSelectedTableRow("table-chargeMission");
    var selectedAutres = getSelectedTableRow("table-autres");
    var selectedCabinetSG = getSelectedTableRow("table-cabinetSG");

    var myRequest = {
        contentId: null,
        dataToSend: {
            priorite: priorite,
            personne: personne,
            signataire: signataire,
            cabinetPM: selectedCabinetPM,
            chargeMission: selectedChargeMission,
            autres: selectedAutres,
            cabinetSG: selectedCabinetSG,
        },
        method: "POST",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btnEditerBordereauEnvoi"),
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function editerCheminCroix() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer";

    var myRequest = {
        contentId: null,
        dataToSend: {
            cheminCroix: $("#cheminCroix").prop("checked"),
            sgg: $("#sgg").val(),
            cabinetPM: $("#cabinetPM").val(),
        },
        method: "POST",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#editer-chemin-croix-button"),
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function editerBordereauRetour() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer";

    var myRequest = {
        contentId: null,
        dataToSend: getSubTabRetourForm(),
        method: "POST",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#editer-bordereau-retour"),
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function editerBordereauEnvoiRelecture(btn) {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/editer";

    var idPrefix = $(btn).data("id");
    var idBtn = $(btn).attr("id");

    var myRequest = {
        contentId: null,
        dataToSend: getEpreuveForm(idPrefix),
        method: "POST",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#" + idBtn),
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
        extraDatas: {
            successCallback: function () {
                $("#" + idPrefix + "DateEnvoiRelecture").val(getDateToday());
            },
        },
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function getEpreuveForm(idPrefix) {
    if (idPrefix === "epreuvePremiereDemande") {
        return {
            dossierId: $("#dossierId").val(),
            dateEpreuve: $("#" + idPrefix + "DateEpreuve").val(),
            dateEpreuvePourLe: $("#" + idPrefix + "DateEpreuvePourLe").val(),
            numeroListe: $("#" + idPrefix + "NumeroListe").val(),
            dateEnvoiRelecture: $("#" + idPrefix + "DateEnvoiRelecture").val(),
            destinataire: $("#" + idPrefix + "Destinataire").val(),
            signataire: getSignataire(idPrefix),
        };
    } else {
        return {
            dossierId: $("#dossierId").val(),
            nouvelleDateEpreuve: $("#" + idPrefix + "DateEpreuve").val(),
            nouvelleDateEpreuvePourLe: $("#" + idPrefix + "DateEpreuvePourLe").val(),
            nouveauNumeroListe: $("#" + idPrefix + "NumeroListe").val(),
            nouvelleDateEnvoiRelecture: $("#" + idPrefix + "DateEnvoiRelecture").val(),
            nouveauDestinataire: $("#" + idPrefix + "Destinataire").val(),
            nouveauSignataire: getSignataire(idPrefix),
        };
    }
}

function actualiserDestinataire(action) {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/actualiser";

    var idPrefix = $(action).data("id");

    var myRequest = {
        contentId: idPrefix + "Destinataire",
        dataToSend: getEpreuveForm(idPrefix),
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#" + idPrefix + "Destinataire-actualiser"),
    };

    callAjaxRequest(myRequest, replaceDestinataire, displaySimpleErrorMessage);
}

window.replaceDestinataire = function (contentId, result) {
    var jsonResponse = JSON.parse(result);
    var messagesContaineur = jsonResponse.messages;
    if (messagesContaineur.dangerMessageQueue.length > 0) {
        constructAlertContainer(messagesContaineur.dangerMessageQueue);
    } else {
        $("#" + contentId).val(jsonResponse.data);
    }
};

function showDatePublication() {
    if ($("#delaiPublication-trtPapier option:selected").text() === "A date précisée") {
        if ($("#datePublication-div").hasClass("hide-element")) {
            $("#datePublication-div").removeClass("hide-element");
        }
    } else {
        if (!$("#datePublication-div").hasClass("hide-element")) {
            $("#datePublication-div").addClass("hide-element");
        }
    }
}

function loadMailContentAmpliation() {
    if (!isValidForm($("#ampliationForm"))) {
        return;
    }

    if ($("#destinataire").val().length === 0 && $("#destinataireLibre").val().length === 0) {
        constructAlert(errorMessageType, ["Un destinataire est requis"]);
        return;
    }

    // si au moins un fichier
    if ($(".list-item-file-ampliation").children().length > 0) {
        // init modal
        doInitModal("#init-modal-ampliation");
        const idModal = $("#init-modal-ampliation").data("controls");
        const modal = $("#" + idModal);
        modal.addClass("interstitial-overlay__content--visible");
        modal.find("button")[0].focus();

        var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
        var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/contenu";

        var formData = new FormData();
        formData.append("dossierId", encodeURIComponent($("#dossierId").val()));
        formData.append("ampliationFile_nom", encodeURIComponent($(".list-item-file-ampliation").children().text()));

        const myRequest = {
            contentId: "modal-mail-ampliation-content",
            enctype: "multipart/form-data",
            dataToSend: formData,
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + targetURL,
            isChangeURL: false,
            overlay: null,
        };
        callAjaxRequest(myRequest, onLoadMCEModal, displaySimpleErrorMessage);
    } else {
        constructAlert(errorMessageType, ["Un fichier est requis"]);
    }
}

updateAmpliationFile = debounce(() => {
    addAmpliationFile();
}, 500);

function addAmpliationFile() {
    if (isValidForm($("#modal-ampliation-form"))) {
        var ul = $(".list-item-file-ampliation");
        $(ul).empty(); // empty list
        var file = $("input[id^=form_input_file_drag_and_drop_single_with_text_ampliation-file_")[0].files[0];
        var filename = file.name;

        var li = '<li class="uploadedFile"></li>';
        var btn =
            '<button type="button" onclick=removeAmpliationFile(this)' +
            ' class="base-btn base-btn--button base-btn--default basemodal-mail-ampliation-content-btn--light base-btn--transparent">' +
            '<span aria-hidden="true" class="icon icon--bin link__icon link__icon--append"></span></button>';

        var link = '<a onclick="downloadAmpliationFile()" download="' + filename + '">' + filename + "</a>";

        $(ul).append(li);
        $(ul).children().append(link);
        $(ul).children().append(btn);
        $(ul)
            .children()
            .children("button")
            .attr("aria-label", "Annuler l'ajout de " + filename);
        $(ul)
            .children()
            .children("button")
            .attr("data-tippy-content", "Annuler l'ajout de " + filename);

        const formData = new FormData();
        formData.append("dossierId", encodeURIComponent($("#dossierId").val()));
        formData.append("ampliationFile", file);
        formData.append("ampliationFile_nom", filename);

        _sendAmpliationFile(formData);
        closeModal($("#modal-ampliation").get(0));
    }
}

function removeAmpliationFile(button) {
    $(button).closest("li").remove();

    const formData = new FormData();
    formData.append("dossierId", encodeURIComponent($("#dossierId").val()));

    _sendAmpliationFile(formData);
}

function _sendAmpliationFile(formData) {
    const sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    const targetURL = `/dossier/${$("#dossierId").val()}/traitement/papier/${sousOnglet}`;

    const myRequest = {
        enctype: "multipart/form-data",
        dataToSend: formData,
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function convertInputToTable(name) {
    var tableItem = [];
    $("[name='" + name + "']").each(function () {
        tableItem.push(this.value);
    });

    return tableItem;
}

function sendMailAmpliation() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet + "/mail";
    const message = encodeHTMLSpecialChar(tinymce.get("mce").getContent());

    var formData = getSubTabAmpliationForm();
    formData.expediteur = DOMPurify.sanitize($("#expediteur option:selected").val());
    formData.objet = DOMPurify.sanitize($("#objet").val());
    formData.message = DOMPurify.sanitize(message);

    const myRequest = {
        contentId: "historique-ampliation",
        dataToSend: formData,
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
    closeModal($("#modal-send-mail-ampliation").get(0));
}

function annulerAmpliation() {
    var sousOnglet = $(".tabulation__item--active[name=subtab]").data("name");
    var targetURL = "/dossier/" + $("#dossierId").val() + "/traitement/papier/" + sousOnglet;

    const myRequest = {
        contentId: null,
        dataToSend: null,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function downloadAmpliationFile() {
    const dossierId = $("#dossierId").val();
    const url =
        $("#ajaxCallPath")
            .val()
            .substring(0, $("#ajaxCallPath").val().length - 5) +
        "/dossier/" +
        dossierId +
        "/traitement/papier/ampliation/telecharger";

    window.open(url, "_blank");
}
