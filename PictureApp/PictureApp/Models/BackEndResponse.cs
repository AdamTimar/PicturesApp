using System.Collections.Generic;

namespace PictureApp.Models
{
    public class BackEndResponse<T>
    {
        public int Code { get; set; } = 400;
        public string Error { get; set; } = null;
        public T Result = default;

        public BackEndResponse(int code, string error)
        {
            Code = code;
            Error = error;
        }   

        public BackEndResponse(int code, T result)
        {
            Code = code;
            Result = result;
        }

        public BackEndResponse(int code, string error, T result)
        {
            Code = code;
            Error = error;
            Result = result;
        }
    }

}
