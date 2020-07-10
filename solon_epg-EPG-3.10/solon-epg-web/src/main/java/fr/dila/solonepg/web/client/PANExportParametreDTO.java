package fr.dila.solonepg.web.client;

import java.io.Serializable;
import java.util.Date;

public class PANExportParametreDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -9125582607213255629L;

	private Date				bilanPromulgationDebut;
	private Date				bilanPromulgationFin;
	private Date				bilanPublicationDebut;
	private Date				bilanPublicationFin;
	private Date				tauxPromulgationDebut;
	private Date				tauxPromulgationFin;
	private Date				tauxPublicationDebut;
	private Date				tauxPublicationFin;
	private Date				lolfPromulgationDebut;
	private Date				lolfPromulgationFin;
	private Date				lolfPublicationDebut;
	private Date				lolfPublicationFin;
	private Date				dateDebutLegislature;
	// Application des ordonnances
	private Date				bilanPublicationDebutOrdo;
	private Date				bilanPublicationFinOrdo;
	private Date				bilanPublicationDebutOrdoDecret;
	private Date				bilanPublicationFinOrdoDecret;
	// Début législature
	private Date				tauxDLPublicationOrdosDebut;
	private Date				tauxDLPublicationOrdosFin;
	private Date				tauxDLPublicationDecretsOrdosDebut;
	private Date				tauxDLPublicationDecretsOrdosFin;
	// Dernière session parlementaire
	private Date				tauxSPPublicationOrdosDebut;
	private Date				tauxSPPublicationOrdosFin;
	private Date				tauxSPPublicationDecretsOrdosDebut;
	private Date				tauxSPPublicationDecretsOrdosFin;

	public Date getBilanPromulgationDebut() {
		return bilanPromulgationDebut;
	}

	public void setBilanPromulgationDebut(Date bilanPromulgationDebut) {
		this.bilanPromulgationDebut = bilanPromulgationDebut;
	}

	public Date getBilanPromulgationFin() {
		return bilanPromulgationFin;
	}

	public void setBilanPromulgationFin(Date bilanPromulgationFin) {
		this.bilanPromulgationFin = bilanPromulgationFin;
	}

	public Date getBilanPublicationDebut() {
		return bilanPublicationDebut;
	}

	public void setBilanPublicationDebut(Date bilanPublicationDebut) {
		this.bilanPublicationDebut = bilanPublicationDebut;
	}

	public Date getBilanPublicationFin() {
		return bilanPublicationFin;
	}

	public void setBilanPublicationFin(Date bilanPublicationFin) {
		this.bilanPublicationFin = bilanPublicationFin;
	}

	public Date getTauxPromulgationDebut() {
		return tauxPromulgationDebut;
	}

	public void setTauxPromulgationDebut(Date tauxPromulgationDebut) {
		this.tauxPromulgationDebut = tauxPromulgationDebut;
	}

	public Date getTauxPromulgationFin() {
		return tauxPromulgationFin;
	}

	public void setTauxPromulgationFin(Date tauxPromulgationFin) {
		this.tauxPromulgationFin = tauxPromulgationFin;
	}

	public Date getTauxPublicationDebut() {
		return tauxPublicationDebut;
	}

	public void setTauxPublicationDebut(Date tauxPublicationDebut) {
		this.tauxPublicationDebut = tauxPublicationDebut;
	}

	public Date getTauxPublicationFin() {
		return tauxPublicationFin;
	}

	public void setTauxPublicationFin(Date tauxPublicationFin) {
		this.tauxPublicationFin = tauxPublicationFin;
	}

	public Date getLolfPromulgationDebut() {
		return lolfPromulgationDebut;
	}

	public void setLolfPromulgationDebut(Date lolfPromulgationDebut) {
		this.lolfPromulgationDebut = lolfPromulgationDebut;
	}

	public Date getLolfPromulgationFin() {
		return lolfPromulgationFin;
	}

	public void setLolfPromulgationFin(Date lolfPromulgationFin) {
		this.lolfPromulgationFin = lolfPromulgationFin;
	}

	public Date getLolfPublicationDebut() {
		return lolfPublicationDebut;
	}

	public void setLolfPublicationDebut(Date lolfPublicationDebut) {
		this.lolfPublicationDebut = lolfPublicationDebut;
	}

	public Date getLolfPublicationFin() {
		return lolfPublicationFin;
	}

	public void setLolfPublicationFin(Date lolfPublicationFin) {
		this.lolfPublicationFin = lolfPublicationFin;
	}

	public Date getDateDebutLegislature() {
		return dateDebutLegislature;
	}

	public void setDateDebutLegislature(Date dateDebutLegislature) {
		this.dateDebutLegislature = dateDebutLegislature;
	}

	/*###########################################################################################
	 * 								APPLICATION DES ORDONNANCES									#
	 ############################################################################################*/

	/**
	 * date début publication ordo bilan semestriel
	 */
	public Date getBilanPublicationDebutOrdo() {
		return bilanPublicationDebutOrdo;
	}

	/**
	 * date début publication ordo bilan semestriel
	 */
	public void setBilanPublicationDebutOrdo(Date bilanPublicationDebutOrdo) {
		this.bilanPublicationDebutOrdo = bilanPublicationDebutOrdo;
	}

	/**
	 * date fin publication ordo bilan semestriel
	 */
	public Date getBilanPublicationFinOrdo() {
		return bilanPublicationFinOrdo;
	}

	/**
	 * date fin publication ordo bilan semestriel
	 */
	public void setBilanPublicationFinOrdo(Date bilanPublicationFinOrdo) {
		this.bilanPublicationFinOrdo = bilanPublicationFinOrdo;
	}

	/**
	 * date début publication décret bilan semestriel
	 */
	public Date getBilanPublicationDebutOrdoDecret() {
		return bilanPublicationDebutOrdoDecret;
	}

	/**
	 * date début publication décret bilan semestriel
	 */
	public void setBilanPublicationDebutOrdoDecret(Date bilanPublicationDebutOrdoDecret) {
		this.bilanPublicationDebutOrdoDecret = bilanPublicationDebutOrdoDecret;
	}

	/**
	 * date fin publication décret bilan semestriel
	 */
	public Date getBilanPublicationFinOrdoDecret() {
		return bilanPublicationFinOrdoDecret;
	}

	/**
	 * date fin publication décret bilan semestriel
	 */
	public void setBilanPublicationFinOrdoDecret(Date bilanPublicationFinOrdoDecret) {
		this.bilanPublicationFinOrdoDecret = bilanPublicationFinOrdoDecret;
	}

	/**
	 * Date début publication ordo taux d'exécution Début législature 
	 */
	public Date getTauxDLPublicationOrdosDebut() {
		return tauxDLPublicationOrdosDebut;
	}

	/**
	 * Date début publication ordo taux d'exécution Début législature 
	 */
	public void setTauxDLPublicationOrdosDebut(Date tauxDLPromulgationOrdosDebut) {
		this.tauxDLPublicationOrdosDebut = tauxDLPromulgationOrdosDebut;
	}

	/**
	 * Date fin publication ordo taux d'exécution Début législature 
	 */
	public Date getTauxDLPublicationOrdosFin() {
		return tauxDLPublicationOrdosFin;
	}

	/**
	 * Date fin publication ordo taux d'exécution Début législature 
	 */
	public void setTauxDLPublicationOrdosFin(Date tauxDLPromulgationOrdosFin) {
		this.tauxDLPublicationOrdosFin = tauxDLPromulgationOrdosFin;
	}

	/**
	 * Date début publication ordo taux d'exécution Début législature 
	 */
	public Date getTauxDLPublicationDecretsOrdosDebut() {
		return tauxDLPublicationDecretsOrdosDebut;
	}

	/**
	 * Date début publication décret taux d'exécution Début législature 
	 */
	public void setTauxDLPublicationDecretsOrdosDebut(Date tauxDLPublicationDecretsOrdosDebut) {
		this.tauxDLPublicationDecretsOrdosDebut = tauxDLPublicationDecretsOrdosDebut;
	}

	/**
	 * Date fin publication décret taux d'exécution Début législature 
	 */
	public Date getTauxDLPublicationDecretsOrdosFin() {
		return tauxDLPublicationDecretsOrdosFin;
	}

	/**
	 * Date fin publication décret taux d'exécution Début législature 
	 */
	public void setTauxDLPublicationDecretsOrdosFin(Date tauxDLPublicationDecretsOrdosFin) {
		this.tauxDLPublicationDecretsOrdosFin = tauxDLPublicationDecretsOrdosFin;
	}

	/**
	 * Date début publication ordo taux d'exécution Session parlementaire 
	 */
	public Date getTauxSPPublicationOrdosDebut() {
		return tauxSPPublicationOrdosDebut;
	}

	/**
	 * Date début publication ordo taux d'exécution Session parlementaire 
	 */
	public void setTauxSPPublicationOrdosDebut(Date tauxSPPromulgationOrdosDebut) {
		this.tauxSPPublicationOrdosDebut = tauxSPPromulgationOrdosDebut;
	}

	/**
	 * Date fin publication ordo taux d'exécution Session parlementaire 
	 */
	public Date getTauxSPPublicationOrdosFin() {
		return tauxSPPublicationOrdosFin;
	}

	/**
	 * Date fin publication ordo taux d'exécution Session parlementaire 
	 */
	public void setTauxSPPublicationOrdosFin(Date tauxSPPromulgationOrdosFin) {
		this.tauxSPPublicationOrdosFin = tauxSPPromulgationOrdosFin;
	}

	/**
	 * Date début publication décret taux d'exécution Session parlementaire 
	 */
	public Date getTauxSPPublicationDecretsOrdosDebut() {
		return tauxSPPublicationDecretsOrdosDebut;
	}

	/**
	 * Date début publication décret taux d'exécution Session parlementaire 
	 */
	public void setTauxSPPublicationDecretsOrdosDebut(Date tauxSPPublicationDecretsOrdosDebut) {
		this.tauxSPPublicationDecretsOrdosDebut = tauxSPPublicationDecretsOrdosDebut;
	}

	/**
	 * Date fin publication décret taux d'exécution Session parlementaire 
	 */
	public Date getTauxSPPublicationDecretsOrdosFin() {
		return tauxSPPublicationDecretsOrdosFin;
	}

	/**
	 * Date fin publication décret taux d'exécution Session parlementaire 
	 */
	public void setTauxSPPublicationDecretsOrdosFin(Date tauxSPPublicationDecretsOrdosFin) {
		this.tauxSPPublicationDecretsOrdosFin = tauxSPPublicationDecretsOrdosFin;
	}
}
