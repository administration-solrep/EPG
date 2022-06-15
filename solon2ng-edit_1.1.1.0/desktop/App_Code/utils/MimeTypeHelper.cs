using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;

namespace solonng_launcher.utils
{
    public static class MimeTypeHelper
    {

        [DllImport("urlmon.dll", CharSet = CharSet.Unicode, ExactSpelling = true,
   SetLastError = false)]
        static extern int FindMimeFromData(IntPtr pBC,
       [MarshalAs(UnmanagedType.LPWStr)] string pwzUrl,
       [MarshalAs(UnmanagedType.LPArray, ArraySubType=UnmanagedType.I1,
        SizeParamIndex=3)]
        byte[] pBuffer,
       int cbSize,
       [MarshalAs(UnmanagedType.LPWStr)] string pwzMimeProposed,
       int dwMimeFlags,
       out IntPtr ppwzMimeOut,
       int dwReserved);

        public static string GetMimeFromFile(byte[] file)
        {

            try
            {
                byte[] buffer = new byte[256];
                using (Stream stream = new MemoryStream(file))
                {
                    if (stream.Length >= 256)
                        stream.Read(buffer, 0, 256);
                    else
                        stream.Read(buffer, 0, (int)stream.Length);
                }
                FindMimeFromData(IntPtr.Zero, null, buffer, buffer.Length, null, 0, out IntPtr mimeTypePtr, 0);
                string mime = Marshal.PtrToStringUni(mimeTypePtr);
                Marshal.FreeCoTaskMem(mimeTypePtr);
                return mime;
            }
            catch (Exception e)
            {
                return "unknown/unknown";
            }
        }
    }
}
