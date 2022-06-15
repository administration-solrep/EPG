using LightJson;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Security.Cryptography;
using System.Configuration;
using RestSharp;
using System.Diagnostics;
using System.Runtime.InteropServices;
using System.Text;
using System.Linq;

namespace solonng_launcher.utils
{
     static class Helpers
    {
        private static readonly object _saveJsonLock = new object();
        private static readonly object _sha256CheckSumLock = new object();
      
        /// <summary>
        /// Checks whether the file is on use or not
        /// </summary>
        /// <param name="fullfilename"></param>
        /// <returns>true if the file is on use false if not</returns>
        public static bool IsHandleOnFile(string fullfilename)
        {
            FileInfo fileInfo = new FileInfo(fullfilename);
            try
            {
                using (FileStream stream = fileInfo.Open(FileMode.Open, FileAccess.Read, FileShare.None))
                {
                    stream.Close();
                }
            }
            catch (IOException)
            {
                return true;
            }
            //file is not locked
            return false;
        }



        /// <summary>
        /// 
        /// </summary>
        /// <param name="fileContext"></param>
        public static void SaveContexteFileToJson(FileContext fileContext)
        {
            lock (_saveJsonLock)
            {
                LogHelper.DebugInformation($"Saving the contextFile to Json ...");

                SaveTextToFile(fileContext.FullPathJson, fileContext.ToJsonString());
            }
        }
        /// <summary>
        /// Saves a string content into a file specified path
        /// </summary>
        /// <param name="path"></param> file path
        /// <param name="content"></param> file content to write
        public static void SaveTextToFile(string path,string content)
        {
            LogHelper.DebugInformation($"Saving the content: {content} into {path}");

            try
            {
                using (StreamWriter sw = File.CreateText(path))
                {
                    sw.WriteLine(content);
                }
            }catch(Exception e)
            {
                LogHelper.LogError("Erreur", e);
            }
            
        }

        /// <summary>
        /// Reads a file from a path and returns a JsonValue
        /// </summary>
        /// <param name="path"></param>
        /// <returns>JsonValue</returns>
        public static JsonValue JsonParseFile(string path,bool isEncrypted=false)
        {
            try
            {
                string jsonString = File.ReadAllText(path);
                if (isEncrypted)
                {
                    jsonString = EncryptionHelper.Decrypt(jsonString);
                }
                return JsonValue.Parse(jsonString);
            }catch(Exception e)
            {
                LogHelper.LogError("Erreur", e);
                return JsonValue.Null;
            }
        }
       
       /// <summary>
       /// Generate the appropriate SHA256 Checksum to the bytes[]
       /// </summary>
       /// <param name="buffer"></param>
       /// <returns></returns>
        public static string GenerateCheckSumSHA256(byte[] buffer)
        {
            lock (_sha256CheckSumLock)
            {
                byte[] sha256CheckSum = null;
               
                using (var sha256 = SHA256.Create())
                {
               
                    sha256CheckSum = sha256.ComputeHash(buffer);
 
                }

                return BitConverter.ToString(sha256CheckSum).Replace("-", "").ToLowerInvariant();
            }
        }
        /// <summary>
        /// Get the list of the recommended programs to open a file with a specific extension from the registry
        /// </summary>
        /// <param name="ext">The extension with the .</param>
        /// <returns>List of porcess name</returns>
        public static IEnumerable<string> GetRecommendedProgramsByExtension(string ext)
        {
            List<string> progs = new List<string>();

            string baseKey = @"Software\Microsoft\Windows\CurrentVersion\Explorer\FileExts\" + ext;

            using (RegistryKey rk = Registry.CurrentUser.OpenSubKey(baseKey + @"\OpenWithList"))
            {
                if (rk != null)
                {
                    string mruList = (string)rk.GetValue("MRUList");
                    if (mruList != null)
                    {
                        foreach (char c in mruList.ToString())
                            progs.Add(rk.GetValue(c.ToString()).ToString());
                    }
                }
            }

            using (RegistryKey rk = Registry.CurrentUser.OpenSubKey(baseKey + @"\OpenWithProgids"))
            {
                if (rk != null)
                {
                    foreach (string item in rk.GetValueNames())
                        progs.Add(item);
                }
            }

            return progs;
        }
        /// <summary>
        /// Returns the appropriate configuration string value using a key
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public static string GetConfigurationStringValue(string key)
        {
            try
            {
                return AppConfigurationManager.Instance.GetStringValue(key);
            }catch(Exception e)
            {
                LogHelper.LogError("Erreur", e);
                return "";
            }
        }
        public static void StartProcess(string name)
        {

            ProcessStartInfo startInfo = new ProcessStartInfo
            {
                //UseShellExecute = false,
                FileName = name,
                
            };
            
            Process.Start(startInfo);
        }
        
