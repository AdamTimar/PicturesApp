using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class StorageEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        public int PictureId { get; set; }
        public PictureEntity Picture { get; set; }

        public int TypeSizeId { get; set; }
        public TypeSizeEntity TypeSize {get; set;}

        [Required]
        [Range(0,int.MaxValue)]
        public int Quantity { get; set; }

        public virtual ICollection<OrderEntity> Orders { get; set; }

    }
}
