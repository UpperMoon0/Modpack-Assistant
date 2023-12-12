using CommunityToolkit.Mvvm.Input;
using ModpackAssistant.Service;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace ModpackAssistant.ViewModel.RightScreen
{
    internal class CreatePrjScrViewModel : BaseViewModel
    {
        private string prjName;
        private string prjPath;
        private string fullPath;

        public CreatePrjScrViewModel()
        {
            PrjName = "New Project";
            PrjPath = Environment.GetFolderPath(Environment.SpecialFolder.Desktop);
            UpdateFullPath();

            CreatePrjCmd = new RelayCommand(CreateProject, CanCreateProject);
        }

        public string PrjName
        {
            get => prjName;
            set
            {
                prjName = value;
                OnPropertyChanged(nameof(PrjName));
                UpdateFullPath();
            }
        }

        public string PrjPath
        {
            get => prjPath;
            set
            {
                prjPath = value;
                OnPropertyChanged(nameof(PrjPath));
                UpdateFullPath();
            }
        }

        public string FullPath
        {
            get => fullPath;
            set
            {
                fullPath = value;
                OnPropertyChanged(nameof(FullPath));
            }
        }

        public ICommand CreatePrjCmd { get; private set; }

        private void UpdateFullPath()
        {
            try 
            { 
                FullPath = Path.Combine(PrjPath, StringService.ConvertToDirName(PrjName)); 
            }
            catch(ArgumentNullException)  
            {
                FullPath = "NA";
            }
        }

        private void CreateProject()
        {
            string[] subFolders = { "Bountiful", "KubeJS", "ZenScript" };

            foreach (var folder in subFolders)
            {
                FileService.CreateFolderIfNotExists(Path.Combine(FullPath, folder));
            }
        }

        private bool CanCreateProject()
        {
            return FileService.IsValidPath(PrjPath);
        }
    }
}
