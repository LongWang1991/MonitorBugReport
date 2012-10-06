
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.Parser;

/**
* @author www.baizeju.com
*/
public class BugSaving {
    private static String ENCODE = "GBK";
    
    ArrayList allBugID= new ArrayList();

    public ArrayList getBugID(BugSaving bs){//get all bug id from web site 
    	ArrayList bugID = new ArrayList();
    	try{          
            //Parser parser = new Parser((HttpURLConnection) (new URL("https://bugzilla.mozilla.org/buglist.cgi?resolution=---;resolution=DUPLICATE;chfieldto=Now;query_format=advanced;chfieldfrom="+bs.getDate())).openConnection());       
            //Parser parser = new Parser((HttpURLConnection) (new URL("https://bugzilla.mozilla.org/buglist.cgi?order=Importance;field0-0-0=cf_crash_signature;resolution=FIXED;chfieldto=Now;query_format=advanced;chfield=cf_crash_signature;chfieldfrom="+bs.getDate()+";type0-0-0=isnotempty")).openConnection());       
            Parser parser = new Parser((HttpURLConnection) (new URL("https://bugzilla.mozilla.org/buglist.cgi?order=Importance;field0-0-0=cf_crash_signature;resolution=FIXED;chfieldto=Now;query_format=advanced;chfield=cf_crash_signature;chfieldfrom="+bs.getDate()+";type0-0-0=isnotempty")).openConnection());       
            
            
            
            NodeFilter filter = new HasAttributeFilter( "name", "id");
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
                  
            Boolean isEqual = false;
            
            if(nodes!=null) {
                for (int i = 0; i < nodes.size(); i++) {
                    Node textnode = (Node) nodes.elementAt(i);
                    String text = textnode.getText();
                    
                    for(int j = 0; j<5; j++){
                  	  
                  	  int index_begin = text.indexOf("\"");
                        text = text.substring(index_begin+1); 
                    }
                    int index_end = text.indexOf("\"");
                    String value = text.substring(0, index_end);
                    
                    if(bugID.size()==0){
                  	  bugID.add(value);
                  	  
                    }else {
                  	  for(int k=0;k<bugID.size();k++){
                       	 if(bugID.get(k).equals(value)){
                       		 isEqual = true;
                       	 }
                       	  
                   }
                   if(!isEqual){
                      	  bugID.add(value);     	  
                   }
                                       
                   }
                    
                }
               
            }

        }
        catch( Exception e ) {     
            e.printStackTrace();
        }

   
		return bugID;
    	
    }
    
    public static String GetURLstr(String strUrl){// get URL String
      
       InputStream in = null;
       OutputStream out = null;

       String strdata = "";

       try

       {

        URL url = new URL(strUrl); // Create URL

        in = url.openStream(); // Open URL stream

        byte[] buffer = new byte[4096];

        int bytes_read;

        while ((bytes_read = in.read(buffer)) != -1)

        {

         String reads = new String(buffer, 0, bytes_read, "UTF-8");

         strdata = strdata + reads;

        }

        in.close();
        return strdata;

       }

 

       catch (Exception e)

       {

        System.err.println(e);

        System.err.println("Usage: java GetURL <URL> [<filename>]");

        return strdata;

       }

    }

