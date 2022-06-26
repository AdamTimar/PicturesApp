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
    public class DiscountService : IDiscountService
    {
        private readonly Context _context;
        public DiscountService(Context context)
        {
            _context = context;
        }
        public async Task<DiscountServiceResponses> AddDiscount(DiscountEntity discount)
        {
            if (await _context.Discounts.FirstOrDefaultAsync(d => d.PictureId == discount.PictureId) != null)
                return DiscountServiceResponses.DISCOUNTALREADYEXISTS;

            _context.Discounts.Add(discount);

            try
            {
                await _context.SaveChangesAsync();
                return DiscountServiceResponses.SUCCESS;
            }

            catch
            {
                return DiscountServiceResponses.EXCEPTION;
            }
        }

        public async Task<DiscountServiceResponses> DeleteDiscountById(int id)
        {
            var result = await _context.Discounts.FirstOrDefaultAsync(d => d.Id == id);

            if (result == null)
                return DiscountServiceResponses.NOTFOUND;

            _context.Discounts.Remove(result);

            try
            {
                await _context.SaveChangesAsync();
                return DiscountServiceResponses.SUCCESS;
            }

            catch
            {
                return DiscountServiceResponses.EXCEPTION;
            }
        }

        public async Task<DiscountWithImageUrlAndPictureNameEntity> GetDiscountById(int id)
        {
            var list = _context.Discounts.Where(d => d.Id == id);
            return await list.Join(_context.Pictures, d => d.PictureId, p => p.Id, (d, p) => new { a = d, b = p })
                .Join(_context.PictureContents, c => c.b.ContentTypeId, pc => pc.Id, (c, pc) => new DiscountWithImageUrlAndPictureNameEntity { Content = pc.Name, ImageUrl = c.b.ImageUrl, Percentage = c.a.Percentage, PictureName = c.b.Name }).FirstOrDefaultAsync();
        }

        public async Task<List<DiscountWithImageUrlAndPictureNameEntity>> GetDiscounts()
        {
            return await _context.Discounts.Join(_context.Pictures, d => d.PictureId, p => p.Id, (d, p) => new { a = d, b = p })
                .Join(_context.PictureContents, c => c.b.ContentTypeId, pc => pc.Id, (c, pc) => new DiscountWithImageUrlAndPictureNameEntity { Content = pc.Name, ImageUrl = c.b.ImageUrl, Percentage = c.a.Percentage, PictureName = c.b.Name }).ToListAsync();
        }

        public async Task<DiscountServiceResponses> UpdateDiscount(DiscountEntity discount)
        {
            var result = await GetDiscountById(discount.Id);

            if (result == null)
                return DiscountServiceResponses.NOTFOUND;


            _context.Entry(discount).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
                return DiscountServiceResponses.SUCCESS;
            }

            catch
            {
                return DiscountServiceResponses.EXCEPTION;
            }
        }
    }
}
