using System.Text.RegularExpressions;

namespace ModpackAssistant.Service
{
    internal class StringService
    {
        internal static string ConvertToDirName(string input)
        {
            string result = Regex.Replace(input, @"[^a-zA-Z0-9\s]", string.Empty);

            result = result.Replace(' ', '_');

            return result;
        }
    }
}