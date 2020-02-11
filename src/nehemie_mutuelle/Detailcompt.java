/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import org.apache.commons.lang.time.DateUtils;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

/**
 *
 * @author elommarcarnold
 */
public class Detailcompt extends javax.swing.JFrame implements ActionListener {
    private Connection conn;
    private Date beginDate;
    private Date endDate;
    private Double sumdep;
    private Double sumretr;
    private Double sumadhep;
    private Double sumadhtont;
    private Double sumcommtont;
    private Double ftcsomme; 
    private Vector<Vector<Object>> datadep; // Data for dep
    private Vector<Vector<Object>> dataretr; // Data for dep
    private Vector <String> headerdep = new Vector<String>();
    private JPopupMenu popupMenu;
    private JMenuItem menuItemMod;
    private JMenuItem menuItemSup;
    private final int fraisadhep;
    private final int fraisadtont;
    public List<Date> ftcexceptionlist = new ArrayList<>();
    private Map <String, ArrayList<Date>> ftchash;
    private Map <Date, ArrayList<String>> ftchashreverse;
    private PreparedStatement pre;
    private IntializeDate dateini; 


    /** Creates new form Detailcompt */
    public Detailcompt() throws SQLException {
        IntializeDate dateini = new IntializeDate();
        fraisadhep = 2500;   // To do remplacer les valeurs
        fraisadtont = 500;
        ftcsomme = 0.0;
       
        initComponents();
      
    }
    
    
    public void setTab(int i){
        jTabbedPane1.setSelectedIndex(i);
    }
    
    
  private String getchildfname(int idenf) throws SQLException {
     String result="";
     conn = Connect.ConnectDb2();
     String sql = "SELECT Prenoms FROM Profil_enfant WHERE idProfil_enfant='"+idenf+"'";
     Statement stmt = conn.createStatement();
     ResultSet rst =stmt.executeQuery(sql);
     if (rst.next()) result= rst.getString(1);
     
     conn.close();
     rst.close();
     stmt.close();
     return result;
}
    
  private String getchildlstname(int idenf) throws SQLException {
     String result="";
     conn = Connect.ConnectDb2();
     String sql = "SELECT Nom FROM Profil_enfant WHERE idProfil_enfant='"+idenf+"'";
     Statement stmt = conn.createStatement();
     ResultSet rst =stmt.executeQuery(sql);
     if (rst.next()) result= rst.getString(1);
     
     conn.close();
     rst.close();
     stmt.close();
     return result;
}
   
  private String getadultfname(int idadult) throws SQLException{
     String result="";
     conn = Connect.ConnectDb2();
     String sql = "SELECT Prenoms FROM Profil_adulte WHERE idProfil_adulte='"+idadult+"'";
     Statement stmt = conn.createStatement();
     ResultSet rst =stmt.executeQuery(sql);
     if (rst.next()) result= rst.getString(1);
     
     conn.close();
     rst.close();
     stmt.close();
     return result; 
        
}
    
   private String getadultlname(int idadult) throws SQLException {
     String result="";
     conn = Connect.ConnectDb2();
     String sql = "SELECT Noms FROM Profil_adulte WHERE idProfil_adulte='"+idadult+"'";
     Statement stmt = conn.createStatement();
     ResultSet rst =stmt.executeQuery(sql);
     if (rst.next()) result= rst.getString(1);
     
     conn.close();
     rst.close();
     stmt.close();
     return result; 
}
    
   
private String getpersmor(int idpersmor) throws SQLException{
     String result="";
     conn = Connect.ConnectDb2();
     String sql = "SELECT Raison_sociale FROM Profil_persmorale WHERE idProfil_persmorale='"+idpersmor+"'";
     Statement stmt = conn.createStatement();
     ResultSet rst =stmt.executeQuery(sql);
     if (rst.next()) result= rst.getString(1);
     
     conn.close();
     rst.close();
     stmt.close();
     return result; 
        
        
        
   }

    public class YMDRenderer extends DefaultTableCellRenderer
{
    private Format formatter = new SimpleDateFormat("yy/MM/dd");
 
    public void setValue(Object value)
    {
        //  Format the Object before setting its value in the renderer
 
        try
        {
            if (value != null)
                value = formatter.format(value);
        }
        catch(IllegalArgumentException e) {}
 
        super.setValue(value);
    }
}
    
    
    // Juste un test
    
