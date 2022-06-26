
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class EmailValidationKeyModel
    {
        [Required]
        [EmailAddress(ErrorMessage = "Not an email address")]
        [MaxLength(150, ErrorMessage = "Email must contain maximum 150 characters")]
        public string Email { get; set; }


        [Required]
        [StringLength(8,MinimumLength = 8,ErrorMessage = "The validation key must contain fix 8 characters")]
        public string ValidationKey { get; set; }
    }
}
