using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Lucene.Net.Analysis;
using Lucene.Net.Analysis.Standard;
using Lucene.Net.Documents;
using Lucene.Net.Index;
using Lucene.Net.QueryParsers;
using Lucene.Net.Search;
using Lucene.Net.Store;

namespace ActivityLibrary
{
    public class LuceneService
    {

        private static Analyzer analyzer = new StandardAnalyzer();
        private static Directory luceneIndexDirectory;
        private static IndexWriter writer;
        private static string indexPath = @"c:\temp\LuceneIndex";

         static LuceneService()
        {
            //InitialiseLucene();
        }

         public static void InitialiseLucene()
        {
            if (System.IO.Directory.Exists(indexPath))
            {
                try
                {
                    System.IO.Directory.Delete(indexPath, true);
                }
                catch (Exception ex) { }
            }
            Analyzer analyzer = new StandardAnalyzer();
            luceneIndexDirectory = FSDirectory.GetDirectory(indexPath);
            writer = new IndexWriter(luceneIndexDirectory, analyzer, true);
        }

        public static void BuildIndex()
        {
            var sDir = AppDomain.CurrentDomain.BaseDirectory + @"\APKDecompile\apkcode\src";
            var Files=DirSearch(sDir);

            foreach (var file in Files)
            {
                string data=System.IO.File.ReadAllText(file);

                System.Text.RegularExpressions.Regex rgx = new System.Text.RegularExpressions.Regex("[^a-zA-Z0-9]");
                data = rgx.Replace(data, " ");

                Document doc = new Document();
                doc.Add(new Field("FileName", file.Substring(file.LastIndexOf('\\') + 1), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.Add(new Field("Data", data, Field.Store.YES, Field.Index.TOKENIZED));
                writer.AddDocument(doc);
            }
            writer.Optimize();
            writer.Flush();
            writer.Close();
            luceneIndexDirectory.Close();
        }

        public static IList<string> Search(string searchTerm)
        {

            System.Text.RegularExpressions.Regex rgx = new System.Text.RegularExpressions.Regex("[^a-zA-Z0-9]");
            searchTerm = rgx.Replace(searchTerm, " ");

            IndexSearcher searcher = new IndexSearcher(luceneIndexDirectory);
            QueryParser parser = new QueryParser("Data", analyzer);

            Query query = parser.Parse(searchTerm.ToLower());
            Hits hitsFound = searcher.Search(query);

            IList<string> results = new List<string>();

            for (int i = 0; i < hitsFound.Length(); i++)
            { 
                Document doc = hitsFound.Doc(i);
                float score = hitsFound.Score(i);
                string fileName = doc.Get("FileName");
                
                if(score>0.6)
                results.Add(doc.Get("FileName"));

            }

            searcher.Close();
            
            return results;

        }
        private static List<String> DirSearch(string sDir)
        {
            

            List<String> files = new List<String>();
            try
            {
                foreach (string f in System.IO.Directory.GetFiles(sDir))
                {
                    files.Add(f);
                }
                foreach (string d in System.IO.Directory.GetDirectories(sDir))
                {
                    if (!d.Contains("APKDecompile\\apkcode\\src\\android"))
                    files.AddRange(DirSearch(d));
                }
            }
            catch (System.Exception excpt)
            {
                //MessageBox.Show(excpt.Message);
            }

            return files;
        }

    }
}