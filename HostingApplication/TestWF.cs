using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;

namespace HostingApplication
{

    public sealed class TestWF : CodeActivity
    {
        // Define an activity input argument of type string
        public InArgument<string> Text { get; set; }
        public OutArgument<IList<string>> Response { get; set; }  
        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override void Execute(CodeActivityContext context)
        {
            // Obtain the runtime value of the Text input argument


            //var response = context.GetValue(this.Response);

            IList<string> results = new List<string>();

            results.Add("1,Results is passed");
            results.Add("0,Failed");

            Response.Set(context,results);
            
        }
    }
}
