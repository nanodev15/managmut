/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import static nehemie_mutuelle.IntializeDate.initdate;


import org.apache.commons.lang.time.DateUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author elommarcarnold
 */
public class Epargneview2 extends javax.swing.JFrame {
    private Connection conn;
    private Vector<Vector<Object>> data;
    private Vector<Vector<Object>> data2;
    private Vector<Vector<Object>> datamonth;
    Vector<String> header = new Vector<String>();
    PreparedStatement pre= null;
    private Date startDate=null;
    private Date endDate=null;
    private PreparedStatement pre1;
    private PreparedStatement pre2;
    static int totalrow;
    static int TOTAL_COLUMN=1;
    private TableRowSorter <DefaultTableModel> sorter;
    private Map <String, ArrayList<Date>> ftchash;
    private int originmonth; // month to block at origin month
    private int visitedmth [];   // show which year has already been visited for each month
    Date today;
    private Double sumepPlage = 0.0;
    public static final Color LightYellow = new Color(255,255,153);

    /** Creates new form Epargneview2 */
    public Epargneview2() throws Exception {
    today=new Date();
    visitedmth= new int [12];
    DecimalFormat decformatter = new DecimalFormat( "#00" );
    String datestr="2018"+"-"+decformatter.format(6)+"-"+"01";
    SimpleDateFormat fmm=new SimpleDateFormat("yyyy-MM-dd");
    Date testdate = fmm.parse(datestr);
   // System.out.println("datestr"+datestr);
        ftchash= new HashMap<String, ArrayList<Date>>();
        buildftchash();
        
        for (int i=0; i<12; i++){
            visitedmth[i]=0;
        }
        
        
        
        
        initComponents();
//        fillmonthsummtable(2017, 5);
//        System.out.println("blance"+getReport("Enfant", 1, fmm.parse("2018-07-01")));
//        System.out.println("check FTC"+checkftcwdr("Adulte", 6, 5, 2017));
//        System.out.println("check FTC2"+checkftcwdr("Adulte", 6, 6, 2018));
//        System.out.println("get report"+getReport("Adulte", 5, fmm.parse("2017-04-01")));
         int width = jTable1.getPreferredSize().width;//4=inset?
  //       System.out.print("width table"+width);
    }
    
    public class DateCellRenderer extends DefaultTableCellRenderer {
        public DateCellRenderer() {
            
            super();
        }

        @Override
        public void setValue(final Object value) {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String strValue = "";
            if(value != null && value instanceof Date){
               strValue = sdf.format(value);
            }
            super.setText(strValue);
        }
    }

    
    public Vector fillmonthsummtable2(int year, int month) throws SQLException, ParseException, Exception {
     JTable tbletfill = null;
     JScrollPane pnfill= null;
     switch (month) {
         case 1: tbletfill=jTable2;
                 pnfill=jScrollPane2;
                 break;
         case 2: tbletfill=jTable3;
                 pnfill=jScrollPane3;
                 break;
         case 3: tbletfill=jTable5;
                 pnfill=jScrollPane5;
                 break;
         case 4: tbletfill=jTable6;
                 pnfill=jScrollPane6;
                 break;
         case 5: tbletfill=jTable7;
                 pnfill=jScrollPane7;
                 break;
         case 6: tbletfill=jTable8;
                 pnfill=jScrollPane8;
                 break;
         case 7: tbletfill=jTable9;
                 pnfill=jScrollPane9;
                 break;
         case 8: tbletfill=jTable10;
                 pnfill=jScrollPane10;
                 break;
         case 9: tbletfill=jTable11;
                 pnfill=jScrollPane11;
                 break;
         case 10:tbletfill=jTable12;
                 pnfill=jScrollPane12;
                 break;
         case 11: tbletfill=jTable13;
                  pnfill=jScrollPane13;
                  break;
         case 12: tbletfill=jTable14;
                  pnfill=jScrollPane14;
                  break;
    } 
     
     
  //  if (tbletfill == null) System.out.println("null value for tbletfill");
    conn = Connect.ConnectDb(); 
    String datestr;
    DecimalFormat decformatter = new DecimalFormat( "#00" );
    datestr=String.valueOf(year)+"-"+decformatter.format(month)+"-"+"01";
 //   System.out.println("datestr"+datestr);
    PreparedStatement prepr;
    prepr = conn.prepareStatement("SELECT ide, Type, id, w, Nom, Prenoms, carnet, SUM(cshIN),SUM(cshOUT), Date_adhesion_ep FROM (SELECT idEpargne AS ide, DateEpargne AS w , Nom, Prenoms, carnet, Date_adhesion_ep, Type, id,\n" +
                "    CASE WHEN (MontantEpargne>=0) THEN MontantEpargne ELSE NULL END AS cshIN,\n" +
                "    CASE WHEN (MontantEpargne<0) THEN (SUBSTR(MontantEpargne,2,15)) ELSE NULL END AS cshOUT\n" +
                "    FROM ((SELECT * From Epargne WHERE MONTH(DateEpargne)=MONTH('"+datestr+"') AND YEAR(DateEpargne)=YEAR('"+datestr+"')) ep RIGHT JOIN (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom, a.Prenoms, a.Date_adhesion_ep as Date_adhesion_ep, 'Enfant' as Type From Profil_enfant a WHERE a.Type_adhesion LIKE '%Epargne%' UNION\n" +
                "														SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms, b.Prenoms, b.Date_adhesion_ep as Date_adhesion_ep, 'Adulte' as Type From Profil_adulte b WHERE b.Type_adhesion LIKE '%Epargne%' UNION\n" +
                "													 SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale, c.Raison_sociale, c.Date_adhesion_ep as Date_adhesion_ep , 'Pers Morale' as Type From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Epargne%'\n" +
                "							) un  ON ep.IdEpargnant = un.id AND ep.TypeEpargnant = un.Type)) u WHERE u.Date_adhesion_ep <= LAST_DAY('"+datestr+"') GROUP BY carnet;");
    
    
    ResultSet rs = prepr.executeQuery();
    Vector<Vector> dataVector = new Vector<Vector>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    int iterator=1;
    while(rs.next()) {
        Vector<Object> tont = new Vector<Object>();
        tont.add(iterator);   // Numero
        tont.add(rs.getString(7));  // Numero carnet
        tont.add(rs.getString(5)+" "+rs.getString(6));  // Nom & Prénoms
        double report= getReport(rs.getString(2), rs.getInt(3), formatter.parse(datestr));
        tont.add(report);  // Report 
        int ftc=0;
        if (checkftcwdr(rs.getString(2), rs.getInt(3), month , year)==true) {
               ftc=100;
        } else {
               ftc= 0; 
        }
        tont.add(ftc);
        // report à ajouter
        
        tont.add(rs.getDouble(8));  // Total cotisé
     //   System.out.println("Object"+rs.getObject(8));
    //    System.out.println("Total cotisé"+rs.getDouble(8));
        tont.add(rs.getDouble(9));  // Retraits 
        tont.add(rs.getDouble(8)+rs.getDouble(9)+report-ftc);
        tont.add(rs.getInt(3));
        tont.add(rs.getString(2));  
        iterator++;          
        dataVector.add(tont);     
    }
     
//    Object[][] out = to2DimArray(dataVector);
//    // refresh 
//    
//    
//tbletfill.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//tbletfill.setFillsViewportHeight(true);
//tbletfill.setPreferredScrollableViewportSize(new Dimension(1000,70));
//tbletfill.setModel(new javax.swing.table.DefaultTableModel(
//out,
//    new String [] {
//        "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
//    }
//) {
//    Class[] types = new Class [] {
//        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
//    };
//    boolean[] canEdit = new boolean [] {
//        false, false, false, false, false, false, false, false, false, false
//    };
//
//    public Class getColumnClass(int columnIndex) {
//        return types [columnIndex];
//    }
//
//    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        return canEdit [columnIndex];
//    }
//});
//tbletfill.getColumnModel().getColumn(0).setPreferredWidth(30);
//tbletfill.getColumnModel().getColumn(1).setPreferredWidth(90);
//tbletfill.getColumnModel().getColumn(3).setPreferredWidth(130);
//tbletfill.getColumnModel().getColumn(4).setPreferredWidth(130);
//tbletfill.getColumnModel().getColumn(5).setPreferredWidth(90);
//tbletfill.getColumnModel().getColumn(6).setPreferredWidth(120);
//tbletfill.getColumnModel().getColumn(7).setPreferredWidth(130);
////jTable2.getColumnModel().getColumn(8).setPreferredWidth(0);
////jTable2.getColumnModel().getColumn(9).setPreferredWidth(0);
////jTable2.getColumnModel().getColumn(10).setPreferredWidth(0);
//tbletfill.getColumnModel().getColumn(8).setMaxWidth(0);
//tbletfill.getColumnModel().getColumn(8).setMinWidth(0);
//tbletfill.getColumnModel().getColumn(9).setMaxWidth(0);
//tbletfill.getColumnModel().getColumn(9).setMinWidth(0);

////pnfill=new FrozenTablePane(tbletfill, 3);
////jScrollPane2=new FrozenTablePane(jTable2, 3); 
//// ((FrozenTablePane) pnfill).updateFrozenModel(tbletfill, 3, pnfill.getViewport());
//
//    
//    
     if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
    
      // System.out.println("This is data vector "+ dataVector);
       visitedmth[month-1]=year;   // update  visited month
       
       return dataVector;
        
       
 }   
    
//    
//    public void fillmonthTables (int month, int year) throws SQLException{
//       
//
//    }
    
