package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class ElasticDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String UID = "UID";
    public static final String PATH = "path";
    public static final String FILE_SYSTEM_PATH = "file:binaryPath";
    public static final String COMMON_ICON = "common:icon";
    public static final String COMMON_ICON_EXPANDED = "common:icon-expanded";
    public static final String COMMON_SIZE = "common:size";
    public static final String DC_CONTRIBUTORS = "dc:contributors";
    public static final String DC_COVERAGE = "dc:coverage";
    public static final String DC_CREATED = "dc:created";
    public static final String DC_CREATOR = "dc:creator";
    public static final String DC_EXPIRED = "dc:expired";
    public static final String DC_FORMAT = "dc:format";
    public static final String DC_ISSUED = "dc:issued";
    public static final String DC_LANGUAGE = "dc:language";
    public static final String DC_LAST_CONTRIBUTOR = "dc:lastContributor";
    public static final String DC_MODIFIED = "dc:modified";
    public static final String DC_NATURE = "dc:nature";
    public static final String DC_RIGHTS = "dc:rights";
    public static final String DC_SOURCE = "dc:source";
    public static final String DC_SUBJECTS = "dc:subjects";
    public static final String DC_TITLE = "dc:title";
    public static final String DC_VALID = "dc:valid";
    public static final String FILE_CONTENT = "file:content";
    public static final String FILE_NAME = "file:name";
    public static final String FILE_MIME_TYPE = "file:mime-type";
    public static final String FILE_ENCODING = "file:encoding";
    public static final String FILE_DIGEST = "file:digest";
    public static final String FILE_LENGTH = "file:length";
    public static final String FILE_DATA = "file:data";
    public static final String FILE_FILENAME = "file:filename";
    public static final String FILEPG_ENTITE = "filepg:entite";
    public static final String FILEPG_FILETYPE_ID = "filepg:filetypeId";
    public static final String FILEPG_RELATED_DOCUMENT = "filepg:relatedDocument";
    public static final String FILES_FILES = "files:files";
    public static final String UID_MAJOR_VERSION = "uid:major_version";
    public static final String UID_MINOR_VERSION = "uid:minor_version";
    public static final String UID_UID = "uid:uid";
    public static final String FILE_CURRENTVERSION = "file:currentVersion";

    @JsonProperty(UID)
    private String uid;

    @JsonProperty(PATH)
    private String path;

    @JsonProperty(FILE_SYSTEM_PATH)
    private String fileSystemPath;

    public String getFileSystemPath() {
        return fileSystemPath;
    }

    public void setFileSystemPath(String fileSystemPath) {
        this.fileSystemPath = fileSystemPath;
    }

    /* common:... */
    @JsonProperty(COMMON_ICON)
    private String commonIcon;

    @JsonProperty(COMMON_ICON_EXPANDED)
    private String commonIconExpanded;

    @JsonProperty(COMMON_SIZE)
    private Long commonSize;

    /* dc:... */
    @JsonProperty(DC_CONTRIBUTORS)
    private String[] dcContributors;

    @JsonProperty(DC_COVERAGE)
    private String dcCoverage;

    @JsonProperty(DC_CREATED)
    private Date dcCreated;

    @JsonProperty(DC_CREATOR)
    private String dcCreator;

    @JsonProperty(DC_EXPIRED)
    private Date dcExpired;

    @JsonProperty(DC_FORMAT)
    private String dcFormat;

    @JsonProperty(DC_ISSUED)
    private String dcIssued;

    @JsonProperty(DC_LANGUAGE)
    private String dcLanguage;

    @JsonProperty(DC_LAST_CONTRIBUTOR)
    private String dcLastContributor;

    @JsonProperty(DC_MODIFIED)
    private Date dcModified;

    @JsonProperty(DC_NATURE)
    private String dcNature;

    @JsonProperty(DC_RIGHTS)
    private String dcRights;

    @JsonProperty(DC_SOURCE)
    private String dcSource;

    @JsonProperty(DC_SUBJECTS)
    private String[] dcSubjects;

    @JsonProperty(DC_TITLE)
    private String dcTitle;

    @JsonProperty(DC_VALID)
    private String dcValid;

    /* file:... */
    @JsonProperty(FILE_NAME)
    private String fileName;

    @JsonProperty(FILE_MIME_TYPE)
    private String fileMimeType;

    @JsonProperty(FILE_ENCODING)
    private String fileEncoding;

    @JsonProperty(FILE_DIGEST)
    private String fileDigest;

    @JsonProperty(FILE_LENGTH)
    private long fileLength;

    @JsonProperty(FILE_DATA)
    private String fileData;

    @JsonProperty(FILE_FILENAME)
    private String fileFilename;

    /* filepg:... */
    @JsonProperty(FILEPG_ENTITE)
    private String filepgEntite;

    @JsonProperty(FILEPG_FILETYPE_ID)
    private Long filepgFiletypeId;

    @JsonProperty(FILEPG_RELATED_DOCUMENT)
    private String filepgRelatedDocument;

    /* files:... */
    @JsonProperty(FILES_FILES)
    private String files;

    /* uid:... */
    @JsonProperty(UID_MAJOR_VERSION)
    private Long uidMajorVersion;

    @JsonProperty(UID_MINOR_VERSION)
    private Long uidMinorVersion;

    @JsonProperty(UID_UID)
    private String uidUid;

    @JsonProperty(FILE_CURRENTVERSION)
    private Boolean currentVersion;

    /**
     * @return the commonIcon
     */
    public String getCommonIcon() {
        return commonIcon;
    }

    /**
     * @param commonIcon
     *            the commonIcon to set
     */
    public void setCommonIcon(String commonIcon) {
        this.commonIcon = commonIcon;
    }

    /**
     * @return the commonIconExpanded
     */
    public String getCommonIconExpanded() {
        return commonIconExpanded;
    }

    /**
     * @param commonIconExpanded
     *            the commonIconExpanded to set
     */
    public void setCommonIconExpanded(String commonIconExpanded) {
        this.commonIconExpanded = commonIconExpanded;
    }

    /**
     * @return the commonSize
     */
    public Long getCommonSize() {
        return commonSize;
    }

    /**
     * @param commonSize
     *            the commonSize to set
     */
    public void setCommonSize(Long commonSize) {
        this.commonSize = commonSize;
    }

    /**
     * @return the dcContributors
     */
    public String[] getDcContributors() {
        return dcContributors;
    }

    /**
     * @param dcContributors
     *            the dcContributors to set
     */
    public void setDcContributors(String[] dcContributors) {
        this.dcContributors = dcContributors;
    }

    /**
     * @return the dcCoverage
     */
    public String getDcCoverage() {
        return dcCoverage;
    }

    /**
     * @param dcCoverage
     *            the dcCoverage to set
     */
    public void setDcCoverage(String dcCoverage) {
        this.dcCoverage = dcCoverage;
    }

    /**
     * @return the dcCreated
     */
    public Date getDcCreated() {
        return dcCreated;
    }

    /**
     * @param dcCreated
     *            the dcCreated to set
     */
    public void setDcCreated(Date dcCreated) {
        this.dcCreated = dcCreated;
    }

    /**
     * @return the dcCreator
     */
    public String getDcCreator() {
        return dcCreator;
    }

    /**
     * @param dcCreator
     *            the dcCreator to set
     */
    public void setDcCreator(String dcCreator) {
        this.dcCreator = dcCreator;
    }

    /**
     * @return the dcExpired
     */
    public Date getDcExpired() {
        return dcExpired;
    }

    /**
     * @param dcExpired
     *            the dcExpired to set
     */
    public void setDcExpired(Date dcExpired) {
        this.dcExpired = dcExpired;
    }

    /**
     * @return the dcFormat
     */
    public String getDcFormat() {
        return dcFormat;
    }

    /**
     * @param dcFormat
     *            the dcFormat to set
     */
    public void setDcFormat(String dcFormat) {
        this.dcFormat = dcFormat;
    }

    /**
     * @return the dcIssued
     */
    public String getDcIssued() {
        return dcIssued;
    }

    /**
     * @param dcIssued
     *            the dcIssued to set
     */
    public void setDcIssued(String dcIssued) {
        this.dcIssued = dcIssued;
    }

    /**
     * @return the dcLanguage
     */
    public String getDcLanguage() {
        return dcLanguage;
    }

    /**
     * @param dcLanguage
     *            the dcLanguage to set
     */
    public void setDcLanguage(String dcLanguage) {
        this.dcLanguage = dcLanguage;
    }

    /**
     * @return the dcLastContributor
     */
    public String getDcLastContributor() {
        return dcLastContributor;
    }

    /**
     * @param dcLastContributor
     *            the dcLastContributor to set
     */
    public void setDcLastContributor(String dcLastContributor) {
        this.dcLastContributor = dcLastContributor;
    }

    /**
     * @return the dcModified
     */
    public Date getDcModified() {
        return dcModified;
    }

    /**
     * @param dcModified
     *            the dcModified to set
     */
    public void setDcModified(Date dcModified) {
        this.dcModified = dcModified;
    }

    /**
     * @return the dcNature
     */
    public String getDcNature() {
        return dcNature;
    }

    /**
     * @param dcNature
     *            the dcNature to set
     */
    public void setDcNature(String dcNature) {
        this.dcNature = dcNature;
    }

    /**
     * @return the dcRights
     */
    public String getDcRights() {
        return dcRights;
    }

    /**
     * @param dcRights
     *            the dcRights to set
     */
    public void setDcRights(String dcRights) {
        this.dcRights = dcRights;
    }

    /**
     * @return the dcSource
     */
    public String getDcSource() {
        return dcSource;
    }

    /**
     * @param dcSource
     *            the dcSource to set
     */
    public void setDcSource(String dcSource) {
        this.dcSource = dcSource;
    }

    /**
     * @return the dcSubjects
     */
    public String[] getDcSubjects() {
        return dcSubjects;
    }

    /**
     * @param dcSubjects
     *            the dcSubjects to set
     */
    public void setDcSubjects(String[] dcSubjects) {
        this.dcSubjects = dcSubjects;
    }

    /**
     * @return the dcTitle
     */
    public String getDcTitle() {
        return dcTitle;
    }

    /**
     * @param dcTitle
     *            the dcTitle to set
     */
    public void setDcTitle(String dcTitle) {
        this.dcTitle = dcTitle;
    }

    /**
     * @return the dcValid
     */
    public String getDcValid() {
        return dcValid;
    }

    /**
     * @param dcValid
     *            the dcValid to set
     */
    public void setDcValid(String dcValid) {
        this.dcValid = dcValid;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileMimeType
     */
    public String getFileMimeType() {
        return fileMimeType;
    }

    /**
     * @param fileMimeType
     *            the fileMimeType to set
     */
    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

    /**
     * @return the fileEncoding
     */
    public String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * @param fileEncoding
     *            the fileEncoding to set
     */
    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    /**
     * @return the fileDigest
     */
    public String getFileDigest() {
        return fileDigest;
    }

    /**
     * @param fileDigest
     *            the fileDigest to set
     */
    public void setFileDigest(String fileDigest) {
        this.fileDigest = fileDigest;
    }

    /**
     * @return the fileLength
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * @param fileLength
     *            the fileLength to set
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    /**
     * @return the fileData
     */
    public String getFileData() {
        return fileData;
    }

    /**
     * @param fileData
     *            the fileData to set
     */
    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    /**
     * @return the fileFilename
     */
    public String getFileFilename() {
        return fileFilename;
    }

    /**
     * @param fileFilename
     *            the fileFilename to set
     */
    public void setFileFilename(String fileFilename) {
        this.fileFilename = fileFilename;
    }

    /**
     * @return the filepgEntite
     */
    public String getFilepgEntite() {
        return filepgEntite;
    }

    /**
     * @param filepgEntite
     *            the filepgEntite to set
     */
    public void setFilepgEntite(String filepgEntite) {
        this.filepgEntite = filepgEntite;
    }

    /**
     * @return the filepgFiletypeId
     */
    public Long getFilepgFiletypeId() {
        return filepgFiletypeId;
    }

    /**
     * @param filepgFiletypeId
     *            the filepgFiletypeId to set
     */
    public void setFilepgFiletypeId(Long filepgFiletypeId) {
        this.filepgFiletypeId = filepgFiletypeId;
    }

    /**
     * @return the filepgRelatedDocument
     */
    public String getFilepgRelatedDocument() {
        return filepgRelatedDocument;
    }

    /**
     * @param filepgRelatedDocument
     *            the filepgRelatedDocument to set
     */
    public void setFilepgRelatedDocument(String filepgRelatedDocument) {
        this.filepgRelatedDocument = filepgRelatedDocument;
    }

    /**
     * @return the files
     */
    public String getFiles() {
        return files;
    }

    /**
     * @param files
     *            the files to set
     */
    public void setFiles(String files) {
        this.files = files;
    }

    /**
     * @return the uidMajorVersion
     */
    public Long getUidMajorVersion() {
        return uidMajorVersion;
    }

    /**
     * @param uidMajorVersion
     *            the uidMajorVersion to set
     */
    public void setUidMajorVersion(Long uidMajorVersion) {
        this.uidMajorVersion = uidMajorVersion;
    }

    /**
     * @return the uidMinorVersion
     */
    public Long getUidMinorVersion() {
        return uidMinorVersion;
    }

    /**
     * @param uidMinorVersion
     *            the uidMinorVersion to set
     */
    public void setUidMinorVersion(Long uidMinorVersion) {
        this.uidMinorVersion = uidMinorVersion;
    }

    @JsonIgnore
    public Optional<String> getVersion() {
        String version = null;
        if (uidMajorVersion != null && uidMinorVersion != null) {
            version = uidMajorVersion.toString() + '.' + uidMinorVersion;
        }
        return Optional.ofNullable(version);
    }

    /**
     * Alias for UID (@see {@link #getId()})
     */
    public String getId() {
        return getUid();
    }

    /**
     * Alias for UID (@see #setUid(String))
     * @param uid
     */
    public void setId(String uid) {
        setUid(uid);
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the uidUid
     */
    public String getUidUid() {
        return uidUid;
    }

    /**
     * @param uidUid
     *            the uidUid to set
     */
    public void setUidUid(String uidUid) {
        this.uidUid = uidUid;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Boolean currentVersion) {
        this.currentVersion = currentVersion;
    }
}
