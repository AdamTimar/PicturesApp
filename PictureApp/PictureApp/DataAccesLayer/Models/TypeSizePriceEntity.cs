using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.DataAccesLayer.Models
{
    public class TypeSizePriceEntity
    {
        public int Id { get; set; }
        public string Type { get; set; }
        public string Size { get; set; }
        public float Price { get; set; }
    }
}
