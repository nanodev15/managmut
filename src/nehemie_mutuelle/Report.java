/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;

import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import java.awt.Color;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import static nehemie_mutuelle.EpargneContext_rel.conn;
import static nehemie_mutuelle.EpargneContext_rel.pre;
import static nehemie_mutuelle.TontineUser.conn;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.eclipse.persistence.config.QueryType;

/**
 *
 * @author elommarcarnold
 */
public class Report extends javax.swing.JFrame {
    private ArrayList<String> listEpargne;
    private ArrayList<String> listTontine;
    private Map <String, Date> epmaplist;
    private Map <String, Date> tomaplist;
    private Map <String, String> idlist; // idlisr epae
    private Map <String, String> toidlist;  // idlist tontine
    
    private int IdEpargnant=0;
    private String typeEpargnant="";
    public List<Date> ftcexceptionlist = new ArrayList<>();
    private Connection conn;
    private int nbcolumns;
    private String [] columnName; // = new String [nbcolumns];
    private String [] columnNameTontine; // = new String [nbcolumns];
    private IntializeDate initDate = new IntializeDate ();
    /** Creates new form Report */
    
    
    public static String fmt(double d)
            
{
    DecimalFormat formatter;
    formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    formatter.setDecimalFormatSymbols(symbols);
    if(d == (long) d)
        //  return String.format("%d",(long)d);
        return formatter.format((long)d);
    else
        //return String.format("%s",d);
       return formatter.format(d);
}
    
    
    
    public boolean isTwentyEighthDayOfTheMonth(Date dateToday){
        Calendar c = new GregorianCalendar();
        c.setTime(dateToday);
        return c.get(Calendar.DAY_OF_MONTH)==28;
    }
    
    public class DynamicTableModel extends AbstractTableModel {
/** The column names. */
private String[] columnNames;
/** The data. */
private Object[][] data;

/**
* Instantiates a new dynamic table model.
*
* @param dataset the dataset
*/




public DynamicTableModel(String [] columnNames, Object [][] data) {
super();
this.data = data;
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
     
  for(int i=0; i<data.length; i++) {
        data[i][0]= (formatter.format(data[i][0])).toString(); //"22/22/2019";  
        for(int j=1; j<data[i].length; j++) {
           if(data[i][j] != null && data[i][j].getClass()==Double.class) data[i][j] = fmt(((Double)data[i][j]).doubleValue());
        }
    }
this.columnNames = columnNames; 


}


public DynamicTableModel(Object[] dataset) {
super();
initModel(dataset);

}
/**
* Inits the model.
*
* @param dataset the dataset
*/
private void initModel(Object[] dataset) {
if (dataset.length > 0) {
Object obj = dataset[0];
System.out.println("getclass nameeeeeeeeeeeeeee " + obj.getClass().getName());
Field[] fields = obj.getClass().getDeclaredFields();
//columnNames = new String[fields.length];
data = new String[dataset.length][fields.length];
for (int i = 0; i < fields.length; i++) {
//columnNames[i] = fields[i].getName();
columnNames[i] = columnName[i];
}
for (int j = 0; j < dataset.length; j++) {
try {
for (int k = 0; k < nbcolumns; k++) {
    
    if(k==0) {
           data[j][k] ="22/22/2019";
    } else  {

data[j][k] = BeanUtils.getProperty(dataset[j],
columnNames[k]); }
}

} catch (Exception e) {
e.printStackTrace();
}

}

}
}

/* (non-Javadoc)
* @see javax.swing.table.TableModel#getColumnCount()
*/
public int getColumnCount() {
return columnNames.length;
}
/* (non-Javadoc)
* @see javax.swing.table.TableModel#getRowCount()
*/
public int getRowCount() {
return data.length;
}
/* (non-Javadoc)
* @see javax.swing.table.AbstractTableModel#getColumnName(int)
*/
public String getColumnName(int col) {
return columnNames[col];
}
/* (non-Javadoc)
* @see javax.swing.table.TableModel#getValueAt(int, int)
*/
public Object getValueAt(int row, int col) {
return data[row][col];
}
/**
* Gets the column names.
*
* @return the column names
*/
public String[] getColumnNames() {
return columnNames;
}
/**
* Sets the column names.
*
* @param columnNames the new column names
*/
public void setColumnNames(String[] columnNames) {
this.columnNames = columnNames;
}
/**
* Gets the data.
*
* @return the data
*/
public Object[][] getData() {
return data;
}
/**
* Sets the data.
*
* @param data the new data
*/
public void setData(Object[][] data) {
this.data = data;
}

}

    
    
    
    public Report() throws SQLException {
        listEpargne = new ArrayList<String>();
        listTontine = new ArrayList<String>();
        epmaplist = new HashMap <String, Date>();
        tomaplist = new HashMap <String, Date>();
        idlist = new HashMap <String, String> ();
        toidlist = new HashMap <String, String> ();
       
        fillEpargne();
        fillTontine();
        initComponents();
        Collections.sort(listEpargne);
        Collections.sort(listTontine);
        jComboBox2.setModel(new DefaultComboBoxModel(listEpargne.toArray()));
        demoDateField2.setDate(epmaplist.get((String) jComboBox2.getSelectedItem()));
        setTitle("Rapports de la mutuelle");

        
    }
    
