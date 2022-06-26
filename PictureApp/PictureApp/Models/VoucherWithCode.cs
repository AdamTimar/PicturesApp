using System;

namespace PictureApp.Models
{
    public class VoucherWithCode
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public DateTime DeadLine { get; set; }
        public string ValidationKey { get; set; }
        public string ImageData { get; set; }
    }
}
