using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace solonng_launcher
{
    public class Statut
    {
        public Statut(int aCode, string aLabel)
        {
            Code = aCode;
            Label = aLabel;
        }
        public int Code { get; }
        public string Label { get; }

        public bool Equal(Statut aStatut)
        {
            return aStatut.Code == this.Code;
        }
    }
}
