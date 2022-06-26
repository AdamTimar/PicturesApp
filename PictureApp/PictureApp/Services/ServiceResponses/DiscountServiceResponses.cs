using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services.ServiceResponses
{
    public enum DiscountServiceResponses
    {
        SUCCESS = 201,
        EXCEPTION = 500,
        NOTFOUND = 404,
        DISCOUNTALREADYEXISTS = 409
    }
}
