/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.Component;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.Query.value;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.apache.commons.lang.time.DateUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author elommarcarnold
 */
public class Compta_mutuelle extends javax.swing.JFrame {
    private Connection conn;
    private PreparedStatement pre;
    private Map <String, ArrayList<Date>> ftchash;
    private int  FRAISADHEP = 2500;  // to get
    private int  FRAISADHTONT = 500;   // to get
    private Detailcompt det;
    private Double totalentrees;
    private Double totalsorties;
    private Map <String, String> automint;
    private Map <String, String> libreint;
    private Map <String, String> termeint;
    IntializeDate dateini;
    /** Creates new form Compta_mutuelle */
    public Compta_mutuelle() throws Exception {
        initComponents();
        dateini = new IntializeDate();
        IntializeDate dateini = new IntializeDate();
        ftchash= new HashMap<String, ArrayList<Date>>();
        automint = new HashMap<String, String>();
        termeint = new HashMap<String, String>();
        libreint = new HashMap<String, String>();
        buildftchash();
        System.out.println("ftcsum" + calculateftcsumm(2016,7,2018,7));
        System.out.println("openfees" +calculateopenfees(2016,7,2018,7));
        System.out.println("partsociale" +calculateSocialPart(2016,7,2018,7));
        System.out.println("commission tontine" +commissiontontine(2018,1,2018,1));
        System.out.println("Frais d'adhesion tontine" +calculateopenfeestontine(2010,7,2018,7));
        System.out.println("getMinDate"+getMinDate());
//        // interets 
//        System.out.println("interets autom" +calculateinterestautom(2010,7,1,2020,7,1));
//        System.out.println("interets libre" +calculateinterestautom(2010,7,1,2020,7,1));
//        System.out.println("interets terme" +calculateinterestautom(2010,7,1,2020,7,1));
//        // penalites 
//         System.out.println("penalites autom" +calculatepenaliteautom(2010,7,1,2020,7,1));
//        System.out.println("penalites libre" +calculatepenalitelibre(2010,7,1,2020,7,1));
//        System.out.println("penalites terme" +calculatepenaliteterme(2010,7,1,2020,7,1));
        
        // caution
        System.out.println("caution" + String.format("%.12f", calculatecaution(2010,7,1,2020,7,1)));
        // Frais suivi
        System.out.println("frais suivi" + String.format("%.12f", calculatefraissuivi(2010,7,1,2020,7,1)));

        // Frais dossier
        System.out.println("frais dossier" + String.format("%.12f", calculatefraisdossier(2010,7,1,2020,7,1)));

        
        Date minDate = getMinDate();
        demoDateField1.setDate(minDate);
        demoDateField2.setDate(new Date());
        
        det= new Detailcompt(minDate, new Date());
        
        
}
        
    public class MyRenderer extends JLabel implements TableCellRenderer {
   public Component getTableCellRendererComponent(JTable table,
      Object value, boolean isSelected, boolean hasFocus, int row,
      int col) {
      DefaultTableCellRenderer renderer =
         new DefaultTableCellRenderer();
      Component c = renderer.getTableCellRendererComponent(table,
         value, isSelected, hasFocus, row, col);
      String s = "";
         DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
         Double d;
         if (value.getClass() == Integer.class) d = new Double((Integer)value);
         else d = (Double) value;
         s = formatter.format(d);
         c = renderer.getTableCellRendererComponent(table, s,
            isSelected, hasFocus, row, col);
          ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
      
      return c;
   }
}
    
    
        public Double getEpargneplus(Date beginDate, Date stopDate) throws SQLException {
        conn= Connect.ConnectDb();
        Double result=0.0;
        String sql = "SELECT SUM(MontantEpargne) FROM Epargne WHERE DateEpargne >='"+new java.sql.Date(beginDate.getTime())+"' AND DateEpargne <='"+new java.sql.Date(stopDate.getTime())+"' AND MontantEpargne >= 0";
                
        if (dateini.change) sql = sql + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        
        PreparedStatement pre1=conn.prepareStatement(sql);
        ResultSet rs1 = pre1.executeQuery();
        while (rs1.next()) {
           result = rs1.getDouble(1);
        }
        
        if (conn != null) conn.close();
        if (pre1 != null) pre1.close();
        if (rs1 != null) rs1.close();
        
        return result;
    }
        
        public Double getEpargnemns(Date beginDate, Date stopDate) throws SQLException {
        conn= Connect.ConnectDb();
        Double result=0.0;
        String sql= "SELECT SUM(MontantEpargne) FROM Epargne WHERE DateEpargne >='"+new java.sql.Date(beginDate.getTime())+"' AND DateEpargne <='"+new java.sql.Date(stopDate.getTime())+"' AND MontantEpargne <= 0"
                + ""; 
        if (dateini.change) sql = sql + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

        PreparedStatement pre1=conn.prepareStatement(sql);
        ResultSet rs1 = pre1.executeQuery();
        while (rs1.next()) {
           result = rs1.getDouble(1);
        }
        
        if (conn != null) conn.close();
        if (pre1 != null) pre1.close();
        if (rs1 != null) rs1.close();
        
        return result;
    }
     
     
    public Double getTdepontine(Date beginDate, Date endDate) throws SQLException {
        Double somm =0.0;
        conn = Connect.ConnectDb2();    
        PreparedStatement pre;
        
        String sql = "SELECT SUM((bit_count(JoursTontine))*Mise) FROM Tontine  WHERE (bit_count(JoursTontine)) >=1 AND DateTontine >='" + new java.sql.Date(beginDate.getTime()) + "' AND DateTontine <='"+ new java.sql.Date(endDate.getTime()) +"'";
        if (dateini.change) sql = sql + " AND DateTontine  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

        
        pre = conn.prepareStatement(sql);
        ResultSet rs1 = pre.executeQuery();
        
        while (rs1.next()) {
            somm = rs1.getDouble(1);
                                
        } 
        
        if (conn != null) conn.close();
        if (pre != null) pre.close();
        if (rs1 != null) rs1.close();
        return somm;
        
    }
        
