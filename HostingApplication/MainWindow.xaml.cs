using System.Windows;

namespace HostingApplication
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        // private WorkflowDesigner wd;

        public MainWindow()
        {
            InitializeComponent();
        }

        private void NewTestCase_Click(object sender, RoutedEventArgs e)
        {
            TestCaseDesigner _TCDWindow = new TestCaseDesigner();
            _TCDWindow.Show();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            TestCaseExecuter _TCEWindow = new TestCaseExecuter();
            _TCEWindow.Show();
        }

        //private void RegisterMetadata()
        //{
        //    DesignerMetadata dm = new DesignerMetadata();
        //    dm.Register();
        //}

        //private void AddDesigner()
        //{
        //    //Create an instance of WorkflowDesigner class.
        //    this.wd = new WorkflowDesigner();

        //    //Place the designer canvas in the middle column of the grid.
        //    Grid.SetColumn(this.wd.View, 1);

        //    ActivityBuilder activityBuilderType = new ActivityBuilder();
        //    activityBuilderType.Name = "Activity Builder";
        //    activityBuilderType.Implementation = new Sequence()
        //    {
        //        DisplayName = "Default Template",
        //        Activities =
        //         {
        //                new WriteLine()
        //                  {
        //                        Text = "Workflow Rehosted Designer"
        //                  }
        //         }
        //    };

        //    //Load a new Sequence as default.
        //    this.wd.Load(activityBuilderType);

        //    //Add the designer canvas to the grid.
        //    grid1.Children.Add(this.wd.View);

        //    this.wd.Context.Services.GetService<DesignerView>().WorkflowShellBarItemVisibility
        //       = ShellBarItemVisibility.All;

        //}

        //private ToolboxControl GetToolboxControl()
        //{
        //    // Create the ToolBoxControl.
        //    ToolboxControl ctrl = new ToolboxControl();

        //    // Create a category.
        //    ToolboxCategory category = new ToolboxCategory("category1");

        //    // Create Toolbox items.
        //    ToolboxItemWrapper tool1 =
        //        new ToolboxItemWrapper("System.Activities.Statements.Assign",
        //        typeof(Assign).Assembly.FullName, null, "Assign");

        //    ToolboxItemWrapper tool2 = new ToolboxItemWrapper("System.Activities.Statements.Sequence",
        //        typeof(Sequence).Assembly.FullName, null, "Sequence");

        //    ToolboxItemWrapper tool3 = new ToolboxItemWrapper("System.Activities.Statements.WriteLine",
        //        typeof(Sequence).Assembly.FullName, null, "WriteLine");

        //    ToolboxItemWrapper tool4 = new ToolboxItemWrapper("ActivityLibrary.CodeActivity1",
        //        typeof(ActivityLibrary.CodeActivity1).Assembly.FullName, null, "CodeActivit1");

        //    ToolboxItemWrapper tool5 = new ToolboxItemWrapper("ActivityLibrary.Activity1",
        //       typeof(ActivityLibrary.Activity1).Assembly.FullName, null, "Activity1");

        //    // Add the Toolbox items to the category.
        //    category.Add(tool1);
        //    category.Add(tool2);
        //    category.Add(tool3);
        //    category.Add(tool4);
        //    category.Add(tool5);

        //    // Add the category to the ToolBox control.
        //    ctrl.Categories.Add(category);
        //    return ctrl;
        //}

        //private void AddToolBox()
        //{
        //    ToolboxControl tc = GetToolboxControl();
        //    Grid.SetColumn(tc, 0);
        //    grid1.Children.Add(tc);
        //}

        //private void AddPropertyInspector()
        //{
        //    Grid.SetColumn(wd.PropertyInspectorView, 2);
        //    grid1.Children.Add(wd.PropertyInspectorView);
        //}
    }
}