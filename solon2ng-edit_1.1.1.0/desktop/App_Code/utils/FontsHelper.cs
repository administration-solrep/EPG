using System;
using System.Collections.Generic;
using System.Drawing.Text;
using System.Linq;
using System.Text;

namespace solonng_launcher.App_Code.utils
{
    public static class FontsHelper
    {
        public static PrivateFontCollection RobotSlabFontCollection { get; private set; }


        public static void LoadRobotSlabFont()
        {
            if (RobotSlabFontCollection != null)
                return;

            RobotSlabFontCollection = new PrivateFontCollection();
            //Select your font from the resources.
            var fontLength = Properties.Resources.RobotoSlab_VariableFont_wght.Length;

            // create a buffer to read in to
            byte[] fontdata = Properties.Resources.RobotoSlab_VariableFont_wght;

            // create an unsafe memory block for the font data
            IntPtr data = System.Runtime.InteropServices.Marshal.AllocCoTaskMem(fontLength);

            // copy the bytes to the unsafe memory block
            System.Runtime.InteropServices.Marshal.Copy(fontdata, 0, data, fontLength);

            // pass the font to the font collection
            RobotSlabFontCollection.AddMemoryFont(data, fontLength);

            System.Runtime.InteropServices.Marshal.FreeCoTaskMem(data);
        }

    }
}
