using LightJson;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace solonng_launcher.dto
{
     class FileDto
    {

        public string Id { get; set; }
        public string FileName { get; set; }
        public string Version { get; set; }

        public byte[] Data { get; set; }
    }
}
