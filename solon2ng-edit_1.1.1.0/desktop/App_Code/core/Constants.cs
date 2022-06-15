using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using LightJson;
using solonng_launcher.Resources;
using solonng_launcher.utils;

namespace solonng_launcher
{
     static class Constants
    {

        /// <summary>
        /// Represents the name of the root directory
        /// see App.Config for the value.
        /// </summary>
        public static string FOLDER_SOLON_EDIT {
            get {
                return Helpers.GetConfigurationStringValue("FOLDER_SOLON_EDIT"); 
            }
        }
        /// <summary>
        /// Represents the name of the root directory
        /// see App.Config for the value.
        /// </summary>
        public static string FOLDER_SOLON_DOCUMENT
        {
            get {
                return Helpers.GetConfigurationStringValue("FOLDER_SOLON_DOCUMENT"); 
            }
        }

        /// <summary>
        /// Represents the name of the logs directory 
        /// see App.Config for the value.
        /// </summary>
        public static string FOLDER_SOLON_LOG
        {
            get
            {
                return Helpers.GetConfigurationStringValue("FOLDER_SOLON_LOG");
            }
        }
        /// <summary>
        /// Represents the file that contains the documents index  
        /// see App.Config for the value.
        /// </summary>
        public static string FILE_SOLON_NOTIFIER
        {
            get
            {
                return Helpers.GetConfigurationStringValue("FILE_SOLON_NOTIFIER");
            }
        }
        /// <summary>
        /// Represents the file that contains the headers and threadSleep 
        /// see ApiHandler
        /// see App.Config for the value.
        /// </summary>
        public static string FILE_HEADERS
        {
            get
            {
                return Helpers.GetConfigurationStringValue("FILE_HEADERS");
            }
        }

        // ContextFile Status
        public static readonly Statut STATUT_DOWNLOAD = new Statut(0, ApplicationMessages.StatutDownload);
        public static readonly Statut STATUT_NEW = new Statut(1, ApplicationMessages.StatutNew);
        public static readonly Statut STATUT_PRE_EDIT = new Statut(2, ApplicationMessages.StatutPreEdit);
        public static readonly Statut STATUT_EDITING = new Statut(3, ApplicationMessages.StatutEditing);
        public static readonly Statut STATUT_SAVED = new Statut(4, ApplicationMessages.StatutSaved);
        public static readonly Statut STATUT_LOCKED = new Statut(5, ApplicationMessages.StatutLocked);
        public static readonly Statut STATUT_NOT_FOUND = new Statut(6, ApplicationMessages.StatutNotFound);
        public static readonly Statut[] STATUTS = new Statut[] { STATUT_DOWNLOAD, STATUT_NEW, STATUT_PRE_EDIT, STATUT_EDITING, STATUT_SAVED, STATUT_LOCKED, STATUT_NOT_FOUND };

        // valid extentions 

        public static readonly HashSet<string> VALID_EXTENSIONS = new HashSet<string>
        {
            "doc",//"application/msword"
            "docx", //"application/msword"
            "dot", //"application/msword"
            "odt", // open office
            "rtf",
            "xla", //"application/excel");
            "xlb",// "application/excel");
            "xlc", //"application/excel");
            "xld", //"application/excel");
            "xlk", //"application/excel");
            "xll", //"application/excel");
            "xlm", //"application/excel");
            "xls", //"application/excel");
            "xlsx", //"application/excel");
            "xlt", //"application/excel");
            "xlv", //"application/excel");
            "xlw",//"application/excel");
            "csv",
            "pdf"
        };
        //valid memetypes
        public static readonly HashSet<string> VALID_MIME_TYPES = new HashSet<string>
        {
            "application/x-zip-compressed",
            "application/octet-stream",
            "application/pdf"
        };
        /// <summary>
        /// Directory containing all physical files.
        /// </summary>
        public static string SolonDocumentFolder
        {
            get
            {
                return $"{HomePath}\\{FOLDER_SOLON_EDIT}\\{Constants.FOLDER_SOLON_DOCUMENT}";
            }
        }

        /// <summary>
        /// Directory containing all applications files.
        /// </summary>
        public static string SolonHomeFolder
        {
            get
            {
                return $"{ HomePath}\\{FOLDER_SOLON_EDIT}";
            }
        }
         /// <summary>
        /// Directory containing all applications logs.
        /// </summary>
        public static string SolonLogFolder
        {
            get
            {
                return $"{HomePath}\\{FOLDER_SOLON_EDIT}\\{Constants.FOLDER_SOLON_LOG}";
            }
        }

        /// <summary>
        /// Root directory that contains the home directory and the log files.
        /// </summary>
        public static string HomePath
        {
            get
            {
                return Environment.ExpandEnvironmentVariables("%UserProfile%");
            }
        }
   
        /// <summary>
        /// Contain all files that must be processed.
        /// </summary>
        public static string SolonNotififierFile
        {
            get
            {
                return $"{SolonHomeFolder}\\{Constants.FILE_SOLON_NOTIFIER}";
            }
        }
        /// <summary>
        /// Contains all header and cookies informations.
        /// </summary>
        public static string SolonHeaderFile
        {
            get
            {
                return $"{SolonHomeFolder}\\{Constants.FILE_HEADERS}";
            }
        }

        
    }

}
