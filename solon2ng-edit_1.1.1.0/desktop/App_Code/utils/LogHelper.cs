using log4net;
using log4net.Appender;
using log4net.Repository.Hierarchy;
using System;
using System.Windows.Forms;
using System.Linq;

namespace solonng_launcher.utils
{
    static class LogHelper
    {
        private static readonly log4net.ILog _logger = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

         
        public static void Init()
        {
            string appPath = System.IO.Path.GetDirectoryName(Application.ExecutablePath);
            var fileInfo = new System.IO.FileInfo($"{appPath}\\log4net.config");
            log4net.Config.XmlConfigurator.Configure(fileInfo);
            
        }

       

      
        public static void LogInformation(string infos)
        {
            _logger.Info(infos);
        }
        public static void DebugInformation(string infos)
        {
            _logger.Debug(infos);
        }
        public static void LogError(string error,Exception exception = null)
        {
            if (exception == null)
            {
                _logger.Error(error);

            }
            else
            {
                _logger.Error(error, exception);
            }
        }
        public static void LogError(string error, string exceptionMessage)
        {
            _logger.Error($"{error}: {exceptionMessage}");
        }
    }
}