    public Double getRetrtontine(Date beginDate, Date endDate) throws SQLException {
        Double somme =0.0;
        
        conn = Connect.ConnectDb2(); 
        String sql = "SELECT SUM(Montant) FROM retraits_tontine WHERE DateRet >='" + new java.sql.Date(beginDate.getTime()) +"' AND DateRet <= '"+new java.sql.Date(endDate.getTime()) +"'";
        if (dateini.change) sql = sql + " AND DateRet  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

        pre = conn.prepareStatement(sql);
        ResultSet rs1;
        rs1 = pre.executeQuery();
        
        
        while (rs1.next()) {
           somme = rs1.getDouble(1);
        }
        
        if (conn != null) conn.close();
        if (pre != null) pre.close();
        if (rs1 != null) rs1.close();
        
        return -somme;
    }     
    
    private void buildepargxyseries(XYSeries a) throws SQLException {
        
     
        a.add(0, getTotalEp(2013));
         a.add(1, getTotalEp(2014));
          a.add(2, getTotalEp(2015));
           a.add(3, getTotalEp(2016));
            a.add(4, getTotalEp(2017));
         a.add(5, getTotalEp(2018));
        
    }
    
     private void buildTontgxyseries (XYSeries a) throws SQLException {
      
           a.add(0, getTotalTont(2013));
           a.add(1, getTotalTont(2014));
           a.add(2, getTotalTont(2015));
           a.add(3, getTotalTont(2016));
           a.add(4, getTotalTont(2017));
           a.add(5, getTotalTont(2018));
        
    }
     
     
    public void getEpargCountValueOrdered(HashMap<String, Double> mp) {
        
    } 
    
    public Double getTotalEp (int year) throws SQLException{
        
        String sql = "SELECT SUM(MontantEpargne) FROM Epargne WHERE YEAR(DateEpargne)='"+year+"'";
        conn=Connect.ConnectDb();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        Double sum=0.0;
        
        while(rs.next()) {
          sum =rs.getDouble(1);
            System.out.println("sum vaut"+sum);
        }
        
        return sum;
    }
    
    public Double getTotalTont (int year) throws SQLException{
        
        String sql = "SELECT SUM(bit_count(JoursTontine)*Mise) FROM Tontine WHERE YEAR(DateTontine)='"+year+"'";
        conn= Connect.ConnectDb();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        Double sum=0.0;
        
        while(rs.next()) {
          sum =rs.getDouble(1);
            System.out.println("sum tont  vaut"+sum);
        }
        
        return sum;
    }

    public int getnbfraisadhEp (Date a, Date b) throws SQLException{
        Date zerodatea;
        Date zerodateb;
        Calendar cdatea = new GregorianCalendar();
        cdatea.setTime(a);
        cdatea.set(Calendar.MILLISECOND, 0);
        cdatea.set(Calendar.SECOND, 0); 
        cdatea.set(Calendar.MINUTE, 0);
        cdatea.set(Calendar.HOUR_OF_DAY, 0);
        cdatea.set(Calendar.DAY_OF_MONTH,1);
        zerodatea= cdatea.getTime();
        
        Calendar cdateb = new GregorianCalendar();
        cdateb.setTime(b);
        cdateb.set(Calendar.MILLISECOND, 0);
        cdateb.set(Calendar.SECOND, 0); 
        cdateb.set(Calendar.MINUTE, 0);
        cdateb.set(Calendar.HOUR_OF_DAY, 0);
        cdateb.set(Calendar.DAY_OF_MONTH,1);
        zerodateb= cdateb.getTime();
        
        String sql="SELECT COUNT(*) FROM (" + "(SELECT idProfil_adulte as 'id' FROM Profil_adulte  WHERE Date_adhesion_ep >='"+new java.sql.Date(zerodatea.getTime())+"' AND Date_adhesion_ep <= '"+new java.sql.Date(zerodateb.getTime())+"' ";
        if (dateini.change) sql = sql + " AND Date_adhesion_ep  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        sql = sql + ") "+ " UNION ALL"+ "(SELECT idProfil_enfant as 'id' FROM Profil_enfant  WHERE Date_adhesion_ep >='"+new java.sql.Date(zerodatea.getTime())+"' AND Date_adhesion_ep <= '"+new java.sql.Date(zerodateb.getTime())+"' ";
        
        if (dateini.change) sql = sql + " AND Date_adhesion_ep  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' "
        + ") "+ " UNION ALL"+ " (SELECT idProfil_persmorale as 'id' FROM Profil_persmorale  WHERE Date_adhesion_ep >='"+new java.sql.Date(zerodatea.getTime())+"' AND Date_adhesion_ep <= '"+new java.sql.Date(zerodateb.getTime())+"' ";
        
        if (dateini.change) sql = sql + " AND Date_adhesion_ep  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' "
        + ") ";
        
       sql = sql+  ") AS tble1";
        
        
        System.out.println("printed sql getNbfraisepar"+sql);
        conn=Connect.ConnectDb();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        int sum=0;
        while (rs.next()) {
              sum= rs.getInt(1);
         }
          
         conn.close();
         pre.close();
         rs.close();

         return sum;
        
        
    }
    
