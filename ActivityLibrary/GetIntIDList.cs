using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Activities;
using System.ComponentModel;
using System.Xml;

namespace ActivityLibrary
{

    public sealed class GetIntIDList : CodeActivity<IList<String>>
    {
        // Define an activity input argument of type string
        [RequiredArgument]
        [Category("Input")]
        public InArgument<IList<String>> ControlIdList { get; set; }

        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override IList<String> Execute(CodeActivityContext context)
        {
            IList<String> controlIDIntList = new List<String>();

            XmlDocument doc = new XmlDocument();
            doc.Load(@"APKDecompile\apkcode\res\values\public.xml");

            
            XmlNodeList result = doc.SelectNodes(@"//public");
            var controlIdList = context.GetValue(this.ControlIdList);

            foreach (var ControlID in controlIdList)
	        {
		        foreach (XmlNode item in result)
                {
                    if (item.Attributes["name"].Value == ControlID)
                    {
                        var controlIdHex = item.Attributes["id"].Value;
                        int controlIdInt = int.Parse(controlIdHex, System.Globalization.NumberStyles.HexNumber);
                        controlIDIntList.Add(controlIdInt.ToString());
                    }
                }
            }

            return controlIDIntList;
        }
    }
}
