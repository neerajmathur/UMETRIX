using Microsoft.Win32;
using System;
using System.Activities;
using System.Activities.Core.Presentation;
using System.Activities.Presentation;
using System.Activities.Presentation.Toolbox;
using System.Activities.Presentation.View;
using System.Activities.Statements;
using System.Windows;
using System.Windows.Controls;

namespace HostingApplication
{
    /// <summary>
    /// Interaction logic for TestCaseDesigner.xaml
    /// </summary>
    public partial class TestCaseDesigner : Window
    {
        private WorkflowDesigner wd;

        public TestCaseDesigner()
        {
            InitializeComponent();

            RegisterMetadata();

            // Add the WFF Designer
            AddDesigner();

            AddToolBox();

            this.AddPropertyInspector();

            //this.wd.Save("Test.xaml");

            //this.wd.Load("Test.xaml");
        }

        private void RegisterMetadata()
        {
            DesignerMetadata dm = new DesignerMetadata();
            dm.Register();
        }

        private void AddDesigner()
        {
            //Create an instance of WorkflowDesigner class.
            this.wd = new WorkflowDesigner();

            //Place the designer canvas in the middle column of the grid.
            Grid.SetColumn(this.wd.View, 1);

            ActivityBuilder activityBuilderType = new ActivityBuilder();
            activityBuilderType.Name = "Activity Builder";
            activityBuilderType.Implementation = new Sequence()
            {
                DisplayName = "Default Template",
                Activities =
                 {
         		        new WriteLine()
                          {
                  		        Text = "Workflow Rehosted Designer"
                          }
                 }
            };

            //Load a new Sequence as default.
            //this.wd.Load(activityBuilderType);
            this.wd.Load("Test.xaml");

            //Add the designer canvas to the grid.
            grid1.Children.Add(this.wd.View);

            this.wd.Context.Services.GetService<DesignerView>().WorkflowShellBarItemVisibility
               = ShellBarItemVisibility.All;
        }

        private ToolboxControl GetToolboxControl()
        {
            ToolboxControl Control = new ToolboxControl();

            var cat = new ToolboxCategory("Android");
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.GetIntIDList), "GetIntIDList"));
            //cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.ReadLayouts), "ReadLayouts"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.ReadLayoutAttribute), "ReadLayoutAttribute"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.ReadMenuAttribute), "ReadMenuAttribute"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.ReadManifestAttribute), "ReadManifestAttribute"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.ClearResults), "ClearResponse"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.WriteResponse), "WriteResponse"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.GetControlIntIds), "GetControlIntIds"));
            cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.SearchInCode), "SearchJavaCode"));
            
           // cat.Add(new ToolboxItemWrapper(typeof(ActivityLibrary.Activity1), "Activity1"));
            Control.Categories.Add(cat);


            cat = new ToolboxCategory("Control Flow");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.DoWhile)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.ForEach<>), "ForEach<T>"));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.If)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Parallel)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.ParallelForEach<>), "ParallelForEach<T>"));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Pick)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.PickBranch)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Sequence)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Switch<>), "Switch<T>"));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.While)));
            Control.Categories.Add(cat);


            cat = new ToolboxCategory("FlowChart");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Flowchart)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.FlowDecision)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.FlowSwitch<>), "FlowSwitch<T>"));
            Control.Categories.Add(cat);

           
            cat = new ToolboxCategory("Runtime");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Persist)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.TerminateWorkflow)));
            Control.Categories.Add(cat);

            cat = new ToolboxCategory("Primitives");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Assign)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Delay)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.InvokeMethod)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.WriteLine)));
            Control.Categories.Add(cat);

            cat = new ToolboxCategory("Transaction");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.CancellationScope)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.CompensableActivity)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Compensate)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Confirm)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.TransactionScope)));
            Control.Categories.Add(cat);

            cat = new ToolboxCategory("Collection");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.AddToCollection<>), "AddToCollection<T>"));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.ClearCollection<>), "ClearCollection<T>"));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.ExistsInCollection<>), "ExistsInCollection<T>"));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.RemoveFromCollection<>), "RemoveFromCollection<T>"));
            Control.Categories.Add(cat);

            cat = new ToolboxCategory("Error Handling");
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Rethrow)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.Throw)));
            cat.Add(new ToolboxItemWrapper(typeof(System.Activities.Statements.TryCatch)));
            Control.Categories.Add(cat);

            return Control;
        }

        private void AddToolBox()
        {
            ToolboxControl tc = GetToolboxControl();
            Grid.SetColumn(tc, 0);
            grid1.Children.Add(tc);
        }

        private void AddPropertyInspector()
        {
            Grid.SetColumn(wd.PropertyInspectorView, 2);
            grid1.Children.Add(wd.PropertyInspectorView);
        }

        private void MenuItemNew_Click(object sender, RoutedEventArgs e)
        {
            grid1.Children.Remove(this.wd.View);

            ////Place the designer canvas in the middle column of the grid.

            ////Create an instance of WorkflowDesigner class.
            this.wd = new WorkflowDesigner();

            ActivityBuilder activityBuilderType = new ActivityBuilder();
            activityBuilderType.Name = "Activity Builder";
            activityBuilderType.Implementation = new Sequence()
            {
                DisplayName = "Default Template",
                Activities =
                 {
         		        new WriteLine()
                          {
                  		        Text = "Workflow Rehosted Designer"
                          }
                 }
            };

            //Load a new Sequence as default.
            this.wd.Load(activityBuilderType);

            Grid.SetColumn(this.wd.View, 1);

            //Add the designer canvas to the grid.
            grid1.Children.Add(this.wd.View);
            this.AddPropertyInspector();
        }

        private void MenuItemSave_Click(object sender, RoutedEventArgs e)
        {
            string subPath = "TestCases"; // your code goes here

            bool exists = System.IO.Directory.Exists(subPath);

            if (!exists)
                System.IO.Directory.CreateDirectory(subPath);

            InputDialogue saveFileDialog = new InputDialogue();
            if (saveFileDialog.ShowDialog() == true)
                this.wd.Save(@"TestCases\" + saveFileDialog.ResponseText + ".xaml");
        }

        private void MenuItemOpen_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openDialogue = new OpenFileDialog();
            openDialogue.InitialDirectory = AppDomain.CurrentDomain.BaseDirectory + @"TestCases";
            openDialogue.Filter = "*Test Case (*.xaml)|*.xaml";
            if (openDialogue.ShowDialog() == true)
            {
                grid1.Children.Remove(this.wd.View);

                ////Place the designer canvas in the middle column of the grid.

                ////Create an instance of WorkflowDesigner class.
                this.wd = new WorkflowDesigner();

                //Load a new Sequence as default.
                this.wd.Load(openDialogue.FileName);

                Grid.SetColumn(this.wd.View, 1);

                //Add the designer canvas to the grid.
                grid1.Children.Add(this.wd.View);

                this.AddPropertyInspector();
            }
        }
    }
}