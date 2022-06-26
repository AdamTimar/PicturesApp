using System.ComponentModel.DataAnnotations;

namespace PictureApp.DataAccesLayer.Models
{
    public class SendChangePasswordKeyModel
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; }
    }
}
