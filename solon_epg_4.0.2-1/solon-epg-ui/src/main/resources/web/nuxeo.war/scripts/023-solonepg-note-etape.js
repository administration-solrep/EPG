function showSelectOrganigrammeRestriction() {
    var typeRestriction = $("input[name='addNoteEtapeRadioTypeSelect']:checked").val();
    if (typeRestriction == "NON") {
        $("#noteEtapeSelectOrgaRestriction").empty();
    } else {
        var ajaxUrl = $("#ajaxCallPath").val() + "/etape/note/shwoSelectOrganigrammeRestriction";
        var myRequest = {
            contentId: "noteEtapeSelectOrgaRestriction",
            dataToSend: {
                typeRestriction: $("input[name='addNoteEtapeRadioTypeSelect']:checked").val(),
            },
            method: "POST",
            dataType: "html",
            ajaxUrl: ajaxUrl,
            isChangeURL: false,
            overlay: null,
        };

        callAjaxRequest(myRequest, replaceHtmlFunctionAndInitOrganigramme, displaySimpleErrorMessage);
    }
}

function doCreateNoteEtape() {
    if (valideFormModaleNoteEtape()) {
        var dataToSend = {
            stepId: $("#stepId").val(),
            commentContent: DOMPurify.sanitize($("#note-textarea").val()),
            dossierLinkId: $("#dossierLinkId").val(),
            typeRestriction: $("input[name='addNoteEtapeRadioTypeSelect']:checked").val(),
            idNode: $("#addNoteRestrictionValue").val(),
        };

        var ajaxUrl = $("#ajaxCallPath").val() + "/etape/note/creer";
        doRequestAddEditNote(ajaxUrl, dataToSend);
    }
}

function doAnswerNoteEtape() {
    if (valideFormModaleNoteEtape()) {
        var dataToSend = {
            fdrId: document.getElementById("d_fdr_content").getAttribute("fdr-id"),
            stepId: $("#stepId").val(),
            commentContent: DOMPurify.sanitize($("#note-textarea").val()),
            commentParentId: $("#noteId").val(),
            dossierLinkId: $("#dossierLinkId").val(),
            typeRestriction: $("input[name='addNoteEtapeRadioTypeSelect']:checked").val(),
            idNode: $("#addNoteRestrictionValue").val(),
        };

        var ajaxUrl = $("#ajaxCallPath").val() + "/etape/note/creer";
        doRequestAddEditNote(ajaxUrl, dataToSend);
    }
}

function doCreateNoteEtapeEnCours() {
    if (valideFormModaleNoteEtape()) {
        var dataToSend = {
            commentContent: DOMPurify.sanitize($("#note-textarea").val()),
            dossierLinkId: $("#dossierLinkId").val(),
            dossierId: $("#dossierId").val(),
            typeRestriction: $("input[name='addNoteEtapeRadioTypeSelect']:checked").val(),
            idNode: $("#addNoteRestrictionValue").val(),
        };

        var ajaxUrl = $("#ajaxCallPath").val() + "/etape/note/creerEnCours";
        doRequestAddEditNote(ajaxUrl, dataToSend);
    }
}

function getOtherData() {
    return {
        typeRestriction: $("input[name='addNoteEtapeRadioTypeSelect']:checked").val(),
        idNode: $("#addNoteRestrictionValue").val(),
    };
}

function doEditNoteEtape() {
    if (valideFormModaleNoteEtape()) {
        var dataToSend = {
            stepId: $("#stepId").val(),
            commentId: $("#noteId").val(),
            commentContent: DOMPurify.sanitize($("#note-textarea").val()),
            dossierLinkId: $("#dossierLinkId").val(),
            typeRestriction: $("input[name='addNoteEtapeRadioTypeSelect']:checked").val(),
            idNode: $("#addNoteRestrictionValue").val(),
        };

        var ajaxUrl = $("#ajaxCallPath").val() + "/etape/note/modifier";
        doRequestAddEditNote(ajaxUrl, dataToSend);
    }
}

function doRequestAddEditNote(ajaxUrl, data) {
    var myRequest = {
        contentId: null,
        dataToSend: data,
        method: "POST",
        dataType: "html",
        ajaxUrl: ajaxUrl,
        isChangeURL: false,
    };
    $("#reload-loader").css("display", "block");
    closeModal($("#add-edit-note-etape-modal").get(0));
    resetTextarea();

    callAjaxRequest(myRequest, displayNoteSuccessToastAndLoadFdrTab, displaySimpleErrorMessage);
}
