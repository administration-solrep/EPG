package fr.dila.solonepg.adamant.test;

import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RunnerFeature;

@Deploy("fr.dila.solonepg.adamant")
public class SolonEPGAdamantFeature implements RunnerFeature {

	/**
	 * Initialisation des properties de test pour adamant
	 */
    @Override
    public void initialize(FeaturesRunner runner) {
        System.setProperty("solonepg.traitement.dossiers.fin.threadPoolSize", "1");
        System.setProperty("solonepg.traitement.dossiers.fix.acl.rattachement.threadPoolSize", "1");
        System.setProperty("mail.transient.store.max.size.target", "1");
        System.setProperty("mail.transient.store.max.size.absolute", "1");
        System.setProperty("mail.transient.store.max.time.unreleased", "1");
        System.setProperty("mail.transient.store.max.time.released", "1");
        
        System.setProperty("solon.archivage.definitif.repertoire", "target/archivage");
        System.setProperty("solon.archivage.definitif.balise.href", "../Profil%20SOLON%2013-04-2018-2.1.rng");
        System.setProperty("solon.archivage.definitif.archival.agreement", "FRAN_CE_0019");
        System.setProperty("solon.archivage.definitif.message.digest.algorithme", "MessageDigestAlgorithmCodeListVersion0");
        System.setProperty("solon.archivage.definitif.mime.type", "MimeTypeCodeListVersion0");
        System.setProperty("solon.archivage.definitif.file.format", "FileFormatCodeListVersion0");
        System.setProperty("solon.archivage.definitif.appraisal.rule.code.list.version", "AppraisalRuleCodeListVersion0");
        System.setProperty("solon.archivage.definitif.access.rule.code.list.version", "AccessRuleCodeListVersion0");
        System.setProperty("solon.archivage.definitif.dissemination.rule.code.list.version", "DisseminationRuleCodeListVersion0");
        System.setProperty("solon.archivage.definitif.reuse.rule.code.list.version", "ReuseRuleCodeListVersion0");
        System.setProperty("solon.archivage.definitif.acquisition.information", "AcquisitionInformationCodeListVersion0");
        System.setProperty("solon.archivage.definitif.service.level", "Contrat de service SOLON");
        System.setProperty("solon.archivage.definitif.archival.acency.identifier", "Archives nationales de France");
        System.setProperty("solon.archivage.definitif.transfering.agency.identifier", "Mission Premier ministre");
        System.setProperty("solon.archivage.definitif.appraisal.rule.rule", "APP-00001");
        System.setProperty("solon.archivage.definitif.appraisal.rule.final.action", "Keep");
        System.setProperty("solon.archivage.definitif.dissemination.rule.rule", "DIS-00011");
        System.setProperty("solon.archivage.definitif.reuse.rule.rule", "REU-00004");
        System.setProperty("solon.archivage.definitif.reply.code.list.version", "ReplyCodeListVersion0");
        System.setProperty("solon.archivage.definitif.encoding.code.list.version", "EncodingCodeListVersion0");
        System.setProperty("solon.archivage.definitif.compression.algorithm.code.list.version", "CompressionAlgorithmCodeListVersion0");
        System.setProperty("solon.archivage.definitif.data.object.version.code.list.version", "DataObjectVersionCodeListVersion0");
        System.setProperty("solon.archivage.definitif.storage.rule.code.list.version", "StorageRuleCodeListVersion0");
        System.setProperty("solon.archivage.definitif.classification.rule.code.list.version", "ClassificationRuleCodeListVersion0");
        System.setProperty("solon.archivage.definitif.relationship.code.list.version", "RelationshipCodeListVersion0");
        System.setProperty("solon.archivage.definitif.authorization.code.list.version", "AuthorizationReasonCodeListVersion0");
        System.setProperty("solon.archivage.definitif.access.rule.list.ACC_00002", "6,8,11,13,19,20,21,24,26,27,28,29,30,31,32,45");
        System.setProperty("solon.archivage.definitif.access.rule.list.ACC_00003", "10,23");
        System.setProperty("solon.archivage.definitif.access.rule.list.ACC_00020", "1,2,3,4,5,9,12,14,15,16,17,18,25,33,34,36,37,38,39,40,41,42,43,44,46,47");
        System.setProperty("solon.archivage.definitif.access.rule.list.ACC_00025", "7,22,48");
    }
}
