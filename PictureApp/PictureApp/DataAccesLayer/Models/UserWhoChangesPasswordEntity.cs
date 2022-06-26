using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.DataAccesLayer.Models
{
    public class UserWhoChangesPasswordEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [ForeignKey(nameof(UserId))]
        public UserEntity User { get; set; }
        public int UserId { get; set; }

        [Required]
        [MaxLength(8)]
        public string ChangePasswordKey { get; set; }

        [Required]
        [DataType(DataType.Date)]
        public DateTime? ValidationKeyExpirationDate { get; set; }

        [Required]
        public bool KeyIsValidated { get; set; }
    }
}
