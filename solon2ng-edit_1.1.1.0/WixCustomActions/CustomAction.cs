using Microsoft.Deployment.WindowsInstaller;
using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.Win32;
using System.Diagnostics;
using System.ComponentModel;

namespace WixCustomActions
{
    public class CustomActions
    {
        private static void BuildRegistry(RegistryKey mainKey, string path)
        {
            var epg2ng = mainKey.CreateSubKey("epg2ng");
            epg2ng.SetValue("URL Protocol", "");
            epg2ng.SetValue("", "URL:Solon-edit Protocol");
            var shell = epg2ng.CreateSubKey(@"shell");
            var open = shell.CreateSubKey(@"open");
            var cmd = open.CreateSubKey(@"command");
            cmd.SetValue("", $"\"{path}\" \"%1\"");
            var defaultIcon = epg2ng.CreateSubKey(@"DefaultIcon");
            defaultIcon.SetValue("", $"{path}");
            
            defaultIcon.Close();
            cmd.Close();
            open.Close();
            shell.Close();
            epg2ng.Close();

        }
        [CustomAction]
        public static ActionResult WritePerUserRegistry(Session session)
        {

            RemovePerUserRegistry();
            var data = session.CustomActionData;
            var path = data["APPLICATION_FOLDER"] + data["Target_File_Name"];
            session.Log("Begin WritePerUserRegistry");
            session.Log($"PATH = {path}");
            var subkey = Registry.CurrentUser.OpenSubKey(@"SOFTWARE\Classes", true);
            
            BuildRegistry(subkey, path);
            subkey.Close();

            var flag = Registry.CurrentUser.CreateSubKey(@"SOFTWARE\SolonEditFlag\perUser");
            flag.SetValue("SET", "1");
            flag.Close();


            return ActionResult.Success;
        }
        [CustomAction]
        public static ActionResult WritePerMachineRegistry(Session session)
        {
            RemovePerMachineRegistry();
            var data = session.CustomActionData;
            var path = data["APPLICATION_FOLDER"] + data["Target_File_Name"];
            session.Log("Begin WritePerMachineRegistry");
            session.Log($"PATH = {path}");
            BuildRegistry(Registry.ClassesRoot, path);

            return ActionResult.Success;
        }
        /// <summary>
        /// Removes the subkey installed with the application (used normally when uninstalling
        /// </summary>
        /// <param name="session"></param>
        /// <returns></returns>
        [CustomAction]
        public static ActionResult DeleteRegistry(Session session)
        {
            try
            {
                var software = Registry.CurrentUser.OpenSubKey(@"SOFTWARE", true);
                var flag = software.OpenSubKey("SolonEditFlag");
                if (flag == null)
                {
                    RemovePerMachineRegistry();
                }
                else
                {
                    flag.Close();
                    software.DeleteSubKeyTree("SolonEditFlag");
                    software.Close();
                    RemovePerUserRegistry();

                }

            }
            catch {}
            return ActionResult.Success;
        }

        private static void RemovePerUserRegistry()
        {
            try
            {

                using (var classes = Registry.CurrentUser.OpenSubKey(@"SOFTWARE\Classes", true))
                {
                    
                    
                    if (classes.OpenSubKey("epg2ng") != null)
                    {
                        classes?.DeleteSubKeyTree("epg2ng");
                    }
                }
            }
            catch{}
        }
        private static void RemovePerMachineRegistry()
        {
            try
            {
                var epg2ng = Registry.ClassesRoot.OpenSubKey("epg2ng", true);
                if (epg2ng != null)
                {
                    
                    foreach (string key in epg2ng.GetSubKeyNames())
                    {
                        epg2ng.DeleteSubKeyTree(key);
                    }
                    epg2ng.Close();
                    Registry.ClassesRoot.DeleteSubKey("epg2ng");
                }
            }
            catch{}

        }


    }
}