    private  MembDataNode createDataStructure() throws SQLException {
         SimpleDateFormat  sd = new SimpleDateFormat("dd-MM-yyyy");
         List<MembDataNode> rootNodes = new ArrayList<MembDataNode>();
      
         List<Date> sortedKeys=new ArrayList(ftchashreverse.keySet());
         System.out.println("sortedkeys"+sortedKeys);
        Collections.sort(sortedKeys);
        for (Date dat : sortedKeys) {
            ArrayList<String> value = ftchashreverse.get(dat);
            List<MembDataNode> children = new ArrayList<MembDataNode>();
            for (String str: value) {
                 String[] splited = str.split("\\s+");
                 System.out.println("splitted"+str+"date"+dat);
                 String nom="";
                 String prenoms="";
                 String typeep=splited[0];
                 int ide =0;
                 System.out.println("typeep"+typeep);
                 if (!splited[1].equalsIgnoreCase("morale")) ide = Integer.parseInt(splited[1]);
                 if(typeep.equals("Adulte")){nom = getadultlname(ide);   prenoms=getadultfname(ide);}
                 if(typeep.equals("Enfant")){nom = getchildlstname(ide); prenoms=getchildfname(ide);}
                 if(typeep.equalsIgnoreCase("pers")){ide = Integer.parseInt(splited[2]); nom = getpersmor(ide);}
                 
              //  ftcsomme = ftcsomme + 100; 
                 ftcsomme =  ftcsomme + 100; 
                children.add(new MembDataNode(null,nom, prenoms,null));
                
            }
            
            
            rootNodes.add(new MembDataNode(sd.format(dat),"", "", children));
        }
        
       MembDataNode root = new MembDataNode(sd.format(new Date()), "R1", "R2",  rootNodes);

//        List<MyDataNode> children1 = new ArrayList<MyDataNode>();
//        children1.add(new MyDataNode("N12", "C12", new Date(), Integer.valueOf(50), null));
//        children1.add(new MyDataNode("N13", "C13", new Date(), Integer.valueOf(60), null));
//        children1.add(new MyDataNode("N14", "C14", new Date(), Integer.valueOf(70), null));
//        children1.add(new MyDataNode("N15", "C15", new Date(), Integer.valueOf(80), null));
// 
//        List<MyDataNode> children2 = new ArrayList<MyDataNode>();
//        children2.add(new MyDataNode("N12", "C12", new Date(), Integer.valueOf(10), null));
//        children2.add(new MyDataNode("N13", "C13", new Date(), Integer.valueOf(20), children1));
//        children2.add(new MyDataNode("N14", "C14", new Date(), Integer.valueOf(30), null));
//        children2.add(new MyDataNode("N15", "C15", new Date(), Integer.valueOf(40), null));
// 
//        List<MyDataNode> rootNodes = new ArrayList<MyDataNode>();
//        rootNodes.add(new MyDataNode("N1", "C1", new Date(), Integer.valueOf(10), children2));
//        rootNodes.add(new MyDataNode("N2", "C2", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N3", "C3", new Date(), Integer.valueOf(10), children2));
//        rootNodes.add(new MyDataNode("N4", "C4", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N5", "C5", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N6", "C6", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N7", "C7", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N8", "C8", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N9", "C9", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N10", "C10", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N11", "C11", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N12", "C7", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N13", "C8", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N14", "C9", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N15", "C10", new Date(), Integer.valueOf(10), children1));
//        rootNodes.add(new MyDataNode("N16", "C11", new Date(), Integer.valueOf(10), children1));
     
 
        return root;
    }
    
     public class MembDataNode {
 
    private String name;
    private String pren;
    private int summ;
   
//    private Date decnlared;
//    private Integer area;
 
    private List<MembDataNode> children;
        private  String dat;
 
    public MembDataNode(String dat, String name, String pren, List<MembDataNode> children) {
        this.dat=dat;
        this.name = name;
        this.pren = pren;
        this.children = children;
 
        if (this.children == null) {
            this.children = Collections.emptyList();
        }
    }
 
    public String getName() {
        return name;
    }
 
    public String getPren() {
        return pren;
    }
    
    public String getDate() {
        return dat;
    }
    
   public int getSumm() {
       
        if (this.children.isEmpty()) return 100;
        else {return 100*this.children.size();}
    }
 
   
    public List<MembDataNode> getChildren() {
        return children;
    }
 
    /**
     * Knotentext vom JTree.
     */
    public String toString() {
        return name;
    }
    }
    
    
    public class MyDataNode {
 
    private String name;
    private String capital;
    private Date declared;
    private Integer area;
 
    private List<MyDataNode> children;
 
    public MyDataNode(String name, String capital, Date declared, Integer area, List<MyDataNode> children) {
        this.name = name;
        this.capital = capital;
        this.declared = declared;
        this.area = area;
        this.children = children;
 
        if (this.children == null) {
            this.children = Collections.emptyList();
        }
    }
 
    public String getName() {
        return name;
    }
 
    public String getCapital() {
        return capital;
    }
 
    public Date getDeclared() {
        return declared;
    }
 
    public Integer getArea() {
        return area;
    }
 
    public List<MyDataNode> getChildren() {
        return children;
    }
 
    /**
     * Knotentext vom JTree.
     */
    public String toString() {
        return name;
    }
}
    
 public class MembDataModel extends AbstractTreeTableModel {
  
  private final String[] columnNames = { "Date", "Nom/RS", "Prénoms", "Integer" };
    // Spalten Typen.
   // static protected Class<?>[] columnTypes = { MyTreeTableModel.class, String.class, Date.class, Integer.class };
 
    public MembDataModel(MembDataNode rootNode) {
        super(rootNode);
        root = rootNode;
    }
 
    public Object getChild(Object parent, int index) {
        return ((MembDataNode) parent).getChildren().get(index);
    }
 
 
    public int getChildCount(Object parent) {
        return ((MembDataNode) parent).getChildren().size();
    }
 
 
    public int getColumnCount() {
        return columnNames.length;
    }
 
 
    public String getColumnName(int column) {
        return columnNames[column];
    }
 
 
//    public Class<?> getColumnClass(int column) {
//        return columnTypes[column];
//    }
 
