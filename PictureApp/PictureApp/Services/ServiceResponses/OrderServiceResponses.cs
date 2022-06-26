using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services.ServiceResponses
{
    public enum OrderServiceResponses
    {
        SUCCESS = 200,
        EXCEPTION = 500,
        PICTURENOTFOUND = 404,
        SIZENOTFOUND = 4041
    }
}
