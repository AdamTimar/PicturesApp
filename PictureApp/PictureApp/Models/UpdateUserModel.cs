using System;
using System.ComponentModel.DataAnnotations;


namespace PictureApp.Models
{
    public class UpdateUserModel
    {
        [Required(ErrorMessage = "Id is required")]
        public int? Id{ get; set; }

        [EmailAddress(ErrorMessage = "Not an email address")]
        [MaxLength(150)]
        [Required(ErrorMessage = "Email is required")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Password is required")]
        [StringLength(50,MinimumLength = 8, ErrorMessage = "The password length is not between 8 and 50 characters")]
        public string Password { get; set; }

        [Required(ErrorMessage = "First name is required")]
        [StringLength(100,MinimumLength = 1, ErrorMessage = "The first name length is not between 1 and 50 characters")]
        public string FirstName { get; set; }

        [Required(ErrorMessage = "Last name is required")]
        [StringLength(100, MinimumLength = 1, ErrorMessage = "The last name length is not between 1 and 50 characters")]
        public string LastName { get; set; }

        [Required(ErrorMessage = "Birth date is required")]
        [DataType(DataType.Date, ErrorMessage = "Not a date form")]
        public DateTime BirthDate { get; set; }
    }
}
