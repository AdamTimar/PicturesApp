using PictureApp.DataAccesLayer.Models;
using PictureApp.Models;
using PictureApp.Services.ServiceResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PictureApp.Services
{
    public interface IDiscountService
    {
        public Task<DiscountServiceResponses> AddDiscount(DiscountEntity discount);
        public Task<DiscountServiceResponses> UpdateDiscount(DiscountEntity discount);

        public Task<DiscountServiceResponses> DeleteDiscountById(int id);

        public Task<List<DiscountWithImageUrlAndPictureNameEntity>> GetDiscounts();
        public Task<DiscountWithImageUrlAndPictureNameEntity> GetDiscountById(int id);
    }
}
