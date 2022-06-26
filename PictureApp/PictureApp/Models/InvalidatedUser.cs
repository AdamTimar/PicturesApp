using System;

namespace PictureApp.Models
{
    public class InvalidatedUser
    {
        public int Id { get; set; }
        public string Email { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public DateTime BirthDate { get; set; }
        public DateTime ValidationKeyExpirationDate { get; set; }
    }
}
