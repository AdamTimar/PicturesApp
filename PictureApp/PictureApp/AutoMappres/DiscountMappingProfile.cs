using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.AutoMappres
{
    public class DiscountMappingProfile : Profile
    {
        public DiscountMappingProfile()
        {
            CreateMap<AddDiscountModel, DiscountEntity>();
            CreateMap<UpdateDiscountModel, DiscountEntity>();
            CreateMap<DiscountEntity, Discount>();
            CreateMap<DiscountWithImageUrlAndPictureNameEntity, Discount>();
        }
    }
}
