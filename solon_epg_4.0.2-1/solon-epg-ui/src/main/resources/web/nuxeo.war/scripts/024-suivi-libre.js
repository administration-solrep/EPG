function updateLegislature() {
    var legislature = $("#legislature-enCours:checked").length ? "enCours" : "precedente";
    var typeApplication = $("#typeApplication").val();
    var ajaxUrl = $("#ajaxCallPath").val() + "/suiviLibre/application/charger";
    var url =
        $("#basePath").val() +
        "suiviLibre/" +
        typeApplication +
        "/consulter?legislatureEnCours=" +
        (legislature === "enCours");

    var myRequest = {
        contentId: "resultatLegislature",
        dataToSend: {
            legislatureEnCours: legislature === "enCours",
            typeActe: $("#typeActe").val(),
            typeApplication: typeApplication,
        },
        method: "GET",
        dataType: "html",
        url: url,
        ajaxUrl: ajaxUrl,
        isChangeURL: true,
        overlay: "resultatLegislature_overlay",
    };

    callAjaxRequest(myRequest, replaceHtmlFunction);
}
