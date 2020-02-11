/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RowFilter.Entry;
import static nehemie_mutuelle.TontineSynthese.totalrow;
import org.apache.commons.lang.time.DateUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


/**
 *
 * @author elommarcarnold
 */
public class Statistics extends javax.swing.JFrame {
    
    private Connection conn;
    private final Map<Integer, Double> m;
    private final Map<String, Double> m2;
    private final Map<String, Double> m3; // tontine
    private Map<String, Double> m4; // total revenue

    private IntializeDate ini = new IntializeDate();
    PreparedStatement pre= null;
    private Map <String, ArrayList<Date>> ftchash;
    
    
    private int nbhommes, nbfemmes, nbpersmor =0;
    
    
    public static <K> Map<K, Double> mergeAndAdd(Map<K, Double>... maps) {
    Map<K, Double> result = new HashMap<>();
    for (Map<K, Double> map : maps) {
        for (Map.Entry<K, Double> entry : map.entrySet()) {
            K key = entry.getKey();
            Double current = result.get(key);
            result.put(key, current == null ? entry.getValue() : entry.getValue() + current);
        }
    }
    return result;
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
    
    
    public String getfullname(int id, String typeEpargnant) throws SQLException {
        conn=Connect.ConnectDb();
        String result="";
        ResultSet rs=null;
        PreparedStatement pre1;
        
        if(typeEpargnant.equalsIgnoreCase("enfant")) {
            pre1=conn.prepareStatement("SELECT Nom, Prenoms FROM Profil_enfant WHERE idProfil_enfant='"+id+"'");
            rs= pre1.executeQuery();
            while (rs.next()) {
                result= rs.getString(1)+" "+rs.getString(2);
            }
                
      } else if(typeEpargnant.equalsIgnoreCase("adulte")) {
            pre1=conn.prepareStatement("SELECT Noms, Prenoms FROM Profil_adulte WHERE idProfil_adulte='"+id+"'");
            rs= pre1.executeQuery();
            while (rs.next()) {
                result= rs.getString(1)+" "+rs.getString(2);
            }
      } else {
            pre1=conn.prepareStatement("SELECT Raison_sociale FROM Profil_persmorale WHERE idProfil_persmorale='"+id+"'");
            rs= pre1.executeQuery();
            while (rs.next()) {
                result= rs.getString(1);
            }
          
      }
        
        
      if(rs!=null) rs.close();
      if(pre1!=null) rs.close();
      if(conn !=null) conn.close();
      
      return result;
          
          
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
        sql =sql + "     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne,  Epargne.IdEpargne) t";
        
        pre =conn.prepareStatement(sql);
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
                if(isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry==false) && !excList.contains(d)) {
                    balance =balance-100;
                    onceftc=true;
                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d);
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
                if(isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && (!(isTwentyEighthDayOfTheMonth(previous) && onceftc==true &&  DateUtils.isSameDay(d2,d) && !excList.contains(d2)))) {     
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
        
       
    }
    
    
    
public Double getReport (String typeEpargnant, int idEpargnant, Date begindate) throws Exception {
        conn = Connect.ConnectDb2();
        SimpleDateFormat fmm=new SimpleDateFormat("yyyy-MM-dd");
        String datestr= fmm.format(begindate);
        PreparedStatement pre21= conn.prepareStatement("SET @SumMontant := 0;");
        pre21.executeQuery();
        pre = conn.prepareStatement("SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d,\n" +
"          Epargne.MontantEpargne AS a, (@SumMontant := @SumMontant + MontantEpargne) AS CumulativeSum\n" +
"     FROM Epargne\n" +
"     WHERE IdEpargnant='"+idEpargnant+"' AND TypeEpargnant='"+typeEpargnant+"' AND DateEpargne <= '"+datestr+ "'\n" +
"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne ORDER BY DateEpargne ASC;");

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
     
        balance= balance - count* 100;  // @todo remplcer par valeur actuelle
        
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
        
        
    
    
    /** Creates new form Statistics */
    public  Statistics(int i) throws SQLException, Exception {
        this.m = new TreeMap<>();
        this.m2 = new HashMap<>();
        this.m3 = new HashMap<>();
        ftchash= new HashMap<String, ArrayList<Date>>();
           buildftchash();
        initComponents();
        if (i==2)  { 
         getpercentage();
        getplotnumberpercentage(nbhommes, nbfemmes, nbpersmor);
        setChart3((ChartPanel) jPanel1, nbhommes, nbfemmes, nbpersmor);
         } else if (i==3){
             
              getHighestTontine();
              setChart2((ChartPanel) jPanel1);
        
        
             
         } else if (i==4) {
             
             getHighestEpargne();
             setChart3((ChartPanel) jPanel1);
             
             
         } else if (i==5) {
             
             getHighestRevenue();
             setChart4((ChartPanel) jPanel1);
             
             
         } 
        
        

        
//        getTontine(2010, 2019);
//        System.out.println("m"+m.keySet());
//        setChart((ChartPanel) jPanel1);
        
        // Percentage 
//        getpercentage();
//        getplotnumberpercentage(nbhommes, nbfemmes, nbpersmor);
//        setChart3((ChartPanel) jPanel1, nbhommes, nbfemmes, nbpersmor);
        
        // Most highest revenue in Tontine
      
        
        
    }
    
    
      public Statistics(int i, int year1, int year2) throws SQLException {
        this.m = new TreeMap<>();
        this.m2 = new HashMap<>();
        this.m3 = new HashMap<>();
        ftchash= new HashMap<String, ArrayList<Date>>();
      
        initComponents();
        if (i==0) { getEpargne(year1, year2);
                     setChart((ChartPanel) jPanel1);
        
        
        } else if (i==1){ 
            
            getTontine(year1, year2);
            setChart((ChartPanel) jPanel1);
            
        
        
        } 
//        getTontine(2010, 2019);
//        System.out.println("m"+m.keySet());
//        setChart((ChartPanel) jPanel1);
        
        // Percentage 
//        getpercentage();
//        getplotnumberpercentage(nbhommes, nbfemmes, nbpersmor);
//        setChart3((ChartPanel) jPanel1, nbhommes, nbfemmes, nbpersmor);
        
        // Most highest revenue in Tontine
      
        
        
        
        
    }

    public static <K, V extends Comparable<V>> Map<K, V> 
  sortByValues(final Map<K, V> map) {
    Comparator<K> valueComparator = 
             new Comparator<K>() {
      public int compare(K k1, K k2) {
        int compare = 
              map.get(k1).compareTo(map.get(k2));
        if (compare == 0) 
          return 1;
        else 
          return -compare;
      }
    };
 
    Map<K, V> sortedByValues = 
      new TreeMap<K, V>(valueComparator);
    sortedByValues.putAll(map);
    return sortedByValues;
    }
    

    public void setChart3 (ChartPanel p, int n1, int n2, int n3) {

            

            final JFreeChart rgChart = getplotnumberpercentage(n1, n2, n3);
            p.setChart(rgChart);
        
    } 
    
    
    
      public JFreeChart getChart () {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries t1 = new TimeSeries("Commission sur Tontine");
//        for (int i=0; i< jTable4.getRowCount(); i++) {
//            t1.add(new Day((Date)jTable4.getValueAt(i, 0)), (double) jTable4.getValueAt(i, 4));
//        }
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
    
    
    public void getEpargne(int beginYear, int stopYear) throws SQLException {
        conn= Connect.ConnectDb();
        m.clear();
        String sql = "SELECT YEAR(DateEpargne), SUM(MontantEpargne) FROM Epargne WHERE DateEpargne >= '" +new java.sql.Date(ini.initdate.getTime())+ "' AND  YEAR(DateEpargne) >="+beginYear+" AND YEAR(DateEpargne) <="+stopYear+" GROUP BY YEAR(DateEpargne) ORDER BY YEAR(DateEpargne)";
        System.out.println("SQL vaut"+sql);
        PreparedStatement pre1=conn.prepareStatement(sql);
        ResultSet rs1 = pre1.executeQuery();
        while (rs1.next()) {
            m.put(rs1.getInt(1), rs1.getDouble(2));
            System.out.println("Year"+rs1.getInt(1)+"Value"+rs1.getInt(2));
        }
        
        if (conn != null) conn.close();
        if (pre1 != null) pre1.close();
        if (rs1 != null) rs1.close();
    }
    
    
    
    public void getTontine(int beginYear, int stopYear) throws SQLException {
        m.clear();
        conn = Connect.ConnectDb();    
        PreparedStatement pre, pre2;
        pre = conn.prepareStatement("SELECT YEAR(DateTontine), SUM((bit_count(JoursTontine))*Mise) FROM Tontine  WHERE DateTontine >= '"+new java.sql.Date(ini.initdate.getTime())+ "' AND (bit_count(JoursTontine)) >=1 AND YEAR(DateTontine) <='" + stopYear + "' AND YEAR(DateTontine) >='"+ beginYear+"' GROUP BY YEAR(DateTontine)");
        pre2 = conn.prepareStatement("SELECT YEAR(DateRet), SUM(Montant) FROM retraits_tontine WHERE DateRet >= '"+new java.sql.Date(ini.initdate.getTime())+ "'AND YEAR(DateRet) <='" + stopYear+"' AND YEAR(DateRet) >= '"+beginYear+"' GROUP BY YEAR(DateRet);");
        
        ResultSet rs1 = pre.executeQuery();
        ResultSet rs2 = pre2.executeQuery();
        m.clear();
        while (rs1.next()) {
            m.put(rs1.getInt(1), rs1.getDouble(2));
            System.out.println("Year"+rs1.getInt(1)+"Value"+rs1.getInt(2));
        }
        
        while (rs2.next()) {
            if (m.get(rs2.getInt(1)) != null) {
                m.put(rs2.getInt(1), m.get(rs2.getInt(1))- rs2.getDouble(2));
            }                       
        }    
    }
    
   public void getHighestEpargne() throws SQLException, Exception {
        conn = Connect.ConnectDb();    
        PreparedStatement pre;
        String sql= "SELECT id, type FROM (SELECT idProfil_enfant as id, 'Enfant' as type FROM Profil_enfant WHERE lower(Type_adhesion) LIKE '%epargne%' or lower(Type_adhesion) LIKE '%épargne%' ";
        sql = sql + " UNION ALL"+ " SELECT idProfil_Adulte as id, 'Adulte' as type FROM Profil_adulte WHERE lower(Type_adhesion) LIKE '%epargne%' or lower(Type_adhesion) LIKE '%épargne%' ";
        sql = sql + " UNION ALL"+ " SELECT idProfil_persmorale as id, 'Pers Morale' as type FROM Profil_persmorale WHERE lower(Type_adhesion) LIKE '%epargne%' or lower(Type_adhesion) LIKE '%épargne%') summpeople";
        Date now = new Date();
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        while(rs.next()){
          m3.put(getfullname(rs.getInt(1), rs.getString(2)), getReport(rs.getString(2), rs.getInt(1), now));  
            
        }
               
    
       
   }   
    
    
    public void getHighestTontine() throws SQLException {
        
        conn = Connect.ConnectDb();    
        PreparedStatement pre;
        pre = conn.prepareStatement("SELECT entrees, depens, IFNULL(entrees,0)-IFNULL(depens,0) as rev, IdEpargnant, TypeEpargnant FROM (SELECT entrees, depens,  epargnes.IdEpargnant, epargnes.TypeEpargnant FROM (SELECT SUM(bit_count(JoursTontine-1)*Mise) as entrees, IdEpargnant, TypeEpargnant FROM Tontine WHERE DateTontine >= '"+new java.sql.Date(ini.initdate.getTime())+ "' GROUP BY IdEpargnant, TypeEpargnant) AS epargnes\n" +
"LEFT JOIN \n" +
"(SELECT SUM(Montant) as depens, IdEpargnant, TypeEpargnant  FROM retraits_tontine WHERE DateRet >= '"+new java.sql.Date(ini.initdate.getTime())+ "' GROUP BY IdEpargnant, TypeEpargnant)  AS retr \n" +
"ON epargnes.IdEpargnant=retr.IdEpargnant AND epargnes.TypeEpargnant=retr.TypeEpargnant\n" +
"\n" +
"UNION \n" +
"\n" +
"SELECT entrees, depens,  epargnes.IdEpargnant, epargnes.TypeEpargnant  FROM (SELECT SUM(bit_count(JoursTontine-1)*Mise) as entrees, IdEpargnant, TypeEpargnant FROM Tontine WHERE DateTontine >= '"+new java.sql.Date(ini.initdate.getTime())+ "' GROUP BY IdEpargnant, TypeEpargnant) AS epargnes\n" +
"RIGHT JOIN \n" +
"(SELECT SUM(Montant) as depens, IdEpargnant, TypeEpargnant  FROM retraits_tontine WHERE DateRet >= '"+new java.sql.Date(ini.initdate.getTime())+ "' GROUP BY IdEpargnant, TypeEpargnant)  AS retr \n" +
"ON epargnes.IdEpargnant=retr.IdEpargnant AND epargnes.TypeEpargnant=retr.TypeEpargnant) Summ ORDER BY rev DESC;"); // removed limit 10
        
        ResultSet rs1 = pre.executeQuery();
        
        while (rs1.next()) {
            if (rs1.getObject(5) !=null){
            if (rs1.getString(5).equalsIgnoreCase("enfant")) {
            m2.put(getfullname(rs1.getInt(4), "enfant"), rs1.getDouble(3));
                System.out.println("key" + getfullname(rs1.getInt(4), "enfant") + "value "+ m2.get(getfullname(rs1.getInt(4), "enfant")));

            } else if (rs1.getString(5).equalsIgnoreCase("adulte")) {
            m2.put(getfullname(rs1.getInt(4), "adulte"), rs1.getDouble(3));
                System.out.println("key" + getfullname(rs1.getInt(4), "adulte") + "value "+ m2.get(getfullname(rs1.getInt(4), "adulte")));

            } else {
            m2.put(getfullname(rs1.getInt(4), "pers morale"), rs1.getDouble(3));    
                System.out.println("key" + getfullname(rs1.getInt(4), "pers morale") + "value "+ m2.get(getfullname(rs1.getInt(4), "pers morale")));

            }
            }
            
            
        }
        
        
        
        
        
    }
    
    public void getHighestRevenue() throws SQLException, Exception {
        
        getHighestEpargne();
        getHighestTontine();
        
        m4 = mergeAndAdd(m2, m3);
        
        
        
    }
    
    
    public JFreeChart getplotnumberpercentage (int nbho, int nbfem, int nbpers) {
      DefaultPieDataset dataset = new DefaultPieDataset();
      dataset.setValue("Hommes "+nbho , nbho);  
      dataset.setValue("Femmes "+nbfem , nbfem);   
      dataset.setValue("Personnes morales "+nbpers, nbpers);    
      RingPlot plot = new RingPlot(dataset);
      plot.setSeparatorStroke( new BasicStroke(10));
      plot.setSeparatorPaint(new Color(255, 255, 255));
      plot.setShadowPaint(null);
      plot.setOutlineVisible(false);
      //StringBuffer chartFileName = new StringBuffer(Integer.toString(generatedCharts)).append(Long.toString(System.currentTimeMillis())).append(".png");

      JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

TextTitle t = chart.getTitle();
t.setHorizontalAlignment(org.jfree.ui.HorizontalAlignment.LEFT);
t.setPaint(new Color(240, 240, 240));
t.setFont(new Font("Arial", Font.BOLD, 26));

plot.setBackgroundPaint(new Color(255, 255, 255));
plot.setOutlineVisible(false);
plot.setLabelGenerator(null);
plot.setSectionDepth(0.35);
plot.setSectionOutlinesVisible(false);
plot.setSimpleLabels(true);
plot.setShadowPaint(null);
plot.setOuterSeparatorExtension(0);
plot.setInnerSeparatorExtension(0);
//plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}",new DecimalFormat("#,##0"), new DecimalFormat("0.000%")));
plot.setLabelBackgroundPaint(null);
plot.setLabelOutlinePaint(null);

Font font=new Font("",0,16);
plot.setLabelFont(font);
return chart;

//chart.getLegend().setFrame(BlockBorder.NONE);
//hart.getLegend().setPosition(RectangleEdge.BOTTOM); 
//chart.setBackgroundPaint(java.awt.Color.white);
//chart.setPadding(new RectangleInsets(4, 8, 2, 2));
}
    
    public void getpercentage() throws SQLException {
        
        conn = Connect.ConnectDb();   
     
        PreparedStatement pre, pre2, pre3;
        pre = conn.prepareStatement("SELECT COUNT(*) FROM ((SELECT idProfil_adulte as id FROM Profil_adulte  WHERE UPPER(Sexe) LIKE 'HOMME%' or UPPER(Sexe) LIKE 'MASCULIN%') UNION ALL (SELECT idProfil_enfant as id FROM Profil_enfant  WHERE UPPER(Sexe) LIKE 'HOMME%' or UPPER(Sexe) LIKE 'MASCULIN%')) test0");
        pre2 = conn.prepareStatement("SELECT COUNT(*) FROM ((SELECT idProfil_adulte as id FROM Profil_adulte  WHERE UPPER(Sexe) LIKE 'FÉMININ%' or UPPER(Sexe) LIKE 'FEMININ%' or UPPER(Sexe) LIKE 'FEMME%') UNION ALL (SELECT idProfil_enfant as id FROM Profil_enfant  WHERE UPPER(Sexe) LIKE 'FEMME%' or UPPER(Sexe) LIKE 'FEMININ%' or UPPER(Sexe) LIKE 'FÉMININ%')) test1");
        pre3 = conn.prepareStatement("SELECT COUNT(*) FROM Profil_persmorale");
        ResultSet rs1 =pre.executeQuery();
        ResultSet rs2 = pre2.executeQuery();
        ResultSet rs3 = pre3.executeQuery();
        
        if (rs1.next()) {nbhommes = rs1.getInt(1);}
        if (rs2.next()) {nbfemmes = rs2.getInt(1);}
        if (rs3.next()) {nbpersmor= rs3.getInt(1);}
        
        
        System.out.println("nombre de femmes"+nbfemmes);
        System.out.println("nombre d'hommes"+nbhommes);
        System.out.println("nombre personnes morales"+nbpersmor);

    }
    
    public void setChart (ChartPanel p){
 // pasted 
           
            final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for(int year : m.keySet()) {
                final Double nb = m.get(year);
                dataset.addValue( (Number) nb, "Montant", year);
            }

            final JFreeChart barChart = ChartFactory.createBarChart("Montant","Année","Chiffres", /**/
                    dataset, PlotOrientation.VERTICAL, true, true, false);
            p.setChart(barChart);
//            final ChartPanel cPanel = new ChartPanel(barChart);

         //   poidsJdialog.getContentPane().add(cPanel, CENTER);

         //   poidsJdialog.pack();
         //   poidsJdialog.setVisible(true);
         
//        TimeSeriesCollection dataset = new TimeSeriesCollection();
//        TimeSeries t1 = new TimeSeries("Solde par mois");
//        for (int i=0; i< jTable4.getRowCount()-1; i++) {
//            System.out.println("Date"+(Date)jTable4.getValueAt(i, 0));
//           // System.out.println("dttype"+jTable1.getValueAt(i, 0));
//            t1.add(new Day((Date)jTable4.getValueAt(i, 0)), (double) jTable4.getValueAt(i, 4));
//        }
////        XYSeries series = new XYSeries("MyGraph");
////        series.add(0, 1);
////        series.add(1, 2);
////        series.add(2, 5);
////        series.add(7, 8);
////        series.add(9, 10);
//        dataset.addSeries(t1);
////        
////        XYSeriesCollection dataset = new XYSeriesCollection();
////        dataset.addSeries(series);
//        TimeSeries t2 = new TimeSeries("FTC");
//        for (int i=0; i< jTable4.getRowCount()-1; i++) {
//            System.out.println("Date"+(Date)jTable4.getValueAt(i, 0));
//           // System.out.println("dttype"+jTable1.getValueAt(i, 0));
//            t2.add(new Day((Date)jTable4.getValueAt(i, 0)), (double) jTable4.getValueAt(i, 1));
//        }
////        XYSeries series = new XYSeries("MyGraph");
////        series.add(0, 1);
////        series.add(1, 2);
////        series.add(2, 5);
////        series.add(7, 8);
////        series.add(9, 10);
//        dataset.addSeries(t2);
//        
//        
//        JFreeChart chart = ChartFactory.createTimeSeriesChart(
//                "Progression des épargnes pour "+jComboBox1.getSelectedItem().toString(),
//                "Mois",
//                "Montant",
//                dataset);
//        
//      p.setChart(chart);
      

//    Container contenu = getContentPane() ;
//    contenu.add(panel);
        
    } 
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1 = new ChartPanel(getChart());

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1189, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 839, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Get most highest revenues ... 
    public void getMostEpRevenue () throws SQLException {
          conn = Connect.ConnectDb();   
     
        PreparedStatement pre;
        pre = conn.prepareStatement("SELECT id, typeEpargnant, SUM(Montant) FROM Epargne GROUP BY id, typeEpargnant");
        ResultSet rs1 = pre.executeQuery();
        while (rs1.next()) {
           int id = rs1.getInt(1);
           String typeEpargnant = rs1.getString(2);
           int montant = rs1.getInt(3);
           System.out.println("id:"+id+ "typeEpargant"+typeEpargnant+ "montant"+ rs1.getInt(3));
        }
        
        if (conn != null) conn.close();
   //     if (pre1 != null) pre1.close();
        if (rs1 != null) rs1.close();
    }
    
   public boolean isTwentyEighthDayOfTheMonth(Date dateToday) {
        Calendar c = new GregorianCalendar();
        c.setTime(dateToday);
        return c.get(Calendar.DAY_OF_MONTH)==28;
    }
      

 public void setChart3 (ChartPanel p) {   // highest revenue
 // pasted 
         
            final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           
            Map sortedMap = sortByValues(m3);
            
              
    // Get Set of entries
    Set set = sortedMap.entrySet();
    // Get iterator
    Iterator it = set.iterator();
    int k =1;
    // Show TreeMap elements

    while(it.hasNext() && k <= 10) {
      Map.Entry pair = (Map.Entry)it.next();
      dataset.addValue((Number) pair.getValue(), "Montant", (String) pair.getKey());
      System.out.print("Key is: "+pair.getKey() + " and ");
      System.out.println("Value is: "+pair.getValue());
      k++;
    }
            
//            for (String customer : (Set<String>) sortedMap.keySet()) {
//                final Double nb = (Double) sortedMap.get(customer);
//                dataset.addValue( (Number) nb, "Montant", customer);
//            }
            
            Integer width = 500, height = 300;
 
            JFreeChart chart = ChartFactory.createBarChart(
            "",                         // chart title
            "",                         // domain axis label
            "",                         // range axis label
            dataset,            // data
            PlotOrientation.VERTICAL,   // orientation
            true,                       // include legend
            true,                       // tooltips?
            false                       // URLs?
            );
            
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.WHITE);
            plot.setOutlineVisible(false);
         
            
            
            // remove the gradient fill
//            plot.getRenderer(). = null;
            StandardBarPainter painter = new StandardBarPainter();
            BarRenderer.setDefaultBarPainter(painter);
            BarRenderer renderer = new BarRenderer();
            plot.setRenderer(renderer);
            Color blue =     new Color(0, 172, 178);
            Color red =      new Color(239, 70, 55);
            Color green =    new Color(255, 177, 69);
 
            Paint[] colors = new Paint [] { blue, red, green
            };
             
            // change the default colors
            for (int i = 0; i < 4; i++) {
                plot.getRenderer().setSeriesPaint(i, colors[i % colors.length]);
            }
            

//            final JFreeChart barChart = ChartFactory.createBarChart("Montant","Année","Chiffres", /**/
//                    dataset, PlotOrientation.VERTICAL, true, true, false);
            p.setChart(chart);
            
           

        
    } 
	
    
    
    public void setChart2 (ChartPanel p) {   // highest revenue
 // pasted 
         
            final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           
            Map sortedMap = sortByValues(m2);
            
              
    // Get Set of entries
    Set set = sortedMap.entrySet();
    // Get iterator
    Iterator it = set.iterator();
    int k = 1;  // number wanted
    // Show TreeMap elements

    while(it.hasNext() && k <= 10) {
      Map.Entry pair = (Map.Entry)it.next();
      dataset.addValue((Number) pair.getValue(), "Montant", (String) pair.getKey());
      System.out.print("Key is: "+pair.getKey() + " and ");
      System.out.println("Value is: "+pair.getValue());
      k++;
    }
            
//            for (String customer : (Set<String>) sortedMap.keySet()) {
//                final Double nb = (Double) sortedMap.get(customer);
//                dataset.addValue( (Number) nb, "Montant", customer);
//            }
            
            Integer width = 500, height = 300;
 
            JFreeChart chart = ChartFactory.createBarChart(
            "",                         // chart title
            "",                         // domain axis label
            "",                         // range axis label
            dataset,            // data
            PlotOrientation.VERTICAL,   // orientation
            true,                       // include legend
            true,                       // tooltips?
            false                       // URLs?
            );
            
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.WHITE);
            plot.setOutlineVisible(false);
         
            
            
            // remove the gradient fill
//            plot.getRenderer(). = null;
            StandardBarPainter painter = new StandardBarPainter();
            BarRenderer.setDefaultBarPainter(painter);
            BarRenderer renderer = new BarRenderer();
            plot.setRenderer(renderer);
            Color blue =     new Color(0, 172, 178);
            Color red =      new Color(239, 70, 55);
            Color green =    new Color(255, 177, 69);
 
            Paint[] colors = new Paint [] { blue, red, green
            };
             
            // change the default colors
            for (int i = 0; i < 4; i++) {
                plot.getRenderer().setSeriesPaint(i, colors[i % colors.length]);
            }
            

//            final JFreeChart barChart = ChartFactory.createBarChart("Montant","Année","Chiffres", /**/
//                    dataset, PlotOrientation.VERTICAL, true, true, false);
            p.setChart(chart);
            
           

        
    } 
        
    
    public void setChart4 (ChartPanel p) {   // highest revenue
 // pasted 
         
            final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           
            Map sortedMap = sortByValues(m4);
            
              
    // Get Set of entries
    Set set = sortedMap.entrySet();
    // Get iterator
    Iterator it = set.iterator();
    int k = 1;  // number wanted
    // Show TreeMap elements

    while(it.hasNext() && k <= 10) {
      Map.Entry pair = (Map.Entry)it.next();
      dataset.addValue((Number) pair.getValue(), "Montant", (String) pair.getKey());
      System.out.print("Key is: "+pair.getKey() + " and ");
      System.out.println("Value is: "+pair.getValue());
      k++;
    }
            
//            for (String customer : (Set<String>) sortedMap.keySet()) {
//                final Double nb = (Double) sortedMap.get(customer);
//                dataset.addValue( (Number) nb, "Montant", customer);
//            }
            
            Integer width = 500, height = 300;
 
            JFreeChart chart = ChartFactory.createBarChart(
            "",                         // chart title
            "",                         // domain axis label
            "",                         // range axis label
            dataset,            // data
            PlotOrientation.VERTICAL,   // orientation
            true,                       // include legend
            true,                       // tooltips?
            false                       // URLs?
            );
            
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.WHITE);
            plot.setOutlineVisible(false);
         
            
            
            // remove the gradient fill
//            plot.getRenderer(). = null;
            StandardBarPainter painter = new StandardBarPainter();
            BarRenderer.setDefaultBarPainter(painter);
            BarRenderer renderer = new BarRenderer();
            plot.setRenderer(renderer);
            Color blue =     new Color(0, 172, 178);
            Color red =      new Color(239, 70, 55);
            Color green =    new Color(255, 177, 69);
 
            Paint[] colors = new Paint [] { blue, red, green
            };
             
            // change the default colors
            for (int i = 0; i < 4; i++) {
                plot.getRenderer().setSeriesPaint(i, colors[i % colors.length]);
            }
            

//            final JFreeChart barChart = ChartFactory.createBarChart("Montant","Année","Chiffres", /**/
//                    dataset, PlotOrientation.VERTICAL, true, true, false);
            p.setChart(chart);
            
           

        
    } 
    
    
    
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
            java.util.logging.Logger.getLogger(Statistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Statistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Statistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Statistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Statistics(0).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