     public class FrozenTablePane extends JScrollPane{
          TableModel frozenModel;
public void updateFrozenModel(JTable tab, int colsToFreeze, JViewport viewport) {
    
    frozenModel = new DefaultTableModel(
                                tab.getRowCount(),
                                colsToFreeze);
   //JTable frozenTable = (JTable) viewport.getView();
    
   TableModel model = tab.getModel();
   
   
  //  System.out.println("model rowcount"+ model);
    String value="";
   
   for (int i = 0; i < model.getRowCount(); i++) {
      for (int j = 0; j < colsToFreeze; j++) {
          if(model.getValueAt(i, j)==null) value="";
          else if (model.getValueAt(i, j).getClass()==Integer.class) value= Integer.toString((Integer) model.getValueAt(i, j));
          else value = (String) model.getValueAt(i, j);
        frozenModel.setValueAt(value, i, j);
      }
    }
   
   //frozenTable.setModel(frozenModel);
   JTable frozenTable = new JTable(frozenModel);
   
    for (int j = 0; j < colsToFreeze; j++) {
        JTableHeader th = frozenTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(j);
   //     System.out.println(tab.getColumnName(j));
        tc.setHeaderValue(tab.getColumnName(j));
        th.repaint();
        
    }
    
    frozenTable.getColumnModel().getColumn(0).setPreferredWidth(30);
    frozenTable.getColumnModel().getColumn(1).setPreferredWidth(90);
    frozenTable.getColumnModel().getColumn(2).setPreferredWidth(250);
//    JTable tcopy= tab;
//    //remove the frozen columns from the original table
//    for (int j = 0; j < colsToFreeze; j++) {
//      tcopy.removeColumn(tcopy.getColumnModel().getColumn(0));
//    }
     //remove the frozen columns from the original table
//    for (int j = 0; j < colsToFreeze; j++) {
//      tab.removeColumn(tab.getColumnModel().getColumn(0));
//    }
 //   tcopy.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
   
// format the frozen table
    JTableHeader header = tab.getTableHeader();
    frozenTable.setBackground(header.getBackground());
    frozenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    frozenTable.setEnabled(false);
    
    //remove the frozen columns from the original table
    for (int j = 0; j < colsToFreeze; j++) {
      tab.removeColumn(tab.getColumnModel().getColumn(0));
    }
    
 
    
 
    //set frozen table as row header view
    JViewport viewport2 = new JViewport();
    viewport2.setView(frozenTable);
    viewport2.setPreferredSize(frozenTable.getPreferredSize());  
    this.setRowHeaderView(viewport2);
    this.setCorner(JScrollPane.UPPER_LEFT_CORNER, frozenTable.getTableHeader());
    this.setViewportView(tab);
    
} 

public TableModel getFrozenModel() {
    return frozenModel;
} 
 public FrozenTablePane(JTable table, int colsToFreeze) {
    super(table);
 
    TableModel model = table.getModel();
    
 
    //create a frozen model
     frozenModel = new DefaultTableModel(
                                model.getRowCount(),
                                colsToFreeze);
 
    //populate the frozen model
    for (int i = 0; i < model.getRowCount(); i++) {
      for (int j = 0; j < colsToFreeze; j++) {
        String value = (String) model.getValueAt(i, j);
        frozenModel.setValueAt(value, i, j);
      }
    }
 
    //create frozen table
    JTable frozenTable = new JTable(frozenModel);
    for (int j = 0; j < colsToFreeze; j++) {
        JTableHeader th = frozenTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(j);
        tc.setHeaderValue(table.getColumnName(j));
        th.repaint();
        
    }
    
    frozenTable.getColumnModel().getColumn(0).setPreferredWidth(30);
    frozenTable.getColumnModel().getColumn(1).setPreferredWidth(90);
    frozenTable.getColumnModel().getColumn(2).setPreferredWidth(250);
    
 
    //remove the frozen columns from the original table
    for (int j = 0; j < colsToFreeze; j++) {
      table.removeColumn(table.getColumnModel().getColumn(0));
    }
   // table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//    table.getColumn("Report").setMinWidth(0);
//    table.getColumn("Report").setMaxWidth(0);
//    table.getColumn("Report").setWidth(0);
//   
 
    //format the frozen table
    JTableHeader header = table.getTableHeader();
    frozenTable.setBackground(header.getBackground());
    frozenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    frozenTable.setEnabled(false);
 
    //set frozen table as row header view
    JViewport viewport = new JViewport();
    viewport.setView(frozenTable);
    viewport.setPreferredSize(frozenTable.getPreferredSize());
    setRowHeaderView(viewport);
    setCorner(JScrollPane.UPPER_LEFT_CORNER, frozenTable.getTableHeader());
  }
}
     
