using EvaluatorMVC.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace EvaluatorMVC.Controllers
{
    [Authorize]
    public class HomeController : Controller
    {
        [AllowAnonymous]
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult UploadAPKFile()
        {

            return View();
        }


        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult SelectCriteria()
        {
            var Model = GetGuidelines();

            HashSet<string> categoriesList = new HashSet<string>();
            HashSet<string> domainList = new HashSet<string>();

            foreach (var item in Model)
            {
                var categories = item.Categories.Split(',');
                var domains = item.Domain.Split(',');

                foreach (var cat in categories)
                {
                    categoriesList.Add(cat.Trim());
                    
                }

                foreach (var domain in domains)
                {
                    domainList.Add(domain.Trim());
                }
            }

            ViewBag.CatgoryList = categoriesList.ToList();
            ViewBag.DomainList = domainList.ToList();




            return View();
        }


        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }


        public ActionResult ValidationPlan()
        {
            var Model = GetGuidelines();
            ViewBag.TestCasesList = GetTestCaseList();


            return View("ValidationPlan",Model);
        }

        private IEnumerable<string> GetTestCaseList()
        {
            var testCases = new List<string>();
            foreach (string file in Directory.EnumerateFiles(Server.MapPath("~/App_Data/TestCases/"), "*.xaml"))
            {
                testCases.Add(file.Substring(file.LastIndexOf('\\')+1));
            }
            return testCases;

        }

        private List<GuidelineViewModel> GetGuidelines()
        {
            var Model = new List<GuidelineViewModel>();
            string testPlanStr = System.IO.File.ReadAllText(Path.Combine(Server.MapPath("~/App_Data/"), "TestPlan.json"));
            Model = JsonConvert.DeserializeObject<List<GuidelineViewModel>>(testPlanStr);
            return Model;
        }


        private void SaveTestPlan(List<GuidelineViewModel> guildeines)
        {
            try
            {
                var testPlanStr = JsonConvert.SerializeObject(guildeines);
                System.IO.File.WriteAllText(Path.Combine(Server.MapPath("~/App_Data/"), "TestPlan.json"), testPlanStr);
            }
            catch (System.Exception ex)
            {
                throw ex;
            }
        }

        public ActionResult DeleteValidationCase(int GuidelineID, int TestCaseID)
        {

            var Model = GetGuidelines();

            var TestCase = Model.FirstOrDefault(x => x.Id == GuidelineID).TestCases.FirstOrDefault(x => x.Id == TestCaseID);

            Model.FirstOrDefault(x => x.Id == GuidelineID).TestCases.Remove(TestCase);

            SaveTestPlan(Model);

            ViewBag.TestCasesList = GetTestCaseList();

            return View("ValidationPlan", Model);

        }


        [HttpPost]
        public ActionResult Upload(HttpPostedFileBase file)
        {

            if (file != null && file.ContentLength > 0)
            {
                try
                {
                    System.IO.Directory.Delete(Path.Combine(Server.MapPath("~/APKDecompile/"), "apkcode"), true);
                }
                catch (System.Exception) { }

                //var fileName = Path.GetFileName(file.FileName);
                var path = Path.Combine(Server.MapPath("~/APKDecompile/"), "app-debug.apk");
                file.SaveAs(path);


                //string strCmdText;
                //var strFile = Path.Combine(Server.MapPath("~/APKDecompile/"), "DecompileAPK.exe");
                //code for apk decompile

                //1. Decompile apk file from apktool
                string basePath = @"D:\E\";//AppDomain.CurrentDomain.BaseDirectory;
                //1. Decompile apk file from apktool
                string strCmdText;
                strCmdText = "/c java -jar \"" + basePath + "apktool\\apktool.jar\" d \"" + basePath + "app-debug.apk\" -s  -o \"" + basePath + "apkcode\"";
                System.Diagnostics.Process Process = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
                Process.WaitForExit();

                //convert .dex to .jar
                strCmdText = "/c " + basePath + "dex2jar\\d2j-dex2jar \"" + basePath + "apkcode\\classes.dex\" -o \"" + basePath + "apkcode\\classes.jar\"";
                System.Diagnostics.Process Process1 = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
                Process1.WaitForExit();

                //decompile jar file
                strCmdText = "/c java -jar \"" + basePath + "jd-core\\jd-core-java-1.2.jar\" \"" + basePath + "apkcode\\classes.jar\" \"" + basePath + "apkcode\\src\"";
                System.Diagnostics.Process Process2 = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
                Process2.WaitForExit();

                ViewBag.FileUploadStatus = "File decompiled successfully";
            }
            else
            {
                ViewBag.FileUploadStatus = "File decompile failed";
            }

            return View("UploadAPKFile");
        }


        public ActionResult AddNewGudiline()
        {
            return View();
        }

        public ActionResult EditGuideline(int id)
        {
            var Model = GetGuidelines();

            var model = Model.FirstOrDefault(x => x.Id == id);

            return View(model);
        }

        [HttpPost]
        public ActionResult EditGuidelineData(GuidelineViewModel data)
        {
            data.TestCases = new List<TestCase>();
            var Model = GetGuidelines();

            Model.FirstOrDefault(x => x.Id == data.Id).Name=data.Name;
            Model.FirstOrDefault(x => x.Id == data.Id).Description = data.Description;
            Model.FirstOrDefault(x => x.Id == data.Id).Categories = data.Categories;
            Model.FirstOrDefault(x => x.Id == data.Id).Domain = data.Domain;
            Model.FirstOrDefault(x => x.Id == data.Id).Execute = data.Execute;

            SaveTestPlan(Model);
            ViewBag.TestCasesList = GetTestCaseList();

            return View("ValidationPlan", Model);
        }


        [HttpPost]
        public ActionResult CreateNewGuideline(GuidelineViewModel data)
        {
            data.TestCases = new List<TestCase>();
            var Model = GetGuidelines();
            data.Id = Model.Count + 1;
            Model.Add(data);
            SaveTestPlan(Model);

            ViewBag.TestCasesList = GetTestCaseList();

            return View("ValidationPlan", Model);
        }

        [HttpPost]
        public ActionResult AddValidationCase()
        {
            var validationCase = Request["ValidationCase"];
            var GuidelineId = Convert.ToInt32(Request["GuidelineId"]);

            var Model = GetGuidelines();

            Model.FirstOrDefault(x => x.Id == GuidelineId).TestCases.Add(
                new TestCase() { Id = Model.FirstOrDefault(x => x.Id == GuidelineId).TestCases.Count + 1, 
                    Name = validationCase }
            );

            SaveTestPlan(Model);

            ViewBag.TestCasesList = GetTestCaseList();

            return View("ValidationPlan", Model);
        }


        public ActionResult DeleteGuideline(int GuidelineID)
        {
            var Model = GetGuidelines();

            Model.Remove(Model.FirstOrDefault(x => x.Id == GuidelineID));

            SaveTestPlan(Model);
            ViewBag.TestCasesList = GetTestCaseList();

            return View("ValidationPlan", Model);
        }

        [HttpPost]
        public ActionResult ExecuteTestPlan(IEnumerable<GuidelineViewModel> data)
        {
            ActivityLibrary.LuceneService.InitialiseLucene();
            ActivityLibrary.LuceneService.BuildIndex();

            //string searchText = "setOnClickListener(new View.OnClickListener() \n" + "public void onClick(View paramAnonymousView)";

            //var responseData = ActivityLibrary.LuceneService.Search(searchText);

            var guildeines = GetGuidelines();
            ActivityLibrary.Results.Clear();
            var resultHTMLResponse = "";
            int i = 1;

         string _results =
        @"<p>
	            <span style='font-size: 11pt;'><span style='color: #008080;'><strong>Guideline# {9}:  </strong></span>{0}</span><br />
	            <span style='font-size: 9pt;'><span style='color: #008080;'><strong>Usability Category:</strong></span> {1}</span><br />
	            <span style='color: #808080; font-size: 9pt;'>{2}</span><br /><br /><span style='color: #ff0000;'><strong>{3}</strong> Failed Matches</span> | <span style='color: #339966;'>{4} Success Matches</span> | Success Rate {5} % |
	            <span style='text-decoration: underline;'><span style='color: #0000ff; text-decoration: underline;'><a href='javascript:void(0)' onclick='toggelDetailedResults(""detailedResults{6}"")'>View Results</a></span></span>
                    <div id='detailedResults{7}' style='display:none;'>
                        {8}
                    </div>
                </p>

            <hr />";

             foreach (var guildeine in guildeines)
             {
                 int passed = 0;
                 int failed = 0;
                 double passedRate = 0;

                 var testResultsPassed = "";
                 var testResultsFailed = "";
                 //execute each test case inside the test plan
                 foreach (var testcase in guildeine.TestCases)
                 {
                     System.Activities.Activity workflow = System.Activities.XamlIntegration.ActivityXamlServices.Load(Path.Combine(Server.MapPath("~/App_Data/TestCases/"), testcase.Name));
                     var result = System.Activities.WorkflowInvoker.Invoke(workflow);
                     var results = ActivityLibrary.Results.Get();

                     foreach (var res in results)
                     {
                         passed = res.IsPassed ? passed + 1 : passed;
                         failed = res.IsPassed ? failed : failed + 1;

                         if (res.IsPassed)
                             testResultsPassed += "<li> <span style='color: #339966; font-size: 11pt;'>Passed : " + res.ResponseStr + " </span> </li>";
                         else
                             testResultsFailed += "<li> <span style='color: #ff0000; font-size: 11pt;'> Failed : " + res.ResponseStr + " </span> </li>";
                     }
                 }

                 passedRate = (Convert.ToDouble(passed) / Convert.ToDouble(passed + failed)) * 100;

                 resultHTMLResponse += String.Format(_results, guildeine.Name, guildeine.Categories, guildeine.Description, failed, passed, passedRate.ToString("##.##"), i, i, "<ul>" + testResultsFailed + testResultsPassed + "</ul>", i);
                 i++;
             }


            //System.Activities.Activity workflow = System.Activities.XamlIntegration.ActivityXamlServices.Load(Path.Combine(Server.MapPath("~/App_Data/TestCases/"), "CheckFaceBookLogin.xaml"));
            //var result = System.Activities.WorkflowInvoker.Invoke(workflow);
            //var results = ActivityLibrary.Results.Get();

            ViewBag.Results = resultHTMLResponse;

            return View("Results");
        }


        [HttpGet]
        public JsonResult SetExecuteForGuideline(int id, bool value)
        {

            var Model = GetGuidelines();

            Model.FirstOrDefault(x => x.Id == id).Execute = value;

            SaveTestPlan(Model);

            return Json(true);
        }

    }
}