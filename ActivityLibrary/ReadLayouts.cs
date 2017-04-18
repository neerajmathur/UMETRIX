using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;
using System.IO;

namespace ActivityLibrary
{

    public sealed class ReadLayouts : CodeActivity<IList<String>>
    {
        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override IList<String> Execute(CodeActivityContext context)
        {

            IList<String> layouts = Directory.GetFiles(@"APKDecompile\apkcode\res\layout", "*.xml");
            
            return layouts;
        }
    }
}
