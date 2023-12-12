using ModpackAssistant.ViewModel.RightScreen;

namespace ModpackAssistant.ViewModel
{
    internal class MainWindowViewModel : BaseViewModel
    {
        private BaseViewModel _rightScreen = new HomeScrViewModel();

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
        }
    }
}