        /// <summary>
        /// Check whether the file is a valid file or not based on the mime type and the extension
        /// </summary>
        /// <param name="fullPathDocument"></param>
        /// <returns></returns>
        public static bool IsValidDocument(string fullPathDocument)
        {
            LogHelper.LogInformation($"Validating the document with the path: {fullPathDocument}");
            if (string.IsNullOrEmpty(fullPathDocument))
                return false;
            try
            {
                var extension = Path.GetExtension(fullPathDocument).Remove(0, 1).ToLower();
                var file = File.ReadAllBytes(fullPathDocument);
                var mimeType = MimeTypeHelper.GetMimeFromFile(file);
                LogHelper.DebugInformation($"Checking the mimetype and the extention of the file in: {fullPathDocument}, mimetype:{mimeType}   extension:{extension}");
                return Constants.VALID_EXTENSIONS.Contains(extension) && Constants.VALID_MIME_TYPES.Contains(mimeType);
            }
            catch(Exception e)
            {
                LogHelper.LogError($"Error while validating the file with the path: {fullPathDocument}", e);
                return false;
            }
        }
        /// <summary>
        /// Gets the solon edit version from the api
        /// </summary>
        /// <returns></returns>
        public static string GetSolonEditPublishedVersion()
        {
            var response = GlobalContext.Instance.ApiHandler.GetSolonVersion();
            var version = "";
            if (response.Status == HttpStatusCode.OK)
            {
                 version = response.Message;
            }
            else
            {
                LogHelper.LogInformation("coulnd check the version");
            }
            return version;
        }
        #region "Extension methods"

        /// <summary>
        /// 
        /// </summary>
        /// <param name="response"></param>
        /// <returns></returns>
        public static JsonValue GetJsonResponse(this WebResponse response)
        {
            var jsonValue = JsonValue.Null;
            try
            {

                using (var stream = response.GetResponseStream())
                using(var reader = new StreamReader(stream))
                {
                    var jsonString = reader.ReadToEnd();
                    if (!string.IsNullOrEmpty(jsonString))
                    {
                        jsonValue = JsonValue.Parse(jsonString);
                    }

                }
            }catch(Exception e)
            {
                LogHelper.LogError("Helpers.GetJsonResponse", e);
            }
            response.Close();
            return jsonValue;
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="httpWebRequest"></param>
        /// <param name="headers"></param>
        public static void SetHeaders(this RestRequest restRequest,Dictionary<string,string> headers)
        {
            if(headers.Count == 0)
            {
                return;
            }
            foreach(var header in headers)
            {
                restRequest.AddHeader(header.Key, header.Value);
            }
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="httpWebRequest"></param>
        /// <param name="cookies"></param>
        /// <param name="domain"></param>
        public static void SetCookies(this RestRequest restRequest,Dictionary<string,string> cookies)
        {
            if(cookies.Count == 0)
            {
                return;
            }
           
            foreach (var cookie in cookies)
            {

                restRequest.AddParameter(cookie.Key, System.Web.HttpUtility.UrlEncode(cookie.Value),ParameterType.Cookie);
            }
        }
        #endregion

    }


 

}

