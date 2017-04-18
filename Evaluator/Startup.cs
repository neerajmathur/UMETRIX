using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(Evaluator.Startup))]
namespace Evaluator
{
    public partial class Startup {
        public void Configuration(IAppBuilder app) {
            ConfigureAuth(app);
        }
    }
}
