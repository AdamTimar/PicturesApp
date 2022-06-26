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
    public class HostedUserService : IHostedService
    {
        private Timer _timer;
        private readonly IServiceScopeFactory _scopeFactory;

        public HostedUserService(IServiceScopeFactory scopeFactrory)
        {
            _scopeFactory = scopeFactrory;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(
            new TimerCallback(DeleteUsersWhoDidNotRegisterAndUsersWhoDidNotChangePassword),
            null,
            TimeSpan.Zero,
            TimeSpan.FromSeconds(5));

            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            return Task.CompletedTask;
        }

        public void DeleteUsersWhoDidNotRegisterAndUsersWhoDidNotChangePassword(object state)
        {
            using (var scope = _scopeFactory.CreateScope())
            {
                var context = scope.ServiceProvider.GetRequiredService<Context>();

                foreach (InvalidatedUserEntity invalidatedUser in context.InvalidatedUsers)
                {
                    if (DateTime.Now > invalidatedUser.ValidationKeyExpirationDate)
                    {
                        context.InvalidatedUsers.Remove(invalidatedUser);
                    }
                }

                foreach (UserWhoChangesPasswordEntity userWhoChangesPassword in context.UsersWhoChangePassword)
                {
                    if (DateTime.Now > userWhoChangesPassword.ValidationKeyExpirationDate)
                    {
                        context.UsersWhoChangePassword.Remove(userWhoChangesPassword);
                    }
                }

                context.SaveChanges();
            }
        }
        
    }
}
