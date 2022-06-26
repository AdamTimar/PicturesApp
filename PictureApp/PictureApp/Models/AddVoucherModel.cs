using Microsoft.AspNetCore.Http;
using System;
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class AddVoucherModel
    {
        [Required(ErrorMessage = "Name is required")]
        [StringLength(150, MinimumLength = 1, ErrorMessage = "Name length must containb maximum 150 characters")]
        public string Name { get; set; }

        [Required(ErrorMessage = "Deadline required")]
        [DataType(DataType.Date, ErrorMessage = "Invalid date type")]
        public DateTime? DeadLine { get; set; }

        [Required(ErrorMessage = "Image required")]
        public IFormFile Image { get; set; }

    }
}
