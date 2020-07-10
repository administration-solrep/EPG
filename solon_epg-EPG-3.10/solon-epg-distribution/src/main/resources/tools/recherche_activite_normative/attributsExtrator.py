#!/usr/bin/python
# -*- coding: utf-8 -*-

from xml.dom.minidom import parse, parseString
import json

""" Element de l'extracteur """
class ExtractorElt:
	SCHEMA_PREFIX = "texm:"
	
	def __init__(self,elt,categories):
		self.elt = elt
		self.categories = categories

	def widget_prefix(self,category):
		document_prefix = "d" 
		if category == "mesure":
			document_prefix = "m" 
		if category == "decret":
			document_prefix = "de" 
		return document_prefix + "." + ExtractorElt.SCHEMA_PREFIX

	def name(self):
		return self.elt.getAttribute("name")

	def fullname(self,category):
		return self.widget_prefix(category) + self.name()
		
	def fullnamewithdots(self,category):
		return self.fullname(category).replace(":",".")
	
	def type(self):
		return self.elt.getAttribute("type").replace("xs:","")
	
	def is_in_categorie(self,categorie):
		return categorie in self.categories
	
	def __str__(self):
		return self.name()

""" Class d'extraction des élements du schéma, et association avec les informations de certains commentaires"""
TOOL_MARKER="AE"

class Extractor:
	def __init__(self, filename):
		self.filename = filename
		self.dom = parse(filename)
		self.results = []

	def __register(self,elt,comment):
		categories = [cat.strip() for cat in comment.data.split(":")[1].split(",")]
		self.results.append(ExtractorElt(elt,categories))	

	def __has_tool_directives(self,comment):
		return TOOL_MARKER in comment.data.split(':')[0]
	
	def __is_comment(self,comment):
		return comment.COMMENT_NODE == comment.nodeType

	def __is_elt_extractible(self,next_node):
		return self.__is_comment(next_node) and self.__has_tool_directives(next_node)
		
	def extract(self):
		elts = self.dom.getElementsByTagName("xs:element")
		for elt in elts:
			next_node = elt.nextSibling
			if self.__is_elt_extractible(next_node):
				self.__register(elt,next_node)
	
	def get_elements(self):
		return self.results


CATEGORIES = ['applicationLoi',"transposition","ordonnance","ordonnance38C","traite","decret","mesure"]

""" Classe d'affichage des informations d'un extracteur """
class DefaultPrinter:
	
	def print_categorie_header(self,cat):
		print "Categorie "  + cat + " : "

	def print_categorie_footer(self,cat):
		print "\n"

	def print_categorie_elt(self,cat,elt):
		print " - " + elt.__str__()

	def print_categorie(self,extractor,cat):
		self.print_categorie_header(cat)
		for elt in extractor.get_elements():
			if elt.is_in_categorie(cat):
				self.print_categorie_elt(cat,elt)
		self.print_categorie_footer(cat)

	
	def print_categories(self,extractor):
		for categorie in CATEGORIES:
			self.print_categorie(extractor,categorie)


""" Classe d'affichage du contenu de la contribution des widgets de la recherche de l'activité normative """
class ContributionPrinter(DefaultPrinter):

	def print_categorie_header(self,cat):
		print "\t<!-- Catégorie "  + cat + " --> "

	def print_categorie_elt(self,cat,elt):
		print "\t<widgetDescription category=\"%s\" name=\"%s\" type=\"%s\"/>" % (cat,elt.fullname(cat),elt.type())

""" Classe d'affichage du contenu de la contribution des widgets de la recherche de l'activité normative en json"""
class JsonPrinter(DefaultPrinter):
	
	def print_categorie_header(self,cat):
		pass
	
	def print_categorie_footer(self,cat):
		pass

	def print_categorie_elt(self,cat,elt):
		print json.dumps({"category":cat,"name":elt.fullname(cat),"type":elt.type()})

""" Classe d'affichage des messages d'internationalisation"""
class I10nPrinter(DefaultPrinter):
	
	def print_categorie_header(self,cat):
		print "##Activité normative : " + cat
	
	def print_categorie_footer(self,cat):
		pass

	def print_categorie_elt(self,cat,elt):
		print "label.requeteur." + elt.fullnamewithdots(cat) + "=" + elt.name()


texte_maitre = '/mnt/hgfs/D/workspace/solon_epg/solon-epg-core/src/main/resources/schemas/texte_maitre.xsd' 
extractor = Extractor(texte_maitre)
extractor.extract()
#printer = DefaultPrinter()
#printer = ContributionPrinter()
printer = I10nPrinter()
#printer = JsonPrinter()
printer.print_categories(extractor)
