using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services;
using PictureApp.Services.ServiceResponses;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;


namespace PictureApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReviewsController: ControllerBase
    {
        private readonly IReviewService _reviewService;
        private IMapper _mapper;

        public ReviewsController(IReviewService reviewService, IMapper mapper)
        {
            _reviewService = reviewService;
            _mapper = mapper;
        }

        [HttpPost]
        [Authorize(Roles = "User")]
        public async Task<IActionResult> AddReview(AddReviewModel review)
        {
            try
            {
                var reviewEntity = _mapper.Map<ReviewEntity>(review);
                var result = await _reviewService.AddReview(reviewEntity);

                if(result == ReviewServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "Review to add can't be null"));

                var user = await _reviewService.GetUserById(review.UserId);

                if(user == null)
                    return NotFound(new BackEndResponse<object>(404, "User with given id not found"));

                if (!string.Equals(user.Role,"User"))
                    return NotFound(new BackEndResponse<object>(404, "User with given id is not a simple user"));

                var picture = await _reviewService.GetPictureById(review.PictureId);

                if (picture == null)
                    return NotFound(new BackEndResponse<object>(404, "Picture with given id not found"));
            

                if (result == ReviewServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Review could't be added"));

                var userResult = _mapper.Map<Review>(reviewEntity);
                userResult.UserName = user.FirstName + " " + user.LastName;
                userResult.PictureName = picture.Name;

                return StatusCode(200, new BackEndResponse<Review>(200, userResult));
            
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        public async Task<IActionResult> GetReviews()
        {
            var reviews = await _reviewService.GetReviews();

            try
            {
                return StatusCode(200, new BackEndResponse<List<Review>>(200, _mapper.Map<List<Review>>(reviews)));
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "Admin,User")]
        [Route("PictureReviews{pictureId}")]
        public async Task<IActionResult> GetPictureReviews(int pictureId)
        {
            var picture = await _reviewService.GetPictureById(pictureId);

            if(picture == null)
                return NotFound(new BackEndResponse<object>(404, "Picture with given id not found"));

            var reviews = await _reviewService.GetPictureReviews(pictureId);
             
            try
            {
                return StatusCode(200, new BackEndResponse<List<Review>>(200, _mapper.Map<List<Review>>(reviews)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Route("{id}")]
        public async Task<IActionResult> GetReviewById(int id)
        {
            var review = await _reviewService.GetReviewById(id);

            if (review == null)
                return NotFound(new BackEndResponse<object>(404, "Review with given id not found"));

            try
            {
                return StatusCode(200, new BackEndResponse<Review>(200, _mapper.Map<Review>(review)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }


        [HttpDelete]
        [Authorize(Roles = "User")]
        [Route("{id}")]
        public async Task<IActionResult> DeleteReviewById(int id)
        {
            var result = await _reviewService.DeleteReviewById(id);

            if (result == ReviewServiceResponses.REVIEWNOTFOUND)
                return NotFound(new BackEndResponse<object>(404, "Review with given id not found"));

            if (result == ReviewServiceResponses.EXCEPTION)
                return StatusCode(500, new BackEndResponse<object>(500, "Review could't be deleted"));

            try
            {
                return StatusCode(200, new BackEndResponse<string>(200, null, string.Format("Review with id {0} deleted", id)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }



    }
}
