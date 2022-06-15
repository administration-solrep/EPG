function createAlert(input) {
    let loc = null;

    if ($(input).attr("id") === "create-alert-experte-button") {
        loc = "suivi/alertes/creerExperte?sessionDtoKey=" + $("#sessionDtoKey").val();
    } else {
        var baseProduction = $("#chk_base_production").get(0).checked;
        var baseArchivage = $("#chk_base_archivage").get(0).checked;
        var expressionExacte = $("#chk_expression_exacte").get(0).checked;
        var derniereVersion = $("#chk_derniere_version").get(0).checked;
        var recherche = $("#recherche").val();

        loc =
            "suivi/alertes/creerLibre?baseProduction=" +
            baseProduction +
            "&baseArchivage=" +
            baseArchivage +
            "&expressionExacte=" +
            expressionExacte +
            "&derniereVersion=" +
            derniereVersion +
            "&recherche=" +
            recherche;
    }

    window.location.href = $("#basePath").val() + loc + "#main_content";
}

var alertCallBack = checkErrorOrGoPrevious;
