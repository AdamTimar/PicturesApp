using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class PictureTypeEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [MaxLength(150)]
        [MinLength(1)]
        public string Name { get; set; }

        public virtual ICollection<TypeSizeEntity> TypeSizePrices { get; set; }
    }
}
