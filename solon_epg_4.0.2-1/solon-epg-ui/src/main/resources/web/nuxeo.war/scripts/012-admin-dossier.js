function retirerList() {
    var myTable = $(".tableForm").first();
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });
    var ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/suppression/retirer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            idDossiers: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function openModalAdminSuppressionDossier() {
    const myTable = $(".tableForm").first();
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/suppression/selectionner";
    const myRequest = {
        contentId: "suppression-dossiers-summary-content",
        dataToSend: {
            idDossiers: data,
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndSetupModal, displaySimpleErrorMessage);
}

function validerList() {
    const form = $("#valider_liste_dossier_form");
    if (isValidForm(form)) {
        var ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/suppression/valider";
        var myRequest = {
            contentId: null,
            method: "POST",
            dataToSend: {
                username: $("#form_login_username").val(),
                data: $("#form_login_password").val(),
                idDossiers: getSelectedContent(),
            },
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: "reload-loader",
        };

        callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
    }
}

function exportDossierSupprimer() {
    var ajaxUrl =
        $("#ajaxCallPath").val() +
        "/admin/dossiers/suppression/exporter?tab=" +
        $(".tabulation__item--active").data("name");
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

function uploadPJsThenSaveParapheurs(pieceJointes, result, urlTarget) {
    var curPieceJointe = pieceJointes.shift();
    if (curPieceJointe != null) {
        var files = curPieceJointe.files;
        if (files.length) {
            uploadFiles(
                files,
                null,
                (batchId) => {
                    result.push({
                        pieceId: curPieceJointe.pieceId,
                        uploadBatchId: batchId,
                    });
                    uploadPJsThenSaveParapheurs(pieceJointes, result, urlTarget);
                },
                displaySimpleErrorMessage
            );
        } else {
            uploadPJsThenSaveParapheurs(pieceJointes, result, urlTarget);
        }
    } else {
        saveParapheursFinale(result, urlTarget);
    }
}

function saveParapheursFinale(result, urlTarget) {
    let parapheurs = [];
    $("fieldset.parapheur-item").each(function () {
        const enumFolder = $(this).find("input[name='parapheurFolderType']").first().val();

        let parapheur = {
            estNonVide: $(this).find("input[name^='estNonVide']:checked").first().val(),
            nbDocAccepteMax: $(this).find("input[name='nbDocAccepteMax']").first().val(),
            feuilleStyleFiles: getIdBatchByEnumFolder(result, enumFolder),
            formatsAutorises: $(this)
                .find("[name='formatsAutorises[]']")
                .map(function () {
                    return $(this).val();
                })
                .get(),
            parapheurFolderLabel: enumFolder,
            parapheurFolderId: enumFolder,
        };
        parapheurs.push(JSON.stringify(parapheur));
    });

    const myRequest = {
        contentId: null,
        dataToSend: {
            parametreParapheurFormCreations: parapheurs,
            typeActe: $("#TypeActeSaved").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: urlTarget,
        isChangeURL: false,
        loadingButton: $(".ACTION_ENREGISTRER_PARAPHEURS"),
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function retirerDossierAbandon() {
    var myTable = $(".tableForm").first();
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });
    var ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/abandon/retirer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            idDossiers: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function getIdBatchByEnumFolder(result, enumFolder) {
    let batchs = [];
    result.forEach(function (file) {
        if (file.pieceId == enumFolder) {
            batchs.push(file.uploadBatchId);
        }
    });
    return batchs;
}

function enregistrerParapheurs() {
    const formParapheur = $("#parapheurs-form");
    let pieceJointes = [];
    const formIsValid = isValidForm(formParapheur);
    const optinIsValid = isValidOptinParapheur(formParapheur);
    if (formIsValid && optinIsValid) {
        const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/parapheur/enregistrer";
        $("form#parapheurs-form input[type=file]").each(function () {
            if ($(this)[0].files.length) {
                pieceJointes.push({
                    pieceId: $(this).attr("name").split("-file")[0],
                    files: $(this)[0].files,
                });
            }
        });

        uploadPJsThenSaveParapheurs(pieceJointes, [], ajaxUrl);
    }
}

function isValidOptinParapheur(formParapheur) {
    let isValid = true;
    $("fieldset.parapheur-item").each(function () {
        const $input = $(this).find("input[name^='estNonVide']").first();
        const val = $(this).find("input[name^='estNonVide']:checked").first().val();
        cleanPreviousErrorOrSuccess($input);
        if (val === undefined || !new RegExp(/([^\s])/).test(val)) {
            $input.focus();
            constructErrors(["Le champ est requis."], $input);
            isValid = false;
        }
    });
    return isValid;
}

function editerParapheurs() {
    const typeActe = $("#typeActe").val();
    if (typeActe) {
        const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/parapheur";
        const myRequest = {
            contentId: "parapheursContent",
            dataToSend: {
                typeActe: typeActe,
            },
            method: "GET",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndEnableSave, displaySimpleErrorMessage);
    }
}

function supprimerFichierParapheur(button, batchID) {
    $("#" + batchID + "-file").hide();

    const contentHidden = $(button).parents("fieldset.parapheur-item").get(0);

    $("<input>")
        .attr({
            type: "hidden",
            id: "deleted-file-" + batchID,
            name: "deleted-file",
            value: batchID,
            "aria-hidden": "true",
        })
        .appendTo(contentHidden);
}

function replaceHtmlFunctionAndEnableSave(elementID, html, extraDatas) {
    $(".ACTION_ENREGISTRER_PARAPHEURS").each(function () {
        $(this).removeAttr("disabled");
    });
    replaceHtmlFunctionAndInitSelectAndInitValidation(elementID, html, extraDatas);
}

function exportDossierAbandon() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/abandon/exporter";
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

function retirerDossiersArchivageDefinitif() {
    var myTable = $(".tableForm").first();
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });
    var ajaxUrl = $("#ajaxCallPath").val() + "/admin/archivage/retirer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            idDossiers: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function genererArchive() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/admin/archivage/genererArchive";
    var myRequest = {
        contentId: null,
        dataToSend: null,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function exportDossierArchivageDefinitif() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/admin/archivage/exporter";
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

$(document).ready(function () {
    $("#typeActeFdd").bind("DOMNodeInserted", function () {
        $("#button-edit-fdd").removeAttr("disabled");
    });
    $("#typeActeFdd").bind("DOMNodeRemoved", function () {
        $("#button-edit-fdd").attr("disabled", "disabled");
    });
    updateCurrentTabSuppressionDossier();
    if ($("#listeDossierSuppressionconsultation table tr td").length === 0) {
        $(".ACTION_VALIDER_DOSSIERS").attr("disabled", "disabled");
    }
});

function chargerFdd() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/fdd/charger/" + $("#typeActeFdd").val();
    const myRequest = {
        contentId: "fdd_content",
        dataToSend: {},
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "overlay_fdd",
        extraDatas: {
            elementToFocus: $("#fdd_titre"),
        },
    };
    callAjaxRequest(myRequest, replaceHtmlFunctionAndFocusResult, displaySimpleErrorMessage);
}

function initFddModalAddIn(id) {
    initFddModal(id, "in");
}

function initFddModalAddAfter(id) {
    initFddModal(id, "after");
}

function initFddModalAddBefore(id) {
    initFddModal(id, "before");
}

function initFddModalEdit(params) {
    const arrParams = params.split(";");
    const id = arrParams[0].split("=")[1];
    initFddModal(id, "");
    const nom = arrParams[1].split("=")[1];
    $("#fdd-name").val(nom);
}

function initFddModal(docId, pos) {
    cleanAllPreviousErrorOrSuccess();
    $("#fdd-name").val("");
    $("#fdd-ref").val(docId);
    $("#fdd-position").val(pos);
}

function updateFdd(endpoint) {
    if (!isValidForm($("#form-fdd"))) {
        return false;
    }

    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/fdd/" + endpoint;
    let data = $("#form-fdd").serialize();
    data = data + "&typeActe=" + $("#fdd-typeActe").val();
    const myRequest = {
        contentId: "fdd_content",
        dataToSend: data,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "overlay_fdd",
    };
    callAjaxRequest(myRequest, replaceHtmlFunctionShowToastAndFocus, displaySimpleErrorMessage);
    closeModal($("#admin-fdd-modal").get(0));
}

var replaceHtmlFunctionShowToastAndFocus = function replaceHtmlFunctionShowToastAndFocus(container, result) {
    replaceHtmlFunctionAndShowToast(container, result);
    initFormValidation();
    const id = $("#fdd-ref").val();
    const btn = $(
        "#fdd_content .tree-navigation__item[data-id='" + id + "'] .tree-navigation__button-ellipsis button:first"
    );
    btn.focus();
};

function ajouterFdd() {
    updateFdd("ajouterNoeud");
}

function editerFdd() {
    updateFdd("editerNoeud");
}

function supprimerFdd() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/fdd/supprimerNoeud";
    const myRequest = {
        contentId: "fdd_content",
        dataToSend: {
            ref: $("#ref").val(),
            typeActe: $("#fdd-typeActe").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "overlay_fdd",
    };
    callAjaxRequest(myRequest, replaceHtmlFunctionAndShowToast, displaySimpleErrorMessage);
}

function enregistrerFdd() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/fdd/enregistrer";
    const myRequest = {
        contentId: null,
        dataToSend: {
            formatsAcceptes: $("#formatsAcceptesFdd").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("main .base-btn--submit"),
    };
    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function chargeOngletDossiersSupprimer(onglet, me) {
    var targetURL = "admin/dossiers/suppression?tab=" + onglet;
    var myRequest = {
        contentId: $(me).attr("aria-controls"),
        dataToSend: {},
        method: "GET",
        async: true,
        dataType: "html",
        url: $("#basePath").val() + targetURL,
        ajaxUrl: $("#ajaxCallPath").val() + "/admin/dossiers/suppression/onglet?tab=" + onglet,
        isChangeURL: true,
        overlay: null,
        caller: me,
        extraDatas: {
            targetURL: "/" + targetURL,
        },
    };

    callAjaxRequest(myRequest, loadOngletDataSuppressionDossier, tabLoadError);
}

var loadOngletDataSuppressionDossier = function loadOnglet(containerID, result, caller, extraDatas) {
    replaceHtmlFunction(containerID, result);
    $(caller).removeAttr("onclick");
    $(caller).removeAttr("data-script");

    var targetURL = extraDatas.targetURL;
    $(caller).on("click", function (evt) {
        return updateOngletDataSuppressionDossier(evt, targetURL);
    });

    initAsyncSelect();
    updateCurrentTabSuppressionDossier();
};

function updateCurrentTabSuppressionDossier() {
    const $this = $(".dossier-suppression-onglet.tabulation__item--active");
    if ($this) {
        var targetURL = "admin/dossiers/suppression?tab=" + $this.attr("data-name");

        $this.removeAttr("onclick");

        $this.on("click", function (evt) {
            return updateOngletDataSuppressionDossier(evt, targetURL);
        });
    }
}

function updateOngletDataSuppressionDossier(evt, targetURL) {
    var encode = encodeURI($("#basePath").val() + targetURL);
    pushURLFunction(encode);
}

function ajouterVecteurPublication() {
    const form = $("#vecteurs-publication-form");
    if (!isValidForm(form)) return;
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/vecteurs/sauvegarde";
    const myRequest = {
        contentId: "listeVecteurs",
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#ajouter-vecteur-publication"),
    };
    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function initModalVecteurPublication(paramInitJs) {
    var paramArray = paramInitJs.split(";");
    for (var i = 0; i < paramArray.length; i++) {
        var elts = paramArray[i].split("=");
        var paramName = elts[0];
        var paramValue = elts[1] === "null" ? "" : elts[1];
        // set values
        $("#" + paramName).val(paramValue);
    }
}

function editVecteurPublication() {
    const modal = $("#vecteurs-publication-modal");
    if (!isValidForm(modal)) return;
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/vecteurs/sauvegarde";
    const myRequest = {
        contentId: "listeVecteurs",
        dataToSend: {
            id: $("#id").val(),
            vecteurPublicationJO: $("#vecteurPublicationJOModal").val(),
            dateDebut: $("#dateDebutModal").val(),
            dateFin: $("#dateFinModal").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
    closeModal(modal.get(0));
}

function ajouterPublicationMinisterielle() {
    const form = $("#publication-ministerielle-form");
    if (!isValidForm(form)) return;
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/vecteurs/publication/sauvegarde";
    const myRequest = {
        contentId: "listePublications",
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
        loadingButton: $("#ajouter-publication-ministerielle"),
    };
    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function supprimerPublicationMinisterielle() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/vecteurs/publication/supprimer";
    const myRequest = {
        contentId: "listePublications",
        dataToSend: {
            id: $("#id").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function initModalModeParution(paramInitJs) {
    if (paramInitJs === undefined) {
        // ajout du mode de parution : reset champs
        $("#modeParution").val("");
        $("#dateDebut").val("");
        $("#dateFin").val("");
    } else {
        // modification du mode de parution
        // on initialise les champs
        var paramArray = paramInitJs.split(";");
        for (var i = 0; i < paramArray.length; i++) {
            var elts = paramArray[i].split("=");
            var paramName = elts[0];
            var paramValue = elts[1];
            // set values
            if (paramValue !== "null") {
                $("#" + paramName).val(paramValue);
            } else {
                $("#" + paramName).val("");
            }
        }
    }
}

function createModeParution() {
    const modal = $("#mode-parution-modal");
    if (!isValidForm(modal)) return;
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/table/reference/mode/parution/sauvegarder";
    const myRequest = {
        contentId: "listeModeParution",
        dataToSend: {
            mode: $("#modeParution").val(),
            dateDebut: $("#dateDebut").val(),
            dateFin: $("#dateFin").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
    closeModal(modal.get(0));
}

function editModeParution() {
    const modal = $("#mode-parution-modal");
    if (!isValidForm(modal)) return;
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/table/reference/mode/parution/sauvegarder";
    const myRequest = {
        contentId: "listeModeParution",
        dataToSend: {
            id: $("#id").val(),
            mode: $("#modeParution").val(),
            dateDebut: $("#dateDebut").val(),
            dateFin: $("#dateFin").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
    closeModal(modal.get(0));
}

function deleteModeParution() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/table/reference/mode/parution/supprimer";
    const myRequest = {
        contentId: "listeModeParution",
        dataToSend: {
            id: $("#id").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction, displaySimpleErrorMessage);
}

function saveTableReference() {
    var signatairesLibres = [];
    $(".form-terms__terms__el")
        .children("span")
        .each(function () {
            signatairesLibres.push($(this).text());
        });

    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/table/reference/sauvegarder";
    const myRequest = {
        contentId: null,
        dataToSend: $("#table-reference-form").serialize() + "&signatairesLibres=" + signatairesLibres,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function doAddSignataire() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/table/reference/signataire/ajouter";
    const myRequest = {
        contentId: "input-list-signatairesLibres",
        dataToSend: {
            signataire: $("#form_input_liste_signatairesLibres").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, displaySimpleErrorMessage);
}

function doRemoveSignataire() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/admin/dossiers/table/reference/signataire/supprimer";
    const myRequest = {
        contentId: "input-list-signatairesLibres",
        dataToSend: {
            id: $("#idSignataire").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, displaySimpleErrorMessage);
}
