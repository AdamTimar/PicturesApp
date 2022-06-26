using Microsoft.AspNetCore.Http;
using System;
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class UpdateVoucherModel
    {
        [Required(ErrorMessage = "Id is required")]
        public int? Id { get; set; }

        [Required(ErrorMessage = "Name is required")]
        [MaxLength(150, ErrorMessage = "Name length must containb maximum 150 characters")]
        public string Name { get; set; }

        [Required(ErrorMessage = "DeadLine is required")]
        [DataType(DataType.Date, ErrorMessage = "Invalid date type")]
        public DateTime? DeadLine { get; set; }

        [Required(ErrorMessage = "Image is required")]
        public IFormFile Image { get; set; }

    }
}
