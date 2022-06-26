using System;
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class AddReviewModel
    {
        [Required(ErrorMessage = "User id is required")]
        public int UserId { get; set; }

        [Required(ErrorMessage = "Picture id is required")]
        public int PictureId { get; set; }

        [Required(ErrorMessage = "Quality level is required")]
        [Range(1, 5, ErrorMessage = "Quality level must be between 1 and 5")]
        public int QualityLevel { get; set; }

        [MaxLength(1000, ErrorMessage = "Comment must contain maximum 1000 characters")]
        public string Comment { get; set; }
    }
}
