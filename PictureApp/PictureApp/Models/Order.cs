using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Models
{
    public class Order
    {
        [Required(ErrorMessage = "Picture id is required")]
        public int PictureId { get; set; }

        [Required(ErrorMessage = "Size id is required")]
        public int SizeId { get; set; }

        [Required(ErrorMessage = "Quantity is required")]
        [Range(1, int.MaxValue, ErrorMessage = "Quantity must be greater than 0")]
        public int Quantity { get; set; }
    }
}
