namespace PictureApp.Models
{
    public class Review
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string UserName { get; set; }
        public string PictureName { get; set; }
        public int PictureId { get; set; }
        public int QualityLevel { get; set; }
        public string Comment { get; set; }
    }
}
