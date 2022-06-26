namespace PictureApp.Models
{
    public class Picture
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string ImageUrl { get; set; }
        public string Content { get; set; }
        public int Discount { get; set; }
        public float Average { get; set; }
    }
}
