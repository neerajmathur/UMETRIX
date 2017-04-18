using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;

namespace ActivityLibrary
{

    public sealed class WriteResponse : CodeActivity
    {
        // Define an activity input argument of type string

        public InArgument<bool> IsPassed { get; set; }

        public InArgument<string> ResponseText { get; set; }
        

        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override void Execute(CodeActivityContext context)
        {
            // Obtain the runtime value of the Text input argument
            var responseText = context.GetValue(this.ResponseText);
            var isPassed = context.GetValue(this.IsPassed);
            Results.Add(isPassed, responseText);

        }
    }
}
