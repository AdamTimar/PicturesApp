using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services;
using PictureApp.Services.ServiceResponses;
using PictureApp.Utils;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PictureApp.Controllers
{
    [ApiController]
    [Route("api/[controller]")]

    public class UsersController : ControllerBase
    {
        private readonly IUserService _userService;
        private IMapper _mapper;

        public UsersController(IUserService userService, IMapper mapper)
        {
            _userService = userService;
            _mapper = mapper;
        }

        [HttpGet("{id}")]
        [Authorize(Roles = "User,Admin")]
        public async Task<IActionResult> GetUser(int id)
        {


            var user = await _userService.GetUserById(id);
            if (user == null)
            {
                string template = "User with id {0} doesn't exist";
                return NotFound(new BackEndResponse<object>(404, string.Format(template, id)));
            }

            try
            {
                var result = _mapper.Map<User>(user);
                return Ok(new BackEndResponse<User>(200, result));
            }
            catch (Exception ex)
            {

                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

      
        [HttpPost]
        [Route("Registration")]
        public async Task<IActionResult> RegistrateUser(EmailValidationKeyModel emailValidationKeyModel)
        {
            try
            {

                var invalidatedUser = await _userService.GetInvalidatedUserByEmail(emailValidationKeyModel.Email);

                if (invalidatedUser == null)
                    return NotFound(new BackEndResponse<object>(404, "Invalidated user doesn't exist"));

                UserEntity userEntity = new UserEntity();

                userEntity.BirthDate = invalidatedUser.BirthDate;
                userEntity.Email = invalidatedUser.Email;
                userEntity.FirstName = invalidatedUser.FirstName;
                userEntity.LastName = invalidatedUser.LastName;
                userEntity.Password = invalidatedUser.Password;

                if (!string.Equals(emailValidationKeyModel.ValidationKey, invalidatedUser.ValidationKey, StringComparison.OrdinalIgnoreCase))
                    return NotFound(new BackEndResponse<object>(404, "The given key is incorrect or it expired"));

                var result = await _userService.DeleteInvalidatedUserByEmail(invalidatedUser.Email);
             
                if (result == UserServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "Ungiven email address"));

                if (result == UserServiceResponses.NOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "Invalidated user not found"));

                if (result == UserServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Invalidated user couldn't be deleted"));

                var result2 = await _userService.AddUser(userEntity);

                if (result2 == UserServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "User to add cant't be null"));

                if (result2 == UserServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "User couldn't be added"));

                var userModel = _mapper.Map<User>(userEntity);


                return StatusCode(201, new BackEndResponse<User>(201, _mapper.Map<User>(userEntity)));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpPut]
        [Authorize(Roles = "User,Admin")]
        public async Task<IActionResult> UpdateUser(UpdateUserModel user)
        {
            try
            {
                if (user == null)
                    return BadRequest(new BackEndResponse<object>(400, "Updateable user can't be null"));

                if (await _userService.GetUserById((int)user.Id) == null)
                {
                    string template = "User with id {0} doesn't exist";
                    return NotFound(new BackEndResponse<object>(404, string.Format(template, user.Id)));
                }

                if (await _userService.EmaiAlreadyExists(user.Email) == UserServiceResponses.TRUE && _userService.GetEmailById((int)user.Id) != user.Email)
                {
                    return StatusCode(409, new BackEndResponse<object>(409, "Email already exists"));
                }


                var userWithUpdates = _mapper.Map<UserEntity>(user);

                var result = await _userService.UpdateUser(userWithUpdates);

                if (result == UserServiceResponses.BADDATE)
                    return BadRequest(new BackEndResponse<object>(400, "Birthdate can't be later than current date"));

                if (result == UserServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "User to update cant't be null"));

                if (result == UserServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "User couldn't be updated"));

                return Ok(new BackEndResponse<User>(200, _mapper.Map<User>(userWithUpdates)));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }

        }



        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteUser(int id)
        {
            try
            {
                var userToDelete = await _userService.GetUserById(id);

                if (userToDelete == null)
                {
                    string template = "User with id {0} doesn't exist";
                    return NotFound(new BackEndResponse<object>(404, string.Format(template, id)));
                }

                var result = await _userService.DeleteUserById(id);
                if (result == UserServiceResponses.NOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "User not found"));

                if (result == UserServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "User delete fail"));

                return StatusCode(200, new BackEndResponse<string>(200, null, string.Format("User with id {0} deleted", userToDelete.Id)));

            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }

        }

        [HttpPost]
        [Route("SendRegistrationKey")]
        public async Task<IActionResult> SendRegistrationKey(AddUserModel user)
        {
            try
            {
                if (await _userService.GetUserByEmail(user.Email) != null)
                {
                    return StatusCode(409, new BackEndResponse<object>(409, "User already registered"));
                }


                var result = await _userService.GetInvalidatedUserByEmail(user.Email);

                if (result == null)
                {
                    var userToAdd = _mapper.Map<InvalidatedUserEntity>(user);

                    var result2 = await _userService.AddInvalidatedUser(userToAdd);

                    if (result2 == UserServiceResponses.BADDATE)
                        return BadRequest(new BackEndResponse<object>(400, "Birthdate can't be later than current date"));

                    if (result2 == UserServiceResponses.NULLPARAM)
                        return BadRequest(new BackEndResponse<object>(400, "Invalidated user to add can't be null"));

                    if (result2 == UserServiceResponses.EXCEPTION)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't add invalidated user to wait for registration key"));

                    if (result2 == UserServiceResponses.FALSE)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't send email"));

                    var invalidatedUserResponse = _mapper.Map<InvalidatedUser>(userToAdd);


                    return StatusCode(201, new BackEndResponse<InvalidatedUser>(201, invalidatedUserResponse));
                }
                else
                {
                    var result3 = await _userService.UpdateInvalidatedUser(result, user);

                    if (result3 == UserServiceResponses.BADDATE)
                        return BadRequest(new BackEndResponse<object>(400, "Birthdate can't be later than current date"));

                    if (result3 == UserServiceResponses.NULLPARAM)
                        return BadRequest(new BackEndResponse<object>(400, "Invalidated user to update can't be null"));

                    if (result3 == UserServiceResponses.EXCEPTION)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't add invalidated user to wait for registration key"));

                    if (result3 == UserServiceResponses.FALSE)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't resend email"));

                    var invalidatedUserResponse = _mapper.Map<InvalidatedUser>(result);

                    return StatusCode(200, new BackEndResponse<InvalidatedUser>(200, invalidatedUserResponse));
                }

                

            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpPost]
        [Route("Login")]
        public async Task<IActionResult> LoginUser(LoginModel loginModel)
        {
            try
            {
                var result = await _userService.GetUserByEmail(loginModel.Email);
                if (result == null)
                {
                    return NotFound(new BackEndResponse<object>(404, "Email doesn't exist"));
                }

                if (!string.Equals(loginModel.Password, result.Password))
                {
                    return StatusCode(401, new BackEndResponse<object>(401, "Password mismatch"));
                }

                var user = _mapper.Map<LoginResponseModel>(result);
                user.Token = _userService.GenerateJwtToken(result);
                return StatusCode(201, new BackEndResponse<LoginResponseModel>(201, user));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Route("UsersWhoHaveBirthDay")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetUsersWhoHaveBirthDay()
        {
            try
            {
                var result = await _userService.GetUsersWhoHaveBirthDay();

                return StatusCode(200, new BackEndResponse<List<User>>(200, _mapper.Map<List<User>>(result)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpPost]
        [Route("SendChangePasswordKey")]

        public async Task<IActionResult> SendPasswordChangeKey(SendChangePasswordKeyModel sendPassword)
        {
            try
            {
                var userWhoChangesPassword = new UserWhoChangesPasswordEntity();
                var user = await _userService.GetUserByEmail(sendPassword.Email);
                if (user == null)
                    return NotFound(new BackEndResponse<object>(404, "User with given email not found"));

                userWhoChangesPassword.UserId = user.Id;
                userWhoChangesPassword.ValidationKeyExpirationDate = DateTime.Now.AddMinutes(5);
                userWhoChangesPassword.KeyIsValidated = false;

                string changePasswordKey;

                do
                {
                    changePasswordKey = RandomStringGenerator.RandomString(8);

                }
                while (await _userService.GetUserWhoChangesPasswordByChangePasswordKey(changePasswordKey) != null);

                userWhoChangesPassword.ChangePasswordKey = changePasswordKey;
                var result = await _userService.GetUserWhoWantsToChangePasswordById(userWhoChangesPassword.UserId);

                if (result == null)
                {
                    var addResult = await _userService.AddUserWhoChangesPassword(userWhoChangesPassword);

                    if (addResult == UserServiceResponses.FALSE)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't send password change key via email"));

                    if (addResult == UserServiceResponses.EXCEPTION)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't add user who wants to change password"));

                    return StatusCode(200, new BackEndResponse<PasswordChangeKeyEmailExpirationDate>(200, null, new PasswordChangeKeyEmailExpirationDate { PasswordChangeKeyExpirationDate = (DateTime)userWhoChangesPassword.ValidationKeyExpirationDate, Email = user.Email, UserId = userWhoChangesPassword.UserId }));
                }
                else
                {
                    userWhoChangesPassword.Id = result.Id;
                    var updateResult = await _userService.UpdateUserWhoChangesPassword(userWhoChangesPassword);

                    if (updateResult == UserServiceResponses.FALSE)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't send password change key via email"));

                    if (updateResult == UserServiceResponses.EXCEPTION)
                        return StatusCode(500, new BackEndResponse<object>(500, "Couldn't update user who wants to change password"));

                    return StatusCode(200, new BackEndResponse<PasswordChangeKeyEmailExpirationDate>(200, null, new PasswordChangeKeyEmailExpirationDate { PasswordChangeKeyExpirationDate = (DateTime)userWhoChangesPassword.ValidationKeyExpirationDate, Email = user.Email, UserId = userWhoChangesPassword.UserId }));
                }

            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpPost]
        [Route("ValidatePasswordChangeKey")]

        public async Task<IActionResult> ValidatePasswordChangeKey(EmailValidationKeyModel emailValidationKey)
        {
            try
            {
                var userWhoChangesPassword = await _userService.GetUserWhoWantsToChangePasswordByEmail(emailValidationKey.Email);

                if (userWhoChangesPassword == null)
                    return NotFound(new BackEndResponse<object>(404, "User with given email not found or did not request to change password"));

                if (!string.Equals(emailValidationKey.ValidationKey, userWhoChangesPassword.ChangePasswordKey, StringComparison.OrdinalIgnoreCase))
                    return NotFound(new BackEndResponse<object>(404, "Incorrect password change key or the key expired"));

                userWhoChangesPassword.KeyIsValidated = true;
                var result = await _userService.UserWhoChangesPasswordMatchedTheValidationKey(userWhoChangesPassword);

                if (result == false)
                    return StatusCode(500, new BackEndResponse<string>(500, "There was an error when user was set to be ready to change his/her password"));

                return StatusCode(200, new BackEndResponse<string>(200, null, "User with email " + emailValidationKey.Email + " can reset his/her password"));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }


        [HttpPut]
        [Route("ChangePassword")]
        public async Task<IActionResult> ChangePassword(EmailPasswordModel emailPassword)
        {
            try
            {
                var result = await _userService.GetUserWhoWantsToChangePasswordByEmail(emailPassword.Email);

                if (result == null)
                    return NotFound(new BackEndResponse<object>(404, "User with given email not found or did not request to change password"));

                if (result.KeyIsValidated == false)
                    return NotFound(new BackEndResponse<object>(404, "User with given did not validate password change key"));

                var user = await _userService.GetUserById(result.UserId);
                user.Password = emailPassword.Password;

                var updateResult = await _userService.UpdateUser(user);

                if (updateResult == UserServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "User to update cant't be null"));

                if (updateResult == UserServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Password couldn't be changed"));

                var deleteResult = await _userService.DeleteUserFromThoseWhoAreSetToChangePassword(result);

                if(deleteResult == false)
                    return StatusCode(500, new BackEndResponse<object>(500, "Could not delete user from the pasword changers table"));

                return StatusCode(201, new BackEndResponse<string>(200, null, "User with email " + emailPassword.Email + " reseted his/her password"));
            }

            catch(Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }

        }


    }
}

