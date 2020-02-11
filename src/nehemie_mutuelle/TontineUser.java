/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import static nehemie_mutuelle.IntializeDate.initdate;
//import static  nehemie_mutuelle.EpargneContext.conn;
//import static nehemie_mutuelle.RetraitTontine.conn;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author elommarcarnold
 */
public class TontineUser extends javax.swing.JFrame implements ActionListener {
    
    
    
    String nomEpargnant;
    String prenomEpargnant;
    String typeEpargnant;
    int IdEpargnant;
    static Connection conn = null;
    private Vector<Vector> data;
    Date date_adhesion;
    Connection connect = null;
    PreparedStatement pre= null;
    PreparedStatement pre21= null;
    PreparedStatement pre2= null;
    PreparedStatement pre3= null;
    private String startDate ="01/01/2014"; 
    private Date startDate2 = new Date();
    private Date fromDate;
    List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
    private TableRowSorter<DefaultTableModel> sorter;
    boolean first_item1=false;
    boolean first_item2=false;
    String[] shortMonths = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();
    Boolean wipe = false;
    RowFilter<Object,Object> retraitfilter;
    private MiseTontine miseTontine;
    private static SimpleDateFormat formattedDate = new SimpleDateFormat("HH:mm:SS");
    private JMenuItem menuItemMise;
    private JMenuItem menuItemSuppTont;
    private JMenuItem menuItemRet;
    private JMenuItem menuItemSupp;
    private JPopupMenu popupMenu;
    private String carnet="";
    private boolean additional =false;
      RowFilter< TableModel, Integer > filter2;     
    
    public TontineUser (String carnet, String nomEpargnant, String prenomEpargnant, String typeEpargnant) throws SQLException, ParseException {
        this.nomEpargnant = nomEpargnant;
        this.prenomEpargnant = prenomEpargnant;
        this.typeEpargnant = typeEpargnant;
        this.IdEpargnant = getId(this.nomEpargnant, this.prenomEpargnant, this.typeEpargnant);
        setTitle("Historique des cotisations tontine de " + this.nomEpargnant + " " + this.prenomEpargnant+ " Carnet "+carnet);       
        setDate_adhesion(); 
        this.carnet = carnet;
        this.additional = true;
        
         final String OLD_FORMAT = "dd/MM/yyyy";
         SimpleDateFormat newFormat = new SimpleDateFormat(OLD_FORMAT);
         Date startDatedat = newFormat.parse(startDate);
         if (IntializeDate.change) {
             
             if (IntializeDate.initdate.before(startDatedat)) startDatedat = initdate;

         }    
         
        
        initComponents();
    }
   
    
    
     private void addListenerToSorter(RowSorter rowSorter,  
                           final JLabel rowCountLabel) {

    rowSorter.addRowSorterListener(new RowSorterListener() {
        @Override
        public void sorterChanged(RowSorterEvent e) {
//            int newRowCount = table.getRowCount();
//            rowCountLabel.setText("Number of view rows: " + newRowCount);
            
       NumberFormat n = NumberFormat.getCurrencyInstance(new Locale( "fr", "TG")); 
       double doublePayment=0;
       if(rowCountLabel.equals(jLabel3)){
           doublePayment = getSumRet().doubleValue();
       } else {
           doublePayment = getSumCumul().doubleValue();
       }
          
       String s = n.format(doublePayment);
       rowCountLabel.setText(s);
        }
    });
}
    
    /** Creates new form TontineUser */
    public TontineUser() {
        initComponents();
    }
    

    
   public void refresh() {
       try {
            // TODO add your handling code here:
           
            if (additional) data=getTontine2();
            else data=getTontine();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[][] out = to2DimArray(data);

//jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setFillsViewportHeight(true);
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(out,
    new String [] {
        "Date", "Mise", "Total", "Total retraits", "Idtontine", "Type"
    }
){

    Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, Integer.class,
        Boolean.class};
    @Override
    //  public Class getColumnClass(int columnIndex) {
        //     return this.types[columnIndex];
        //  }

    public Class getColumnClass(int column)
    {
        for (int row = 0; row < getRowCount(); row++)
        {
            Object o = getValueAt(row, column);

            if (o != null)
            return o.getClass();
        }

        return Object.class;
    }
}
);
// Sorter 
sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
addListenerToSorter(sorter, jLabel5);
addListenerToSorter(sorter, jLabel3);
final TontineUser copie= this;
jTable1.getModel().addTableModelListener(new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {
            NumberFormat n = NumberFormat.getCurrencyInstance(new Locale( "fr", "TG"));
            double sumcot =copie.getSumCumul().doubleValue();
            String s = n.format(sumcot);
            jLabel5.setText(s);
            double sumcot2 =copie.getSumCumul().doubleValue();
            String s2 = n.format(sumcot2);
            jLabel3.setText(s2);
        }
 });

jTable1.setRowSorter(sorter);

// renderer

