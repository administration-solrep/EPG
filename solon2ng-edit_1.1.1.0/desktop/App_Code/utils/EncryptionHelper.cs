using solonng_launcher.utils;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;

namespace solonng_launcher.utils
{
    public static class EncryptionHelper
    {
        private const string IV = @"Ai5jQc+LHZroDVtEsuPkSg==";
        private const string KEY = @"lf1u3+ujwDQm5MwsMx2KpRH+Ly9JiWQ0zr8XzczxyU4=";
        private static readonly string SALT = GlobalContext.Instance.AuthenticatedUserSID;
        private const string SALT_KEY = "_salt=";
        private static string RemoveSalt(string textToDecrypt)
        {
            return textToDecrypt.Substring(0, textToDecrypt.IndexOf(SALT_KEY));
        }
        private static string AddSalt(string textToSalt)
        {
            return textToSalt += SALT_KEY + SALT;
        }
        public static string Encrypt(string textToEncrypt)
        {
            try
            {

                var result = "";
                textToEncrypt = AddSalt(textToEncrypt);
                using (Aes aesAlg = Aes.Create())
                {

                    aesAlg.Key = Convert.FromBase64String(KEY);
                    aesAlg.IV = Convert.FromBase64String(IV);

                    // Create an encryptor to perform the stream transform.
                    ICryptoTransform encryptor = aesAlg.CreateEncryptor(aesAlg.Key, aesAlg.IV);

                    // Create the streams used for encryption.
                    using (MemoryStream msEncrypt = new MemoryStream())
                    {
                        using (CryptoStream csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write))
                        {
                            using (StreamWriter swEncrypt = new StreamWriter(csEncrypt))
                            {
                                //Write all data to the stream.
                                swEncrypt.Write(textToEncrypt);
                            }
                            result = Convert.ToBase64String(msEncrypt.ToArray());
                        }
                    }
                }
                return result;
            }
            catch (Exception e)
            {
                LogHelper.LogError(e.Message, e);
                return "";
            }
        }
        
        public static string Decrypt(string textToDecrypt)
        {
            try
            {

                var result = "";
                using (Aes aesAlg = Aes.Create())
                {
                    aesAlg.Key = Convert.FromBase64String(KEY);
                    aesAlg.IV = Convert.FromBase64String(IV);
                    ICryptoTransform decryptor = aesAlg.CreateDecryptor(aesAlg.Key, aesAlg.IV);

                    // Create the streams used for decryption.
                    using (MemoryStream msDecrypt = new MemoryStream(Convert.FromBase64String(textToDecrypt)))
                    {
                        using (CryptoStream csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read))
                        {
                            using (StreamReader srDecrypt = new StreamReader(csDecrypt))
                            {

                                // Read the decrypted bytes from the decrypting stream
                                // and place them in a string.
                                result = srDecrypt.ReadToEnd();
                            }
                        }
                    }
                }
                return RemoveSalt(result);
            }
            catch (Exception e)
            {
                LogHelper.LogError(e.Message, e);
                return "";
            }
        }
    }
}
