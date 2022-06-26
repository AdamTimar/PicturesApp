using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Models
{
    public class PictureWithSizePriceQuantity
    {
        public string PictureName { get; set; }
        public int Quantity { get; set; }
        public string Size { get; set; }

        public string Content { get; set; }
        public float Price { get; set; }
    }
}