jTable1.setDefaultRenderer(Object.class, new TabelaCellRenderer());
jTable1.getColumn("Date").setCellRenderer(new TabelaCellRenderer());
jTable1.getColumnModel().getColumn(4).setMinWidth(0);
jTable1.getColumnModel().getColumn(4).setMaxWidth(0);
jTable1.getColumnModel().getColumn(5).setMinWidth(0);
jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
}
    
   private void setDate_adhesion() throws SQLException {
       String sql0;
       if(typeEpargnant.equalsIgnoreCase("Enfant")) {
           sql0="SELECT Date_adhesion_to FROM Profil_enfant WHERE idProfil_enfant='"+this.IdEpargnant+"';";
       } else if (typeEpargnant.equalsIgnoreCase("Adulte")){
           sql0="SELECT Date_adhesion_to FROM Profil_adulte WHERE idProfil_adulte='"+this.IdEpargnant+"';";
       } else {
           sql0="SELECT Date_adhesion_to FROM Profil_persmorale WHERE idProfil_persmorale='"+this.IdEpargnant+"';";
       }
       
            connect=Connect.ConnectDb();
            Statement stmt = null;
            ResultSet rs = null;
           
            stmt= connect.createStatement();
            rs=stmt.executeQuery(sql0);
            final String OLD_FORMAT = "dd/MM/yyyy";
            SimpleDateFormat newFormat = new SimpleDateFormat(OLD_FORMAT);
            System.out.println("sql vaut"+sql0);
            while(rs.next()){
                System.out.println("it is true");  
              if(rs.getDate(1)!=null) {  startDate=newFormat.format(rs.getDate(1)); startDate2=rs.getDate(1); }
            }
            System.out.println("startDate"+startDate);
            if(connect!=null) connect.close();
            if(stmt!=null) stmt.close();
            if(rs!=null)  rs.close();
   }
    public BigDecimal getSumCumul() {
        int rowcount= jTable1.getRowCount();
        BigDecimal sum= new BigDecimal(0);
        BigDecimal money;

        
        for (int i=0; i< rowcount; i++){
           if(jTable1.getValueAt(i, 2) != null && (boolean) jTable1.getValueAt(i, 5) == TRUE ) {
           money = new BigDecimal(jTable1.getValueAt(i, 2).toString().replaceAll(" ", ""));
           sum=sum.add(money);
           }
        }
        
        return sum;
        
    }
    
     public BigDecimal getSumRet() {
        int rowcount= jTable1.getRowCount();
        BigDecimal sum= new BigDecimal(0);
        BigDecimal money;

        
        for (int i=0; i< rowcount; i++){
           if(jTable1.getValueAt(i, 2) != null && (boolean) jTable1.getValueAt(i, 5) == FALSE ) {
           money = new BigDecimal(jTable1.getValueAt(i, 2).toString().replaceAll(" ", ""));
               System.out.println("money"+money);
           sum=sum.add(money);
           }
        }
        
        return sum;
        
    }
    
   public TontineUser(String nomEpargnant, String prenomEpargnant, String typeEpargnant) throws SQLException {
       this.nomEpargnant = nomEpargnant;
        this.prenomEpargnant = prenomEpargnant;
        this.typeEpargnant = typeEpargnant;
        this.IdEpargnant = getId(this.nomEpargnant, this.prenomEpargnant, this.typeEpargnant);
        this.additional = false;
        setTitle("Historique des cotisations tontine de " + this.nomEpargnant + " " + this.prenomEpargnant);    
         filter2 = new RowFilter< TableModel, Integer >() {
         @Override
         public boolean include( javax.swing.RowFilter.Entry< ? extends TableModel, ? extends Integer > entry )
         {
//            TableModel model = entry.getModel();
//            return (Boolean)model.getValueAt(7, 1 );
            
        DefaultTableModel personModel =   (DefaultTableModel) entry.getModel();
        boolean active = ((Boolean) personModel.getValueAt(entry.getIdentifier(), 5)).booleanValue();
       return ! active;
         }
      };
        
        setDate_adhesion();
        initComponents();
       
   }
   
    public Double getTotalTontine() {
        conn = Connect.ConnectDb();
         ResultSet rs = null;
        try {
            if (additional) pre = conn.prepareStatement("SELECT SUM((bit_count(JoursTontine)-1)*Mise) FROM Tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"' AND bit_count(JoursTontine) >=1 AND idTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='ajout');");
            else pre = conn.prepareStatement("SELECT SUM((bit_count(JoursTontine)-1)*Mise) FROM Tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"' AND bit_count(JoursTontine) >=1;");
            rs = pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while (rs.next()) {
                return rs.getDouble(1);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (double) 0;
    }
    
    public Double getTotalRetraits() {
        conn = Connect.ConnectDb();
         ResultSet rs = null;
        try {
            if (additional) pre = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+" ' AND idretraits_tontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='retrait');");

            else pre = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+" ';");
            rs = pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while (rs.next()) {
                return rs.getDouble(1);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (double) 0;
    }

    private void deleteRetId(int i) throws SQLException {
        String sql= "DELETE FROM retraits_tontine WHERE idretraits_tontine = ?";
        int P = JOptionPane.showConfirmDialog(null,"Voulez vous vraiment supprimer ce retrait ?","Confirmation",JOptionPane.YES_NO_OPTION);
        connect=Connect.ConnectDb();
        try
        {
           if (P==0){
            pre=connect.prepareStatement(sql);
            pre.setString(1, String.valueOf(i));
       //     JOptionPane.showConfirmDialog(null, "Delete Record?");
            pre.execute();
            //JOptionPane.showMessageDialog(null, "deleted");
          //  refresh();
           JOptionPane.showMessageDialog(null, "Suppression effectuée avec suucès");
        }}
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);

        }
        // Close connection
        if(connect !=null) connect.close();
        if(pre!=null) pre.close();

    }

    private void deleteTontId(int i) throws SQLException {
       String sql= "DELETE FROM Tontine WHERE idTontine = ?";
        int P = JOptionPane.showConfirmDialog(null,"Voulez vous vraiment supprimer cet enregistrement ?","Confirmation",JOptionPane.YES_NO_OPTION);
        connect=Connect.ConnectDb();
        try
        {
           if (P==0){
            pre=connect.prepareStatement(sql);
            pre.setString(1, String.valueOf(i));
       //     JOptionPane.showConfirmDialog(null, "Delete Record?");
            pre.execute();
            //JOptionPane.showMessageDialog(null, "deleted");
          //  refresh();
           JOptionPane.showMessageDialog(null, "Suppression effectuée avec suucès");
        }}
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);

        }
        // Close connection
        if(connect !=null) connect.close();
        if(pre!=null) pre.close();

    }
     
   
   
   
   class TabelaCellRenderer  extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(
                 JTable table,
                    java.lang.Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
        System.out.println("in renderer ");
        Component c= super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       
        if ((Boolean) table.getValueAt(row, 5)==false){
            System.out.println("At least one test is true");
           c.setForeground(Color.BLUE);
        } else {
            c.setForeground(Color.BLACK);
        }
 
        if (column== 0 && ((Boolean) table.getValueAt(row, 5))==true) {
           // setValue(value);
            System.out.println("Boolean true");
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.FRENCH);
            

            setText((value == null) ? "" : StringUtils.capitalize(sdf.format(value)));
        //    setBackground(Color.green);
        //  table.convertRowIndexToView(row);
        } else if( column== 0 && ((Boolean) table.getValueAt(row, 5))==false) {
            System.out.println("Boolean true");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            System.out.println("format"+formattedDate.format(table.getValueAt(row, 0)));
            if(formattedDate.format(table.getValueAt(row, 0)).equalsIgnoreCase("00:00:00")){
                 setText((value == null) ? "" : sdf2.format(value));
            } else {
             setText((value == null) ? "" : sdf.format(value));
            } 
        }
//        }else if (column == 3) {
//            
//          setText(String.format("%.0f", value));
//        }
//        if (table.getModel().getValueAt(row, 1).equals("yellow")) {
//            setBackground(Color.yellow);
//    //      table.convertRowIndexToView(row);
//        }
//        if (table.getModel().getValueAt(row, 1).equals("red")) {
//            setBackground(Color.red);
//     //     table.convertRowIndexToView(row);
//        }
        return this;
    }
}
   
     public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    }

    public void wipecombo2() {
        wipe =true;
        int size = jComboBox2.getItemCount();
        for (int i = 2; i < size; i++) {
            jComboBox2.removeItemAt(2);
        }
      
    }
   