 public Double getReport (String typeEpargnant, int idEpargnant, Date begindate) throws Exception {
        conn = Connect.ConnectDb2();
        SimpleDateFormat fmm=new SimpleDateFormat("yyyy-MM-dd");
        String datestr= fmm.format(begindate);
        PreparedStatement pre21= conn.prepareStatement("SET @SumMontant := 0;");
        pre21.executeQuery();
        String sql = "SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d,\n" +
"          Epargne.MontantEpargne AS a, (@SumMontant := @SumMontant + MontantEpargne) AS CumulativeSum\n" +
"     FROM Epargne\n" +
"     WHERE IdEpargnant='"+idEpargnant+"' AND TypeEpargnant='"+typeEpargnant+"' AND DateEpargne < '"+datestr+ "'\n" +
"     ORDER BY DateEpargne ASC;";
        pre = conn.prepareStatement(sql);
        
        
        
        
        //GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne

        ResultSet rs = pre.executeQuery();
       // SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

       
        Double balance = 0d;
        while (rs.next()) {
            balance= rs.getDouble(5);
            
        
        }
        
        
    
        // Retrieve sum from ftchash 
        ArrayList<Date> value= ftchash.get(typeEpargnant+" "+idEpargnant);
        int count=0;
        if (value !=null){
        for (Date ftc: value ) {
         //   System.out.println("value"+value);
          
            if(ftc.before(begindate)) {
               
                count++;
            }
            
            
            
                    

        }
        }
        
        
        if (idEpargnant == 52 && typeEpargnant.equalsIgnoreCase("adulte")) {
            System.out.println("sql new"+sql);
            System.out.println("balance"+balance);
            System.out.println("count"+count);
        }
        

        balance= balance - count* 100;   // FTC
        
          
        
        
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
         
         
         
         
        return balance; 
 }
 
 
  public Boolean checkftcwdr(String typeEpargnant, int idEpargnant, int month, int year) throws Exception {
  //    System.out.println(ftchash.get(typeEpargnant+" "+idEpargnant));
    String datestr;
    DecimalFormat decformatter = new DecimalFormat( "#00" );
    datestr=String.valueOf(year)+"-"+decformatter.format(month)+"-"+"28";  
    SimpleDateFormat ffm= new SimpleDateFormat("yyyy-MM-dd");
    Date checkDate= ffm.parse(datestr);
    ArrayList<Date> value= ftchash.get(typeEpargnant+" "+idEpargnant);
 //   System.out.println(typeEpargnant+" "+idEpargnant+"debug");
   
    if(value!= null && value.contains(checkDate)) {
              return true;  
    }
    
  return false;    
      
  }
  
    
 public void fillmonthsummtable(int year, int month) throws SQLException, ParseException, Exception {
     JTable tbletfill = null;
     JScrollPane pnfill= null;
     switch (month) {
         case 1: tbletfill=jTable2;
                 pnfill=jScrollPane2;
                 break;
         case 2: tbletfill=jTable3;
                 pnfill=jScrollPane3;
                 break;
         case 3: tbletfill=jTable5;
                 pnfill=jScrollPane5;
                 break;
         case 4: tbletfill=jTable6;
                 pnfill=jScrollPane6;
                 break;
         case 5: tbletfill=jTable7;
                 pnfill=jScrollPane7;
                 break;
         case 6: tbletfill=jTable8;
                 pnfill=jScrollPane8;
                 break;
         case 7: tbletfill=jTable9;
                 pnfill=jScrollPane9;
                 break;
         case 8: tbletfill=jTable10;
                 pnfill=jScrollPane10;
                 break;
         case 9: tbletfill=jTable11;
                 pnfill=jScrollPane11;
                 break;
         case 10:tbletfill=jTable12;
                 pnfill=jScrollPane12;
                 break;
         case 11: tbletfill=jTable13;
                  pnfill=jScrollPane13;
                  break;
         case 12: tbletfill=jTable14;
                  pnfill=jScrollPane14;
                  break;
    } 
     
     
  //  if (tbletfill == null) System.out.println("null value for tbletfill");
    //   conn = Connect.ConnectDb(); 
    conn= Connect.ConnectDb2();
    String datestr;
    DecimalFormat decformatter = new DecimalFormat( "#00" );
    datestr=String.valueOf(year)+"-"+decformatter.format(month)+"-"+"01";
  //  System.out.println("datestr"+datestr);
    PreparedStatement prepr;
//    prepr = conn.prepareStatement("SET sql_mode=(SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY',''))");
//    prepr.executeQuery();
//     System.out.println("executed");
    prepr = conn.prepareStatement("SELECT ide, Type, id, w, Nom, Prenoms, carnet, SUM(cshIN),SUM(cshOUT), Date_adhesion_ep FROM (SELECT idEpargne AS ide, DateEpargne AS w , Nom, Prenoms, carnet, Date_adhesion_ep, Type, id,\n" +
                "    CASE WHEN (MontantEpargne>=0) THEN MontantEpargne ELSE NULL END AS cshIN,\n" +
                "    CASE WHEN (MontantEpargne<0) THEN (SUBSTR(MontantEpargne,2,15)) ELSE NULL END AS cshOUT\n" +
                "    FROM ((SELECT * From Epargne WHERE MONTH(DateEpargne)=MONTH('"+datestr+"') AND YEAR(DateEpargne)=YEAR('"+datestr+"')) ep RIGHT JOIN (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom, a.Prenoms, a.Date_adhesion_ep as Date_adhesion_ep, 'Enfant' as Type From Profil_enfant a WHERE a.Type_adhesion LIKE '%Epargne%' UNION\n" +
                "														SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms, b.Prenoms, b.Date_adhesion_ep as Date_adhesion_ep, 'Adulte' as Type From Profil_adulte b WHERE b.Type_adhesion LIKE '%Epargne%' UNION\n"
             + "													 SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale, c.Raison_sociale, c.Date_adhesion_ep as Date_adhesion_ep , 'Pers Morale' as Type From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Epargne%'\n"
             + "							) un  ON ep.IdEpargnant = un.id AND ep.TypeEpargnant = un.Type)) u WHERE u.Date_adhesion_ep <= LAST_DAY('" + datestr + "') GROUP BY carnet;");
    
    
    ResultSet rs = prepr.executeQuery();
    Vector<Vector> dataVector = new Vector<Vector>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    int iterator=1;
    while(rs.next()) {
        Vector<Object> tont = new Vector<Object>();
        tont.add(iterator);   // Numero
        tont.add(rs.getString(7));  // Numero carnet
        tont.add(rs.getString(5)+" "+rs.getString(6));  // Nom & Prénoms
        double report= getReport(rs.getString(2), rs.getInt(3), formatter.parse(datestr));
        tont.add(report);  // Report 
        int ftc=0;
        if (checkftcwdr(rs.getString(2), rs.getInt(3), month , year)==true) {
               ftc=100;
        } else {
               ftc= 0; 
        }
        tont.add(ftc);
        // report à ajouter
        
        tont.add(rs.getDouble(8));  // Total cotisé
      //  System.out.println("Object"+rs.getObject(8));
     //   System.out.println("Total cotisé"+rs.getDouble(8));
        tont.add(rs.getDouble(9));  // Retraits 
        tont.add(rs.getDouble(8)-rs.getDouble(9)+report-ftc);
        tont.add(rs.getInt(3));
        tont.add(rs.getString(2));  
        iterator++;          
        dataVector.add(tont);     
    }
     
    Object[][] out = to2DimArray(dataVector);
    // refresh 
    
    
tbletfill.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
tbletfill.setFillsViewportHeight(true);
tbletfill.setPreferredScrollableViewportSize(new Dimension(1000,70));
tbletfill.setModel(new javax.swing.table.DefaultTableModel(
out,
    new String [] {
        "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
    }
) {
    Class[] types = new Class [] {
        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
    };
    boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false, false, false, false, false
    };

    public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
});
//tbletfill.getColumnModel().getColumn(8).setPreferredWidth(0);
//tbletfill.getColumnModel().getColumn(9).setPreferredWidth(0);

tbletfill.getColumnModel().getColumn(9).setMinWidth(0);
tbletfill.getColumnModel().getColumn(9).setMaxWidth(0);
tbletfill.getColumnModel().getColumn(8).setMinWidth(0);
tbletfill.getColumnModel().getColumn(8).setMaxWidth(0);
tbletfill.getColumnModel().getColumn(0).setPreferredWidth(30);
tbletfill.getColumnModel().getColumn(1).setPreferredWidth(90);
tbletfill.getColumnModel().getColumn(3).setPreferredWidth(130);
tbletfill.getColumnModel().getColumn(4).setPreferredWidth(130);
tbletfill.getColumnModel().getColumn(5).setPreferredWidth(90);
tbletfill.getColumnModel().getColumn(6).setPreferredWidth(120);
tbletfill.getColumnModel().getColumn(7).setPreferredWidth(130);

//jTable2.getColumnModel().getColumn(8).setPreferredWidth(0);
//jTable2.getColumnModel().getColumn(9).setPreferredWidth(0);
//jTable2.getColumnModel().getColumn(10).setPreferredWidth(0);
//tbletfill.getColumnModel().getColumn(8).setMaxWidth(0);
//tbletfill.getColumnModel().getColumn(8).setMinWidth(0);
//tbletfill.getColumnModel().getColumn(9).setMaxWidth(0);
//tbletfill.getColumnModel().getColumn(9).setMinWidth(0);

////pnfill=new FrozenTablePane(tbletfill, 3);
////jScrollPane2=new FrozenTablePane(jTable2, 3); 
 if (tbletfill !=null) { ((FrozenTablePane) pnfill).updateFrozenModel(tbletfill, 3, pnfill.getViewport());
                        
  
                       }
//
//    
//    
     if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
    
