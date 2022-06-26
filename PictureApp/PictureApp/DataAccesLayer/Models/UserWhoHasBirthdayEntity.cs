using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class UserWhoHasBirthdayEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [ForeignKey(nameof(UserId))]
        public UserEntity User { get; set; }
        public int UserId { get; set; }

        [Required]
        public bool EmailSentToUser {get; set;}
    }
}
