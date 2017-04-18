using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace EvaluatorMVC.Controllers
{
    public class DeveloperController : Controller
    {
        //
        // GET: /Developer/
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult UploadValidationCase()
        {

            return View();
        }

        [HttpPost]
        public ActionResult UploadValidationCase(HttpPostedFileBase file)
        {
            try
            {
                if (file != null && file.ContentLength > 0)
                {
                    var path = Path.Combine(Server.MapPath("~/App_Data/TestCases/"), file.FileName);
                    file.SaveAs(path);
                    ViewBag.FileUploadSuccess = "True";
                } 
            }
            catch (Exception ex)
            {
                ViewBag.FileUploadFailed = "False";
                ViewBag.ErrorMsg = ex.Message;
            }

            return View();

        }
        

	}
}