public Vector getTontine() throws Exception {
    System.out.println("In get tontine");      
  retraitfilter = new RowFilter<Object,Object>() {
  public boolean include(Entry<? extends Object, ? extends Object> entry) {
    //for (int i = entry.getValueCount() - 1; i >= 0; i--) {
      if (entry.getValue(5).equals(true)) {
       return true;
     // }
    }
    return false;
 }
};
    
    conn = Connect.ConnectDb(); 
   // String date1 = "01/01/2014";
    final String OLD_FORMAT = "dd/MM/yyyy";
    final String NEW_FORMAT = "MMM, YY";
    SimpleDateFormat newFormat = new SimpleDateFormat(OLD_FORMAT);
    DateFormat formatter = new SimpleDateFormat(NEW_FORMAT);
    fromDate = newFormat.parse(startDate);
    Date toDate = new Date(); //newFormat.parse(date2);

    Calendar beginCalendar = Calendar.getInstance();
    Calendar finishCalendar = Calendar.getInstance();

    beginCalendar.setTimeInMillis(fromDate.getTime());
    beginCalendar.set (Calendar.DAY_OF_MONTH,1); 
    finishCalendar.setTimeInMillis(toDate.getTime());
    Vector<Vector> TontineVector = new Vector<Vector>();
    
    Date date;

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
        
        sqlret= sqlret + "AND idretraits_tontine NOT IN (SELECT idtontine  FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='retrait')";   // changed
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
        
        if(!rs.next()) {
            tont.add(date);
            tont.add(null);
            tont.add(null);
            if (rs3.next()) {
                tont.add(rs3.getDouble(1));
            } else {
                tont.add(0);
            }
            tont.add(null);
            tont.add(new Boolean(true));
            TontineVector.add(tont);
            
            while(rs2.next()) {
                
                  Vector<Object> tontr = new Vector<Object>();
                  tontr.add(rs2.getTimestamp(2));
                  tontr.add(null);
                  tontr.add(rs2.getDouble(3));
                  tontr.add(rs2.getDouble(4));
                  tontr.add(rs2.getInt(1));
                  tontr.add(new Boolean(false));
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
            if((valuesSet.size()-1) >= 0)  tont.add(rs.getDouble(4)*(valuesSet.size()-1));
            else tont.add(0);
            if (rs3.next()) {
                tont.add(rs3.getDouble(1));
            } else {
                tont.add(0);
            }
            tont.add(rs.getInt(1));
            tont.add(new Boolean(true));
            TontineVector.add(tont);
            System.out.println("here list");
            
             while(rs2.next()) {
                  Vector<Object> tontr = new Vector<Object>();
                  tontr.add(rs2.getTimestamp(2));
                  tontr.add(null);
                  tontr.add(rs2.getDouble(3));
                  tontr.add(rs2.getDouble(4));
                  tontr.add(rs2.getInt(1));
                  tontr.add(new Boolean(false));
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
    int j=TontineVector.size();
    while( (j-1) >= 0 && (Boolean) TontineVector.get(j-1).get(5)==false){  // cherche le dernier élement synthèse 
        j--;
    }
//    TontineVector.remove(TontineVector.size()-1);
         TontineVector.remove(j-1);
      
    return TontineVector;
}


public Vector getTontine2() throws Exception {
    System.out.println("In tontine2"+carnet);
        
  retraitfilter = new RowFilter<Object,Object>() {
  public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {
    //for (int i = entry.getValueCount() - 1; i >= 0; i--) {
      if (entry.getValue(5).equals(true)) {
       return true;
     // }
    }
    return false;
 }
};
    
    conn = Connect.ConnectDb(); 
   // String date1 = "01/01/2014";
    final String OLD_FORMAT = "dd/MM/yyyy";
    final String NEW_FORMAT = "MMM, YY";
    SimpleDateFormat newFormat = new SimpleDateFormat(OLD_FORMAT);
    DateFormat formatter = new SimpleDateFormat(NEW_FORMAT);
    fromDate = newFormat.parse(startDate);
    Date toDate = new Date(); //newFormat.parse(date2);

    Calendar beginCalendar = Calendar.getInstance();
    Calendar finishCalendar = Calendar.getInstance();

    beginCalendar.setTimeInMillis(fromDate.getTime());
    beginCalendar.set (Calendar.DAY_OF_MONTH,1); 
    finishCalendar.setTimeInMillis(toDate.getTime());
    Vector<Vector> TontineVector = new Vector<Vector>();
    
    Date date;

   // String date;
    while (beginCalendar.before(finishCalendar)) {
        // add one month to date per loop
        //date = newFormat.format(beginCalendar.getTime());
        date = beginCalendar.getTime(); 
        java.sql.Date sqldate= new java.sql.Date(date.getTime());
        
        // Find all debit 
        pre3=conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(DateRet)=MONTH('"+sqldate+"') AND YEAR(DateRet)=YEAR('"+sqldate+"') AND idretraits_tontine IN (SELECT idenrtontinesupp FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='retrait') ");   // changed
        ResultSet rs3 = pre3.executeQuery();
        pre21= conn.prepareStatement("SET @SumMontant := 0;");
        pre21.executeQuery();
        pre2= conn.prepareStatement("SELECT idretraits_tontine, DateRet, Montant, (@SumMontant := @SumMontant + Montant) AS CumulativeSum FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(DateRet)=MONTH('"+sqldate+"') AND YEAR(DateRet)=YEAR('"+sqldate+"') AND idretraits_tontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='retrait') ORDER by DateRet;");  // changed
        String sql2 = "SELECT idretraits_tontine, DateRet, Montant, (@SumMontant := @SumMontant + Montant) AS CumulativeSum FROM retraits_tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(DateRet)=MONTH('"+sqldate+"') AND YEAR(DateRet)=YEAR('"+sqldate+"') AND idretraits_tontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='retrait') ORDER by DateRet;"; 
        System.out.println("sql"+sql2);
        //System.out.println("SET @SumMontant := 0; SELECT idretraits_tontine, Date, Montant, (@SumMontant := @SumMontant + Montant) AS CumulativeSum FROM retraits_tontine WHERE IdEpargnant='"+this.IdEpargnant +"' AND TypeEpargnant='" + this.typeEpargnant + "' AND MONTH(Date)=MONTH("+sqldate+") AND YEAR(Date)=YEAR("+sqldate+");");
        ResultSet rs2 = pre2.executeQuery();
        pre=conn.prepareStatement("SELECT idTontine, DateTontine, JoursTontine, Mise FROM Tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant+ "' AND DateTontine='"+sqldate+"' AND idTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='ajout');");
        
        System.out.println("sql"+"SELECT idTontine, DateTontine, JoursTontine, Mise FROM Tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant+ "' AND DateTontine='"+sqldate+"' AND IdEpargnant IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='ajout');");
 
        ResultSet rs = pre.executeQuery();
        Vector<Object> tont = new Vector<Object>();
        // Does not exist in the database
        
        if(!rs.next()) {
            tont.add(date);
            tont.add(null);
            tont.add(null);
            if (rs3.next()) {
                tont.add(rs3.getDouble(1));
            } else {
                tont.add(0);
            }
            tont.add(null);
            tont.add(new Boolean(true));
            TontineVector.add(tont);
            
            while(rs2.next()) {
                
                  Vector<Object> tontr = new Vector<Object>();
                  tontr.add(rs2.getTimestamp(2));
                  tontr.add(null);
                  tontr.add(rs2.getDouble(3));
                  tontr.add(rs2.getDouble(4));
                  tontr.add(rs2.getInt(1));
                  tontr.add(new Boolean(false));
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
            if((valuesSet.size()-1) >= 0)  tont.add(rs.getDouble(4)*(valuesSet.size()-1));
            else tont.add(0);
            if (rs3.next()) {
                tont.add(rs3.getDouble(1));
            } else {
                tont.add(0);
            }
            tont.add(rs.getInt(1));
            tont.add(new Boolean(true));
            TontineVector.add(tont);
            System.out.println("here list");
            
             while(rs2.next()) {
                  Vector<Object> tontr = new Vector<Object>();
                  tontr.add(rs2.getTimestamp(2));
                  tontr.add(null);
                  tontr.add(rs2.getDouble(3));
                  tontr.add(rs2.getDouble(4));
                  tontr.add(rs2.getInt(1));
                  tontr.add(new Boolean(false));
                  TontineVector.add(tontr);
                  System.out.println("here"+ rs2.getTimestamp(2));
            }
            
        }
        
      //  System.out.println(date);
        beginCalendar.add(Calendar.MONTH, 1);
    }


    int j=TontineVector.size();
    while( (j-1) >= 0 && (Boolean) TontineVector.get(j-1).get(5)==false){  // cherche le dernier élement synthèse 
        j--;
    }
//    TontineVector.remove(TontineVector.size()-1);
         TontineVector.remove(j-1);
      
    return TontineVector;
}

    
    private int getId(String nomEpargnant, String prenomEpargnant, String typeEpargnant) {
        connect = Connect.ConnectDb();
        if (typeEpargnant.equalsIgnoreCase("adulte")) {
            String sql01 = "SELECT idProfil_adulte FROM Profil_adulte WHERE Noms= '" + nomEpargnant + "' AND lower(Prenoms)= '" + prenomEpargnant.toLowerCase(Locale.FRENCH) + "'";
            Statement stmt = null;
            connect = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = connect.createStatement();
                rs1 = stmt.executeQuery(sql01);
                //result= rs1.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (rs1.next()) {

                    result = rs1.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        } else if (typeEpargnant.equalsIgnoreCase("enfant")) {
            String sql01 = "SELECT idProfil_enfant FROM Profil_enfant WHERE Nom= '" + nomEpargnant + "' AND lower(Prenoms)= '" + prenomEpargnant.toLowerCase(Locale.FRENCH) + "'";
            Statement stmt = null;

            connect = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = connect.createStatement();

                rs1 = stmt.executeQuery(sql01);

                //    System.out.println("valeur"+rs1.getInt("idProfil_enfant"));
                //  result= rs1.getInt("idProfil_enfant");
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (rs1.next()) {

                    result = rs1.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        } else if (typeEpargnant.equalsIgnoreCase("pers morale")) {
            String sql01 = "SELECT idProfil_persmorale FROM Profil_persmorale WHERE Raison_sociale= '" + nomEpargnant + "'";
            Statement stmt = null;
            connect = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = connect.createStatement();
                rs1 = stmt.executeQuery(sql01);
                //  result= rs1.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (rs1.next()) {

                    result = rs1.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }

            return result;

        }

        return 0;
    }

    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox(
            new Object[] {
                "Tous",
                new JSeparator(JSeparator.HORIZONTAL),

            }) {
                @Override
                public void setSelectedItem(Object item) {
                    if (item.getClass()!= JSeparator.class)
                    super.setSelectedItem(item);
                }};
                jComboBox2 = new javax.swing.JComboBox(
                    new Object[] {
                        "Tous",
                        new JSeparator(JSeparator.HORIZONTAL),

                    }) {
                        @Override
                        public void setSelectedItem(Object item) {
                            if (item.getClass()!= JSeparator.class)
                            super.setSelectedItem(item);
                        }};
                        jComboBox2.setRenderer(new SeparatorComboBoxRenderer());
                        jLabel7 = new javax.swing.JLabel();
                        jLabel8 = new javax.swing.JLabel();
                        jCheckBox1 = new javax.swing.JCheckBox();
                        jButton2 = new javax.swing.JButton();

                        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                        try {
                            // TODO add your handling code here:
                            if   (additional)  data=getTontine2();
                            else data=getTontine();
                        } catch (Exception ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Object[][] out = to2DimArray(data);

                        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        jTable1.setFillsViewportHeight(true);
                        jTable1.setAutoCreateRowSorter(true);
                        //jTable1.setPreferredScrollableViewportSize(new Dimension(1000,70));
                        //jTable1.setModel(new javax.swing.table.DefaultTableModel(out,
                            // new Object [][] {
                                //    {"20/10/2011", "500000000", "Dépôt initial", "1"},
                                //    {null, null, null, null},
                                //  {null, null, null, null},
                                //      {null, null, null, null},
                                //     {null, null, null, null},
                                //   {null, null, null, null},
                                // {null, null, null, null},
                                //      {null, null, null, null},
                                //      {null, null, null, null},
                                //      {null, null, null, null},
                                //      {null, null, null, null},
                                //     {null, null, null, null},
                                //   {null, null, null, null},
                                //     {null, null, null, null},
                                //      {null, null, null, null},
                                //    {null, null, null, null},
                                //    {null, null, null, null},
                                //   {null, null, null, null},
                                //    {null, null, null, null},
                                //     {null, null, null, null}
                                // },
                            //  new String [] {
                                //     "Date ", "Montant(en Frcs CFA)", "Motif", "ID"
                                //   }
                            //    new String  // à partir
                            //      "Date ", "Débit", "Crédit", "Solde", "Libellé", "ID"
                            //  }

                        //){

                        //    Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, String.class,
                            //        String.class};
                        //    @Override
                        //  public Class getColumnClass(int columnIndex) {
                            //     return this.types[columnIndex];
                            //  }

                        //    public Class getColumnClass(int column)
                        //    {
                            //        for (int row = 0; row < getRowCount(); row++)
                            //       {
                                //            Object o = getValueAt(row, column);

                                //            if (o != null)
                                //            return o.getClass();
                                //        }

                            //        return Object.class;
                            //    }
                        //}
                    //);
                jTable1.setModel(new javax.swing.table.DefaultTableModel(out,
                    //  new Object [][] {
                        //      {null, null, null, null, null, null},
                        //      {null, null, null, null, null, null},
                        //     {null, null, null, null, null, null},
                        //     {null, null, null, null, null, null}
                        // }
                    // ,
                    new String [] {
                        "Date", "Mise", "Total", "Total retraits", "Idtontine", "Type"
                    }
                ){

                    Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, Integer.class,
                        Boolean.class};
                    @Override
                    //  public Class getColumnClass(int columnIndex) {
                        //     return this.types[columnIndex];
                        //  }

                    public Class getColumnClass(int column)
                    {
                        for (int row = 0; row < getRowCount(); row++)
                        {
                            Object o = getValueAt(row, column);

                            if (o != null)
                            return o.getClass();
                        }

                        return Object.class;
                    }
                }
            );
            // Sorter
            sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
            addListenerToSorter(sorter, jLabel5);
            addListenerToSorter(sorter, jLabel3);
            final TontineUser copie= this;
            jTable1.getModel().addTableModelListener(new TableModelListener() {

                @Override
                public void tableChanged(TableModelEvent e) {
                    NumberFormat n = NumberFormat.getCurrencyInstance(new Locale( "fr", "TG"));
                    double sumcot =copie.getSumCumul().doubleValue();
                    String s = n.format(sumcot);
                    jLabel5.setText(s);
                    double sumcot2 =copie.getSumCumul().doubleValue();
                    String s2 = n.format(sumcot2);
                    jLabel3.setText(s2);
                }
            });

            jTable1.setRowSorter(sorter);

            // renderer

            jTable1.setDefaultRenderer(Object.class, new TabelaCellRenderer());
            jTable1.getColumn("Date").setCellRenderer(new TabelaCellRenderer());
            jTable1.getColumnModel().getColumn(4).setMinWidth(0);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(5).setMinWidth(0);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(0);

            // Popupmenu
            popupMenu = new JPopupMenu();
            menuItemMise = new JMenuItem("Modifier la mise");
            menuItemSuppTont = new JMenuItem("Supprimer complètement l'enregistrement");
            menuItemRet = new JMenuItem("Modifier le retrait");
            menuItemSupp = new JMenuItem("Supprimer le retrait");

            //            menuItemRetraits = new JMenuItem("Retraits");
            //            menuItemTontine = new JMenuItem("Tontine");
            //            menuItemMouvements = new JMenuItem("Mouvements");
            menuItemMise.addActionListener(this);
            menuItemRet.addActionListener(this);
            menuItemSupp.addActionListener(this);
            menuItemSuppTont.addActionListener(this);
            //            menuItemRetraits.addActionListener(this);
            //            menuItemTontine.addActionListener(this);
            //            menuItemMouvements.addActionListener(this);
            popupMenu.add (menuItemMise);
            popupMenu.add (menuItemSuppTont);
            popupMenu.add (menuItemRet);
            popupMenu.add (menuItemSupp);
            //            popupMenu.add (menuItemRetraits);
            //            popupMenu.add(menuItemTontine);
            //            popupMenu.add(menuItemMouvements);
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

            jTable1.setComponentPopupMenu(popupMenu);

            // personnalisation de la liste de sélection
            jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                @Override
                public void valueChanged(ListSelectionEvent event){
                    if (jTable1.getSelectedRow() > -1){
                        popupMenu.getComponent(0).setEnabled(true);
                        popupMenu.getComponent(1).setEnabled(true);

                        int row=jTable1.getSelectedRow();

                        //      String product_type=jTable1.getModel().getValueAt(row, 3).toString();
                        Boolean type=(Boolean) jTable1.getValueAt(row, 5);

                        if(type.booleanValue()==TRUE) {
                            popupMenu.getComponent(2).setEnabled(false);
                            popupMenu.getComponent(3).setEnabled(false);

                        } else {
                            popupMenu.getComponent(0).setEnabled(false);
                            popupMenu.getComponent(1).setEnabled(false);
                        }

                    }
                }
            });

            // Fill jComboBox
            Date enddate;
            if (jTable1.getRowCount()==0) { enddate=startDate2; } else {
                enddate =((java.util.Date) (jTable1.getModel().getValueAt(jTable1.getRowCount()-1, 0)));}
            String endyear= new SimpleDateFormat("yyyy").format(enddate);
            String startyear= new SimpleDateFormat("yyyy").format(fromDate);

            int cur = Integer.parseInt(startyear);
            int stop =Integer.parseInt(endyear);
            while (cur <= stop) {
                jComboBox1.addItem(Integer.toString(cur));
                cur++;
            }
            jScrollPane1.setViewportView(jTable1);

            jButton1.setText("Remplir le mois");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jLabel1.setText("Solde");

            jLabel2.setText("jLabel2");
            Locale specifiedLocale = new Locale( "fr", "TG");
            NumberFormat n = NumberFormat.getCurrencyInstance(specifiedLocale);
            jLabel2.setText(n.format(getTotalTontine()-getTotalRetraits()));

            jLabel3.setText("jLabel2");
            specifiedLocale = new Locale( "fr", "TG");
            n = NumberFormat.getCurrencyInstance(specifiedLocale);
            double doubleRet =this.getSumRet().doubleValue();
            String s2 = n.format(doubleRet);
            jLabel3.setText(s2);

            jLabel4.setText("Total retraits");

            jLabel5.setText("jLabel2");
            specifiedLocale = new Locale( "fr", "TG");
            n = NumberFormat.getCurrencyInstance(specifiedLocale);
            double doublePayment =this.getSumCumul().doubleValue();
            String s = n.format(doublePayment);
            jLabel5.setText(s);

            jLabel6.setText("Cumul");

            jComboBox1.setRenderer(new SeparatorComboBoxRenderer());
            //jComboBox1.setSelectedIndex(jComboBox1.getItemCount()-1);
            jComboBox1.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jComboBox1ItemStateChanged(evt);
                }
            });

            jComboBox1.setSelectedIndex(jComboBox1.getItemCount()-1);
            jComboBox2.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jComboBox2ItemStateChanged(evt);
                }
            });

            jLabel7.setText("Mois");

            jLabel8.setText("Année");

            jCheckBox1.setText("Afficher les retraits");
            jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jCheckBox1ItemStateChanged(evt);
                }
            });

            jButton2.setText("Nouveau retrait");
            //jButton2.setForeground(Color.BLACK);
            //jButton2.setBackground(Color.WHITE);
            //Border line = new LineBorder(Color.BLACK);
            // Border margin = new EmptyBorder(5, 15, 5, 15);
            //  Border compound = new CompoundBorder(line, margin);
            //  jButton2.setBorder(compound);
            jButton2.setBackground(new Color(0x2dce98));
            jButton2.setForeground(Color.white);
            // customize the button with your own look
            jButton2.setUI(new StyledButtonUI());
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jSeparator1)
                .add(layout.createSequentialGroup()
                    .add(23, 23, 23)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(layout.createSequentialGroup()
                            .add(jLabel6)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 97, Short.MAX_VALUE)
                            .add(jLabel5))
                        .add(layout.createSequentialGroup()
                            .add(jLabel1)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel2)))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(jLabel4)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel3))
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(62, 62, 62))
                .add(layout.createSequentialGroup()
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 583, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(jCheckBox1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel8))
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel7)
                                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel7)
                        .add(jLabel8))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jCheckBox1))
                    .add(18, 18, 18)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 192, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(18, 18, 18)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel1)
                        .add(jLabel2)
                        .add(jLabel4)
                        .add(jLabel3))
                    .add(6, 6, 6)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel6)
                        .add(jLabel5)
                        .add(jButton2))
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
  //      System.out.println("changed"+jComboBox1.getSelectedItem());
   //     System.out.println("Filter size before"+ filters.size());
        // Rowfilter 
        if (jComboBox1.getSelectedIndex() != 0) {
            System.out.println("first_item2"+first_item2+"first_item1"+first_item1);
            if (first_item2 == false && first_item1 == true && filters.size()==2){ for (int i=0; i< filters.size(); i++) {filters.remove(i);} System.out.println("Made");}
            if (filters.size() < 2) {
                System.out.println("size is not 2");
                System.out.println("Selected index !=0 and size <2");
                
                // Code à revoir
                
              //  SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.FRENCH);
            

           // setText((value == null) ? "" : StringUtils.capitalize(sdf.format(value)));
           
                
               RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox1.getSelectedItem().toString(), 0);
                //RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(shortMonths[jComboBox1.getSelectedIndex()], 0);
                filters.add(rf);
                System.out.println("Filter 1 added index !=0 size"+filters.size());
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox1.getSelectedItem().toString(), 0);
               // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(shortMonths[jComboBox1.getSelectedIndex()], 0);

                System.out.println("size is 2");
                filters.set(1, rf);
                System.out.println("Selected index !=0 and size >=2");
               // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

       // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
        } else {
            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
            // sorter.setRowFilter(rf);
             if (first_item2 == false && first_item1 == true && filters.size()==2){ for (int i=0; i< filters.size(); i++) {filters.remove(i);}}
            if (filters.size() < 2) {
                System.out.println("Selected index =0 and size <2");

                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.add(rf);
                System.out.println("Filter 1 added index ==0 size"+filters.size());
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                System.out.println("Selected index =0 and size >2 size"+filters.size());

                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.set(1, rf);
                
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }
        
        
        int combo2sel= jComboBox2.getSelectedIndex();
        if (jComboBox1.getSelectedIndex()==0) { // Item = Tous 
            if (jComboBox1.getItemCount()== 3) { // Only two elements including the separator
                  // Now we get the last month 
                Date enddate;
               if (jTable1.getModel().getRowCount() == 0) {
                   
                   
                   enddate = startDate2;
               
               
               } else {
                   
                   enddate=((java.util.Date) (jTable1.getModel().getValueAt(jTable1.getModel().getRowCount()-1, 0)));
               
               
               
               }
            //    Date enddate =((java.util.Date) (jTable1.getModel().getValueAt(jTable1.getModel().getRowCount()-1, 0)));
                Calendar cal = Calendar.getInstance();
                cal.setTime(enddate);
                int month = cal.get(Calendar.MONTH);
                wipecombo2();
                for (int i=0; i<= month; i++){
                    jComboBox2.addItem(StringUtils.capitalize(new DateFormatSymbols().getMonths()[i]));
                }
            } else if (jComboBox1.getItemCount() > 3) {
                wipecombo2();
                for (int i=0; i<= 11; i++){
                    jComboBox2.addItem(StringUtils.capitalize(new DateFormatSymbols().getMonths()[i]));
                }
             
                        
            }
            } else if (jComboBox1.getSelectedIndex()==(jComboBox1.getItemCount()-1)) {
                //debug 
                System.out.println("rowcount"+ jTable1.getRowCount());
                Date enddate;
               if (jTable1.getModel().getRowCount() == 0) { enddate = startDate2;} else { enddate=((java.util.Date) (jTable1.getModel().getValueAt(jTable1.getModel().getRowCount()-1, 0)));}
                Calendar cal = Calendar.getInstance();
                cal.setTime(enddate);
                int month = cal.get(Calendar.MONTH);
                wipecombo2();
                for (int i=0; i<= month; i++){
                    jComboBox2.addItem(StringUtils.capitalize(new DateFormatSymbols().getMonths()[i]));
                }
         } else {
                 wipecombo2();
                for (int i=0; i<= 11; i++){
                    jComboBox2.addItem(StringUtils.capitalize(new DateFormatSymbols().getMonths()[i]));
                }
                
         
         }
        
        if (combo2sel <= jComboBox2.getItemCount()-1) {
            jComboBox2.setSelectedIndex(combo2sel);
        } else {
            jComboBox2.setSelectedIndex(jComboBox2.getItemCount()-1);
        }
        
        
        if(first_item1==false) first_item1=true;
        System.out.println("Filter size after"+ filters.size());
        wipe = false;
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
      //   System.out.println("changed"+jComboBox2.getSelectedItem());
      //  System.out.println("Filter size before"+ filters.size());
//        System.out.println("shortmonths"+StringUtils.capitalize(shortMonths[jComboBox2.getSelectedIndex()-2])+ "index"+jComboBox2.getSelectedIndex());
       
        if (wipe != true) {
        if (jComboBox2.getSelectedIndex() != 0) {
            if (filters.isEmpty()) {
                System.out.println("shortmonths"+shortMonths[jComboBox2.getSelectedIndex()]);
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(StringUtils.capitalize(shortMonths[jComboBox2.getSelectedIndex()-2])+"|-"+ String.format("%02d", jComboBox2.getSelectedIndex()-1)+"-", 0);
                System.out.println("valeur"+StringUtils.capitalize(shortMonths[jComboBox2.getSelectedIndex()-2])+"|-"+ String.format("%02d", jComboBox2.getSelectedIndex()-1)+"/");
               //  RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString().substring(0, 3), 0);
                
                filters.add(rf);
                 System.out.println("Filter 2 added index ==0 size "+filters.size());
                 sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
               //  System.out.println("value"+jComboBox2.getSelectedItem().toString().substring(0, 2));
                 RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(StringUtils.capitalize(shortMonths[jComboBox2.getSelectedIndex()-2])+"|-"+ String.format("%02d", jComboBox2.getSelectedIndex()-1)+"-", 0);
                 System.out.println("valeur"+StringUtils.capitalize(shortMonths[jComboBox2.getSelectedIndex()-2])+"|/"+ String.format("%02d", jComboBox2.getSelectedIndex()-1)+"/");
                // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(shortMonths[jComboBox2.getSelectedIndex()], 0);
                //RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString().substring(0, 3), 0);
                filters.set(0, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

       // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
        } else {
            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
            // sorter.setRowFilter(rf);
            if (filters.isEmpty()) {
                System.out.println("value"+jComboBox2.getSelectedItem().toString().substring(0, 2));
                //RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(shortMonths[jComboBox2.getSelectedIndex()], 0);
               // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString().substring(0, 3), 0);
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.add(rf);
                System.out.println("Filter 2 added index ==0 size"+filters.size());
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                 System.out.println("value"+jComboBox2.getSelectedItem().toString().substring(0, 2));
               //  RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(shortMonths[jComboBox2.getSelectedIndex()], 0);
              //  RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString().substring(0, 3), 0);
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.set(0, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }
          
        if(first_item2==false){ first_item2=true;  System.out.println("done firstite");}
        
        System.out.println("Filter size after"+ filters.size());
         }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        final TontineUser copie= this;
        
        
      //  Sauvegarde des anciennes valeurs 
       
        if (jTable1.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez choisir un mois");
        } else if ((Boolean)jTable1.getValueAt(jTable1.getSelectedRow(),5)== false){
            JOptionPane.showMessageDialog(null, "Ceci est un retrait! Veuillez choisir un mois");
        }else {
            final int oldjIndex1 = jComboBox1.getSelectedIndex();
            final int oldjIndex2 = jComboBox2.getSelectedIndex();
            System.out.println(jTable1.getValueAt(jTable1.getSelectedRow(),5)+"old index"+oldjIndex1);
            Tontinedata tontdata = null;
          
            try {
                
                tontdata = new Tontinedata(this.IdEpargnant, this.typeEpargnant, (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 0), startDate);
               
               
            } catch (ParseException ex) {
                Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
            }
            tontdata.setLocationRelativeTo(null);
            tontdata.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            tontdata.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                try {
                 // TODO add your handling code here:
                   if(additional)  data=getTontine2();
                   else data= getTontine();
                   //  originalTableModel=data;
                } catch (Exception ex) {
                     Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
                }
                    Object[][] out = to2DimArray(data);

jTable1.setFillsViewportHeight(true);
jTable1.setAutoCreateRowSorter(true);


jTable1.setModel(new javax.swing.table.DefaultTableModel(out,

    new String [] {
        "Date", "Mise", "Total", "Total retraits", "Idtontine", "Type"
    }
){

    Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, Integer.class,
        Boolean.class};
    @Override
    //  public Class getColumnClass(int columnIndex) {
        //     return this.types[columnIndex];
        //  }

    public Class getColumnClass(int column)
    {
        for (int row = 0; row < getRowCount(); row++)
        {
            Object o = getValueAt(row, column);

            if (o != null)
            return o.getClass();
        }

        return Object.class;
    }
}
);

// Sorter 
sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
addListenerToSorter(sorter, jLabel5);
addListenerToSorter(sorter, jLabel3);

    jTable1.getModel().addTableModelListener(new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {
            NumberFormat n = NumberFormat.getCurrencyInstance(new Locale( "fr", "TG"));
            double sumcot =copie.getSumCumul().doubleValue();
            String s = n.format(sumcot);
            jLabel5.setText(s);
            double sumcot2 =copie.getSumCumul().doubleValue();
            String s2 = n.format(sumcot2);
            jLabel3.setText(s2);
        }
    });

jTable1.setRowSorter(sorter);

// renderer
jTable1.setDefaultRenderer(Object.class, new TabelaCellRenderer());
jTable1.getColumn("Date").setCellRenderer(new TabelaCellRenderer());
jTable1.getColumnModel().getColumn(4).setMinWidth(0);
jTable1.getColumnModel().getColumn(4).setMaxWidth(0);
jTable1.getColumnModel().getColumn(5).setMinWidth(0);
jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
jComboBox1.setSelectedIndex(0);
jComboBox1.setSelectedIndex(oldjIndex1);

jComboBox2.setSelectedIndex(0);
jComboBox2.setSelectedIndex(oldjIndex2);

jTable1.revalidate();
jTable1.repaint();
}});  
            

 Boolean checkmise = false;
 if (additional) checkmise = tontdata.CheckMise(carnet);
 else checkmise = tontdata.CheckMise();           
           if (!checkmise) {
                final Tontinedata  tontinedata2=tontdata;
                if (additional) miseTontine= new MiseTontine(this.IdEpargnant, this.typeEpargnant, (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 0), 0, carnet);
                else  miseTontine= new MiseTontine(this.IdEpargnant, this.typeEpargnant, (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 0), 0);
                
                miseTontine.setLocationRelativeTo(null);
                miseTontine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                miseTontine.addWindowListener(new java.awt.event.WindowAdapter() {
                     @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                       if (miseTontine.getMise()!=0){
                            tontinedata2.setMise(miseTontine.getMise());
                            tontinedata2.setVisible(true);  
                            jComboBox1.setSelectedIndex(oldjIndex1);
                            jComboBox2.setSelectedIndex(oldjIndex2);
        }
            
    }
    
    
    
});
                miseTontine.setVisible(true);
            } else {
               
                if(jTable1.getValueAt(jTable1.getSelectedRow(), 1) != null) {
                    
                    System.out.println("I am in not null");
                Double d=new Double((double)jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                tontdata.setMise(d.intValue());
                try {
                    tontdata.fillcotbuttons();
                } catch (SQLException ex) {
                    Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
                }
                tontdata.setVisible(true);  
            }else{
                    
                      System.out.println("I am in null");
                final Tontinedata  tontinedata2=tontdata;
                if (additional) miseTontine= new MiseTontine(this.IdEpargnant, this.typeEpargnant, (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 0), 0, carnet);
                else  miseTontine= new MiseTontine(this.IdEpargnant, this.typeEpargnant, (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 0), 0);
                
                miseTontine.setLocationRelativeTo(null);
                miseTontine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                miseTontine.addWindowListener(new java.awt.event.WindowAdapter() {
                     @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                       if (miseTontine.getMise()!=0){
                            tontinedata2.setMise(miseTontine.getMise());
                            tontinedata2.setVisible(true);  
                            jComboBox1.setSelectedIndex(oldjIndex1);
                            jComboBox2.setSelectedIndex(oldjIndex2);
                        } 
                    
                }});
                
                
        }  miseTontine.setVisible(true);}}
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        final TontineUser copie= this;
        final int oldjIndex1 = jComboBox1.getSelectedIndex();
        final int oldjIndex2 = jComboBox2.getSelectedIndex();
        RetraitTontine retTont;
        if (additional) retTont = new RetraitTontine(this.IdEpargnant, this.typeEpargnant, this, carnet);
        else retTont= new RetraitTontine(this.IdEpargnant, this.typeEpargnant);
        retTont.setLocationRelativeTo(null);
        retTont.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        retTont.setVisible(true);
        
        retTont.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                try {
                 // TODO add your handling code here:
                    Locale specifiedLocale = new Locale( "fr", "TG");
                    NumberFormat n = NumberFormat.getCurrencyInstance(specifiedLocale);
                    if(additional) data = getTontine2();
                    else data=getTontine();
                    
                    jLabel2.setText(n.format(getTotalTontine()-getTotalRetraits()));
                    double doublePayment =getSumCumul().doubleValue();
                    String s = n.format(doublePayment);
                    jLabel5.setText(s);
                    specifiedLocale = new Locale( "fr", "TG");
                    n = NumberFormat.getCurrencyInstance(specifiedLocale);
                    double doubleRet =getSumRet().doubleValue();
                    String s2 = n.format(doubleRet);
                    jLabel3.setText(s2);
                   //  originalTableModel=data;
                } catch (Exception ex) {
                     Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
                }
                    Object[][] out = to2DimArray(data);

                jTable1.setFillsViewportHeight(true);
                jTable1.setAutoCreateRowSorter(true);


                jTable1.setModel(new javax.swing.table.DefaultTableModel(out,

             new String [] {
        "Date", "Mise", "Total", "Total retraits", "Idtontine", "Type"
    }
){

    Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, Integer.class,
        Boolean.class};
    @Override

    public Class getColumnClass(int column)
    {
        for (int row = 0; row < getRowCount(); row++)
        {
            Object o = getValueAt(row, column);

            if (o != null)
            return o.getClass();
        }

        return Object.class;
    }
}
);
                
