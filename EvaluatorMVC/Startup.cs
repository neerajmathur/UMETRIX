using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(EvaluatorMVC.Startup))]
namespace EvaluatorMVC
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
