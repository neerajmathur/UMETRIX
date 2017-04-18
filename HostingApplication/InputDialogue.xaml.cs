using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace HostingApplication
{
    /// <summary>
    /// Interaction logic for InputDialogue.xaml
    /// </summary>
    public partial class InputDialogue : Window
    {
        public InputDialogue()
        {
            InitializeComponent();
        }


        public string ResponseText
        {
            get { return ResponseTextBox.Text; }
            set { ResponseTextBox.Text = value; }
        }

        private void OKButton_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }

        private void CancleButton_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = false;
        }
    }
}
