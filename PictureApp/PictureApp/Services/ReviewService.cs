using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Services.ServiceResponses;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public class ReviewService : IReviewService
    {
        private readonly Context _context;

        public ReviewService(Context context)
        {
            _context = context;
        }
        public async Task<ReviewServiceResponses> AddReview(ReviewEntity review)
        {
            if (review == null)
                return ReviewServiceResponses.NULLPARAM;

            _context.Reviews.Add(review);

            try
            {
                await _context.SaveChangesAsync();
                return ReviewServiceResponses.SUCCESS;
            }
            catch
            {
                return ReviewServiceResponses.EXCEPTION;
            }
        }

        public async Task<ReviewWithUserNamesEntity> GetReviewById(int id)
        {
            var list = _context.Reviews as IQueryable<ReviewEntity>;

            return await list.Where(r => r.Id == id).Join(_context.Users, r => r.UserId, u => u.Id, (r, u) => new { review = r, user = u })
                .Join(_context.Pictures, r2 => r2.review.PictureId, p2 => p2.Id, (r2, p2) => new ReviewWithUserNamesEntity { Id = r2.review.Id, UserId=r2.user.Id, UserName = r2.user.FirstName + ' ' + r2.user.LastName, PictureName = p2.Name, Comment = r2.review.Comment, QualityLevel = r2.review.QualityLevel, PictureId = p2.Id }).FirstOrDefaultAsync();

        }

        public async Task<List<ReviewWithUserNamesEntity>> GetReviews()
        {
            var list = _context.Reviews as IQueryable<ReviewEntity>;

            return await list.Join(_context.Users, r => r.UserId, u => u.Id, (r, u) => new { review = r, user = u })
                .Join(_context.Pictures, r2 => r2.review.PictureId, p2 => p2.Id, (r2, p2) => new ReviewWithUserNamesEntity { Id = r2.review.Id, UserId = r2.user.Id,UserName = r2.user.FirstName + ' ' + r2.user.LastName, PictureName = p2.Name, Comment = r2.review.Comment, QualityLevel = r2.review.QualityLevel, PictureId = p2.Id }).ToListAsync();

        }

        public async Task<List<ReviewWithUserNamesEntity>> GetPictureReviews(int PictureId)
        {
            var list = _context.Reviews as IQueryable<ReviewEntity>;

            return await list.Where(r => r.PictureId == PictureId).Join(_context.Users, r => r.UserId, u => u.Id, (r, u) => new { review = r, user = u })
                .Join(_context.Pictures, r2 => r2.review.PictureId, p2 => p2.Id, (r2, p2) => new ReviewWithUserNamesEntity { Id = r2.review.Id, UserId = r2.user.Id, UserName = r2.user.FirstName + ' ' + r2.user.LastName, PictureName = p2.Name, Comment = r2.review.Comment, QualityLevel = r2.review.QualityLevel, PictureId = p2.Id }).ToListAsync();

        }

        public async Task<UserEntity> GetUserById(int id)
        {
            return await _context.Users.FirstOrDefaultAsync(u => u.Id == id);
        }

        public async Task<PictureEntity> GetPictureById(int id)
        {
            return await _context.Pictures.FirstOrDefaultAsync(p => p.Id == id);
        }

        public async Task<ReviewServiceResponses> DeleteReviewById(int id)
        {
            var result = await _context.Reviews.FirstOrDefaultAsync(r => r.Id == id);
            if (result == null)
                return ReviewServiceResponses.REVIEWNOTFOUND;

            _context.Remove(result);
            try
            {
                await _context.SaveChangesAsync();
                return ReviewServiceResponses.SUCCESS;
            }
            catch
            {
                return ReviewServiceResponses.EXCEPTION;
            }
        }

    }
}
