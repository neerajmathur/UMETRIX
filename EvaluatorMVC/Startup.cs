using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(UmetrixWeb.Startup))]
namespace UmetrixWeb
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
