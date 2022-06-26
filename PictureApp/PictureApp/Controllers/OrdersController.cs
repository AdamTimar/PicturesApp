using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrdersController : ControllerBase
    {
        private readonly IOrderService _orderService;
        private readonly IUserService _userService;
        private IMapper _mapper;

        public OrdersController(IOrderService orderService, IUserService userService, IMapper mapper)
        {
            _orderService = orderService;
            _userService = userService;
            _mapper = mapper;
        }

        [HttpPost]
        [Authorize(Roles = "User")]
        public async Task<IActionResult> AddOrder(AddOrderModel addOrderModel)
        {
            try
            {
                if (await _userService.GetUserById(addOrderModel.UserId) == null)
                    return NotFound(new BackEndResponse<object>(404, "User with given id not found"));

                var orderList = _mapper.Map<List<OrderEntity>>(addOrderModel.Orders);
                var result = await _orderService.AddOrders(orderList, addOrderModel.UserId, addOrderModel.Location);

                if (result == Services.ServiceResponses.OrderServiceResponses.PICTURENOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "One picture from the list was not found"));

                if (result == Services.ServiceResponses.OrderServiceResponses.SIZENOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "One size from the list was not found"));

                if (result == Services.ServiceResponses.OrderServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Order could not be added"));

                return StatusCode(201, new BackEndResponse<string>(201, null, "Order added"));

            }

            catch(Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetOrders()
        {
            try
            {
                var result = _mapper.Map<List<AdminsOrderResult>>(await _orderService.GetOrders());
                return StatusCode(200, new BackEndResponse<List<AdminsOrderResult>>(200, result));
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }
    }
}
