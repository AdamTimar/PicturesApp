using AutoMapper;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services;
using PictureApp.Services.ServiceResponses;
using PictureApp.Utils;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PictureApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PicturesController : ControllerBase
    {
        private readonly IPictureService _pictureService;
        private readonly IContentService _contentService;
        private IMapper _mapper;

        public PicturesController(IPictureService PictureService, IContentService contentService, IMapper mapper)
        { 
     
            _pictureService = PictureService;
            _contentService = contentService;
            _mapper = mapper;
        }

        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> AddPicture(AddPictureModel Picture)
        {
            try
            {
       
                var PictureEntity = _mapper.Map<PictureEntity>(Picture);

                var result = await _pictureService.AddPicture(PictureEntity, (int)Picture.ContentTypeId);

                if (result == PictureServiceResponses.CONTENTTYPENOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "Picture content type doesn't exist"));

                if (result == PictureServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "Picture to add can't be null"));

                if (result == PictureServiceResponses.PICTURENAMEALREADYEXISTS)
                    return StatusCode(409, new BackEndResponse<object>(409, "Picture name already exists"));

                if (result == PictureServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Picture couldn't be added"));

                var PictureResult = _mapper.Map<Picture>(PictureEntity);
                PictureResult.Content = (await _contentService.GetContentById((int)Picture.ContentTypeId)).Name;

                return StatusCode(201, new BackEndResponse<Picture>(201, PictureResult));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }
        [HttpDelete]
        [Authorize(Roles = "Admin")]
        [Route("{id}")]
        public async Task<IActionResult> DeletePicture(int id)
        {

            try
            {
                var Picture = await _pictureService.GetPictureById(id);

                if (Picture == null)
                {
                    return NotFound(new BackEndResponse<object>(404, "Picture not found"));
                }

                var result = await _pictureService.DeletePictureById(id);

                if (result == PictureServiceResponses.NOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "Picture not found"));

                if (result == PictureServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Picture couldn't be deleted"));

                return StatusCode(200, new BackEndResponse<string>(200, null, string.Format("Picture with id {0} deleted", Picture.Id)));

            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpPut]
        public async Task<IActionResult> UpdatePicture( UpdatePictureModel Picture)
        {

            try
            { 
                
                var PictureEntity = _mapper.Map<PictureEntity>(Picture);
                var result = await _pictureService.UpdatePicture(PictureEntity, (int)Picture.ContentTypeId);

                if (result == PictureServiceResponses.CONTENTTYPENOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "Picture content type doesn't exist"));

                if (result == PictureServiceResponses.NOTFOUND)
                    return NotFound(new BackEndResponse<object>(404, "Id doesn't exist"));

                if (result == PictureServiceResponses.NULLPARAM)
                    return BadRequest(new BackEndResponse<object>(400, "Picture to update can't be null"));

                if (result == PictureServiceResponses.PICTURENAMEALREADYEXISTS)
                    return StatusCode(409, new BackEndResponse<object>(409, "Picture name already exists"));


                if (result == PictureServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackEndResponse<object>(500, "Picture couldn't be updated"));


                var updatedPicture = _mapper.Map<Picture>(PictureEntity);
                updatedPicture.Content = (await _contentService.GetContentById((int)Picture.ContentTypeId)).Name;

                return StatusCode(200, new BackEndResponse<Picture>(200, updatedPicture));

            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "Admin,User")]
        public async Task<IActionResult> GetPictures()
        {
            var result = await _pictureService.GetPictures();
            try
            {
                return Ok(new BackEndResponse<List<Picture>>(200, _mapper.Map<List<Picture>>(result)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "Admin,User")]
        [Route("{id}")]
        public async Task<IActionResult> GetPictureById(int id)

        {
            var result = await _pictureService.GetPictureWithContentById(id);

            if (result == null)
                return NotFound(new BackEndResponse<object>(404, "Picture not found"));


            try
            {
                var Picture = _mapper.Map<Picture>(result);
                return Ok(new BackEndResponse<Picture>(200, Picture));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }      

        [HttpGet]
        [Route("Contents")]
        [Authorize(Roles = "Admin,User")]
        public async Task<IActionResult> GetPictureContents()
        {

            try
            {
                var contentTypes = _mapper.Map<List<ContentType>>(await _pictureService.GetContentTypes());

                return Ok(new BackEndResponse<List<ContentType>>(200, contentTypes));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "Admin,User")]
        [Route("Sizes")]
        public async Task<IActionResult> GetSizes()
        {

            try
            {
                var sizes = _mapper.Map<List<SizeModel>>(await _pictureService.GetSizes());

                return Ok(new BackEndResponse<List<SizeModel>>(200, sizes));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackEndResponse<object>(500, ex.Message));
            }
        }
    }
}
