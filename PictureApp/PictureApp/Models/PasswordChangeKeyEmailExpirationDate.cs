using System;

namespace PictureApp.Models
{
    public class PasswordChangeKeyEmailExpirationDate
    {
        public int UserId { get; set; }
        public string Email { get; set; }
        public DateTime PasswordChangeKeyExpirationDate { get; set; } 
    }
}
