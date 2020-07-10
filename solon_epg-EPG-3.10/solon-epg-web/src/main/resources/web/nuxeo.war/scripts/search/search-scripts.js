// Gestion des glyphicons sur les facets
jQuery(document).ready(function($){
	
	$("#facets").on( "click", ".facet-legend", function(){
		toogleGlyphicon($(this));
	});
	
	function toogleGlyphicon($this) {
		$glyphicon = $this.find(".glyphicon");
		$glyphicon.toggleClass("glyphicon-plus glyphicon-minus");
	}
});

// Gestion du retour en haut de page
jQuery(document).ready(function($){
	if ($('#back-to-top').length) {
	    var scrollTrigger = 100, // px
	        backToTop = function () {
	            var scrollTop = $(window).scrollTop();
	            if (scrollTop > scrollTrigger) {
	                $('#back-to-top').addClass('show');
	            } else {
	                $('#back-to-top').removeClass('show');
	            }
	        };
	    backToTop();
	    $(window).on('scroll', function () {
	        backToTop();
	    });
	    $('#back-to-top').on('click', function (e) {
	        e.preventDefault();
	        $('html,body').animate({
	            scrollTop: 0
	        }, 700);
	    });
	}
});

// Si on a du JS, on masque les éléments devenus inutiles
jQuery(document).ready(function($){
	//$("#facets .extra-submit-button").hide();
	$("a.close").show();
	$("button#search-date").show();
});

