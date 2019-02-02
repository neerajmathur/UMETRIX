using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace UmetrixWeb.Models
{
    public class GuidelineViewModel
    {
        public int Id { get; set; }

        [Required]
        public bool Execute { get; set; }

        [Required]
        public string Name { get; set; }

        [Required]  
        public string Description { get; set; }

        [Required]
        public string Categories { get; set; }

        [Required]
        public string Domain { get; set; }


        public List<TestCase> TestCases { get; set; }
    }

    public class TestCase
    {
        public int Id { get; set; }

        [Required]
        public string Name { get; set; }
    }
}