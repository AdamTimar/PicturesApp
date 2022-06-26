
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class LoginModel
    {
        [EmailAddress(ErrorMessage = "Not an email address")]
        [MaxLength(150)]
        public string Email { get; set; }

        [StringLength(50, MinimumLength = 8, ErrorMessage = "The password length is not between 8 and 50 characters")]
        public string Password { get; set; }
    }
}