// Gestion des facettes et critères de la recherche
jQuery(document).ready(function($){
	
	var $criteria = $(".criteria"),
		$searchForm = $("#search-form"),
		$removeButton = $searchForm.find(".glyphicon-remove"),
		$facets = $("#facets"),
		$selectedCriteria = $(".selected-criteria"),
		$sortType = $("#search-form\\:sort-select"),
		$pageSize = $("#search-form\\:pageSize-select"),
		$pagination = $(".pagination"),
		$inputSearch = $("#search-form\\:fulltext"),
		$criteriaContainer = $("#criteria-container"),
		$searchButton = $("#main-submit-button"),
		$resetCriteria = $("#reset-criteria"),
		$reloadResults = $("#search-form\\:reload-results"),
		$reloadFacets = $("#search-form\\:reload-facets"),
		$formGroup = $(".form-group"),
		$archivage = $("#rechercheArchivageForm\\:requeteur_dansBaseArchivageIntermediaire"),
		$production = $("#rechercheArchivageForm\\:requeteur_dansBaseProduction"),
		$archive = $("#search-form\\:archive-checkbox"),
		$prod = $("#search-form\\:prod-checkbox"),
		$resultats = $("#resultats");

	var fullTextVal;

	// Initialisation
	function init() {

		if ( ($archivage.is(':checked') != $archive.is(':checked')) || ($production.is(':checked') != $prod.is(':checked')) ) {
			updateArchiveOptions();
		}

		// On lance la recharge AJAX des facets et résultats
		$reloadFacets.click();
		$reloadResults.click();

		// On affiche les critères préselectionnés
		$selectedCriteria.find("p").each(function(){
			$this = $(this);

			displayCriterion($this.data('key'), $this.data('label'), $this.data('value'), $this.data('display'));
		});

		// On affiche les critères de dates si les dateInputs sont préremplis
		$facets.find("input[class='form-control date-input']").each(function(){
			$this = $(this);

			if($this.val() != undefined && $this.val() != "")
				addDateCriterion($this);
		});

		// On cache la partie criteria si aucun n'est préselectionné
		if($criteriaContainer.find("div[class='well well-sm criterion']").length) {
			$criteriaContainer.show();
		}

		// On affiche le "X" dans la barre de recherche fulltext si elle n'est pas vide
		if($inputSearch.val() != ""){
			$removeButton.show();
		}

		// On met à part la valeur initiale du fulltext
		fullTextVal = $inputSearch.val();

		// On affecte l'état du checkbox "Base d'archivage intermédiaire" au paramètre archive du SearchCriteria
		$archive.prop('checked', $archivage.is(':checked'));
	}

	// Affiche un critère, et son champ hidden dans le formulaire
	function displayCriterion(key, label, value, display) {
		tpl = '<div class="well well-sm criterion" data-id="'
			+ key + ':' + value
			+ '">'
			+ label
			+ ' : '
			+ display
			+ '<button class="close" name="erase" data-key="' + key + '" value="' + key + ':' + value + '"><span aria-hidden="true">&times;</span></button></div>';
		$criteria.append(tpl);
	}

	// Affiche un critère de type date, et son champ hidden dans le formulaire
	function addDateCriterion($facet) {
		key = $facet.parents('label').data('key');

		// Si disponible, on prend la valeur du data-attribute correspondant pour la valeur d'affichage
		value = $facets.data("label-" + $facet.val()) ? $facets.data("label-" + $facet.val()) : $facet.val();

		tpl = '<div class="well well-sm criterion" data-id="'
			+ key + ':' + value
			+ '">'
			+ $facet.parents('label').data('criterion-label')
			+ ' : '
			+ value
			+ '<button class="close" name="erase" data-key="' + key + '" value="' + key + ':' + value + '"><span aria-hidden="true">&times;</span></button></div>';
		$criteria.append(tpl);
	}

	// Ajoute ou supprime un critère du searchCriteria
	function changeCriterionState(facetName, facetType, isAdding) {
		document.getElementById("checkbox-actions:facetName").value = facetName;
		document.getElementById("checkbox-actions:facetType").value = facetType;
		if(isAdding) {
			document.getElementById("checkbox-actions:check").click();
		} else {
			document.getElementById("checkbox-actions:uncheck").click();
		}
	}

	// Synchroniser les checkboxes liés aux propriétés 'archive' et 'prod' avec les checkboxes 'Base de production' et 'Base d'archivage intermédiaire'
	function updateArchiveOptions() {
		$archive.prop('checked', $archivage.is(':checked'));
		$prod.prop('checked', $production.is(':checked'));
		backToPage1();
		reloadPage();
	}

	// Soumets le formulaire de recherche
	function reloadPage() {
		if(fullTextVal !== $inputSearch.val())
			backToPage1();
		$searchButton.click();
	}

	// Réinitialise la page courante à 1
	function backToPage1() {
		console.log("Back to page 1");
		$searchForm.find('input[id="search-form:currPage"]').val("1");
	}

	// Si on est sur une page de recherche
	if($("#search").length) {

		init();

		// Gestion du clic sur une facette
		$facets.on( "change", "input[type='checkbox']", function(e){
			$this = $(this);

			changeCriterionState($this.val(), $this.parents('label').data('key'), $this.prop('checked'));
		});

		// Gestion du click sur "Voir plus" ou "Voir moins"
		$facets.on( "click", ".facet_more_link", function() {
			$this = $(this);
			facetClass = "." + $this.prev().attr("class");
			elts = $(facetClass);

			if(elts[0].style.display == 'none') {
				for (i = 0; i < elts.length; i++) {
					elts[i].style.display = 'block';
				}
				$this.text("Voir moins...");
			} else {
				for (i = 0; i < elts.length; i++) {
					elts[i].style.display = 'none';
				}
				$this.text("Voir plus...");
			}
		});

		// Gestion du click sur "Voir plus de documents" ou "Voir moins de documents"
		$resultats.on( "click", ".docs_more_link", function() {
			$this = $(this);
			resultClass = "." + $this.prev().children().last().attr("class");
			elts = $(resultClass);

			if(elts.length) {
				if(elts[0].style.display == 'none') {
					for (i = 0; i < elts.length; i++) {
						elts[i].style.display = 'list-item';
					}
					$this.text("Voir moins de documents...");
				} else {
					for (i = 0; i < elts.length; i++) {
						elts[i].style.display = 'none';
					}
					$this.text("Voir plus de documents...");
				}
			}
		});

		// Gestion du click sur les checkboxes en dessous de la barre de recherche
		$formGroup.on( "change", "input[type='checkbox']", function(e){
			backToPage1();
		});

		// Gestion de la suppression d'un critère
		$criteria.on( "click", ".close", function(e){
			$this = $(this);
			backToPage1();

			$criterion = $criteria.find('div[data-id="' + $this.val() + '"]');
			$checkboxFacet = $facets.find('input[data-id="' + $criterion.data('id') + '"]');
			$dateFacet = $facets.find('label[data-key="' + $this.data("key") + '"]').find('input[class="form-control date-input"]');

			$criterion.remove();

			if($checkboxFacet.length) {
				$checkboxFacet.click();
			} else if($dateFacet.length) {
				$dateFacet.val("");
				reloadPage();
			} else {
				console.log($this.val().substring($this.data("key").length+1));
				changeCriterionState($this.val().substring($this.data("key").length+1), $this.data("key"), false);
			}
		});

		// Gestion du changement de type de tri
		$sortType.on( "change", function(){
			backToPage1();
			reloadPage();
		});

		// Gestion du changement du nombre de résultat par page
		$pageSize.on( "change", function(){
			backToPage1();
			reloadPage();
		});

		// Au click du bouton "Rechercher"
		$inputSearch.on( "click", function(){
			backToPage1();
		});

		// A la saisie de texte dans le champ fulltext
		$inputSearch.keyup(function(){
			var val = $(this).val();
			
			if(val != ""){
				$removeButton.show();
			}
			else {
				$removeButton.hide();
			}
		});
		
		// Au clic sur le bouton de suppresion du fulltext
		$removeButton.on( "click", function(){
			$inputSearch.val("");
			reloadPage();
		});

		// Au clic du checkbox 'Base d'archivage intermédiaire'
		$archivage.on('change', function(){
			updateArchiveOptions();
		});

		// Au clic du checkbox 'Base de production'
		$production.on('change', function(){
			updateArchiveOptions();
		});
	}
});

