using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class PictureEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [MaxLength(150)]
        public string Name { get; set; }

        [Required]
        [MaxLength(1500)]
        public string ImageUrl { get; set; }


        [ForeignKey(nameof(ContentTypeId))]
        public PictureContentTypeEntity ContentType {get; set;}
        public int ContentTypeId { get; set; }


        public virtual ICollection<ReviewEntity> ReviewsAboutPicture { get; set; }

        public virtual ICollection<DiscountEntity> DiscountsOnPictures { get; set; }

        public virtual ICollection<OrderEntity> PicturesInOrder { get; set; }
    }
}
