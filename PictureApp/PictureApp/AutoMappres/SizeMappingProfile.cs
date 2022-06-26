using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.AutoMappres
{
    public class SizeMappingProfile : Profile
    {
        public SizeMappingProfile()
        {
            CreateMap<SizeEntity, SizeModel>();
        }
    }
}