jQuery(document).ready(function($){
	$('.facet-date-picker .input-group.date').datepicker({
	    format: "dd/mm/yyyy",
	    weekStart: 1,
	    language: "fr",
	    autoclose: true,
	    todayHighlight: true
	});
});

// Au clic sur "Réinitialiser"
function resetCriteria(){
	var dateInputs = document.getElementsByClassName("form-control date-input");
	var searchInput = document.getElementById("search-form:fulltext");
	var resetCriteria = document.getElementById("checkbox-actions:resetCriteria");
	var currPage = document.getElementById("search-form:currPage");
	var sortSelect = document.getElementById("search-form:sort-select");
	var pageSizeSelect = document.getElementById("search-form:pageSize-select");
	var expressionExacte = document.getElementById("search-form:expressionExacte-checkbox");
	var currentVersion = document.getElementById("search-form:docCurrentVersion-checkbox");

	searchInput.value = "";
	for (i = 0; i < dateInputs.length; i++) {
		dateInputs[i].value = "";
	}
	if(currPage != null)
		currPage.value = 1;
	sortSelect.value = "RELEVANCE_DESC";
	pageSizeSelect.value = 25;
	expressionExacte.checked = false;
	currentVersion.checked = true;

	resetCriteria.click();
}

// Ouvre la page indiquée
function goToPage(pageNb){
	var currPage = document.getElementById("search-form:currPage");
	var searchButton = document.getElementById("main-submit-button");

	currPage.value = pageNb;
	searchButton.click();
}

// Au clic d'un NOR, affiche les détails du dossier concerné
function showNor(nor){
	var norSearchInput = document.getElementById("userMetaServicesSearchForm:numeroNor");
	var norSearchSubmit = document.getElementById("userMetaServicesSearchForm:rechercheNumeroSubmitButton");

	norSearchInput.value = nor;
	norSearchSubmit.click();
}
