using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;
using System.Xml;
using System.IO;

namespace ActivityLibrary
{

	public sealed class GetControlIntIds : CodeActivity
	{
		// Define an activity input argument of type string
        public InArgument<Dictionary<String, IList<String>>> ControlAttributes { get; set; }


		protected override void Execute(CodeActivityContext context)
		{
			// Obtain the runtime value of the Text input argument
            //Dictionary<String, IList<String>> ControlAttributes = context.GetValue(this.ControlAttributes);



            XmlDataDocument xmldoc = new XmlDataDocument(); 
            XmlNodeList xmlnode; 
            int i = 0; 
            string str = null;
            FileStream fs = new FileStream(@"APKDecompile\apkcode\res\values\public.xml", FileMode.Open, FileAccess.Read); 
            xmldoc.Load(fs);
            xmlnode = xmldoc.GetElementsByTagName("public"); 
            for (i = 0; i <= xmlnode.Count - 1; i++) 
            {

                str = xmlnode[i].Attributes[1].Value; 
              
            }

		}
	}
}
