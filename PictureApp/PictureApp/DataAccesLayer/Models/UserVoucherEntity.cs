using System.ComponentModel.DataAnnotations;

namespace PictureApp.DataAccesLayer.Models
{
    public class UserVoucherEntity
    {
        public int UserId { get; set; }
        public UserEntity User { get; set; }
        public int VoucherId { get; set; }
        public DiscountEntity Voucher { get; set; }

        [Required]
        [MaxLength(8)]
        public string ValidationKey { get; set; }

        [Required]
        public bool UserNotifiedOfExpiration { get; set; }

        [Required]
        public bool UserNotifiedOfAppearance { get; set; }



    }
}
