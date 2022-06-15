using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Configuration;

namespace solonng_launcher.utils
{
    public class AppConfigurationManager
    {
        private static AppConfigurationManager _appConfigurationManager;
        public AppSettingsReader AppSettingReader { get; private set; }
        private AppConfigurationManager() {
            AppSettingReader = new AppSettingsReader();
        }

        public static AppConfigurationManager Instance
        {
            get
            {
                if(_appConfigurationManager == null)
                {
                    _appConfigurationManager = new AppConfigurationManager();
                }
                return _appConfigurationManager;
            }
        }

        /// <summary>
        /// Get the value of a parameter from the config
        /// </summary>
        /// <param name="key"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public  T GetValue<T>(string key)
        {
            return (T)AppSettingReader.GetValue(key, typeof(T));
        }

        public  string GetStringValue(string key)
        {
            return GetValue<string>(key);
        }
    }
}
