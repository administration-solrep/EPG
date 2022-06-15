using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using solonng_launcher.utils;
using LightJson;
using System.Windows.Forms;
using System.Threading;

namespace solonng_launcher
{
    /// <summary>
    /// The global context of the application
    /// Contains the main window, the main thread, the api handler and the file contexts index
    /// </summary>
     class GlobalContext
    {
        /// <summary>
        /// Lock used to make function thread safe
        /// </summary>
        private static readonly object _lock = new object();

        /// <summary>
        /// The index of file contexts
        /// </summary>
        public Dictionary<string, FileContext> FileContextIndex { get; private set; }
        public bool IsContextFilesChanged { get; set; }

        private static GlobalContext _globaContext;
        public MainWindow MainWindow { get; set; }
        public ThreadController ThreadController { get; private set; }
        public LauncherManager LauncherManager { get; set; }
        public string AuthenticatedUserSID
        {
            get
            {
                try
                {
                    var currentUser = System.Security.Principal.WindowsIdentity.GetCurrent();
                    LogHelper.DebugInformation($"Getting the sid of the user: isAnonymous:{currentUser.IsAnonymous}, value: {currentUser.User?.Value} ");
                    return currentUser.IsAnonymous ? "" : currentUser.User.Value;
                }
                catch(Exception e)
                {
                    LogHelper.LogError("Error when getting AuthenticatedUserSID", e);
                    return "";
                }
            }
        }
        public ApiHandler ApiHandler
        {
            get
            {
                return ApiHandler.Instance;
            }
        }

        private GlobalContext(){
            FileContextIndex = new Dictionary<string, FileContext>();
        }
        
        public static GlobalContext Instance
        {
            get
            {
                if (_globaContext == null)
                {
                    lock (_lock)
                    {
                        if (_globaContext == null)
                        {
                            _globaContext = new GlobalContext();
                        }
                    }
                }
                return _globaContext;
            }
        }
        

        /// <summary>
        /// Add FileContext to the list of files
        /// </summary>
        /// <param name="key"></param>
        /// <param name="fc"></param>
        public void Addfile(string key, FileContext fc)
        {
            

            if (!FileContextIndex.ContainsKey(key))
            {
                LogHelper.DebugInformation($"Adding the filecontext with the json params: {fc.ToJsonString()} to the fileindex to be processed.");

                FileContextIndex.Add(key,fc);
                IsContextFilesChanged = true;
            }else if (FileContextIndex[key].Statut.Equal(Constants.STATUT_SAVED) || FileContextIndex[key].Statut.Equal(Constants.STATUT_LOCKED))
            {
                LogHelper.DebugInformation($"Adding the filecontext with the json params: {fc.ToJsonString()} to the fileindex to be processed.");

                FileContextIndex[key] = fc;
                IsContextFilesChanged = true;
            }
            
        }
        /// <summary>
        /// Removes a filecontext from the collection with the home and configuration files
        /// </summary>
        /// <param name="key"></param>
        /// <param name="soft">determine whether to remove the document file or not</param>
        public void RemoveFile(string key,bool soft = false)
        {
            try
            {
                FileContext fileContext = FileContextIndex[key];
                if (!soft)
                {
                    File.Delete(fileContext.FullPathJson);
                    File.Delete(fileContext.FullPathDocument);
                }
                FileContextIndex.Remove(key);
                IsContextFilesChanged = true;
            }catch(Exception e)
            {
                LogHelper.LogError("GlobalContext.RemoveFile", e);
            }



        }
        /// <summary>
        /// Retrieves context files added by others instances of this app and regresh also the IsContextFilesChanged status
        /// and also refresh the token
        /// </summary>
        /// <returns></returns>
        public void RefreshCurrentFiles()
        {
            IsContextFilesChanged = false;
            if (File.Exists(Constants.SolonNotififierFile))
            {
                lock (_lock)
                {
                    var json = Helpers.JsonParseFile(Constants.SolonNotififierFile);
                    if (json.IsJsonObject)
                    {
                        var jsonObject = json.AsJsonObject;

                        foreach (var element in jsonObject)
                        {

                            var fileContext = new FileContext(element.Key, element.Value);
                            if (File.Exists(fileContext.FullPathJson)){
                                fileContext = new FileContext(Helpers.JsonParseFile(fileContext.FullPathJson));
                                if (fileContext.Statut != null )
                                {
                                    this.Addfile(fileContext.DocumentId, fileContext);

                                }
                            }
                            else
                            {
                                LogHelper.DebugInformation($"File with the id: {fileContext.DocumentId} and filename: {fileContext.Filename} and the full path: {fileContext.FullPathDocument} was not found  ");
                            }
                        }

                    }
                    File.Delete(Constants.SolonNotififierFile);
                }
            }

        }
        /// <summary>
        ///  Add the filecontext infos to the queue to be downloaded
        /// </summary>
        public void AddFileIndex(FileContext fileContext)
        {
            lock (_lock)
            {
                JsonObject jsonObject;
                if (!File.Exists(Constants.SolonNotififierFile))
                {
                    jsonObject = new JsonObject{
                        { fileContext.DocumentId, fileContext.Filename }
                    };
                }
                else
                {
                    jsonObject = Helpers.JsonParseFile(Constants.SolonNotififierFile).AsJsonObject;
                    if (!jsonObject.ContainsKey(fileContext.DocumentId))
                    {
                        jsonObject.Add(fileContext.DocumentId, fileContext.Filename);
                    }

                }
                LogHelper.DebugInformation($"Adding the file to the json index ...");

                Helpers.SaveTextToFile(Constants.SolonNotififierFile, jsonObject.ToString());
            }
        }

        /// <summary>
        /// Check if the current file have a file that have the statut saved or locked
        /// </summary>
        /// <returns></returns>
        public bool CurrentFilesHavePendingFile()
        {
            foreach(var keyValuePair in FileContextIndex)
            {
                var fileContext = keyValuePair.Value;
                if (fileContext.Statut.Equal(Constants.STATUT_SAVED) || fileContext.Statut.Equal(Constants.STATUT_LOCKED))
                {
                    return true;
                }
            }

            return false;
        }
        /// <summary>
        /// Get the files from the json in the home directory that have the statut saved or locked
        /// </summary>
        public void GetPendingUnprocessedFiles()
        {
            LogHelper.DebugInformation($"Loading unprocessed files...");

            try
            {
                LogHelper.DebugInformation($"Getting the files from {Constants.SolonHomeFolder}");

                var files = Directory.GetFiles(Constants.SolonHomeFolder, "*.json");
                LogHelper.DebugInformation($"The files:  {string.Join(",",files)}");

                foreach (var file in files)
                {
                    var fileContext = new FileContext(Helpers.JsonParseFile(file));
                    if (fileContext.Statut.Equal(Constants.STATUT_SAVED) || fileContext.Statut.Equal(Constants.STATUT_LOCKED))
                    {

                        this.Addfile(fileContext.DocumentId, fileContext);

                    }
                }
            }catch(Exception e)
            {
                LogHelper.LogError("", e);
            }
          
        }

        public  void StartMainWindow()
        {
            LogHelper.DebugInformation($"Starting the MainWindow");

            MainWindow = new MainWindow();
            Application.Run(MainWindow);
        }

        public void StartThreadController()
        {
            LogHelper.DebugInformation($"Starting the thread controller.");

            ThreadController = ThreadController.Instance;
            ThreadController.Start();
            
        }


        

    }
}
