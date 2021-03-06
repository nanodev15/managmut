/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author elommarcarnold
 */
public class EpargneContext extends javax.swing.JFrame {

    String nomEpargnant;
    String prenomEpargnant;
    String typeEpargnant;
    int IdEpargnant;
    private Vector<Vector> data;
    static Connection conn = null;
    Connection connect = null;
    static PreparedStatement pre = null;
    List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form EpargneContext
     */
    private void addListenerToSorter(RowSorter rowSorter,  
                           final JLabel rowCountLabel) {

    rowSorter.addRowSorterListener(new RowSorterListener() {
        @Override
        public void sorterChanged(RowSorterEvent e) {
//            int newRowCount = table.getRowCount();
//            rowCountLabel.setText("Number of view rows: " + newRowCount);
            
       NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
       double doublePayment = getSum().doubleValue();
       String s = n.format(doublePayment);
 
       rowCountLabel.setText(s);
        }
    });
}
    
   public static class StripedTableCellRenderer implements TableCellRenderer {
  public StripedTableCellRenderer(TableCellRenderer targetRenderer,
      Color evenBack, Color evenFore, Color oddBack, Color oddFore) {
    this.targetRenderer = targetRenderer;
    this.evenBack = evenBack;
    this.evenFore = evenFore;
    this.oddBack = oddBack;
    this.oddFore = oddFore;
  }

  // Implementation of TableCellRenderer interface
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    TableCellRenderer renderer = targetRenderer;
    if (renderer == null) {
      // Get default renderer from the table
      renderer = table.getDefaultRenderer(table.getColumnClass(column));
    }

    // Let the real renderer create the component
    Component comp = renderer.getTableCellRendererComponent(table, value,
        isSelected, hasFocus, row, column);

    // Now apply the stripe effect
    if (isSelected == false && hasFocus == false) {
      if ((row & 1) == 0) {
        comp.setBackground(evenBack != null ? evenBack : table
            .getBackground());
        comp.setForeground(evenFore != null ? evenFore : table
            .getForeground());
      } else {
        comp.setBackground(oddBack != null ? oddBack : table
            .getBackground());
        comp.setForeground(oddFore != null ? oddFore : table
            .getForeground());
      }
    }

    return comp;
  }

  // Convenience method to apply this renderer to single column
  public static void installInColumn(JTable table, int columnIndex,
      Color evenBack, Color evenFore, Color oddBack, Color oddFore) {
    TableColumn tc = table.getColumnModel().getColumn(columnIndex);

    // Get the cell renderer for this column, if any
    TableCellRenderer targetRenderer = tc.getCellRenderer();

    // Create a new StripedTableCellRenderer and install it
    tc.setCellRenderer(new StripedTableCellRenderer(targetRenderer,
        evenBack, evenFore, oddBack, oddFore));
  }

  // Convenience method to apply this renderer to an entire table
  public static void installInTable(JTable table, Color evenBack,
      Color evenFore, Color oddBack, Color oddFore) {
    StripedTableCellRenderer sharedInstance = null;
    int columns = table.getColumnCount();
    for (int i = 0; i < columns; i++) {
      TableColumn tc = table.getColumnModel().getColumn(i);
      TableCellRenderer targetRenderer = tc.getCellRenderer();
      if (targetRenderer != null) {
        // This column has a specific renderer
        tc.setCellRenderer(new StripedTableCellRenderer(targetRenderer,
            evenBack, evenFore, oddBack, oddFore));
      } else {
        // This column uses a class renderer - use a shared renderer
        if (sharedInstance == null) {
          sharedInstance = new StripedTableCellRenderer(null,
              evenBack, evenFore, oddBack, oddFore);
        }
        tc.setCellRenderer(sharedInstance);
      }
    }
  }

  protected TableCellRenderer targetRenderer;

  protected Color evenBack;

  protected Color evenFore;

  protected Color oddBack;

