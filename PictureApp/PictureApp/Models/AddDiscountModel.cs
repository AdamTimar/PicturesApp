using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Models
{
    public class AddDiscountModel
    {
        [Required(ErrorMessage = "Picture id is required")]
        public int PictureId { get; set; }

        [Required(ErrorMessage = "Discount percentage is required")]
        [Range(1,99, ErrorMessage = "Discount percentage is not between 1 and 99")]
        public int Percentage { get; set; }

        [Required(ErrorMessage = "Discount deadline is required")]
        public DateTime? DeadLine { get; set; }
    }
}