    public void fillExceptionList () throws SQLException {
        conn = Connect.ConnectDb();
        String sql= "SELECT Dateftc FROM Exceptionftc WHERE IdEpargnant ='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"'";
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rst =pst.executeQuery();
        while (rst.next()){
           ftcexceptionlist.add(rst.getDate("Dateftc"));
        }
        
        //Closing
        rst.close();
        pst.close();
        conn.close();
    }
    
    public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    }
    
    
public DynamicReport buildReport(DynamicTableModel model) throws Exception {

/**
* Creates the DynamicReportBuilder and sets the basic options for
* the report
*/
FastReportBuilder drb = new FastReportBuilder();
Style columDetail = new Style();
//columDetail.setBorder(Border.THIN);
Style columDetailWhite = new Style();
columDetailWhite.setBorder(Border.DOTTED());
columDetailWhite.setBackgroundColor(Color.WHITE);
Style columDetailWhiteBold = new Style();
//columDetailWhiteBold.setBorder(Border.THIN);
columDetailWhiteBold.setBackgroundColor(Color.WHITE);
Style titleStyle = new Style();
titleStyle.setFont(new Font(18,Font._FONT_VERDANA,true));
Style numberStyle = new Style();
numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
Style amountStyle = new Style();
amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
amountStyle.setBackgroundColor(Color.cyan);
amountStyle.setTransparency(Transparency.OPAQUE);
Style oddRowStyle = new Style();
//oddRowStyle.setBorder(Border.NO_BORDER);
Color veryLightGrey = new Color(230,230,230);
oddRowStyle.setBackgroundColor(veryLightGrey);oddRowStyle.setTransparency(Transparency.OPAQUE);


// table name column
String[] headings=model.getColumnNames();
for(int i=0;i<headings.length;i++){
String key=headings[i];
AbstractColumn column = ColumnBuilder.getInstance().setColumnProperty(key, String.class.getName())
.setTitle(key).setWidth(new Integer(100))
.setStyle(columDetailWhite).build();
drb.addColumn(column);

}
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
String title ="";
if (jComboBox1.getSelectedIndex()==0)   title = "Relevé Epargne de " +jComboBox2.getSelectedItem() + " du " + formatter.format(demoDateField2.getDate()) +  " au " +  formatter.format(demoDateField1.getDate()); 
else if (jComboBox1.getSelectedIndex()==1) title = "Relevé Tontine de " +jComboBox2.getSelectedItem() + " du" + demoDateField2.getDate() +  " au" + demoDateField1.getDate(); 


drb.setTitle(title)
.setTitleStyle(titleStyle).setTitleHeight(new Integer(30))
.setSubtitleHeight(new Integer(20))
.setDetailHeight(new Integer(15))
.addFirstPageImageBanner(getClass().getResource("/nehemie_mutuelle/logo_MUNE2.jpg").getPath(), new Integer(197), new Integer(60), ImageBanner.ALIGN_LEFT)
//.setLeftMargin(margin)
//.setRightMargin(margin)
//.setTopMargin(margin)
// .setBottomMargin(margin)
.setPrintBackgroundOnOddRows(true)
.setAllowDetailSplit(true)    
.setOddRowBackgroundStyle(oddRowStyle)
.setColumnsPerPage(new Integer(1))
.setUseFullPageWidth(true)
.setColumnSpace(new Integer(5));
DynamicReport dr = drb.build();

return dr;
}


    
public void generateReport() throws Exception {
        Vector v = getEpargne();
        System.out.println("v epargne vaut"+v);
        if (v!=null) System.out.println("v !=null");
        
        
    try {
        Object[][] test = to2DimArray(v);
      
                
        
        DynamicTableModel md = new DynamicTableModel(columnName,test);
        DynamicReport dr = buildReport(md);
        JRDataSource ds = new JRTableModelDataSource(md);
        System.out.println("ms"+ds);
        System.out.println("column name"+columnName[0]);

        if (md == null) System.out.println("md null");
        if (dr == null) System.out.println("dr null");
        if (ds == null) System.out.println("ds null");
        JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
//        JasperViewer.viewReport(jp); 
//        JRPdfExporter exporter = new JRPdfExporter();
//       exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
//     
//	
//	
//            
//            final OutputStream os;
//            os = Files.newOutputStream(Paths.get("./"+(String) jComboBox1.getSelectedItem()+".pdf "));
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
//            exporter.exportReport();
            JasperExportManager.exportReportToPdfFile(jp,
        "Historique Epargne "+ (String) jComboBox2.getSelectedItem()+".pdf"); 
	
    } catch (Exception e) {
        e.printStackTrace();
    }
    

    
}





