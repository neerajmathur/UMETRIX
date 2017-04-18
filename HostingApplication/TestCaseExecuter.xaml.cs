using Newtonsoft.Json;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Media;
using MahApps.Metro.Controls;
using System;
using System.Activities.XamlIntegration;

namespace HostingApplication
{
    /// <summary>
    /// Interaction logic for TestCaseExecuter.xaml
    /// </summary>
    public partial class TestCaseExecuter : Window
    {
        //https://www.codeproject.com/Questions/710119/How-to-Bind-wpf-DataGridComboBoxColumn

        public TestCaseExecuter()
        {
            DataViewActivity = CreateActivityList();
            _dataView = new ListCollectionView(new ObservableCollection<Guideline>(CreateData()));

            InitializeComponent();

            DataContext = this;
        }

        private void btnDecompile_Click(object sender, RoutedEventArgs e)
        {
            var ofd = new Microsoft.Win32.OpenFileDialog()
            {
                Filter = "Android APK (*.apk)|*.apk|All File (*.*)|*.*"
            };

            if (ofd.ShowDialog() ?? false)
            {
                txtFilePath.Text = ofd.FileNames[0];

                try
                {
                    System.IO.Directory.Delete(@"APKDEcompile\apkcode", true);
                }
                catch (System.Exception) { }

                //code for apk decompile

                //1. Decompile apk file from apktool
                string strCmdText;
                strCmdText = "/c java -jar APKDecompile\\apktool\\apktool.jar d \"" + txtFilePath.Text + "\" -s  -o APKDecompile\\apkcode";
                System.Diagnostics.Process Process = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
                Process.WaitForExit();

                //convert .dex to .jar
                strCmdText = "/c APKDecompile\\dex2jar\\d2j-dex2jar APKDecompile\\apkcode\\classes.dex -o APKDecompile\\apkcode\\classes.jar";
                Process = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
                Process.WaitForExit();

                //decompile jar file
                strCmdText = "/c java -jar APKDecompile\\jd-core\\jd-core-java-1.2.jar APKDecompile\\apkcode\\classes.jar APKDecompile\\apkcode\\src";
                Process = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
                Process.WaitForExit();

                MessageBox.Show("Decompilation Finished Successfully!");
            }
        }

        private ObservableCollection<string> CreateActivityList()
        {
            var testCases = new ObservableCollection<string>();
            foreach (string file in Directory.EnumerateFiles("TestCases", "*.xaml"))
            {
                testCases.Add(file.Replace("TestCases\\",""));
            }
            return testCases; 

        }

        private void ReadTestPlan()
        {

           
        
        }

        private List<Guideline> CreateData()
        {
            string testPlanStr = System.IO.File.ReadAllText("testPlan.json");
            List<Guideline> data = JsonConvert.DeserializeObject<List<Guideline>>(testPlanStr);
            return data;

            #region "NotRequired"
            //return new List<Guideline>()
            //  {
            //    new Guideline
            //    {
            //      Execute = false,
            //      Name = "Davide",
            //      Description = "Seddio",
            //     // Nationality="CH",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Check Facebook login"},
            //        new TestCase {Name = "Check Facebook login"},
            //        new TestCase {Name = "Check Google login"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = true,
            //      Name = "Luca",
            //      Description = "Bianchi",
            //     // Nationality = "CH",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Fiat"},
            //        new TestCase {Name = "Ford"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = true,
            //      Name = "Marco",
            //      Description = "Milani",
            //      //Nationality = "CH",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Suzuki"},
            //        new TestCase {Name = "Subaru"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = true,
            //      Name = "Alfred",
            //      Description = "Zurcher",
            //     // Nationality = "CH",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Mercedes"},
            //        new TestCase {Name = "Ferrari"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = true,
            //      Name = "Franco",
            //      Description = "Veri",
            //      //Nationality = "IT",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Audi"},
            //        new TestCase {Name = "BMW"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = true,
            //      Name = "Luigi",
            //      Description = "Mulinelli",
            //      //Nationality = "IT",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "VW"},
            //        new TestCase {Name = "Jaguar"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = true,
            //      Name = "Carlo",
            //      Description = "Zanfrini",
            //     // Nationality = "IT",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Autobianchi"},
            //        new TestCase {Name = "Lancia"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = false,
            //      Name = "Alex",
            //      Description = "Calderoli",
            //      //Nationality = "DE",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Alfa Romeo"},
            //        new TestCase {Name = "Renault"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = false,
            //      Name = "Andrea",
            //      Description = "Laranguate",
            //      //Nationality = "FR",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Peugeot"},
            //        new TestCase {Name = "Citroen"}
            //      }
            //    },
            //    new Guideline
            //    {
            //        Execute = false,
            //      Name = "Oreste",
            //      Description = "Tranquilli",
            //     // Nationality = "EN",
            //      TestCases = new ObservableCollection<TestCase>
            //      {
            //        new TestCase {Name = "Saab"},
            //        new TestCase {Name = "Lamborghini"}
            //      }
            //    }
            //  };
            #endregion
        }

        private ListCollectionView _dataView;
        private ObservableCollection<string> _dataViewActivity;

        public ObservableCollection<string> DataViewActivity
        {
            get { return _dataViewActivity; }
            set { _dataViewActivity = value; }
        }

        public ListCollectionView DataView
        {
            get { return _dataView; }
            set { _dataView = value; }
        }

        private ObservableCollection<TestCase> _testCases;

 