    public Object getValueAt(Object node, int column) {
        switch (column) {
        case 0:
            return ((MembDataNode) node).getDate();
        case 1:
            return ((MembDataNode) node).getName();
        case 2:
            return ((MembDataNode) node).getPren();
        case 3:
             return ((MembDataNode) node).getSumm();
  
        default:
            break;
        }
        return null;
    }
 
    public boolean isCellEditable(Object node, int column) {
        return true; // Important to activate TreeExpandListener
    }
 
    public void setValueAt(Object aValue, Object node, int column) {
    }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
 
}
    
    public class MyDataModel extends AbstractTreeTableModel {
    // Spalten Name.
   //   private final String[] columnNames = { "Knotentext", "String", "Datum", "Integer" };
  private final String[] columnNames = { "Date", "Nom/RS", "Prénoms", "Integer" };
    // Spalten Typen.
   // static protected Class<?>[] columnTypes = { MyTreeTableModel.class, String.class, Date.class, Integer.class };
 
    public MyDataModel(MyDataNode rootNode) {
        super(rootNode);
        root = rootNode;
    }
 
    public Object getChild(Object parent, int index) {
        return ((MyDataNode) parent).getChildren().get(index);
    }
 
 
    public int getChildCount(Object parent) {
        return ((MyDataNode) parent).getChildren().size();
    }
 
 
    public int getColumnCount() {
        return columnNames.length;
    }
 
 
    public String getColumnName(int column) {
        return columnNames[column];
    }
 
 
//    public Class<?> getColumnClass(int column) {
//        return columnTypes[column];
//    }
 
    public Object getValueAt(Object node, int column) {
        switch (column) {
        case 0:
            return ((MyDataNode) node).getName();
        case 1:
            return ((MyDataNode) node).getCapital();
        case 2:
            return ((MyDataNode) node).getDeclared();
        case 3:
            return ((MyDataNode) node).getArea();
        default:
            break;
        }
        return null;
    }
 
    public boolean isCellEditable(Object node, int column) {
        return true; // Important to activate TreeExpandListener
    }
 
    public void setValueAt(Object aValue, Object node, int column) {
    }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
 
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
    
    public synchronized void addToList(Date mapKey, String myItem) {
    List<String> itemsList = ftchashreverse.get(mapKey);

    // if list does not exist create it
    if(itemsList == null) {
         itemsList = new ArrayList<String>();
         itemsList.add(myItem);
         ftchashreverse.put(mapKey, (ArrayList<String>) itemsList);
    } else {
        // add if item is not already in list
        if(!itemsList.contains(myItem)) itemsList.add(myItem);
    }
}
    
    public void reverseftchash () {
        ftchashreverse = new HashMap<Date, ArrayList<String>>();
        for (String str : ftchash.keySet()) {
            ArrayList<Date> value = ftchash.get(str);
            for (Date d: value) {
                
                addToList(d, str);
                if(d.getYear()==119) System.out.println("cest 2019");
                 if(d.getYear()==118) System.out.println("cest 2018");
            }
        }
        
        System.out.println("ftchashreverse"+ftchashreverse);
    }
    
    // FTC functions 
    public boolean isTwentyEighthDayOfTheMonth(Date dateToday){
        Calendar c = new GregorianCalendar();
        c.setTime(dateToday);
        return c.get(Calendar.DAY_OF_MONTH)==28;
    }
    
