using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class DiscountEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        [Required]
        [DataType(DataType.Date)]
        public DateTime? DeadLine { get; set; }

        [ForeignKey(nameof(PictureId))]
        public PictureEntity Picture { get; set; }
        public int PictureId { get; set; }

        [Required]
        [Range(1,99)]
        public int Percentage { get; set; }
      
    }
}
