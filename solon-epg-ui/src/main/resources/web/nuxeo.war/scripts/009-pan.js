function initPan() {
    updateCurrentPanTab(".pan-onglet");
    updateCurrentPanTab(".pan-sous-onglet");
    updateCurrentContextualPanTab(".pan-onglet-contextuel");

    $(".ACTION_TEXTE_MAITRE_ADD").attr("disabled", "disabled");

    $("#addTexteMaitreNor").bind("DOMNodeInserted", function () {
        $(".ACTION_TEXTE_MAITRE_ADD").removeAttr("disabled");
    });
    $("#addTexteMaitreNor").bind("DOMNodeRemoved", function () {
        $(".ACTION_TEXTE_MAITRE_ADD").attr("disabled", "disabled");
    });

    $("#panStatistiquesForm #legislatures")
        .parent()
        .find("input")
        .blur(function () {
            // l'ajout de legislatures disponibles va entrainer la mise à jour du select de legislature choisie
            var legisDispo = $("#legislatures > option[selected=selected]")
                .map(function () {
                    return DOMPurify.sanitize($(this).val());
                })
                .get();

            var legisProp = $("#legislatureEnCours > option")
                .filter((f) => f > 0)
                .map(function () {
                    return $(this).val();
                })
                .get();

            legisDispo.forEach(function (i) {
                if (!legisProp.includes(i)) {
                    $("#legislatureEnCours").append($("<option>", { value: i, text: i }));
                }
            });
        });

    var currentSection = $("#currentSection").val();
    if (currentSection == "directives") {
        $("#addTexteMaitreNor").on("input change", function () {
            // pas d'auto-complétion pour l'ajout par numéro de directive, mais du coup on empêche quand même l'utilisateur de pouvoir saisir un champ vide
            if ($(this).val() != "") {
                $(".ACTION_TEXTE_MAITRE_ADD").removeAttr("disabled");
            } else {
                $(".ACTION_TEXTE_MAITRE_ADD").attr("disabled", "disabled");
            }
        });
    }
}

function updateOngletPanData(evt, targetURL, onglet) {
    var encode = encodeURI(
        $("#basePath")
            .val()
            .substring(0, $("#basePath").val().length - 1) + targetURL
    );
    pushURLFunction(encode);
    $("#currentPanTab").val(onglet); // nécessaire pour charger les sous-onglets

    // Si on change d'onglet contextuel, on cache le bloc contextuel de l'ancien onglet et on affiche celui de l'onglet cible
    var blocs = $("[id^=blocContextuel-]");
    blocs.hide();
    $("#blocContextuel-" + onglet).show();

    //On met à jour le contexte et l'id contextuel pour pouvoir charger les sous-onglets contextuels correctement si nécessaire
    var id = "";
    var contexte = "";
    if (targetURL.includes("/textemaitre/")) {
        contexte = "textemaitre";
    } else if (targetURL.includes("/ministere/")) {
        contexte = "ministere";
    }
    if (contexte.length > 0) {
        var res = targetURL.split(contexte + "/");
        if (res.length > 1) {
            res = res[1].split("/");
            id = res[0];
        }
    }
    $("#idContextuel").val(id);
    $("#panContext").val(contexte);
}

function updateOngletContextuelPanData(evt, targetURL, onglet) {
    updateOngletPanData(evt, targetURL, onglet);
    updateCurrentPanTab(".pan-onglet"); // on change l'URL associé à l'onglet parent
}

function updateCurrentPanTab(ongletClass) {
    // A appeler au chargement du premier onglet afin d'appeler la fonction
    // updateOngletPanData lorsqu'on recliquera éventuellement sur l'onglet
    const $this = $(ongletClass + ".tabulation__item--active");
    var basePath = $("#basePath").val();
    var currentTab = $("#currentPanTab").val();
    if ($this && $this.length > 0) {
        var targetURL = "/" + window.location.pathname.replace(basePath, "");

        $this.removeAttr("onclick");
        $this.off("click");

        $this.on("click", function (evt) {
            return updateOngletPanData(evt, targetURL, currentTab);
        });
    }
    initAsyncSelect();
}

