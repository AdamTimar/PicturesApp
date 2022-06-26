using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PictureApp.DataAccesLayer.Models
{
    public class OrderEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }


        public UserEntity Customer { get; set; }
        public int CustomerId { get; set; }
     

        [ForeignKey(nameof(PictureId))]
        public PictureEntity Picture { get; set; }
        public int PictureId { get; set; }


        [ForeignKey(nameof(SizeId))]
        public SizeEntity Size { get; set; }
        public int SizeId { get; set; }


        [Required]
        [Range(1,int.MaxValue)]
        public int Quantity { get; set; }

        [Required]
        public DateTime? OrderDate { get; set; }

        [Required]
        public string Location { get; set; }


    }
}
