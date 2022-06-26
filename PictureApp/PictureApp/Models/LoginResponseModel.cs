using System;

namespace PictureApp.Models
{
    public class LoginResponseModel
    {
        public int Id { get; set; }
        public string Email { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public DateTime BirthDate { get; set; }
        public string Role { get; set; }
        public string Token { get; set; }
    }
}
