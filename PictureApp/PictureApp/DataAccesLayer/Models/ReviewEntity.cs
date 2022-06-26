using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class ReviewEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        public UserEntity User { get; set; }
        public int UserId { get; set; }

        public PictureEntity Picture{ get; set; }
        public int PictureId { get; set; }

        [Required]
        [Range(0,5)]
        public int QualityLevel { get; set; }

        [MaxLength(1000)]
        public string Comment { get; set; }
    }
}
