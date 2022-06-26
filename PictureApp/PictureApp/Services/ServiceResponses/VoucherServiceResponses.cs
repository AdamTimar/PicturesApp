namespace PictureApp.Services.ServiceResponses
{
    public enum VoucherServiceResponses
    {
        SUCCESS = 200,
        EXCEPTION = 500,
        CANTADDVOUCHEREXCEPTION = 4044,
        NULLPARAM = 400,
        NOTFOUND = 404,
        VOUCHERNOTFOUND = 4041,
        USERNOTFOUND = 4042,
        USERVOUCHERNOTFOUND = 4043,
        TRUE = 1,
        FALSE = 0,
        WRONGCODELENGTH = 4045,
        ADDIMAGEEXCEPTION = 4001,
        VOUCHEREXPIRESBEFORENOW = 4046

    }
}
