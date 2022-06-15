using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Collections.Specialized;
using LightJson;
using LightJson.Serialization;
using solonng_launcher.utils;
using System.Net;
using System.IO;

namespace solonng_launcher
{
     class FileContext
    {
        const String PARAM_FILENAME = "filename";
        const String PARAM_DOCUMENT_ID = "id";
        public object lockFlag = new object();
        //public readonly object Lock = new object();
        public string Filename { get; set; }
        public string FileExtension { get; private set; }
        public string FileType { get; set; }
        public string LbNuxeoRoute { get; set; }
        public string DocumentId { get; set; }
        public string CreationDate { get; set; }
        public string JsonFilename { get; set; }
        public string DocumentFilename { get; set; }

        public string CheckSum { get; private set; }
        public int ReUploadCounter { get; set; }
        public int OpenFileCounter { get; set; }
        public string Version { get; set; }

        public Statut Statut { get; set; }

        public byte[] File { get;  set; }

        public bool CanShowLockedError { get; set; } = true;
        public bool CanShowErrors { get; set; } = true;

        public void LoadFile(byte[] file)
        {
            File = file;
            CheckSum = Helpers.GenerateCheckSumSHA256(file);
        }
      

        public string FullPathDocument
        {
            get
            {
                return $"{Constants.SolonDocumentFolder}\\{this.Filename}";
            }
        }

        public string FullPathJson
        {
            get
            {
                return $"{Constants.SolonHomeFolder}\\{this.JsonFilename}";
            }
        }

        public FileContext(string documentId, string fileName)
        {
            Filename = fileName;
            DocumentId = documentId;
            DocumentFilename = $"{this.DocumentId}_{this.Filename}";
            JsonFilename = $"{this.DocumentId}_{this.Filename}.json";
        }

        public FileContext(JsonValue fileContextJson)
        {

            LogHelper.DebugInformation($"Starting the FileContext from the json value {fileContextJson}");

            DocumentId = fileContextJson[ContextFileJsonConstants.PARAM_DOCUMENT_ID];

            CreationDate = fileContextJson[ContextFileJsonConstants.PARAM_CREATION_DATE];
            Filename = fileContextJson[ContextFileJsonConstants.PARAM_FILENAME];
            FileExtension = Path.GetExtension(Filename).ToLower();
            DocumentFilename = fileContextJson[ContextFileJsonConstants.PARAM_DOCUMENT_FILE_NAME];
            JsonFilename = fileContextJson[ContextFileJsonConstants.PARAM_JSON_FILE_NAME];
            Version = fileContextJson[ContextFileJsonConstants.PARAM_VERSION];
            FileType = fileContextJson[ContextFileJsonConstants.PARAM_FILE_TYPE];
            File = null;
            CheckSum = fileContextJson[ContextFileJsonConstants.PARAM_CHECK_SUM];
            if (int.TryParse(fileContextJson[ContextFileJsonConstants.PARAM_STATUT_CODE].AsString, out int status) && status < Constants.STATUTS.Length)
            {
                Statut = Constants.STATUTS[status];
            }
            else
            {
                Statut = null;
            }
            LogHelper.DebugInformation($"Filename: {Filename}");
            LogHelper.DebugInformation($"DocumentId: {DocumentId}");
            LogHelper.DebugInformation($"Document File Name: {DocumentFilename}");
            LogHelper.DebugInformation($"JSON File Name: {JsonFilename}");
            LogHelper.DebugInformation($"Statut: {Statut.ToString()}");
            LogHelper.DebugInformation($"FileExtension: {FileExtension}");

            LogHelper.DebugInformation($"FileContext Loaded.");
        }
        public FileContext(Uri uri)
        {

            LogHelper.DebugInformation($"Starting the FileContext...");

            NameValueCollection queryValues = HttpUtility.ParseQueryString(uri.Query);
            Filename = queryValues[PARAM_FILENAME];
            DocumentId = queryValues[PARAM_DOCUMENT_ID];
            DateTime dt = DateTime.Now;
            CreationDate = dt.ToShortDateString() + " " + dt.ToShortTimeString();
            DocumentFilename = $"{this.DocumentId}_{this.Filename}";
            JsonFilename = $"{this.DocumentId}_{this.Filename}.json";
            File = null;
            Statut = Constants.STATUT_DOWNLOAD;
            FileExtension = Path.GetExtension(Filename).ToLower();
            LogHelper.DebugInformation($"Filename: {Filename}");
            LogHelper.DebugInformation($"DocumentId: {DocumentId}");
            LogHelper.DebugInformation($"Document File Name: {DocumentFilename}");
            LogHelper.DebugInformation($"JSON File Name: {JsonFilename}");
            LogHelper.DebugInformation($"Statut: {Statut.ToString()}");
            LogHelper.DebugInformation($"FileExtension: {FileExtension}");

            LogHelper.DebugInformation($"FileContext Loaded.");

        }



        public string ToJsonString()
        {
            var json = new JsonObject().Add(ContextFileJsonConstants.PARAM_DOCUMENT_ID, new JsonValue(DocumentId))
                                          .Add(ContextFileJsonConstants.PARAM_FILE_TYPE, new JsonValue(FileType))
                                          .Add(ContextFileJsonConstants.PARAM_CREATION_DATE, new JsonValue(CreationDate))
                                          .Add(ContextFileJsonConstants.PARAM_FILENAME, new JsonValue(Filename))
                                          .Add(ContextFileJsonConstants.PARAM_DOCUMENT_FILE_NAME, new JsonValue(DocumentFilename))
                                          .Add(ContextFileJsonConstants.PARAM_JSON_FILE_NAME, new JsonValue(JsonFilename))
                                          .Add(ContextFileJsonConstants.PARAM_STATUT_CODE, new JsonValue(Statut.Code))
                                          .Add(ContextFileJsonConstants.PARAM_STATUT_LABEL, new JsonValue(Statut.Label))
                                          .Add(ContextFileJsonConstants.PARAM_CHECK_SUM, new JsonValue(CheckSum))
                                          .Add(ContextFileJsonConstants.PARAM_VERSION, new JsonValue(Version))
                                         .ToString(true);
            return json;
        }


        
    }
}
