using ModpackAssistant.ViewModel.RightScreen;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ModpackAssistant.ViewModel
{
    internal class MainWindowViewModel : BaseViewModel
    {
        private BaseViewModel _rightScreen;

        public BaseViewModel RightScreen
        {
            get { return _rightScreen; }
            set
            {
                _rightScreen = value;
                OnPropertyChanged(nameof(RightScreen));
            }
        }

        public MainWindowViewModel()
        {
            RightScreen = new HomeScrViewModel();
        }
    }
}