public void generateReportTontine() throws Exception {
        Vector v = getTontine();
        System.out.println("v epargne vaut"+v);
        if (v!=null) System.out.println("v !=null");
        
        
    try {
        Object[][] test = to2DimArray(v);
      
                
        
        DynamicTableModel md = new DynamicTableModel(columnNameTontine,test);
        DynamicReport dr = buildReport(md);
        JRDataSource ds = new JRTableModelDataSource(md);
      

        JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
//        JasperViewer.viewReport(jp); 
//        JRPdfExporter exporter = new JRPdfExporter();
//       exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
//     
//	
//	
//            
//            final OutputStream os;
//            os = Files.newOutputStream(Paths.get("./"+(String) jComboBox1.getSelectedItem()+".pdf "));
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
//            exporter.exportReport();
//	
        

            JasperExportManager.exportReportToPdfFile(jp,
        "Historique Tontine "+ (String) jComboBox2.getSelectedItem()+".pdf"); 
    } catch (Exception e) {
        e.printStackTrace();
    }
    

    
}


//public Vector getTontine () throws Exception {
//    
//}


public Double soldeinitialtontine() throws Exception {
   String sql0 = "SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND DateRet < '"+new java.sql.Date(demoDateField2.getDate().getTime())+"' AND DateRet  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' "; 
   sql0 = sql0 + "AND idretraits_tontine NOT IN (SELECT idtontine  FROM enrtontinesupp WHERE type ='retrait')";   // changed
   String sql2="SELECT idTontine, DateTontine, JoursTontine, Mise FROM Tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant+ "' ";
   sql2 = sql2+ "AND DateTontine <'"+new java.sql.Date(demoDateField2.getDate().getTime())+"' AND DateTontine >= '"+ new java.sql.Date(IntializeDate.initdate.getTime())+ "' AND idTontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE type ='ajout');";
   conn = Connect.ConnectDb(); 
   PreparedStatement pre, pre3; 
   
    System.out.println("sql2"+sql2);
   pre = conn.prepareStatement(sql0);
   pre3 = conn.prepareStatement(sql2);
   
   ResultSet rs1 = pre.executeQuery();
   ResultSet rs2 = pre3.executeQuery();
   
   Double soldeinit = 0.0;
   
   while (rs1.next()) {
       soldeinit = rs1.getDouble(1);
   }
   
   System.out.println("Solde init retrait tontine"+soldeinit);
   Double sumtemp = 0.0;
   
   while (rs2.next()) {       
        // retrieve values 
            String values = rs2.getString(3);
            HashSet<String> valuesSet = new HashSet<>();
            if (values != null) Collections.addAll(valuesSet , values.split(","));
            Double depot;
            if((valuesSet.size()-1) >= 0){depot = rs2.getDouble(4)*(valuesSet.size()-1);}
            else depot = 0.0;
            sumtemp +=depot; 
   }
   
    System.out.println("Solde init ajout"+sumtemp);
   
            
     return sumtemp - soldeinit;       
    
    
    
    
}
    
    // Changer cette fonction quand il y a des modifications
   