    public int getnbfraisadhTon (Date a, Date b) throws SQLException{
        Date zerodatea;
        Date zerodateb;
        Calendar cdatea = new GregorianCalendar();
        cdatea.setTime(a);
        cdatea.set(Calendar.MILLISECOND, 0);
        cdatea.set(Calendar.SECOND, 0); 
        cdatea.set(Calendar.MINUTE, 0);
        cdatea.set(Calendar.HOUR_OF_DAY, 0);
        cdatea.set(Calendar.DAY_OF_MONTH,1);
        zerodatea= cdatea.getTime();
        
        Calendar cdateb = new GregorianCalendar();
        cdateb.setTime(b);
        cdateb.set(Calendar.MILLISECOND, 0);
        cdateb.set(Calendar.SECOND, 0); 
        cdateb.set(Calendar.MINUTE, 0);
        cdateb.set(Calendar.HOUR_OF_DAY, 0);
        cdateb.set(Calendar.DAY_OF_MONTH,1);
        zerodateb= cdateb.getTime();
        
      
        
        
         String sql="SELECT COUNT(*) FROM (" + "(SELECT idProfil_adulte as 'id' FROM Profil_adulte  WHERE Type_adhesion LIKE '"+"%Tontine%'"+ " AND Date_adhesion_to >='"+new java.sql.Date(zerodatea.getTime())+"' AND Date_adhesion_to <= '"+new java.sql.Date(zerodateb.getTime())+"' ";
         if (dateini.change) sql = sql + " AND Date_adhesion_to  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
         sql = sql+ ")"+ " UNION ALL"+"(SELECT idProfil_enfant as 'id' FROM Profil_enfant  WHERE Type_adhesion LIKE '"+"%Tontine%'"+ " AND Date_adhesion_to >='"+new java.sql.Date(zerodatea.getTime())+"' AND Date_adhesion_to <= '"+new java.sql.Date(zerodateb.getTime())+"'"; 
         if (dateini.change) sql = sql + " AND Date_adhesion_to  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        
         sql= sql + ")"+ " UNION ALL"+ " (SELECT idProfil_persmorale as 'id' FROM Profil_persmorale  WHERE Type_adhesion LIKE '"+"%Tontine%'"+ " AND Date_adhesion_to >='"+new java.sql.Date(zerodatea.getTime())+"' AND Date_adhesion_to <= '"+new java.sql.Date(zerodateb.getTime())+"'";
         if (dateini.change) sql = sql + " AND Date_adhesion_to  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
         sql= sql+ ")) AS tble1";
        
        conn=Connect.ConnectDb();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        int sum=0;
        while (rs.next()) {
              sum= rs.getInt(1);
         }
          
         conn.close();
         pre.close();
         rs.close();
         return sum;
        
        
    }
    
    
   public double getDep(Date a, Date b) throws SQLException{
        String sql="SELECT SUM(montant) FROM Entreessorties  WHERE montant <=0 AND daterec >='"+new java.sql.Date(a.getTime())+"' AND daterec <= '"+new java.sql.Date(b.getTime())+"'";
        conn = Connect.ConnectDb2();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double sumctont=0;
          while (rs.next()) {
              sumctont= rs.getDouble(1);
          }
          
         conn.close();
         pre.close();
         rs.close();
         return sumctont;
   }
   
   public double getEntr(Date a, Date b) throws SQLException{
        String sql="SELECT SUM(montant) FROM Entreessorties  WHERE montant >=0 AND daterec >='"+new java.sql.Date(a.getTime())+"' AND daterec <= '"+new java.sql.Date(b.getTime())+"'";
        if (dateini.change) sql = sql + " AND daterec  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

        conn = Connect.ConnectDb2();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double sumctont=0;
          while (rs.next()) {
              sumctont= rs.getDouble(1);
          }
          
         conn.close();
         pre.close();
         rs.close();
         return sumctont;
   }
    
   public double getcommtont (Date a, Date b) throws SQLException{
        Date zerodatea;
        Date zerodateb;
        Calendar cdatea = new GregorianCalendar();
        cdatea.setTime(a);
        cdatea.set(Calendar.MILLISECOND, 0);
        cdatea.set(Calendar.SECOND, 0); 
        cdatea.set(Calendar.MINUTE, 0);
        cdatea.set(Calendar.HOUR_OF_DAY, 0);
        cdatea.set(Calendar.DAY_OF_MONTH,1);
        zerodatea= cdatea.getTime();
        
        Calendar cdateb = new GregorianCalendar();
        cdateb.setTime(b);
        cdateb.set(Calendar.MILLISECOND, 0);
        cdateb.set(Calendar.SECOND, 0); 
        cdateb.set(Calendar.MINUTE, 0);
        cdateb.set(Calendar.HOUR_OF_DAY, 0);
        cdateb.set(Calendar.DAY_OF_MONTH,1);
        zerodateb= cdateb.getTime();
        
        String sql="SELECT SUM(Mise) FROM Tontine  WHERE (bit_count(JoursTontine)) >=1 AND DateTontine >= '"+new java.sql.Date(zerodatea.getTime())+"' AND DateTontine <= '"+new java.sql.Date(zerodateb.getTime())+"'";
        if (dateini.change) sql = sql + " AND DateTontine  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        conn = Connect.ConnectDb2();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double sumctont=0;
          while (rs.next()) {
              sumctont= rs.getDouble(1);
          }
          
          conn.close();
          pre.close();
         rs.close();
         
         return sumctont;
    
   
   }
    
