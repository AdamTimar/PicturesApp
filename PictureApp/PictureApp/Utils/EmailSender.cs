using MailKit.Net.Smtp;
using MailKit.Security;
using MimeKit;
using MimeKit.Text;

namespace PictureApp.Utils
{
    public static class EmailSender
    {
        public static bool SendEmail(string email, string subject, string text)
        {

            if (email == null || text == null)
                return false;
            try
            {
              
                var mimeMessage = new MimeMessage();
                mimeMessage.From.Add(MailboxAddress.Parse("softwareproject1124@gmail.com"));
                mimeMessage.To.Add(MailboxAddress.Parse(email));
                mimeMessage.Subject = subject;
                mimeMessage.Body = new TextPart(TextFormat.Plain) { Text = text};

                // send email

                SmtpClient client = new SmtpClient();
                client.Connect("smtp.gmail.com", 465, SecureSocketOptions.SslOnConnect);
                client.Authenticate("softwareproject1124@gmail.com", "dvzqxswzmbtpqfjk");
                client.Send(mimeMessage);
                client.Disconnect(true);
                client.Dispose();

                return true;
            }
            catch
            {
                return false;
            }

        }
    }
}

