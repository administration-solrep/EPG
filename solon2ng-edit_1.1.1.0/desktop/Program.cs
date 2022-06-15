using System;
using solonng_launcher.utils;
using solonng_launcher.Resources;
namespace solonng_launcher
{
    static class Program
    {


        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            try
            {
                LogHelper.Init();
                LogHelper.LogInformation(LogMessages.ApplicationStarted);
                if (args.Length == 0)
                {

                    LogHelper.LogError(LogMessages.NoArgsError);
                    return;
                }
                LauncherManager launcherManager = new LauncherManager();
                launcherManager.LunchApplication(args[0]);
            } catch(Exception e)
            {
                LogHelper.LogInformation(LogMessages.ApplicationStoppedDueToErrorInformation);
                LogHelper.LogError("Erreur", e);
            }

}
      

    }
}