// Sorter 
sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
addListenerToSorter(sorter, jLabel5);
addListenerToSorter(sorter, jLabel3);

    jTable1.getModel().addTableModelListener(new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {
            NumberFormat n = NumberFormat.getCurrencyInstance(new Locale( "fr", "TG"));
            double sumcot =copie.getSumCumul().doubleValue();
            String s = n.format(sumcot);
            jLabel5.setText(s);
            double sumcot2 =copie.getSumCumul().doubleValue();
            String s2 = n.format(sumcot2);
            jLabel3.setText(s2);
        }
    });

jTable1.setRowSorter(sorter);

// renderer
jTable1.setDefaultRenderer(Object.class, new TabelaCellRenderer());
jTable1.getColumn("Date").setCellRenderer(new TabelaCellRenderer());
jTable1.getColumnModel().getColumn(4).setMinWidth(0);
jTable1.getColumnModel().getColumn(4).setMaxWidth(0);
jTable1.getColumnModel().getColumn(5).setMinWidth(0);
jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
jComboBox1.setSelectedIndex(0);
jComboBox1.setSelectedIndex(oldjIndex1);

jComboBox2.setSelectedIndex(0);
jComboBox2.setSelectedIndex(oldjIndex2);

}});
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
     TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());       

      if(evt.getStateChange()==ItemEvent.SELECTED){
             sorter.setRowFilter(filter2);
             jTable1.setRowSorter(sorter);
       
      } else if(evt.getStateChange()==ItemEvent.DESELECTED){
              sorter.setRowFilter(null);
                jTable1.setRowSorter(sorter);
      }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

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
            java.util.logging.Logger.getLogger(TontineUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TontineUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TontineUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TontineUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TontineUser ton = new TontineUser();
                ton.setVisible(true);
                try {
                    Vector tontine = ton.getTontine();
                } catch (Exception ex) {
                    Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        
    }
    
       // Les actions clic droits sont ici
      public void actionPerformed(ActionEvent event) {
        EpargneContext epargne;
        EpargneContextRet epargne2;
        EpargneContext_rel epargne3;
        TontineUser tont;
        JMenuItem menu = (JMenuItem) event.getSource();
        if (menu == menuItemMise) {
            if (jTable1.getValueAt(jTable1.getSelectedRow(), 1)==null){
               JOptionPane.showMessageDialog(null, "Veuillez d'abord renseigner la mise");
            } else {
            MiseTontine mise = new MiseTontine(this.IdEpargnant, this.typeEpargnant, (Date) jTable1.getValueAt(jTable1.getSelectedRow(), 0), 1);
            
            mise.setMise(new Double((double) jTable1.getValueAt(jTable1.getSelectedRow(), 1)).intValue());
            mise.setLocationRelativeTo(null);
            mise.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mise.setVisible(true);
     
//        } else if (menu == menuItemRetraits) {
//       //      System.out.println("MenuItemCredits");
//            if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
//                epargne2 = new EpargneContextRet((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant" );  
//            } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
//                 epargne2 = new EpargneContextRet((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte" );  
//            } else {
//                epargne2 = new EpargneContextRet((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),"", "Pers Morale" );  
//            }
//  
//      
//            epargne2.setLocationRelativeTo(null);
//            epargne2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            epargne2.setVisible(true);
//        } else if (menu == menuItemTontine) {
//             if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
//                 tont= new TontineUser((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant");
//
//             }else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
//                 tont= new TontineUser((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte");
//             } else {
//                 tont= new TontineUser((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Pers Morale"); 
//             }
//             
//            tont.setLocationRelativeTo(null);
//            tont.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            tont.setVisible(true);
//        } else if (menu == menuItemMouvements) {
//          if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
//                epargne3 = new EpargneContext_rel((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant" );  
//            } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
//                 epargne3 = new EpargneContext_rel((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte" );  
//            } else {
//                epargne3 = new EpargneContext_rel((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),"", "Pers Morale" );  
//            }
//  
//      
//            epargne3.setLocationRelativeTo(null);
//            epargne3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            epargne3.setVisible(true);    
//    }
     }} else if (menu == menuItemRet) {
         ModRetraitTontine modret= new ModRetraitTontine((int) jTable1.getValueAt(jTable1.getSelectedRow(), 4));
         modret.setLocationRelativeTo(null);
         modret.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         modret.setVisible(true);
     } else if(menu == menuItemSupp) {
            try {
                deleteRetId((int) jTable1.getValueAt(jTable1.getSelectedRow(), 4));
            } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Erreur, la suppression n'a pu être effectuée");
                Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
            }
     } else if (menu == menuItemSuppTont) {
          try {
                deleteTontId((int) jTable1.getValueAt(jTable1.getSelectedRow(), 4));
            } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null, "Erreur, la suppression n'a pu être effectuée");
                Logger.getLogger(TontineUser.class.getName()).log(Level.SEVERE, null, ex);
            }
         
     }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
