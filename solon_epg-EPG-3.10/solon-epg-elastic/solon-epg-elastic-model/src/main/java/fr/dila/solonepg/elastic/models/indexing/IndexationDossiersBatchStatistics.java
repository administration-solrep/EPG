package fr.dila.solonepg.elastic.models.indexing;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;

public class IndexationDossiersBatchStatistics {

	private static final ThreadLocal<IndexationDossiersBatchStatistics> statistics =
			new ThreadLocal<IndexationDossiersBatchStatistics>();

	private AtomicLong startTimeMillis = new AtomicLong(0);
	private AtomicInteger dossier = new AtomicInteger(0);
	private AtomicInteger dossierAIndexer = new AtomicInteger(0);
	/** nombre de dossiers avec une erreur (hors erreur document) */
	private AtomicInteger dossierError = new AtomicInteger(0);
	/** nombre de dossiers dont la génération json a entrainé une erreur */
	private AtomicInteger jsonError = new AtomicInteger(0);
	private AtomicInteger lotSize = new AtomicInteger(0);
	private AtomicInteger dossierSuccess = new AtomicInteger(0);
	/** nombre de dossiers avec une erreur (hors erreur de document) */ 
	private AtomicInteger dossierDocumentError = new AtomicInteger(0);
	/** nombre de dossiers avec au moins un document manquant */
	private AtomicInteger dossierDocumentMissing = new AtomicInteger(0);
	/** nombre de dossiers avec au moins une erreur d'extraction textuelle */
	private AtomicInteger dossierDocumentNotParsed = new AtomicInteger(0);
	/** nombre de dossiers avec au moins un document vide */
	private AtomicInteger dossierDocumentEmpty = new AtomicInteger(0);
	/** nombre de dossiers avec au moins un document trop volumineux */
	private AtomicInteger dossierDocumentTooLarge = new AtomicInteger(0);

	private AtomicInteger document = new AtomicInteger(0);
	private AtomicInteger documentMissing = new AtomicInteger(0);
	private AtomicInteger documentNotParsed = new AtomicInteger(0);
	private AtomicInteger documentEmpty = new AtomicInteger(0);
	private AtomicInteger documentTooLarge = new AtomicInteger(0);
	private AtomicInteger documentSuccess = new AtomicInteger(0);
	private AtomicInteger documentDoublons = new AtomicInteger(0);
	private AtomicInteger documentUnknownError = new AtomicInteger(0);

	public void reset() {
		startTimeMillis.set(0);
		lotSize.set(0);
		dossier.set(0);
		dossierAIndexer.set(0);
		dossierError.set(0);
		jsonError.set(0);
		document.set(0);
		dossierSuccess.set(0);
		dossierDocumentError.set(0);
		dossierDocumentMissing.set(0);
		dossierDocumentNotParsed.set(0);
		dossierDocumentEmpty.set(0);
		dossierDocumentTooLarge.set(0);

		document.set(0);
		documentMissing.set(0);
		documentNotParsed.set(0);
		documentEmpty.set(0);
		documentTooLarge.set(0);
		documentSuccess.set(0);
		documentDoublons.set(0);
		documentUnknownError.set(0);
	}

	public void start(int nombreDeDossiersAIndexer, int lotSize) {
		this.dossierAIndexer.set(nombreDeDossiersAIndexer);
		this.lotSize.set(lotSize);
		this.startTimeMillis.set(System.currentTimeMillis());
	}

	public int getDossier() {
		return dossier.get();
	}

	public int getDossierAIndexer() {
		return dossierAIndexer.get();
	}

	public long getStartTimeMillis() {
		return startTimeMillis.get();
	}

