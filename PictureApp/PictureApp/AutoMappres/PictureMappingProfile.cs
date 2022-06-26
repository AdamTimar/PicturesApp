using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;

namespace PictureApp.AutoMappres
{
    public class PictureMappingProfile : Profile
    {
        public PictureMappingProfile()
        {
            CreateMap<AddPictureModel, PictureEntity>();
            CreateMap<UpdatePictureModel, PictureEntity>();
            CreateMap<PictureEntity, Picture>();
            CreateMap<PictureWithContentEntity, Picture>();
            CreateMap<PictureContentTypeEntity, ContentType>();
            CreateMap<TypeSizePriceEntity, TypeSizePrice>();

        }
    }
}
