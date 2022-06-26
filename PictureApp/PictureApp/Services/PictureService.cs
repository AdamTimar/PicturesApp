using Microsoft.EntityFrameworkCore;
using PictureApp.DataAccesLayer;
using PictureApp.DataAccesLayer.Models;
using PictureApp.Services.ServiceResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public class PictureService : IPictureService
    {
        private readonly Context _context;
        public PictureService(Context context)
        {
            _context = context;
        }
        public async Task<PictureServiceResponses> AddPicture(PictureEntity Picture, int typeId)
        {
            if (Picture == null)
                return PictureServiceResponses.NULLPARAM;

            if (await _context.PictureContents.FirstOrDefaultAsync(pc => pc.Id == Picture.ContentTypeId) == null)
                return PictureServiceResponses.CONTENTTYPENOTFOUND;

            var PictureToCheckNameExistance = await GetPictureByName(Picture.Name);
            if (PictureToCheckNameExistance != null)
                return PictureServiceResponses.PICTURENAMEALREADYEXISTS;


            _context.Pictures.Add(Picture);
            Picture.ContentTypeId = typeId;

           
            try
            {
                await _context.SaveChangesAsync();
                return PictureServiceResponses.SUCCESS;
            }

            catch (Exception)
            {
                return PictureServiceResponses.EXCEPTION;
            }
        }

        public async Task<PictureServiceResponses> DeletePictureById(int id)
        {
            var result = await GetPictureById(id);

            if (result == null)
                return PictureServiceResponses.NOTFOUND;

            
            var reviewsAboutPicture = await _context.Reviews.Where(r => r.PictureId == id).ToListAsync();

            var discount = await _context.Discounts.FirstOrDefaultAsync(d => d.PictureId == id);
            if (discount != null)
                _context.Discounts.Remove(discount);

            foreach (var r in reviewsAboutPicture)
            {
                _context.Reviews.Remove(r);
            }

            foreach (var o in _context.Orders)
            {
                if(o.PictureId == id)
                    _context.Orders.Remove(o);
            }

            _context.Pictures.Remove(result);

            try
            {
                await _context.SaveChangesAsync();

                return PictureServiceResponses.SUCCESS;
            }

            catch
            {
                return PictureServiceResponses.EXCEPTION;
            }
        }

        public async Task<PictureEntity> GetPictureById(int id)
        {
            return await _context.Pictures.AsNoTracking().FirstOrDefaultAsync(p => p.Id == id);
        }

        public async Task<PictureWithContentEntity> GetPictureWithContentById(int id)
        {
            var pic = await _context.Pictures
                .Where(p => p.Id == id)
                .Join(_context.PictureContents, pi => pi.ContentTypeId, c => c.Id, (pi, c) 
                => new PictureWithContentEntity { Content = c.Name, Id = pi.Id, ImageUrl = pi.ImageUrl, Name = pi.Name })
                .FirstOrDefaultAsync();

            var res = await _context.Discounts.FirstOrDefaultAsync(d => d.PictureId == id);

            if(res != null)
            {
                pic.Discount = res.Percentage;
            }
            else
                pic.Discount = 0;

            float sum = 0;
            int ct = 0;
            foreach (var r in _context.Reviews)
            {
                if (r.PictureId == id)
                {
                    sum += r.QualityLevel;
                    ct++;
                }
            }

            if (ct == 0)
                pic.Average = 0;

            else
                pic.Average = sum / ct;

            return pic;
        }

        public async Task<PictureEntity> GetPictureByName(string name)
        {
            return await _context.Pictures.AsNoTracking().FirstOrDefaultAsync(p => string.Equals(p.Name, name));
        }

        public async Task<List<PictureWithContentEntity>> GetPictures()

        {
            var list = await _context.Pictures
                .Join(_context.PictureContents, pi => pi.ContentTypeId, c => c.Id, (pi, c)
                => new PictureWithContentEntity {Content = c.Name, Id = pi.Id, ImageUrl = pi.ImageUrl, Name = pi.Name})
                .ToListAsync();

            foreach (var p in list)
            {
                var res = await _context.Discounts.FirstOrDefaultAsync(d => d.PictureId == p.Id);
                if (res != null)
                    p.Discount = res.Percentage;
                else
                    p.Discount = 0;

                float sum = 0;
                int ct = 0;
                foreach(var r in _context.Reviews)
                {      
                    if(r.PictureId == p.Id)
                    {
                        sum += r.QualityLevel;
                        ct++;
                    }
                }

                if (ct == 0)
                    p.Average = 0;

                else
                    p.Average = sum / ct;
            }

            return list;
        }

        
        public async Task<PictureServiceResponses> UpdatePicture(PictureEntity Picture, int contentTypeId)
        {
            if (Picture == null)
                return PictureServiceResponses.NULLPARAM;

            var result = await GetPictureById(Picture.Id);

            if (result == null)
                return PictureServiceResponses.NOTFOUND;

            var PictureType = await _context.PictureContents.FirstOrDefaultAsync(pC => pC.Id == contentTypeId);

            if (PictureType == null)
                return PictureServiceResponses.CONTENTTYPENOTFOUND;

            var PictureToCheckNameExistance = await GetPictureByName(Picture.Name);
            if (PictureToCheckNameExistance != null && PictureToCheckNameExistance.Id != Picture.Id)
                return PictureServiceResponses.PICTURENAMEALREADYEXISTS;

            _context.Entry(Picture).State = EntityState.Modified;


            try
            {
                await _context.SaveChangesAsync();
                return PictureServiceResponses.SUCCESS;
            }
            catch (Exception)
            {
                return PictureServiceResponses.EXCEPTION;
            }

        }

        public async Task<List<PictureContentTypeEntity>> GetContentTypes()
        {
            return await _context.PictureContents.ToListAsync();
        }

        public async Task<ReviewServiceResponses> DeleteReviewById(int id)
        {
            var result = await _context.Reviews.FirstOrDefaultAsync(r => r.Id == id);
            if (result == null)
                return ReviewServiceResponses.REVIEWNOTFOUND;

            _context.Remove(result);
            try
            {
                await _context.SaveChangesAsync();
                return ReviewServiceResponses.SUCCESS;
            }
            catch
            {
                return ReviewServiceResponses.EXCEPTION;
            }
        }

        public async Task<List<SizeEntity>> GetSizes()
        {
            return await _context.Sizes.ToListAsync();
        }
    }
            
}