     //  System.out.println("This is data vector "+ dataVector);
       visitedmth[month-1]=year;   // update  visited month 
    
     
     
 }   
 
 public void buildftchash() throws SQLException, Exception {
        conn = Connect.ConnectDb();
        pre = conn.prepareStatement("SELECT DISTINCT IdEpargnant, TypeEpargnant FROM Epargne ");
        ResultSet rs = pre.executeQuery();
          while (rs.next()) {
              fillftctable(rs.getString(2), rs.getInt(1));
          }
          
           if (conn != null) {
            conn.close();
        }
      
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
    }
    
    
    private int countftc(Date date) {
        int count=0;
        for (ArrayList<Date> value: ftchash.values()) {
         //   System.out.println("value"+value);
            if(value.contains((Date) date)) {
               // System.out.println(ftchash);
                count++;
            }
        }
        
        return count;
        
    }
     
     public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    } 
     
      public boolean isTwentyEighthDayOfTheMonth(Date dateToday){
        Calendar c = new GregorianCalendar();
        c.setTime(dateToday);
        return c.get(Calendar.DAY_OF_MONTH)==28;
    }
     
     
public void fillftctable(String typeEpargnant, int idEpargnant) throws Exception {
        ftchash.put(typeEpargnant+" "+idEpargnant, new ArrayList<Date>()); 
        conn = Connect.ConnectDb();
            // exception list

        String sql0="SELECT Dateftc FROM Exceptionftc WHERE typeEpargnant='"+typeEpargnant+"' AND idEpargnant='"+idEpargnant+"'";
     IntializeDate dateini = new IntializeDate();   
                
        try {

if (dateini.change) sql0 = sql0+ " AND Dateftc  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
 } catch (Exception ex) {
    ex.printStackTrace();
}
        PreparedStatement pr0 = conn.prepareStatement(sql0);
        ResultSet rst = pr0.executeQuery();
        
        ArrayList<Date> excList= new ArrayList<>(); 
        while (rst.next()) {
         
            excList.add(rst.getDate(1));
            
        }
        
        if (idEpargnant==24 && typeEpargnant.equalsIgnoreCase("Adulte")) System.out.println("excList"+excList);
        
        
        String sql;
       


//        pre = conn.prepareStatement("SELECT id AS ide, w AS dte, d AS description, \n" +
//"   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n" +
//"   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n" +
//"  FROM\n" +
// "  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n" +
////"  (SELECT  Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n" +                
//"          Epargne.MontantEpargne AS a\n" +
//"     FROM Epargne\n" +
//"     WHERE IdEpargnant='" + idEpargnant + "' AND TypeEpargnant='" + typeEpargnant + "'\n" +          
//"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne, Epargne.IdEpargne) t");

          sql="SELECT id AS ide, w AS dte, d AS description, \n"
                + "   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n"
                + "   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n"
                + "  FROM\n"
                + "  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n"
                + "          Epargne.MontantEpargne AS a\n"
                + "     FROM Epargne\n"
                + "     WHERE IdEpargnant='" + idEpargnant + "' AND TypeEpargnant='" + typeEpargnant + "'\n";
        
        if (dateini.change) sql = sql + "AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "'";
        sql =sql + "     ORDER BY Epargne.DateEpargne) t";
        
        pre =conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

        
        Double balance = 0d;
        boolean firstentry=true;
        Date previous = null;
        Date date=new Date();
        Date d=new Date(); 
        Date lastprevdate = new Date();
        boolean onceftc=false;
        while (rs.next()) {
            if (firstentry) previous = sdf2.parse(rs.getString("dte"));   // modified
             date=sdf2.parse(rs.getString("dte"));
             // Modified 
            GregorianCalendar gcal= new GregorianCalendar();
            gcal.setTime(previous);
            gcal.set(Calendar.MILLISECOND, 0);
            gcal.set(Calendar.SECOND, 0); 
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.HOUR_OF_DAY, 0);       
            while (!gcal.getTime().after(date)) {
                 d = gcal.getTime();
                if (isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry==false) && !excList.contains(d)) {
                    
                    
                    if (lastprevdate.getTime() != d.getTime() && onceftc==true) {
                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d);
                    balance = balance - 100;
             
                    lastprevdate = d;

                    } else if (onceftc== false) {
                   
                   ftchash.get(typeEpargnant+" "+idEpargnant).add(d);
                    lastprevdate = d;
                   
                    balance = balance - 100;
                   
                    onceftc = true;

                        
                    }
                    
                    
                    
                    
                    
                   
//                 if (idEpargnant==24 && typeEpargnant.equalsIgnoreCase("Adulte"))     System.out.println(" datchr vaut"+d);
                  
                }
                gcal.add(Calendar.DAY_OF_YEAR, 1);
            }             
            balance= balance -rs.getDouble("cshOUT") +rs.getDouble("cshIN");           
            previous=date; // modified
            firstentry= false; //modified
        }
        
        // retraits des frais de tenue de compte jusqu'au jour actuel
       
          if (!firstentry) {
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
                if(isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && (!(isTwentyEighthDayOfTheMonth(previous) && onceftc==true &&  DateUtils.isSameDay(d2,d)) && !excList.contains(d2))) {
                    
                    if (lastprevdate.getTime() != d2.getTime() && onceftc==true) {
                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d2);
                    balance = balance - 100;
             
                    lastprevdate = d2;

                    } else if (onceftc== false) {
                   
                   ftchash.get(typeEpargnant+" "+idEpargnant).add(d2);
                    lastprevdate = d2;
                   
                    balance = balance - 100;
                   
                    onceftc = true;   // added

                        
                    }
                    
                    
//                    balance =balance-100;
//                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d2);
//                                        if (idEpargnant==24 && typeEpargnant.equalsIgnoreCase("Adulte"))  {     System.out.println(" datchrapre vaut"+d2);
//                                        
//                                            System.out.println("ex1"+excList.get(0));
//                                            System.out.println("d2"+d2);
//                                            System.out.println("true or false"+excList.contains(d2));
//                       }
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
         
    
          if (pr0 != null) {
            pr0.close();
        }
         
         if (rst != null) {
            rst.close();
        }
         
         
        ArrayList<Date> choix = ftchash.get(typeEpargnant+" "+idEpargnant);
          GregorianCalendar gcal= new GregorianCalendar();
            gcal.setTime(new Date());
            gcal.set(Calendar.MILLISECOND, 0);
            gcal.set(Calendar.SECOND, 0); 
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.HOUR_OF_DAY, 0);
            gcal.set(Calendar.DAY_OF_MONTH, 28);
            gcal.set(Calendar.YEAR, 2012);
            gcal.set(Calendar.MONTH,3);
            
            System.out.println("date defeefe"+gcal.getTime());
            if (choix.contains(gcal.getTime())) System.out.println("type ep"+typeEpargnant + "id ep"+idEpargnant);
            
            System.out.println("ftcatch atc"+ftchash.get(("Adulte"+" "+"24")));
        
       
    }
////public boolean ftc(String typedher, int id, ){
     
