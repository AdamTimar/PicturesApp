using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services.ServiceResponses;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public interface IUserService
    {
        public Task<List<UserEntity>> GetUsers();
        public Task<UserEntity> GetUserById(int id);
        public Task<UserEntity> GetUserByEmail(string email);

        public Task<UserServiceResponses> UpdateUser(UserEntity user);

        public Task<UserServiceResponses> DeleteUserById(int id);

        public Task<UserServiceResponses> AddUser(UserEntity user);

        public Task<UserServiceResponses> EmaiAlreadyExists(string email);

        public string GetEmailById(int id);

        public Task<UserServiceResponses> AddInvalidatedUser(InvalidatedUserEntity invalidatedUser);
        public Task<UserServiceResponses> UpdateInvalidatedUser(InvalidatedUserEntity invalidatedUser, AddUserModel user);
        public Task<UserServiceResponses> DeleteInvalidatedUserByEmail(string email);
        public Task<InvalidatedUserEntity> GetInvalidatedUserByEmail(string email);
        public Task<InvalidatedUserEntity> GetInvalidatedUserByValidationKey(string key);
        public Task<UserWhoChangesPasswordEntity> GetUserWhoChangesPasswordByChangePasswordKey(string key);
        public string GenerateJwtToken(UserEntity user);
        public Task<List<UserEntity>> GetUsersWhoHaveBirthDay();

        public Task<UserServiceResponses> AddUserWhoChangesPassword(UserWhoChangesPasswordEntity userWhoChangesPassword);

        public Task<UserServiceResponses> UpdateUserWhoChangesPassword(UserWhoChangesPasswordEntity userWhoChangesPassword);

        public Task<UserWhoChangesPasswordEntity> GetUserWhoWantsToChangePasswordById(int id);
        public Task<UserWhoChangesPasswordEntity> GetUserWhoWantsToChangePasswordByEmail(string email);
        public Task<bool> UserWhoChangesPasswordMatchedTheValidationKey(UserWhoChangesPasswordEntity user);
        public Task<bool> DeleteUserFromThoseWhoAreSetToChangePassword(UserWhoChangesPasswordEntity user);
        public Task<List<UserEntity>> GetWaiters();

    }
}