  public  Date least(Date a, Date b) {
    return a == null ? b : (b == null ? a : (a.before(b) ? a : b));
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
                System.out.println("true"+value);
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
      
    // get minimum date   
    private Date getMinDate () throws SQLException {
        // Get min date from Epargne
        Date minEpDate=new Date();
        Date minTontDate=new Date();
        Date minOriDate=new Date();
        conn = Connect.ConnectDb();
        pre = conn.prepareStatement("SELECT Min(DateEpargne) FROM Epargne");
        ResultSet rs = pre.executeQuery();
        while(rs.next()) {
            minEpDate = rs.getDate(1);
        }
        
        
        // Get min Date from Tontine 
        pre = conn.prepareStatement("SELECT Min(DateTontine) FROM Tontine");
        rs = pre.executeQuery();
        while(rs.next()) {
            minTontDate = rs.getDate(1);
        }
        
        
        // Get Min from loan 
        pre = conn.prepareStatement("SELECT Min(DateOri) FROM Loan");
        rs = pre.executeQuery();
        while(rs.next()) {
            minOriDate = rs.getDate(1);
        }
        
       
        
        conn.close();
        rs.close();
        pre.close();
        
        
       return least(least(minEpDate,minTontDate),minOriDate);
        
    }  
     
     
public void fillftctable(String typeEpargnant, int idEpargnant) throws Exception {
        
        
        ftchash.put(typeEpargnant+" "+idEpargnant, new ArrayList<Date>()); 
        conn = Connect.ConnectDb2();
        //pre = conn.prepareStatement(
                
        String sql="SELECT id AS ide, w AS dte, d AS description, \n" +
"   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n" +
"   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n" +
"  FROM\n" +
"  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n" +
"          Epargne.MontantEpargne AS a\n" +
"     FROM Epargne\n" +
"     WHERE IdEpargnant='" + idEpargnant + "' AND TypeEpargnant='" + typeEpargnant + "'\n"; 
        
        
    if (dateini.change) sql = sql + "AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
    sql =sql + "     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne,  Epargne.IdEpargne) t";
    pre = conn.prepareStatement(sql);            
        

    ResultSet rs = pre.executeQuery();
    SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

        
        Double balance = 0d;
        boolean firstentry=true;
        Date previous = null;
        Date date=new Date();
        Date d=new Date(); 
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
                if(isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry==false)) {
                    balance =balance-100;
                    onceftc=true;
                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d);
                }
                gcal.add(Calendar.DAY_OF_YEAR, 1);
            }             
            balance= balance - rs.getDouble("cshOUT") + rs.getDouble("cshIN");           
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
                if(isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && (!(isTwentyEighthDayOfTheMonth(previous) && onceftc==true &&  DateUtils.isSameDay(d2,d)))) {     
                    balance =balance-100;
                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d2);
                  
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
        
       
}


 public Double calculateftcsumm (Date startDate, Date endDate) {
        Calendar caltweighthismonth= Calendar.getInstance();  
        caltweighthismonth.set(Calendar.HOUR_OF_DAY, 0);
        caltweighthismonth.set(Calendar.MINUTE, 0);
        caltweighthismonth.set(Calendar.SECOND, 0);
        caltweighthismonth.set(Calendar.MILLISECOND, 0);
        //Date date = // the date instance
        Calendar startcalendar = Calendar.getInstance();
        startcalendar.set(Calendar.HOUR_OF_DAY, 0);
        startcalendar.set(Calendar.MINUTE, 0);
        startcalendar.set(Calendar.SECOND, 0);
        startcalendar.set(Calendar.MILLISECOND, 0);
        startcalendar.setTime(startDate);
        Calendar endcalendar = Calendar.getInstance();
        endcalendar.set(Calendar.HOUR_OF_DAY, 0);
        endcalendar.set(Calendar.MINUTE, 0);
        endcalendar.set(Calendar.SECOND, 0);
        endcalendar.set(Calendar.MILLISECOND, 0);
        endcalendar.setTime(endDate);
        
        int startday = startcalendar.get(Calendar.DAY_OF_MONTH);
        int startmonth = startcalendar.get(Calendar.MONTH);
        int startyear = startcalendar.get(Calendar.YEAR);
      
        if (startday > 28) {
            startcalendar.set(startyear, startmonth, 28);
            startcalendar.add(Calendar.MONTH, 1);        
        } else {
             startcalendar.set(startyear, startmonth, 28);
        }
        
        int endday = endcalendar.get(Calendar.DAY_OF_MONTH);
        int endmonth = endcalendar.get(Calendar.MONTH);
        int endyear = endcalendar.get(Calendar.YEAR);
        
        if(endday > 28) {
            endcalendar.set(endyear, endmonth, 28);
            
        } else {
             endcalendar.set(endyear, endmonth, 28);
             endcalendar.add(Calendar.MONTH, -1);  
        }
        
       // caltweighthismonth.set(startyr, startmnth, 28);
        Date startDate0 =  startcalendar.getTime();
        Date endDate0 =  endcalendar.getTime();
        //caltweighthismonth.set(endyr, endmnth, 28);
      //  Date endDate =  caltweighthismonth.getTime();
         int mnthcount=0;
         int count=0;
         
         if (startDate0.compareTo(endDate0) <=0) {
         for (Date dat=startDate0; dat.compareTo(endDate0)<=0; dat= DateUtils.addMonths(dat, 1) ) {
            mnthcount= countftc(dat)*100;
            System.out.println("mnthcount"+mnthcount);
            count= count + mnthcount;
            
            
        }
        }
            
       return (double) count;  
         
     }
     
 
     public Double calculateftcsumm (int startyr, int startmnth, int endyr, int endmnth) {
        Calendar caltweighthismonth= Calendar.getInstance();  
        caltweighthismonth.set(Calendar.HOUR_OF_DAY, 0);
        caltweighthismonth.set(Calendar.MINUTE, 0);
        caltweighthismonth.set(Calendar.SECOND, 0);
        caltweighthismonth.set(Calendar.MILLISECOND, 0);
        caltweighthismonth.set(startyr, startmnth, 28);
        Date startDate =  caltweighthismonth.getTime();
        caltweighthismonth.set(endyr, endmnth, 28);
        Date endDate =  caltweighthismonth.getTime();
         int mnthcount=0;
         int count=0;
        for (Date dat=startDate; dat.compareTo(endDate)<=0; dat= DateUtils.addMonths(dat, 1) ) {
            mnthcount= countftc(dat)*100;
            System.out.println("mnthcount"+mnthcount+"dat"+dat);
            count= count + mnthcount;
            
            
        }
            
       return (double) count;  
         
     }
     
     public double convertstrdbl(String str)
{
    String[] str_arr = str.split(",");
    double sum=0;
    for (int i = 0; i > str_arr.length; i++)
    {
        sum += Double.parseDouble(str_arr[i]);
    }
return sum;
}
     
     // Interets automatiques
     
     public Double calculateinterestautom (int startyr, int startmnth, int startday,  int endyr, int endmnth, int enday) throws SQLException {
        conn = Connect.ConnectDb2();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(enday); 
        String sql = "SELECT idloan, SUM(sumrembours) FROM rembours_auto WHERE daterembours >= '"+beginDateStr+"' AND daterembours <= '"+endDateStr+"' ";
        if (dateini.change) sql = sql + " AND daterembours  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        sql = sql +" GROUP BY idloan";
        

        System.out.println("full sql"+sql);
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double summ=0;
        
        double fees = 0;
        while (rs.next()) {
            automint.put(rs.getString(1), String.valueOf(rs.getDouble(2)));
            System.out.println("get double value"+rs.getDouble(2));
        }
       System.out.println("automin"+automint);
       StringBuilder csvBuilder = new StringBuilder();

       for (String loan : automint.keySet()){
             csvBuilder.append("'");
            csvBuilder.append(loan);
         
            csvBuilder.append("'");
             csvBuilder.append(",");
       }

       String csv = csvBuilder.toString();
       System.out.println("csv vaut"+csv);
       if (csv.length() !=0) csv =csv.substring(0, csv.length()-1);
       sql = "SELECT Loanrefnum, Montant FROM Loan WHERE Loanrefnum IN ("+csv+")";
       System.out.println("loan sql"+sql);
       pre = conn.prepareStatement(sql);
       rs = pre.executeQuery();
       String loanref;
       while (rs.next()) {
           
           loanref =rs.getString(1);
           System.out.println("value of loanref get"+automint.get(loanref));
           if (rs.getDouble(2) <  convertstrdbl(automint.get(loanref)))   summ = summ + convertstrdbl(automint.get(loanref)) - rs.getDouble(2);
           automint.put(loanref, (String)automint.get(loanref) + "," + String.valueOf(rs.getDouble(2)));
            
      }
        
       
       
         System.out.println("automin"+automint);
          
      
            
       return summ;  
  }
     
  //  interets libres
     
 public Double calculateinterestlibres (int startyr, int startmnth, int startday,  int endyr, int endmnth, int enday) throws SQLException {
       conn = Connect.ConnectDb2();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(enday); 
        String sql = "SELECT idloan, SUM(montant) FROM rembours_libre WHERE dateEnr >= '"+beginDateStr+"' AND dateEnr <= '"+endDateStr+"' ";
        if (dateini.change) sql = sql + " AND dateEnr >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

         System.out.println("full sql"+sql);
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double summ=0;
        
        double fees = 0;
        while (rs.next()) {
            libreint.put(rs.getString(1), String.valueOf(rs.getDouble(2)));
        }
         System.out.println("libreint"+libreint);
       StringBuilder csvBuilder = new StringBuilder();

        for(String loan : libreint.keySet()){
             csvBuilder.append("'");
            csvBuilder.append(loan);
         
            csvBuilder.append("'");
             csvBuilder.append(",");
        }

       String csv = csvBuilder.toString();
       csv =csv.substring(0, csv.length()-1);
       sql = "SELECT Loanrefnum, Montant FROM Loan WHERE Loanrefnum IN ("+csv+")";
        System.out.println("loan sql"+sql);
       pre = conn.prepareStatement(sql);
       rs = pre.executeQuery();
       String loanref;
       while (rs.next()) {
           
           loanref =rs.getString(1);
           if (rs.getDouble(2) <=  Double.valueOf(libreint.get(loanref)))   summ = summ + Double.valueOf(libreint.get(loanref)) -rs.getDouble(2);
           libreint.put(loanref, (String)libreint.get(loanref) + "," + String.valueOf(rs.getDouble(2)));
            
      }
        
       
       
         System.out.println("automin"+libreint);
          
      
            
       return summ;  
}
 
 

 
 
 
 public Double calculateinterestterme(int startyr, int startmnth, int startday,  int endyr, int endmnth, int enday) throws SQLException {
       conn = Connect.ConnectDb2();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(enday); 
        String sql = "SELECT idloan, SUM(montantpay) FROM rembours_terme WHERE daterembours >= '"+beginDateStr+"' AND daterembours <= '"+endDateStr+"' ";
        if (dateini.change) sql = sql + " AND daterembours  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

         System.out.println("full sql"+sql);
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double summ=0;
        
        double fees = 0;
        while (rs.next()) {
            termeint.put(rs.getString(1), String.valueOf(rs.getDouble(2)));
        }
       System.out.println("libreint"+termeint);
       StringBuilder csvBuilder = new StringBuilder();

        for(String loan : termeint.keySet()){
             csvBuilder.append("'");
            csvBuilder.append(loan);
         
            csvBuilder.append("'");
             csvBuilder.append(",");
        }

       String csv = csvBuilder.toString();
       csv =csv.substring(0, csv.length()-1);
       sql = "SELECT Loanrefnum, Montant FROM Loan WHERE Loanrefnum IN ("+csv+")";
        System.out.println("loan sql"+sql);
       pre = conn.prepareStatement(sql);
       rs = pre.executeQuery();
       String loanref;
       while (rs.next()) {
           
           loanref =rs.getString(1);
           if (rs.getDouble(2) <=  Double.valueOf(termeint.get(loanref)))   summ = summ + Double.valueOf(termeint.get(loanref)) -rs.getDouble(2);
           termeint.put(loanref, (String)termeint.get(loanref) + "," + String.valueOf(rs.getDouble(2)));
            
      }
        
       
       
         System.out.println("termeint"+termeint);
          
      
            
       return summ;  
} 

