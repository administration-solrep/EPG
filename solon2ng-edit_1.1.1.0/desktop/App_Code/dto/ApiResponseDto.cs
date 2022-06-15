using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;

namespace solonng_launcher.dto
{
     class ApiResponseDto
    {
        public HttpStatusCode Status { get; set; }
        public string Message { get; set; }

        public Exception Exception { get; set; }
        public FileDto File { get; set; }


    }
}
