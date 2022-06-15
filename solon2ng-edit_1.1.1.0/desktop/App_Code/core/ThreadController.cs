using solonng_launcher.controls.toast;
using solonng_launcher.Resources;
using solonng_launcher.utils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Threading;

namespace solonng_launcher
{
     class ThreadController
    {
        private static ThreadController _threadController;
        private readonly Thread _watcher = null;
        private bool _stop = false;
        private bool _canShowLogin = true;

        /// <summary>
        /// the number in millisecond the thread is going to sleep after opening a file
        /// (it's set by adding threadSleep to the query parameter of the url opening the file)
        /// </summary>
        public int OpenFileSleepDuration { get; set; }

        private MainWindow MainWindow
        {
            get
            {
                return GlobalContext.MainWindow;
            }
        }
        // represents the max of reuploadcounter in FileCOntext before it resets to 0 (it depends on the sleap thread here the thread sleeps for 500ms x 240 = 2 minutes)
        private const int MAX_UPLOAD_COUNTER = 240;
        // represents the max time of wait for opening a file (1 min)
        private const int MAX_OPEN_FILE_COUNTER = 120;
        // represents the max time of wait for allowing a redirection to login page (5min)
        private const int MAX_SHOW_Login_COUNTER = 600;

        public bool IsAlive
        {
            get {
                return _watcher.IsAlive;
            }

        }

        private GlobalContext GlobalContext
        {
            get
            {
                return GlobalContext.Instance;
            }
        }
        private ThreadController()
        {

            _watcher = new Thread(new ThreadStart(ThreadLoop));

        }
        public static ThreadController Instance
        {
            get
            {
                if (_threadController == null)
                {
                    _threadController = new ThreadController();
                }
                return _threadController;
            }
        }
       

        /// <summary>
        /// Start the thread
        /// </summary>
        public void Start()
        {
            _watcher.Start();
        }
        /// <summary>
        /// Stop the thread
        /// </summary>
        public void Abort()
        {
            _stop = true;
        }