    public void fillExceptionList (int IdEpargnant, String typeEpargnant) throws SQLException {
        if(!ftcexceptionlist.isEmpty()) ftcexceptionlist.clear();
        conn = Connect.ConnectDb();
        String sql= "SELECT Dateftc FROM Exceptionftc WHERE IdEpargnant ='"+IdEpargnant+"' AND TypeEpargnant='"+typeEpargnant+"'";
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
    
    
//    public void fillftctable(String typeEpargnant, int idEpargnant) throws Exception {
//    
//        ftchash.put(typeEpargnant+" "+idEpargnant, new ArrayList<Date>());
//        fillExceptionList(idEpargnant, typeEpargnant);
//        conn = Connect.ConnectDb2();
//        pre = conn.prepareStatement("SELECT id AS ide, w AS dte, d AS description, \n" +
//"   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n" +
//"   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n" +
//"  FROM\n" +
//"  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n" +
//"          Epargne.MontantEpargne AS a\n" +
//"     FROM Epargne\n" +
//"     WHERE IdEpargnant='" + idEpargnant + "' AND TypeEpargnant='" + typeEpargnant + "'\n" +          
//"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne) t");
//
//        ResultSet rs = pre.executeQuery();
//        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();
//
//        
//        Double balance = 0d;
//        boolean firstentry=true;
//        Date previous = null;
//        Date date=new Date();
//        Date d=new Date(); 
//        boolean onceftc=false;
//        while (rs.next()) {
//            if (firstentry) previous = sdf2.parse(rs.getString("dte"));   // modified
//             date=sdf2.parse(rs.getString("dte"));
//             // Modified 
//            GregorianCalendar gcal= new GregorianCalendar();
//            gcal.setTime(previous);
//            gcal.set(Calendar.MILLISECOND, 0);
//            gcal.set(Calendar.SECOND, 0); 
//            gcal.set(Calendar.MINUTE, 0);
//            gcal.set(Calendar.HOUR_OF_DAY, 0);       
//            while (!gcal.getTime().after(date)) {
//                 d = gcal.getTime();
//                if(isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry==false) && !ftcexceptionlist.contains(d)) {
//                    balance =balance-100;
//                    onceftc=true;
//                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d);
//                }
//                gcal.add(Calendar.DAY_OF_YEAR, 1);
//            }             
//            balance= balance -rs.getDouble("cshOUT") +rs.getDouble("cshIN");           
//            previous=date; // modified
//            firstentry= false; //modified
//        }
//        
//        // retraits des frais de tenue de compte jusqu'au jour actuel
//       
//          if (!firstentry) {
//           Date date2=new Date();
//             // Modified 
//            GregorianCalendar gcal= new GregorianCalendar();
//            gcal.setTime(previous);
//            gcal.set(Calendar.MILLISECOND, 0);
//            gcal.set(Calendar.SECOND, 0); 
//            gcal.set(Calendar.MINUTE, 0);
//            gcal.set(Calendar.HOUR_OF_DAY, 0);
//            
//            
//            while (!gcal.getTime().after(date2)) {
//          
//                Date d2 = gcal.getTime();
//                if(isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && ftcexceptionlist.contains(d2) && (!(isTwentyEighthDayOfTheMonth(previous) && onceftc==true &&  DateUtils.isSameDay(d2,d)))) {     
//                    balance =balance-100;
//                    ftchash.get(typeEpargnant+" "+idEpargnant).add(d2);
//                  
//                }
//                gcal.add(Calendar.DAY_OF_YEAR, 1);
//            }
//         }
//
//        /*Close the connection after use (MUST)*/
//        if (conn != null) {
//            conn.close();
//        }
//        
//          if (pre != null) {
//            pre.close();
//        }
//         
//         if (rs != null) {
//            rs.close();
//        }
//        
//        System.out.println("ftchashde detail"+ftchash);
//    }

    
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
                if(isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry==false) && !ftcexceptionlist.contains(d) ) {
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
                if(isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && ! ftcexceptionlist.contains(d2)  &&  (!(isTwentyEighthDayOfTheMonth(previous) && onceftc==true &&  DateUtils.isSameDay(d2,d)))) {     
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


private int countftc(Date date) {
        int count=0;
        for (ArrayList<Date> value: ftchash.values()) {
         //   System.out.println("value"+value);
            if(value.contains((Date) date)) {
                System.out.println("true");
                count++;
            }
        }
        
        return count;
        
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
            System.out.println("mnthcount"+mnthcount);
            count= count + mnthcount;
            
            
        }
            
       return (double) count;  
         
     }
    
     public class FtcDetailTreeTableModel extends AbstractTreeTableModel {
            private final String[] COLUMN_NAMES = new String[] {
               "Nom", "Prénoms"      
            };
            
            private final String ROOT ="_ROOT_";
            private final Map<Date, ArrayList<String>> groupedftcDetails;
            private final List groups;
            
            public FtcDetailTreeTableModel(Map<Date, ArrayList<String>> groupedftcDetails){
                this.groupedftcDetails= groupedftcDetails;
                groups = getGroups(groupedftcDetails);
             //   groups =    to implement 
                        
                
            }
            
            @Override
            public int getColumnCount() {
                return COLUMN_NAMES.length+1;
            }
            
            @Override
            public String getColumnName(int column){
                if(column == 0) {
                    //return  "Group";
                    return ROOT;
                }
                return COLUMN_NAMES[column -1];
                
            }
            
            @Override
            public boolean isCellEditable(Object node, int column) {
                 return false;
            }
            
             @Override
             public boolean isLeaf(Object node) {
                 return node instanceof String;
             }

            @Override
            public Object getValueAt(Object node, int column) {
                if (node instanceof Date) {
                    if(column == 0) {
                       return node;
                    }
                    
                    return COLUMN_NAMES[column - 1];
                 } else if (node instanceof String) {
                    if (column == 0) {
                            return null;
                    }
               return displayColumnValue((String) node, column - 1); //after 
             }
 
             return null;
            }
            

            @Override
	public Object getChild(Object parent, int index) {
		if (ROOT.equals(parent)) {
                    System.out.println("true yhs");
			return groups.get(index);
		} else if (parent instanceof Date) {
                    System.out.println("false this one too");
			return groupedftcDetails.get(parent).get(index);
		}

		return null;
	}
        
        

        @Override
	public int getChildCount(Object parent) {
		if (ROOT.equals(parent)) {
			return groups.size();
		} else if (parent instanceof Date) {
			return groupedftcDetails.get(parent).size();
		}
		return 0;
	}

           @Override
	public int getIndexOfChild(Object parent, Object child) {
       // Date dept = (Date) parent;
        String emp = (String) child;
        return groupedftcDetails.get(parent).indexOf(emp);
	}

	private List<Date> getGroups(Map<Date, ArrayList<String>> groupedftcDetails) {
		List<Date> groups = new ArrayList<>(groupedftcDetails.keySet());
		Collections.sort(groups);
                System.out.println("groups"+groups);
		return groups;
	}

	private String displayColumnValue(String ftcDetail, int columnIndex) {
            
            
//            return "test";
             
    String[] splited = ftcDetail.split("\\s+");
  
 
		switch (columnIndex) {
                    
		case 0:
			return splited[0];
		case 1:
                        return splited[1];
		default:
			throw new IllegalArgumentException("columnIndex " + columnIndex
					+ " is not handled");
		}
	}
        
    }
    
    public class DateCellEditor extends DefaultCellEditor {
  
    JFormattedTextField textField;
    DateFormat dateFormat;

    /**
     * Constructor.
     */
    public DateCellEditor(String format) {
        this(new SimpleDateFormat(format));
    }
    
   
    /**
     * Constructor.
     */
    public DateCellEditor(DateFormat dateFormat) {
        super(new JFormattedTextField());
        textField = (JFormattedTextField) getComponent();

        this.dateFormat = dateFormat;
        DateFormatter dateFormatter = new DateFormatter(dateFormat);

        textField.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
        textField.setHorizontalAlignment(JTextField.TRAILING);
        textField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        // React when the user presses Enter while the editor is
        // active.  (Tab is handled as specified by
        // JFormattedTextField's focusLostBehavior property.)
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
        textField.getActionMap().put("check", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.isEditValid()) { //The text is invalid.
                    Toolkit.getDefaultToolkit().beep();
                    textField.selectAll();
                } else {
                    try {              //The text is valid,
                        textField.commitEdit();     //so use it.
                        textField.postActionEvent(); //stop editing
                    } catch (java.text.ParseException ex) {
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField ftf = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftf.setValue(value);
        return ftf;
    }

    @Override
    public Object getCellEditorValue() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        Object o = ftf.getValue();
        if (o instanceof Date) {
            return o;
        } else {
            try {
                return dateFormat.parseObject(o.toString());
            } catch (ParseException ex) {
                  
                 Logger.getLogger(Detailcompt.class.getName()).log(Level.FINE, "getCellEditorValue: can't parse {0}", o);
                return null;
            }
        }
    }

    // Override to check whether the edit is valid,
    // setting the value if it is and complaining if
    // it isn't.  If it's OK for the editor to go
    // away, we need to invoke the superclass's version 
    // of this method so that everything gets cleaned up.
    @Override
    public boolean stopCellEditing() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        if (ftf.isEditValid()) {
            try {
                ftf.commitEdit();
            } catch (java.text.ParseException ex) {
            }

        } else { //text is invalid
            Toolkit.getDefaultToolkit().beep();
            textField.selectAll();
            return false; //don't let the editor go away
        }
        return super.stopCellEditing();
    }
    }
     public Detailcompt(Date beginDate, Date endDate) throws Exception {
         
         this.beginDate= beginDate;
         this.endDate= endDate;
         fraisadhep = 2500;
         fraisadtont = 500;
         ftchash= new HashMap<String, ArrayList<Date>>();
         this.buildftchash();
         System.out.println("ftchash"+ftchash);
         this.reverseftchash();
         ftcsomme = 0.0;
         
        initComponents();
      
    }
     
    public class DateCellRenderer extends DefaultTableCellRenderer {
    public DateCellRenderer() { super(); }

    @Override
    public void setValue(Object value) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        setText((value == null) ? "" : sdf.format(value));
    }
 } 
    public Vector  getDep() throws Exception {
    conn = Connect.ConnectDb();
    PreparedStatement pre;
    String sql0 = "SELECT * FROM Entreessorties WHERE daterec >='"+new java.sql.Date(beginDate.getTime())+"' AND daterec <= '"+new java.sql.Date(endDate.getTime())+"' ";
   
    
    
    if (dateini.change) sql0 = sql0+ " AND daterec  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
    sql0 = sql0 + "AND montant < 0 ORDER BY daterec ASC";
    pre = conn.prepareStatement(sql0);
    

    int i=1;  // iterator
    ResultSet rs = pre.executeQuery();
    Vector<Vector> membreVector = new Vector<Vector>();
    sumdep=0.0;
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getDate("daterec")); 
        membre.add(rs.getDouble("montant"));
        sumdep=sumdep+rs.getDouble("montant");
        membre.add(rs.getString("libelle"));
        membre.add(rs.getInt("idEntreessorties"));
        membreVector.add(membre);
        i++;
    }
   
