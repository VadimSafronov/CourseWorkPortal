using System;
using Microsoft.EntityFrameworkCore;

namespace CourseWorksPortal.Models
{
    public class DatabaseContext : DbContext
        
    {
        public DbSet<User> Users { get; set; }
        public DbSet<CourseWork> CourseWorks { get; set; }
        public DbSet<FavoriteWork> FavoriteWorks { get; set; }
       
        protected override void OnModelCreating(ModelBuilder builder)
        {
            //builder.Entity<User>().HasAlternateKey(u => u.Username);
        }
        public DatabaseContext(DbContextOptions<DatabaseContext> options): base(options)
        {
            Database.EnsureCreated();
        }
    }
}