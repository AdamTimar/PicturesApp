using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace PictureApp.Utils
{
    public class HostedBirthdayService : IHostedService
    {
        private Timer _timer;
        private readonly IServiceScopeFactory _scopeFactory;


        public HostedBirthdayService(IServiceScopeFactory scopeFactrory)
        {
            _scopeFactory = scopeFactrory;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(
            new TimerCallback(ModifyBirthDaysTable),
            null,
            TimeSpan.Zero,
            TimeSpan.FromSeconds(5));

            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            return Task.CompletedTask;
        }

        public void DeleteUsersWhoHadBirthDayYesterday(Context context)
        {

            foreach (UserWhoHasBirthdayEntity birthdayUser in context.UsersWhoHaveBirthday)
            {
                var user= context.Users.First(u => u.Id == birthdayUser.UserId);
                if(user.BirthDate.Value.Month != DateTime.Now.Month || user.BirthDate.Value.Day != DateTime.Now.Day)
                    context.UsersWhoHaveBirthday.Remove(birthdayUser);
            }

            context.SaveChanges();
        }

        public void AddUsersWhoHaveBirthDayToday(Context context)
        {

            foreach (UserEntity user in context.Users)
            {
                if (DateTime.Now.Month == user.BirthDate.Value.Month && DateTime.Now.Day == user.BirthDate.Value.Day && context.UsersWhoHaveBirthday.FirstOrDefault(bu => bu.UserId == user.Id) == null && !string.Equals(user.Role, "Admin"))
                {
                    context.UsersWhoHaveBirthday.Add(new UserWhoHasBirthdayEntity { UserId = user.Id });
                }
            }

            context.SaveChanges();
        }

                        
        public void ModifyBirthDaysTable(object state)
        {

            using (var scope = _scopeFactory.CreateScope())
            {
                var context = scope.ServiceProvider.GetRequiredService<Context>();

                if (context.UsersWhoHaveBirthday.Count() == 0)
                {
                        AddUsersWhoHaveBirthDayToday(context);                                        
                }
                else
                {                                    
                    DeleteUsersWhoHadBirthDayYesterday(context);
                    AddUsersWhoHaveBirthDayToday(context);

                    var birthDayUsers = context.Users as IQueryable<UserEntity>;
                    var result = birthDayUsers.Join(context.UsersWhoHaveBirthday, u => u.Id, ub => ub.UserId, (u, ub) => new UserIdBirthDateEmailFirstNameEntity { UserId = u.Id, BirthDate = (DateTime)u.BirthDate, Email = u.Email, FirstName = u.FirstName });

                    foreach (var birthDayUser in context.UsersWhoHaveBirthday)
                    {
                        if (birthDayUser.EmailSentToUser == false)
                        {
                            var birthDateEmailFirstName = result.Where(b => b.UserId == birthDayUser.UserId).First();
                            EmailSender.SendEmail(birthDateEmailFirstName.Email, "Birthday voucher", "Happy birthday " + birthDateEmailFirstName.FirstName + "! Come and see our picture store!");
                            birthDayUser.EmailSentToUser = true;
                        }
                    }

                    context.SaveChanges();
                    
                }
               
            }
        }
    }
}