public Vector getTontine() throws Exception {
   
    
    conn = Connect.ConnectDb(); 
   // String date1 = "01/01/2014";
    PreparedStatement pre;
    PreparedStatement pre3;
    PreparedStatement pre21;
    PreparedStatement pre2;
    final String OLD_FORMAT = "dd/MM/yyyy";
    final String NEW_FORMAT = "MMM, YY";
    SimpleDateFormat newFormat = new SimpleDateFormat(OLD_FORMAT);
    DateFormat formatter = new SimpleDateFormat(NEW_FORMAT);
    Date fromDate = demoDateField2.getDate();
    Date toDate = demoDateField1.getDate(); //newFormat.parse(date2);

    Calendar beginCalendar = Calendar.getInstance();
    Calendar finishCalendar = Calendar.getInstance();

    beginCalendar.setTimeInMillis(fromDate.getTime());
    beginCalendar.set (Calendar.DAY_OF_MONTH,1); 
    finishCalendar.setTimeInMillis(toDate.getTime());
    Vector<Vector> TontineVector = new Vector<Vector>();
    
    Date date;
     
    Double solde = soldeinitialtontine();
    
    System.out.println("Le solde initial est "+solde);

   // String date;
    while (beginCalendar.before(finishCalendar)) {
        // add one month to date per loop
        //date = newFormat.format(beginCalendar.getTime());
        date = beginCalendar.getTime(); 
        java.sql.Date sqldate= new java.sql.Date(date.getTime());
        
       
        
        // Find all debit 
       // Find all debit 
      //  pre3=conn.prepareStatement(  // ne pas oublier
                
        String sqlret = "SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(DateRet)=MONTH('"+sqldate+"') AND YEAR(DateRet)=YEAR('"+sqldate+"')";
        
        if (IntializeDate.change) sqlret = sqlret + " AND DateRet  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        
        sqlret= sqlret + "AND idretraits_tontine NOT IN (SELECT idtontine  FROM enrtontinesupp WHERE type ='retrait')";   // changed
        pre3=conn.prepareStatement(sqlret);
        ResultSet rs3 = pre3.executeQuery();
        pre21= conn.prepareStatement("SET @SumMontant := 0;");
        pre21.executeQuery();
        
        // pre2
        String sqlret2="SELECT idretraits_tontine, DateRet, Montant, (@SumMontant := @SumMontant + Montant) AS CumulativeSum FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' ";
        if (IntializeDate.change) sqlret2 = sqlret2 + " AND DateRet  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        sqlret2 = sqlret2 + "AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(DateRet)=MONTH('"+sqldate+"') AND YEAR(DateRet)=YEAR('"+sqldate+"') AND idretraits_tontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE type ='retrait') ORDER by DateRet;";  
        pre2= conn.prepareStatement(sqlret2);  // changed
       
        System.out.println("SELECT idretraits_tontine, DateRet, Montant, (@SumMontant := @SumMontant + Montant) AS CumulativeSum FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(DateRet)=MONTH('"+sqldate+"') AND YEAR(DateRet)=YEAR('"+sqldate+"') AND idretraits_tontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE type ='retrait') ORDER by DateRet;");  // changed

        //System.out.println("SET @SumMontant := 0; SELECT idretraits_tontine, Date, Montant, (@SumMontant := @SumMontant + Montant) AS CumulativeSum FROM retraits_tontine WHERE IdEpargnant='"+this.IdEpargnant +"' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(Date)=MONTH("+sqldate+") AND YEAR(Date)=YEAR("+sqldate+");");
        ResultSet rs2 = pre2.executeQuery();
        
        
        String sqlfin="SELECT idTontine, DateTontine, JoursTontine, Mise FROM Tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant+ "' ";
        if (IntializeDate.change) sqlfin = sqlfin + " AND DateTontine  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        sqlfin = sqlfin+ "AND DateTontine='"+sqldate+"' AND idTontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE type ='ajout');";

        pre=conn.prepareStatement(sqlfin);
        
        // Does not exist in the database
       // System.out.println("SELECT idTontine, DateTontine, JoursTontine, Mise FROM Tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant+ "' AND DateTontine='"+sqldate+"' AND idTontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE type ='ajout');");  // a corriger
 
        ResultSet rs = pre.executeQuery();
        Vector<Object> tont = new Vector<Object>();
        
        
        columnNameTontine = new String[6];
        columnNameTontine[0] =  "Date";
        columnNameTontine[1] =  "Mise"; 
        columnNameTontine[2] =  "Total";
        columnNameTontine[3] =  "Total retraits";
        columnNameTontine[4] =  "Type versements";
        columnNameTontine[5] =   "Solde";
        
        if(!rs.next()) {
//            tont.add(date);
//            tont.add(null);
//            tont.add(null);
//            if (rs3.next()) {
//                tont.add(rs3.getDouble(1));
//            } else {
//                tont.add(0);
//            }
//            tont.add(null);
//            tont.add(new Boolean(true));
//            TontineVector.add(tont);
            
            while(rs2.next()) {
                
                  Vector<Object> tontr = new Vector<Object>();
                  tontr.add(rs2.getTimestamp(2));   //Date 
                  tontr.add(null);                    // mise est nulle 
                  tontr.add(rs2.getDouble(3));     // total
                  tontr.add(rs2.getDouble(4));  // Total retraits
                //  tontr.add(rs2.getInt(1));
              
                  tontr.add(new String("Retrait"));   // Type operation
                  tontr.add(null);   // Type operation   Solde
                  TontineVector.add(tontr);
                  
                  System.out.println("true");
                  System.out.println("TontineVector"+TontineVector);
            }
        } else {
            
            tont.add(date);
            tont.add(rs.getDouble(4));
            // retrieve values 
            String values = rs.getString(3);
            HashSet<String> valuesSet = new HashSet<>();
            if (values != null) Collections.addAll(valuesSet , values.split(","));
            Double depot;
            if((valuesSet.size()-1) >= 0){depot = rs.getDouble(4)*(valuesSet.size()-1);}
            else depot = 0.0;
            Double value;
            if (rs3.next()) {
                value = rs3.getDouble(1);
              //  tont.add(rs3.getDouble(1));
            } else {
              value = 0.0;
            }
            tont.add(depot);
            tont.add(value);
         //   tont.add(rs.getInt(1));
            tont.add(new String("Depot"));
            
          
            solde = solde-value+depot;
            tont.add(solde);
            
            TontineVector.add(tont);
            System.out.println("here list");
            
             while(rs2.next()) {
                  Vector<Object> tontr = new Vector<Object>();
                  tontr.add(rs2.getTimestamp(2));
                  tontr.add(null);
                  tontr.add(rs2.getDouble(3));  // total
                  
                  tontr.add(rs2.getDouble(4));
             //     tontr.add(rs2.getInt(1));
                
                                    tontr.add(new String("Retrait"));
                                    tontr.add(null);

                  TontineVector.add(tontr);
                  System.out.println("here"+ rs2.getTimestamp(2));
            }
            
        }
        
      //  System.out.println(date);
        beginCalendar.add(Calendar.MONTH, 1);
    }