    if(conn!=null)
        conn.close();
    if(rs != null)
        rs.close();
    if(pre !=null)
        pre.close();
    
  

    return membreVector;
}
    
   public void refreshDep() {
       try {
            // TODO add your handling code here:
            
            datadep=getDep();
        } catch (Exception ex) {
            Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
        }


jTable1.setModel(new javax.swing.table.DefaultTableModel(datadep,headerdep){

    Class[] types = {Integer.class, Date.class, Double.class, String.class,
        Integer.class};

    @Override
    public Class getColumnClass(int columnIndex) {
        return this.types[columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int colIndex) {
        if (colIndex==4) return false;
    return true;
}}
);

jTable1.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());

}

public Vector  getFraisadhEp() throws Exception {
    conn = Connect.ConnectDb();
    PreparedStatement pre;
    
    
    String sql0 = "SELECT * FROM (SELECT idProfil_adulte, Noms, Prenoms, Date_adhesion_ep, 'adulte' as typ FROM Profil_adulte WHERE Date_adhesion_ep is NOT NULL UNION \n" +
"SELECT idProfil_enfant, Nom, Prenoms, Date_adhesion_ep,  'enfant' as typ FROM Profil_enfant WHERE Date_adhesion_ep is NOT  NULL UNION \n" +
"SELECT idProfil_persmorale, Raison_sociale, Raison_sociale, Date_adhesion_ep, 'pers' as typ FROM Profil_persmorale WHERE Date_adhesion_ep is NOT  NULL) adh_ep WHERE Date_adhesion_ep >='"+new java.sql.Date(beginDate.getTime())+"' AND Date_adhesion_ep <= '"+new java.sql.Date(endDate.getTime())+"'";
    
   
 if (dateini.change) sql0 = sql0+ " AND Date_adhesion_ep  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
   
sql0 = sql0 + " ORDER BY Date_adhesion_ep";    
    
    pre = conn.prepareStatement(sql0);
    int i=1;  // iterator
    ResultSet rs = pre.executeQuery();
    Vector<Vector> membreVector = new Vector<Vector>();
    sumadhep=0.0;
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getDate("Date_adhesion_ep")); 
        membre.add(fraisadhep);
        sumadhep=sumadhep+fraisadhep;
        if (rs.getString("typ").equalsIgnoreCase("pers")) { 
            membre.add("Frais d'adhésion Epargne de "+rs.getString(2)); 
        } else {
            membre.add("Frais d'adhésion Epargne de "+rs.getString(2)+ " "+rs.getString(3)); 
 
        }
        membre.add(rs.getInt(1));
        membreVector.add(membre);
        i++;
    }
   
    if(conn!=null)
        conn.close();
    if(rs != null)
        rs.close();
    if(pre !=null)
        pre.close();
    
  

    return membreVector;
}

