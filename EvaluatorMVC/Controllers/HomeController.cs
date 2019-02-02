using UmetrixWeb.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace UmetrixWeb.Controllers
{
    [Authorize]
    public class HomeController : Controller
    {

        public static string ResultsInPrintFormat;
        public static string AppName ="Vlab";
        public static string ApkFileName = "Vlab.apk";

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
            AppName = Request["AppName"];
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
                ApkFileName = file.FileName;
                try
                {
                    System.IO.Directory.Delete(Path.Combine(Server.MapPath("~/APKDecompile/"), "apkcode"), true);
                }
                catch (System.Exception) { }

                //var fileName = Path.GetFileName(file.FileName);
                var path = Path.Combine(Server.MapPath("~/APKDecompile/"), "app-debug.apk");
                file.SaveAs(path);

                //System.Threading.Thread.Sleep(5000);

                //string strCmdText;
                //var strFile = Path.Combine(Server.MapPath("~/APKDecompile/"), "DecompileAPK.exe");
                //code for apk decompile

                //1. Decompile apk file from apktool
                string basePath = System.Configuration.ConfigurationManager.AppSettings["DecompilerPath"]; //@"D:\E\";//AppDomain.CurrentDomain.BaseDirectory;
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
            try
            {
                ActivityLibrary.LuceneService.InitialiseLucene();
                ActivityLibrary.LuceneService.BuildIndex();

                int TotalPassed = 0;
                int TotalFailed = 0;

                //string searchText = "setOnClickListener(new View.OnClickListener() \n" + "public void onClick(View paramAnonymousView)";

                //var responseData = ActivityLibrary.LuceneService.Search(searchText);

                var guidelines = GetGuidelines();
                ActivityLibrary.Results.Clear();
                var resultHTMLResponse = "";
                ResultsInPrintFormat = "";
                int i = 1;

                string _results =
               @"<p>
	            <span style='font-size: 11pt;'><span style='color: #008080;'><strong>Guideline# {9}:  </strong></span>{0}</span><br />
	            <span style='font-size: 9pt;'><span style='color: #008080;'><strong>Usability Category:</strong></span> {1}</span><br />
	            <span style='color: #808080; font-size: 9pt;'>{2}</span><br /><br /><span style='color: #ff0000;'><strong>{3}</strong> Violated </span> | <span style='color: #339966;'>{4} Success Matches</span> | Success Rate {5} % |
	            <span style='text-decoration: underline;'><span style='color: #0000ff; text-decoration: underline;'><a href='javascript:void(0)' onclick='toggelDetailedResults(""detailedResults{6}"")'>View Results</a></span></span>
                <span style='text-decoration: underline;'><span style='color: #0000ff; text-decoration: underline;'><a href='javascript:void(0)' onclick='toggelDetailedResults(""codeSnippet{6}"")'>View Code Snippets</a></span></span>
                    <div id='detailedResults{7}' style='display:none;'>
                        {8}
                    </div>
                    <div id='codeSnippet{7}' style='display:none;align=left'>
                        <pre class='prettyprint'>
                             {10}
                        </pre>
                    </div>
                </p>

            <hr />";

                string _results_print =
           @"<p>
	            <span style='font-size: 11pt;'><span style='color: #008080;'><strong>Guideline# {8}:  </strong></span>{0}</span><br />
	            <span style='font-size: 9pt;'><span style='color: #008080;'><strong>Usability Category:</strong></span> {1}</span><br />
	            <span style='color: #808080; font-size: 9pt;'>{2}</span><br /><br /><span style='color: #ff0000;'><strong>{3}</strong> Violated </span> | <span style='color: #339966;'>{4} Success Matches</span> | Success Rate {5} % |
	            <span style='text-decoration: underline;'><span style='color: #0000ff; text-decoration: underline;'></span></span>
                    <div id='detailedResults{6}' >
                        {7}
                    </div>
                </p>

            <hr />";

                string output = "";

                foreach (var guideline in guidelines)
                {
                    if (guideline.Execute)
                    {
                        int passed = 0;
                        int failed = 0;
                        double passedRate = 0;

                        var testResultsPassed = "";
                        var testResultsFailed = "";
                        //execute each test case inside the test plan
                        foreach (var testcase in guideline.TestCases)
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
                                    testResultsFailed += "<li> <span style='color: #ff0000; font-size: 11pt;'> Violated : " + res.ResponseStr + " </span> </li>";
                            }
                        }

                        TotalPassed += passed;
                        TotalFailed += failed;

                        if (passed > 0)
                            output += passed + "P";

                        if (failed > 0)
                            output += failed + "F";

                        output += ",";

                       var CodeSnippet = GetCodeSnippetRecomendations(guideline);


                        passedRate = (Convert.ToDouble(passed) / Convert.ToDouble(passed + failed)) * 100;
                        string passedRateStr = passedRate > 0 ? passedRate.ToString("##.##") : passedRate.ToString();
                        resultHTMLResponse += String.Format(_results, guideline.Name, guideline.Categories, guideline.Description, failed, passed, passedRateStr, i, i, "<ul>" + testResultsFailed + testResultsPassed + "</ul>", i, CodeSnippet);
                        ResultsInPrintFormat += String.Format(_results_print, guideline.Name, guideline.Categories, guideline.Description, failed, passed, passedRateStr, i, "<ul>" + testResultsFailed + testResultsPassed + "</ul>", i);
                        i++;
                    }
                }

                output += TotalPassed.ToString() + "," + TotalFailed.ToString();


                

                resultHTMLResponse = string.Format("<h3>Total Matched: {0} , Total Violated: {1} , Conformance Rate: {2:N2}%</h3>", TotalPassed, TotalFailed, (Convert.ToDouble(TotalPassed) / (TotalPassed + TotalFailed) * 100)) + resultHTMLResponse;
                ResultsInPrintFormat = " <br/>" + string.Format("<h3>Total Matched: {0} , Total Violated: {1} , Conformance Rate: {2:N2}%</h3>", TotalPassed, TotalFailed, (Convert.ToDouble(TotalPassed) / (TotalPassed + TotalFailed) * 100)) + ResultsInPrintFormat;


                //System.Activities.Activity workflow = System.Activities.XamlIntegration.ActivityXamlServices.Load(Path.Combine(Server.MapPath("~/App_Data/TestCases/"), "CheckFaceBookLogin.xaml"));
                //var result = System.Activities.WorkflowInvoker.Invoke(workflow);
                //var results = ActivityLibrary.Results.Get();

                ViewBag.Results = resultHTMLResponse;
            }
            catch(Exception e)
            {
                ViewBag.Results = e.Message + "<br/>" + e.StackTrace;

            }
            return View("Results");
        }

        private static string GetCodeSnippetRecomendations(GuidelineViewModel guideline)
        {
            var codeSnippets = ActivityLibrary.Lucene.LuceneCodeSnippetService.Query(guideline.Name);
            var codeSnippet=string.Empty;

            codeSnippet += "<strong>Download Android Studio Live Templates from Umetrix Github Repo <a target='_blank' href='https://github.com/neerajmathur/UMETRIX/tree/master/Live%20Templates' >Click to Download</a></strong><br/><br/>";

            if (codeSnippets.Count == 0)
                codeSnippet += "Sorry, No code snippet recommentations were found in code snippet repository";

            foreach (var snippet in codeSnippets)
            {
                codeSnippet += "<br/>Live Template Name: " + snippet.Key;
                codeSnippet += "<br/>Snippet: <br/>" + HttpUtility.HtmlEncode(snippet.Value);
                codeSnippet += "<hr/>";
            }
            return codeSnippet;
       }

        [HttpGet]
        public ActionResult PrintReport()
        {
            ViewBag.ApkFileName = ApkFileName;
            ViewBag.AppName = AppName;
            ViewBag.Results = ResultsInPrintFormat;
            return View("ResultsInPrintFormat");
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