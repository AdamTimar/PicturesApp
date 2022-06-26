using PictureApp.DataAccesLayer.Models;
using PictureApp.Services.ServiceResponses;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public interface IReviewService
    {
        public Task<ReviewServiceResponses> AddReview(ReviewEntity review);
        public Task<List<ReviewWithUserNamesEntity>> GetReviews();
        public Task<UserEntity> GetUserById(int id);
        public Task<ReviewWithUserNamesEntity> GetReviewById(int id);
        public Task<PictureEntity> GetPictureById(int id);
        public Task<List<ReviewWithUserNamesEntity>> GetPictureReviews(int pictureId);
        public Task<ReviewServiceResponses> DeleteReviewById(int id);
    }
}
