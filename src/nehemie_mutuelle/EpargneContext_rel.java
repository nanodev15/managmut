/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author elommarcarnold
 */
public class EpargneContext_rel extends javax.swing.JFrame implements ActionListener {

    String nomEpargnant;
    String prenomEpargnant;
    String typeEpargnant;
    private JPopupMenu popupMenu;
    private JMenuItem menuItemExeption;
    int IdEpargnant;
    private Vector<Vector> data;
    static Connection conn = null;
    Connection connect = null;
    static PreparedStatement pre = null;
    public List<Date> ftcexceptionlist = new ArrayList<>();
    List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
    boolean first_item1 = false;
    boolean first_item2 = false;
    Boolean wipe = false;
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
                NumberFormat n = NumberFormat.getCurrencyInstance(new Locale("fr", "TG"));
                double doublePayment = (double) 0;
                if (rowCountLabel == jLabel4) {
                    doublePayment = getSum().doubleValue();
                } else if (rowCountLabel == jLabel6) {
                    doublePayment = getSumDebit().doubleValue();
                } else {
                    doublePayment = getSumCredit().doubleValue();
                }
                String s = n.format(doublePayment);
                rowCountLabel.setText(s);
//       NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
//       double doublePayment = getSum().doubleValue();
//       String s = n.format(doublePayment);
//       System.out.println("changed"+s);
//       rowCountLabel.setText(s);
            }
        });
    }

    public boolean isFirstDayOfTheMonth(Date dateToday) {
        Calendar c = new GregorianCalendar();
        c.setTime(dateToday);
        return c.get(Calendar.DAY_OF_MONTH) == 1;
    }

    public void addYearComboitem() {
        for (int i = 0; i < jTable1.getModel().getRowCount(); i++) {
            Date d = (Date) jTable1.getModel().getValueAt(i, 0);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int year = c.get(Calendar.YEAR);
            if (jComboBox2.getItemCount() > 2 && !((String) jComboBox2.getItemAt(jComboBox2.getItemCount() - 1)).equalsIgnoreCase(String.valueOf(year))) {
                jComboBox2.addItem(String.valueOf(year));
            } else if (jComboBox2.getItemCount() == 2) {
                jComboBox2.addItem(String.valueOf(year));
            }

        }
    }

    public void fillExceptionList() throws SQLException {
        connect = Connect.ConnectDb();
        String sql = "SELECT Dateftc FROM Exceptionftc WHERE IdEpargnant ='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'";
        PreparedStatement pst = connect.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            ftcexceptionlist.add(rst.getDate("Dateftc"));
        }

        //Closing
        rst.close();
        pst.close();
        connect.close();
    }

    public boolean isTwentyEighthDayOfTheMonth(Date dateToday) {
        Calendar c = new GregorianCalendar();
        c.setTime(dateToday);
        return c.get(Calendar.DAY_OF_MONTH) == 28;
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
      //  comp.setForeground(evenFore != null ? evenFore : table
                    //     .getForeground());
                    comp.setForeground(Color.BLACK);
                } else {
                    comp.setBackground(oddBack != null ? oddBack : table
                            .getBackground());
       // comp.setForeground(oddFore != null ? oddFore : table
                    //    .getForeground());
                    comp.setForeground(Color.BLACK);
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
            System.out.println("Entering Decimal" + column);
            if ((column == 1 || column == 2 || column == 3) && value != null) {
                System.out.println("Entering Decimal");
                symbols.setGroupingSeparator(' ');
                formatter.setDecimalFormatSymbols(symbols);

         // First format the cell value as required
                //      value = formatter.format((Number)value);
                value = formatter.format((Number) value);

                // And pass it on to parent class
            }
            return super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
        }
    }

    public class YMDRenderer extends DefaultTableCellRenderer {

        //  private SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        public void setValue(Object value) {
            System.out.println("Entering YMDrenderr");
            try {
                if (value != null) {
                    value = sdf.format(value);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            super.setValue(value);
        }
    }

    public class ThousandSepRenderer extends DefaultTableCellRenderer {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        public void setValue(Object value) {
            symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);
            System.out.println("Entering test");

            try {
                if (value != null) {
                    value = formatter.format(value);
                }
            } catch (IllegalArgumentException e) {
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
//        int rowcount= jTable1.getRowCount();
//        BigDecimal sum= new BigDecimal(0);
//        BigDecimal money;
//
//        
//        for (int i=0; i< rowcount; i++){
//            if ((jTable1).getValueAt(i, 1) != null) {
//           money = new BigDecimal("-"+jTable1.getValueAt(i, 1).toString().replaceAll(" ", ""));
//            } else {
//            money = new BigDecimal(jTable1.getValueAt(i, 2).toString().replaceAll(" ", ""));  
//          
//            }  
//            
//            
//           sum=sum.add(money);
//         
//        }

        if (jTable1.getRowCount() != 0) {
            return new BigDecimal(jTable1.getValueAt(jTable1.getRowCount() - 1, 3).toString().replaceAll(" ", ""));
        } else {
            return new BigDecimal(0);
        }

    }

    public BigDecimal getSumDebit() {
        int rowcount = jTable1.getRowCount();
        BigDecimal sum = new BigDecimal(0);
        BigDecimal money;

        for (int i = 0; i < rowcount; i++) {
            if ((jTable1).getValueAt(i, 1) != null) {
                money = new BigDecimal("-" + jTable1.getValueAt(i, 1).toString().replaceAll(" ", ""));
            } else {
                money = new BigDecimal(0);
            }

            sum = sum.add(money);

        }

        return sum;
    }

    public BigDecimal getSumCredit() {
        int rowcount = jTable1.getRowCount();
        BigDecimal sum = new BigDecimal(0);
        BigDecimal money;

        for (int i = 0; i < rowcount; i++) {
            if ((jTable1).getValueAt(i, 2) != null) {
                money = new BigDecimal(jTable1.getValueAt(i, 2).toString().replaceAll(" ", ""));
            } else {
                money = new BigDecimal(0);
            }

            sum = sum.add(money);

        }

        return sum;
    }

    public Vector getEpargne() throws Exception {
        conn = Connect.ConnectDb2();
        fillExceptionList();
        String sql ="";
        IntializeDate initDate = new IntializeDate();
        //    pre = conn.prepareStatement("SELECT DateEpargne, MontantEpargne, MotifEpargne, idEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
       
                
                sql="SELECT id AS ide, w AS dte, d AS description, \n"
                + "   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n"
                + "   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n"
                + "  FROM\n"
                + "  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n"
                + "          Epargne.MontantEpargne AS a\n"
                + "     FROM Epargne\n"
                + "     WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'\n";
        
        if (initDate.change) sql = sql + "AND DateEpargne  >= '"+new java.sql.Date(IntializeDate.initdate.getTime()) + "'";
        sql =sql + " ORDER BY DateEpargne) t";   // change instead of group by
        System.out.println("SELECT DateEpargne, MontantEpargne, MotifEpargne, IdEpargne FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' ORDER BY DateEpargne");
        System.out.println("query:" + "SELECT id AS ide, w AS dte, d AS description, \n"
                + "   CASE WHEN (a>=0) THEN a ELSE NULL END AS cshIN,\n"
                + "   CASE WHEN (a<0) THEN SUBSTR(a,2,10) ELSE NULL END AS cshOUT\n"
                + "  FROM\n"
                + "  (SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d, \n"
                + "          Epargne.MontantEpargne AS a\n"
                + "     FROM Epargne\n"
                + "     WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "'\n"
                + "     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne) ");
        
        pre = conn.prepareStatement(sql);
        ResultSet rs = pre.executeQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

        Vector<Vector> membreVector = new Vector<Vector>();
        Double balance = 0d;
        boolean firstentry = true;
        Date previous = null;
        Date date = new Date();
        Date d = new Date();
        boolean onceftc = false;
        Date lastprevdate = new Date();  // Last date of prelev
        while (rs.next()) {
            if (firstentry) {
                previous = sdf2.parse(rs.getString("dte"));   // modified
            }
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

            date = sdf2.parse(rs.getString("dte"));
            System.out.println("date" + date + "previous" + previous);
            // Modified 
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(previous);

            gcal.set(Calendar.MILLISECOND, 0);
            gcal.set(Calendar.SECOND, 0);
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.HOUR_OF_DAY, 0);

            while (!gcal.getTime().after(date)) {
                d = gcal.getTime();
                if (isTwentyEighthDayOfTheMonth(d) && balance >= 100 && (firstentry == false) && !ftcexceptionlist.contains(d)) {
                    
                   
//                    Date dateref = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2018-09-28 00:00:00");
//                    if (d.equals(dateref)) {
//                        System.out.println("equalityO" + d);
//                    } else {
//                        System.out.println("no equalityO" + d);
//                    }
//                    System.out.println("first entry" + firstentry);
//                    
                    if (lastprevdate.getTime() != d.getTime() && onceftc==true) {
                        Vector<Object> membre0 = new Vector<Object>();
                       membre0.add(new java.sql.Timestamp(d.getTime()));
                        membre0.add(new Double(100));
                    
                    lastprevdate = d;
                    membre0.add(null);
                    balance = balance - 100;
                    membre0.add(balance);
                    membre0.add("Frais de tenue de compte");
                    membreVector.add(membre0);
                    lastprevdate = d;

                    } else if (onceftc== false) {
                     Vector<Object> membre0 = new Vector<Object>();
                    membre0.add(new java.sql.Timestamp(d.getTime()));
                    membre0.add(new Double(100));
                    
                    lastprevdate = d;
                    membre0.add(null);
                    balance = balance - 100;
                    membre0.add(balance);
                    membre0.add("Frais de tenue de compte");
                    membreVector.add(membre0);
                    onceftc = true;

                        
                    }
                   
                }
                gcal.add(Calendar.DAY_OF_YEAR, 1);
            }

            Vector<Object> membre = new Vector<Object>();

            membre.add(new java.sql.Timestamp(date.getTime()));
            if (rs.getDouble("cshOUT") > 0) {
                membre.add(rs.getDouble("cshOUT"));
            } else {
                membre.add(null);
            }
            if (rs.getDouble("cshIN") > 0) {
                membre.add(rs.getDouble("cshIN"));
            } else {
                membre.add(null);
            }
            balance = balance - rs.getDouble("cshOUT") + rs.getDouble("cshIN");
            membre.add(balance);
            membre.add(rs.getString("description"));
            membre.add(rs.getString("ide"));
            membreVector.add(membre);

            previous = date; // modified
            firstentry = false; //modified
        }

        // retraits des frais de tenue de compte jusqu'au jour actuel
        if (!firstentry) {
            Date date2 = new Date();
            // Modified 
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(previous);
            gcal.set(Calendar.MILLISECOND, 0);
            gcal.set(Calendar.SECOND, 0);
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.HOUR_OF_DAY, 0);

            while (!gcal.getTime().after(date2)) {

                Date d2 = gcal.getTime();
                if (isTwentyEighthDayOfTheMonth(d2) && balance >= 100 && !ftcexceptionlist.contains(d2) && (!(isTwentyEighthDayOfTheMonth(previous) && onceftc == true && DateUtils.isSameDay(d2, d)))) {
                    Date dateref = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2018-09-28 00:00:00");
                    if (d2.equals(dateref)) {
                        System.out.println("equality1");
                    }
                    
                    if (lastprevdate.getTime() != d2.getTime() && onceftc==true) {
                    
                    Vector<Object> membre0 = new Vector<Object>();
                    membre0.add(new java.sql.Timestamp(d2.getTime()));
                    
                    membre0.add(new Double(100));
                    lastprevdate = d2;
                    membre0.add(null);
                    balance = balance - 100;
                    membre0.add(balance);
                    membre0.add("Frais de tenue de compte");
                    membreVector.add(membre0);
                      lastprevdate = d2;
                    } else if (onceftc == false) {
                                Vector<Object> membre0 = new Vector<Object>();
                    membre0.add(new java.sql.Timestamp(d2.getTime()));
                    
                    membre0.add(new Double(100));
                    lastprevdate = d2;
                    membre0.add(null);
                    balance = balance - 100;
                    membre0.add(balance);
                    membre0.add("Frais de tenue de compte");
                    membreVector.add(membre0);
                        onceftc = true;  // added
                    }
                   
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

        final EpargneContext_rel copie = this;
        System.out.println("Model" + jTable1.getModel().toString() + "finish");
        jTable1.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                NumberFormat n = NumberFormat.getCurrencyInstance(new Locale("fr", "TG"));
                double doublePayment = copie.getSum().doubleValue();
                String s = n.format(doublePayment);
                jLabel4.setText(s);
                doublePayment = getSumDebit().doubleValue();
                s = n.format(doublePayment);
                jLabel6.setText(s);
                doublePayment = getSumCredit().doubleValue();
                s = n.format(doublePayment);
                jLabel8.setText(s);
//       NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
//       double doublePayment =copie.getSum().doubleValue();
//       String s = n.format(doublePayment);
//       jLabel4.setText(s);
            }
        });

        return membreVector;
    }

    public EpargneContext_rel() {
        initComponents();

    }

    public class DynamicTableModel extends AbstractTableModel {

        /**
         * The column names.
         */
        private String[] columnNames;
        /**
         * The data.
         */
        private Object[][] data;

        /**
         * Instantiates a new dynamic table model.
         *         
* @param dataset the dataset
         */
        public DynamicTableModel(String[] columnNames, Object[][] data) {
            super();
            this.data = data;
            for (int i = 0; i < data.length; i++) {
                data[i][0] = "22/22/2019";
                for (int j = 1; j < data[i].length; j++) {
                    if (data[i][j] != null && data[i][j].getClass() == Double.class) {
                        data[i][j] = String.valueOf(data[i][j]);
                    }
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
                columnNames = new String[fields.length];
                data = new String[dataset.length][fields.length];
                for (int i = 0; i < fields.length; i++) {
                    columnNames[i] = fields[i].getName();

                }
                for (int j = 0; j < dataset.length; j++) {
                    try {
                        for (int k = 0; k < columnNames.length; k++) {

                            if (k == 0) {
                                data[j][k] = "22/22/2019";
                            } else {

                                data[j][k] = BeanUtils.getProperty(dataset[j],
                                        columnNames[k]);
                            }
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

    private int getId(String nomEpargnant, String prenomEpargnant, String typeEpargnant) {
        connect = Connect.ConnectDb2();
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

    public EpargneContext_rel(String nomEpargnant, String prenomEpargnant, String typeEpargnant) {
        this.nomEpargnant = nomEpargnant;
        this.prenomEpargnant = prenomEpargnant;
        this.typeEpargnant = typeEpargnant;

        System.out.println("Nom" + this.nomEpargnant);
        System.out.println("Prénom" + this.prenomEpargnant);
        System.out.println("Type" + this.typeEpargnant);
        this.IdEpargnant = getId(this.nomEpargnant, this.prenomEpargnant, this.typeEpargnant);
        setTitle("Historique des mouvements Epargne de " + this.nomEpargnant + " " + this.prenomEpargnant);
        initComponents();
    }

    public int findyearindex(String year) {
        for (int i = 2; i < jComboBox2.getItemCount(); i++) {
            if (((String) jComboBox2.getItemAt(i)).equalsIgnoreCase(year)) {
                return i;
            }
        }

        return -1;
    }

    public void actionPerformed(ActionEvent event) {
        boolean success = true;
        JMenuItem menu = (JMenuItem) event.getSource();
        if (menu == menuItemExeption) {

            if (((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 4))).equalsIgnoreCase("Frais de tenue de compte")) {
                // Ajout de l'exception dans la Base de données 
                connect = Connect.ConnectDb();
                PreparedStatement pst = null;
                String sql = "INSERT INTO Exceptionftc(idExceptionftc, IdEpargnant, TypeEpargnant, Dateftc) VALUES(?,?,?,?)";
                try {
                    pst = connect.prepareStatement(sql);
                    pst.setString(1, null);
                    pst.setInt(2, this.IdEpargnant);
                    pst.setString(3, this.typeEpargnant);
                    pst.setTimestamp(4, (java.sql.Timestamp) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                    int rowsaffected = pst.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    success = false;
                }

                if (success = true) {
                    JOptionPane.showMessageDialog(this, "Exception ajoutée avec succès");
                    this.refresh();
                }
                try {
                    //closing
                    connect.close();
                    pst.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EpargneContext_rel.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Ceci n'est pas un frais de tenue de compte");
            }

        }
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
                    "Date ", "Débit", "Crédit", "Solde", "Libellé", "ID"
                }
        ) {

            Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, String.class,
                String.class};

            @Override
    //  public Class getColumnClass(int columnIndex) {
            //     return this.types[columnIndex];
            //  }

            public Class getColumnClass(int column) {
                for (int row = 0; row < getRowCount(); row++) {
                    Object o = getValueAt(row, column);

                    if (o != null) {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        }
        );

//        Object[][] out = to2DimArray(data);
//        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        jTable1.setFillsViewportHeight(true);
//        jTable1.setAutoCreateRowSorter(true);
//        jTable1.setPreferredScrollableViewportSize(new Dimension(1000, 70));
// //       jTable1.getModel().notifyAll();
//        jTable1.setModel(new javax.swing.table.DefaultTableModel(out,
////                // new Object [][] {
////                //    {"20/10/2011", "500000000", "Dépôt initial", "1"},
////                //    {null, null, null, null},
////                //  {null, null, null, null},
////                //      {null, null, null, null},
////                //     {null, null, null, null},n
////                //   {null, null, null, null},
////                // {null, null, null, null},
////                //      {null, null, null, null},
////                //      {null, null, null, null},
////                //      {null, null, null, null},
////                //      {null, null, null, null},
////                //     {null, null, null, null},
////                //   {null, null, null, null},
////                //     {null, null, null, null},
////                //      {null, null, null, null},
////                //    {null, null, null, null},
////                //    {null, null, null, null},
////                //   {null, null, null, null},
////                //    {null, null, null, null},
////                //     {null, null, null, null}
////                // },
//               new String[]{
//                    "Date ", "Montant(en Frcs CFA)", "Motif", "ID"
//               }
//                
//                
//       ){   
//            
//           Class[] types = {java.sql.Timestamp.class, BigDecimal.class, String.class,
//                    String.class};
//            @Override
//                public Class getColumnClass(int columnIndex) {
//                    return this.types[columnIndex];
//                }
//       });
//        
//        
//        jTable1.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
//        jTable1.getColumnModel().getColumn(3).setMinWidth(0);
//        jTable1.getColumnModel().getColumn(3).setMaxWidth(0);
//        jTable1.getColumnModel().getColumn(1).setPreferredWidth(215);
//        jTable1.getColumnModel().getColumn(0).setPreferredWidth(180);
//        jTable1.getColumnModel().getColumn(2).setPreferredWidth(238);
//        jTable1.getColumn("Date ").setCellRenderer(new YMDRenderer()); 
//        jTable1.setDefaultRenderer(BigDecimal.class, new ThousandSepRenderer());
//        jTable1.getColumn("Montant(en Frcs CFA)").setCellRenderer(new ThousandSepRenderer());
//        sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) jTable1.getModel());
//        jTable1.setRowSorter(sorter);
//        StripedTableCellRenderer.installInTable(jTable1, Color.lightGray,
//        Color.white, null, null);
        jTable1.setDefaultRenderer(Double.class, new DecimalFormatRenderer());
        jTable1.getColumnModel().getColumn(5).setMinWidth(0);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(190);
        sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) jTable1.getModel());
        addListenerToSorter(sorter, jLabel4);
        jTable1.setRowSorter(sorter);
        jTable1.getColumn("Date ").setCellRenderer(new YMDRenderer());

        final EpargneContext_rel copie = this;
        jTable1.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                NumberFormat n = NumberFormat.getCurrencyInstance(new Locale("fr", "TG"));
                double doublePayment = copie.getSum().doubleValue();
                String s = n.format(doublePayment);
                jLabel4.setText(s);
                doublePayment = getSumDebit().doubleValue();
                s = n.format(doublePayment);
                jLabel6.setText(s);
                doublePayment = getSumCredit().doubleValue();
                s = n.format(doublePayment);
                jLabel8.setText(s);
//       NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
//       double doublePayment =copie.getSum().doubleValue();
//       String s = n.format(doublePayment);
//       System.out.println("changed"+s);
//       jLabel4.setText(s);
            }
        });

 //StripedTableCellRenderer.installInTable(jTable1, Color.lightGray,
        //       Color.white, null, null);
        StripedTableCellRenderer.installInTable(jTable1, new Color(244, 244, 244),
                Color.white, null, null);

//DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
//leftRenderer.setHorizontalAlignment(JLabel.LEFT);
//jTable1.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        jScrollPane1.setViewportView(jTable1);
        String itemselected = (String) jComboBox2.getSelectedItem();
        int size = jComboBox2.getItemCount();
        for (int i = 2; i < size; i++) {
            jComboBox2.removeItemAt(2);
        }

        // left alignement 
//DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
//leftRenderer.setHorizontalAlignment(JLabel.LEFT);
//jTable1.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        // update total 
        //  NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE); 
        NumberFormat n = NumberFormat.getCurrencyInstance(new Locale("fr", "TG"));
        double doublePayment = getSum().doubleValue();
        String s = n.format(doublePayment);
        jLabel4.setText(s);
        doublePayment = getSumDebit().doubleValue();
        s = n.format(doublePayment);
        jLabel6.setText(s);
        doublePayment = getSumCredit().doubleValue();
        s = n.format(doublePayment);
        jLabel8.setText(s);
//          doublePayment = getSumDebit().doubleValue();
//        s = n.format(doublePayment);
//       jLabel6.setText(s);

       //Add listener to sorter
        addListenerToSorter(sorter, jLabel4);
        addListenerToSorter(sorter, jLabel6);
        addListenerToSorter(sorter, jLabel8);

//        ResultSet rst = null;
//        String sql = "SELECT YEAR(DateEpargne) FROM Epargne WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.typeEpargnant + "' AND MontantEpargne >=0 GROUP BY YEAR(DateEpargne)";
//        connect = Connect.ConnectDb();
//        Statement stmt = null;
//        try {
//            stmt = connect.createStatement();
//            rst = stmt.executeQuery(sql);
//            while (rst.next()) {
//                jComboBox2.addItem(rst.getString(1));
//            }
        addYearComboitem();
        if (!itemselected.equalsIgnoreCase("tous")) {
            int position = findyearindex(itemselected);
            if (position != -1) {
                jComboBox2.setSelectedIndex(position);
            } else {
                jComboBox2.setSelectedIndex(0);
            }

        }

//        } catch (SQLException ex) {
//            Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
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
        jTable1 = new javax.swing.JTable() {
            public boolean getScrollableTracksViewportWidth(){
                return getPreferredSize().width < getParent().getWidth();
            }
        };
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
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    jButton4 = new javax.swing.JButton();
    jButton5 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jTable1.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);
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
        Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex); // à revoir
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
            "Date ", "Débit", "Crédit", "Solde", "Libellé", "ID"
        }

    ){

        Class[] types = {java.sql.Timestamp.class, Double.class, Double.class, Double.class, String.class,
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
    jTable1.getColumnModel().getColumn(5).setMinWidth(0);
    jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(130);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(130);
    jTable1.getColumnModel().getColumn(3).setPreferredWidth(130);
    jTable1.getColumnModel().getColumn(4).setPreferredWidth(190);
    sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
    addListenerToSorter(sorter, jLabel4);
    addListenerToSorter(sorter, jLabel6);
    addListenerToSorter(sorter, jLabel8);
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
    final EpargneContext_rel copie= this;
    jTable1.getModel().addTableModelListener(new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {

            NumberFormat n = NumberFormat.getCurrencyInstance(new Locale( "fr", "TG"));
            double doublePayment =copie.getSum().doubleValue();
            String s = n.format(doublePayment);
            jLabel4.setText(s);
            doublePayment =copie.getSumDebit().doubleValue();
            s = n.format(doublePayment);
            jLabel6.setText(s);
            doublePayment =copie.getSumCredit().doubleValue();
            s = n.format(doublePayment);
            jLabel8.setText(s);
            // NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            // double doublePayment =copie.getSum().doubleValue();
            //  String s = n.format(doublePayment);
            // System.out.println("changed"+s);
            // jLabel4.setText(s);
        }
    });
    // initialize jComboBox
    addYearComboitem();

    //StripedTableCellRenderer.installInTable(jTable1, Color.lightGray,
        //       Color.white, null, null);
    StripedTableCellRenderer.installInTable(jTable1, new Color(244,244,244),
        Color.white, null, null);

    popupMenu = new JPopupMenu();
    menuItemExeption = new JMenuItem("Ajouter une exception");
    menuItemExeption.addActionListener(this);
    popupMenu.add(menuItemExeption);
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

    // JFrae
    //BufferedImage bf = ImageIO.read(new File(getClass().getResource("/nehemie_mutuelle/pexels-photo4.jpg").getPath()));
    //BufferedImage bf = ImageIO.read(main.class.getClass().getResourceAsStream("/nehemie_mutuelle/pexels-photo4.jpg"));
    //this.setContentPane(new backImage(bf));

    //jTable1.setAutoCreateRowSorter(true);
    jTable1.setComponentPopupMenu(popupMenu);

    //DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
    //leftRenderer.setHorizontalAlignment(JLabel.LEFT);
    //jTable1.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
    jScrollPane1.setViewportView(jTable1);
    //jTable1.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

        //    public void columnAdded(TableColumnModelEvent e) {
            //    jTable1.columnAdded(e);
            //}

        //    public void columnRemoved(TableColumnModelEvent e){
            //    jTable1.columnRemoved(e);
            //}

        //    public void columnMoved(TableColumnModelEvent e){
            //    jTable1.columnMoved(e);
            //    }
        //    public void columnSelectionChanged(ListSelectionEvent e) {
            //     jTable1.columnSelectionChanged(e);
            //    }

        //    public void columnMarginChanged(ChangeEvent e) {
            //        Dimension tableSize=jTable1.getSize();
            //      jTable1.getColumnModel().getColumn(1).setPreferredWidth(Math.round(tableSize.width*0.18f));
            //jTable1.getColumnModel().getColumn(0).setPreferredWidth(Math.round(tableSize.width*0.18f));
            //jTable1.getColumnModel().getColumn(2).setPreferredWidth(Math.round(tableSize.width*0.18f));
            //jTable1.getColumnModel().getColumn(3).setPreferredWidth(Math.round(tableSize.width*0.18f));
            //jTable1.getColumn("Date ").setPreferredWidth(Math.round(tableSize.width*0.26f));

            //    }
        //});

jButton1.setText("Nouveau >>");
final JPopupMenu menu = new JPopupMenu();
JMenuItem item1 = new JMenuItem("Dépôt à vue");
JMenuItem item2 = new JMenuItem("Dépôt initial");
JMenuItem item2_0 = new JMenuItem("Dépôt: Autre");
JMenuItem item3 = new JMenuItem("Retrait");

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
                        NewEpargne epargne= new NewEpargne("autre", nomEpargnant,prenomEpargnant,typeEpargnant, false);
                        epargne.setLocationRelativeTo(null);
                        epargne.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        epargne.setVisible(true);
                        epargne.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent we) {
                                refresh();
                            }});

                        }});

                        item2_0.addActionListener(new ActionListener() {
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
                                menu.add(item2_0);
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

                                //   ResultSet rst=null;
                                //String sql="SELECT YEAR(DateEpargne) FROM Epargne WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"' GROUP BY YEAR(DateEpargne)";
                                //  //  String sql="SELECT YEAR(DateEpargne) FROM Epargne GROUP BY YEAR(DateEpargne)";
                                //   connect=Connect.ConnectDb();
                                //   Statement stmt = null;
                                //       try {
                                    //           stmt= connect.createStatement();
                                    //           rst=stmt.executeQuery(sql);
                                    //           while(rst.next()) {
                                        //               jComboBox2.addItem(rst.getString(1));
                                        //          }

                                    //           } catch (SQLException ex) {
                                    //                 Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
                                    //       }
                                jComboBox2.addItemListener(new java.awt.event.ItemListener() {
                                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                                        jComboBox2ItemStateChanged(evt);
                                    }
                                });

                                jLabel1.setText("Motif");

                                jLabel2.setText("Année");

                                jLabel3.setText("Solde:");

                                jLabel4.setText("jLabel4");
                                //NumberFormat n = NumberFormat.getCurrencyInstance(Locale.FRANCE);
                                //double doublePayment =this.getSum().doubleValue();
                                //String s = n.format(doublePayment);
                                //jLabel4.setText(s);
                                Locale specifiedLocale = new Locale( "fr", "TG");
                                NumberFormat n = NumberFormat.getCurrencyInstance(specifiedLocale);
                                double doublePayment =this.getSum().doubleValue();
                                String s2 = n.format(doublePayment);
                                jLabel4.setText(s2);

                                jLabel5.setText("Débit:");

                                jLabel6.setText("jLabel6");
                                doublePayment =this.getSumDebit().doubleValue();
                                s2 = n.format(doublePayment);
                                jLabel6.setText(s2);

                                jLabel7.setText("Crédit:");

                                jLabel8.setText("jLabel8");
                                doublePayment =this.getSumCredit().doubleValue();
                                s2 = n.format(doublePayment);
                                jLabel8.setText(s2);

                                jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/pdf.png"))); // NOI18N
                                jButton4.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jButton4ActionPerformed(evt);
                                    }
                                });

                                jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/excel.png"))); // NOI18N
                                jButton5.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jButton5ActionPerformed(evt);
                                    }
                                });

                                org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
                                getContentPane().setLayout(layout);
                                layout.setHorizontalGroup(
                                    layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(jLabel2))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                                            .add(layout.createSequentialGroup()
                                                .add(jButton1)
                                                .add(18, 18, 18)
                                                .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(18, 18, 18)
                                                .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(87, 87, 87)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(jLabel3)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5))
                                                    .add(jLabel7))
                                                .add(48, 48, 48)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(layout.createSequentialGroup()
                                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(jLabel8)
                                                            .add(jLabel6))
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                        .add(jButton5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(22, 22, 22))
                                                    .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addContainerGap())
                                );
                                layout.setVerticalGroup(
                                    layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jLabel1)
                                            .add(jLabel2))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 299, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 24, Short.MAX_VALUE)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                                            .add(jLabel4))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                .add(jButton1)
                                                .add(jButton2)
                                                .add(jButton3))
                                            .add(layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(jLabel6)
                                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                    .add(jLabel8)
                                                    .add(jLabel7)))
                                            .add(jButton5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jButton4))
                                        .addContainerGap())
                                );

                                pack();
                            }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (!jTable1.getSelectionModel().isSelectionEmpty()) {
            System.out.println("valeur IdEpargne " + jTable1.getValueAt(jTable1.getSelectedRow(), 3));
            Date date = new Date();
//            try {
            // date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
            date = (java.sql.Timestamp) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
//            } catch (ParseException ex) {
//                Logger.getLogger(EpargneContext.class.getName()).log(Level.SEVERE, null, ex);
//            }
            boolean pos = true;
            String montant = "";
            if (jTable1.getValueAt(jTable1.getSelectedRow(), 1) != null) {
                pos = false;
                //montant=jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
                montant = BigDecimal.valueOf((double) jTable1.getValueAt(jTable1.getSelectedRow(), 1)).toPlainString();
            } else {
                pos = true;
                //montant=jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
                montant = BigDecimal.valueOf((double) jTable1.getValueAt(jTable1.getSelectedRow(), 2)).toPlainString();
            }
            ModifyMouvement epargne = new ModifyMouvement(Integer.parseInt((String) jTable1.getValueAt(jTable1.getSelectedRow(), 5)), date, montant, (String) jTable1.getValueAt(jTable1.getSelectedRow(), 4), pos);
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
            if (first_item1 == false && first_item2 == true && filters.size() == 2) {
                for (int i = 0; i < filters.size(); i++) {
                    filters.remove(i);
                }
                System.out.println("Made");
            }

            // if (filters.isEmpty()) {
            if (filters.size() < 2) {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
                filters.add(rf);
                sorter.setRowFilter(RowFilter.andFilter(filters));
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
                filters.set(1, rf);
                // apply filters 
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
        } else {
            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
            // sorter.setRowFilter(rf);
            if (first_item1 == false && first_item2 == true && filters.size() == 2) {
                for (int i = 0; i < filters.size(); i++) {
                    filters.remove(i);
                }
            }
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
        if (first_item2 == false) {
            first_item2 = true;
        }
        System.out.println("Filter size after" + filters.size());
        wipe = false;
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        System.out.println("Changed in rel");
      //  if (evt.getStateChange() == evt.SELECTED) {

        if (wipe != true) {
            if (jComboBox1.getSelectedIndex() != 0) {

                // if (filters.size() < 2) {
                if (filters.isEmpty()) {
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox1.getSelectedItem().toString(), 4);
                    filters.add(rf);
                    sorter.setRowFilter(RowFilter.andFilter(filters));
                } else {
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox1.getSelectedItem().toString(), 4);
                    filters.set(0, rf);
                    // apply filters 
                    sorter.setRowFilter(RowFilter.andFilter(filters));
                }

                // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(jComboBox2.getSelectedItem().toString(), 0);
            } else {
            // RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 0);
                // sorter.setRowFilter(rf);
                if (filters.isEmpty()) {
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 4);
                    filters.add(rf);
                    sorter.setRowFilter(RowFilter.andFilter(filters));
                } else {
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("", 4);
                    filters.set(0, rf);
                    // apply filters 
                    sorter.setRowFilter(RowFilter.andFilter(filters));
                }
            }
   //     }

            if (first_item1 == false) {
                first_item1 = true;
                System.out.println("done firstite");
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (!jTable1.getSelectionModel().isSelectionEmpty()) {
            connect = Connect.ConnectDb();
            boolean success = true;
            PreparedStatement pst = null;
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                System.out.println("ID value" + jTable1.getValueAt(i, 5));
            }
            String sql = "delete from Epargne where idEpargne='" + jTable1.getValueAt(jTable1.getSelectedRow(), 5) + "'";

            try {
                pst = connect.prepareStatement(sql);
            } catch (SQLException ex) {
                success = false;
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                pst.execute();
            } catch (SQLException ex) {
                success = false;
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (success) {
                JOptionPane.showMessageDialog(null, "Suppression effectuée avec succès");
                this.refresh();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        generateReport();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        generateReportExcel();
    }//GEN-LAST:event_jButton5ActionPerformed
    public DynamicReport buildReport(DynamicTableModel model) throws Exception {

        /**
         * Creates the DynamicReportBuilder and sets the basic options for the
         * report
         */
        FastReportBuilder drb = new FastReportBuilder();
        Style columDetail = new Style();
//columDetail.setBorder(Border.THIN);
        Style columDetailWhite = new Style();
//columDetailWhite.setBorder(Border.THIN);
        columDetailWhite.setBackgroundColor(Color.WHITE);
        Style columDetailWhiteBold = new Style();
//columDetailWhiteBold.setBorder(Border.THIN);
        columDetailWhiteBold.setBackgroundColor(Color.WHITE);
        Style titleStyle = new Style();
        titleStyle.setFont(new Font(18, Font._FONT_VERDANA, true));
        Style numberStyle = new Style();
        numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        Style amountStyle = new Style();
        amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        amountStyle.setBackgroundColor(Color.cyan);
        amountStyle.setTransparency(Transparency.OPAQUE);
        Style oddRowStyle = new Style();
//oddRowStyle.setBorder(Border.NO_BORDER);
        Color veryLightGrey = new Color(230, 230, 230);
        oddRowStyle.setBackgroundColor(veryLightGrey);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

// table name column
        String[] headings = model.getColumnNames();
        for (int i = 0; i < headings.length; i++) {
            String key = headings[i];
            AbstractColumn column = ColumnBuilder.getInstance().setColumnProperty(key, String.class.getName())
                    .setTitle(key).setWidth(new Integer(100))
                    .setStyle(columDetailWhite).build();
            drb.addColumn(column);

        }
        drb.setTitle("Sample Report")
                .setTitleStyle(titleStyle).setTitleHeight(new Integer(30))
                .setSubtitleHeight(new Integer(20))
                .setDetailHeight(new Integer(15))
                //.setLeftMargin(margin)
                //.setRightMargin(margin)
                //.setTopMargin(margin)
                // .setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setColumnsPerPage(new Integer(1))
                .setUseFullPageWidth(true)
                .setColumnSpace(new Integer(5));
        DynamicReport dr = drb.build();

        return dr;
    }

    public Object[][] getTableData(JTable table) {
        TableModel dtm = table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                tableData[i][j] = dtm.getValueAt(i, j);
            }
        }
        return tableData;
    }

    public void generateReport() {
        try {
            Object[][] test = getTableData(jTable1);
            int nbcolumns = jTable1.getColumnCount();
            String[] columnName = new String[nbcolumns];
            for (int j = 0; j < nbcolumns; j++) {
                columnName[j] = jTable1.getColumnName(j);
            }
            DynamicTableModel md = new DynamicTableModel(columnName, test);

            DynamicReport dr = buildReport(md);
            JRDataSource ds = new JRTableModelDataSource(md);
            System.out.println("ms" + ds);
            JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
            if (jp == null) {
                System.out.println("jp null");
            }
            JasperViewer.viewReport(jp);
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);

            final OutputStream os;
            os = Files.newOutputStream(Paths.get("./test.pdf"));
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void generateReportExcel() {

        JTableToExcelExporter excel = new JTableToExcelExporter();
        excel.exportFromTable(jTable1, nomEpargnant+" "+prenomEpargnant+ ".xls");
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
                EpargneContext_rel context = new EpargneContext_rel("dfdf", "edede", "Pers Morale");

                context.pack();
                context.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
