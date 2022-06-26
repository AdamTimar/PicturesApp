namespace PictureApp.DataAccesLayer.Models
{
    public class PictureWithImageDataEntity
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int Price { get; set; }
        public string ImageData { get; set; }
        public int TypeId { get; set; }
    }
}