public Vector  getFraisadhTont() throws Exception {
    conn = Connect.ConnectDb();
    PreparedStatement pre;
    
     String sql0 = "SELECT * FROM (SELECT idProfil_adulte, Noms, Prenoms, Date_adhesion_to, 'adulte' as typ FROM Profil_adulte WHERE Date_adhesion_to is NOT NULL UNION \n" +
    "SELECT idProfil_enfant, Nom, Prenoms, Date_adhesion_to,  'enfant' as typ FROM Profil_enfant WHERE Date_adhesion_to is NOT  NULL UNION \n" +
    "SELECT idProfil_persmorale, Raison_sociale, Raison_sociale, Date_adhesion_to, 'pers' as typ FROM Profil_persmorale WHERE Date_adhesion_to is NOT  NULL) adh_ep WHERE Date_adhesion_to >='"+new java.sql.Date(beginDate.getTime())+"' AND Date_adhesion_to <= '"+new java.sql.Date(endDate.getTime())+"' "; 
    
    
 if (dateini.change) sql0 = sql0+ " AND Date_adhesion_to  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
     
  sql0 = sql0 +" ORDER BY Date_adhesion_to"; 
    
    
    pre = conn.prepareStatement(sql0);
    int i=1;  // iterator
    ResultSet rs = pre.executeQuery();
    Vector<Vector> membreVector = new Vector<Vector>();
    sumadhtont=0.0;
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getDate("Date_adhesion_to")); 
        membre.add(fraisadtont);
        sumadhtont=sumadhtont+fraisadtont;
        if (rs.getString("typ").equalsIgnoreCase("pers")) { 
            membre.add("Frais d'adhésion Tontine de "+rs.getString(2)); 
        } else {
            membre.add("Frais d'adhésion Tontine de "+rs.getString(2)+ " "+rs.getString(3)); 
        }
        membre.add(rs.getInt(1));
        membreVector.add(membre);
        i++;
    }
   
    if(conn!=null)
        conn.close();
    if(rs != null)
        rs.close();
    if(pre !=null)
        pre.close();
    
  

    return membreVector;
}
   
   
    
    public Vector  getEntr() throws Exception {
    conn = Connect.ConnectDb();
    
     PreparedStatement pre;
    String sql0 = "SELECT * FROM Entreessorties WHERE daterec >='"+new java.sql.Date(beginDate.getTime())+"' AND daterec <= '"+new java.sql.Date(endDate.getTime())+"' ";
   
    
    
    if (dateini.change) sql0 = sql0+ " AND daterec  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
    sql0 = sql0 + "AND montant >= 0 ORDER BY daterec ASC";
    pre = conn.prepareStatement(sql0);
       
    int i=1;  // iterator
    ResultSet rs = pre.executeQuery();
    Vector<Vector> membreVector = new Vector<Vector>();
    sumretr=0.0;
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getDate("daterec")); 
        membre.add(rs.getDouble("montant"));
        sumretr=sumretr+rs.getDouble("montant");
        membre.add(rs.getString("libelle")); 
        membre.add(rs.getInt("idEntreessorties"));
        membreVector.add(membre);
        i++;
    }
   
    if(conn!=null)
        conn.close();
    if(rs != null)
        rs.close();
    if(pre !=null)
        pre.close();
    
  

    return membreVector;
}
     