    public void saveAsHTML(String name,String bs)throws IOException{//Create html file and save it.
    	 
    	 String filepath="E:\\" + name + ".html";
    	 
    	 FileWriter fw=new FileWriter(filepath);
    	 BufferedWriter bw=new BufferedWriter(fw);
    	 PrintWriter pw=new PrintWriter(bw);
    	 
    	 
    	 pw.print(bs);
    	 
    	 
    	 bw.close();
    	 fw.close();
    	 
    	 
    	 } 
    public void saveAsTXT(String name,String bs)throws IOException{//Create txt file and save it.
   	 
   	 String filepath="E:\\" + name + ".txt";
   	 
   	 FileWriter fw=new FileWriter(filepath);
   	 BufferedWriter bw=new BufferedWriter(fw);
   	 PrintWriter pw=new PrintWriter(bw);
   	 
   	 
   	 pw.print(bs);
   	 
   	 
   	 bw.close();
   	 fw.close();
   	 
   	 
   	 } 

public void saveHtml(String id,BugSaving bs){
    	
    	try {
    		
    		String url = new String("https://bugzilla.mozilla.org/show_bug.cgi?id="+id);
    			Parser parser = new Parser((HttpURLConnection) (new URL(url).openConnection()));
    			NodeFilter filter = new HasAttributeFilter( "class", "uneditable_textarea");
                NodeList nodes = parser.extractAllNodesThatMatch(filter);
                
                if(nodes!=null) {
                    for (int k = 0; k < nodes.size(); k++) {
                        Node textnode = (Node) nodes.elementAt(k);
                       
                        if(!textnode.toPlainTextString().equals("")){
                        	
                        	String htmlString = bs.GetURLstr(url);
                        	bs.saveAsHTML(id, htmlString);
                        	
                        }
                    }
                }     
    		
    		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("saveHtml is wrong");
			e.printStackTrace();
		}
    	//return htmlContent;	
    }

public void saveTXT(String id,BugSaving bs){ //get all the string to save to txt file
	
	String fixedDate = bs.getFixedDate(id);
	String platform = bs.getPlatform(id);
	String[] crashSignatureArray = bs.getCrashSignature(id);
	
	String signature="";
	
	System.out.println("platform:"+platform);
	for(int i=0;i<crashSignatureArray.length;i++){
		signature+="Signature:"+crashSignatureArray[i].replace("\n", "")+"\r\n";
	}
	String fdAndPf = "FixedDate:"+fixedDate+"\r\n"+"Platform:"+platform+"\r\n";
	String txtFile = fdAndPf + signature;
	try {
		bs.saveAsTXT(id, txtFile);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
    
   public String getDate(){
	   
	   Calendar cal=Calendar.getInstance();    
       SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
       return f.format(cal.getTime()); 
	   
   }
   
   public void doSave(){
	   boolean isEqual = false;
	   BugSaving bs = new BugSaving();
	   ArrayList id = bs.getBugID(bs); 
	   for(int i=0;i<id.size();i++){
		   for(int j=0;j<allBugID.size();j++){
			   if (allBugID.get(j).equals(id.get(i))){
				   isEqual = true;
			   }
		   }
		   if(!isEqual){
			   allBugID.add(id.get(i)); 
			   bs.saveHtml((String)id.get(i), bs);
			   bs.saveTXT((String)id.get(i), bs);
			   
		   }
		   
	   }
	   
   }
    
   public String getPlatform(String id){// get Platform information
	   String platform = null;
   	try {
   		
   		String url = new String("https://bugzilla.mozilla.org/show_bug.cgi?id="+id);
   		
   			Parser parser = new Parser((HttpURLConnection) (new URL(url).openConnection())); 
            NodeFilter filter = new HasAttributeFilter("class","field_value");
       
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
               
               if(nodes!=null) {
                   for (int k = 0; k < nodes.size(); k++) {
                       Node textnode = (Node) nodes.elementAt(k);
                       Node parent = textnode.getParent(); 
                       
                       String parentText = parent.toPlainTextString().replace(" ", "").replace("\n", "");                         
                       String[] array = parentText.split(":");
                       
                       for(int i=0;i<array.length;i++){
                    	   if(array[i].equals("Platform")){
                    		   platform = array[i+1];
                    	   }
                       }
                   }
               }   
   		
   		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Getting Platform is wrong");
			e.printStackTrace();
		}
   	//return htmlContent;	
   	return platform;
   }
   
public String[] getCrashSignature(String id){//get CrashSignature information
   	String[] crashSignatureArray = null;
   	try {
   		
   		String url = new String("https://bugzilla.mozilla.org/show_bug.cgi?id="+id);
   			Parser parser = new Parser((HttpURLConnection) (new URL(url).openConnection()));
   			NodeFilter filter = new HasAttributeFilter( "class", "uneditable_textarea");
               NodeList nodes = parser.extractAllNodesThatMatch(filter);
               
               if(nodes!=null) {
                   for (int k = 0; k < nodes.size(); k++) {
                       Node textnode = (Node) nodes.elementAt(k);
                      
                       if(!textnode.toPlainTextString().equals("")){
                       	
                       	String crashSignatureString = textnode.toPlainTextString();
                       	crashSignatureArray = crashSignatureString.split("\n");

                       }
                   }
               }     
   		
   		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Getting CrashSignature is wrong");
			e.printStackTrace();
		}
   	return crashSignatureArray;
   }
   
public String getFixedDate(String id){//get FixedDate information
	   String fixedDate = null;
	try {
		
		String url = new String("https://bugzilla.mozilla.org/show_activity.cgi?id="+id);
		
			Parser parser = new Parser((HttpURLConnection) (new URL(url).openConnection())); 
			NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
    
			NodeList tableList = parser.extractAllNodesThatMatch(tableFilter);
            
            if(tableList!=null) {
                for (int i = 0; i < tableList.size(); i++) {
                	 TableTag table = (TableTag) tableList.elementAt(i);  //get all the table
                     TableRow[] rows = table.getRows();  //put all the rows to an array
                     for (int r=0; r<rows.length; r++) {
                         TableRow tr = rows[r];
                         TableColumn[] td = tr.getColumns();//get all the columns of one row
                         
                         for (int c=0; c<td.length; c++) {
                               if(td[c].toPlainTextString().trim().equals("RESOLVED")){                         	
                               fixedDate=td[1].toPlainTextString().trim();                          
                            }
                         }
                        
                     }
                    
                }
                
            }   
		
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Getting FixedDate is wrong");
			e.printStackTrace();
		}
	
		
	return fixedDate;
}

   public static void main(String[] args) {
        
	   
	   
	   Timer timer = new Timer();
	   timer.schedule(new MyTask(),1000,600000);
	   try {
		   int ch = System.in.read();
		   if(ch-'c'==0){
		   timer.cancel(); //
		  
		   }
		   } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		   }
 
       
    }
   
   
   static class MyTask extends TimerTask{
	   
		@Override
		public void run() {
			System.out.println("=========Every 10 minutes==========");
			BugSaving bs = new BugSaving();
			bs.doSave();	
		}
		   
	   }
}

