using Microsoft.EntityFrameworkCore;
using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public class ContentService : IContentService
    {
        private readonly Context _context;
        public ContentService(Context context)
        {
            _context = context;
        }
        public async Task<PictureContentTypeEntity> GetContentById(int id)
        {
            return await _context.PictureContents.AsNoTracking().FirstOrDefaultAsync(c => c.Id == id);
        }
    }
}
