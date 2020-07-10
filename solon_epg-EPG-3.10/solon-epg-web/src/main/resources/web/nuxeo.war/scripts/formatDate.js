 			window.Date.prototype.formatDate = function(format) {
  	  			var MONTH_NAMES = ['janvier','février','mars','avril','mai','juin','juillet','août','septembre','octobre','novembre','décembre'],
   			       DAY_NAMES = ['Dimanche','Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi'],
   			       LZ = function(x) {return(x<0||x>9?"":"0")+x},
   			       date = this,
   				    format = format + "",
   				    result = "",
   				    i_format = 0,
   				    c = "",
   				    token = "",
   				    y = date.getYear() + "",
   				    M = date.getMonth() + 1,
   				    d = date.getDate(),
   				    E = date.getDay(),
   				    H = date.getHours(),
   				    m = date.getMinutes(),
   				    s = date.getSeconds(),   
   				    yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k,
   				    value = new Object();
   				// Convert real date parts into formatted versions
   				if (y.length < 4) {y=""+(y-0+1900);}
   				value["y"]=""+y;
   				value["yyyy"]=y;
   				value["yy"]=y.substr(2,4);
   				value["M"]=M;
   				value["MM"]=LZ(M);
   				value["MMM"]=MONTH_NAMES[M-1];
   				value["NNN"]=MONTH_NAMES[M-1].substr(0,3);
   				value["N"]=MONTH_NAMES[M-1].substr(0,1);
   				value["d"]=d;
   				value["dd"]=LZ(d);
   				value["e"]=DAY_NAMES[E].substr(0,1);
   				value["ee"]=DAY_NAMES[E].substr(0,2);
   				value["E"]=DAY_NAMES[E].substr(0,3);
   				value["EE"]=DAY_NAMES[E];
   				value["H"]=H;
   				value["HH"]=LZ(H);
   				if (H==0){value["h"]=12;}
   				else if (H>12){value["h"]=H-12;}
   				else {value["h"]=H;}
   				value["hh"]=LZ(value["h"]);
   				if (H>11){value["K"]=H-12;} else {value["K"]=H;}
   				value["k"]=H+1;
   				value["KK"]=LZ(value["K"]);
   				value["kk"]=LZ(value["k"]);
   				if (H > 11) { value["a"]="PM"; }
   				else { value["a"]="AM"; }
   				value["m"]=m;
   				value["mm"]=LZ(m);
   				value["s"]=s;
   				value["ss"]=LZ(s);
   				while (i_format < format.length) {
   					c=format.charAt(i_format);
   					token="";
   					while ((format.charAt(i_format)==c) && (i_format < format.length)) {
   						token += format.charAt(i_format++);
   						}
   					if (value[token] != null) { result=result + value[token]; }
   					else { result=result + token; }
   				}
   				return result;
   			}