using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services.ServiceResponses;
using PictureApp.Utils;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public class UserService : IUserService
    {
        private readonly Context _context;
        private readonly IConfiguration _config;
        public UserService(Context context, IConfiguration config)
        {
            _context = context;
            _config = config;
        }
        public async Task<UserServiceResponses> AddUser(UserEntity user)
        {
            if (user == null)
                return UserServiceResponses.NULLPARAM;

            if (user.BirthDate > DateTime.Now)
                return UserServiceResponses.BADDATE;


            _context.Users.Add(user);

            try
            {
                await _context.SaveChangesAsync();
            }

            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
            
        

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }

            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

           
        public async Task<UserServiceResponses> DeleteUserById(int id)
        {
            var user = await GetUserById(id);

            if (user == null)
                return UserServiceResponses.NOTFOUND;

            foreach(var review in _context.Reviews)
            {
                if (review.UserId == id)
                    _context.Reviews.Remove(review);
            }


            var birthDayUser = await _context.UsersWhoHaveBirthday.FirstOrDefaultAsync(bu => bu.Id == id);

            if (birthDayUser != null)
                _context.UsersWhoHaveBirthday.Remove(birthDayUser);

            var passwordChangeUser = await GetUserWhoWantsToChangePasswordById(id);

            if (passwordChangeUser != null)
                _context.UsersWhoChangePassword.Remove(passwordChangeUser);

            _context.Users.Remove(user);

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }

            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

       
        public async Task<UserEntity> GetUserById(int id)
        {
            return await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Id == id);
        }

        public async Task<UserEntity> GetUserByEmail(string email)
        {

            if (email == null)
                return null;

            return await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => string.Equals(u.Email, email));
        }


        public async Task<List<UserEntity>> GetUsers()
        {
            return await _context.Users.ToListAsync();
        }

        public async Task<UserServiceResponses> UpdateUser(UserEntity user)
        {
            if (user == null)
                return UserServiceResponses.NULLPARAM;

            if (user.BirthDate > DateTime.Now)
                return UserServiceResponses.BADDATE;

            _context.Entry(user).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }

            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

        public async Task<UserServiceResponses> EmaiAlreadyExists(string email)
        {
            if (email == null)
                return UserServiceResponses.NULLPARAM;

            var result = await _context.Users.AnyAsync(u => string.Equals(u.Email, email));
            if (result == true)
                return UserServiceResponses.TRUE;

            return UserServiceResponses.FALSE;
        }

        public string GetEmailById(int id)
        {
            return _context.Users.AsNoTracking().FirstOrDefault(u => u.Id == id).Email;
        }

        public async Task<UserServiceResponses> AddInvalidatedUser(InvalidatedUserEntity invalidatedUser)
        {
            if (invalidatedUser == null)
                return UserServiceResponses.NULLPARAM;

            if (invalidatedUser.BirthDate > DateTime.Now)
                return UserServiceResponses.BADDATE;

            string validationKey;
            do
            {
                validationKey = RandomStringGenerator.RandomString(8);
            }
            while (await GetInvalidatedUserByValidationKey(validationKey) != null);

            invalidatedUser.ValidationKey = validationKey;
            invalidatedUser.ValidationKeyExpirationDate = DateTime.Now.AddMinutes(5);
            _context.InvalidatedUsers.Add(invalidatedUser);
            try
            {
                await _context.SaveChangesAsync();

                if (EmailSender.SendEmail(invalidatedUser.Email, "Registration", "Your registration key is: " + validationKey + " .") == false)
                    return UserServiceResponses.FALSE;

                return UserServiceResponses.SUCCESS;
            }
            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

        public async Task<UserServiceResponses> DeleteInvalidatedUserByEmail(string email)
        {
            if (email == null)
                return UserServiceResponses.NULLPARAM;

            var userToDelete = await GetInvalidatedUserByEmail (email);

            if (userToDelete == null)
                return UserServiceResponses.NOTFOUND;

            _context.InvalidatedUsers.Remove(userToDelete);

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }
            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }


        public async Task<UserServiceResponses> UpdateInvalidatedUser(InvalidatedUserEntity invalidatedUser, AddUserModel user)
        {
            if (invalidatedUser == null)
                return UserServiceResponses.NULLPARAM;

            if (user.BirthDate > DateTime.Now)
                return UserServiceResponses.BADDATE;

            string validationKey;
            do
            {
                validationKey = RandomStringGenerator.RandomString(8);
            }
            while (await GetInvalidatedUserByValidationKey(validationKey) != null);

          
            invalidatedUser.BirthDate = user.BirthDate;
            invalidatedUser.Email = user.Email;
            invalidatedUser.FirstName = user.FirstName;
            invalidatedUser.LastName = user.LastName;
            invalidatedUser.Password = user.Password;
            invalidatedUser.ValidationKey = validationKey;
            invalidatedUser.ValidationKeyExpirationDate = DateTime.Now.AddMinutes(5);
            try
            {
                await _context.SaveChangesAsync();

                if (EmailSender.SendEmail(invalidatedUser.Email, "Registration", "Your registration key is: " + validationKey + " .") == false)
                    return UserServiceResponses.FALSE;

                return UserServiceResponses.SUCCESS;
            }
            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

        public async Task<InvalidatedUserEntity> GetInvalidatedUserByEmail(string email)
        {
            if (email == null)
                return null;

            return await _context.InvalidatedUsers.FirstOrDefaultAsync(iu => string.Equals(iu.Email, email));
        }

        public string GenerateJwtToken(UserEntity user)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(_config["Jwt:Issuer"],
              _config["Jwt:Issuer"],
              claims: new List<Claim>
                        {
                            new Claim(ClaimTypes.Name, user.Email),
                            new Claim(ClaimTypes.Role, user.Role),
                        },
              expires: DateTime.Now.AddMinutes(60),
              
            signingCredentials: credentials);

            
            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        public async Task<InvalidatedUserEntity> GetInvalidatedUserByValidationKey(string key)
        {
            return await _context.InvalidatedUsers.FirstOrDefaultAsync(iu => string.Equals(iu.ValidationKey, key));
        }

        public async Task<List<UserEntity>> GetUsersWhoHaveBirthDay()
        {
            var list = _context.UsersWhoHaveBirthday as IQueryable<UserWhoHasBirthdayEntity>;
            return await list.Join(_context.Users, ub => ub.UserId, u => u.Id, (ub, u) => new UserEntity { Id = u.Id, BirthDate = u.BirthDate, Email = u.Email, FirstName = u.FirstName, LastName = u.LastName, Password = u.Password}).ToListAsync();
        }

        public async Task<UserServiceResponses> AddUserWhoChangesPassword(UserWhoChangesPasswordEntity userWhoChangesPassword)
        {
            _context.UsersWhoChangePassword.Add(userWhoChangesPassword);

            var user = await GetUserById(userWhoChangesPassword.UserId);

            try
            {
                await _context.SaveChangesAsync();

                if (EmailSender.SendEmail(user.Email, "Change password", "Your password change key is " + userWhoChangesPassword.ChangePasswordKey + " .") == false)
                    return UserServiceResponses.FALSE;

                return UserServiceResponses.SUCCESS;
            }

            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

        public async Task<UserServiceResponses> UpdateUserWhoChangesPassword(UserWhoChangesPasswordEntity userWhoChangesPassword)
        {
            _context.Entry(userWhoChangesPassword).State = EntityState.Modified;

            var user = await GetUserById(userWhoChangesPassword.UserId);
            try
            {
                await _context.SaveChangesAsync();

                if (EmailSender.SendEmail(user.Email, "Change password", "Your password change key is " + userWhoChangesPassword.ChangePasswordKey + " .") == false)
                    return UserServiceResponses.FALSE;

                return UserServiceResponses.SUCCESS;
            }

            catch
            {
                return UserServiceResponses.EXCEPTION;
            }
        }

        public async Task<UserWhoChangesPasswordEntity> GetUserWhoWantsToChangePasswordById(int id)
        {
            return await _context.UsersWhoChangePassword.AsNoTracking().FirstOrDefaultAsync(ucp => ucp.UserId == id);
        }

        public async Task<UserWhoChangesPasswordEntity> GetUserWhoWantsToChangePasswordByEmail(string email)
        {
            var user = await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => string.Equals(u.Email, email));

            if (user == null)
                return null;

            return await _context.UsersWhoChangePassword.AsNoTracking().FirstOrDefaultAsync(ucp => ucp.UserId == user.Id);
        }

        public async Task<bool> UserWhoChangesPasswordMatchedTheValidationKey(UserWhoChangesPasswordEntity user)
        {
            _context.Entry(user).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> DeleteUserFromThoseWhoAreSetToChangePassword(UserWhoChangesPasswordEntity user)
        {
            _context.UsersWhoChangePassword.Remove(user);

            try
            {
                await _context.SaveChangesAsync();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public async Task<UserWhoChangesPasswordEntity> GetUserWhoChangesPasswordByChangePasswordKey(string key)
        {
            return await _context.UsersWhoChangePassword.AsNoTracking().FirstOrDefaultAsync(ucp => string.Equals(ucp.ChangePasswordKey, key));
        }

        public async Task<List<UserEntity>> GetWaiters()
        {
            return await _context.Users.Where(u => string.Equals(u.Role, "Waiter")).ToListAsync();
        }
    }
}
