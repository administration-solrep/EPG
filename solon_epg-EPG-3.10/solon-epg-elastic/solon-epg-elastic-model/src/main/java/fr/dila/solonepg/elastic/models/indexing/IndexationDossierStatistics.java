package fr.dila.solonepg.elastic.models.indexing;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexationDossierStatistics {

	private static final ThreadLocal<IndexationDossierStatistics> statistics =
			new ThreadLocal<IndexationDossierStatistics>();

	// Atomic* not really usefull as thread safety is not needed
	private AtomicLong startTimeMillis = new AtomicLong(0);
	private AtomicBoolean error = new AtomicBoolean(false);
	private AtomicBoolean jsonError = new AtomicBoolean(false);
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
		error.set(false);
		jsonError.set(false);
		document.set(0);
		documentMissing.set(0);
		documentNotParsed.set(0);
		documentEmpty.set(0);
		documentTooLarge.set(0);
		documentSuccess.set(0);
		documentDoublons.set(0);
		documentUnknownError.set(0);
	}

	public void start() {
		this.startTimeMillis.set(System.currentTimeMillis());
	}

	public void updateDossiersBatchStatistics() {
		if (error.get()) {
			IndexationDossiersBatchStatistics.dossierError();
		}
		if (jsonError.get()) {
			IndexationDossiersBatchStatistics.jsonError();
		}
		IndexationDossiersBatchStatistics.document(document.get());
		IndexationDossiersBatchStatistics.documentMissing(documentMissing.get());
		IndexationDossiersBatchStatistics.documentNotParsed(documentNotParsed.get());
		IndexationDossiersBatchStatistics.documentEmpty(documentEmpty.get());
		IndexationDossiersBatchStatistics.documentTooLarge(documentTooLarge.get());
		IndexationDossiersBatchStatistics.documentSuccess(documentSuccess.get());
		IndexationDossiersBatchStatistics.documentDoublons(documentDoublons.get());
		IndexationDossiersBatchStatistics.documentUnknownError(documentUnknownError.get());
		
		if (documentUnknownError.get() > 0) {
			IndexationDossiersBatchStatistics.dossierDocumentError();
		}
		if (documentMissing.get() > 0) {
			IndexationDossiersBatchStatistics.dossierDocumentMissing();
		}
		if (documentTooLarge.get() > 0) {
			IndexationDossiersBatchStatistics.dossierDocumentTooLarge();
		}
		if (documentEmpty.get() > 0) {
			IndexationDossiersBatchStatistics.dossierDocumentEmpty();
		}
		if (documentNotParsed.get() > 0) {
			IndexationDossiersBatchStatistics.dossierDocumentNotParsed();
		}
	}

	public static final IndexationDossierStatistics create() {
		IndexationDossierStatistics s = new IndexationDossierStatistics();
		s.reset();
		return s;
	}

	public boolean hasDocumentError() {
		return documentUnknownError.get() > 0
				|| documentMissing.get() > 0
				|| documentEmpty.get() > 0
				|| documentTooLarge.get() > 0
				|| documentNotParsed.get() > 0;
	}

	public boolean hasError() {
		return error.get();
	}

	public boolean hasJsonError() {
		return jsonError.get();
	}

	public boolean hasAnyError() {
		return hasError() || hasDocumentError() || hasJsonError();
	}

	public String warningMessage() {
		StringBuilder message = new StringBuilder();
		if (hasAnyError()) {
			if (hasDocumentError()) {
				message.append("Erreurs liées aux documents:\n");
				if (documentMissing.get() > 0) {
					message.append(String.format("manquants: %d\n", documentMissing.get()));
				}
				if (documentEmpty.get() > 0) {
					message.append(String.format("vides: %d\n", documentEmpty.get()));
				}
				if (documentNotParsed.get() > 0) {
					message.append(String.format("extraction: %d\n", documentNotParsed.get()));
				}
				if (documentTooLarge.get() > 0) {
					message.append(String.format("ignorés car trop volumineux: %d\n", documentTooLarge.get()));
				}
				if (documentUnknownError.get() > 0) {
					message.append(String.format("erreur non catégorisées: %d\n", documentUnknownError.get()));
				}
			}
			if (hasJsonError()) {
				message.append("Erreur d'extraction du fichier JSON");
			}
		}
		return message.toString();
	}

	/**
	 * Doit être appelé avec un try {} finally { {@link #unset()} }
	 */
	public static final void set(IndexationDossierStatistics s) {
		if (statistics.get() != null) {
			throw new IllegalStateException(String.format(
					"%s déjà positionné sur le thread", IndexationDossierStatistics.class.getSimpleName()));
		}
		statistics.set(s);
	}

	public static final void unset() {
		statistics.set(null);
	}

	public static void error() {
		statistics.get().error.set(true);
	}

	public static void jsonError() {
		statistics.get().jsonError.set(true);
	}

	public static void document() {
		statistics.get().document.incrementAndGet();
	}

	public static void documentEmpty() {
		statistics.get().documentEmpty.incrementAndGet();
	}

	public static void documentMissing() {
		statistics.get().documentMissing.incrementAndGet();
	}

	public static void documentNotParsed() {
		statistics.get().documentNotParsed.incrementAndGet();
	}

	public static void documentSuccess() {
		statistics.get().documentSuccess.incrementAndGet();
	}

	public static void documentTooLarge() {
		statistics.get().documentTooLarge.incrementAndGet();
	}

	public static void documentDoublons() {
		statistics.get().documentDoublons.incrementAndGet();
	}

	public static void documentError() {
		statistics.get().documentUnknownError.incrementAndGet();
	}

}