//        
//        
//    }  
    public Vector getSynthese()throws Exception {
        IntializeDate dateini = new IntializeDate();   
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        if(startDate != null) {
        beginCalendar.setTimeInMillis(startDate.getTime());
        beginCalendar.set (Calendar.DAY_OF_MONTH,1); 
        finishCalendar.setTimeInMillis(endDate.getTime());
        Vector<Vector> TontineVector = new Vector<Vector>();
        Date date;
        // String date;
        conn = Connect.ConnectDb(); 
        while (beginCalendar.before(finishCalendar) || beginCalendar.equals(finishCalendar)) {
        //add one month to date per loop
        date = beginCalendar.getTime(); 
        java.sql.Date sqldate= new java.sql.Date(date.getTime());
        String sql0, sql1, sql2;
        sql0 = "SELECT SUM(MontantEpargne) FROM Epargne  WHERE MONTH(DateEpargne)=MONTH('"+sqldate+"') AND YEAR(DateEpargne)=YEAR('"+sqldate+"') AND MontantEpargne >= 0";
        sql1=  "SELECT SUM(MontantEpargne) FROM Epargne  WHERE MONTH(DateEpargne)=MONTH('"+sqldate+"') AND YEAR(DateEpargne)=YEAR('"+sqldate+"') AND MontantEpargne < 0";
        sql2 = "SELECT SUM(MontantEpargne) FROM Epargne  WHERE MONTH(DateEpargne)=MONTH('"+sqldate+"') AND YEAR(DateEpargne)=YEAR('"+sqldate+"')";
        
        
        if (dateini.change) sql0 = sql0 + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "'";
        if (dateini.change) sql1 = sql1 + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "'";
        if (dateini.change) sql2 = sql2 + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "'";

        pre1=conn.prepareStatement(sql0);
        ResultSet rs1 = pre1.executeQuery(sql0);
        pre2=conn.prepareStatement(sql1);
        pre=conn.prepareStatement(sql2);
        ResultSet rs = pre.executeQuery(sql2);
        ResultSet rs2 = pre2.executeQuery(sql1);
        // Algorithme un peu naif à améliorer 
        Calendar caltweighthismonth= Calendar.getInstance();  
        caltweighthismonth.set(Calendar.HOUR_OF_DAY, 0);
        caltweighthismonth.set(Calendar.MINUTE, 0);
        caltweighthismonth.set(Calendar.SECOND, 0);
        caltweighthismonth.set(Calendar.MILLISECOND, 0);
        caltweighthismonth.set(beginCalendar.get(Calendar.YEAR), beginCalendar.get(Calendar.MONTH), 28);
        Date tweighthismonth =  caltweighthismonth.getTime();
     //   System.out.println("tweig"+tweighthismonth);
        // looking for ftc value 
        Double ftc= (double) countftc(tweighthismonth)*100;
        
        Vector<Object> tont = new Vector<Object>();
        tont.add(date);
        tont.add(ftc);
        if (rs1.next()){
            tont.add(rs1.getDouble(1));
            if (rs2.next()){
                  tont.add(rs2.getDouble(1));
            } else {
                  tont.add((double)0);
            }
          //  tont.add(rs.getDouble(1));
            if (rs.next()){
                  tont.add(rs.getDouble(1));
            } else {
                  tont.add((double)0);
            }
            TontineVector.add(tont);
        } else {
           
            tont.add((double)0);
            if (rs2.next()){
                  tont.add(rs1.getDouble(1));
            } else {
                  tont.add((double)0);
            }
            
            if (rs.next()){
                  tont.add(rs.getDouble(1));
            } else {
                  tont.add((double)0);
            }
            TontineVector.add(tont);
        }
        
        // avant ajout 
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month == 11) {
            
             Vector<Object> tont2 = new Vector<Object>();
             tont2.add(date);
             tont2.add(0.0);
             tont2.add(0.0);
             tont2.add(0.0);
             tont2.add(0.0);
             
             TontineVector.add(tont2);
            
        }
        
        
        
        
        
     beginCalendar.add(Calendar.MONTH, 1);     
        
    } 
      totalrow=TontineVector.size()-1; 
      Vector<Object> tont2 = new Vector<Object>();
      tont2.add(new Date());
      tont2.add((double) 0);
      tont2.add((double) 0);
      tont2.add((double) 0);
      TontineVector.add(tont2);
      
      
      
      
      
      return TontineVector;} else {
            return null;
        }}
    
    
    public ChartPanel PlotCurve(){
 
        XYSeries series = new XYSeries("MyGraph");
        series.add(0, 1);
        series.add(1, 2);
        series.add(2, 5);
        series.add(7, 8);
        series.add(9, 10);


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Chart",
                "x-axis",
                "y-axis",
                dataset, 
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;

  //  Container contenu = getContentPane() ;
   // contenu.add(panel);
        
    } 
    
    public void setChart (ChartPanel p){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries t1 = new TimeSeries("Solde par mois");
        for (int i=0; i< jTable4.getRowCount()-1; i++) {
           // System.out.println("Date"+(Date)jTable4.getValueAt(i, 0));
           // System.out.println("dttype"+jTable1.getValueAt(i, 0));
            t1.add(new Day((Date)jTable4.getValueAt(i, 0)), (double) jTable4.getValueAt(i, 4));
        }
//        XYSeries series = new XYSeries("MyGraph");
//        series.add(0, 1);
//        series.add(1, 2);
//        series.add(2, 5);
//        series.add(7, 8);
//        series.add(9, 10);
        dataset.addSeries(t1);
//        
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(series);
        TimeSeries t2 = new TimeSeries("FTC");
        for (int i=0; i< jTable4.getRowCount()-1; i++) {
            //System.out.println("Date"+(Date)jTable4.getValueAt(i, 0));
           // System.out.println("dttype"+jTable1.getValueAt(i, 0));
            t2.add(new Day((Date)jTable4.getValueAt(i, 0)), (double) jTable4.getValueAt(i, 1));
        }
//        XYSeries series = new XYSeries("MyGraph");
//        series.add(0, 1);
//        series.add(1, 2);
//        series.add(2, 5);
//        series.add(7, 8);
//        series.add(9, 10);
        dataset.addSeries(t2);
        
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Progression des épargnes pour "+jComboBox1.getSelectedItem().toString(),
                "Mois",
                "Montant",
                dataset);
        
      p.setChart(chart);
      

