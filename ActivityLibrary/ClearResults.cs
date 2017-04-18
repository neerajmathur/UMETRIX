using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;

namespace ActivityLibrary
{

    public class ClearResults : CodeActivity
    {

        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override void Execute(CodeActivityContext context)
        {
            // Obtain the runtime value of the Text input argument
            Results.Clear();
        }
    }
}
