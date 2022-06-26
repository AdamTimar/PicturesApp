using PictureApp.DataAccesLayer.Models;
using Microsoft.AspNetCore.Http;
using System;
using System.IO;
using System.Drawing;

namespace PictureApp.Utils
{
    public static class MediaTypes
    {
        
        public static MediaTypeResponses UploadFile(ImageEntity image, IFormFile file)
        {
            if (file == null)
                return MediaTypeResponses.NULLPARAM;

            var extension = Path.GetExtension(file.FileName);
            if (!(string.Equals(extension, ".jpg", StringComparison.OrdinalIgnoreCase) || string.Equals(extension, ".png", StringComparison.OrdinalIgnoreCase) || string.Equals(extension, ".jpeg", StringComparison.OrdinalIgnoreCase)))
            {
                return MediaTypeResponses.BADEXTENSION;
            }


            try
            {
                var isValidImage = Image.FromStream(file.OpenReadStream());
            }
            catch
            {
                return MediaTypeResponses.NOTTRULYIMAGE;
            }

            image.Title = file.FileName;

            using (MemoryStream ms = new MemoryStream())
            {
                file.CopyTo(ms);
                image.Data = Convert.ToBase64String(ms.ToArray());
                ms.Close();
                ms.Dispose();
            }

            return MediaTypeResponses.SUCCESS;
        }       
    }
}
