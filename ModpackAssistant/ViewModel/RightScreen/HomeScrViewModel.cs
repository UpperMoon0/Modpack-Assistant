using CommunityToolkit.Mvvm.Input;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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
