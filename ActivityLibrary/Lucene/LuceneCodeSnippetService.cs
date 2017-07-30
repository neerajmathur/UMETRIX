using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Lucene.Net.Analysis;
using Lucene.Net.Analysis.Standard;
using Lucene.Net.Documents;
using Lucene.Net.Index;
using Lucene.Net.QueryParsers;
using Lucene.Net.Search;
using Lucene.Net.Store;
using System.Xml.Linq;
using System.Net;

namespace ActivityLibrary.Lucene
{
    public class LuceneCodeSnippetService
    {
        private static Analyzer analyzer = new StandardAnalyzer();
        private static Directory luceneIndexDirectory;
        private static IndexWriter writer;
        private static Task BuildIndexTask;
        private static string CodeSnippetRepoUrl= "https://raw.githubusercontent.com/neerajmathur/UMETRIX/master/Live%20Templates/UMETRIX.xml";

        static LuceneCodeSnippetService()
        {
            InitialiseLucene();
            BuildIndexTask = Task.Run(() => BuildIndex());
        }

        private static XDocument ReadCodeSnippets()
        {
            byte[] data;
            using (WebClient webClient = new WebClient())
                data = webClient.DownloadData(CodeSnippetRepoUrl);

            string str = Encoding.GetEncoding("Windows-1252").GetString(data);
            return XDocument.Parse(str);

        }

        private static void InitialiseLucene()
        {
            var indexPath = AppDomain.CurrentDomain.BaseDirectory + @"\CodeSnippetIndex";

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

        private static void BuildIndex()
        {
            XDocument xd = ReadCodeSnippets();

            var templates = xd.Descendants("templateSet").Elements();

            foreach (var template in templates)
            {
                string data = template.Attribute("description").Value;

                System.Text.RegularExpressions.Regex rgx = new System.Text.RegularExpressions.Regex("[^a-zA-Z0-9]");
                data = rgx.Replace(data, " ");

                Document doc = new Document();
                doc.Add(new Field("CodeSnippetName", template.Attribute("name").Value, Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.Add(new Field("Guideline", data, Field.Store.YES, Field.Index.TOKENIZED));
                doc.Add(new Field("CodeSnippet", template.Attribute("value").Value, Field.Store.YES, Field.Index.TOKENIZED));
                writer.AddDocument(doc);
            }
            writer.Optimize();
            writer.Flush();
            writer.Close();
            luceneIndexDirectory.Close();
        }

        public static IDictionary<string, string> Query(string searchTerm)
        {
            BuildIndexTask.Wait();

            System.Text.RegularExpressions.Regex rgx = new System.Text.RegularExpressions.Regex("[^a-zA-Z0-9]");
            searchTerm = rgx.Replace(searchTerm, " ");

            IndexSearcher searcher = new IndexSearcher(luceneIndexDirectory);
            QueryParser parser = new QueryParser("Guideline", analyzer);

            Query query = parser.Parse(searchTerm.ToLower());
            Hits hitsFound = searcher.Search(query);

            IDictionary<string, string> results = new Dictionary<string, string>();

            for (int i = 0; i < hitsFound.Length(); i++)
            {
                Document doc = hitsFound.Doc(i);
                float score = hitsFound.Score(i);
                string CodeSnippetName = doc.Get("CodeSnippetName");
                string CodeSnippet = doc.Get("CodeSnippet");

                if (score > 0.6)
                    results.Add(CodeSnippetName, CodeSnippet);

            }

            searcher.Close();

            return results;

        }
    }
}
