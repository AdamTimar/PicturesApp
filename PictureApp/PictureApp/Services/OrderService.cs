using Microsoft.EntityFrameworkCore;
using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services.ServiceResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public class OrderService : IOrderService
    {
        private readonly Context _context;
        public OrderService(Context context)
        {
            _context = context;
        }
        public async Task<OrderServiceResponses> AddOrders(List<OrderEntity> orders, int userId, string location)
        {
            var orderDate = DateTime.Now;
            foreach (var o in orders)
            {
                if (await _context.Pictures.AsNoTracking().FirstOrDefaultAsync(p => p.Id == o.PictureId) == null)
                    return OrderServiceResponses.PICTURENOTFOUND;

                if (await _context.Sizes.AsNoTracking().FirstOrDefaultAsync(s => s.Id == o.SizeId) == null)
                    return OrderServiceResponses.SIZENOTFOUND;

                o.CustomerId = userId;
                o.OrderDate = orderDate;
                o.Location = location;

                _context.Orders.Add(o);
            }

            try
            {
                await _context.SaveChangesAsync();
                return OrderServiceResponses.SUCCESS;
            }
            catch
            {
                return OrderServiceResponses.EXCEPTION;
            }


        }

        public async Task<List<AdminsOrder>> GetOrders()
        {
            var result = new List<AdminsOrder>();
            var list = _context.Orders.GroupBy(o => new { o.CustomerId, o.OrderDate})
                .Select(g => new { g.Key.CustomerId, g.Key.OrderDate});


            foreach (var uo in list)
            {
                var location = (await _context.Orders.FirstOrDefaultAsync(o => o.CustomerId == uo.CustomerId && DateTime.Equals(uo.OrderDate, o.OrderDate))).Location;
                var usersOrders = new List<PictureWithSizePriceQuantityEntity>();
                var matchingOrders = _context.Orders.Where(o => o.CustomerId == uo.CustomerId && DateTime.Equals(uo.OrderDate, o.OrderDate));
                var list2 = await matchingOrders.Join(_context.Pictures, o => o.PictureId, p => p.Id, (o, p) => new { a = o, b = p })
                    .Join(_context.PictureContents, p1 => p1.b.ContentTypeId, c => c.Id, (p1, c) => new { a1 = p1, b1 = c})
                    .Join(_context.Sizes, op => op.a1.a.SizeId, s => s.Id, (op, s) => new PictureWithSizePriceQuantityEntity { ImageUrl = op.a1.b.ImageUrl, PictureId = op.a1.b.Id, PictureName = op.a1.b.Name, Quantity = op.a1.a.Quantity, Size = s.Size, Content = op.b1.Name, Price = s.Price}).ToListAsync();

                float sum = 0;

                for(int i=0; i <list2.Count(); i++)
                {
                    var discount = await _context.Discounts.AsNoTracking().FirstOrDefaultAsync(d => d.PictureId == list2[i].PictureId);
                    if (discount != null)
                        list2[i].Price -= list2[i].Price * (float)discount.Percentage / 100;
                    sum += list2[i].Price * list2[i].Quantity;
                }

                var user = await _context.Users.FirstOrDefaultAsync(u => u.Id == uo.CustomerId);
                result.Add(new AdminsOrder { UserId = user.Id, OrderDate = (DateTime)uo.OrderDate, Pictures = list2.ToList(), UserName = user.FirstName + " " + user.LastName, Price = sum, Location = location  });
            }

            return result;
        }
    }
}
