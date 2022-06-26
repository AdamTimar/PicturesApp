using System;
using System.ComponentModel.DataAnnotations;

namespace PictureApp.Models
{
    public class Voucher
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public DateTime DeadLine { get; set; }
        public string ImageData { get; set; }
    }
}
