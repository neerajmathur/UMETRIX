using System;
using System.Activities;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Xml;

namespace ActivityLibrary
{
    public sealed class ReadManifestAttribute : CodeActivity<IList<String>>
    {
        // Define an activity input argument of type string
        [Category("Input")]
        [RequiredArgument]
        public InArgument<string> XPath { get; set; }

        [Category("Input")]
        [RequiredArgument]
        public InArgument<string> AttributeToRead { get; set; }

        protected override IList<String> Execute(CodeActivityContext context)
        {
            List<String> _attributeList = new List<string>();

            try
            {

                string _XPath = context.GetValue(this.XPath);
                string _attributeToRead = context.GetValue(this.AttributeToRead).ToString();

                XmlDocument doc = new XmlDocument();
                doc.Load(AppDomain.CurrentDomain.BaseDirectory + @"\APKDecompile\apkcode\AndroidManifest.xml");

                var nsmgr = new XmlNamespaceManager(doc.NameTable);
                nsmgr.AddNamespace("android", "http://schemas.android.com/apk/res/android");
                nsmgr.AddNamespace("app", "http://schemas.android.com/apk/res-auto");
                nsmgr.AddNamespace("tools", "http://schemas.android.com/tools");
                XmlNodeList result = doc.SelectNodes(_XPath, nsmgr);

                foreach (XmlNode item in result)
                {
                    var attribute = item.Attributes[_attributeToRead].Value;
                    _attributeList.Add(attribute);
                }

            }
            catch (Exception e)
            {

            }

            return _attributeList;
        }
    }
}
