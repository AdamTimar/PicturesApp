using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class UserVoucher
    {
        [Required]
        public int? UserId { get; set; }

        [Required]
        public int? VoucherId { get; set; }

        [Required]
        [StringLength(8, MinimumLength = 8)]
        public string ValidationKey { get; set; }
    }
}
