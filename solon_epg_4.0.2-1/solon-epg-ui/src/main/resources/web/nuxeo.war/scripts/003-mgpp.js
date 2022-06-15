var widgetNumber = 0;

var evtType;
var typeEvenementId = "#typeEvenement";
var natureLoiId = "#natureLoi";
var natureLoiText = "#natureLoiText";
var typeLoiId = "#typeLoi";
var objetId = "#form_textarea-objet";
var intituleId = "#form_textarea-intitule";
var dateCongresId = "#dateCongres";
var emetteurId = "#emetteur";
var destinataireId = "#destinataire";
var destinataireCopieId = "#destinataireCopie";
var destinataireCopieText = "#destinataireCopieText";
var niveauLectureId = "#niveauLecture";
var previousDateValue;

$(document).ready(function () {
    evtType = $(typeEvenementId).val();

    switch (evtType) {
        case "EVT01":
        case "EVT02":
            $(natureLoiId).change(function () {
                updateFields();
            });
            $(typeLoiId).change(function () {
                updateFields();
            });
        case "EVT39":
        case "EVT43BIS":
            $(objetId).change(function () {
                updateFields();
            });
            break;
        case "EVT35":
            setInterval(function () {
                if (previousDateValue != $(dateCongresId).val()) {
                    updateFields();
                    previousDateValue = $(dateCongresId).val();
                }
            }, 1000);
            break;
    }

    $(destinataireId).change(function () {
        updateDestinataires();

        if (
            evtType == "EVT01" ||
            evtType == "EVT09" ||
            evtType == "EVT10" ||
            evtType == "EVT23BIS" ||
            evtType == "EVT25"
        ) {
            updateNiveauLecture();
            showHideNiveauLecture($("#niveauLecture").val());
        }
    });

    showHideNiveauLecture($("#niveauLecture").val());
});

function updateFields() {
    switch (evtType) {
        case "EVT01":
        case "EVT02":
            updateIntitule();
            break;
        case "EVT35":
            updateObjet();
            break;
        case "EVT39":
            updateIntituleWithObjet(
                "Proposition de résolution, déposée en application de l'article 34-1 de la constitution, "
            );
            break;
        case "EVT43BIS":
            updateIntituleWithObjet("Résolution européenne ");
            break;
    }
}

function updateIntitule() {
    var natureLoi;
    if ($(natureLoiId).length > 0) {
        natureLoi = $(natureLoiId + " option:selected").text();
        natureLoi = jQuery.trim(natureLoi);
    } else {
        natureLoi = $(natureLoiText).text() == "" ? $(natureLoiText).val() : $(natureLoiText).text();
        natureLoi = jQuery.trim(natureLoi);
    }
    var typeLoi = $(typeLoiId + " option:selected").text();
    var typeLoiValue = $(typeLoiId + " option:selected").val();
    var objet = $(objetId).val();

    if ("" == typeLoiValue) {
        $(intituleId).val(natureLoi + " " + objet);
    } else {
        $(intituleId).val(natureLoi + " de " + typeLoi.toLowerCase() + " " + objet);
    }
}

function updateObjet() {
    var date = $(dateCongresId).val();
    $(objetId).val("Convocation du congrès " + date);
}

function updateIntituleWithObjet(value) {
    var objet = $(objetId).val();
    $(intituleId).val(value + objet);
}

function updateDestinataires() {
    var destinataire = $(destinataireId).val();
    if (destinataire) {
        if ($(destinataireCopieId).length) {
            $(destinataireCopieId).val(destinataire == "ASSEMBLEE_NATIONALE" ? "SENAT" : "ASSEMBLEE_NATIONALE");
        }
        if ($(destinataireCopieText).length) {
            $(destinataireCopieText).text(destinataire == "ASSEMBLEE_NATIONALE" ? "Sénat" : "Assemblée nationale");
            $(destinataireCopieText).val(destinataire == "ASSEMBLEE_NATIONALE" ? "Sénat" : "Assemblée nationale");
        }
    }
}

function updateNiveauLecture() {
    var destinataire = $(destinataireId).val();
    if (destinataire == "ASSEMBLEE_NATIONALE") {
        if (evtType == "EVT23BIS") {
            $(niveauLectureId).val("NOUVELLE_LECTURE_AN");
        } else {
            $(niveauLectureId).val("AN");
        }
    } else if (destinataire == "SENAT") {
        if (evtType == "EVT23BIS") {
            $(niveauLectureId).val("NOUVELLE_LECTURE_SENAT");
        } else {
            $(niveauLectureId).val("SENAT");
        }
    }
}

