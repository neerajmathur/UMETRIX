using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ActivityLibrary
{
    public class Results
    {

        static Results()
        {
            res = new List<Response>();
        }

        private static IList<Response> res { get; set; }

        public static void Clear()
        {
            res.Clear();
        }
        public static void Add(bool IsPassed, string ResultStr)
        {
            res.Add(new Response() { IsPassed = IsPassed, ResponseStr = ResultStr });
        }

        public static IList<Response> Get()
        {
            return res;
        }

    }

    public class Response
    {

        public bool IsPassed { get; set; }

        public string ResponseStr { get; set; }

    }
}