function updateCurrentContextualPanTab(ongletClass) {
    // A appeler au chargement du premier onglet afin d'appeler la fonction
    // updateOngletContextuelPanData lorsqu'on recliquera éventuellement sur l'onglet
    var currentTab = $("#currentPanTab").val();
    var currentBlocContextuel = $("#blocContextuel-" + currentTab);
    var currentContextualSubtab = currentBlocContextuel.find(".pan-onglet-contextuel.tabulation__item--active");
    var basePath = $("#basePath").val();
    if (currentContextualSubtab && currentContextualSubtab.length > 0) {
        var targetURL = "/" + window.location.pathname.replace(basePath, "");

        currentContextualSubtab.removeAttr("onclick");
        currentContextualSubtab.off("click");

        currentContextualSubtab.on("click", function (evt) {
            return updateOngletContextuelPanData(evt, targetURL, currentTab);
        });
    }
}

var loadOngletPanData = function loadOnglet(containerID, result, caller, extraDatas) {
    replaceHtmlFunction(containerID, result);
    $(caller).removeAttr("onclick");
    $(caller).removeAttr("data-script");

    var targetURL = extraDatas.targetURL;
    var onglet = extraDatas.onglet;
    $(caller).on("click", function (evt) {
        return updateOngletPanData(evt, targetURL, onglet);
    });

    updateCurrentPanTab(".pan-sous-onglet");
    $("#blocContextuel-" + onglet).show();
};

var loadSousOngletPanData = function loadSousOnglet(containerID, result, caller, extraDatas) {
    replaceHtmlFunction(containerID, result);
    $(caller).removeAttr("onclick");
    $(caller).removeAttr("data-script");

    var targetURL = extraDatas.targetURL;
    var onglet = extraDatas.onglet;
    $(caller).on("click", function (evt) {
        return updateOngletPanData(evt, targetURL, onglet);
    });
};

var loadOngletContextuelPanData = function loadOngletContextuel(containerID, result, caller, extraDatas) {
    replaceHtmlFunction(containerID, result);
    $(caller).removeAttr("onclick");
    $(caller).removeAttr("data-script");

    var targetURL = extraDatas.targetURL;
    var onglet = extraDatas.onglet;
    $(caller).on("click", function (evt) {
        return updateOngletContextuelPanData(evt, targetURL, onglet);
    });
    updateCurrentPanTab(".pan-onglet");
};

function chargeOngletPan(onglet, sousOnglet, me, async) {
    var section = $("#currentSection").val();

    if (sousOnglet == "") {
        var targetURL = "/suivi/pan/" + section + "/" + onglet;
        var ajaxURL = $("#ajaxCallPath").val() + targetURL;
    } else {
        var targetURL = "/suivi/pan/" + section + "/" + onglet + "/" + sousOnglet;
        var ajaxURL = $("#ajaxCallPath").val() + targetURL + "/loadtab";
    }

    var myRequest = {
        contentId: $(me).attr("aria-controls"),
        method: "GET",
        async: async,
        dataType: "html",
        url:
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) + targetURL,
        ajaxUrl: ajaxURL,
        isChangeURL: true,
        overlay: null,
        caller: me,
        extraDatas: {
            targetURL: targetURL,
            onglet: onglet,
        },
    };
    var blocs = $("[id^=blocContextuel-]");
    blocs.hide();
    $("#currentPanTab").val(onglet); // nécessaire pour charger les sous-onglets

    callAjaxRequest(myRequest, loadOngletPanData, tabLoadError);
}

function chargeSousOngletPan(sousOnglet, me, async) {
    var section = $("#currentSection").val();
    var onglet = $("#currentPanTab").val();
    var targetURL = "/suivi/pan/" + section + "/" + onglet + "/" + sousOnglet;

    var myRequest = {
        contentId: $(me).attr("aria-controls"),
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
            targetURL: targetURL,
            onglet: onglet,
        },
    };

    callAjaxRequest(myRequest, loadSousOngletPanData, tabLoadError);
}

function chargeOngletContextuelPan(ongletContextuel, me, async) {
    var section = $("#currentSection").val();
    var onglet = $("#currentPanTab").val();
    var contexte = $("#panContext").val();
    var id = $("#idContextuel").val();
    var targetURL = panUrlContextuel() + id + "/" + ongletContextuel;

    var myRequest = {
        contentId: $(me).attr("aria-controls"),
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
            targetURL: targetURL,
            onglet: onglet,
        },
    };

    callAjaxRequest(myRequest, loadOngletContextuelPanData, tabLoadError);
}

