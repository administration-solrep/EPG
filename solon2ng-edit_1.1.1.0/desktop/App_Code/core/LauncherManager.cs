using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using solonng_launcher.utils;
using System.Diagnostics;
using System.Reflection;
using solonng_launcher.Resources;
using System.Windows.Forms;
using solonng_launcher.controls.modal;
using System.Security.Authentication;
using System.Net;
using System.Drawing.Text;
using System.Threading;

namespace solonng_launcher
{
    /// <summary>
    /// Handles the application start
    /// </summary>
     class LauncherManager
    {

        private bool _isApplicationStarted = false;
        private bool _canAddFileContext = true;

        /// <summary>
        /// Preparation du contexte, fichier sonde ...
        /// </summary>
        public void PrepareFoldersAndFile()
        {
            try
            {
                LogHelper.DebugInformation($"Preparing the folders and files");
                LogHelper.DebugInformation($"Checking if the directory exists : {Constants.SolonHomeFolder}");

                // Répertoire technique
                if (!Directory.Exists(Constants.SolonHomeFolder))
                {
                    LogHelper.DebugInformation($"Creating the directory: {Constants.SolonHomeFolder}");

                    Directory.CreateDirectory(Constants.SolonHomeFolder);
                }

                LogHelper.DebugInformation($"Checking if the directory exists : {Constants.SolonDocumentFolder}");

                // Répertoire de travail (zone de download)
                if (!Directory.Exists(Constants.SolonDocumentFolder))
                {
                    LogHelper.DebugInformation($"Creating the directory: {Constants.SolonDocumentFolder}");

                    Directory.CreateDirectory(Constants.SolonDocumentFolder);
                }
               
            }catch(Exception e)
            {
                LogHelper.LogError("", e);
            }
        }

     

        /// <summary>
        /// Check if another instance of the application is running
        /// </summary>
        /// <returns></returns>
        public bool IsAnotherInstanceOfApllicationRunning()
        {
            return Process.GetProcessesByName(Path.GetFileNameWithoutExtension(Assembly.GetEntryAssembly().Location)).Count() > 1;
        }

        /// <summary>
        /// Main instrance
        /// </summary>
        /// <param name="url"></param>
        public void LunchApplication(string url)
        {
            
            LogHelper.DebugInformation($"Lunching the application with the url: {url}");
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            // set the appropriate tls configuration to communicate with the api
            this.SetTLSConfiguration();
            // load the application fonts
            this.LoadCustomFonts();
           
            var globalContext = GlobalContext.Instance;
            var apiHandler = globalContext.ApiHandler;

            Uri uri;
            try
            {
                uri = new Uri(url);
                apiHandler.LoadConfigFromUri(uri);
                LogHelper.DebugInformation($"Parsing the url is done.");

            }
            catch (Exception e)
            {
                // uri is not valid
                MessageBox.Show(ApplicationMessages.MalFormedUrlError, "", MessageBoxButtons.OK, MessageBoxIcon.Error);
                LogHelper.LogError(LogMessages.MalFormedUrlError, e);
                return;
            }
            this.PrepareFoldersAndFile();

            apiHandler.SaveHeaders();
            if (apiHandler.IsAuthUri)
            {
                LogHelper.LogInformation(LogMessages.AuthUrlRefreshHeadersAndExit);
                return;
            }
            FileContext fileContext = new FileContext(uri);

            if (fileContext.Filename == null || fileContext.DocumentId == null)
            {
                LogHelper.LogError(string.Format(LogMessages.MissingParamsUrlError, (fileContext.Filename == null ? "Filename, " : "")
                    , (fileContext.DocumentId == null ? "DocumentId " : "")));

                return;
            }
            var isAnotherInstanceOfApllicationRunning = IsAnotherInstanceOfApllicationRunning();
            if (!isAnotherInstanceOfApllicationRunning)
            {
                //check the version
                var version = Helpers.GetSolonEditPublishedVersion();
                if (string.IsNullOrEmpty(version))
                {
                    SolonModal.Show(string.Format(ApplicationMessages.SolonEditVersionNotChecked), "Information", action2Text: "Valider");
                    LogHelper.LogInformation("coulnd check the version");
                }
                else
                {
                    var currentVersion = FileVersionInfo.GetVersionInfo(Assembly.GetExecutingAssembly().Location).FileVersion;
                    if (version != currentVersion)
                    {
                        LogHelper.LogInformation($"The version is different from the release version. \n\rInstalled version:{currentVersion}, Actif version (from the api): {version}");
                        SolonModal.Show($"{string.Format(ApplicationMessages.SolonEditVersionWarning, currentVersion)}", "Information", action2Text: "Valider");
                    }
                    else
                    {
                        LogHelper.LogInformation("The version is good");
                    }
                }
            }
            // check if the file is opened or already exists and then let the user chose
            this.CheckAndConfirmFileOpened(ref fileContext);

            // ---- Preparation du contexte


            // Env global
            LogHelper.DebugInformation($"Checking if we cann add the file context  {_canAddFileContext}");

            if (_canAddFileContext)
            {
                Helpers.SaveContexteFileToJson(fileContext);
                globalContext.AddFileIndex(fileContext);
            }



            if (isAnotherInstanceOfApllicationRunning)
            {
                LogHelper.LogInformation(LogMessages.ApplicationClosedOtherInstanceExistsInfo);
                //Another instance exists we already add the file context to the index we will let the thread controller do the rest
                return;
            }
            else
            {
                
                globalContext.LauncherManager = this;
                globalContext.GetPendingUnprocessedFiles();
                globalContext.StartThreadController();
                _isApplicationStarted = true;
                globalContext.StartMainWindow();
                


            }
        }
        