function initMgpp() {
    updateCurrentTabMgpp();
    enableDisableCreateCommunicationCreatrice();
}

function enableDisableCreateCommunicationCreatrice() {
    if ($("#communication-type option").length == 0) {
        $("#btnCreateCommunicationCreatrice").attr("disabled", "disabled");
    } else {
        $("#btnCreateCommunicationCreatrice").removeAttr("disabled");
    }
}

function doSearchMGPP() {
    var map = buildSearchMap();

    var myRequest = {
        contentId: "listeCommunications",
        dataToSend: { search: JSON.stringify([...map]) },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/dossier/recherche",
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: null,
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, tabLoadError);
}

function refreshCorbeille() {
    const optionSelected = $("#dossiersParlementaires").val();

    const path = $("#ajaxCallPath")
        .val()
        .substring(0, $("#ajaxCallPath").val().length - 5);
    window.location.href = path + "/mgpp/dossier?dossierParlementaire=" + optionSelected + "#main_content";
}

function tableChangeEventMgpp($table) {
    if (window.location.toString().includes("suivi/pan")) {
        doRapidFiltrePanTab($table);
    } else {
        var map = buildSearchMap();

        $table.find("input[data-isForm='true'], select[data-isForm='true']").each(function () {
            if (this.value !== undefined) {
                map.set(this.name, this.value);
            }
        });

        // ajout des colonnes de tri à la map
        $table.find("[data-field]").each(function () {
            if ($(this).data("value") !== undefined) {
                map.set($(this).data("field"), $(this).data("value"));
                map.set(
                    `${$(this).data("field")}Order`,
                    $(this).data("order") ? $(this).data("order").toFixed(0) : null
                );
            }
        });

        var isRechercheExperte = $table.data("ajaxurl").endsWith("ajax/mgpp/recherche/experte/resultats");
        var idElement = isRechercheExperte ? "resultList" : $table.attr("id");

        var myRequest = {
            contentId: idElement,
            dataToSend: { search: JSON.stringify([...map]) },
            method: "POST",
            dataType: "html",
            url: $table.data("url"),
            ajaxUrl: $table.data("ajaxurl"),
            isChangeURL: false,
            overlay: $("#" + $table[0].id)
                .find(".overlay")
                .first()
                .attr("id"),
            caller: null,
        };

        callAjaxRequest(myRequest, replaceWithHtmlFunction);
    }
}

function doGlobalSearch() {
    var map = buildSearchMap();

    var myRequest = {
        contentId: "listeCommunicationsGlobale",
        dataToSend: { search: JSON.stringify([...map]) },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/recherche/rechercher",
        isChangeURL: false,
        overlay: null,
        caller: this,
        loadingButton: $("#rechercheCommunicationSubmit"),
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, tabLoadError);
}

function loadOngletDossierMgpp(onglet, me) {
    loadOngletMgpp("/mgpp/dossier/" + $("#idMessage").val() + "/" + onglet, me);
}

function loadOngletDossierSimpleMgpp(onglet, me) {
    loadOngletMgpp("/mgpp/dossierSimple/" + $("#ficheId").val() + "/" + onglet, me);
}

function loadOngletMgpp(targetURL, me) {
    var myRequest = {
        contentId: $(me).attr("aria-controls"),
        dataToSend: "",
        method: "GET",
        dataType: "html",
        url:
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) + targetURL,
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: true,
        overlay: "reload-loader",
        caller: me,
    };

    callAjaxRequest(myRequest, replaceHtmlAndInitDropdown, tabLoadError);
}

var replaceHtmlAndInitDropdown = function replaceHtmlAndInitDropdown(containerID, result) {
    replaceHtmlFunction(containerID, result);
    initDropdown();
};

