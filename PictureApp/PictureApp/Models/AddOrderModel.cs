using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Models
{
    public class AddOrderModel
    {
        [Required(ErrorMessage = "Orders is required")]
        public List<Order> Orders { get; set; }

        [Required(ErrorMessage = "User id is required")]
        [Range(1, int.MaxValue, ErrorMessage = "User id must be greater than 0")]
        public int UserId { get; set; }
        
        [Required(ErrorMessage = "Location is required")]
        public string Location { get; set; }
    }
}