//    Container contenu = getContentPane() ;
//    contenu.add(panel);
        
    } 
    
    
     public JFreeChart getChart (){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries t1 = new TimeSeries("Commission sur Tontine");
        for (int i=0; i< jTable4.getRowCount(); i++) {
            t1.add(new Day((Date)jTable4.getValueAt(i, 0)), (double) jTable4.getValueAt(i, 4));
        }
//        XYSeries series = new XYSeries("MyGraph");
//        series.add(0, 1);
//        series.add(1, 2);
//        series.add(2, 5);
//        series.add(7, 8);
//        series.add(9, 10);
        dataset.addSeries(t1);
//
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Progression des épargnes pour ",
                "Mois",
                "Montant",
                dataset, 
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
        
     return(chart);

  //  Container contenu = getContentPane() ;
   // contenu.add(panel);
        
    } 
    
    
   private void reCalcurate(TableModel ml) {
    if (ml == null)
      return;
    double total = 0.0;
    for (int i = 0; i < totalrow; i++) {
   //   total += ((Double) ml.getValueAt(i, totalrow)).doubleValue();
        total += ((Double) jTable1.getValueAt(i, totalrow)).doubleValue();
    }

    jTable1.setValueAt(new Double(total), totalrow, TOTAL_COLUMN);
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDBCLoginService1 = new org.jdesktop.swingx.auth.JDBCLoginService();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel11 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel12 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel13 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel14 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel15 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jPanel16 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel3 = new ChartPanel(getChart());
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jLabel3 = new javax.swing.JLabel();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/synthèse.png"))); // NOI18N

        jTabbedPane1.setName(""); // NOI18N
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setVerifyInputWhenFocusTarget(false);

        conn = Connect.ConnectDb();
        ResultSet rs0 = null;
        try {
            pre = conn.prepareStatement("SELECT MIN(DateEpargne), MAX(DateEpargne) FROM Epargne");
            rs0 = pre.executeQuery();
            while (rs0.next()) {
                startDate=rs0.getDate(1);

                IntializeDate dateini = new IntializeDate();
                if (dateini.change) {
                    System.out.println("yeah changed");
                    if (dateini.initdate.after(startDate)) startDate = initdate;

                }
                //endDate=rs0.getDate(2);

                endDate = new Date();
                if (endDate.before(rs0.getDate(2))) endDate = rs0.getDate(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
        }   finally {

            try {
                if (rs0 !=null)
                rs0.close();

                if (pre !=null)
                pre.close();

                if (conn !=null)
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            // TODO add your handling code here:
            data=getSynthese();
        } catch (Exception ex) {
            Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[][] out = to2DimArray(data);

        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable4.setFillsViewportHeight(true);
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            out,
            new String [] {
                "Mois", "FTC", "Epargne", "Retraits", "Solde"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            Class[] types = {Date.class, Double.class, Double.class,
                Double.class, Double.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }

            // nouveau ajout
            // public void setValueAt(Object value, int row, int col) {
                //     Vector rowVector = (Vector) dataVector.elementAt(row);
                //     if (col == TOTAL_COLUMN) {
                    //          Double d = null;
                    //         if (value instanceof Double) {
                        //            d = (Double) value;
                        //         } else {
                        //            try {
                            //                 d = new Double(((Number) formatter
                                //                     .parse((String) value)).doubleValue());
                        //         } catch (ParseException ex) {
                        //            d = new Double(0.0);
                        //         }
                    //    }
                //      rowVector.setElementAt(d, col);
                //  } else {
                //      rowVector.setElementAt(value, col);
                //  }}

    });
    //sorter
    sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable4.getModel());
    jTable4.setRowSorter(sorter);

    //jTable1.getModel().addTableModelListener(new TableModelListener() {

        //     public void tableChanged(TableModelEvent e) {
            //          totalrow= jTable1.getRowCount() -1;
            //          reCalcurate(jTable1.getModel());
            //      }
        //    });

TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

    SimpleDateFormat f = new SimpleDateFormat("MMM yyyy");
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
        if(value instanceof Date) {
            value = f.format(value);
        }else {
            value = String.format("%.0f", value);
        }
        if (column == 0 && row == 12) { value = "Total";}
        if (column == 0 && jComboBox1.getSelectedIndex()== (jComboBox1.getItemCount()-1) && row == (jTable4.getRowCount()-1)) { value = "Total";}
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row == 12 || ( jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == (jTable4.getRowCount()-1))) {c.setBackground(LightYellow);} else {
            c.setBackground(Color.WHITE);
        }

        if(isSelected) {
            c.setForeground(Color.BLACK);
        }
        //if (row == 12) { jTable1. }
        //   if (jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == jTable1.getRowCount() -1)  setBackground(Color.GRAY);
        //   return super.getTableCellRendererComponent(table, value, isSelected,
            //           hasFocus, row, column);

        return c;
    }
    };

    TableCellRenderer tableCellRenderer4 = new DefaultTableCellRenderer() {

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

            value =  String.format("%.0f", value);
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row == 12 || ( jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == jTable4.getRowCount() -1)) {c.setBackground(LightYellow);} else {
                c.setBackground(Color.WHITE);
            }

            if(isSelected){
                c.setForeground(Color.BLACK);
            }
            //if (row == 12) { jTable1. }
            //   if (jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == jTable1.getRowCount() -1)  setBackground(Color.GRAY);
            //   return super.getTableCellRendererComponent(table, value, isSelected,
                //           hasFocus, row, column);

            return c;
        }
    };

    jTable4.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);
    jTable4.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer4);
    jTable4.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer4);
    jTable4.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer4);
    jTable4.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer4);
    jTable4.setSelectionBackground(Color.BLUE);
    jScrollPane4.setViewportView(jTable4);

    jLabel6.setText("Choix de l'année:");

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Exercice 2011", "Exercice 2012", "Exercice 2013", "Exercice 2014" }));
    int minyear=0;
    int maxyear=0;
    conn = Connect.ConnectDb();
    ResultSet rs = null;
    try {
        pre = conn.prepareStatement("SELECT MIN(YEAR(DateEpargne)) as minyear, MAX(YEAR(DateEpargne)) as maxyear FROM Epargne");

        rs = pre.executeQuery();
        while (rs.next()) {
            //   minyear=rs.getInt(1);

            maxyear=Calendar.getInstance().get(Calendar.YEAR);  // méthode à revoir

            // the date instance
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            minyear = calendar.get(Calendar.YEAR);
            if(maxyear < rs.getInt(2)) maxyear=rs.getInt(2);
        }
    } catch (SQLException ex) {
        Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
    }   finally {

        try {
            if (rs !=null)
            rs.close();

            if (pre !=null)
            pre.close();

            if (conn !=null)
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Connect.close(conn, rs, pre);

    //Determine boundaries
    conn = Connect.ConnectDb();
    try {
        pre = conn.prepareStatement("SELECT MIN(DateEpargne) FROM Epargne");
        rs = pre.executeQuery();
        while (rs.next()) {
            SimpleDateFormat simplefm = new SimpleDateFormat("MM");
            // Date minDate=(rs.getDate(1));

            Date minDate=startDate;
            originmonth= Integer.valueOf(simplefm.format(minDate)).intValue();

        }
    } catch (SQLException ex) {
        Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
    }   finally {

        try {
            if (rs !=null)
            rs.close();

            if (pre !=null)
            pre.close();

            if (conn !=null)
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    if (minyear !=0 && maxyear !=0){
        jComboBox1.removeAllItems();
        for (int i=minyear; i<=maxyear; i++) {
            jComboBox1.addItem("Exercice "+i);
        }
    }
    jComboBox1.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jComboBox1ItemStateChanged(evt);
        }
    });

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel1Layout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(jLabel6)
                    .add(18, 18, 18)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 269, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 769, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(32, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel1Layout.createSequentialGroup()
            .add(12, 12, 12)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel6)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(18, 18, 18)
            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 275, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(64, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("Bilan annuel", jPanel1);

    jPanel2.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            jPanel2FocusGained(evt);
        }
    });

    jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
    jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            jTabbedPane2StateChanged(evt);
        }
    });

    //jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable2.setFillsViewportHeight(true);
    jTable2.setPreferredScrollableViewportSize(new Dimension(1400,70));
    jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    jTable2.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable2.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(2).setPreferredWidth(125);
    jTable2.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable2.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable2.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable2.getColumnModel().getColumn(7).setPreferredWidth(130);
    jTable2.getColumnModel().getColumn(8).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(9).setPreferredWidth(90);

    //jTable2.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable2.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable2.getColumnModel().getColumn(10).setPreferredWidth(0);

    //jTable2.getColumnModel().getColumn(8).setMaxWidth(0);
    //jTable2.getColumnModel().getColumn(8).setMinWidth(0);
    //jTable2.getColumnModel().getColumn(8).setWidth(0);

    //jTable2.getColumnModel().getColumn(8).setMaxWidth(0);
    //jTable2.getColumnModel().getColumn(8).setMinWidth(0);
    //jTable2.getColumnModel().getColumn(8).setWidth(0);
    //jTable2.getColumnModel().getColumn(9).setMaxWidth(0);
    //jTable2.getColumnModel().getColumn(9).setMinWidth(0);
    //jTable2.getColumnModel().getColumn(9).setWidth(0);
    jScrollPane2= new FrozenTablePane(jTable2, 3);
    jScrollPane2.setViewportView(jTable2);

    org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("JAN", jPanel4);

    jTable3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable3.setFillsViewportHeight(true);
    jTable3.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable3.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable3.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable3.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable3.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable3.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable3.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable3.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable3.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable3.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable3.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable3.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable3.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable3.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable3.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable3.getColumnModel().getColumn(8).setMinWidth(0);
    jTable3.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane3= new FrozenTablePane(jTable3, 3);
    jScrollPane3.setViewportView(jTable3);

    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("FEV", jPanel5);

    jTable5.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable5.setFillsViewportHeight(true);
    jTable5.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable5.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable5.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable5.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable5.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable5.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable5.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable5.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable5.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable5.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable5.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable5.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable5.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable5.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable5.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable5.getColumnModel().getColumn(8).setMinWidth(0);
    jTable5.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane5= new FrozenTablePane(jTable5, 3);
    jScrollPane5.setViewportView(jTable5);

    org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("MAR", jPanel7);

    jTable6.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable6.setFillsViewportHeight(true);
    jTable6.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable6.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable6.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable6.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable6.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable6.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable6.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable6.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable6.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable6.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable6.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable6.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable6.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable6.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable6.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable6.getColumnModel().getColumn(8).setMinWidth(0);
    jTable6.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane6= new FrozenTablePane(jTable6, 3);
    jScrollPane6.setViewportView(jTable6);

    org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("AVR", jPanel8);

    jTable7.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable7.setFillsViewportHeight(true);
    jTable7.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable7.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable7.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable7.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable7.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable7.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable7.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable7.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable7.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable7.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable7.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable7.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable7.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable7.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable7.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable7.getColumnModel().getColumn(8).setMinWidth(0);
    jTable7.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane7= new FrozenTablePane(jTable7, 3);
    jScrollPane7.setViewportView(jTable7);

    org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("MAI", jPanel9);

    jTable8.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable8.setFillsViewportHeight(true);
    jTable8.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable8.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable8.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable8.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable8.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable8.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable8.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable8.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable8.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable8.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable8.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable8.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable8.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable8.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable8.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable8.getColumnModel().getColumn(8).setMinWidth(0);
    jTable8.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane8= new FrozenTablePane(jTable8, 3);
    jScrollPane8.setViewportView(jTable8);

    org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("JUIN", jPanel10);

    jTable9.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable9.setFillsViewportHeight(true);
    jTable9.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable9.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable9.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable9.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable9.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable9.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable9.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable9.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable9.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable9.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable9.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable9.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable9.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable9.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable9.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable9.getColumnModel().getColumn(8).setMinWidth(0);
    jTable9.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane9= new FrozenTablePane(jTable9, 3);
    jScrollPane9.setViewportView(jTable9);

    org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("JUIL", jPanel11);

    jTable10.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable10.setFillsViewportHeight(true);
    jTable10.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable10.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable10.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable10.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable10.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable10.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable10.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable10.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable10.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable10.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable10.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable10.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable10.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable10.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable10.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable10.getColumnModel().getColumn(8).setMinWidth(0);
    jTable10.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane10= new FrozenTablePane(jTable10, 3);
    jScrollPane10.setViewportView(jTable10);

    org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
        jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel12Layout.setVerticalGroup(
        jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("AOU", jPanel12);

    jTable11.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable11.setFillsViewportHeight(true);
    jTable11.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable11.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable11.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable11.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable11.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable11.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable11.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable11.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable11.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable11.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable11.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable11.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable11.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable11.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable11.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable11.getColumnModel().getColumn(8).setMinWidth(0);
    jTable11.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane11= new FrozenTablePane(jTable11, 3);
    jScrollPane11.setViewportView(jTable11);

    org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
        jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel13Layout.setVerticalGroup(
        jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("SEP", jPanel13);

    jTable12.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable12.setFillsViewportHeight(true);
    jTable12.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable12.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable12.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable12.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable12.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable12.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable12.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable12.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable12.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable12.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable12.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable12.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable12.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable12.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable12.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable12.getColumnModel().getColumn(8).setMinWidth(0);
    jTable12.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane12= new FrozenTablePane(jTable12, 3);
    jScrollPane12.setViewportView(jTable12);

    org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("OCT", jPanel14);

    jTable13.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable13.setFillsViewportHeight(true);
    jTable13.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable13.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable13.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable13.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable13.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable13.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable13.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable13.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable13.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable13.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable13.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable13.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable13.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable13.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable13.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable13.getColumnModel().getColumn(8).setMinWidth(0);
    jTable13.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane13= new FrozenTablePane(jTable13, 3);
    jScrollPane13.setViewportView(jTable13);

    org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
    jPanel15.setLayout(jPanel15Layout);
    jPanel15Layout.setHorizontalGroup(
        jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel15Layout.setVerticalGroup(
        jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("NOV", jPanel15);

    jTable14.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable14.setFillsViewportHeight(true);
    jTable14.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable14.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "FTC", "Total cotisé", "Retrait", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable14.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable14.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable14.getColumnModel().getColumn(2).setPreferredWidth(250);
    jTable14.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable14.getColumnModel().getColumn(4).setPreferredWidth(130);
    jTable14.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable14.getColumnModel().getColumn(6).setPreferredWidth(120);
    jTable14.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable14.getColumnModel().getColumn(8).setPreferredWidth(0);
    //jTable14.getColumnModel().getColumn(9).setPreferredWidth(0);
    //jTable14.getColumnModel().getColumn(10).setPreferredWidth(0);
    jTable14.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable14.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable14.getColumnModel().getColumn(8).setMinWidth(0);
    jTable14.getColumnModel().getColumn(9).setMinWidth(0);
    jScrollPane14= new FrozenTablePane(jTable14, 3);
    jScrollPane14.setViewportView(jTable14);
    SimpleDateFormat fm = new SimpleDateFormat("MM");

    org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
    jPanel16.setLayout(jPanel16Layout);
    jPanel16Layout.setHorizontalGroup(
        jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
    );
    jPanel16Layout.setVerticalGroup(
        jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("DEC", jPanel16);

    jLabel7.setText("Année :");

    jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    JPanel jPanel5 = new JPanel();
    //jTabbedPane2.add("tab 2", jPanel5);
    if (minyear !=0 && maxyear !=0){
        jComboBox2.removeAllItems();
        for (int i=minyear; i<=maxyear; i++) {
            jComboBox2.addItem("Exercice "+i);
        }
    }
    //jTabbedPane2.add("tab 3", jPanel4);
    //jTabbedPane2.add("tab 4", jPanel4);
    //jTabbedPane2.add("tab 5", jPanel4);
    jComboBox2.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jComboBox2ItemStateChanged(evt);
        }
    });

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, jTabbedPane2)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(jLabel7)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 256, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel7)
                .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 352, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    jTabbedPane2.setSelectedIndex(Integer.valueOf(fm.format(today)).intValue()-1);
    jComboBox2.setSelectedIndex(0);
    jComboBox2.setSelectedIndex(jComboBox2.getItemCount()-1);

    jTabbedPane1.addTab("Versements", jPanel2);

    //if (jComboBox1.getSelectedIndex() != -1) setChart((ChartPanel) jPanel3);
    jComboBox1.setSelectedIndex(jComboBox1.getItemCount()-1);

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 814, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 397, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("Progression annuelle", jPanel3);

    jPanel6.setVerifyInputWhenFocusTarget(false);

    jTable1.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
    header.add("Id");
    header.add("Date");
    header.add("Motif");
    header.add("Epargnant");
    header.add("Montant");

    //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable1.setFillsViewportHeight(true);
    jTable1.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable1.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
        },
        new String [] {
            "Id", "Date", "Motif", "Epargnant", "Montant"
        }
    ));
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(185);
    jTable1.getColumnModel().getColumn(3).setPreferredWidth(230);
    jTable1.getColumnModel().getColumn(4).setPreferredWidth(150);
    jTable1.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
    jScrollPane1.setViewportView(jTable1);

    jLabel2.setText("Date début: ");

    demoDateField1.setYearDigitsAmount(4);

    jLabel3.setText("Date fin:");

    demoDateField2.setYearDigitsAmount(4);

    jButton1.setText("Valider");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jLabel4.setText("Total:");

    org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel2))
            .add(43, 43, 43)
            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(demoDateField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .add(demoDateField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(159, 159, 159)
            .add(jLabel4)
            .add(63, 63, 63)
            .add(jLabel5)
            .addContainerGap(230, Short.MAX_VALUE))
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jButton1)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(9, 9, 9)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 793, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel6Layout.createSequentialGroup()
            .add(26, 26, 26)
            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel2)
                .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jLabel5)))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel3))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 263, Short.MAX_VALUE)
            .add(jButton1)
            .addContainerGap())
        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(101, 101, 101)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .add(53, 53, 53)))
    );

    jTabbedPane1.addTab("Historique des mouvements", jPanel6);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jTabbedPane1)
                .addContainerGap()))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(447, Short.MAX_VALUE))
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(65, 65, 65)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap()))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        try {
//            // TODO add your handling code here:
//            data2=getMembres();
//        } catch (Exception ex) {
//            Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
       
        //header.add("Identifiant");
        //header.add("Date");
        //header.add("Motif");
        //header.add("Epargnant");
        //header.add("Montant");
        
          Vector<String> header = new Vector<String>();
        header.add("ID");
        header.add("Date");
        header.add("Motif");
        header.add("Epargnant");
        header.add("Montant");
         
                if (demoDateField1.getDate() == null || demoDateField2.getDate() == null) {
                     JOptionPane.showMessageDialog(this, "Veuillez renseigner des dates valides");

                } else {
        try {
            // TODO add your handling code here:
            data2=getMembres();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //jTable1.setModel(new javax.swing.table.DefaultTableModel(
            //            data,header
            //        ));

    // new javax.swing.table.DefaultTableModel(
        //    data,header
   //      System.out.println("data"+ data2);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(data2,header){

            Class[] types = {Integer.class, Date.class, String.class,
                String.class,  Integer.class  };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            
               boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false
           };
               
     @Override
     public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
               
               
        });
jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
jTable1.setFillsViewportHeight(true);
jTable1.setPreferredScrollableViewportSize(new Dimension(1000,70));
jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
jTable1.getColumnModel().getColumn(2).setPreferredWidth(185);
jTable1.getColumnModel().getColumn(3).setPreferredWidth(230);
jTable1.getColumnModel().getColumn(4).setPreferredWidth(200);
TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if( value instanceof Date) {
            value = f.format(value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
    }
};
DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
leftRenderer.setHorizontalAlignment(JLabel.LEFT);
jTable1.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);
jTable1.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
jScrollPane1.setViewportView(jTable1);
 NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("fr", "TG"));
 jLabel5.setText(formatter.format((sumepPlage)));
                }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
       
        
        RowFilter<DefaultTableModel, Object> rf  = RowFilter.regexFilter(((String)jComboBox1.getSelectedItem().toString()).substring(jComboBox1.getSelectedItem().toString().lastIndexOf(" ")+1), 0);
        sorter.setRowFilter(rf);
        
        // calculates sums 
        
        Double ftcsum = 0.0;
        Double eparsum = 0.0;
        Double retrsum = 0.0;
        Double soldesum = 0.0;
     //   System.out.println("jTabe row count"+ jTable4.getRowCount());
        for (int i=0; i < jTable4.getRowCount() - 1;  i++ ){
            ftcsum = ftcsum+ (double) jTable4.getValueAt(i, 1);
           
            
        
            
        }
        
         for (int i=0; i < jTable4.getRowCount() - 1;  i++ ){
            eparsum = eparsum+ (double) jTable4.getValueAt(i, 2);
           
            
        
            
        }
        
         for (int i=0; i < jTable4.getRowCount() - 1;  i++ ){
            retrsum = retrsum + (double) jTable4.getValueAt(i, 3);
           
            
        
            
        }
         
         for (int i=0; i < jTable4.getRowCount() - 1;  i++ ){
            soldesum = soldesum + (double) jTable4.getValueAt(i, 4);
           
            
        
            
        }
         
        jTable4.setValueAt(ftcsum, jTable4.getRowCount()-1, 1);
        jTable4.setValueAt(eparsum, jTable4.getRowCount()-1, 2);
        jTable4.setValueAt(retrsum, jTable4.getRowCount()-1, 3);
        jTable4.setValueAt(soldesum, jTable4.getRowCount()-1, 4);
        
      
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jPanel2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel2FocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanel2FocusGained
      
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        JTabbedPane sourceTabbedPane = (JTabbedPane) evt.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        if(index==2 && jComboBox1.getSelectedIndex() != -1) {
            setChart((ChartPanel) jPanel3);
        }
        
        

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
         if(jComboBox2.getSelectedIndex()==0) {
            for (int i=0; i<originmonth-1; i++) {
         //       System.out.println("i"+i);
                jTabbedPane2.setEnabledAt(i, false);
            }
            
            for (int i=originmonth-1; i<12; i++){
                jTabbedPane2.setEnabledAt(i, true);
            }
            
            if(jTabbedPane2.getSelectedIndex()< originmonth-1) jTabbedPane2.setSelectedIndex(originmonth-1);
                    
            
        } 
        
        if (jComboBox2.getSelectedIndex()==(jComboBox2.getItemCount()-1)) {  // Si c'est le dernier
          //  System.out.println("it is true");
            SimpleDateFormat fm = new SimpleDateFormat("MM");
            int nowmonth= Integer.valueOf(fm.format(today)).intValue();
             for (int i=nowmonth; i<12; i++){
                jTabbedPane2.setEnabledAt(i, false);
           //      System.out.println("setting false"+i);
            }
             
            for (int i=0; i<nowmonth; i++){
                jTabbedPane2.setEnabledAt(i, true);
            }
            
            if(jTabbedPane2.getSelectedIndex() >= nowmonth) jTabbedPane2.setSelectedIndex(nowmonth-1);
        }
        
        if (jComboBox2.getSelectedIndex()!=0 && jComboBox2.getSelectedIndex()!=(jComboBox2.getItemCount()-1)){
            for (int i=0; i<12; i++) {
                jTabbedPane2.setEnabledAt(i, true);
               
            }
        }
        
        // update table 
          if(jComboBox2.getSelectedItem() != null &&  visitedmth[jTabbedPane2.getSelectedIndex()] != Integer.valueOf(((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length()-4, ((String) jComboBox2.getSelectedItem()).length()))) {
              try {
              //  System.out.println("filling");
                fillmonthsummtable(Integer.valueOf((((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length()-4, ((String) jComboBox2.getSelectedItem()).length()))).intValue(), jTabbedPane2.getSelectedIndex()+1);
            } catch (ParseException ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            }
             
          }
          
          
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        // TODO add your handling code here:
        if(jComboBox2.getSelectedItem() !=null &&  visitedmth[jTabbedPane2.getSelectedIndex()] != Integer.valueOf(((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length()-4, ((String) jComboBox2.getSelectedItem()).length())))  {
            
            try {
              //  System.out.println("filling");
                fillmonthsummtable(Integer.valueOf((((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length()-4, ((String) jComboBox2.getSelectedItem()).length()))).intValue(), jTabbedPane2.getSelectedIndex()+1);
            } catch (ParseException ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
       
        
  //  if(jComboBox2.getSelectedItem() !=null) System.out.println("value string" + (((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length()-4, ((String) jComboBox2.getSelectedItem()).length())));
    }//GEN-LAST:event_jTabbedPane2StateChanged
    public String getNamefromId(int id, String type) throws SQLException{
        if(type.equalsIgnoreCase("Enfant")){
             String nom="";
             PreparedStatement pre;
             pre = conn.prepareStatement("SELECT Nom, Prenoms FROM Profil_enfant WHERE idProfil_enfant='"+id+"';");
             ResultSet rs = pre.executeQuery();
              while(rs.next()) {
                nom =rs.getString("Nom")+","+rs.getString("Prenoms");
              }
              return nom;
        } else if ((type.equalsIgnoreCase("Adulte"))){ 
            String nom="";
             PreparedStatement pre;
             pre = conn.prepareStatement("SELECT Noms, Prenoms FROM Profil_adulte WHERE idProfil_adulte='"+id+"';");
             ResultSet rs = pre.executeQuery();
              while(rs.next()) {
                nom =rs.getString("Noms")+","+rs.getString("Prenoms");
              }
              return nom;
            
        } else {
            String nom="";
             PreparedStatement pre;
             pre = conn.prepareStatement("SELECT Raison_sociale FROM Profil_persmorale WHERE idProfil_persmorale='"+id+"';");
             ResultSet rs = pre.executeQuery();
              while(rs.next()) {
                nom =rs.getString("Raison_sociale");
              }
              return nom;
            
        }
            
    }
    
    public  Vector  getMembres() throws Exception {
    conn = Connect.ConnectDb();
    int i=1;
    sumepPlage =0.0;
    java.sql.Date sqlDate1=new java.sql.Date(demoDateField1.getDate().getTime());
    java.sql.Date sqlDate2=new java.sql.Date(demoDateField2.getDate().getTime());
    PreparedStatement pre;
      //  System.out.println("requête"+"SELECT * FROM Epargne WHERE DateEpargne between '"+sqlDate1+"' AND '"+sqlDate2+ "';");
    pre = conn.prepareStatement("SELECT * FROM Epargne WHERE DateEpargne between '"+sqlDate1+"' AND '"+sqlDate2+ "';");
    ResultSet rs = pre.executeQuery();
   // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();
    Vector<Vector> membreVector = new Vector<Vector>();
    while(rs.next()) {
     //   Vector<String> membre = new Vector<String>();
     //   membre.add(String.valueOf(i)); 
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getDate("DateEpargne")); 
        membre.add(rs.getString("MotifEpargne")); 
        String type=rs.getString("TypeEpargnant");
        int id= rs.getInt("IdEpargnant");
        String epargnant=getNamefromId(id, type);
        membre.add(epargnant);
        membre.add(String.format("%,d", rs.getInt("MontantEpargne")));
        sumepPlage = sumepPlage + rs.getInt("MontantEpargne");

   //
        membreVector.add(membre);
        i++;
    }
    
   
   

/*Close the connection after use (MUST)*/
    if(conn!=null)
        conn.close();

    return membreVector;
}
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
            java.util.logging.Logger.getLogger(Epargneview2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Epargneview2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Epargneview2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Epargneview2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Epargneview2().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private org.jdesktop.swingx.auth.JDBCLoginService jDBCLoginService1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    // End of variables declaration//GEN-END:variables

}
