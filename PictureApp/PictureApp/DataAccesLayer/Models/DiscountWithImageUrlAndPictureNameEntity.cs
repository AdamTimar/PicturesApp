using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.DataAccesLayer.Models
{
    public class DiscountWithImageUrlAndPictureNameEntity
    {
        public string PictureName { get; set; }
        public string Content { get; set; }
        public string ImageUrl { get; set; }
        public int Percentage { get; set; }
    }
}