function chargerBlocContextuel(el, id, contexte) {
    var onglet = $("#currentPanTab").val();
    $("#panContext").val(contexte);
    var targetURL = panUrlContextuel() + id;

    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        url:
            $("#ajaxCallPath")
                .val()
                .substring(0, $("#ajaxCallPath").val().length - 5) + targetURL,
        isChangeURL: onglet !== "recherche-an",
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: contexte,
            idContextuel: id,
        },
    };

    $(el).closest("tbody").find("tr").removeClass("table-line--selected");
    $(el).closest("tr").addClass("table-line--selected");

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function panUrlContextuel() {
    var section = $("#currentSection").val();
    var onglet = $("#currentPanTab").val();
    var contexte = $("#panContext").val();
    var URL = "/suivi/pan/" + section + "/" + onglet + "/" + contexte + "/";
    return URL;
}

var callbackBlocContextuel = function callbackBlocContextuel(containerID, result, caller, extraDatas) {
    $("#panContext").val(extraDatas.contexte);
    $("#idContextuel").val(extraDatas.idContextuel);
    var container = $("#" + containerID);
    if (container.length) {
        container.remove();
    }
    appendHtmlFunction("blocContextuel", result);
    $(extraDatas.hash).attr("tabindex", -1).focus();
    updateCurrentContextualPanTab(".pan-onglet-contextuel");
    updateCurrentPanTab(".pan-onglet"); // on change l'URL associé à l'onglet parent
    initAsyncSelect(); // pour l'autocomplete du ministere pilote
};

var callbackBlocContextuelAndScrollToAlerts = function callbackBlocContextuelAndScrollToAlerts(
    containerID,
    result,
    caller,
    extraDatas
) {
    callbackBlocContextuel(containerID, result, caller, extraDatas);
    setTimeout(function () {
        var alerts = $(".alert");
        if (alerts.length) {
            alerts.get(0).scrollIntoView();
        }
    }, 0);
};

function chargerDetails(el) {
    var idPremierTableau = $(el).closest("tr").data("id");
    if (idPremierTableau != undefined) {
        var idTexteMaitre = $("#idContextuel").val();
        var targetURL = panUrlContextuel() + idTexteMaitre + "/texte-maitre/" + idPremierTableau;

        var myRequest = {
            contentId: "secondTableau",
            method: "GET",
            dataType: "html",
            ajaxUrl: $("#ajaxCallPath").val() + targetURL,
            extraDatas: {
                hash: "#texteMaitreSecondTableau",
                targetURL: targetURL,
            },
            url:
                $("#ajaxCallPath")
                    .val()
                    .substring(0, $("#ajaxCallPath").val().length - 5) + targetURL,
            isChangeURL: true,
            overlay: "reload-loader",
        };
        $(el).closest("tbody").find("tr").removeClass("table-line--selected");
        $(el).closest("tr").addClass("table-line--selected");
        callAjaxRequest(myRequest, replaceHtmlFunctionSecondTableau, displaySimpleErrorMessage);
    }
}

var replaceHtmlFunctionSecondTableau = function replaceHtmlFunctionSecondTableau(
    containerID,
    result,
    caller,
    extraDatas
) {
    replaceHtmlFunction(containerID, result);
    $(extraDatas.hash).attr("tabindex", -1).focus();
    updateCurrentContextualPanTab(".pan-onglet-contextuel");
    updateCurrentPanTab(".pan-onglet"); // on change l'URL associé à l'onglet parent
};

function supprimerTexteMaitre(el) {
    var section = $("#currentSection").val();
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#supprEltTableauMaitreId").val();
    var ongletURL = "suivi/pan/" + section + "/" + onglet;
    var targetURL = "/" + ongletURL + "/supprimer/" + idTexteMaitre;
    var myRequest = {
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            redirectUrl: $("#basePath").val() + ongletURL,
        },
    };
    callAjaxRequest(myRequest, checkErrorOrGoToUrl, displaySimpleErrorMessage);
}

