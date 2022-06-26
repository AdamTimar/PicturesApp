using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.DataAccesLayer.Models
{
    public class PictureWithSizePriceQuantityEntity
    {
        public string PictureName { get; set; }
        public int Quantity { get; set; }
        public string Size { get; set; }

        public string ImageUrl { get; set; }
        public string Content { get; set; }
        public float Price { get; set; }
        public int PictureId { get; set; }
    }
   
}
