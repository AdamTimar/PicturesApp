using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services.ServiceResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public interface IOrderService
    {
        public Task<OrderServiceResponses> AddOrders(List<OrderEntity> orders, int userId, string location);
        public Task<List<AdminsOrder>> GetOrders();
    }
}
