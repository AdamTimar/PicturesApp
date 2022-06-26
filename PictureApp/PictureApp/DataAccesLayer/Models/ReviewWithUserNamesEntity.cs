namespace PictureApp.DataAccesLayer.Models
{
    public class ReviewWithUserNamesEntity
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string UserName { get; set; }
        public int PictureId { get; set; }
        public string PictureName { get; set; }
        public int QualityLevel { get; set; }
        public string Comment { get; set; }
    }
}