public Double calculatepenaliteautom (int startyr, int startmnth, int startday,  int endyr, int endmnth, int enday) throws SQLException {
       conn = Connect.ConnectDb();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(enday); 
        String sql = "SELECT SUM(penalite) FROM rembours_auto WHERE daterembours >= '"+beginDateStr+"' AND daterembours <= '"+endDateStr+"'";
        if (dateini.change) sql = sql + " AND daterembours  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

        //System.out.println("full sql"+sql);
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double summ=0;
        
        double fees = 0;
        while (rs.next()) {
           return rs.getDouble(1);
        }
               
       return 0.0;  
}
  
  public Double calculatepenalitelibre (int startyr, int startmnth, int startday,  int endyr, int endmnth, int enday) throws SQLException {
       conn = Connect.ConnectDb();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(enday); 
        String sql = "SELECT SUM(montant) FROM rembours_libre WHERE dateEnr >= '"+beginDateStr+"' AND dateEnr <= '"+endDateStr+"' AND libelle LIKE '%Pénalité%'";
        if (dateini.change) sql = sql + " AND dateEnr  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

         System.out.println("full sql"+sql);
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double summ=0;
        
        double fees = 0;
        while (rs.next()) {
           return rs.getDouble(1);
        }
               
       return 0.0;  
}
  