  protected Color oddFore;
}
    
 public class DecimalFormatRenderer extends DefaultTableCellRenderer {
   //   private final DecimalFormat formatter = new DecimalFormat( "#.00" );
     DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
     DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
 
     @Override
      public Component getTableCellRendererComponent(
         JTable table, Object value, boolean isSelected,
         boolean hasFocus, int row, int column) {
          System.out.println("Entering Decimal"+ column);
          if (column==1) {
          System.out.println("Entering Decimal");
          symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);
 
         // First format the cell value as required
       //      value = formatter.format((Number)value);
 
         value = formatter.format((Number)value);
 
            // And pass it on to parent class
          }
         return super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column );
      }
   }    
    
    public class YMDRenderer extends DefaultTableCellRenderer {
        private SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        public void setValue(Object value){
            System.out.println("Entering YMDrenderr");
           try {
                if  (value !=null) {
                    value= sdf.format(value); }
           }
           
           catch (IllegalArgumentException e) {
                e.printStackTrace();
           }
                
               super.setValue(value);
        }
    }
    
     public class ThousandSepRenderer extends DefaultTableCellRenderer {
       DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
       DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
     
        public void setValue(Object value){
            symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);
            System.out.println("Entering test");
            
            try {
                 if  (value !=null)
                     value= formatter.format(value);
            }
            
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            //    value= formatter.format(((BigDecimal)value).longValue());
//             if  (value !=null) {
//                System.out.println("value is not null"+value);
//                value= new String("dfdfdfd");
//                
//             } else {
//             System.out.println("value is null");
//
//             }
                super.setValue(value);
        }
    }
     
    public BigDecimal getSum() {
        int rowcount= jTable1.getRowCount();
        BigDecimal sum= new BigDecimal(0);
        BigDecimal money;

        
        for (int i=0; i< rowcount; i++){
           money = new BigDecimal(jTable1.getValueAt(i, 1).toString().replaceAll(" ", ""));
           sum=sum.add(money);
        }
        
        return sum;
        
    }
    public Vector getEpargne() throws Exception {
        conn = Connect.ConnectDb();
        
        String sql ="SELECT DateEpargne, MontantEpargne, MotifEpargne, idEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND MontantEpargne >=0" + " AND TypeEpargnant='" + this.typeEpargnant + "' ";
                
  
                
               
        

                
        if (IntializeDate.change) sql = sql + "AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        
        sql = sql + "ORDER BY DateEpargne";
        pre = conn.prepareStatement(sql);
        System.out.println("SELECT DateEpargne, MontantEpargne, MotifEpargne, IdEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
        ResultSet rs = pre.executeQuery();
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();
        Vector<Vector> membreVector = new Vector<Vector>();
        while (rs.next()) {
     //   Vector<String> membre = new Vector<String>();
            //   membre.add(String.valueOf(i)); 
            Vector<Object> membre = new Vector<Object>();
            Date date=sdf2.parse(rs.getString("DateEpargne"));
           // membre.add(sdf.format(date));
            membre.add(new java.sql.Timestamp(date.getTime()));
            membre.add(rs.getDouble("MontantEpargne"));
            // membre.add(new Double("5.5"));
            membre.add(rs.getString("MotifEpargne"));
            membre.add(rs.getString("idEpargne"));

            //
            membreVector.add(membre);
        }

        /*Close the connection after use (MUST)*/
        if (conn != null) {
            conn.close();
        }
        
        
        final EpargneContext copie= this; 
        System.out.println("Model"+ jTable1.getModel().toString()+ "finish");
jTable1.getModel().addTableModelListener(new TableModelListener() {

    @Override
    public void tableChanged(TableModelEvent e) {
       NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
       double doublePayment =copie.getSum().doubleValue();
       String s = n.format(doublePayment);
       jLabel4.setText(s);
    }
});

        return membreVector;
    }

    public EpargneContext() {
        initComponents();
       
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

    public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    }

    public EpargneContext(String nomEpargnant, String prenomEpargnant, String typeEpargnant) {
        this.nomEpargnant = nomEpargnant;
        this.prenomEpargnant = prenomEpargnant;
        this.typeEpargnant = typeEpargnant;

        System.out.println("Nom" + this.nomEpargnant);
        System.out.println("Prénom" + this.prenomEpargnant);
        System.out.println("Type" + this.typeEpargnant);
        this.IdEpargnant = getId(this.nomEpargnant, this.prenomEpargnant, this.typeEpargnant);
        setTitle("Historique des épargnes de " + this.nomEpargnant + " " + this.prenomEpargnant);
        initComponents();
    }
    public int findyearindex(String year){
        for (int i=2; i<jComboBox2.getItemCount(); i++) {
            if (((String) jComboBox2.getItemAt(i)).equalsIgnoreCase(year)) 
                return i;
        }
        
        return -1;
    }
    public void refresh() {
        try {
            // TODO add your handling code here:
            data = getEpargne();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object[][] out = to2DimArray(data);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setFillsViewportHeight(true);
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setPreferredScrollableViewportSize(new Dimension(1000, 70));
 //       jTable1.getModel().notifyAll();
        jTable1.setModel(new javax.swing.table.DefaultTableModel(out,
//                // new Object [][] {
//                //    {"20/10/2011", "500000000", "Dépôt initial", "1"},
//                //    {null, null, null, null},
//                //  {null, null, null, null},
//                //      {null, null, null, null},
//                //     {null, null, null, null},n
//                //   {null, null, null, null},
//                // {null, null, null, null},
//                //      {null, null, null, null},
//                //      {null, null, null, null},
//                //      {null, null, null, null},
//                //      {null, null, null, null},
//                //     {null, null, null, null},
//                //   {null, null, null, null},
//                //     {null, null, null, null},
//                //      {null, null, null, null},
//                //    {null, null, null, null},
//                //    {null, null, null, null},
//                //   {null, null, null, null},
//                //    {null, null, null, null},
//                //     {null, null, null, null}
//                // },
               new String[]{
                    "Date ", "Montant(en Frcs CFA)", "Motif", "ID"
               }
                
                
       ){   
            
           Class[] types = {java.sql.Timestamp.class, BigDecimal.class, String.class,
                    String.class};
            @Override
                public Class getColumnClass(int columnIndex) {
                    return this.types[columnIndex];
                }
       });
        
        
        
        jTable1.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        jTable1.getColumnModel().getColumn(3).setMinWidth(0);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(215);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(180);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(238);
        jTable1.getColumn("Date ").setCellRenderer(new YMDRenderer()); 
        jTable1.setDefaultRenderer(BigDecimal.class, new ThousandSepRenderer());
        jTable1.getColumn("Montant(en Frcs CFA)").setCellRenderer(new ThousandSepRenderer());
        sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) jTable1.getModel());
        jTable1.setRowSorter(sorter);
        StripedTableCellRenderer.installInTable(jTable1, Color.lightGray,
        Color.white, null, null);
        String itemselected= (String) jComboBox2.getSelectedItem();
        int size = jComboBox2.getItemCount();
        for (int i = 2; i < size; i++) {
            jComboBox2.removeItemAt(2);
        }
        
 Locale specifiedLocale = new Locale( "fr", "TG");
NumberFormat n = NumberFormat.getCurrencyInstance(specifiedLocale);

  
//Currency currentCurrency = Currency.getInstance(specifiedLocale);
double doublePayment =this.getSum().doubleValue();

String s = n.format(doublePayment);
       jLabel4.setText(s);
       
       //Add listener to sorter
       
       addListenerToSorter(sorter, jLabel4);

        
        ResultSet rst = null;
        String sql = "SELECT YEAR(DateEpargne) FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MontantEpargne >=0";
        if (IntializeDate.change) sql = sql + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
        
        sql= sql + " GROUP BY YEAR(DateEpargne)";

        connect = Connect.ConnectDb();
        Statement stmt = null;
        try {
            stmt = connect.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                jComboBox2.addItem(rst.getString(1));
            }
            
            if (! itemselected.equalsIgnoreCase("tous")) {
                int position= findyearindex(itemselected);
                if (position !=-1) {
                    jComboBox2.setSelectedIndex(position);
                } else {
                    jComboBox2.setSelectedIndex(0);
                }
                
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox(new Object[] {
            "Tous",
            new JSeparator(JSeparator.HORIZONTAL),

        }) {
            @Override
            public void setSelectedItem(Object item) {
                if (item.getClass()!= JSeparator.class)
                super.setSelectedItem(item);
            }

        };

        //};
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jTable1.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
    //jTable1.getColumnModel().getColumn(3).setMinWidth(0);
    //jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

    //jTable1.removeColumn(jTable1.getC);
    //Object[][] datatest =
    //        {
        //           {new java.sql.Timestamp(new Date().getTime()), new Double(1), "Test1"},
        //           {new java.sql.Timestamp(new Date().getTime()), new Double(2.25), "Test2"},
        //           {new java.sql.Timestamp(new Date().getTime()), new Double(12.34), "Test3" },
        //           {new java.sql.Timestamp(new Date().getTime()), new Double(1234.56), "Test4"},
        //           {new java.sql.Timestamp(new Date().getTime()), new Double("2342.56"), "Test5"}
        //       };
    try {
        // TODO add your handling code here:
        data=getEpargne();
    } catch (Exception ex) {
        Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
    }

    Object[][] out = to2DimArray(data);
    System.out.println(out);
    System.out.println(data);
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable1.setFillsViewportHeight(true);
    jTable1.setAutoCreateRowSorter(true);
    jTable1.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable1.setModel(new javax.swing.table.DefaultTableModel(out,
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
        new String[]{
            "Date ", "Montant(en Frcs CFA)", "Motif", "ID"
        }

    ){

        Class[] types = {java.sql.Timestamp.class, Double.class, String.class,
            String.class};
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
    //NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
    //       DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRENCH);
    //       DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
    //        symbols.setGroupingSeparator('-');
    //       formatter.setDecimalFormatSymbols(symbols);

    //      TableCellRenderer renderer = new NumberRenderer(formatter);
    jTable1.setDefaultRenderer(Double.class, new DecimalFormatRenderer());

    jTable1.getColumnModel().getColumn(3).setMinWidth(0);
    jTable1.getColumnModel().getColumn(3).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(215);
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(180);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(238);
    sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
    addListenerToSorter(sorter, jLabel4);
    jTable1.setRowSorter(sorter);

    //jTable1.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
    //jTable1.setDefaultRenderer(Double.class, new ThousandSepRenderer());
    //System.out.println("type: "+jTable1.getValueAt(0, 1).getClass());
    //jTable1.getColumn("Montant(en Frcs CFA)").setCellRenderer(new DecimalFormatRenderer());
    //System.out.println("cellrenderer utilise" +jTable1.getColumn("Montant(en Frcs CFA)").getCellRenderer().getClass());
    //jTable1.getColumn("Motif").setCellRenderer(new YMDRenderer());
    //jTable1.getColumnModel().getColumn(1).setCellRenderer(new ThousandSepRenderer());
    jTable1.getColumn("Date ").setCellRenderer(new YMDRenderer());

    //new ThousandSepRenderer()   // new YMDRenderer()
    final EpargneContext copie= this;
    jTable1.getModel().addTableModelListener(new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {
            NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            double doublePayment =copie.getSum().doubleValue();
            String s = n.format(doublePayment);
            System.out.println("changed"+s);
            jLabel4.setText(s);
        }
    });

    StripedTableCellRenderer.installInTable(jTable1, Color.lightGray,
        Color.white, null, null);

    //DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
    //leftRenderer.setHorizontalAlignment(JLabel.LEFT);
    //jTable1.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
    jScrollPane1.setViewportView(jTable1);

    jButton1.setText("Nouveau >>");
    final JPopupMenu menu = new JPopupMenu();
    JMenuItem item1 = new JMenuItem("Dépôt à vue");
    JMenuItem item2 = new JMenuItem("Dépôt initial");
    JMenuItem item3 = new JMenuItem("Autre");

    item1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionevent){
            NewEpargne epargne= new NewEpargne("Dépôt à vue",nomEpargnant,prenomEpargnant,typeEpargnant, true);
            epargne.setLocationRelativeTo(null);
            epargne.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            epargne.setVisible(true);
            epargne.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent we) {
                    refresh();
                }});

            }});

            item2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionevent){
                    NewEpargne epargne= new NewEpargne("Dépôt initial", nomEpargnant, prenomEpargnant, typeEpargnant, true);
                    epargne.setLocationRelativeTo(null);
                    epargne.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    epargne.setVisible(true);
                    epargne.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent we) {
                            refresh();
                        }});

                    }});

                    item3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionevent){
                            NewEpargne epargne= new NewEpargne("autre", nomEpargnant,prenomEpargnant,typeEpargnant, true);
                            epargne.setLocationRelativeTo(null);
                            epargne.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            epargne.setVisible(true);
                            epargne.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent we) {
                                    refresh();
                                }});

                            }});

                            menu.add(item1);
                            menu.add(item2);
                            menu.add(item3);
                            final int height= menu.getPreferredSize().height;
                            jButton1.addMouseListener(new MouseAdapter(){
                                public void mouseReleased(MouseEvent e){
                                    if (e.getButton() == 1){
                                        menu.show(e.getComponent(), 0 , jButton1.getHeight());
                                    }
                                }
                            });

                            jButton2.setText("Editer");
                            jButton2.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    jButton2ActionPerformed(evt);
                                }
                            });

                            jButton3.setText("Supprimer");
                            jButton3.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    jButton3ActionPerformed(evt);
                                }
                            });

                            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tous", "Dépôt initial", "Dépôt à vue", " " }));
                            jComboBox1.addItemListener(new java.awt.event.ItemListener() {
                                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                                    jComboBox1ItemStateChanged(evt);
                                }
                            });

                            jComboBox2.setRenderer(new SeparatorComboBoxRenderer());

                            ResultSet rst=null;
                            String sql="SELECT YEAR(DateEpargne) FROM Epargne WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"'";
                            try {
                                IntializeDate dateini = new IntializeDate();
                                if (dateini.change) sql = sql + " AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "' ";
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            sql=sql+" GROUP BY YEAR(DateEpargne) ";
                            System.out.println("sql"+sql);

                            //  String sql="SELECT YEAR(DateEpargne) FROM Epargne GROUP BY YEAR(DateEpargne)";
                            connect=Connect.ConnectDb();
                            Statement stmt = null;
                            try {
                                stmt= connect.createStatement();
                                rst=stmt.executeQuery(sql);
                                while(rst.next()) {
                                    jComboBox2.addItem(rst.getString(1));
                                    System.out.println("year"+rst.getString(1));
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            jComboBox2.addItemListener(new java.awt.event.ItemListener() {
                                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                                    jComboBox2ItemStateChanged(evt);
                                }
                            });

                            jLabel1.setText("Motif");

                            jLabel2.setText("Année");

                            jLabel3.setText("Total:");

                            jLabel4.setText("jLabel4");
                            //NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE);
                            //double doublePayment =this.getSum().doubleValue();

                            //String s = n.format(doublePayment);
                            //jLabel4.setText(s);
                            //Locale specifiedLocale = new Locale( "fr", "TG", "F cfa");
                            Locale specifiedLocale = new Locale( "fr", "TG");
                            NumberFormat n = NumberFormat.getCurrencyInstance(specifiedLocale);

                            Currency currentCurrency = Currency.getInstance(specifiedLocale);
                            //n.setCurrency(Currency.getInstance("F cfa"));
                            double doublePayment =this.getSum().doubleValue();
                            // DecimalFormat formatter = (DecimalFormat)
                            //                    NumberFormat.getCurrencyInstance(Locale.FRENCH);
                            //DecimalFormatSymbols symbol =
                            //                    new DecimalFormatSymbols(Locale.FRENCH);
                            //            symbol.setCurrencySymbol("F cfa");
                            //formatter.setDecimalFormatSymbols(symbol);
                            //n.setDecimalFormatSymbols(symbol);
                            String s = n.format(doublePayment);
                            jLabel4.setText(s);

                            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
                            getContentPane().setLayout(layout);
                            layout.setHorizontalGroup(
                                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                            .add(jButton1)
                                            .add(18, 18, 18)
                                            .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(18, 18, 18)
                                            .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(18, 18, 18)
                                            .add(jLabel3)
                                            .add(18, 18, 18)
                                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                            .add(0, 286, Short.MAX_VALUE)
                                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(jLabel2))
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE))
                                    .addContainerGap())
                            );
                            layout.setVerticalGroup(
                                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup()
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel1)
                                        .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 299, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jButton1)
                                        .add(jButton2)
                                        .add(jButton3)
                                        .add(jLabel3)
                                        .add(jLabel4))
                                    .add(27, 27, 27))
                            );

                            pack();
                        }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (!jTable1.getSelectionModel().isSelectionEmpty()) {
            System.out.println("valeur IdEpargne "+jTable1.getValueAt(jTable1.getSelectedRow(), 3));
            Date date = new Date();
//            try {
               // date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                  date= (java.sql.Timestamp) jTable1.getValueAt(jTable1.getSelectedRow(), 0);  
//            } catch (ParseException ex) {
//                Logger.getLogger(EpargneContext.class.getName()).log(Level.SEVERE, null, ex);
//            }

            ModifyEpargne epargne = new ModifyEpargne(Integer.parseInt((String) jTable1.getValueAt(jTable1.getSelectedRow(), 3)), date, jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString(), (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2), true);
            epargne.setLocationRelativeTo(null);
            epargne.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            epargne.setVisible(true);
            epargne.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent we) {
                    refresh();
                }
            });
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox2.getSelectedIndex() != 0) {
            if (filters.isEmpty()) {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
                filters.add(rf);
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
                filters.set(0, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

       // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
        } else {
            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
            // sorter.setRowFilter(rf);
            if (filters.isEmpty()) {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.add(rf);
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.set(0, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox1.getSelectedIndex() != 0) {
            if (filters.size() < 2) {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox1.getSelectedItem().toString(), 2);
                filters.add(rf);
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox1.getSelectedItem().toString(), 2);
                filters.set(1, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

       // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
        } else {
            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
            // sorter.setRowFilter(rf);
            if (filters.size() < 2) {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.add(rf);
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                filters.set(1, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (! jTable1.getSelectionModel().isSelectionEmpty()) {
            connect = Connect.ConnectDb();
             boolean success=true;
             PreparedStatement pst=null;
             
                    String sql="delete from Epargne where idEpargne='" +jTable1.getValueAt(jTable1.getSelectedRow(), 3)+"'";
                    
             try {
                 pst=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pst.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             
             if (success) {
                 JOptionPane.showMessageDialog(null, "Suppression effectuée avec succès");
                 this.refresh();
             }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(EpargneContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EpargneContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EpargneContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EpargneContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //EpargneContext context=  new EpargneContext();
                EpargneContext context = new EpargneContext("dfdf", "edede", "Pers Morale");
                context.pack();
                context.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
