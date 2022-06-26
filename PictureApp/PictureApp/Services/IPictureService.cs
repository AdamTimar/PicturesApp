using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services.ServiceResponses;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public interface IPictureService
    {
        public Task<PictureServiceResponses> AddPicture(PictureEntity Picture, int typeId);
        public Task<PictureServiceResponses> UpdatePicture(PictureEntity Picture, int typeId);
        public Task<PictureServiceResponses> DeletePictureById(int id);
        public Task<PictureEntity> GetPictureById(int id);
        public Task<PictureEntity> GetPictureByName(string name);
        public Task<List<PictureWithContentEntity>> GetPictures();
        public Task<List<PictureContentTypeEntity>> GetContentTypes();
        public Task<ReviewServiceResponses> DeleteReviewById(int id);
        public Task<List<SizeEntity>> GetSizes();
        public Task<PictureWithContentEntity> GetPictureWithContentById(int id);
    }
}