public Vector  getCommTont() throws Exception {
    conn = Connect.ConnectDb();
    PreparedStatement pre;
    System.out.println("sql vaut"+"SELECT * FROM Tontine tont  LEFT JOIN (SELECT * FROM (SELECT idProfil_adulte as id, Profil_adulte.Noms as Noms, Prenoms, '"+"Adulte"+"' as typ FROM Profil_adulte UNION"+ 
"SELECT idProfil_enfant as id, Profil_enfant.Nom as Noms, Prenoms, '"+"Enfant"+"' as typ FROM Profil_enfant UNION"+ 
"SELECT idProfil_persmorale as id, Profil_persmorale.Raison_sociale as Noms, Profil_persmorale.Raison_sociale as prenoms, '"+"Pers Morale"+"' as typ FROM Profil_persmorale) adh_ep) allse on tont.TypeEpargnant = allse."+ 
"typ AND tont.IdEpargnant = allse.id WHERE bit_count(JoursTontine) >= 1 AND DateTontine >= '"+ new java.sql.Date(beginDate.getTime())+ "' AND DateTontine <= '"+new java.sql.Date(beginDate.getTime())+"' ORDER BY DateTontine");
    
String sql="SELECT * FROM Tontine tont  LEFT JOIN (SELECT * FROM (SELECT idProfil_adulte as id, Profil_adulte.Noms as Noms, Prenoms, '"+"Adulte"+"' as typ FROM Profil_adulte UNION"+ 
" SELECT idProfil_enfant as id, Profil_enfant.Nom as Noms, Prenoms, '"+"Enfant"+"' as typ FROM Profil_enfant UNION"+ 
" SELECT idProfil_persmorale as id, Profil_persmorale.Raison_sociale as Noms, Profil_persmorale.Raison_sociale as prenoms, '"+"Pers Morale"+"' as typ FROM Profil_persmorale) adh_ep) allse on tont.TypeEpargnant = allse."+ 
"typ AND tont.IdEpargnant = allse.id WHERE bit_count(JoursTontine) >= 1 AND DateTontine >= '"+ new java.sql.Date(beginDate.getTime())+ "' AND DateTontine <= '"+new java.sql.Date(endDate.getTime())+"'";
  
    
   if (dateini.change) sql = sql + " AND DateTontine  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
   sql = sql + " ORDER BY DateTontine"; 
   pre = conn.prepareStatement(sql);
   
    
    int i=1;  // iterator
    ResultSet rs = pre.executeQuery();
    Vector<Vector> membreVector = new Vector<Vector>();
    sumcommtont=0.0;
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getDate("DateTontine")); 
        membre.add(rs.getDouble("Mise"));
        sumcommtont=sumcommtont+rs.getDouble("Mise");
        if (rs.getString("typ").equalsIgnoreCase("Pers Morale")) { 
            membre.add("Commission tontine "+rs.getString("Noms")); 
        } else {
            membre.add("Commission tontine "+rs.getString("Noms")+ " "+rs.getString("prenoms")); 
 
        }
        membre.add(rs.getInt(1));
        membreVector.add(membre);
        i++;
    }
   
    if(conn!=null)
        conn.close();
    if(rs != null)
        rs.close();
    if(pre !=null)
        pre.close();
    System.out.println("membreVector"+membreVector);
    return membreVector;
}     

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jXTreeTable1 = new org.jdesktop.swingx.JXTreeTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        try {
            // TODO add your handling code here:

            datadep=getDep();
        } catch (Exception ex) {
            Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
        }

        headerdep.add("N°");
        headerdep.add("Date");
        headerdep.add("Montant");
        headerdep.add("Libellé");
        headerdep.add("ID");
        jTable1.setModel(new javax.swing.table.DefaultTableModel(datadep,headerdep){

            Class[] types = {Integer.class, Date.class, Double.class, String.class,
                Integer.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex==4) return false;
                return true;
            }}
        );
        popupMenu = new JPopupMenu();
        menuItemMod = new JMenuItem("Modifier");
        menuItemSup = new JMenuItem("Supprimer");
        menuItemMod.addActionListener(this);
        menuItemSup.addActionListener(this);
        popupMenu.add (menuItemMod);
        popupMenu.add (menuItemSup);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = jTable1.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), jTable1));
                        if (rowAtPoint > -1) {
                            jTable1.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });

        jTable1.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
        jTable1.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor("dd/MM/yyyy"));

        jTable1.setComponentPopupMenu(popupMenu);
        jScrollPane1.setViewportView(jTable1);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dépenses", jPanel1);

        try {
            // TODO add your handling code here:

            dataretr=getEntr();
        } catch (Exception ex) {
            Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable2.setModel(new javax.swing.table.DefaultTableModel(dataretr,headerdep){

            Class[] types = {Integer.class, Date.class, Double.class, String.class,
                Integer.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

        }
    );
    jTable2.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
    jScrollPane2.setViewportView(jTable2);

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup()
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    jTabbedPane1.addTab("Entrées", jPanel2);

    try {
        // TODO add your handling code here:

        datadep=getFraisadhEp();
    } catch (Exception ex) {
        Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
    }
    jTable3.setModel(new javax.swing.table.DefaultTableModel(datadep,headerdep){

        Class[] types = {Integer.class, Date.class, Double.class, String.class,
            Integer.class};

        @Override
        public Class getColumnClass(int columnIndex) {
            return this.types[columnIndex];
        }
        public boolean isCellEditable(int rowIndex, int colIndex) {
            if (colIndex==4) return false;
            return true;
        }}
    );
    jTable3.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
    jTable3.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor("dd/MM/yyyy"));
    jScrollPane3.setViewportView(jTable3);

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel3Layout.createSequentialGroup()
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    jTabbedPane1.addTab("Frais Adh Ep", jPanel3);

    try {
        // TODO add your handling code here:

        datadep=getFraisadhTont();
    } catch (Exception ex) {
        Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
    }
    jTable4.setModel(new javax.swing.table.DefaultTableModel(datadep,headerdep){

        Class[] types = {Integer.class, Date.class, Double.class, String.class,
            Integer.class};

        @Override
        public Class getColumnClass(int columnIndex) {
            return this.types[columnIndex];
        }
        public boolean isCellEditable(int rowIndex, int colIndex) {
            if (colIndex==4) return false;
            return true;
        }}
    );
    jTable3.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
    jTable3.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor("dd/MM/yyyy"));
    jScrollPane4.setViewportView(jTable4);

    org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel4Layout.createSequentialGroup()
            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    jTabbedPane1.addTab("Frais Adh Tontine", jPanel4);

    try {
        datadep=getCommTont();
    } catch (Exception ex) {
        Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
    }
    jTable5.setModel(new javax.swing.table.DefaultTableModel(datadep,headerdep){

        Class[] types = {Integer.class, Date.class, Double.class, String.class,
            Integer.class};

        @Override
        public Class getColumnClass(int columnIndex) {
            return this.types[columnIndex];
        }
        public boolean isCellEditable(int rowIndex, int colIndex) {
            if (colIndex==4) return false;
            return true;
        }}
    );
    jTable5.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
    jTable5.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor("dd/MM/yyyy"));
    jScrollPane6.setViewportView(jTable5);

    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel5Layout.createSequentialGroup()
            .add(jScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    jTabbedPane1.addTab("Commission Tontine", jPanel5);

    try{
        AbstractTreeTableModel treeTableModel = new MembDataModel(createDataStructure());
        jXTreeTable1.setTreeTableModel(treeTableModel);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    jScrollPane5.setViewportView(jXTreeTable1);
    //System.out.println("Hash reverse vaut"+ftchashreverse);
    //jXTreeTable1.setTreeTableModel(new FtcDetailTreeTableModel(ftchashreverse));

    jXTreeTable1.getColumn(1).setCellRenderer(new YMDRenderer());

    org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("FTC", jPanel6);

    jLabel1.setText("Total:");

    jLabel2.setText(String.valueOf(sumdep));

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jTabbedPane1)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jLabel1)
            .add(37, 37, 37)
            .add(jLabel2)
            .add(85, 85, 85))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 397, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel2)
                .add(jLabel1))
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        if(jTabbedPane1.getSelectedIndex() == 0){
            jLabel2.setText(String.valueOf(sumdep));
        } else if(jTabbedPane1.getSelectedIndex() == 1) {
            jLabel2.setText(String.valueOf(sumretr));
        } else if(jTabbedPane1.getSelectedIndex() == 2) {
            System.out.println("sum adhep"+sumadhep);
            jLabel2.setText(String.valueOf(sumadhep));
        } else if(jTabbedPane1.getSelectedIndex() == 3) {
             jLabel2.setText(String.valueOf(sumadhtont));
        } else if(jTabbedPane1.getSelectedIndex() == 4) {
             jLabel2.setText(String.valueOf(sumcommtont));
        } else if(jTabbedPane1.getSelectedIndex() == 5) {
             jLabel2.setText(String.valueOf(ftcsomme));
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

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
            java.util.logging.Logger.getLogger(Detailcompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Detailcompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Detailcompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Detailcompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Detailcompt().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private org.jdesktop.swingx.JXTreeTable jXTreeTable1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
         EpargneContext epargne;
        EpargneContextRet epargne2;
        EpargneContext_rel epargne3;
        TontineUser tont = null;
        JMenuItem menu = (JMenuItem) e.getSource();
        if (menu == menuItemMod) {
            if (!jTable1.isEditing()) {
            Date regisdate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            regisdate = (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
             
                 Double montant = (Double) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                 String libelle = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 3);
                 int id = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 4);
                 conn= Connect.ConnectDb();
                 String sql = "UPDATE Entreessorties SET daterec='"+new java.sql.Date(regisdate.getTime())+"', montant='"+montant+"', libelle='"+libelle+"' WHERE idEntreessorties='"+ id +"' ";
                 Statement stmt=null;
             try {
                 stmt = conn.createStatement();
                 stmt.executeUpdate(sql);
                 JOptionPane.showMessageDialog(this, "Modification effectuée avec succès");
                 this.refreshDep();
             } catch (SQLException ex) {
                 Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
             }
             
            try {
             if (conn != null) conn.close();
             if (stmt != null) stmt.close();
         
            } catch (SQLException ex) {
                Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
            }
            }else {
                  JOptionPane.showMessageDialog(this, "Veuillez finir d'entrer les modifications");
            } 
            
          

        } else if (menu == menuItemSup) {
            
                   int P = JOptionPane.showConfirmDialog(null,"Etes-vous sur de supprimer cet élement ?","Confirmation",JOptionPane.YES_NO_OPTION);
                   if (P==0){
                        conn= Connect.ConnectDb();
                        int id = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 4);
                        String sql = "DELETE FROM Entreessorties WHERE idEntreessorties='"+ id +"' ";
                       
                        
                        try {
                        PreparedStatement pre = conn.prepareStatement(sql);                          
                        pre.executeUpdate();
                        if (conn != null) conn.close();
                        if (pre != null)  pre.close();
                        JOptionPane.showMessageDialog(this, "Modification effectuée avec succès");
                        this.refreshDep();
                        } catch (SQLException ex) {
                         Logger.getLogger(Detailcompt.class.getName()).log(Level.SEVERE, null, ex);
                        }   
        }
    }
    }
        
}