//        pre = conn.prepareStatement("SELECT DateEpargne, MontantEpargne, MotifEpargne, idEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND MontantEpargne >=0" + " AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
//        System.out.println("SELECT DateEpargne, MontantEpargne, MotifEpargne, IdEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
//        ResultSet rs = pre.executeQuery();
//        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
//        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();
//        Vector<Vector> membreVector = new Vector<Vector>();
//        while (rs.next()) {
//     //   Vector<String> membre = new Vector<String>();
//            //   membre.add(String.valueOf(i)); 
//            Vector<Object> membre = new Vector<Object>();
//            Date date=sdf2.parse(rs.getString("DateEpargne"));
//           // membre.add(sdf.format(date));
//            membre.add(new java.sql.Timestamp(date.getTime()));
//            membre.add(rs.getDouble("MontantEpargne"));
//            // membre.add(new Double("5.5"));
//            membre.add(rs.getString("MotifEpargne"));
//            membre.add(rs.getString("idEpargne"));
//
//            //
//            membreVector.add(membre);
//        }
//
//        /*Close the connection after use (MUST)*/
//        if (conn != null) {
//            conn.close();
//        }
//        
//        
//        final EpargneContext copie= this; 
//        System.out.println("Model"+ jTable1.getModel().toString()+ "finish");
//jTable1.getModel().addTableModelListener(new TableModelListener() {
//
//    @Override
//    public void tableChanged(TableModelEvent e) {
//       NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
//       double doublePayment =copie.getSum().doubleValue();
//       String s = n.format(doublePayment);
//       jLabel4.setText(s);
//    }
//});
//
//        return membreVector
//  
//    int j=TontineVector.size();
//    while((j-1) >= 0 && ((String) TontineVector.get(j-1).get(5)).equalsIgnoreCase("depot")){  // cherche le dernier élement synthèse 
//        j--;
//    }
////    TontineVector.remove(TontineVector.size()-1);
//         TontineVector.remove(j-1);
      
    return TontineVector;
}



