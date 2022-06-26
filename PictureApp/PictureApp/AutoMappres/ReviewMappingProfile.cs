using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;

namespace PictureApp.AutoMappres
{
    public class ReviewMappingProfile : Profile
    {
        public ReviewMappingProfile()
        {
            CreateMap<ReviewEntity, Review>().ForMember(r => r.UserName, opt => opt.Ignore()).ForMember(r => r.PictureName, opt => opt.Ignore());
            CreateMap<ReviewWithUserNamesEntity, Review>();
            CreateMap<AddReviewModel, ReviewEntity>();

        }
    }
}
