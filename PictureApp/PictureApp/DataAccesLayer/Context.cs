using PictureApp.DataAccesLayer.Models;
using Microsoft.EntityFrameworkCore;

namespace PictureApp.DataAccesLayer
{

    public class Context : DbContext{

        public DbSet<UserEntity> Users { get; set; }
        public DbSet<InvalidatedUserEntity> InvalidatedUsers { get; set; }
        public DbSet<DiscountEntity> Discounts { get; set; }
        public DbSet<PictureEntity> Pictures { get; set; }
        public DbSet<PictureContentTypeEntity> PictureContents { get; set; }
        public DbSet<SizeEntity> Sizes { get; set; }
        public DbSet<OrderEntity> Orders { get; set; }
        public DbSet<ReviewEntity> Reviews { get; set; }
        public DbSet<UserWhoHasBirthdayEntity> UsersWhoHaveBirthday { get; set; }
        public DbSet<UserWhoChangesPasswordEntity> UsersWhoChangePassword { get; set; }


        public Context(DbContextOptions<Context> options) : base(options)
        {
        }

        protected Context(DbContextOptions contextOptions)
       : base(contextOptions)
        {
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<UserEntity>()
                .HasIndex(u => u.Email)
                .IsUnique();

            modelBuilder.Entity<InvalidatedUserEntity>()
                .HasIndex(u => u.Email)
                .IsUnique();

            modelBuilder.Entity<InvalidatedUserEntity>()
                .HasIndex(u => u.ValidationKey)
                .IsUnique();

            modelBuilder.Entity<PictureEntity>()
                .HasIndex(u => u.Name)
                .IsUnique();

            modelBuilder.Entity<SizeEntity>()
                .HasIndex(sp => sp.Size)
                .IsUnique();

          

            modelBuilder.Entity<UserWhoHasBirthdayEntity>()
                 .HasIndex(u => u.UserId)
                 .IsUnique();

            modelBuilder.Entity<UserWhoChangesPasswordEntity>()
                .HasIndex(u => u.UserId)
                .IsUnique();

            modelBuilder.Entity<UserWhoChangesPasswordEntity>()
                .HasIndex(u => u.ChangePasswordKey)
                .IsUnique();

            modelBuilder.Entity<DiscountEntity>()
               .HasIndex(d => d.PictureId)
               .IsUnique();

            modelBuilder.Entity<UserWhoChangesPasswordEntity>()
                .HasIndex(u => u.ChangePasswordKey)
                .IsUnique();


            modelBuilder.Entity<ReviewEntity>()
                    .HasOne(r => r.User)
                    .WithMany(u => u.ReviewsFromUser)
                    .HasForeignKey(r => r.UserId)
                    .OnDelete(DeleteBehavior.Restrict);


            modelBuilder.Entity<ReviewEntity>()
                    .HasOne(r => r.Picture)
                    .WithMany(p => p.ReviewsAboutPicture)
                    .HasForeignKey(r => r.PictureId)
                    .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<PictureContentTypeEntity>()
                    .HasIndex(pt => pt.Name)
                    .IsUnique();

          

        }
    }
}