        /// <summary>
        /// The main intrance of the thread
        /// </summary>
        private void ThreadLoop()
        {
            bool canRefreshDataGrid = false;
            int showLoginCounter = 0;
            while (Thread.CurrentThread.IsAlive && !_stop)
            {

                Thread.Sleep(500);
                if(MainWindow == null)
                {
                    continue;
                }
                Dictionary<string, FileContext> allFiles = GlobalContext.FileContextIndex;

                if ((this.GlobalContext.IsContextFilesChanged || canRefreshDataGrid && IsAlive))
                {
                    MainWindow.SafeRefreshMainDataGrid();
                    canRefreshDataGrid = false;
                }
                this.GlobalContext.RefreshCurrentFiles();
               

                GlobalContext.ApiHandler.RefreshHeaders();

                foreach (KeyValuePair<string, FileContext> entry in allFiles.ToList())
                {
                    FileContext fileContext = entry.Value;
                    int oldSatutCode = fileContext.Statut.Code;

                    //Download file section
                    if (fileContext.Statut.Equals(Constants.STATUT_DOWNLOAD))
                    {
                        //download the file and set the status to new
                        LogHelper.LogInformation(string.Format(LogMessages.FileDownloadStartedInfo,fileContext.Filename, fileContext.DocumentId));
                        var apiResponse = GlobalContext.ApiHandler.DownloadFile(fileContext);

                        if (apiResponse.Status != HttpStatusCode.OK)
                        {
                            //error  on download we remove the file from globalcontext
                            LogHelper.LogError(string.Format(LogMessages.DownloadErrorInfo, fileContext.Filename,fileContext.DocumentId, apiResponse.Status, apiResponse.Message));
                            HandleDownloadResponseError(apiResponse);
                            GlobalContext.RemoveFile(entry.Key,true);
                            continue;
                        }
                        else
                        {

                            var _bytes = apiResponse.File.Data;

                            // we load the file to the file context a checksum will be created to this file
                            fileContext.LoadFile(_bytes);

                            LogHelper.LogInformation(string.Format(LogMessages.FileDownloadSuccessInfo, fileContext.Filename, fileContext.DocumentId));
                            try
                            {
                                using (BinaryWriter sw = new BinaryWriter(File.Open(fileContext.FullPathDocument, FileMode.Create)))
                                {
                                    sw.Write(_bytes);
                                }
                                fileContext.Statut = Constants.STATUT_NEW;
                                fileContext.Version = apiResponse.File.Version;
                                Helpers.SaveContexteFileToJson(fileContext);
                            }
                            catch (IOException)
                            {
                                ShowNotification(string.Format("Le fichier {0} est dèja en cours d'édition", fileContext.Filename), ToastType.Error);
                            }
                        }
                        

                    }
                    //Opening the file section
                    if (fileContext.Statut.Equals(Constants.STATUT_NEW))
                    {
                       
                        if (File.Exists(fileContext.FullPathDocument))
                        {
                            if (Helpers.IsValidDocument(fileContext.FullPathDocument))
                            {
                                ShowNotification(string.Format(ApplicationMessages.FileBeingEditedNotification, fileContext.Filename), ToastType.Info);
                                LogHelper.LogInformation(string.Format(LogMessages.OpeningFileToEditInfo, fileContext.Filename, fileContext.DocumentId));
                                
                                Helpers.StartProcess(fileContext.FullPathDocument);

                                Thread.Sleep(OpenFileSleepDuration);
                                fileContext.Statut = Constants.STATUT_PRE_EDIT;
                                LogHelper.LogInformation(string.Format(LogMessages.FileBeingEditedInfo, fileContext.Filename, fileContext.DocumentId));
                            }
                            else
                            {
                                LogHelper.LogInformation(string.Format(LogMessages.InvalidFileType, fileContext.Filename, fileContext.DocumentId));
                                ShowNotification(string.Format(ApplicationMessages.InvalidFileType, fileContext.Filename), ToastType.Error);

                                GlobalContext.RemoveFile(entry.Key);
                            }
                        }
                        else
                        {
                            GlobalContext.RemoveFile(entry.Key);
                        }

                    }
                    else if (fileContext.Statut.Equal(Constants.STATUT_PRE_EDIT)) // Checking if the file is opened (have the handle)
                    {
                        if (Helpers.IsHandleOnFile(fileContext.FullPathDocument))
                        {
                            fileContext.Statut = Constants.STATUT_EDITING;
                        }
                        else
                        {
                            if (fileContext.OpenFileCounter >= MAX_OPEN_FILE_COUNTER)
                            {
                                GlobalContext.RemoveFile(entry.Key);
                                LogHelper.LogError(string.Format(LogMessages.ErrorOpeningFile, fileContext.Filename, fileContext.DocumentId));
                                ShowNotification(string.Format(ApplicationMessages.ErrorOpeningFile, fileContext.Filename), ToastType.Error);

                                continue;
                            }
                            fileContext.OpenFileCounter++;

                        }
                    }
                    else if (fileContext.Statut.Equals(Constants.STATUT_EDITING)) // Editing Section
                    {
                        // file is closed (lost the handle) 
                        if (!Helpers.IsHandleOnFile(fileContext.FullPathDocument))
                        {
                            var oldChekSum = fileContext.CheckSum;
                            // the new file from the user directory
                            byte[] newFile = null;
                            try
                            {
                                newFile = File.ReadAllBytes(fileContext.FullPathDocument);

                            }catch(Exception e)
                            {
                                LogHelper.LogError("Erreur", e);
                            }
                            if (newFile != null)
                            {
                                //calculate the new checksum
                                var newChekSum = Helpers.GenerateCheckSumSHA256(newFile);
                                // checksum comparison we check if the file was updated
                                if (newChekSum.Equals(oldChekSum))
                                {
                                    try
                                    {
                                        LogHelper.LogInformation(string.Format(LogMessages.RemovingIdenticalFile, fileContext.Filename, fileContext.DocumentId));
                                        GlobalContext.RemoveFile(entry.Key);
                                        GlobalContext.ApiHandler.NotifyFileIsNotBeingEdited(fileContext);
                                    }
                                    catch (Exception e)
                                    {
                                        LogHelper.LogError(string.Format(LogMessages.RemovingFileError, fileContext.Filename, fileContext.DocumentId), e);
                                    }
                                }
                                else // Files not identical
                                {
                                    fileContext.File = newFile;
                                    //we check if the file is valid 
                                    if (Helpers.IsValidDocument(fileContext.FullPathDocument))
                                    {
                                        // switch the status to saved so we can start the upload process
                                        fileContext.Statut = Constants.STATUT_SAVED;
                                        Helpers.SaveContexteFileToJson(fileContext);
                                    }
                                    else
                                    {
                                        LogHelper.LogInformation(string.Format(LogMessages.InvalidFileType, fileContext.Filename, fileContext.DocumentId));
                                        ShowNotification(string.Format(ApplicationMessages.InvalidFileType, fileContext.Filename), ToastType.Error);

                                        GlobalContext.RemoveFile(entry.Key);
                                    }

                                }
                            }
                        }
                    }
                    else if (fileContext.Statut.Equals(Constants.STATUT_SAVED)) // upload section the file was edited and closed 
                    {

                        if (fileContext.ReUploadCounter == 0 || fileContext.ReUploadCounter > MAX_UPLOAD_COUNTER )
                        {
                            fileContext.ReUploadCounter = 1;
                            LogHelper.LogInformation(string.Format(LogMessages.UploadInfo, fileContext.Filename, fileContext.DocumentId));

                            //file was pending and was locally loaded
                            if(fileContext.File == null)
                            {
                                //check that the file still exists in the document directory 
                                if (File.Exists(fileContext.FullPathDocument))
                                {
                                    try
                                    {
                                        fileContext.File = File.ReadAllBytes(fileContext.FullPathDocument);
                                    }catch(Exception e)
                                    {
                                        LogHelper.LogError("Erreur", e);
                                    }
                                }
                                else
                                {
                                    GlobalContext.RemoveFile(entry.Key);
                                    continue;
                                }
                            }
                            if (fileContext.File != null)
                            {
                                // we try to upload the file 

                                this.UploadFileContext(ref fileContext);
                            }
                        }
                        else
                        {
                            fileContext.ReUploadCounter++;
                        }
                    }else if(fileContext.Statut.Equals(Constants.STATUT_LOCKED) && fileContext.CanShowLockedError ) // locked file section
                    {
                        if (File.Exists(fileContext.FullPathDocument))
                        {
                            Helpers.SaveContexteFileToJson(fileContext);
                            fileContext.CanShowLockedError = false;
                        }
                        else
                        {
                            GlobalContext.RemoveFile(entry.Key);
                            continue;
                        }
                    }


                    // let the thread refresh the datagrid when the statut changes
                    canRefreshDataGrid = canRefreshDataGrid ? canRefreshDataGrid : oldSatutCode != fileContext.Statut.Code;
                }
                //Reset the _canShowLogin flag variable so that the user get a notification and get redirected to login page when the authentication token is expired
                if (showLoginCounter >= MAX_SHOW_Login_COUNTER){
                    showLoginCounter = 0;
                    _canShowLogin = true;
                }
                // When there is no file to process quit the thread
                if (allFiles.Count == 0)
                {
                    MainWindow.SafeRefreshMainDataGrid();
                    LogHelper.LogInformation(LogMessages.StopingAppNofileLeftInfo);
                    Thread.Sleep(3000);
                    this.Abort();
                }


            }

            // Stop the application when we exit the thread
            MainWindow.Exit();
        }
        #region "Helper methods"
        /// <summary>
        ///  Handle the upload file process
        /// </summary>
        /// <param name="fileContext"></param>
        /// <param name="testcounter">for debug only to remove later</param>
        private void UploadFileContext(ref FileContext fileContext)
        {
            
            var apiHandler = GlobalContext.ApiHandler;
            var apiResponse = apiHandler.UploadFile(fileContext);
            if (apiResponse.Status == HttpStatusCode.OK)
            {
                LogHelper.LogInformation(string.Format(LogMessages.UploadSuccess, fileContext.Filename, fileContext.DocumentId));
                ShowNotification(string.Format(ApplicationMessages.UploadSuccess, fileContext.Filename), ToastType.Success);
                this.GlobalContext.RemoveFile(fileContext.DocumentId);
            }
            else
            {
                LogHelper.LogInformation(string.Format(LogMessages.UploadErrorInfo, fileContext.Filename, fileContext.DocumentId));
                if (apiResponse.Status == HttpStatusCode.Unauthorized)
                {
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_Unauthorized, fileContext.Filename, fileContext.DocumentId), apiResponse.Exception);
                    if (_canShowLogin)
                    {
                        //fileContext.ReUploadCounter = 0;
                        ShowNotification(ApplicationMessages.RedirectToLoginPageNotification, ToastType.Warning);
                        Process.Start(apiHandler.AuthUrl());
                        _canShowLogin = false;
                    }
                }
                else if (apiResponse.Status == HttpStatusCode.Forbidden)
                {
                    GlobalContext.FileContextIndex[fileContext.DocumentId].Statut = Constants.STATUT_LOCKED;
                    //if (fileContext.CanShowLockedError)
                    //{
                    this.HandleUploadResponseError(apiResponse, fileContext.CanShowLockedError);

                    //}
                }
                else
                {
                    this.HandleUploadResponseError(apiResponse, fileContext.CanShowErrors);
                    if (apiResponse.Status == HttpStatusCode.NotFound)
                    {
                        GlobalContext.FileContextIndex[fileContext.DocumentId].Statut = Constants.STATUT_NOT_FOUND;
                    }
                    if (fileContext.CanShowErrors)
                    {
                        fileContext.CanShowErrors = false;
                    }
                }
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="message"></param>
        /// <param name="type"></param>
        private void ShowNotification(string message, ToastType type)
        {
            MainWindow.SafeShowNotification(message, type);
        }
        /// <summary>
        /// The appropriate process on upload error
        /// </summary>
        /// <param name="apiResponseDto"></param>
        private void HandleUploadResponseError(dto.ApiResponseDto apiResponseDto,bool showNotification)
        {
            // get the error message and add line in the log file
            string message = GlobalContext.ApiHandler.HandleUploadResponseErrorMessageAndLogs(apiResponseDto);
            if (showNotification && !String.IsNullOrEmpty(message))
            {
                ShowNotification( message,ToastType.Error);
            }
        }
        /// <summary>
        /// The appropriate process on download error
        /// </summary>
        /// <param name="apiResponseDto"></param>
        private void HandleDownloadResponseError(dto.ApiResponseDto apiResponseDto)
        {
            string message = GlobalContext.ApiHandler.GetDownloadResponseErrorMessage(apiResponseDto);
            if (!String.IsNullOrEmpty(message))
            {
                ShowNotification( message,ToastType.Error);
            }
        }
        #endregion
    }
}