function chargeOngletDossierMgpp(onglet, me, async) {
    const modale = $("#modale-sauvegarde-dossier");
    modale.attr("data-confirmed", "yes");

    replaceHtmlFunction($(me).attr("aria-controls"), "Chargement");

    var targetURL = "/dossier/" + $("#dossierId").val() + "/" + onglet;
    var myRequest = {
        contentId: $(me).attr("aria-controls"),
        dataToSend: $("#dossierLinkId").serialize() + "&" + $("#idMessage").serialize() + "&isMgpp=true",
        method: "GET",
        async: async,
        dataType: "html",
        url:
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) + targetURL,
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: true,
        overlay: null,
        caller: me,
        extraDatas: {
            targetURL: targetURL + "?" + $("#dossierLinkId").serialize(),
            triggerBtn: $(me),
        },
    };

    callAjaxRequest(myRequest, loadOngletData, tabLoadError);
}

function createFullMandatForm() {
    var targetURL = "admin/mgpp/referenceEPP/mandat/creationMandat";
    var mandatForm = $("#formMandat, #formIdentite").serialize() + "&idParent=" + $("#idParent").val();
    var myRequest = {
        contentId: null,
        dataToSend: mandatForm,
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#basePath").val() + targetURL,
        isChangeURL: false,
        overlay: null,
        caller: this,
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function updateCurrentTabMgpp() {
    const $this = $(".dossier-onglet.dossier-onglet-mgpp.tabulation__item--active");
    if ($this) {
        var targetURL = "/mgpp/dossier/" + $("#idMessage").val() + "/" + $this.attr("data-name");

        $this.removeAttr("onclick");

        $this.on("click", function () {
            pushURLFunction(
                encodeURI(
                    $("#ajaxCallPath")
                        .val()
                        .substring(0, $("#ajaxCallPath").val().length - 5) + targetURL
                )
            );
        });
    }
}

function goToCreationCommunication() {
    var path = $("#ajaxCallPath")
        .val()
        .substring(0, $("#ajaxCallPath").val().length - 5);
    var type = $("#communication-type").val();
    window.location.href = path + "/mgpp/communication/creation?typeEvenement=" + type + "#main_content";
}

function goToCreationCommunicationSuccessive() {
    var path = $("#ajaxCallPath")
        .val()
        .substring(0, $("#ajaxCallPath").val().length - 5);
    var type = $("#communication-select").val();
    var id = $("#idMessage").val();
    if (type != null) {
        window.location.href =
            path + "/mgpp/communication/creation?typeEvenement=" + type + "&idMessagePrecedent=" + id + "#main_content";
    }
}

function changeVersion() {
    const id = $("#idMessage").val();
    const path = $("#ajaxCallPath").val() + "/mgpp/dossier/" + id + "/fiche";

    var myRequest = {
        contentId: "fiche_content",
        dataToSend: {
            version: $("#version-selector").val(),
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: path,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $("#change-version"),
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function saveFiche(diffuser) {
    saveFiche(diffuser, false);
}

function saveFiche(diffuser, goPrevious) {
    saveFiche(diffuser, false, false);
}

function saveFiche(diffuser, goPrevious, redirect) {
    $("#diffuser").val(diffuser);
    var map = new Map();
    const $form = $("#formEditFiche");
    var dataForm = $form.serializeArray();
    if (!isValidForm($form)) {
        return false;
    }
    var doneItem = [];
    for (var i = 0; i < dataForm.length; i++) {
        var curName = dataForm[i].name;
        // Cas générique on gère des strings mais on fait attention qu'on
        // n'est pas dans le cas d'un select multiple avec qu'un enfant
        if (
            !map.has(curName) &&
            $("[name='" + curName + "'][multiple]").length <= 0 &&
            $("[name='" + curName + "'][data-multiple='true']").length <= 0
        ) {
            map.set(curName, dataForm[i].value);
        } else {
            // Sinon c'est un tableau
            // On vérifie qu'on n'a pas déjà traité cet élément
            if (!doneItem.includes(curName)) {
                var values = [];
                // On rajoute l'élément qui avait déjà été stocké dans la
                // map +
                // l'élément courant
                if (map.get(curName)) {
                    values.push(map.get(curName));
                }
                values.push(dataForm[i].value);
                // On nettoie notre map
                map.delete(curName);

                // On va parcourir le reste pour finir de traiter cet item
                if (i + 1 < dataForm.length) {
                    for (var j = i + 1; j < dataForm.length; j++) {
                        if (dataForm[j].name === curName) {
                            values.push(dataForm[j].value);
                        }
                    }
                }
                doneItem.push(curName);
                map.set(curName, values);
            }
        }
    }

    var myRequest = {
        contentId: null,
        dataToSend: {
            communication: JSON.stringify([...map]),
            idFiche: $("#ficheId").val(),
            diffuser: $("#diffuser").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/fiche/sauvegarde",
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: diffuser ? $("#diffuser_fiche") : $("[id^=enregistrer_fiche]"),
    };

    callAjaxRequest(
        myRequest,
        goPrevious ? checkErrorOrGoPrevious : redirect ? checkErrorOrRedirect : checkErrorOrReload,
        displaySimpleErrorMessage
    );
}

function genererCourrier() {
    const valueSelect = $("#courrier_value").val();
    const version = $("#version-selector").val();
    const idMessage = $("#idMessage").val();

    if (isValidInput($("#courrier_value"))) {
        var downloadUrl =
            $("#ajaxCallPath").val() +
            "/mgpp/fiche/genererCourrier?typeComCourrier=" +
            valueSelect +
            "&idCommunication=" +
            idMessage +
            "&version=" +
            version;

        window.open(downloadUrl, "_blank");
    }
}

function doDeleteFiche() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            ficheId: $("#ficheId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/fiche/supprimer",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function addValeurToList(el, widgetName) {
    var inputNode = $(el).closest("div").find("input");
    var listNode = $(el).closest(".rightBlocWidget").find("ul");
    var date_regex = /^(0[1-9]|1\d|2\d|3[01])\/(0[1-9]|1[0-2])\/(19|20)\d{2}$/;
    if ($(inputNode).val().length > 0) {
        if (!widgetName.includes("date") || (widgetName.includes("date") && date_regex.test($(inputNode).val()))) {
            $(listNode).append(
                $("<li/>")
                    .text($(inputNode).val())
                    .append(
                        $("<input/>")
                            .attr("type", "hidden")
                            .attr("name", widgetName)
                            .attr("value", DOMPurify.sanitize($(inputNode).val()))
                            .attr("data-multiple", "true")
                    )
                    .append(
                        $("<span/>")
                            .addClass("icon icon--cross-bold link__icon link__icon--append")
                            .on("click", function () {
                                $(this).closest("li").remove();
                            })
                            .attr("aria-hidden", "true")
                    )
            );
            $(inputNode).val("");
        }
    }
}
function effacerRechercheRapide() {
    $("#rechercheRapideForm")[0].reset();
}

function doPublier() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/publier",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doDeleteCommunication() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/supprimer",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doAnnulerCommunication() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/annuler",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doTraiter() {
    window.location.replace(
        $("#ajaxCallPath")
            .val()
            .substring(0, $("#ajaxCallPath").val().length - 5) +
            "/mgpp/communication/traiter?idMessage=" +
            $("#idMessage").val()
    );
}

function doEnCoursDeTraitement() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/enCoursDeTraitement",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doAccepter() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/accepter",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doRejeter() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/rejeter",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doAccuserReception() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/accuserReception",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doMettreEnAttente(attente) {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
            attente: attente,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/mettreEnAttente",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doAbandonner() {
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + "/mgpp/communication/abandonner",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function addBLocAddPieceJointe(el, pjMultiValue, widgetName, defaultValue) {
    // cacher le bouton si le widget n'est pas multi piece jointe
    if (!pjMultiValue) {
        $(el).hide();
    } else {
        widgetNumber++;
    }

    var widgetLabel = $(el).data("label");
    var widget = pjMultiValue ? widgetName + widgetNumber : widgetName;
    var idDossier = $("#idDossier").val();

    var myRequest = {
        contentId: widgetName + "PjContainer",
        dataToSend: {
            widget: widget,
            widgetName: widgetName,
            widgetLabel: widgetLabel,
            multiValue: pjMultiValue,
            idDossier:
                idDossier.length > 0 && idDossier !== "null" ? idDossier : $("form#formEditCommunication #nor").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl:
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) + "/mgpp/communication/buildBlocPJ",
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, appendBlocPJ, tabLoadError);
}

var appendBlocPJ = function rappendBlocPJinContainer(containerID, result) {
    $("#" + containerID).append(result);
    initInterstitial();
};

function saveNouvelleIdentite() {
    if (isValidForm($("#modalNouvelleIdentiteForm"))) {
        var myRequest = {
            contentId: null,
            dataToSend: $("#modalNouvelleIdentiteForm").serialize(),
            method: "POST",
            dataType: "html",
            ajaxUrl: $("#basePath").val() + "/admin/mgpp/referenceEPP/mandat/nouvelleIdentite",
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}

function doExportEchPromulgation(path) {
    var url = $("#basePath").val() + "mgpp/calendrier/export/" + path;
    window.open(url, "_blank");
}

function loadSelectTableRef(id) {
    var orgaID = "orga-" + id;
    if ($("#" + orgaID + " :only-child").hasClass("overlay")) {
        var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/referenceEPP/currentGouv";
        const myRequest = {
            contentId: orgaID,
            dataToSend: { selectedID: id },
            method: "GET",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "overlay-" + id,
            caller: this,
        };

        callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
    }
}

function onOpenTableReferenceNode(selectedNode) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/referenceEPP";
    var myRequest = {
        contentId: "tableReferenceTree",
        dataToSend: "selectedNode=" + selectedNode,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "overlay_table_reference_tree",
        caller: $(event.target).is(":button") ? $(event.target) : $(event.target).parent(),
    };

    callAjaxRequest(myRequest, loadOrganigrammeTreeData);
}

function verrouilleFiche() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/verrouiller";
    var myRequest = {
        contentId: null,
        dataToSend: $("#ficheId").serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function deverrouilleFiche() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/deverrouiller";
    var myRequest = {
        contentId: null,
        dataToSend: $("#ficheId").serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doExportFiche(path) {
    var url = $("#basePath").val() + "mgpp/dossier/export/" + path + "/" + $("#ficheId").val();
    window.open(url, "_blank");
}

function doExportSuiviEcheances(path) {
    var url = $("#basePath").val() + "mgpp/calendrier/suivi/export/" + path;
    window.open(url, "_blank");
}

function creerElementRepresentantsTableau(typeRepresentant) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/representant/" + typeRepresentant + "/creer";
    var modale = $("#mgppModaleEdit");

    var myRequest = {
        contentId: modale.find(".interstitial__content > div").first().attr("id"),
        dataToSend: {},
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function chargerRepresentantsItem(item) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/representant/" + item + "/charger";
    var modale = $("#mgppModaleEdit");

    var myRequest = {
        contentId: modale.find(".interstitial__content > div").first().attr("id"),
        dataToSend: {},
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function editerElementRepresentantsTableau(item) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/representant/editer";
    var form = $("#content-mgppModaleEdit").find("form");
    var target = $("#typeRepresentant").val() + "Tableau";
    form.find('input[name="idFiche"]').val($("#ficheId").val());

    var myRequest = {
        contentId: target,
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function supprimerElementRepresentantsTableau() {
    var idRepresentant = $("#idRepresentantASupprimer").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/representant/" + idRepresentant + "/supprimer";
    var target = $("#typeRepresentant").val() + "Tableau";

    var myRequest = {
        contentId: target,
        dataToSend: { idFiche: $("#ficheId").val() },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function goDossiersParlementairesHome() {
    showReloadLoader();
    window.location.href = $("#basePath").val() + "mgpp/dossier#main_content";
}

function creerDossierPourTraiter(poste = "") {
    if (isValidFolder()) {
        var idCommunication = $("#idCommunicationATraiter").val();
        var typeActe = getSelectedTypeActe();
        var entite = $("#rbt-ministere-direction").prop("checked") === true ? $("#entite").val() : "";
        var direction = $("#rbt-ministere-direction").prop("checked") === true ? $("#dirConcernee").val() : "";
        var norPrm = $("#rbt-nor").prop("checked") === true ? $("#norPrm").val() : "";
        var norCopie = $("#norCopie").val();
        var norRectificatif = $("#norRectificatif").val();
        // Données des autres formulaires
        var mesuresTransposition = getMesuresTransposition();
        var mesuresApplication = getMesuresApplication();
        var ordonnance = getOrdonnance();

        var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/dossier/creer";
        var myRequest = {
            contentId: null,
            dataToSend: {
                idCommunication: idCommunication,
                poste: poste,
                typeActe: typeActe,
                entite: entite,
                direction: direction,
                norPrm: norPrm,
                norCopie: norCopie,
                norRectificatif: norRectificatif,
                mesuresTransposition: mesuresTransposition,
                mesuresApplication: mesuresApplication,
                ordonnance: ordonnance,
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            loadingButton: poste === "" ? $("#btn-create-dossier") : null,
            overlay: poste === "" ? null : "reload-loader",
        };

        window.scrollTo(0, 0);
        callAjaxRequest(myRequest, checkErrorOrShowModalOrGoPrevious, displaySimpleErrorMessage);
    }
}

function setPosteAndCreerDossierPourTraiter() {
    if (isValidPoste()) {
        var poste;
        if ($("#rbt-poste").prop("checked") === true) {
            poste = $("#poste").val();
        } else if ($("#rbt-poste-defaut").prop("checked") === true) {
            poste = $("#poste-defaut").val();
        }
        creerDossierPourTraiter(poste);
    }
}

function checkErrorOrShowModalOrGoPrevious(contentId, result, caller, extraDatas, xhr) {
    var jsonResponse = JSON.parse(result);
    var data = jsonResponse.data;
    var dataJson = JSON.parse(data);
    if (dataJson && dataJson.isValidPoste) {
        var messagesContaineur = jsonResponse.messages;
        if (messagesContaineur.dangerMessageQueue.length > 0) {
            constructAlertContainer(messagesContaineur.dangerMessageQueue);
        } else {
            goPreviousPage();
        }
    } else {
        // si data vaut false : afficher la modale de choix du poste
        var modal = $("#modal-choix-poste");
        openModal(modal[0]);
        modal.find("button")[0].focus();
    }
}

function rattacherDossier() {
    var nor = $("#norRattachement").val();
    var idCommunication = $("#idCommunicationATraiter").val();

    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/dossier/rattacher";
    var myRequest = {
        contentId: null,
        dataToSend: {
            nor: nor,
            idCommunication: idCommunication,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        loadingButton: $("#btn-rattacher-dossier"),
        overlay: null,
    };

    window.scrollTo(0, 0);
    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function traiterSansDossier() {
    var idCommunication = $("#idCommunicationATraiter").val();

    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/dossier/traiterSansDossier";
    var myRequest = {
        contentId: null,
        dataToSend: {
            idCommunication: idCommunication,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,

        loadingButton: $("#btn-non-rattachement"),
        overlay: null,
    };

    window.scrollTo(0, 0);
    callAjaxRequest(myRequest, checkErrorOrGoPrevious, displaySimpleErrorMessage);
}

function doAddDocumentFondDeDossierMgpp() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/ajout/fichier/fdd";

    var filesCtl = getFilesFromFileInput("fddMgppDocumentFileAdd");

    var formData = new FormData();

    formData.append("ficheId", encodeURIComponent($("#ficheId").val()));

    filesCtl.forEach(function (inp) {
        formData.append("documentFile", inp);
    });

    var myRequest = {
        contentId: null,
        dataToSend: formData,
        processData: false,
        contentType: false,
        enctype: "multipart/form-data",
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
        loadingButton: $("#btn-confirm-add-doc"),
    };
    closeModal($("#mgpp-modal-add-document-fdd").get(0));
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doDeleteDocumentFondDeDossierMgpp() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/suppression/fichier/fdd";
    var myRequest = {
        contentId: null,
        dataToSend: {
            documentIdToDelete: $("#documentIdToDelete").val(),
            ficheId: $("#ficheId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-validation-dialog-modal"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doLierOrganismeOepMgpp() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/communication/lier/oep";
    var myRequest = {
        contentId: null,
        dataToSend: {
            idMessage: $("#idMessage").val(),
            organismeOEP: $("#organismeOEP").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#btn-confirm-mgpp-modal-lier-oep"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function annulerDiffusionFiche() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/fiche/annuler/diffusion";
    var myRequest = {
        contentId: null,
        dataToSend: {
            ficheId: $("#ficheId").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function exportListeDossier(format) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/mgpp/dossier/export/" + format;
    var map = buildSearchMap();

    var myRequest = {
        contentId: null,
        dataToSend: { search: JSON.stringify([...map]) },
        method: "POST",
        ajaxUrl: ajaxUrl,
        xhrFields: {
            responseType: "blob", // to avoid binary data being mangled on charset conversion
        },
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, downloadFileWithoutMessage, displaySimpleErrorMessage);
}

function doSaveIdentite() {
    const $form = $("#formNouvelleIdentite");
    if (isValidForm($form)) {
        const ajaxUrl =
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) +
            "/admin/mgpp/referenceEPP/mandat/sauvegardeNouvelleIdentite";

        const myRequest = {
            contentId: null,
            dataToSend: $form.serialize(),
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
    }
}
