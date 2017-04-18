using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DecompileAPK
{
    class Program
    {
        static void Main(string[] args)
        {


            //code for apk decompile
            string basePath = @"D:\E\";//AppDomain.CurrentDomain.BaseDirectory;
            //1. Decompile apk file from apktool
            string strCmdText;
            strCmdText = "/c java -jar \"" + basePath + "apktool\\apktool.jar\" d \"" + basePath + "app-debug.apk\" -s  -o \"" + basePath + "apkcode\"";
            System.Diagnostics.Process Process = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
            Process.WaitForExit();

            //convert .dex to .jar
            strCmdText = "/c " + basePath + "dex2jar\\d2j-dex2jar \"" + basePath + "apkcode\\classes.dex\" -o \"" + basePath + "apkcode\\classes.jar\"";
            ExecuteCommand(strCmdText);
            //System.Diagnostics.Process Process1 = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
            //Process1.WaitForExit();
            //var code =Process1.ExitCode;

            //decompile jar file
            strCmdText = "/c java -jar \"" + basePath + "jd-core\\jd-core-java-1.2.jar\" \"" + basePath + "apkcode\\classes.jar\" \"" + basePath + "apkcode\\src\"";
            System.Diagnostics.Process Process2 = System.Diagnostics.Process.Start("CMD.exe", strCmdText);
            Process2.WaitForExit();


        }


        static void ExecuteCommand(string command)
        {
            int exitCode;
            ProcessStartInfo processInfo;
            Process process;

            processInfo = new ProcessStartInfo("cmd.exe", command);
            processInfo.CreateNoWindow = true;
            processInfo.UseShellExecute = false;
            // *** Redirect the output ***
            processInfo.RedirectStandardError = true;
            processInfo.RedirectStandardOutput = true;

            process = Process.Start(processInfo);
            process.WaitForExit();

            // *** Read the streams ***
            // Warning: This approach can lead to deadlocks, see Edit #2
            string output = process.StandardOutput.ReadToEnd();
            string error = process.StandardError.ReadToEnd();

            exitCode = process.ExitCode;

            Console.WriteLine("output>>" + (String.IsNullOrEmpty(output) ? "(none)" : output));
            Console.WriteLine("error>>" + (String.IsNullOrEmpty(error) ? "(none)" : error));
            Console.WriteLine("ExitCode: " + exitCode.ToString(), "ExecuteCommand");
            process.Close();
        }
    }
}
