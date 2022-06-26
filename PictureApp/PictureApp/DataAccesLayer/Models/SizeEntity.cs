using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class SizeEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [MaxLength(10)]
        public string Size { get; set; }


        [Range(1,float.MaxValue)]
        public float Price { get; set; }

        public virtual ICollection<OrderEntity> SizesInOrder { get; set; }
    }
}
