using Microsoft.AspNetCore.Http;
using System;
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class UpdatePictureModel
    {
        [Required(ErrorMessage = "Id is required")]
        public int? Id { get; set; }

        [Required(ErrorMessage = "Name is required")]
        [StringLength(150, MinimumLength = 1, ErrorMessage = "Name length must containb maximum 150 characters")]
        public string Name { get; set; }


        [Required(ErrorMessage = "Image url is required")]
        public string ImageUrl { get; set; }

        [Required(ErrorMessage = "Picture content type id is required")]
        public int? ContentTypeId { get; set; }
    }
}