public Vector getEpargne() throws Exception {
        fillExceptionList();
        conn = Connect.ConnectDb2();
        

//    //    pre = conn.prepareStatement("SELECT DateEpargne, MontantEpargne, MotifEpargne, idEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
//PreparedStatement  pre = conn.prepareStatement("SELECT id AS ide, w AS dte, d AS description, \n" +
//"   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n" +
//"   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n" +
//"  FROM\n" +
//"  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n" +
//"          Epargne.MontantEpargne AS a\n" +
//"     FROM Epargne\n" +
//"     WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'\n" +          
//"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne) t");
//        System.out.println("SELECT DateEpargne, MontantEpargne, MotifEpargne, IdEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
//        System.out.println("query:"+ "SELECT id AS ide, w AS dte, d AS description, \n" +
//"   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n" +
//"   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n" +
//"  FROM\n" +
//"  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n" +
//"          Epargne.MontantEpargne AS a\n" +
//"     FROM Epargne\n" +
//"     WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'\n" +          
//"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne) t");
    String sql="SELECT id AS ide, w AS dte, d AS description, \n"
                + "   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n"
                + "   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n"
                + "  FROM\n"
                + "  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n"
                + "          Epargne.MontantEpargne AS a\n"
                + "     FROM Epargne\n"
                + "     WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'\n";
        
        if (initDate.change) sql = sql + "AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "'";
        sql =sql + "     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne) t";
        System.out.println("SELECT DateEpargne, MontantEpargne, MotifEpargne, IdEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
        System.out.println("query:" + "SELECT id AS ide, w AS dte, d AS description, \n"
                + "   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n"
                + "   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n"
                + "  FROM\n"
                + "  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n"
                + "          Epargne.MontantEpargne AS a\n"
                + "     FROM Epargne\n"
                + "     WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'\n"
                + "     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne) t");
        
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

        Vector<Vector> membreVector = new Vector<Vector>();
        Double balance = 0d;
        boolean firstentry=true;
        Date previous = null;
        Date date=new Date();
        Date d=new Date(); 
        boolean onceftc=false;
        
           
          // metadata
          
          ResultSetMetaData rsmd = rs.getMetaData();
          nbcolumns = rsmd.getColumnCount();
          columnName = new String[5];
//          for (int i = 0; i <nbcolumns; i++) {
//                String columnNa = rsmd.getColumnName(i+1);
//                columnName[i] = columnNa;
//            }
           columnName[0] = "Date";
           columnName[1] = "Débit";
           columnName[2] = "Crédit";
           columnName[3] = "Solde";
           columnName[4] = "Libellé";

                   
          
        while (rs.next()) {
            if (firstentry) previous = sdf2.parse(rs.getString("dte"));   // modified
            
//     //   Vector<String> membre = new Vector<String>();
//            //   membre.add(String.valueOf(i)); 
//            Vector<Object> membre = new Vector<Object>();
//            Date date=sdf2.parse(rs.getString("DateEpargne"));
//           // membre.add(sdf.format(date));
//            membre.add(new java.sql.Timestamp(date.getTime()));
//            if (rs.getDouble("MontantEpargne") < 0) {
//                membre.add(Math.abs(rs.getDouble("MontantEpargne")));
//                membre.add(null);
//            } else {
//                membre.add(null);
//                membre.add(rs.getDouble("MontantEpargne"));
//            }
//            membre.add(rs.getDouble("MontantEpargne"));  //solde 
//            // membre.add(new Double("5.5"));
//            membre.add(rs.getString("MotifEpargne"));  // Libellé
//            membre.add(rs.getString("idEpargne"));
//
//            //
//            membreVector.add(membre);
            
             date=sdf2.parse(rs.getString("dte"));
             System.out.println("date"+ date+ "previous"+ previous);
             // Modified 
            GregorianCalendar gcal= new GregorianCalendar();
            gcal.setTime(previous);
            gcal.set(Calendar.MILLISECOND, 0);
            gcal.set(Calendar.SECOND, 0); 
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.HOUR_OF_DAY, 0);
            
            
            while (!gcal.getTime().after(date)) {
                 d = gcal.getTime();
                if(isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry==false) && !ftcexceptionlist.contains(d)) {
        
                    Vector<Object> membre0 = new Vector<Object>(); 
                    membre0.add(new java.sql.Timestamp(d.getTime()));
                    membre0.add(new Double(100));
                    membre0.add(null);
                    balance =balance -100;
                    membre0.add(balance);
                    membre0.add("Frais de tenue de compte"); 
                    membreVector.add(membre0);
                    onceftc=true;
                }
                gcal.add(Calendar.DAY_OF_YEAR, 1);
            }
            
            Vector<Object> membre = new Vector<Object>();
           
            membre.add(new java.sql.Timestamp(date.getTime()));
            if (rs.getDouble("cshOUT") > 0) membre.add(rs.getDouble("cshOUT"));
            else membre.add(null);
            if (rs.getDouble("cshIN") > 0) membre.add(rs.getDouble("cshIN"));
            else membre.add(null);
            balance= balance -rs.getDouble("cshOUT") +rs.getDouble("cshIN");
            membre.add(balance);
            membre.add(rs.getString("description"));
            membre.add(rs.getString("ide"));
            membreVector.add(membre);
            
            previous=date; // modified
            firstentry= false; //modified
        }
        
        // retraits des frais de tenue de compte jusqu'au jour actuel
       
          if (!firstentry){
         Date date2=new Date();
             // Modified 
            GregorianCalendar gcal= new GregorianCalendar();
            gcal.setTime(previous);
            gcal.set(Calendar.MILLISECOND, 0);
            gcal.set(Calendar.SECOND, 0); 
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.HOUR_OF_DAY, 0);
            
            
            while (!gcal.getTime().after(date2)) {
          
                Date d2 = gcal.getTime();
                if(isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && !ftcexceptionlist.contains(d2) && (!(isTwentyEighthDayOfTheMonth(previous) && onceftc==true &&  DateUtils.isSameDay(d2,d)))) {
        
                   Vector<Object> membre0 = new Vector<Object>(); 
                    membre0.add(new java.sql.Timestamp(d2.getTime()));
                    membre0.add(new Double(100));
                    membre0.add(null);
                    balance =balance -100;
                    membre0.add(balance);
                    membre0.add("Frais de tenue de compte"); 
                    membreVector.add(membre0);
                }
                gcal.add(Calendar.DAY_OF_YEAR, 1);
            }
         }
       
          

        /*Close the connection after use (MUST)*/
        if (conn != null) {
            conn.close();
        }
        
         if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
               


        return membreVector;
    }

    
    
    public void fillEpargne() throws SQLException{
        conn = Connect.ConnectDb2();
        String sql ="SELECT Nom, Prenoms, Date_adhesion_ep, id, Typo FROM (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_ep as Date_adhesion_ep, 'Enfant' as Typo From Profil_enfant a WHERE a.Type_adhesion LIKE '%Epargne%' UNION SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_ep as Date_adhesion_ep, 'Adulte' as Typo From Profil_adulte b WHERE b.Type_adhesion LIKE '%Epargne%' UNION SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_ep as Date_adhesion_ep, 'Pers Morale' as Typo From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Epargne%') Test";
        PreparedStatement pre= conn.prepareStatement(sql);
        ResultSet rst = pre.executeQuery(sql);
        while (rst.next()){
            listEpargne.add(rst.getString(1)+ " "+ rst.getString(2));
            if (rst.getDate(3)== null)   epmaplist.put(rst.getString(1)+ " "+ rst.getString(2), new java.util.Date(2013, 12, 01));    // Add start date to do
            else epmaplist.put(rst.getString(1)+ " "+ rst.getString(2), new java.util.Date(rst.getDate(3).getTime()));
            idlist.put(rst.getString(1)+ " "+ rst.getString(2), String.valueOf(rst.getInt(4)) + " "+rst.getString(5));
        }
       
    }
    
    
       public void fillTontine() throws SQLException {
        conn = Connect.ConnectDb();
        String sql ="SELECT Nom, Prenoms, Date_adhesion_to, id, Typo FROM (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo From Profil_enfant a WHERE UPPER(a.Type_adhesion) LIKE  UPPER('%Tontine%') UNION SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo From Profil_adulte b WHERE UPPER(b.Type_adhesion) LIKE UPPER('%Tontine%') UNION SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo From Profil_persmorale c WHERE UPPER(c.Type_adhesion) LIKE UPPER('%Tontine%')) Test";
        PreparedStatement pre= conn.prepareStatement(sql);
        ResultSet rst = pre.executeQuery(sql);
        while (rst.next()) {
            listTontine.add(rst.getString(1)+ " "+ rst.getString(2));
            if (rst.getDate(3)== null)   tomaplist.put(rst.getString(1)+ " "+ rst.getString(2), new java.util.Date(2013, 12, 01));    // Add start date to do
            else tomaplist.put(rst.getString(1)+ " "+ rst.getString(2), new java.util.Date(rst.getDate(3).getTime()));
            toidlist.put(rst.getString(1)+ " "+ rst.getString(2), String.valueOf(rst.getInt(4)) + " "+rst.getString(5));
        }
       
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mouvements Epargne par membre", "Mouvements Tontine par membre" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel1.setText("Type rapport");

        jLabel2.setText("Membre:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mouvements Epargne par membre", "Mouvements Tontine par membre" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel3.setText("Date début:");

        jLabel4.setText("Date fin:");

        demoDateField1.setYearDigitsAmount(4);
        demoDateField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoDateField1ActionPerformed(evt);
            }
        });
        demoDateField1.setDate(new Date());

        demoDateField2.setYearDigitsAmount(4);
        demoDateField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoDateField2ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/excel.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/pdf.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel4))
                .add(91, 91, 91)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(44, 44, 44)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(demoDateField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jComboBox1, 0, 1, Short.MAX_VALUE)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 246, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(demoDateField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(74, 74, 74)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(34, 34, 34)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(25, 25, 25)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .add(43, 43, 43)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void demoDateField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoDateField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_demoDateField1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here: 
        if (jComboBox1.getSelectedIndex() == 0)
        demoDateField2.setDate(epmaplist.get((String) jComboBox2.getSelectedItem()));
        else 
        demoDateField2.setDate(tomaplist.get((String) jComboBox2.getSelectedItem()));
   
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void demoDateField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoDateField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_demoDateField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String parsingstring = idlist.get((String) jComboBox2.getSelectedItem());
        
   
       if(parsingstring != null || ! parsingstring.isEmpty()) { 
           String[] splited = parsingstring.split("\\s+");
           this.IdEpargnant= Integer.valueOf(splited[0]);
           this.typeEpargnant = splited[1];
           
        if(jComboBox1.getSelectedIndex()==0) { 
           
           
        try {
            generateReport();           
            JOptionPane.showMessageDialog(null, "Rapport généré avec succès ");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Impossible d'imprimer le rapport pour ce client");

            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }

       } else if (jComboBox1.getSelectedIndex()==1) {
            
        try {
            generateReportTontine();           
            JOptionPane.showMessageDialog(null, "Rapport généré avec succès ");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Impossible d'imprimer le rapport pour ce client");

            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }

           
           
           
       }}
           
        
       // this.IdEpargnant=1;
     //   this.typeEpargnant="Adulte";
      
    }//GEN-LAST:event_jButton2ActionPerformed
    public void generateExcelReport() throws Exception   {
        
        
        if (jComboBox1.getSelectedIndex()==0)  {
         String parsingstring = idlist.get((String) jComboBox2.getSelectedItem());
        
       if(parsingstring != null || ! parsingstring.isEmpty()) { 
           String[] splited = parsingstring.split("\\s+");
           this.IdEpargnant= Integer.valueOf(splited[0]);
           this.typeEpargnant = splited[1];
               
        String headers[] = {"Date", "Débit", "Crédit", "Solde", "Libellé"};
        Vector v = getEpargne();
        System.out.println("vectors mss"+v);
        JTable table=null;
        
        
    try {
         Object[][] test = to2DimArray(v);
         table = new JTable(test, headers);
          JTableToExcelExporter excel = new JTableToExcelExporter();
         excel.exportFromTable(table, "rapport epargne "+(String) jComboBox2.getSelectedItem() +".xls");
         JOptionPane.showMessageDialog(this, "Rapport généré avec succès");
    } catch(Exception e) {
        e.printStackTrace();
    }
    
}
} else {   // generate tontine
            
       String parsingstring = toidlist.get((String) jComboBox2.getSelectedItem());
        
       if(parsingstring != null || ! parsingstring.isEmpty()) { 
           String[] splited = parsingstring.split("\\s+");
           this.IdEpargnant= Integer.valueOf(splited[0]);
           this.typeEpargnant = splited[1];
               
        String headers[] = {"Date", "Mise", "Total", "Total retraits", "Type versement", "Solde"};
        Vector v = getTontine();
        System.out.println("vectors mss"+v);
        JTable table=null;
        
        
    try {
         Object[][] test = to2DimArray(v);
         table = new JTable(test, headers);
          JTableToExcelExporter excel = new JTableToExcelExporter();
         excel.exportFromTable(table, "rapport tontine "+(String) jComboBox2.getSelectedItem() +".xls");
         JOptionPane.showMessageDialog(this, "Rapport généré avec succès");
    } catch(Exception e) {
        e.printStackTrace();
    }
            
            
            
}
    
}    
}
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            
            // Génération
            generateExcelReport();
        } catch (Exception ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox1.getSelectedIndex() == 0){
        jComboBox2.setModel(new DefaultComboBoxModel(listEpargne.toArray()));
        demoDateField2.setDate(epmaplist.get((String) jComboBox2.getSelectedItem()));
         } else {
             jComboBox2.setModel(new DefaultComboBoxModel(listTontine.toArray()));
             demoDateField2.setDate(tomaplist.get((String) jComboBox2.getSelectedItem()));
            
            
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Report().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables

}
