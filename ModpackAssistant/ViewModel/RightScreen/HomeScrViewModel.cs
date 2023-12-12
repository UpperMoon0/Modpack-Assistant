using CommunityToolkit.Mvvm.Input;
using System.Windows;
using System.Windows.Input;

namespace ModpackAssistant.ViewModel.RightScreen
{
    internal class HomeScrViewModel : BaseViewModel
    {
        public HomeScrViewModel()
        {
            CreatePrjCmd = new RelayCommand(OpenCreatePrjView);
        }

        public ICommand CreatePrjCmd { get; private set; }

        private void OpenCreatePrjView()
        {
            MessageBox.Show("CreatePrjCmd");
        }
    }
}
