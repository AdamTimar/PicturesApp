using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services;
using PictureApp.Services.ServiceResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DiscountsController : ControllerBase
    {

        private readonly IPictureService _pictureService;
        private readonly IDiscountService _discountService;
        private readonly IContentService _contentService;
        private IMapper _mapper;

        public DiscountsController(IPictureService PictureService, IContentService contentService, IDiscountService discountService, IMapper mapper)
        {

            _pictureService = PictureService;
            _discountService = discountService;
            _contentService = contentService;
            _mapper = mapper;
        }

        [Authorize(Roles = "Admin")]
        [HttpPost] 
        public async Task<IActionResult> AddDiscount(AddDiscountModel discount)
        {
            try
            {
                var picture = await _pictureService.GetPictureById(discount.PictureId);

                if (picture == null)
                    return NotFound(new BackEndResponse<object>(404, "Picture with given id not found"));

                var discountEntity = _mapper.Map<DiscountEntity>(discount);
                var result = await _discountService.AddDiscount(discountEntity);

                if (result == DiscountServiceResponses.EXCEPTION)
                    return StatusCode(500, (new BackEndResponse<object>(500, "Discount could not be added")));

                if (result == DiscountServiceResponses.DISCOUNTALREADYEXISTS)
                    return StatusCode(409, (new BackEndResponse<object>(409, "Discount on this picture already exists")));

                var discountResult = _mapper.Map<Discount>(discountEntity);
                discountResult.PictureName = picture.Name;
                discountResult.ImageUrl = picture.ImageUrl;
                discountResult.Content = (await _contentService.GetContentById(picture.ContentTypeId)).Name;

                return StatusCode(201, (new BackEndResponse<Discount>(201, discountResult)));
            }
            catch(Exception ex)
            {
                return StatusCode(500, (new BackEndResponse<object>(500, ex.Message)));
            }
        }

        /*
        [HttpPut]
        public async Task<IActionResult> UpdateDiscount(UpdateDiscountModel discount)
        {
            try
            {

                var result = await _discountService.GetDiscountById(discount.Id);

                if (result == null)
                    return NotFound(new BackEndResponse<object>(404, "Discount with given id not found"));

                var picture = await _pictureService.GetPictureById(discount.PictureId);

                if (picture == null)
                    return NotFound(new BackEndResponse<object>(400, "Picture with given id not found"));

                var discountEntity = _mapper.Map<DiscountEntity>(discount);
                var result2 = await _discountService.UpdateDiscount(discountEntity);

                if (result2 == DiscountServiceResponses.EXCEPTION)
                    return StatusCode(500, (new BackEndResponse<object>(500, "Discount could not be added")));

                var discountResult = _mapper.Map<Discount>(discountEntity);
                discountResult.PictureName = picture.Name;
                discountResult.ImageUrl = picture.ImageUrl;
                discountResult.Content = (await _contentService.GetContentById(picture.ContentTypeId)).Name;

                return StatusCode(201, (new BackEndResponse<Discount>(200, discountResult)));
            }

            catch (Exception ex)
            {
                return StatusCode(500, (new BackEndResponse<object>(500, ex.Message)));
            }

        }

        */

        [HttpDelete]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteDiscountById(int id)
        {
            try
            {
                var result = await _discountService.DeleteDiscountById(id);

                if (result == DiscountServiceResponses.NOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "Discount with given id not found"));

                if (result == DiscountServiceResponses.EXCEPTION)
                    return NotFound(new BackEndResponse<object>(500, "Could not dele discount"));

                return StatusCode(200, new BackEndResponse<string>(200, null, string.Format("Discount with id {0} deleted", id)));
            }

            catch (Exception ex)
            {
                return StatusCode(500, (new BackEndResponse<object>(500, ex.Message)));
            }
        }

        [HttpGet]
        public async Task<IActionResult> GetDiscounts()
        {
            try
            {
                var result = await _discountService.GetDiscounts();
                return StatusCode(200, new BackEndResponse<List<Discount>>(200, _mapper.Map<List<Discount>>(result)));

            }
             catch (Exception ex)
            {
                return StatusCode(500, (new BackEndResponse<object>(500, ex.Message)));
            }
            
        }

        [HttpGet]
        [Route("{id}")]
        public async Task<IActionResult> GetDiscountById(int id)
        {
            try
            {
                var result = await _discountService.GetDiscountById(id);

                if (result == null)
                    return NotFound(new BackEndResponse<object>(404, "Discount with given id not found"));

                return StatusCode(200, new BackEndResponse<Discount>(200, _mapper.Map<Discount>(result)));

            }
            catch (Exception ex)
            {
                return StatusCode(500, (new BackEndResponse<object>(500, ex.Message)));
            }

        }


    }
}
