using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using PictureApp.DataAccesLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace PictureApp.Utils
{
    public class HostedDiscountService : IHostedService
    {
        private Timer _timer;
        private readonly IServiceScopeFactory _scopeFactory;


        public HostedDiscountService(IServiceScopeFactory scopeFactrory)
        {
            _scopeFactory = scopeFactrory;
        }
        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(
            new TimerCallback(ModifyDiscountsTable),
            null,
            TimeSpan.Zero,
            TimeSpan.FromSeconds(5));

            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            throw new NotImplementedException();
        }

        public void ModifyDiscountsTable(object state)
        {
            using (var scope = _scopeFactory.CreateScope())
            {
                var context = scope.ServiceProvider.GetRequiredService<Context>();
                foreach (var discount in context.Discounts)
                {
                    if (discount.DeadLine < DateTime.Now)
                        context.Discounts.Remove(discount);
                }
                context.SaveChanges();
            }
        }
    }
}
