using LightJson;
using solonng_launcher.dto;
using solonng_launcher.Resources;
using solonng_launcher.utils;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace solonng_launcher
{
     class ApiHandler
    {

        /// <summary>
        /// The query parameter key for the headers
        /// </summary>
        private const string HEADER_KEY = "_h";
        /// <summary>
        /// The query parameter key for the cookies
        /// </summary>
        private const string COOKIE_KEY = "_c";
        /// <summary>
        /// The query parameter key for the the number in millisecond the thread is going to sleep after opening a file
        /// See ThreadController.OpenFileSleepDuration
        /// </summary>
        private const string THREAD_SLEEP_KEY = "threadSleep";
        /// <summary>
        /// The seperator of the key value for the header and cookie query params (_h:headerVal&_c:cookieVal)
        /// </summary>
        private const string VALUE_DELIMITER = ":";
        /// <summary>
        /// Authentication action
        /// </summary>
        private const string AUTH_KEY_PATH = "/connexion";

        private static ApiHandler _apiHandler;

        /// <summary>
        /// Lock used for to make functions thread safe
        /// </summary>
        private static readonly object _lock = new object();

        private int _threadSleep;
        private readonly Dictionary<string, string> _headers = new Dictionary<string, string>();
        private readonly Dictionary<string, string> _cookies = new Dictionary<string, string>();
        public string Path { get; set; }
        public string Domaine { get; set; }
        
 
 
        public bool IsAuthUri { get; private set; }
        

        private ApiHandler() { }
        public static ApiHandler Instance
        {
            get
            {
                if (_apiHandler == null)
                {
                    lock (_lock)
                    {
                        if (_apiHandler == null)
                        {
                            _apiHandler = new ApiHandler();
                        }

                    }
                }
                return _apiHandler;
            }
        }
        /// <summary>
        /// Returns the download url for a specific file
        /// </summary>
        /// <param name="fileContext"></param>
        /// <returns></returns>
        public string DownloadUrl(FileContext fileContext)
        {
            return $"{Path}/edit/{fileContext.DocumentId}/download";
        }
        /// <summary>
        /// Returns the upload url for a specific file
        /// </summary>
        /// <param name="fileContext"></param>
        /// <returns></returns>
        public string UploadUrl(FileContext fileContext)
        {
            return $"{Path}/edit/{fileContext.DocumentId}/upload";
        }
        /// <summary>
        /// Returns the unmodified notification for a specific file
        /// </summary>
        /// <param name="fileContext"></param>
        /// <returns></returns>
        private string NotificationUnmodifiedUrl(FileContext fileContext)
        {
            return $"{Path}/edit/{fileContext.DocumentId}/unmodified";
        } 

        /// <summary>
        /// Returns the unmodified notification for a specific file
        /// </summary>
        /// <param name="fileContext"></param>
        /// <returns></returns>
        private string NotificationEditingUrl(FileContext fileContext)
        {
            return $"{Path}/edit/{fileContext.DocumentId}/editing";
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public string VersionUrl()
        {
            return $"{Path}/edit/version";
        }

        /// <summary>
        /// Return the Authentication url
        /// </summary>
        /// <returns></returns>
        public string AuthUrl()
        {
            return $"{Path}/ngedit/auth/token";
        }

        

        /// <summary>
        /// Load the api parameters from a uri
        /// </summary>
        /// <param name="uri"></param>
        public void LoadConfigFromUri(Uri uri)
        {
            LogHelper.DebugInformation($"Start parsing the url ...");

            var hostname = uri.Host;
            var preProcessedPath = CleanPath(uri.LocalPath);
            var port = (uri.Port <= 0) ? null : uri.Port.ToString();
            Domaine = $"https://{hostname}{(port == null ? "" : ":") + port}";
            Path = $"{Domaine}/{preProcessedPath}";
            IsAuthUri = uri.LocalPath.Split(new string[] { AUTH_KEY_PATH }, StringSplitOptions.None).Length > 1;
            var query = System.Web.HttpUtility.ParseQueryString(uri.Query);
            var threadSleep = query.Get(THREAD_SLEEP_KEY) ?? "1000";
            _threadSleep = int.TryParse(threadSleep, out var result) ? result : 1000;
            var preprocessedHeaders = query.GetValues(HEADER_KEY);
            var preprocessedCookiess = query.GetValues(COOKIE_KEY);
            LogHelper.DebugInformation($"Domaine: {Domaine}");
            LogHelper.DebugInformation($"Path: {Path}");
            LogHelper.DebugInformation($"Query String: {uri.Query}");


            if (preprocessedHeaders != null)
            {
                LogHelper.DebugInformation($"Headers: {string.Join(",", preprocessedHeaders)}");

                foreach (string header in preprocessedHeaders)
                {
                    var indexOfValueDelimiter = header.IndexOf(VALUE_DELIMITER);
                    if(indexOfValueDelimiter == -1)
                    {
                        throw new UriFormatException();
                    }
                    var key = header.Substring(0, indexOfValueDelimiter);
                    var value = header.Substring(indexOfValueDelimiter + 1);
                    if (!_headers.ContainsKey(key))
                    {
                        _headers.Add(key, value);
                    }
                }
                LogHelper.DebugInformation($"Parsing the header done, the value : {string.Join(",", _headers.Select(kvp => $" [{kvp.Key}:{kvp.Value}] ").ToArray())}");

            }
            if (preprocessedCookiess != null)
            {
                LogHelper.DebugInformation($"Cookies: {string.Join(",", preprocessedCookiess)}");

                foreach (string cookie in preprocessedCookiess)
                {
                    var indexOfValueDelimiter = cookie.IndexOf(VALUE_DELIMITER);
                    if (indexOfValueDelimiter == -1)
                    {
                        throw new UriFormatException();
                    }
                    var key = cookie.Substring(0, indexOfValueDelimiter);
                    var value = cookie.Substring(indexOfValueDelimiter + 1);
                    if (!_cookies.ContainsKey(key))
                    {
                        _cookies.Add(key, value);
                    }
                }
                LogHelper.DebugInformation($"Parsing the cookies done, the value :  {string.Join(",", _cookies.Select(kvp => $" [{kvp.Key}:{kvp.Value}] ").ToArray())}");

            }



        }

     
        /// <summary>
        /// Clean the path by removing the action
        /// </summary>
        /// <param name="preprocessedPath"></param>
        /// <returns></returns>
        private string CleanPath(string preprocessedPath)
        {
            var lasIndexOfSlash = preprocessedPath.LastIndexOf("/");
            if(lasIndexOfSlash != -1)
            {
                preprocessedPath = preprocessedPath.Substring(0, lasIndexOfSlash);
            }
            if (preprocessedPath.IndexOf("/") == 0)
            {
                preprocessedPath = preprocessedPath.Substring(1);
            }

            return preprocessedPath;
        }
        public ApiResponseDto GetSolonVersion()
        {
            //this.RefreshHeaders();
            var client = new RestSharp.RestClient(VersionUrl())
            {
                Timeout = -1
            };
            var request = new RestSharp.RestRequest(RestSharp.Method.GET);
            request.SetHeaders(_headers);
            request.SetCookies(_cookies);
            request.AddHeader("Accept", "*/*");
            request.AddHeader("Content-Type", "application/json");

            var response = client.Execute(request);
            var apiResponseDto = new ApiResponseDto
            {
                Status = HttpStatusCode.NotImplemented

            }

            ;
            if (response.ResponseStatus == RestSharp.ResponseStatus.Completed)
            {

                apiResponseDto.Status = response.StatusCode;


                if (response.StatusCode == HttpStatusCode.OK)
                {
                    try
                    {
                        apiResponseDto.Message = JsonValue.Parse(response.Content).AsJsonObject["version"].AsString.ToString();
                    }
                    catch (Exception e)
                    {
                        apiResponseDto.Message = "Une erreur est survenue!";
                        LogHelper.LogError("Erreur ApiHandler.GetSolonVersion when parsing the json", e);
                    }
                }
                else
                {

                }
            }
            return apiResponseDto;

        }

        /// <summary>
        /// Get the file from the server
        /// </summary>
        /// <param name="fileContext"></param>
        /// <returns>ApiResponseDto</returns>
        public ApiResponseDto DownloadFile(FileContext fileContext)
        {
            //this.RefreshHeaders();
            var client = new RestSharp.RestClient(DownloadUrl(fileContext))
            {
                Timeout = -1
            };
            var request = new RestSharp.RestRequest(RestSharp.Method.GET);
            request.SetHeaders(_headers);
            request.SetCookies(_cookies);
            request.AddHeader("Accept", "*/*");
            request.AddHeader("Content-Type", "application/json");
            
            RestSharp.IRestResponse response = client.Execute(request);
            var apiResponseDto = new ApiResponseDto
            {
                File = new FileDto
                {
                    Id= fileContext.DocumentId,
                    FileName = fileContext.Filename
                }
            };
            if (response.ResponseStatus == RestSharp.ResponseStatus.Completed)
            {
                apiResponseDto.Status = response.StatusCode;
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    try
                    {
                        var jsonFile = JsonValue.Parse(response.Content).AsJsonObject["file"].AsJsonObject;
                        apiResponseDto.File = new FileDto
                        {
                            Id = jsonFile["id"],
                            FileName = jsonFile["filename "],
                            Data = Convert.FromBase64String(jsonFile["data"]),
                            Version = jsonFile["version"]
                        };
                      
                    }
                    catch (Exception e) {
                        apiResponseDto.Message = "Une erreur est survenue!";
                        LogHelper.LogError("Erreur ApiHandler.DownloadFile", e);
                    }
                }
                else
                {
                    try
                    {
                        var content = JsonValue.Parse(response.Content).AsJsonObject;
                        if (content.ContainsKey("message"))
                        {
                            apiResponseDto.Message = content["message"];
                        }
                    }
                    catch (Exception e)
                    {
                        //apiResponseDto.Message = "Une erreur est survenue!";
                        LogHelper.LogError("Erreur ApiHandler.DownloadFile", e);
                    }
                }
            }
            else
            {
                if (response.ErrorMessage.Contains("SSL/TLS"))
                {
                    apiResponseDto.Message = ApplicationMessages.SslTlsCertificateValidationFailed;
                    LogHelper.LogError(LogMessages.SslTlsCertificateValidationFailed);
                }
            
                    apiResponseDto.Status = response.StatusCode;
                    apiResponseDto.Exception = response.ErrorException;
                    LogHelper.LogError(response.ErrorException?.StackTrace);


            }

          

            
            return apiResponseDto;

        }

        /// <summary>
        /// Post the file to the server
        /// </summary>
        /// <param name="queryString"></param>
        /// <returns>ApiResponseDto</returns>
        public ApiResponseDto UploadFile(FileContext fileContext)
        {
            var client = new RestSharp.RestClient(UploadUrl(fileContext))
            {
                Timeout = -1
            };
            var request = new RestSharp.RestRequest(RestSharp.Method.POST);
            request.SetHeaders(_headers);
            request.AddHeader("Accept", "*/*");
            request.SetCookies(_cookies);
            
            request.AddFile("file", fileContext.File,fileContext.Filename);
            request.AddParameter("version", fileContext.Version);
            RestSharp.IRestResponse response = client.Execute(request);
            
            Console.WriteLine(response.Content);
            var apiResponseDto = new ApiResponseDto
            {
                File = new FileDto
                {
                    Id = fileContext.DocumentId,
                    FileName = fileContext.Filename
                },
               

            };
            if (response.ResponseStatus == RestSharp.ResponseStatus.Completed)
            {
                apiResponseDto.Status = response.StatusCode;
                try
                {
                    var content = JsonValue.Parse(response.Content).AsJsonObject;
                    if (content.ContainsKey("message"))
                    {
                        apiResponseDto.Message = content["message"];
                    }
                }
                catch (Exception e) {
                    //apiResponseDto.Message = "Une erreur est survenue!";
                    LogHelper.LogError("Erreur ApiHandler.UploadFile", e);
                }
                

            }
            else
            {
                apiResponseDto.Status = HttpStatusCode.NotImplemented;
                apiResponseDto.Exception = response.ErrorException;
            }
            return apiResponseDto;
        }


        /// <summary>
        /// Send request to the api to notify it if the file is not beign edited
        /// </summary>
        public void NotifyFileIsNotBeingEdited(FileContext fileContext)
        {

            var client = new RestSharp.RestClient(NotificationUnmodifiedUrl(fileContext))
            {
                Timeout = -1
            };
            var request = new RestSharp.RestRequest(RestSharp.Method.POST);
            request.SetHeaders(_headers);
            request.AddHeader("Accept", "*/*");
            request.SetCookies(_cookies);
            var response = client.Execute(request);
            if (response.StatusCode == HttpStatusCode.OK)
            {
                LogHelper.LogInformation(string.Format(LogMessages.NotifyFileNotBeingEditedSuccess, fileContext.Filename, fileContext.DocumentId));

            }
            else
            {
                LogHelper.LogError(string.Format(LogMessages.NotifyFileNotBeingEditedError, fileContext.Filename, fileContext.DocumentId, (int)response.StatusCode));
            }
        }

        /// <summary>
        /// Send request to the api to notify it if the file is  beign edited
        /// TO DO Add the version number 
        /// </summary>
        public ApiResponseDto NotifyFileIsBeingLocallyEdited(FileContext fileContext)
        {

            var client = new RestSharp.RestClient(NotificationEditingUrl(fileContext))
            {
                Timeout = -1
            };
            var request = new RestSharp.RestRequest(RestSharp.Method.POST);
            request.SetHeaders(_headers);
            request.AddHeader("Accept", "*/*");
            request.SetCookies(_cookies);
            var response = client.Execute(request);
            var apiResponseDto = new ApiResponseDto
            {
                Status = response.StatusCode
            };
            if (response.StatusCode == HttpStatusCode.OK)
            {
                LogHelper.LogInformation(string.Format(LogMessages.NotifyFileBeingEditedLocallySuccess, fileContext.Filename, fileContext.DocumentId));

            }
            else
            {
                LogHelper.LogError(string.Format(LogMessages.NotifyFileBeingEditedLocallyError, fileContext.Filename, fileContext.DocumentId, (int)response.StatusCode));
                try
                {
                    var content = JsonValue.Parse(response.Content).AsJsonObject;
                    if (content.ContainsKey("message"))
                    {
                        apiResponseDto.Message = content["message"];
                    }
                }
                catch (Exception e)
                {
                    //apiResponseDto.Message = "Une erreur est survenue!";
                    LogHelper.LogError("Erreur ApiHandler.UploadFile", e);
                }

               
            }
            return apiResponseDto;
        }
        /// <summary>
        /// Saves the authentication token into  SolonTokenAuthFile
        /// </summary>
        public void SaveHeaders()
        {
            lock (_lock)
            {
                var headers = new JsonArray();
                foreach (KeyValuePair<string,string> head in _headers)
                {
                    headers.Add(new JsonObject{
                        { "key" , head.Key },
                        {"value" , head.Value }
                    });
                }
                var cookies = new JsonArray();
                foreach (KeyValuePair<string, string> head in _cookies)
                {
                    cookies.Add(new JsonObject{
                        { "key" , head.Key },
                        {"value" , head.Value }
                    });
                }
                

                var pairs = new JsonObject
                {
                    {"headers",headers },
                    {"cookies",cookies },
                    {"threadsleep",  _threadSleep}

                };


                if (cookies.Count != 0 || headers.Count != 0)
                {
                    LogHelper.DebugInformation($"saving the header files ...");

                    Helpers.SaveTextToFile(Constants.SolonHeaderFile, EncryptionHelper.Encrypt(pairs.ToString()));
                }
            }
        }

        /// <summary>
        /// Refresh the authentication token from the SolonTokenAuthFile
        /// </summary>
        public void RefreshHeaders()
        {
            lock (_lock)
            {
                if (File.Exists(Constants.SolonHeaderFile))
                {
                    var pairs = Helpers.JsonParseFile(Constants.SolonHeaderFile,true);
                    var headers = pairs["headers"].AsJsonArray;
                    var cookies = pairs["cookies"].AsJsonArray;
                    var globalContext = GlobalContext.Instance;
                    globalContext.ThreadController.OpenFileSleepDuration = pairs["threadsleep"].AsInteger;
                    if (headers != null)
                    {
                        foreach (KeyValuePair<string, FileContext> entry in globalContext.FileContextIndex.ToList())
                        {
                            entry.Value.ReUploadCounter = 0;
                            entry.Value.CanShowErrors = true;
                            entry.Value.CanShowLockedError = true;
                        }
                        foreach (JsonValue head in headers)
                        {

                            _headers[head["key"]] = (head["value"]);
                        }
                    }
                    if (cookies != null)
                    {
                        foreach (JsonValue cookie in cookies)
                        {
                            _cookies[cookie["key"]] = (cookie["value"]);
                        }
                    }
                    File.Delete(Constants.SolonHeaderFile);

                }
            }
        }

        /// <summary>
        /// Handles the upload error messages and logs
        /// </summary>
        /// <param name="apiResponseDto"></param>
        /// <returns></returns>
        public string HandleUploadResponseErrorMessageAndLogs(ApiResponseDto apiResponseDto)
        {
            var message = apiResponseDto.Message;
            var fileName = apiResponseDto.File.FileName;
            switch (apiResponseDto.Status)
            {
                case HttpStatusCode.Unauthorized:
                    // not authenticated
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_Unauthorized, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.UploadErrorReconnectToEpg2ng, fileName);


                    break;
                case HttpStatusCode.Forbidden:
                    // dont have the right to edit the file
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_Forbidden, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.UploadErrorNotAuthorized, fileName);

                    break;
                case HttpStatusCode.NotFound:
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_NotFound, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.UploadErrorFileNotFound, fileName);
                    break;

                case HttpStatusCode.InternalServerError:
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_InternalServerError, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.UploadErrorEpg2ngInaccessible, fileName);
                    break;
                case HttpStatusCode.NotImplemented:
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_UknownError, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.UploadErrorUknownError, fileName);
                    break;
                case HttpStatusCode.GatewayTimeout:
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_GatewayTimeout, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.UploadErrorEpg2ngInaccessible, fileName);
                    break;
                default:
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_InternalServerError, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    
                    message = message ?? string.Format(ApplicationMessages.UploadErrorEpg2ngInaccessible, fileName);
                    break;
            }

            return message;
        }
        /// <summary>
        /// Handles the download error messages and logs
        /// </summary>
        /// <param name="apiResponseDto"></param>
        /// <returns></returns>
        public string GetDownloadResponseErrorMessage(ApiResponseDto apiResponseDto)
        {
            var message = string.IsNullOrEmpty(apiResponseDto.Message) ? null : apiResponseDto.Message;
            var fileName = apiResponseDto.File.FileName;

            switch (apiResponseDto.Status)
            {
                case HttpStatusCode.Unauthorized:
                    // not authenticated
                    LogHelper.LogError(string.Format(LogMessages.DownloadResponse_Unauthorized, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ??  string.Format(ApplicationMessages.DownloadErrorReconnectToEpg2ng, fileName);


                    break;
                case HttpStatusCode.Forbidden:
                    // dont have the right to edit the file
                    LogHelper.LogError(string.Format(LogMessages.DownloadResponse_Forbidden, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.DownloadErrorNotAuthorized,fileName);

                    break;
                case HttpStatusCode.NotFound:
                    LogHelper.LogError(string.Format(LogMessages.DownloadResponse_NotFound, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.DownloadErrorFileNotFound,fileName);
                    break;

                case HttpStatusCode.InternalServerError:
                    LogHelper.LogError(string.Format(LogMessages.DownloadResponse_InternalServerError, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.DownloadErrorEpg2ngInaccessible, fileName);
                    break;
                case HttpStatusCode.NotImplemented:
                    LogHelper.LogError(string.Format(LogMessages.DownloadResponse_UknownError, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.DownloadErrorUknownError, fileName);
                    break;
                case HttpStatusCode.GatewayTimeout:
                    LogHelper.LogError(string.Format(LogMessages.DownloadResponse_GatewayTimeout, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);
                    message = message ?? string.Format(ApplicationMessages.DownloadErrorEpg2ngInaccessible, fileName);
                    break;
                default:
                    LogHelper.LogError(string.Format(LogMessages.UploadResponse_InternalServerError, fileName, apiResponseDto.File.Id, (int)apiResponseDto.Status), apiResponseDto.Exception);

                    message = message ?? string.Format(ApplicationMessages.DownloadErrorEpg2ngInaccessible, fileName);
                    break;
            }

            return message;
        }
        
    }
    
}