function supprimerTexteMaitreRechercheAn(el) {
    var section = $("#currentSection").val();
    var idTexteMaitre = $("#supprEltTableauMaitreId").val();
    var targetURL = "/suivi/pan/" + section + "/recherche/experte/supprimer/" + idTexteMaitre;
    var myRequest = {
        contentId: "resultList",
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function ajouterTexteMaitre() {
    var onglet = $("#currentPanTab").val();
    var section = $("#currentSection").val();
    var nor = $("#addTexteMaitreNor").val();
    var targetURL = "/suivi/pan/" + section + "/" + onglet + "/ajouter/" + encodeURIComponent(nor);
    var myRequest = {
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function verrouillerTexteMaitre() {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    var idPremierTableau = $("#idFirstTable").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/texte-maitre/verrouiller";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        dataToSend: {
            idPremierTableau: idPremierTableau, // pour le rechargement du tableau des décrets
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
            idPremierTableau: idPremierTableau,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function deverrouillerTexteMaitre() {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    var idPremierTableau = $("#idFirstTable").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/texte-maitre/deverrouiller";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "POST",
        dataType: "html",
        dataToSend: {
            idPremierTableau: idPremierTableau, // pour le rechargement du tableau des décrets
        },
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
            idPremierTableau: idPremierTableau,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function enregistrerTexteMaitre(el) {
    var $form = $("#texte-maitre-form");
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/texte-maitre/sauvegarder";
    var data = $form.serialize();
    if ($("#currentSection").val() === "traites") {
        data =
            data +
            "&" +
            $("#texte-maitre-form-ratification").serialize() +
            "&" +
            $("#texte-maitre-form-decret").serialize();
    }
    var myRequest = {
        dataToSend: data,
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

function rafraichirTexteMaitre(el) {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/texte-maitre/rafraichir";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function figerTableauProgrammation(el) {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/tableau-programmation/figer";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function defigerTableauProgrammation(el) {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/tableau-programmation/defiger";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function sauvegarderTableauSuivi(el) {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/tableau-suivi/sauvegarder";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function publierTableauSuivi(el) {
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/tableau-suivi/publier";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
        },
    };

    callAjaxRequest(myRequest, callbackBlocContextuelAndScrollToAlerts, displaySimpleErrorMessage);
}

function switchAffichageMesuresAppliquees(input) {
    // On enlève le style des erreurs
    const formOptinParent = $(input).parents(".form-optin__optins");
    formOptinParent.siblings(".form-input__footer").children(".form-input__description--error").remove();
    formOptinParent.removeClass("form-optin--error");
    var afficher = $(input).val();

    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    $("#panContext").val("textemaitre");
    var targetURL = panUrlContextuel() + idTexteMaitre + "/tableau-suivi/afficher?afficher=" + afficher;

    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        method: "GET",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        caller: this,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
        },
    };
    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function supprimerLignesHistorique() {
    var myTable = $("#listeHistoriqueMAJMinisterielles");
    var data = [];
    $(myTable)
        .find(":checkbox:checked.js-custom-table-line-check")
        .each(function () {
            data.push($(this).closest("tr").attr("data-id"));
        });

    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + $("#currentSection").val() + "/historique/supprimer";
    var myRequest = {
        contentId: null,
        dataToSend: {
            lineIds: data,
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function ajouterTexteMaitreItemNor(item, params, target = null) {
    var section = $("#currentSection").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + section + "/" + item + "/ajouter/";

    params["nor"] = $("#ajouterParNor-" + item).val();

    var myRequest = {
        contentId: target,
        dataToSend: params,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, target != null ? reloadPage : checkErrorOrReload, displaySimpleErrorMessage);
}

function ajouterElementPremierTableauParNor() {
    var typeElement = $("#firstTableType").val().slice(0, -1);
    ajouterTexteMaitreItemNor(typeElement, { idContextuel: $("#idContextuel").val() });
}

function ajouterElementSecondTableauParNor() {
    var typeElement = $("#secondTableType").val().slice(0, -1);
    ajouterTexteMaitreItemNor(
        typeElement,
        { idPremierTableau: $("#idFirstTable").val(), idContextuel: $("#idContextuel").val() },
        "texteMaitreSecondTableau"
    );
}

function afficherMasquerColonnesMesures(button) {
    if (button.hasClass("isHidden")) {
        button.removeClass("isHidden");
        button.find(".sr-only").text("Masquer les colonnes");
        button.attr("data-tippy-content", "Masquer les colonnes");
    } else {
        button.addClass("isHidden");
        button.find(".sr-only").text("Afficher les colonnes");
        button.attr("data-tippy-content", "Afficher les colonnes");
    }
    reloadTooltip();
    $("#texteMaitrePremierTableau table th").each(function (index, elm) {
        elm = $(elm);
        if (elm.data("show-hide")) {
            showHideElm(elm);
            $("#texteMaitrePremierTableau table tbody tr").each(function () {
                showHideElm($(this).children("td:nth-child(" + (index + 1) + ")"));
            });
        }
    });
}

function showHideElm(elm) {
    if (elm.hasClass("hidden")) {
        elm.removeClass("hidden");
    } else {
        elm.addClass("hidden");
    }
}

function creerElementPremierTableau() {
    var section = $("#currentSection").val();
    var typeElement = $("#firstTableType").val().slice(0, -1);
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + section + "/" + typeElement + "/creer/";
    var modale = $("#panModaleEdit");

    var myRequest = {
        contentId: modale.find(".interstitial__content > div").first().attr("id"),
        dataToSend: {
            idContextuel: $("#idContextuel").val(),
        },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function chargerTexteMaitreItem(item, itemId, params) {
    var section = $("#currentSection").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + section + "/" + item + "/charger/";

    var modale = $("#panModaleEdit");

    var myRequest = {
        contentId: modale.find(".interstitial__content > div").first().attr("id"),
        dataToSend: params,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl + itemId,
        isChangeURL: false,
        overlay: null,
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function chargerElementPremierTableau(idPremierTableau) {
    var typeElement = $("#firstTableType").val().slice(0, -1);
    chargerTexteMaitreItem(typeElement, idPremierTableau, {
        idContextuel: $("#idContextuel").val(),
        hideColumns: $("#texteMaitrePremierTableau th[data-show-hide].hidden").length > 0,
    });
}

function chargerElementSecondTableau(idSecondTableau) {
    var typeElement = $("#secondTableType").val().slice(0, -1);
    chargerTexteMaitreItem(typeElement, idSecondTableau, {
        idContextuel: $("#idContextuel").val(),
        idPremierTableau: $("#idFirstTable").val(),
    });
}

function rechargerSecondTableau() {
    var section = $("#currentSection").val();
    var typeElement = $("#secondTableType").val().slice(0, -1);
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + section + "/" + typeElement + "/recharger/";

    var myRequest = {
        contentId: "texteMaitreSecondTableau",
        dataToSend: { idPremierTableau: $("#idFirstTable").val(), idContextuel: $("#idContextuel").val() },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitSelect, displaySimpleErrorMessage);
}

function validerDecrets() {
    var section = $("#currentSection").val();
    var onglet = $("#currentPanTab").val();
    var idTexteMaitre = $("#idContextuel").val();
    var idPremierTableau = $("#idFirstTable").val();
    var targetURL = panUrlContextuel() + idTexteMaitre + "/texte-maitre/valider/decrets";
    var myRequest = {
        contentId: "blocContextuel-" + onglet,
        dataToSend: { idPremierTableau: idPremierTableau, idContextuel: idTexteMaitre },
        method: "POST",
        dataType: "html",
        ajaxUrl: $("#ajaxCallPath").val() + targetURL,
        isChangeURL: false,
        overlay: "reload-loader",
        extraDatas: {
            hash: "#blocContextuel",
            onglet: onglet,
            targetURL: targetURL,
            contexte: "textemaitre",
            idContextuel: idTexteMaitre,
            idPremierTableau: idPremierTableau,
        },
    };
    callAjaxRequest(myRequest, callbackBlocContextuel, displaySimpleErrorMessage);
}

function setApplicationRecu() {
    if ($("#optin-applicationRecu-oui").is(":checked")) {
        $("#saisirDecretsNOR").show();
        $("#saisirDecretsNOR").removeClass("hidden");
    } else {
        $("#saisirDecretsNOR").hide();
    }
}

function supprimerTexteMaitreItem(item, params, target = null) {
    var section = $("#currentSection").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + section + "/" + item + "/supprimer";
    var isChangeUrl = false;
    var newUrl = "";
    if (target == null) {
        var url = window.location.href.split("/");
        var firstElmId = url[url.length - 1];
        if (firstElmId === params["idPremierTableau"]) {
            isChangeUrl = true;
            newUrl = window.location.href.replace(firstElmId, "");
        }
    }

    var myRequest = {
        contentId: target,
        dataToSend: params,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: isChangeUrl,
        url: newUrl,
        overlay: "reload-loader",
    };

    callAjaxRequest(
        myRequest,
        target != null ? replaceHtmlFunctionAndInitSelect : checkErrorOrReload,
        displaySimpleErrorMessage
    );
}

function supprimerElementPremierTableau() {
    var typeElement = $("#firstTableType").val().slice(0, -1);
    var id = $("#supprEltPremierTableauId").val();
    supprimerTexteMaitreItem(typeElement, { idPremierTableau: id, idContextuel: $("#idContextuel").val() });
}

function supprimerElementSecondTableau() {
    var typeElement = $("#secondTableType").val().slice(0, -1);
    var id = $("#supprEltSecondTableauId").val();
    supprimerTexteMaitreItem(
        typeElement,
        {
            idSecondTableau: id,
            idPremierTableau: $("#idFirstTable").val(),
            idContextuel: $("#idContextuel").val(),
        },
        "texteMaitreSecondTableau"
    );
}

function editerTexteMaitreItem(item, target = null) {
    var modal = $("#panModaleEdit");
    if (!isValidForm(modal)) return false;

    var section = $("#currentSection").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/" + section + "/" + item + "/editer";
    var form = $("#content-panModaleEdit").find("form");

    var myRequest = {
        contentId: target,
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(
        myRequest,
        target != null ? replaceHtmlFunctionAndInitSelect : checkErrorOrReload,
        displaySimpleErrorMessage
    );
    closeModal($("#panModaleEdit").get(0));
}

function editerElementPremierTableau() {
    var typeElement = $("#firstTableType").val().slice(0, -1);
    editerTexteMaitreItem(typeElement);
}

function editerElementSecondTableau() {
    var typeElement = $("#secondTableType").val().slice(0, -1);
    editerTexteMaitreItem(typeElement, "texteMaitreSecondTableau");
}

function rafraichirPanBirtReport(btn) {
    var form = $(btn).closest("form");
    var mydatas = form.serialize();
    if (!isValidForm(form)) {
        return;
    }
    mydatas += "&idContextuel=" + $("#idContextuel").val();

    var statId = form.find("input[type=hidden][class=pan-stat]").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/rafraichir/" + statId;

    var myRequest = {
        contentId: form.children("div.birt-fragment").attr("id"),
        dataToSend: mydatas,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        loadingButton: $(btn),
        extraDatas: {
            successCallback: function () {
                $(".pan-birt-refresh-button").attr("disabled", "disabled");
            },
        },
        caller: this,
    };

    callAjaxRequest(myRequest, checkErrorOrToast);
}

function publierPanBirtReport(btn) {
    var form = $(btn).closest("form");

    var statId = form.find("input[type=hidden][class=pan-stat]").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/publier/" + statId;

    if (!isValidForm(form)) {
        return;
    }
    var mydatas = form.serialize() + "&idContextuel=" + $("#idContextuel").val();

    var myRequest = {
        contentId: null,
        dataToSend: mydatas,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        loadingButton: $(btn),
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function sauvegarderPanBirtReport(btn) {
    var form = $(btn).closest("form");

    var statId = form.find("input[type=hidden][class=pan-stat]").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/sauvegarder/" + statId;
    var mydatas = "idContextuel=" + $("#idContextuel").val();

    var myRequest = {
        contentId: null,
        dataToSend: mydatas,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        loadingButton: $(btn),
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function chargerPanBirtReport(button) {
    var $switch = $(button).closest(".pan-stats-switch");
    var statId = $switch.data("stat-id");
    var altStatId = $switch.data("alt-stat-id");
    var currentSubTab = $switch.closest(".tabulation__content").find(".birt-subtab").val();

    var contentIdVal = $switch.closest(".tabulation__content").find(".birt-fragment").attr("id");

    var currentVal = $("#optin-switch-" + currentSubTab + "-oui").is(":checked") ? altStatId : statId;

    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/charger/" + currentVal;
    var mydatas = "idContextuel=" + $("#idContextuel").val();

    var myRequest = {
        contentId: contentIdVal,
        dataToSend: mydatas,
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        caller: null,
        overlay: "birt-fragment-" + currentSubTab + "_overlay",
        extraDatas: {
            currentVal: currentVal,
            currentSubTab: currentSubTab,
        },
    };

    callAjaxRequest(myRequest, replaceHtmlChargerRapport, displaySimpleErrorMessage);
}

function replaceHtmlChargerRapport(containerId, content, caller, extraDatas) {
    var form = $("#birtPanReportForm-" + extraDatas["currentSubTab"]);
    form.find("input[type=hidden][class=pan-stat]").val(extraDatas["currentVal"]);
    replaceHtmlFunctionAndInitSelect(containerId, content);
}

function downloadPanStatPDF(input) {
    doPanStatDownload(input, "telecharger/pdf");
}

function downloadPanStatExcel(input) {
    doPanStatDownload(input, "telecharger/excel");
}

function doPanStatDownload(input, url) {
    var form = $(input).closest("form");

    var statId = form.find("input[type=hidden][class=pan-stat]").val();

    let urlPdf =
        $("#ajaxCallPath")
            .val()
            .substring(0, $("#ajaxCallPath").val().length - 5) +
        "/suivi/pan/stats/" +
        url +
        "?statId=" +
        statId;
    urlPdf += "&idContextuel=" + $("#idContextuel").val();
    window.open(urlPdf, "_blank");
}

function panExportGlobal(btn) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/exporter";

    if (!isValidForm($("#exportGlobalForm"))) {
        return;
    }

    var myRequest = {
        contentId: null,
        dataToSend: $("#exportGlobalForm").serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        caller: null,
        overlay: null,
        loadingButton: $("[class*='ACTION_EXPORT_GLOBAL']"),
        extraDatas: {
            successCallback: panExportGlobalCallback,
        },
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

function panExportLegisPrec(btn) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/exporter/precedente";

    var myRequest = {
        contentId: null,
        dataToSend: {},
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        caller: null,
        overlay: null,
        loadingButton: $("[class*='ACTION_EXPORT_GLOBAL']"),
        extraDatas: {
            successCallback: panExportGlobalCallback,
        },
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

var panExportGlobalCallback = function () {
    $(".ACTION_EXPORT_GLOBAL_LEGISLATURE_PRECEDENTE").attr("disabled", true);
    $(".ACTION_EXPORT_GLOBAL_LEGISLATURE_EN_COURS").attr("disabled", true);
};

function panPublierLegisPrec(btn) {
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/stats/publier/precedente";

    var myRequest = {
        contentId: null,
        dataToSend: {},
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        caller: null,
        overlay: null,
        loadingButton: $("[class*='ACTION_EXPORT_GLOBAL']"),
        extraDatas: {
            successCallback: panPublishCallback,
        },
    };

    callAjaxRequest(myRequest, checkErrorOrToast, displaySimpleErrorMessage);
}

var panPublishCallback = function () {
    $(".ACTION_PUBLIER_LEGISLATURE_PRECEDENTE  ").attr("disabled", true);
};

function ajouterTraite() {
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/traites-tableau-maitre/ajouter";
    var form = $("#panModaleTraiteForm");
    if (!isValidForm(form)) {
        event.stopImmediatePropagation();
        return false;
    }
    var myRequest = {
        contentId: null,
        dataToSend: form.serialize(),
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrReload, displaySimpleErrorMessage);
}

$(document).ready(function () {
    $("#panStatistiquesForm #legislatures")
        .parent()
        .find("input")
        .blur(function () {
            // l'ajout de legislatures disponibles va entrainer la mise à jour du select de legislature choisie
            var legisDispo = $("#legislatures > option[selected=selected]")
                .map(function () {
                    return DOMPurify.sanitize($(this).val());
                })
                .get();

            var legisProp = $("#legislatureEnCours > option")
                .filter((f) => f > 0)
                .map(function () {
                    return $(this).val();
                })
                .get();

            legisDispo.forEach(function (i) {
                if (!legisProp.includes(i)) {
                    $("#legislatureEnCours").append($("<option>", { value: i, text: i }));
                }
            });
        });
});

function afficherMasquerRatification() {
    const ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/ratification/afficherMasquer";
    let changeUrl = false;
    let newUrl = "";
    if ($("#idContextuel").val().length) {
        changeUrl = true;
        newUrl = $("#basePath").val() + "suivi/pan/ratification/tableau-maitre#main_content";
    }
    var myRequest = {
        contentId: null,
        dataToSend: { action: $("#masquerRatification").val() === "true" ? "afficher" : "masquer" },
        method: "GET",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: changeUrl,
        url: newUrl,
        overlay: "reload-loader",
    };
    callAjaxRequest(myRequest, checkErrorOrHardReload, displaySimpleErrorMessage);
}

function buildSearchMapPan(table) {
    let typeForm = $(event.srcElement).closest("#searchForm")[0];
    var map = buildSearchMapRequest(typeForm);

    // ajout des datas de tri et de numero de page à la map
    $(table)
        .find("input[data-isForm='true'], select[data-isForm='true']")
        .each(function () {
            if (this.value !== undefined) {
                map.set(this.name, this.value);
            }
        });

    $(table)
        .find("[data-field]")
        .each(function () {
            if ($(this).data("value") !== undefined) {
                map.set($(this).data("field"), $(this).data("value"));
            }
        });

    return map;
}

function buildSearchMapRequest(typeForm) {
    var map = new Map();

    $(typeForm)
        .find("select[multiple]")
        .each(function () {
            if ($(this).val().length !== 0) {
                map.set($(this).attr("name"), $(this).val());
            }
        });

    var dataForm = $(typeForm).serializeArray();
    for (var i = 0; i < dataForm.length; i++) {
        if (!map.has(dataForm[i].name) && dataForm[i].value !== "" && dataForm[i].name !== "inclure") {
            var valueField = dataForm[i].value.replaceAll("'", "\\'");
            map.set(dataForm[i].name, valueField);
        }
    }

    return map;
}

function buildSearchMapPanTab(table) {
    let typeForm = $($(event.srcElement).closest("div[role=tabpanel]")[0]).find("#searchForm")[0];
    var map = buildSearchMapRequest(typeForm);

    // ajout des datas de tri et de numero de page à la map
    $(table)
        .find("input[data-isForm='true'], select[data-isForm='true']")
        .each(function () {
            if (this.value !== undefined) {
                map.set(this.name, this.value);
            }
        });

    $(table)
        .find("[data-field]")
        .each(function () {
            if ($(this).data("value") !== undefined) {
                map.set($(this).data("field"), $(this).data("value"));
            }
        });

    return map;
}

function doRapidFiltrePan() {
    var table = $(event.srcElement).closest(".tabulation__content")[0].querySelector(".tableForm");
    var map = buildSearchMapPan(table);
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/filtrer";
    if (window.location.toString().includes("historique-maj")) {
        ajaxUrl = table.data("ajaxurl");
    }

    var myRequest = {
        contentId: $($(event.srcElement).closest(".tabulation__content")[0].querySelector(".tableForm")).attr("id"),
        dataToSend: {
            search: JSON.stringify([...map]),
            currentSection: $("#currentSection").val(),
            currentPanTab: $("#currentPanTab").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceHtmlFunctionAndInitAutoSelect, tabLoadError);
}

function doRapidFiltrePanTab(table) {
    var map = buildSearchMapPanTab(table);
    var ajaxUrl = $("#ajaxCallPath").val() + "/suivi/pan/filtrer";
    if (window.location.toString().includes("historique-maj")) {
        ajaxUrl = table.data("ajaxurl");
    }

    var myRequest = {
        contentId: $($(event.srcElement).closest(".tabulation__content")[0].querySelector(".tableForm")).attr("id"),
        dataToSend: {
            search: JSON.stringify([...map]),
            currentSection: $("#currentSection").val(),
            currentPanTab: $("#currentPanTab").val(),
        },
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
        overlay: "reload-loader",
    };

    callAjaxRequest(myRequest, replaceWithHtmlFunction, tabLoadError);
}
