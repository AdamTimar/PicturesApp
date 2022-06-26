using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public interface IContentService
    {
        public Task<PictureContentTypeEntity> GetContentById(int id);
    }
}