	private void _log(Logger logger) {
		logger.info(String.format("\nTotal \tTraités \tSuccess \tJson error \tError \tDoc error \tParse \tMissing \tEmpty \tUnknown \tLarge\n"
				+ "%d \t%d \t%d \t%d \t%d \t%d \t%d \t%d \t%d \t%d \t%d",
				dossierAIndexer.get(),	// nombre de dossiers à indexer
				dossier.get(),			// nombre de dossiers traités
				dossierSuccess.get(),	// nombre de dossiers sans erreur
				jsonError.get(),		// nombre de dossiers dont le json n'a pas pu être généré
				dossierError.get(),		// nombre de dossiers avec une erreur de mapping autre qu'une erreur de document

				// nombre de dossier avec un moins une erreur de document
				dossierDocumentNotParsed.get() + dossierDocumentMissing.get() + dossierDocumentEmpty.get() + dossierDocumentError.get(),

				// nombre de dossier par erreur de document
				dossierDocumentNotParsed.get(),
				dossierDocumentMissing.get(),
				dossierDocumentEmpty.get(),
				// nombre de dossier avec au moins une erreur de document non catégorisée
				dossierDocumentError.get(),
				
				// non décompté comme une erreur
				dossierDocumentTooLarge.get()
		));
		logger.info(String.format("\nDocuments \tSuccess \tParse \tMissing \tEmpty \tUnknown \tLarge \tDoublons\n"
				+ "%d \t%d \t%d \t%d \t%d \t%d \t%d \t%d",
				document.get(),
				documentSuccess.get(),
				documentNotParsed.get(),
				documentMissing.get(),
				documentEmpty.get(),
				documentUnknownError.get(),
				documentTooLarge.get(),
				documentDoublons.get()
		));
	}

	public static IndexationDossiersBatchStatistics get() {
		return statistics.get();
	}

	public static final IndexationDossiersBatchStatistics create() {
		IndexationDossiersBatchStatistics s = new IndexationDossiersBatchStatistics();
		s.reset();
		return s;
	}

	/**
	 * Doit être appelé avec un try {} finally { {@link #unset()} }
	 */
	public static final void set(IndexationDossiersBatchStatistics s) {
		if (statistics.get() != null) {
			throw new IllegalStateException(String.format(
					"%s déjà positionné sur le thread", IndexationDossiersBatchStatistics.class.getSimpleName()));
		}
		statistics.set(s);
	}

	public static final void unset() {
		statistics.set(null);
	}

	public static void dossierDocumentNotParsed() {
		statistics.get().dossierDocumentNotParsed.incrementAndGet();
	}

	public static void dossierDocumentMissing() {
		statistics.get().dossierDocumentMissing.incrementAndGet();
	}

	public static void dossierDocumentEmpty() {
		statistics.get().dossierDocumentEmpty.incrementAndGet();
	}

	public static void dossierDocumentTooLarge() {
		statistics.get().dossierDocumentTooLarge.incrementAndGet();
	}

	public static void dossierDocumentError() {
		statistics.get().dossierDocumentError.incrementAndGet();
	}

	public static void dossierSuccess() {
		statistics.get().dossierSuccess.incrementAndGet();
	}

	public static void dossier() {
		statistics.get().dossier.incrementAndGet();
	}

	public static void dossierError() {
		statistics.get().dossierError.incrementAndGet();
	}

	public static void jsonError() {
		statistics.get().jsonError.incrementAndGet();
	}

	public static void document(int number) {
		statistics.get().document.addAndGet(number);
	}

	public static void documentEmpty(int number) {
		statistics.get().documentEmpty.addAndGet(number);
	}

	public static void documentMissing(int number) {
		statistics.get().documentMissing.addAndGet(number);
	}

	public static void documentNotParsed(int number) {
		statistics.get().documentNotParsed.addAndGet(number);
	}

	public static void documentSuccess(int number) {
		statistics.get().documentSuccess.addAndGet(number);
	}

	public static void documentTooLarge(int number) {
		statistics.get().documentTooLarge.addAndGet(number);
	}

	public static void documentDoublons(int number) {
		statistics.get().documentDoublons.addAndGet(number);
	}

	public static void documentUnknownError(int number) {
		statistics.get().documentUnknownError.addAndGet(number);
	}

	public static void log(Logger logger) {
		statistics.get()._log(logger);
	}

}
