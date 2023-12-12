using System.Windows.Controls;

namespace ModpackAssistant.View.RightScreen
{
    /// <summary>
    /// Interaction logic for HomeScr.xaml
    /// </summary>
    public partial class HomeScr : UserControl
    {
        public HomeScr()
        {
            InitializeComponent();
            DataContext = new ViewModel.RightScreen.HomeScrViewModel();
        }
    }
}
