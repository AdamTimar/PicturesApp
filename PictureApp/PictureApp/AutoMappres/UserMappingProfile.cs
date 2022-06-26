using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;

namespace PictureApp.AutoMappres
{
    public class UserMappingProfile : Profile
    {
        public UserMappingProfile()
        {
            CreateMap<User, UserEntity>();
            CreateMap<UserEntity,User>();
            CreateMap<AddUserModel, UserEntity>();
            CreateMap<UpdateUserModel, UserEntity>();
            CreateMap<InvalidatedUser, InvalidatedUserEntity>();
            CreateMap<InvalidatedUserEntity, InvalidatedUser>();
            CreateMap<InvalidatedUserEntity, UserEntity>();
            CreateMap<AddUserModel, InvalidatedUserEntity>();
            CreateMap<UserEntity, LoginResponseModel>().ForMember(l => l.Token, opt => opt.Ignore());

        }
    }
}
