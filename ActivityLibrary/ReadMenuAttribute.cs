using System;
using System.Activities;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Xml;

namespace ActivityLibrary
{
    public class ReadMenuAttribute : CodeActivity<Dictionary<String, IList<String>>>
    {

        // Define an activity input argument of type string
        [Category("Input")]
        [RequiredArgument]
        public InArgument<string> XPath { get; set; }

        [Category("Input")]
        [RequiredArgument]
        public InArgument<string> AttributesToRead { get; set; }

        // If your activity returns a value, derive from CodeActivity<TResult>
        // and return the value from the Execute method.
        protected override Dictionary<String, IList<String>> Execute(CodeActivityContext context)
        {
            Dictionary<String, IList<String>> _attributeList = new Dictionary<String, IList<String>>();

            try
            {
                string _XPath = context.GetValue(this.XPath);
                string[] _attributesToRead = context.GetValue(this.AttributesToRead).ToString().Split('|');

                var layouts = Directory.GetFiles(AppDomain.CurrentDomain.BaseDirectory + @"\APKDecompile\apkcode\res\menu", "*.xml");

                foreach (var layoutFile in layouts)
                {
                    XmlDocument doc = new XmlDocument();
                    doc.Load(layoutFile);
                    var nsmgr = new XmlNamespaceManager(doc.NameTable);
                    nsmgr.AddNamespace("android", "http://schemas.android.com/apk/res/android");
                    nsmgr.AddNamespace("app", "http://schemas.android.com/apk/res-auto");
                    nsmgr.AddNamespace("tools", "http://schemas.android.com/tools");
                    XmlNodeList result = doc.SelectNodes(_XPath, nsmgr);

                    IList<String> attributes = new List<String>();

                    foreach (XmlNode item in result)
                    {
                        foreach (var _attributeToRead in _attributesToRead)
                        {
                            var attribute = item.Attributes[_attributeToRead].Value;
                            attribute = ClearAttribute(attribute);
                            attributes.Add(_attributeToRead + " |" + attribute);
                        }

                    }

                    if(attributes.Count>0)
                    _attributeList.Add(layoutFile.Replace(AppDomain.CurrentDomain.BaseDirectory + @"\APKDecompile\apkcode\res\menu", ""), attributes);

                }
            }
            catch (Exception e)
            {
            }



            return _attributeList;
        }

        private string ClearAttribute(string attr)
        {
            return attr.Substring(attr.IndexOf("/") + 1);
        }

    }
}
