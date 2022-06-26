using PictureApp.DataAccesLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Models
{
    public class AdminsOrder
    {
        public string UserName { get; set; }

        public int UserId { get; set; }
        public List<PictureWithSizePriceQuantityEntity> Pictures { get; set; }

        public DateTime OrderDate { get; set; }

        public float Price { get; set; }

        public string Location { get; set; }
    }
}
