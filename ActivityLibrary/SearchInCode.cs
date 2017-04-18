using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;

namespace ActivityLibrary
{

    public sealed class SearchInCode : CodeActivity<IList<string>>
    {
        // Define an activity input argument of type string
        public InArgument<string> SearchText { get; set; }

        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override IList<string> Execute(CodeActivityContext context)
        {
            // Obtain the runtime value of the Text input argument
            string searchText = context.GetValue(this.SearchText);

           var results  = LuceneService.Search(searchText);

           return results;
            

        }
    }
}