public Double calculatepenaliteterme (int startyr, int startmnth, int startday,  int endyr, int endmnth, int enday) throws SQLException {
       conn = Connect.ConnectDb();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(enday); 
        String sql = "SELECT SUM(montantpay) FROM rembours_terme WHERE daterembours >= '"+beginDateStr+"' AND daterembours <= '"+endDateStr+"' AND libelle LIKE '%Pénalité%'";
        if (dateini.change) sql = sql + " AND daterembours  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";

         System.out.println("full sql"+sql);
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double summ=0;
        
        double fees = 0;
        while (rs.next()) {
           return rs.getDouble(1);
        }
               
       return 0.0;  
}  
     
     
     
     
     
     
     
     public Double calculateopenfees (int startyr, int startmnth, int endyr, int endmnth) throws SQLException {
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+"01";
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+"01"; 
        conn = Connect.ConnectDb();
        pre = conn.prepareStatement("SELECT COUNT(*) FROM (SELECT a.idProfil_enfant as id, a.Date_adhesion_ep as DateEp,  'Enfant' as TypeEp From Profil_enfant a WHERE a.Type_adhesion LIKE '%Epargne%' UNION\n"+
                "SELECT b.idProfil_adulte as id, b.Date_adhesion_ep as DateEp,  'Adulte' as TypeEp From Profil_adulte b WHERE b.Type_adhesion LIKE '%Epargne%' UNION\n"+
                "SELECT c.idProfil_persmorale as id, c.Date_adhesion_ep as DateEp, 'Pers Morale' as TypeEp From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Epargne%') midtab WHERE DateEp >= '"+ beginDateStr+"' AND DateEp <= LAST_DAY('"+endDateStr+"')\n");
        ResultSet rs = pre.executeQuery();
        double fees = 0;
        if (rs.next()) fees =(double) 2500* rs.getInt(1);  
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return fees;
     }
     
     
    public Double calculateSocialPart (int startyr, int startmnth, int endyr, int endmnth) throws SQLException {
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+"01";
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+"01"; 
        conn = Connect.ConnectDb();
        pre = conn.prepareStatement("SELECT SUM(Part_soc) FROM (SELECT a.idProfil_enfant as id, a.Date_adhesion_ep as DateEp,  'Enfant' as TypeEp, a.Part_sociale as Part_soc From Profil_enfant a WHERE a.Type_adhesion LIKE '%Epargne%' UNION\n"+
                "SELECT b.idProfil_adulte as id, b.Date_adhesion_ep as DateEp,  'Adulte' as TypeEp, b.Part_sociale as Part_soc From Profil_adulte b WHERE b.Type_adhesion LIKE '%Epargne%' UNION\n"+
                "SELECT c.idProfil_persmorale as id, c.Date_adhesion_ep as DateEp, 'Pers Morale' as TypeEp, c.Part_sociale as Part_soc From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Epargne%') midtab WHERE DateEp >= '"+ beginDateStr+"' AND DateEp <= LAST_DAY('"+endDateStr+"')\n");
        ResultSet rs = pre.executeQuery();
        double partsoc = 0;
        if (rs.next()) partsoc =rs.getDouble(1);  
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return partsoc;
       
    }
       
       
       // Partie tontine
    public Double commissiontontine (int startyr, int startmnth, int endyr, int endmnth) throws SQLException {
        
        conn = Connect.ConnectDb2();
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+"01";
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+"01"; 
        System.out.println("beginDateStr"+beginDateStr);
        System.out.println("endDateStr"+endDateStr);
        String sql= "SELECT DateTontine, SUM(Mise) FROM Tontine  WHERE (bit_count(JoursTontine)) >=1 AND DateTontine >='"+beginDateStr+"' AND DateTontine <='"+endDateStr+"'";
        if (dateini.change) sql = sql + "AND DateTontine  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        pre=conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double comm = 0;
        if (rs.next()) comm =rs.getDouble(2);  
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return comm;
       
    }
    
     public Double calculateopenfeestontine (int startyr, int startmnth, int endyr, int endmnth) throws SQLException {   // Peut être sujet à modification, tenir compte de changements d'adhesion
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+"01";
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+"01"; 
        conn = Connect.ConnectDb();
        pre = conn.prepareStatement("SELECT COUNT(*) FROM (SELECT a.idProfil_enfant as id, a.Date_adhesion_ep as DateEp,  'Enfant' as TypeEp From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%' UNION\n"+
                "SELECT b.idProfil_adulte as id, b.Date_adhesion_ep as DateEp,  'Adulte' as TypeEp From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%' UNION\n"+
                "SELECT c.idProfil_persmorale as id, c.Date_adhesion_ep as DateEp, 'Pers Morale' as TypeEp From Profil_persmorale c WHERE c.Type_adhesion LIKE 'Tontine%') midtab WHERE DateEp >= '"+ beginDateStr+"' AND DateEp <= LAST_DAY('"+endDateStr+"')\n");
        ResultSet rs = pre.executeQuery();
        double fees = 0;
        if (rs.next()) fees =(double) 500* rs.getInt(1);  
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return fees;
     }
     
     
     // caution
     
     public Double calculatecaution (int startyr, int startmnth, int startday,  int endyr, int endmnth, int endday) throws SQLException {   // Peut être sujet à modification, tenir compte de changements d'adhesion
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(endday); 
        conn = Connect.ConnectDb();
        pre = conn.prepareStatement("SELECT SUM(Caution) FROM Loan WHERE DateOri >= '"+ beginDateStr+"' AND DateOri <= ('"+endDateStr+"')\n");
        ResultSet rs = pre.executeQuery();
        double caution =0.0;
       
        if (rs.next()) { caution =  rs.getDouble(1); }
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return caution;
     }
       
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/compta_mutu.png"))); // NOI18N
        jLabel1.setToolTipText("");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Libellé", "Montant", "Type"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSplitPane1.setRightComponent(jScrollPane1);

        jButton1.setText("Nouvel enre");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Modifier enre");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconval.png"))); // NOI18N
        jButton3.setText("Valider");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButton2)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton1)
                .add(12, 12, 12)
                .add(jButton2)
                .addContainerGap(281, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel3);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Bilan", jPanel1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Type", "Montant"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Type", "Montant"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel5.setText("Entrees");

        jLabel7.setText("Sorties");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel5)
                            .add(jLabel7))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 204, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(122, 122, 122))
        );

        jTabbedPane1.addTab("Bilan Entrees/Sorties", jPanel2);

        jLabel2.setText("Déb période");

        jLabel3.setText("Fin période");

        jLabel4.setText("Entrées");

        jLabel6.setText("Sorties");

        jLabel9.setText("Solde");

        demoDateField1.setYearDigitsAmount(4);

        demoDateField2.setYearDigitsAmount(4);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel3)
                .add(9, 9, 9)
                .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(219, 219, 219)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jLabel6)
                    .add(jLabel9))
                .add(32, 32, 32)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel11)
                    .add(jLabel10)
                    .add(jLabel8))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(jLabel1)
                .add(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3))
                        .add(31, 31, 31))
                    .add(layout.createSequentialGroup()
                        .add(28, 28, 28)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(jLabel8))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6)
                            .add(jLabel10))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel9)
                            .add(jLabel11))))
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 439, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
           DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new MyRenderer());
         DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
      // Frais pour bilan tontine
        
        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        
        Date beginDate= demoDateField1.getDate();
       
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        int startday = cal.get(Calendar.DATE);
        int startmonth = cal.get(Calendar.MONTH);
        int startyear = cal.get(Calendar.YEAR);
        
        Date endDate= demoDateField2.getDate();
        cal.setTime(endDate);
        int endday = cal.get(Calendar.DATE);
        int endmonth = cal.get(Calendar.MONTH);
        int endyear = cal.get(Calendar.YEAR);
        //  jTable1.removeAll();
        
      
        
        
        // Calculer FTC (frais de tenue de compte
        ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Frais de tenue de compte" , calculateftcsumm(beginDate, endDate), "Entrées" });
        
        try {
            // Calculer Commission sur tontine
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Commission sur tontine" , getcommtont(beginDate, endDate), "Entrées" });
            
            
            
            
            
            
            //  calculateftcsumm(2016,7,2018,7))
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        // Frais d'ouverture de dossier Epargne
        try {
         
            
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Frais d'adhésion épargne" , getnbfraisadhEp(beginDate, endDate)*2500, "Entrées" });
            
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         // Frais d'ouverture de dossier Tontine
        try {
         
            
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Frais d'adhésion tontine" , getnbfraisadhTon(beginDate, endDate)*500, "Entrées" });
            
            
            
            
           
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
         // interets 
         // Frais d'ouverture de dossier Tontine
        try {
         
            
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Pénalités" , calculatepenaliteautom(startyear,startmonth,startday, endyear,endmonth,endday)+ calculatepenalitelibre(startyear,startmonth,startday, endyear,endmonth,endday)+calculatepenaliteterme(startyear,startmonth,startday, endyear,endmonth,endday), "Entrées" });
            
            
            
            
           
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // penalites 
            try {
         
            
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Intérêts" , calculateinterestautom(startyear,startmonth,startday, endyear,endmonth,endday)+ calculateinterestlibres(startyear,startmonth,startday, endyear,endmonth,endday)+calculateinterestterme(startyear,startmonth,startday, endyear,endmonth,endday), "Entrées" });
            
            
            
            
           
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        // Frais de suivi 
              try {
         
            
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Frais de suivi" , calculatefraissuivi(startyear,startmonth,startday, endyear,endmonth,endday), "Entrées" });
            
            
            
            
           
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            
       // Frais de dossier
              
        try {          
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Frais de dossier" , calculatefraisdossier(startyear,startmonth,startday, endyear,endmonth,endday), "Entrées" });          
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
            

        
        
        try {
            // Entrées, aides etc
            
            
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Entrées en caisse" , getEntr(beginDate, endDate), "Entrées" });
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        try {
            // Dépenses

            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Dépenses" , getDep(beginDate, endDate), "Sorties" });
        } catch (SQLException ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
//           Double temp =  (double) jTable1.getValueAt(0, 1);
//        Double temp2 = (double) jTable1.getValueAt(1, 1);
//        Double temp3 = new Double (((Integer) jTable1.getValueAt(2, 1)).doubleValue());
//        Double temp4 = new Double (((Integer) jTable1.getValueAt(3, 1)).doubleValue());
//        Double temp5 = (double) jTable1.getValueAt(4, 1);
//        Double temp6 = (double) jTable1.getValueAt(5, 1);
//        Double temp7 = (double) jTable1.getValueAt(6, 1);
//        Double temp8 = (double) jTable1.getValueAt(7, 1);
//        Double temp9 = (double) jTable1.getValueAt(8, 1);
//        Double temp10 = (double) jTable1.getValueAt(9, 1);
//        
        
     //   new Double ((double) jTable1.getValueAt(0, 1))+ new Double ((double) jTable1.getValueAt(1, 1)) + (double) jTable1.getValueAt(2, 1)+ (double) jTable1.getValueAt(3, 1)+ (double) jTable1.getValueAt(4, 1)+ (double) jTable1.getValueAt(5, 1)+ (double) jTable1.getValueAt(6, 1), "" }
        
        
        
         try {
            // Dépenses

            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {"Total" , (double) jTable1.getValueAt(0, 1)+ (double) jTable1.getValueAt(1, 1) +   new Double (((Integer) jTable1.getValueAt(2, 1)).doubleValue())+  new Double (((Integer) jTable1.getValueAt(3, 1)).doubleValue()) + (double) jTable1.getValueAt(4, 1) + (double) jTable1.getValueAt(5, 1)+ (double) jTable1.getValueAt(6, 1) + (double) jTable1.getValueAt(7, 1)+ (double) jTable1.getValueAt(8, 1)+ (double) jTable1.getValueAt(9, 1), "" });
        } catch (Exception ex) {
            Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
                
     

        
      //Bilan Entrees 
        
        DefaultTableModel dm2 = (DefaultTableModel) jTable2.getModel();
        rowCount = dm2.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm2.removeRow(i);
        }
        
     
      
        
         try {
             double sommeepplus = getEpargneplus(beginDate, endDate);
             double sommetontplus = getTdepontine(beginDate, endDate);
             double entreecaisse = getEntr(beginDate, endDate); 
             double fraisadhep=getnbfraisadhEp(beginDate, endDate)*2500;
             double fraisadhtont=getnbfraisadhTon(beginDate, endDate)*500;
             double interest = calculateinterestautom(startyear,startmonth,startday,endyear,endmonth, endday)+ calculateinterestlibres(startyear,startmonth,startday,endyear,endmonth, endday)+calculateinterestterme(startyear,startmonth,startday,endyear,endmonth, endday);
             double penalites =calculatepenaliteautom(startyear,startmonth,startday,endyear,endmonth, endday)+ calculatepenalitelibre(startyear,startmonth,startday,endyear,endmonth, endday)+calculatepenaliteterme(startyear,startmonth,startday,endyear,endmonth, endday);
             double fraissuivi= calculatefraissuivi(startyear,startmonth,startday, endyear,endmonth,endday);
             double fraisdossier = calculatefraisdossier(startyear, startmonth, startday, endyear, endmonth, endday);


            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Total Epargnes" , sommeepplus , "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Total Tontine" , sommetontplus , "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Entrées en caisse" , entreecaisse, "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Frais d'adhesion épargne" , fraisadhep, "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Frais d'adhesion Tontine" , fraisadhtont, "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Intérêts" , interest, "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Pénalités" , penalites, "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Frais de dossier" , fraisdossier, "Entrées" });
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {"Frais de suivi" , fraissuivi, "Entrées" });


            


             totalentrees = sommeepplus + sommetontplus + entreecaisse+ fraisadhep + fraisadhtont+ interest + penalites + fraisadhep + fraisadhtont;
             jLabel8.setText(formatter.format(totalentrees.longValue()));
        
        } catch (SQLException ex) {
           Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        DefaultTableModel dm3 = (DefaultTableModel) jTable3.getModel();
        rowCount = dm3.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm3.removeRow(i);
        } 

        
        try {
            
             double sommeepmns = getEpargnemns(beginDate, endDate);
             double sommetontmns = getRetrtontine(beginDate, endDate);
             double sortiecaisse = getDep(beginDate, endDate);     
             ((DefaultTableModel) jTable3.getModel()).addRow(new Object[] {"Retrait Epargnes" , sommeepmns, "test" });
             ((DefaultTableModel)jTable3.getModel()).addRow(new Object[] {"Retraits Tontine" , sommetontmns, "test" });
             ((DefaultTableModel) jTable3.getModel()).addRow(new Object[] {"Dépenses" , sortiecaisse, "test" });
       
              totalsorties=sommeepmns + sommetontmns + sortiecaisse ;
              jLabel10.setText(formatter.format(totalsorties.longValue()));

        } catch (SQLException ex) {
           Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Double solde = totalentrees + totalsorties;

        jLabel11.setText(formatter.format(solde.longValue()));
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MvtMutu mvt =new MvtMutu();
        mvt.setLocationRelativeTo(null);
        mvt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mvt.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        // TODO add your handling code here:
        JTable table = (JTable) evt.getSource();
        Point point = evt.getPoint();
        int row = table.rowAtPoint(point);
        if(evt.getClickCount() == 2 && table.getSelectedRow() == 5) {
            try {
                det = new Detailcompt(demoDateField1.getDate(), demoDateField2.getDate());
            } catch (Exception ex) {
                Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
            det.setLocationRelativeTo(null);
            det.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            det.setVisible(true);

        }
        
          if(evt.getClickCount() == 2 && table.getSelectedRow() == 4) {
            try {
                det = new Detailcompt(demoDateField1.getDate(), demoDateField2.getDate());
            } catch (Exception ex) {
                Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
            det.setTab(1);
            det.setLocationRelativeTo(null);
            det.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            det.setVisible(true);

        }
          
  
           
          if(evt.getClickCount() == 2 && table.getSelectedRow() == 3) {
            try {
                det = new Detailcompt(demoDateField1.getDate(), demoDateField2.getDate());
            } catch (Exception ex) {
                Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
            det.setTab(3);
            det.setLocationRelativeTo(null);
            det.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            det.setVisible(true);

        }   
          
           if(evt.getClickCount() == 2 && table.getSelectedRow() == 2) {
            try {
                det = new Detailcompt(demoDateField1.getDate(), demoDateField2.getDate());
            } catch (Exception ex) {
                Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
            det.setTab(2);
            det.setLocationRelativeTo(null);
            det.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            det.setVisible(true);

        }   
         
              
           if(evt.getClickCount() == 2 && table.getSelectedRow() == 1) {
            try {
                det = new Detailcompt(demoDateField1.getDate(), demoDateField2.getDate());
            } catch (Exception ex) {
                Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
            det.setTab(4);
            det.setLocationRelativeTo(null);
            det.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            det.setVisible(true);

        }   
           
            if(evt.getClickCount() == 2 && table.getSelectedRow() == 0) {
            try {
                det = new Detailcompt(demoDateField1.getDate(), demoDateField2.getDate());
            } catch (Exception ex) {
                Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
            }
            det.setTab(5);
            det.setLocationRelativeTo(null);
            det.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            det.setVisible(true);

        }
            
            
            
              
                
    }//GEN-LAST:event_jTable1MousePressed
    
    public Double calculatefraisdossier (int startyr, int startmnth, int startday,  int endyr, int endmnth, int endday) throws SQLException {   // Peut être sujet à modification, tenir compte de changements d'adhesion
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(endday); 
        conn = Connect.ConnectDb();
        String sql = "SELECT SUM(Fraisdossier) FROM Loan WHERE DateOri >= '"+ beginDateStr+"' AND DateOri <= ('"+endDateStr+"')\n";
        sql = "SELECT SUM(Fraisdossier) FROM Loan WHERE DateOri >= '"+ beginDateStr+"' AND DateOri <= ('"+endDateStr+"')\n";
        
        if (dateini.change) sql = sql + " AND DateOri  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        
        pre =conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double caution =0.0;
       
        if (rs.next()) { caution =  rs.getDouble(1); }
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return caution;
     }
    
    
       public Double calculatefraissuivi (int startyr, int startmnth, int startday,  int endyr, int endmnth, int endday) throws SQLException {   // Peut être sujet à modification, tenir compte de changements d'adhesion
        DecimalFormat decformatter = new DecimalFormat( "#00" );
        String beginDateStr = String.valueOf(startyr)+"-"+decformatter.format(startmnth)+"-"+decformatter.format(startday);
        String endDateStr = String.valueOf(endyr)+"-"+decformatter.format(endmnth)+"-"+decformatter.format(endday); 
        conn = Connect.ConnectDb();
        String sql = "SELECT SUM(Fraissuivi) FROM Loan WHERE DateOri >= '"+ beginDateStr+"' AND DateOri <= ('"+endDateStr+"')\n";
        if (dateini.change) sql = sql + " AND DateOri  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        double caution =0.0;
       
        if (rs.next()) { caution =  rs.getDouble(1); }
          
        if (conn != null) {
            conn.close();
        }
        
          if (pre != null) {
            pre.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        return caution;
     }
    
    
    
    
    
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
//        // TODO add your handling code here:
//           if (evt.getClickCount() == 2) {
//                JTable target = (JTable)evt.getSource();
//                int row = target.getSelectedRow();
//                Detailcompt det = new Detailcompt(beginD)
//     
//   }
    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger(Compta_mutuelle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compta_mutuelle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compta_mutuelle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compta_mutuelle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Compta_mutuelle().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Compta_mutuelle.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables

}