        /// <summary>
        /// Check if the file exists and if its is open and do the appropriate actions 
        /// if the file exists asks the user if he wants to replace the file or not, 
        /// if the file is opened inform the user that he must close it to continue the process
        /// </summary>
        /// <param name="fileContext"></param>
        private  void CheckAndConfirmFileOpened(ref FileContext fileContext)
        {
            var globalContext = GlobalContext.Instance;
            // Check if there is a default program for this kind of file

            var defaultProgram = Helpers.GetRecommendedProgramsByExtension(fileContext.FileExtension).FirstOrDefault();
            LogHelper.DebugInformation($"Checking the default program for opening {fileContext.FileExtension} is {defaultProgram}");

            if ( defaultProgram == null)
            {
                MessageBox.Show(string.Format("Vous n'avez pas de programme d'ouverture par defaut pour ce type de fichier", fileContext.Filename, fileContext.FileExtension),"",MessageBoxButtons.OK,MessageBoxIcon.Error);
                
                this.ExitApplication();
            }

            if (File.Exists(fileContext.FullPathDocument))
            {
                if (Helpers.IsHandleOnFile(fileContext.FullPathDocument))
                {
                   
                    var resultIsHandleOnFile = SolonModal.Show(string.Format(ApplicationMessages.FileBeingEditedCondirmation, fileContext.Filename), "Information"
                        , "Annuler", "", "Réessayer");
                    if (resultIsHandleOnFile == ModalDialogResult.Null || resultIsHandleOnFile == ModalDialogResult.Action3)
                    {
                        this.CheckAndConfirmFileOpened(ref fileContext);
                    }
                    else
                    {
                        globalContext.ApiHandler.NotifyFileIsNotBeingEdited(fileContext);
                        if (!File.Exists(Constants.SolonNotififierFile))
                        {
                            LogHelper.LogInformation(string.Format(LogMessages.AbortOpeningFileAndClosingApplicationInformation, fileContext.Filename, fileContext.DocumentId));
                            this.ExitApplication();
                        }
                        else
                        {
                            LogHelper.LogInformation(string.Format(LogMessages.AbortOpeningFileInformation, fileContext.Filename, fileContext.DocumentId));
                            _canAddFileContext = false;
                        }
                    }
                    return;
                }

                var result = SolonModal.Show(string.Format(ApplicationMessages.FileExistsConfirmation, fileContext.Filename), "Confirmation"
                    , "Abandonner", "Télecharger", "Conserver");
                



                if (result == ModalDialogResult.Action3)
                {
                    if (File.Exists(fileContext.FullPathJson))
                    {
                        fileContext = new FileContext(Helpers.JsonParseFile(fileContext.FullPathJson));
                    }
                    globalContext.ApiHandler.NotifyFileIsBeingLocallyEdited(fileContext);
                    fileContext.Statut = Constants.STATUT_NEW;

                }
                else if (result == ModalDialogResult.Action2)
                {
                    fileContext.Statut = Constants.STATUT_DOWNLOAD;
                }
                else if(result == ModalDialogResult.Close || result == ModalDialogResult.Action1)
                {

                    if (!File.Exists(Constants.SolonNotififierFile))
                    {
                        LogHelper.LogInformation(string.Format(LogMessages.AbortOpeningFileAndClosingApplicationInformation, fileContext.Filename,fileContext.DocumentId));
                        this.ExitApplication();
                    }
                    else
                    {
                        LogHelper.LogInformation(string.Format(LogMessages.AbortOpeningFileInformation, fileContext.Filename, fileContext.DocumentId));
                        _canAddFileContext = false;
                    }
                }
            }
          
              
            
            
        }

       /// <summary>
       /// Exit the application
       /// </summary>
        public void ExitApplication()
        {
            if (!_isApplicationStarted)
            {
                Environment.Exit(0);
            }
            var globalContext = GlobalContext.Instance;
            globalContext.ThreadController.Abort();
            globalContext.MainWindow.Exit();

        }

        private void LoadCustomFonts()
        {
            LogHelper.DebugInformation($"Loading Custom Fonts");

            App_Code.utils.FontsHelper.LoadRobotSlabFont();
        }

        private void SetTLSConfiguration()
        {
            // force tls 1.2 
            // see this link for tls1.2 on win7 using .net 3.5
            //https://support.microsoft.com/en-us/topic/support-for-tls-system-default-versions-included-in-the-net-framework-3-5-on-windows-server-2012-db7ff0cb-fc9e-6530-db50-6a3dfc2834ad

            const SslProtocols _Tls12 = (SslProtocols)0x00000C00;
            const SecurityProtocolType Tls12 = (SecurityProtocolType)_Tls12;
            ServicePointManager.SecurityProtocol = Tls12;
            LogHelper.DebugInformation($"Setting Up the TLS 1.2");

        }

    }
}