        private void objDatagrid_Loaded(object sender, RoutedEventArgs e)
        {
        }

        public class Guideline
        {
            public bool Execute { get; set; }
            public string Name { get; set; }
            public string Description { get; set; }

            //public string Nationality { get; set; }
            public ObservableCollection<TestCase> TestCases { get; set; }
        }

        public class TestCase
        {
            public string Name { get; set; }
        }

        private void objDatagrid_AddingNewItem(object sender, System.Windows.Controls.AddingNewItemEventArgs e)
        {
            e.NewItem = new Guideline() { Name = "", TestCases = new ObservableCollection<TestCase>() };
        }

        private void objDatagrid_LoadingRowDetails(object sender, DataGridRowDetailsEventArgs e)
        {
            var objInnerDataGrid = e.DetailsElement.FindName("objInnerDatagrid") as DataGrid;
            var cmbActivity = objInnerDataGrid.FindName("cmbActivity") as DataGridComboBoxColumn;
            cmbActivity.ItemsSource = DataViewActivity;
        }


        private void SaveTestPlan()
        {
            try
            {
                List<Guideline> guildeines = GetGuidelines();
                var testPlanStr = JsonConvert.SerializeObject(guildeines);
                System.IO.File.WriteAllText("testPlan.json",testPlanStr);
                MessageBox.Show("Test plan saved successfully!");
            }
            catch (System.Exception ex)
            {
                throw ex;
            }
        }

        private List<Guideline> GetGuidelines()
        {
            List<Guideline> guildeines = new List<Guideline>();

            foreach (var item in _dataView)
            {
                try
                {
                    if (((Guideline)item).Name != null && ((Guideline)item).Name != string.Empty)
                    {
                        guildeines.Add(((Guideline)item));
                    }
                }
                catch (System.Exception) { }
            }
            return guildeines;
        }

        private void btnSaveTestPlan_Click(object sender, RoutedEventArgs e)
        {
            SaveTestPlan();
        }

        private void btnExecuteTestPlan_Click(object sender, RoutedEventArgs e)
        {
            try
            {

                List<Guideline> guildeines = GetGuidelines();
                ActivityLibrary.Results.Clear();
                var resultHTMLResponse="";
                int i=1;
                foreach (var guildeine in guildeines)
                {
                    int passed=0;
                    int failed=0;
                    double passedRate=0;
                    
                    var testResultsPassed="";
                    var testResultsFailed = "";
                    //execute each test case inside the test plan
                    foreach (var testcase in guildeine.TestCases)
                    {
                        System.Activities.Activity workflow = ActivityXamlServices.Load(@"TestCases\" + testcase.Name);
                        var result = System.Activities.WorkflowInvoker.Invoke(workflow);
                        var results = ActivityLibrary.Results.Get();

                        foreach (var res in results)
                        {
                            passed = res.IsPassed ? passed + 1 : passed;
                            failed = res.IsPassed ? failed : failed + 1;
                            
                            if(res.IsPassed)
                                testResultsPassed += "<li> <span style='color: #339966; font-size: 11pt;'>Passed : " + res.ResponseStr + " </span> </li>";
                            else
                                testResultsFailed += "<li> <span style='color: #ff0000; font-size: 11pt;'> Failed : " + res.ResponseStr + " </span> </li>";
                        }
                    }

                    passedRate = (Convert.ToDouble(passed) / Convert.ToDouble(passed + failed)) * 100;

                    resultHTMLResponse += String.Format(_results, guildeine.Name, "Categories", guildeine.Description, failed, passed, passedRate.ToString("##.##"), i,i,  "<ul>" +testResultsFailed + testResultsPassed + "</ul>", i);
                    i++;
                }

                var resultHTMLBody = @"<html>
                                        <title>UMETRIX - Usability Report</title>
                                        <head>
                                        <script>

				                        function toggelDetailedResults(id) {
						                        var x = document.getElementById(id);
						                        if (x.style.display === 'none') {
							                        x.style.display = 'block';
						                        } else {
							                        x.style.display = 'none';
						                        }
					                        }
			                           </script>
                                    </head>
                                    <body style='font-family: Verdana, Geneva, Tahoma, sans-serif;'>"
                                    + "UMETRIX - Usability Report for "
                                    + "APK File :" + txtFilePath.Text + "</br>"
                                    + resultHTMLResponse
                                    + @" 

                                    </body></html>";

                System.IO.File.WriteAllText("Result.html", resultHTMLBody);

                System.Diagnostics.Process.Start("Result.html");

            }
            catch (Exception ex)
            {
                throw ex;
            }
        }


        private string _results =
        @"<p>
	            <span style='font-size: 11pt;'><span style='color: #008080;'><strong>Guideline# {9}:  </strong></span>{0}</span><br />
	            <span style='font-size: 9pt;'><span style='color: #008080;'><strong>Usability Category:</strong></span> {1}</span><br />
	            <span style='color: #808080; font-size: 9pt;'>{2}</span><br /><br /><span style='color: #ff0000;'><strong>{3}</strong> Failed Matches</span> | <span style='color: #339966;'>{4} Success Matches</span> | Success Rate {5} % |
	            <span style='text-decoration: underline;'><span style='color: #0000ff; text-decoration: underline;'><a href='#' onclick='toggelDetailedResults(""detailedResults{6}"")'>View Results</a></span></span>
                    <div id='detailedResults{7}' style='display:none;'>
                        {8}
                    </div>
                </p>

<hr />";

    }
}