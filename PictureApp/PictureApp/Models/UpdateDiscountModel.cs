using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Models
{
    public class UpdateDiscountModel
    {
        [Required(ErrorMessage = "Discount id is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "Picture id is required")]
        public int PictureId { get; set; }

        [Required(ErrorMessage = "Discount percentage is required")]
        [Range(0, 99, ErrorMessage = "Discount percentage is not between 0 and 99")]
        public int Percentage { get; set; }

        public DateTime? DeadLine { get; set; }
    }
}
