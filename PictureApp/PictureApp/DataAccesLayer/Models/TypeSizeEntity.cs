using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class TypeSizeEntity
    {

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        
        public int TypeId { get; set; }
        public PictureTypeEntity Type { get; set; }


        public int SizeId { get; set; }
        public SizeEntity Size { get; set; }


        [Required]
        [Range(1,float.MaxValue)]
        public float Price { get; set; }

    }
}
