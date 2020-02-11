 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
import static nehemie_mutuelle.Epargneview2.to2DimArray;
import static nehemie_mutuelle.TontineUser.conn;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author elommarcarnold
 */
public class TontineSynthese extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pre = null;
    private Date startDate = null;
    private Date endDate = null;
    private PreparedStatement pre1 = null;
    private PreparedStatement pre2 = null;
    private Vector<Vector> data;
    private int originmonth; // month to block at origin month
    static int totalrow;
    private Vector<Vector<Object>> data2;
    static int TOTAL_COLUMN = 1;
    private static final int MAX = 100;
    private static final int MIN = 0;
    ResultSet rs;
    int minyear;
    int maxyear;
    private int[] visitedmth;
    Date today;
    private double sumtontineplg =0;
    public static final Color LightYellow = new Color(255,255,153);

    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form TontineSynthese
     */
    public TontineSynthese() {
        visitedmth = new int[12];
        for (int i = 0; i < 12; i++) {
            visitedmth[i] = 0;
        }
        today = new Date();
        initComponents();
    }

    public String getNamefromId(int id, String type) throws SQLException {
        if (type.equalsIgnoreCase("Enfant")) {
            String nom = "";
            PreparedStatement pre;
            pre = conn.prepareStatement("SELECT Nom, Prenoms FROM Profil_enfant WHERE idProfil_enfant='" + id + "';");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                nom = rs.getString("Nom") + ", " + rs.getString("Prenoms");
            }
            return nom;
        } else if ((type.equalsIgnoreCase("Adulte"))) {
            String nom = "";
            PreparedStatement pre;
            pre = conn.prepareStatement("SELECT Noms, Prenoms FROM Profil_adulte WHERE idProfil_adulte='" + id + "';");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                nom = rs.getString("Noms") + ", " + rs.getString("Prenoms");
            }
            return nom;

        } else {
            String nom = "";
            PreparedStatement pre;
            pre = conn.prepareStatement("SELECT Raison_sociale FROM Profil_persmorale WHERE idProfil_persmorale='" + id + "';");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                nom = rs.getString("Raison_sociale");
            }
            return nom;

        }

    }

    class IndicatorCellRenderer extends JProgressBar implements TableCellRenderer {

        private Hashtable limitColors;

        private int[] limitValues;

        public IndicatorCellRenderer() {
            super(JProgressBar.HORIZONTAL);
            setBorderPainted(false);
        }

        public IndicatorCellRenderer(int min, int max) {
            super(JProgressBar.HORIZONTAL, min, max);
            setBorderPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            double n = 0;
            System.out.println("value" + value);
            if (!(value instanceof Number)) {
                String str;
                if (value instanceof String) {
                    str = (String) value;
                } else {
                    str = value.toString();
                }
                try {
                    n = Double.valueOf(str).doubleValue();
                } catch (NumberFormatException ex) {
                }
            } else {
                n = ((Number) value).intValue();
            }
            Color color = getColor(n);
            if (color != null) {
                setForeground(color);
            }
            setValue((new Double(n)).intValue());
            DecimalFormat df = new DecimalFormat();
            df.setMaximumIntegerDigits(2);
            setString(df.format(n));
            return this;
        }

        public void setLimits(Hashtable limitColors) {
            this.limitColors = limitColors;
            int i = 0;
            int n = limitColors.size();
            limitValues = new int[n];
            Enumeration e = limitColors.keys();
            while (e.hasMoreElements()) {
                limitValues[i++] = ((Integer) e.nextElement()).intValue();
            }
            sort(limitValues);
        }

        private Color getColor(double value) {
            Color color = null;
            if (limitValues != null) {
                int i;
                for (i = 0; i < limitValues.length; i++) {
                    if (limitValues[i] < value) {
                        color = (Color) limitColors
                                .get(new Integer(limitValues[i]));
                    }
                }
            }
            return color;
        }

        private void sort(int[] a) {
            int n = a.length;
            for (int i = 0; i < n - 1; i++) {
                int k = i;
                for (int j = i + 1; j < n; j++) {
                    if (a[j] < a[k]) {
                        k = j;
                    }
                }
                int tmp = a[i];
                a[i] = a[k];
                a[k] = tmp;
            }
        }
    }

    public void fillmonthsummtable(int year, int month) throws SQLException, ParseException, Exception {
        JTable tbletfill = null;
        JScrollPane pnfill = null;
        switch (month) {
            case 1:
                tbletfill = jTable2;
                pnfill = jScrollPane2;
                break;
            case 2:
                tbletfill = jTable3;
                pnfill = jScrollPane3;
                break;
            case 3:
                tbletfill = jTable4;
                pnfill = jScrollPane4;
                break;
            case 4:
                tbletfill = jTable5;
                pnfill = jScrollPane5;
                break;
            case 5:
                tbletfill = jTable6;
                pnfill = jScrollPane6;
                break;
            case 6:
                tbletfill = jTable7;
                pnfill = jScrollPane7;
                break;
            case 7:
                tbletfill = jTable8;
                pnfill = jScrollPane8;
                break;
            case 8:
                tbletfill = jTable9;
                pnfill = jScrollPane9;
                break;
            case 9:
                tbletfill = jTable10;
                pnfill = jScrollPane10;
                break;
            case 10:
                tbletfill = jTable11;
                pnfill = jScrollPane11;
                break;
            case 11:
                tbletfill = jTable12;
                pnfill = jScrollPane12;
                break;
            case 12:
                tbletfill = jTable13;
                pnfill = jScrollPane13;
                break;
        }

        // Connexion à la base de données 
        conn = Connect.ConnectDb2();
        String datestr;
        DecimalFormat decformatter = new DecimalFormat("#00");
        datestr = String.valueOf(year) + "-" + decformatter.format(month) + "-" + "01";
        PreparedStatement prepr;
//    prepr = conn.prepareStatement("SET sql_mode=(SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY',''))");
//    prepr.executeQuery();
//     System.out.println("executed");
        System.out.println("datestr" + datestr);
//    String sql ="SELECT Nom, Prenoms, carn, IdEpargnant, TypeEpargnant, Mise, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1, mnt, max(carn) FROM (SELECT Nom, Prenoms, carn, globl.IdEpargnant as IdEpargnant, globl.TypeEpargnant as TypeEpargnant , Mise, JoursTontine, mnt FROM (SELECT Nom, Prenoms, IdEpargnant, TypeEpargnant, Mise, JoursTontine, li.carnet as carn FROM ((SELECT Mise, IdEpargnant, TypeEpargnant, JoursTontine, DateTontine FROM Tontine WHERE DateTontine='"+datestr+"') tontn  RIGHT JOIN ((SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%') UNION (SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%') UNION (SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%')) li on tontn.IdEpargnant=li.id AND tontn.TypeEpargnant=li.Typo)) globl LEFT JOIN (SELECT IdEpargnant, TypeEpargnant,  SUM(Montant) as mnt FROM retraits_tontine WHERE month(DateRet)=month('"+datestr+"') and year(DateRet)=year('"+datestr+"') GROUP BY IdEpargnant, TypeEpargnant) retr ON globl.IdEpargnant= retr.IdEpargnant AND globl.TypeEpargnant= retr.TypeEpargnant UNION \n" +
//"SELECT Nom, Prenoms, li.carnet, li.id as IdEpargnant, li.Typo as TypeEpargnant, '0' as Mise, '0' as JoursTontine, mnt FROM ((SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%') UNION (SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%') UNION (SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%'))  li RIGHT JOIN (SELECT IdEpargnant, TypeEpargnant,  SUM(Montant) as mnt FROM retraits_tontine WHERE month(DateRet)=month('"+datestr+"') and year(DateRet)=year('"+datestr+"') AND IdEpargnant  GROUP BY IdEpargnant, TypeEpargnant) retr ON li.id= retr.IdEpargnant AND li.Typo  = retr.TypeEpargnant WHERE li.Date_adhesion_to < LAST_DAY('"+datestr+"')) temp group by carn;";
//     System.out.println("ce qui est ex "+sql);

//        String sql = "SELECT * FROM (SELECT * FROM ("
//                + "(SELECT * FROM (SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%' UNION SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%' UNION SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%')test WHERE Date_adhesion_to < LAST_DAY('" + datestr + "')) globl  LEFT JOIN"
//                + "(SELECT IdEpargnant, TypeEpargnant, Mise, JoursTontine, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE DateTontine='" + datestr + "') tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant)) temp0 LEFT JOIN"
//                + "(SELECT IdEpargnant, TypeEpargnant,  coalesce(SUM(Montant),0) as mnt FROM retraits_tontine WHERE month(DateRet)=month('" + datestr + "') and year(DateRet)=year('" + datestr + "') GROUP BY IdEpargnant, TypeEpargnant) retr ON temp0.id = retr.IdEpargnant AND temp0.Typo = retr.TypeEpargnant order by carnet";

// String sql = "SELECT * FROM (SELECT * FROM ("
//         + "(SELECT * FROM (SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo, 'non added' as cat From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%' UNION "
//         +         "SELECT tp1.idProfil_enfant as id, tp0.numcarnettont as carnet, tp1.Nom as Nom, tp1.Prenoms as Prenoms, tp1.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo, 'added' as cat FROM tontinesupp tp0 JOIN Profil_enfant tp1 ON tp0.iduser= tp1.idProfil_enfant AND tp0.typeuser = 'Enfant'"
//         +         "UNION SELECT UNION SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo, 'non added' as cat  From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%' UNION "
//         +         "SELECT tp3.idProfil_adulte as id, tp2.numcarnettont as carnet, tp3.Noms as Nom, tp3.Prenoms as Prenoms, tp3.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo, 'added' as cat FROM tontinesupp tp2 JOIN Profil_adulte tp3 ON tp2.iduser= tp3.idProfil_adulte AND tp0.typeuser = 'Adulte'"
//         + "SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo, 'non added' as cat From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%' UNION "
//         + "SELECT tp5.idProfil_persmorale as id, tp4.numcarnettont as carnet, tp4.Raison_sociale as Nom, tp4.Raison_sociale as Prenoms, tp4.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo, 'added' as cat FROM tontinesupp tp4 JOIN Profil_persmorale tp5 ON tp4.iduser= tp5.idProfil_persmorale AND tp0.typeuser = 'Pers Morale'"
//
//         + ")"
//         + ""
//         + "test WHERE Date_adhesion_to < LAST_DAY('" + datestr + "')) globl  LEFT JOIN"
//                + "(SELECT IdEpargnant, TypeEpargnant, Mise, JoursTontine, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE DateTontine='" + datestr + "') tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant AND globl.cat ='non added')) temp0 LEFT JOIN"
//                + "(SELECT IdEpargnant, TypeEpargnant,  coalesce(SUM(Montant),0) as mnt FROM retraits_tontine WHERE month(DateRet)=month('" + datestr + "') and year(DateRet)=year('" + datestr + "') GROUP BY IdEpargnant, TypeEpargnant) retr ON temp0.id = retr.IdEpargnant AND temp0.Typo = retr.TypeEpargnant AND globl.cat ='non added'"
//         + ""
//         + "LEFT JOIN"
//                + "((SELECT IdEpargnant, TypeEpargnant, Mise, JoursTontine, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE DateTontine='" + datestr + "' AND idTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = globl.carnet and type ='ajout')) tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant AND globl.cat ='added') temp0 LEFT JOIN"   // changed)  ) tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant AND globl.cat ='non added')) temp0 LEFT JOIN"
//                + "((SELECT IdEpargnant, TypeEpargnant, Mise, JoursTontine, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE DateTontine='" + datestr + "' AND idTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = globl.carnet and type ='retrait')) tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant AND globl.cat ='added')"   // changed)  ) tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant AND globl.cat ='non added')) temp0 LEFT JOIN"
//
//         + " order by carnet";

        
//String sql = "SELECT distinct globl.*, tontn.Mise as tontnonadd, tontn.nbcot as nbcotnonadded, tontn2.Mise as miseadded, tontn2.nbcot as nbcotadded, retr.mnt as retrnonadded, retr2.mnt as retradded  FROM "
//        + " (SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo, 'non added' as cat From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%' UNION SELECT tp1.idProfil_enfant as id, tp0.numcarnettont as carnet, tp1.Nom as Nom, tp1.Prenoms as Prenoms, tp1.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo, 'added' as cat FROM tontinesupp tp0 JOIN Profil_enfant tp1 ON tp0.iduser = tp1.idProfil_enfant AND tp0.typeuser = 'Enfant'"
//        + " UNION SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo, 'non added' as cat  From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%' UNION SELECT tp3.idProfil_adulte as id, tp2.numcarnettont as carnet, tp3.Noms as Nom, tp3.Prenoms as Prenoms, tp3.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo, 'added' as cat FROM tontinesupp tp2 JOIN Profil_adulte tp3 ON tp2.iduser= tp3.idProfil_adulte AND tp2.typeuser = 'Adulte' "
//        + " UNION SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo, 'non added' as cat From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%' UNION SELECT tp5.idProfil_persmorale as id, tp4.numcarnettont as carnet, tp5.Raison_sociale as Nom, tp5.Raison_sociale as Prenoms, tp5.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo, 'added' as cat FROM tontinesupp tp4 JOIN Profil_persmorale tp5 ON tp4.iduser= tp5.idProfil_persmorale AND tp4.typeuser = 'Pers Morale' WHERE Date_adhesion_to < LAST_DAY('"+datestr+"')) globl  LEFT JOIN" 
//        + " (SELECT IdEpargnant, TypeEpargnant, Mise, JoursTontine, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE DateTontine='"+datestr+"') tontn ON globl.id = tontn.IdEpargnant AND globl.Typo = tontn.TypeEpargnant AND globl.cat ='non added' LEFT JOIN"
//        + " (SELECT IdEpargnant, TypeEpargnant, Mise, JoursTontine, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE DateTontine='"+datestr+"') tontn2 ON globl.id = tontn2.IdEpargnant AND globl.Typo = tontn2.TypeEpargnant AND globl.cat ='added'  LEFT JOIN" 
//        + " (SELECT IdEpargnant, TypeEpargnant,  coalesce(SUM(Montant),0) as mnt FROM retraits_tontine WHERE month(DateRet)=month('"+datestr+"') and year(DateRet)=year('"+datestr+"') AND idretraits_tontine NOT IN (SELECT idtontine FROM enrtontinesupp) GROUP BY IdEpargnant, TypeEpargnant) retr ON globl.id = retr.IdEpargnant AND globl.Typo = retr.TypeEpargnant AND globl.cat ='non added' LEFT JOIN"
//        + " (SELECT IdEpargnant, TypeEpargnant,  coalesce(SUM(Montant),0) as mnt FROM retraits_tontine WHERE month(DateRet)=month('"+datestr+"') and year(DateRet)=year('"+datestr+"') GROUP BY IdEpargnant, TypeEpargnant) retr2 ON globl.id = retr2.IdEpargnant AND globl.Typo = retr2.TypeEpargnant AND globl.cat ='added' ORDER BY carnet";        
//        
        
        
String sql = "SELECT distinct * FROM (" +
"SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo, 'non added' as cat From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%' UNION SELECT tp1.idProfil_enfant as id, tp0.numcarnettont as carnet, tp1.Nom as Nom, tp1.Prenoms as Prenoms, tp1.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo, 'added' as cat FROM tontinesupp tp0 JOIN Profil_enfant tp1 ON tp0.iduser = tp1.idProfil_enfant AND tp0.typeuser = 'Enfant'" +
"UNION SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo, 'non added' as cat  From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%' UNION SELECT tp3.idProfil_adulte as id, tp2.numcarnettont as carnet, tp3.Noms as Nom, tp3.Prenoms as Prenoms, tp3.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo, 'added' as cat FROM tontinesupp tp2 JOIN Profil_adulte tp3 ON tp2.iduser= tp3.idProfil_adulte AND tp2.typeuser = 'Adulte'" +
"UNION SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo, 'non added' as cat From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%' UNION SELECT tp5.idProfil_persmorale as id, tp4.numcarnettont as carnet, tp5.Raison_sociale as Nom, tp5.Raison_sociale as Prenoms, tp5.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo, 'added' as cat FROM tontinesupp tp4 JOIN Profil_persmorale tp5 ON tp4.iduser= tp5.idProfil_persmorale AND tp4.typeuser = 'Pers Morale' WHERE Date_adhesion_to < LAST_DAY('"+datestr+"') ORDER BY carnet) globl"  ;       
        prepr = conn.prepareStatement(sql);
        
        System.out.println("valeur de sql"+sql);

//       prepr = conn.prepareStatement("SELECT Nom, Prenoms, carn, IdEpargnant, TypeEpargnant, Mise, bit_count(JoursTontine), mnt, max(carn) FROM (SELECT Nom, Prenoms, carn, globl.IdEpargnant as IdEpargnant, globl.TypeEpargnant as TypeEpargnant , Mise, JoursTontine, mnt FROM (SELECT Nom, Prenoms, IdEpargnant, TypeEpargnant, Mise, JoursTontine, li.carnet as carn FROM ((SELECT Mise, IdEpargnant, TypeEpargnant, JoursTontine, DateTontine FROM Tontine WHERE DateTontine='"+datestr+"') tontn  LEFT JOIN ((SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%') UNION (SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%') UNION (SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%')) li on tontn.IdEpargnant=li.id AND tontn.TypeEpargnant=li.Typo)) globl LEFT JOIN (SELECT IdEpargnant, TypeEpargnant,  SUM(Montant) as mnt FROM retraits_tontine WHERE month(DateRet)=month('"+datestr+"') and year(DateRet)=year('"+datestr+"') GROUP BY IdEpargnant, TypeEpargnant) retr ON globl.IdEpargnant= retr.IdEpargnant AND globl.TypeEpargnant= retr.TypeEpargnant UNION \n" +
//"(SELECT Nom, Prenoms, li.carnet, li.id as IdEpargnant, li.Typo as TypeEpargnant, '0' as Mise, '0' as JoursTontine, mnt FROM ((SELECT a.idProfil_enfant as id, RIGHT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_to as Date_adhesion_to, 'Enfant' as Typo From Profil_enfant a WHERE a.Type_adhesion LIKE '%Tontine%') UNION (SELECT b.idProfil_adulte as id, RIGHT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_to as Date_adhesion_to, 'Adulte' as Typo From Profil_adulte b WHERE b.Type_adhesion LIKE '%Tontine%') UNION (SELECT c.idProfil_persmorale as id, RIGHT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_to as Date_adhesion_to, 'Pers Morale' as Typo From Profil_persmorale c WHERE c.Type_adhesion LIKE '%Tontine%'))  li RIGHT JOIN (SELECT IdEpargnant, TypeEpargnant,  SUM(Montant) as mnt FROM retraits_tontine WHERE month(DateRet)=month('"+datestr+"') and year(DateRet)=year('"+datestr+"') AND IdEpargnant  GROUP BY IdEpargnant, TypeEpargnant) retr ON li.id= retr.IdEpargnant AND li.Typo  = retr.TypeEpargnant) temp0  UNION \n"+
//""               + ""
//               + ""
//               + " temp group by carn;");
//    
        ResultSet rs = prepr.executeQuery();
        Vector<Vector> dataVector = new Vector<Vector>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String carnet = "";
        int iterator = 1;
        while (rs.next()) {
            Vector<Object> tont = new Vector<Object>();
            tont.add(iterator);   // Numero
            carnet = rs.getString(2);
            if (rs.getString(2)!= null) tont.add(rs.getString(2));
        
            else tont.add("");  // Numero carnet
            tont.add(rs.getString(3) + " " + rs.getString(4));  // Nom & Prénoms
            // System.out.println("rs.getstrinh"+rs.getString(4));
            Boolean added =false;
            if (rs.getString("cat").equalsIgnoreCase("added"))  added = true;
            
            double report = getReport(rs.getString(6), rs.getInt(1), formatter.parse(datestr), added, rs.getString(2)); // à programmer
            tont.add(report);  // Report 
            // mise 
            int mise;
            int nbcot; 
            double retraits=0;
            if (rs.getString(7).equalsIgnoreCase("added")) {
                
                
                // retraits 
                String sql4 = "SELECT SUM(Montant) FROM retraits_tontine WHERE idretraits_tontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet='"+carnet+"') AND month(DateRet)=month('" + datestr + "') and year(DateRet)=year('" + datestr +"') AND IdEpargnant='"+rs.getInt(1)+"' AND TypeEpargnant='"+rs.getString(6)+"'";
                conn = Connect.ConnectDb2();
                PreparedStatement prep2 = conn.prepareStatement(sql4);
                ResultSet rs4 = prep2.executeQuery();
                
                if (rs4.next()) retraits = rs4.getDouble(1);
                else retraits =0; 
                
                // cotisations
                
                String sql5 = "SELECT Mise, CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE IdTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet='"+carnet+"') AND month(DateTontine)=month('" + datestr + "') and year(DateTontine)=year('"+ datestr +"') AND IdEpargnant='"+rs.getInt(1)+"' AND TypeEpargnant='"+rs.getString(6)+"'";
          
                PreparedStatement prep3 = conn.prepareStatement (sql5);
                ResultSet rs5 = prep3.executeQuery();
                if (rs5.next()) {
                    mise =rs5.getInt(1);
                    nbcot = rs5.getInt(2);
                   
                } else {
                    mise =0;
                    nbcot=0;
                
                
                }
                
//                 if (rs.getObject(13) == null) retraits = 0;
//                else retraits = rs.getInt(13) ; 
                
            } else {   // non added 
                
                
                
                // retraits 
                String sql4 = "SELECT SUM(Montant) FROM retraits_tontine WHERE idretraits_tontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet='"+carnet+"') AND month(DateRet)=month('" + datestr + "') and year(DateRet)=year('" + datestr +"') AND IdEpargnant='"+rs.getInt(1)+"' AND TypeEpargnant='"+rs.getString(6)+"'";
                conn = Connect.ConnectDb2();
                PreparedStatement prep2 = conn.prepareStatement(sql4);
                ResultSet rs4 = prep2.executeQuery();
                
                if (rs4.next()) retraits = rs4.getDouble(1);
                else retraits =0; 
                
                // cotisations
                
             //   String sql5 = "SELECT Mise,  CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE IdTontine NOT IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet='"+carnet+"') AND month(DateTontine)=month('" + datestr + "') and year(DateTontine)=year('"+ datestr +"') AND IdEpargnant='"+rs.getInt(1)+"' AND TypeEpargnant='"+rs.getString(6)+"'";
                  String sql5 = "SELECT Mise,  CHAR_LENGTH(JoursTontine) - CHAR_LENGTH(REPLACE(JoursTontine, ',', '')) + 1 as nbcot FROM Tontine WHERE IdTontine NOT IN (SELECT idtontine FROM enrtontinesupp) AND month(DateTontine)=month('" + datestr + "') and year(DateTontine)=year('"+ datestr +"') AND IdEpargnant='"+rs.getInt(1)+"' AND TypeEpargnant='"+rs.getString(6)+"'";  // tempsolution to correct

                if(rs.getInt(1)==67 && rs.getString(6).equalsIgnoreCase("Adulte")) System.out.println("SQL555"+sql5);
                PreparedStatement prep3 = conn.prepareStatement (sql5);
                ResultSet rs5 = prep3.executeQuery();
                if (rs5.next()) {
                    mise =rs5.getInt(1);
                    nbcot = rs5.getInt(2);
                } else  {
                    mise =0;
                    nbcot=0;
                }
                
                
                
                
//                
//                 if (rs.getObject(8) == null) mise = 0;
//                 else mise = rs.getInt(8) ;
//                 tont.add(mise);
//                 if (rs.getObject(9) == null) nbcot = 0;
//                 else nbcot = rs.getInt(9) ; 
//                 
//                if (rs.getObject(12) == null) retraits = 0;
//                else retraits = rs.getInt(12) ; 
                        
         }     
            
        //    tont.add(rs.getInt(9));  // Mise
            tont.add(mise);
            
        // retenue
            System.out.println("mise "+mise);
            if(nbcot > 0) tont.add(mise);
            else tont.add(0);
     //       System.out.println("nbcot" + rs.getString("nbcot"));
            tont.add(nbcot);  // Cot/31
            //   System.out.println("cotisation/31"+rs.getInt(7));
            tont.add(new DecimalFormat("#.##").format((Double) (Double.valueOf(nbcot) / 31) * 100)); // Pourcentage
            int total;
            if (nbcot != 0) {
                total = (nbcot - 1) * mise;  // Total
            } else {
                total = 0;
            }
            tont.add(total);
            // retraits
            
            tont.add(retraits);  // Retraits
            tont.add(report + total - retraits);// solde
            tont.add(rs.getInt(1));
            tont.add(rs.getString(6));
            iterator++;
            dataVector.add(tont);
            
             conn.close();
        }
        
       
        
        Object[][] out = to2DimArray(dataVector);
        // refresh 

        tbletfill.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbletfill.setFillsViewportHeight(true);
        tbletfill.setPreferredScrollableViewportSize(new Dimension(1000, 70));
        tbletfill.setModel(new javax.swing.table.DefaultTableModel(out,
                new String[]{
                    "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        
//jTable6.getColumnModel().getColumn(0).setPreferredWidth(30);
//jTable6.getColumnModel().getColumn(1).setPreferredWidth(90);
//jTable6.getColumnModel().getColumn(2).setPreferredWidth(150);
//jTable6.getColumnModel().getColumn(3).setPreferredWidth(100);
//jTable6.getColumnModel().getColumn(4).setPreferredWidth(100);
//jTable6.getColumnModel().getColumn(5).setPreferredWidth(90);
//jTable6.getColumnModel().getColumn(6).setPreferredWidth(100);
//jTable6.getColumnModel().getColumn(7).setPreferredWidth(90);
//jTable6.getColumnModel().getColumn(8).setPreferredWidth(80);
//jTable6.getColumnModel().getColumn(9).setPreferredWidth(80);
//jTable6.getColumnModel().getColumn(10).setPreferredWidth(80);
//
//jTable6.getColumnModel().getColumn(11).setMinWidth(0);
//jTable6.getColumnModel().getColumn(11).setMaxWidth(0);
//jTable6.getColumnModel().getColumn(12).setMinWidth(0);
//jTable6.getColumnModel().getColumn(12).setMaxWidth(0);

        tbletfill.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbletfill.getColumnModel().getColumn(1).setPreferredWidth(70);
        tbletfill.getColumnModel().getColumn(2).setPreferredWidth(170);
        tbletfill.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbletfill.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbletfill.getColumnModel().getColumn(5).setPreferredWidth(90);
        tbletfill.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbletfill.getColumnModel().getColumn(7).setPreferredWidth(90);
        tbletfill.getColumnModel().getColumn(8).setPreferredWidth(80);
        tbletfill.getColumnModel().getColumn(9).setPreferredWidth(80);
        tbletfill.getColumnModel().getColumn(10).setPreferredWidth(80);
           
        tbletfill.getColumnModel().getColumn(12).setMinWidth(0);
        tbletfill.getColumnModel().getColumn(12).setMaxWidth(0);
        tbletfill.getColumnModel().getColumn(11).setMinWidth(0);
        tbletfill.getColumnModel().getColumn(11).setMaxWidth(0);
        
     
 
      
   
      

//pnfill=new FrozenTablePane(tbletfill, 3);
////jScrollPane2=new FrozenTablePane(jTable2, 3); 
        if (tbletfill != null) {
            ((FrozenTablePane) pnfill).updateFrozenModel(tbletfill, 3, pnfill.getViewport());

            if (conn != null) {
                conn.close();
            }

            if (pre != null) {
                pre.close();
            }

            if (rs != null) {
                rs.close();
            }

            visitedmth[month - 1] = year;   // update  visited month 

        }
    }

    public class FrozenTablePane extends JScrollPane {

        TableModel frozenModel;

        public void updateFrozenModel(JTable tab, int colsToFreeze, JViewport viewport) {

            frozenModel = new DefaultTableModel(
                    tab.getRowCount(),
                    colsToFreeze);
            //JTable frozenTable = (JTable) viewport.getView();

            TableModel model = tab.getModel();

            System.out.println("model rowcount" + model);
            String value = "";

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < colsToFreeze; j++) {
                    if (model.getValueAt(i, j) == null) {
                        value = "";
                    } else if (model.getValueAt(i, j).getClass() == Integer.class) {
                        value = Integer.toString((Integer) model.getValueAt(i, j));
                    } else {
                        value = (String) model.getValueAt(i, j);
                    }
                    frozenModel.setValueAt(value, i, j);
                }
            }

            //frozenTable.setModel(frozenModel);
            JTable frozenTable = new JTable(frozenModel);

            for (int j = 0; j < colsToFreeze; j++) {
                JTableHeader th = frozenTable.getTableHeader();
                TableColumnModel tcm = th.getColumnModel();
                TableColumn tc = tcm.getColumn(j);
                System.out.println(tab.getColumnName(j));
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

        public FrozenTablePane(JTable table, int colsToFreeze) {
            super(table);

            TableModel model = table.getModel();

            //create a frozen model
            TableModel frozenModel = new DefaultTableModel(
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
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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

    public Vector getSynthesebymonth(int month) throws Exception {
        conn = Connect.ConnectDb();
        pre1 = conn.prepareStatement("SELECT DateTontine, SUM(M)");
        return null;

    }

    public Double getReport(String typeEpargnant, int idEpargnant, Date begindate, Boolean added, String carnet) throws Exception {
        
        System.out.println("idepargnant"+idEpargnant+"typeEpargnant"+typeEpargnant+ "in getreport");
        if (added == false) {
        conn = Connect.ConnectDb2();
        SimpleDateFormat fmm = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = fmm.format(begindate);
        PreparedStatement pre21 = conn.prepareStatement("SET @SumMontant := 0;");
        pre21.executeQuery();
        pre = conn.prepareStatement("SELECT IdTontine as id, DateTontine AS w, (bit_count(JoursTontine)- 1)*Mise as cot, @SumMontant := @SumMontant+ coalesce(((bit_count(JoursTontine)- 1)*Mise),0) AS CumulativeSum FROM Tontine WHERE IdEpargnant='" + idEpargnant + "'AND TypeEpargnant='" + typeEpargnant + "' AND DateTontine <'" + datestr + "' ORDER BY DateTontine ASC;");

//        pre = conn.prepareStatement("SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d,\n" +
//"          Epargne.MontantEpargne AS a, (@SumMontant := @SumMontant + MontantEpargne) AS CumulativeSum\n" +
//"     FROM Epargne\n" +
//"     WHERE IdEpargnant='"+idEpargnant+"' AND TypeEpargnant='"+typeEpargnant+"' AND DateEpargne < '"+datestr+ "'\n" +
//"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne ORDER BY DateEpargne DESC;");
        ResultSet rs = pre.executeQuery();
        // SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

        Double dep = 0d;
        while (rs.next()) {
            dep = rs.getDouble(4);
            System.out.println("dep vle" + dep);
        }

        String sql = "SELECT IdTontine as id, DateTontine AS w, (bit_count(JoursTontine)- 1)*Mise as cot, @SumMontant := @SumMontant+ coalesce(((bit_count(JoursTontine)- 1)*Mise),0) AS CumulativeSum FROM Tontine WHERE IdEpargnant='" + idEpargnant + "'AND TypeEpargnant='" + typeEpargnant + "' AND DateTontine <'" + datestr + "' ORDER BY DateTontine ASC;";
        System.out.println("sql vautf ff " + sql);
        // Calcul des retraits 
        pre = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='" + idEpargnant + "'AND TypeEpargnant='" + typeEpargnant + "' AND DATE(DateRet) <'" + datestr + "'");
        rs = pre.executeQuery();

        Double retr = 0d;
        while (rs.next()) {
            retr = rs.getDouble(1);
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
        System.out.println("dep " + dep);
        System.out.println("retr " + retr);
        System.out.println("value " + (dep - retr));
        
        if (typeEpargnant.equalsIgnoreCase("adulte") && idEpargnant == 20) {
            
            System.out.println("dep florent" + dep);
            System.out.println("retr florent" + retr);
            System.out.println("value florent" + (dep - retr));
            
            
            
        }
        
        return dep - retr;
    } else {
            
            
             if (typeEpargnant.equalsIgnoreCase("adulte") && idEpargnant == 20) {
            
            System.out.println("dep florent2");
            System.out.println("retr florent2");
            System.out.println("value florent2");
            
            
            
              }
            
            
            
            
            
            
            
            
            
            
            System.out.println("in added"+ carnet);
        conn = Connect.ConnectDb2();
        SimpleDateFormat fmm = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = fmm.format(begindate);
        PreparedStatement pre21 = conn.prepareStatement("SET @SumMontant := 0;");
        pre21.executeQuery();
        pre = conn.prepareStatement("SELECT IdTontine as id, DateTontine AS w, (bit_count(JoursTontine)- 1)*Mise as cot, @SumMontant := @SumMontant+ coalesce(((bit_count(JoursTontine)- 1)*Mise),0) AS CumulativeSum FROM Tontine WHERE idTontine in (SELECT idtontine FROM enrtontinesupp WHERE numcarnet ='"+ carnet+"' AND type='ajout') AND IdEpargnant='" + idEpargnant + "'AND TypeEpargnant='" + typeEpargnant + "' AND DateTontine <'" + datestr + "' ORDER BY DateTontine ASC;");

//        pre = conn.prepareStatement("SELECT Epargne.IdEpargne as id, Epargne.DateEpargne AS w, Epargne.MotifEpargne AS d,\n" +
//"          Epargne.MontantEpargne AS a, (@SumMontant := @SumMontant + MontantEpargne) AS CumulativeSum\n" +
//"     FROM Epargne\n" +
//"     WHERE IdEpargnant='"+idEpargnant+"' AND TypeEpargnant='"+typeEpargnant+"' AND DateEpargne < '"+datestr+ "'\n" +
//"     GROUP BY Epargne.DateEpargne, Epargne.MotifEpargne, Epargne.MontantEpargne ORDER BY DateEpargne DESC;");
        ResultSet rs = pre.executeQuery();
        // SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();

        Double dep = 0d;
        while (rs.next()) {
            dep = rs.getDouble(4);
            System.out.println("dep vle" + dep);
        }

        String sql = "SELECT IdTontine as id, DateTontine AS w, (bit_count(JoursTontine)- 1)*Mise as cot, @SumMontant := @SumMontant+ coalesce(((bit_count(JoursTontine)- 1)*Mise),0) AS CumulativeSum FROM Tontine WHERE idTontine in (SELECT idtontine FROM enrtontinesupp WHERE numcarnet ='"+carnet+"' AND type='ajout') AND IdEpargnant='" + idEpargnant + "'AND TypeEpargnant='" + typeEpargnant + "' AND DateTontine <'" + datestr + "' ORDER BY DateTontine ASC;";
        System.out.println("sql vautf ff " + sql);
        // Calcul des retraits 
        pre = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE idretraits_tontine in (SELECT idtontine FROM enrtontinesupp WHERE numcarnet ='"+carnet+"' AND type='retrait') AND IdEpargnant='" + idEpargnant + "'AND TypeEpargnant='" + typeEpargnant + "' AND DATE(DateRet) <'" + datestr + "'");
        rs = pre.executeQuery();

        Double retr = 0d;
        while (rs.next()) {
            retr = rs.getDouble(1);
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
        System.out.println("dep " + dep);
        System.out.println("retr " + retr);
        System.out.println("value " + (dep - retr));
        return dep - retr;    
            
            
            
            
            
    }
    }      
    public Vector getSynthese() throws Exception {
        conn = Connect.ConnectDb();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        if (startDate != null) {
            beginCalendar.setTimeInMillis(startDate.getTime());
            beginCalendar.set(Calendar.DAY_OF_MONTH, 1);
            finishCalendar.setTimeInMillis(endDate.getTime());
            System.out.println("endDate" + endDate);
            Vector<Vector> TontineVector = new Vector<Vector>();
            ResultSet rs1 = null;
            ResultSet rs2 = null;
            Date date;
            // String date;
            while (beginCalendar.before(finishCalendar) || beginCalendar.equals(finishCalendar)) {
                // add one month to date per loop
                date = beginCalendar.getTime();
                java.sql.Date sqldate = new java.sql.Date(date.getTime());
                pre1 = conn.prepareStatement("SELECT DateTontine, SUM(Mise), SUM((bit_count(JoursTontine))*Mise) FROM Tontine  WHERE (bit_count(JoursTontine)) >=1 AND DateTontine='" + sqldate + "'");
                rs1 = pre1.executeQuery();
                pre2 = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE MONTH(DateRet)=MONTH('" + sqldate + "') AND YEAR(DateRet)=YEAR('" + sqldate + "')");
                rs2 = pre2.executeQuery();
                Vector<Object> tont = new Vector<Object>();
                if (rs1.next()) {
                    tont.add(date);
                    tont.add(((Number) rs1.getInt(2)).doubleValue());
                    tont.add(rs1.getDouble(3));
                    Double retr = 0.0;
                    if (rs2.next()) {
                        retr = rs2.getDouble(1);
                    }
                        tont.add(retr);
                    
                    tont.add(rs1.getDouble(3) - retr );
                    
                     // avant ajout 
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month == 11) {
              TontineVector.add(tont);
//             Vector<Object> tont2 = new Vector<Object>();
//             tont2.add(date);
//             tont2.add(0.0);
//             tont2.add(0.0);
//             tont2.add(0.0);
//             tont2.add(0.0);
//             TontineVector.add(tont2);
        }
                    TontineVector.add(tont);
                } else {
                    tont.add(date);
                    tont.add((double) 0);
                    tont.add((double) 0);
                    if (rs2.next()) {
                        tont.add(rs2.getDouble(1));  // Avréfier 
                    } else {
                        tont.add((double) 0);
                    }
                    
                   // tont.add(-rs2.getDouble(1));
                      if(rs2.getDouble(1) != 0) tont.add(-rs2.getDouble(1));
            else tont.add(0.0);
                    
                     // avant ajout 
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month == 11) {
            
//             Vector<Object> tont2 = new Vector<Object>();
//             tont2.add(date);
//             tont2.add(0.0);
//             tont2.add(0.0);
//             tont2.add(0.0);
//             tont2.add(0.0);
             TontineVector.add(tont);
        }
                    
                    TontineVector.add(tont);
                }
                beginCalendar.add(Calendar.MONTH, 1);

            }

            totalrow = TontineVector.size() - 1;
            Vector<Object> tont2 = new Vector<Object>();

            int yearstopped = beginCalendar.get(Calendar.YEAR);
            int monthstopped = beginCalendar.get(Calendar.MONTH);

            Date today = new Date();
            Calendar caltoday = Calendar.getInstance();
            caltoday.setTime(today);
            int yeartoday = caltoday.get(Calendar.YEAR);
            int monthtoday = caltoday.get(Calendar.MONTH);
            while (yearstopped != yeartoday || monthstopped != monthtoday) {
                System.out.println("true in the loop" + monthstopped);
                date = beginCalendar.getTime();
                java.sql.Date sqldate = new java.sql.Date(date.getTime());
                Vector<Object> tont = new Vector<Object>();
                pre2 = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE MONTH(DateRet)=MONTH('" + sqldate + "') AND YEAR(DateRet)=YEAR('" + sqldate + "')");
                rs2 = pre2.executeQuery();
                tont.add(date);
                tont.add((double) 0);
                tont.add((double) 0);
                if (rs2.next()) {
                    tont.add(rs2.getDouble(1));
                } else {
                    tont.add((double) 0);
                }
               // tont.add(-rs2.getDouble(1));
                  if(rs2.getDouble(1) != 0) tont.add(-rs2.getDouble(1));
            else tont.add(0.0);
                
                
                
                TontineVector.add(tont);
                beginCalendar.add(Calendar.MONTH, 1);
                yearstopped = beginCalendar.get(Calendar.YEAR);
                monthstopped = beginCalendar.get(Calendar.MONTH);

            }

// For this month
            date = beginCalendar.getTime();
            java.sql.Date sqldate = new java.sql.Date(date.getTime());
            Vector<Object> tont = new Vector<Object>();
            pre2 = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE MONTH(DateRet)=MONTH('" + sqldate + "') AND YEAR(DateRet)=YEAR('" + sqldate + "')");
            rs2 = pre2.executeQuery();
            tont.add(date);
            tont.add((double) 0);
            tont.add((double) 0);
            if (rs2.next()) {
                tont.add(rs2.getDouble(1));
            } else {
                tont.add((double) 0);
            }
            if(rs2.getDouble(1) != 0) tont.add(-rs2.getDouble(1));
            else tont.add(0.0);
            TontineVector.add(tont);

            // Total
            tont2.add(new Date());
            tont2.add((double) 0);
            tont2.add((double) 0);
            tont2.add((double) 0);
            TontineVector.add(tont2);

            if (rs1 != null) {
                rs1.close();
            }
            if (rs2 != null) {
                rs2.close();
            }
            if (pre1 != null) {
                pre1.close();
            }
            if (pre2 != null) {
                pre2.close();
            }
            conn.close();
            return TontineVector;
        } else {
            return null;
        }
    }

    public ChartPanel PlotCurve() {

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

    public void setChart(ChartPanel p) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries t1 = new TimeSeries("Commission sur Tontine");
        for (int i = 0; i < jTable1.getRowCount()-1; i++) {
            System.out.println("dttype" + jTable1.getValueAt(i, 0));
            t1.add(new Day((Date) jTable1.getValueAt(i, 0)), (double) jTable1.getValueAt(i, 1));
        }
//        XYSeries series = new XYSeries("MyGraph");
//        series.add(0, 1);
//        series.add(1, 2);
//        series.add(2, 5);
//        series.add(7, 8);
//        series.add(9, 10);
        dataset.addSeries(t1);
        
        TimeSeries t2 = new TimeSeries("Solde par mois");
        for (int i = 0; i < jTable1.getRowCount()-1; i++) {
            System.out.println("dttype" + jTable1.getValueAt(i, 0));
            t2.add(new Day((Date) jTable1.getValueAt(i, 0)), (double) jTable1.getValueAt(i, 4));
        }
//        XYSeries series = new XYSeries("MyGraph");
//        series.add(0, 1);
//        series.add(1, 2);
//        series.add(2, 5);
//        series.add(7, 8);
//        series.add(9, 10);
        dataset.addSeries(t2);
//
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Progression des cotisations pour " + jComboBox1.getSelectedItem().toString(),
                "Mois",
                "Montant",
                dataset);

        p.setChart(chart);

        //  Container contenu = getContentPane() ;
        // contenu.add(panel);
    }

    public JFreeChart getChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries t1 = new TimeSeries("Commission sur Tontine");
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            t1.add(new Day((Date) jTable1.getValueAt(i, 0)), (double) jTable1.getValueAt(i, 1));
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
                "Progression des cotisations pour ",
                "Mois",
                "Montant",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return (chart);

        //  Container contenu = getContentPane() ;
        // contenu.add(panel);
    }

    private void reCalcurate(TableModel ml) {
        if (ml == null) {
            return;
        }
        double total = 0.0;
        for (int i = 0; i < totalrow; i++) {
            //   total += ((Double) ml.getValueAt(i, totalrow)).doubleValue();
            total += ((Double) jTable1.getValueAt(i, totalrow)).doubleValue();
        }

        jTable1.setValueAt(new Double(total), totalrow, TOTAL_COLUMN);
    }
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
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
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable() {
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
        jLabel3 = new javax.swing.JLabel();
        conn = Connect.ConnectDb();
        try {
            pre = conn.prepareStatement("SELECT MIN(YEAR(DateTontine)) as minyear, MAX(YEAR(DateTontine)) as maxyear FROM Tontine");
            rs = pre.executeQuery();
            while (rs.next()) {
                minyear=rs.getInt(1);
                maxyear=Calendar.getInstance().get(Calendar.YEAR);  // méthode à revoir
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
            pre = conn.prepareStatement("SELECT MIN(DateTontine) FROM Tontine");
            rs = pre.executeQuery();
            while (rs.next()) {
                SimpleDateFormat simplefm = new SimpleDateFormat("MM");
                Date minDate=(rs.getDate(1));
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
        jComboBox2 = new javax.swing.JComboBox();
        jPanel3 = new ChartPanel(getChart());
        panel1 = new java.awt.Panel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/Bilan_tontine.png"))); // NOI18N

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
            pre = conn.prepareStatement("SELECT MIN(DateTontine), MAX(DateTontine) FROM Tontine");
            rs0 = pre.executeQuery();
            IntializeDate dateini = new IntializeDate();

            while (rs0.next()) {
                startDate=rs0.getDate(1);
                endDate=rs0.getDate(2);
            }

            if (dateini.change) {

                if (dateini.initdate.after(startDate)) startDate = dateini.initdate;

            }
        } catch (SQLException ex) {
            Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
        }   finally {

            try {
                if (rs0 !=null)
                rs0.close();

                if (pre !=null)
                pre.close();

                if (conn !=null)
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            // TODO add your handling code here:
            data=getSynthese();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[][] out = to2DimArray(data);

        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setFillsViewportHeight(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            out,
            new String [] {
                "Mois", "Commission sur Tontine", "Cotisation", "Retraits", "Solde"
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
    sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());
    jTable1.setRowSorter(sorter);

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
        if( value instanceof Date) {
            value = f.format(value);
        } else {
            value = String.format("%.0f", value);
        }

        if (column == 0 && row == 12) value = "Total";
        if (column == 0 && jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == jTable1.getRowCount() -1) value = "Total";
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row == 12 || ( jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == (jTable1.getRowCount()-1))) {c.setBackground(LightYellow);} else {
            c.setBackground(Color.WHITE);
        }

        if(isSelected) {
            c.setForeground(Color.BLACK);
        }

        return c;
    }
    };

    // pasted

    TableCellRenderer tableCellRenderer4 = new DefaultTableCellRenderer() {

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

            value =  String.format("%.0f", value);
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row == 12 || ( jComboBox1.getSelectedIndex()== jComboBox1.getItemCount()-1 && row == jTable1.getRowCount() -1)) {c.setBackground(LightYellow);} else {
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

    jTable1.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);
    jTable1.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer4);
    jTable1.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer4);
    jTable1.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer4);
    jTable1.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer4);
    jTable1.setSelectionBackground(Color.BLUE);
    jScrollPane1.setViewportView(jTable1);

    jLabel2.setText("Choix de l'année:");

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Exercice 2011", "Exercice 2012", "Exercice 2013", "Exercice 2014" }));
    int minyear=0;
    int maxyear=0;
    conn = Connect.ConnectDb();
    ResultSet rs = null;
    try {
        pre = conn.prepareStatement("SELECT MIN(YEAR(DateTontine)) as minyear, MAX(YEAR(DateTontine)) as maxyear FROM Tontine");
        rs = pre.executeQuery();
        while (rs.next()) {
            //minyear=rs.getInt(1);
            //maxyear=rs.getInt(2);
            maxyear=Calendar.getInstance().get(Calendar.YEAR);  // méthode à revoir
            if(maxyear < rs.getInt(2)) maxyear=rs.getInt(2);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            minyear = calendar.get(Calendar.YEAR);
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
            .addContainerGap(22, Short.MAX_VALUE)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(jLabel2)
                    .add(18, 18, 18)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 269, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 769, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(32, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel1Layout.createSequentialGroup()
            .add(12, 12, 12)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel2)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(18, 18, 18)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 275, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(145, Short.MAX_VALUE))
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

    jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable2.setFillsViewportHeight(true);
    jTable2.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable2.setSurrendersFocusOnKeystroke(true);
    jTable2.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable2.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable2.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable2.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable2.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable2.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable2.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable2.getColumnModel().getColumn(10).setPreferredWidth(80);

    //jTable2.getColumnModel().getColumn(11).setMaxWidth(0);
    //jTable2.getColumnModel().getColumn(12).setMaxWidth(0);
    //jTable2.getColumnModel().getColumn(11).setMinWidth(0);
    //jTable2.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    IndicatorCellRenderer renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable2.getBackground());

    // set limit value and fill color
    Hashtable limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    //   jTable2.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane2= new FrozenTablePane(jTable2, 3);
    jScrollPane2.setViewportView(jTable2);

    org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
            .add(39, 39, 39))
    );

    jTabbedPane2.addTab("JAN", jPanel4);

    jTable3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable3.setFillsViewportHeight(true);
    jTable3.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable3.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable3.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable3.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable3.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable3.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable3.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable3.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable3.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable3.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable3.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable3.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable3.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable3.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable3.getColumnModel().getColumn(11).setMinWidth(0);
    jTable3.getColumnModel().getColumn(12).setMinWidth(0);

    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable2.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    System.out.println("dfdfd column model"+jTable2.getColumnModel().getColumnCount());
    jTable3.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane3= new FrozenTablePane(jTable3, 3);
    jScrollPane3.setViewportView(jTable3);

    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("FEV", jPanel5);

    jTable4.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable4.setFillsViewportHeight(true);
    jTable4.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable4.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable4.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable4.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable4.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable4.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable4.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable4.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable4.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable4.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable4.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable4.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable4.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable4.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable4.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable4.getColumnModel().getColumn(11).setMinWidth(0);
    jTable4.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable2.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable4.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane4= new FrozenTablePane(jTable4, 3);
    jScrollPane4.setViewportView(jTable4);

    org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("MAR", jPanel6);

    jTable5.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable5.setFillsViewportHeight(true);
    jTable5.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable5.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable5.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable5.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable5.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable5.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable5.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable5.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable5.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable5.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable5.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable5.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable5.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable5.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable5.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable5.getColumnModel().getColumn(11).setMinWidth(0);
    jTable5.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable5.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable5.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane5= new FrozenTablePane(jTable5, 3);
    jScrollPane5.setViewportView(jTable5);

    org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("AVR", jPanel7);

    jTable6.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable6.setFillsViewportHeight(true);
    jTable6.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable6.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "", ""
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable6.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable6.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable6.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable6.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable6.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable6.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable6.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable6.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable6.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable6.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable6.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable6.getColumnModel().getColumn(11).setMinWidth(0);
    jTable6.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable6.getColumnModel().getColumn(12).setMinWidth(0);
    jTable6.getColumnModel().getColumn(12).setMaxWidth(0);

    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable6.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable6.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane6= new FrozenTablePane(jTable6, 3);
    jScrollPane6.setViewportView(jTable6);

    org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("MAI", jPanel8);

    jTable7.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable7.setFillsViewportHeight(true);
    jTable7.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable7.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable7.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable7.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable7.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable7.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable7.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable7.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable7.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable7.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable7.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable7.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable7.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable7.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable7.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable7.getColumnModel().getColumn(11).setMinWidth(0);
    jTable7.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable2.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable7.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane7= new FrozenTablePane(jTable7, 3);
    jScrollPane7.setViewportView(jTable7);

    org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("JUIN", jPanel9);

    jTable8.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable8.setFillsViewportHeight(true);
    jTable8.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable8.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable8.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable8.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable8.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable8.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable8.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable8.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable8.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable8.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable8.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable8.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable8.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable8.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable8.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable8.getColumnModel().getColumn(11).setMinWidth(0);
    jTable8.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable2.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable7.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane8= new FrozenTablePane(jTable8, 3);
    jScrollPane8.setViewportView(jTable8);

    org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("JUIL", jPanel10);

    jTable9.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable9.setFillsViewportHeight(true);
    jTable9.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable9.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable9.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable9.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable9.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable9.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable9.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable9.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable9.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable9.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable9.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable9.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable9.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable9.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable9.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable9.getColumnModel().getColumn(11).setMinWidth(0);
    jTable9.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable9.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable9.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane9= new FrozenTablePane(jTable9, 3);
    jScrollPane9.setViewportView(jTable9);

    org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("AOU", jPanel11);

    jTable10.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable10.setFillsViewportHeight(true);
    jTable10.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable10.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable10.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable10.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable10.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable10.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable10.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable10.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable10.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable10.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable10.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable10.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable10.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable10.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable10.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable10.getColumnModel().getColumn(11).setMinWidth(0);
    jTable10.getColumnModel().getColumn(12).setMinWidth(0);

    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable10.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable10.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane10= new FrozenTablePane(jTable10, 3);
    jScrollPane10.setViewportView(jTable10);

    org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
        jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel12Layout.setVerticalGroup(
        jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("SEPT", jPanel12);

    jTable11.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable11.setFillsViewportHeight(true);
    jTable11.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable11.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable11.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable11.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable11.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable11.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable11.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable11.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable11.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable11.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable11.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable11.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable11.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable11.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable11.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable11.getColumnModel().getColumn(11).setMinWidth(0);
    jTable11.getColumnModel().getColumn(12).setMinWidth(0);

    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable11.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable11.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane11= new FrozenTablePane(jTable11, 3);
    jScrollPane11.setViewportView(jTable11);

    org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
        jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel13Layout.setVerticalGroup(
        jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("OCT", jPanel13);

    jTable12.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable12.setFillsViewportHeight(true);
    jTable12.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable12.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable12.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable12.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable12.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable12.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable12.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable12.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable12.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable12.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable12.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable12.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable12.getColumnModel().getColumn(10).setPreferredWidth(80);

    jTable12.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable12.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable12.getColumnModel().getColumn(11).setMinWidth(0);
    jTable12.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable12.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable12.getColumnModel().getColumn(7).setCellRenderer(renderer);
    jScrollPane12= new FrozenTablePane(jTable12, 3);
    jScrollPane12.setViewportView(jTable12);

    org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("NOV", jPanel14);

    jTable13.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable13.setFillsViewportHeight(true);
    jTable13.setPreferredScrollableViewportSize(new Dimension(1000,70));
    jTable13.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, "df", "df", null, null, null, null, "50.4534", null, null, null, null, null},
            {null, null, null, null, null, null, null, "20.58", null, null, null, null, null},
            {null, null, null, null, null, null, null, "10.2585", null, null, null, null, null},
            {null, null, null, null, null, null, null, "85.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "69.258", null, null, null, null, null},
            {null, null, null, null, null, null, null, "3258.25", null, null, null, null, null}
        },
        new String [] {
            "N°", "N° Carnet", "Noms & Prénoms", "Report", "Mise", "Retenue", "Cot. /31", "Pourcentage", "Total", "Retraits", "Solde", "IdEpargnant", "TypeEpargnant"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
    });
    jTable13.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable13.getColumnModel().getColumn(1).setPreferredWidth(90);
    jTable13.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable13.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable13.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable13.getColumnModel().getColumn(5).setPreferredWidth(90);
    jTable13.getColumnModel().getColumn(6).setPreferredWidth(100);
    jTable13.getColumnModel().getColumn(7).setPreferredWidth(90);
    jTable13.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable13.getColumnModel().getColumn(9).setPreferredWidth(80);
    jTable13.getColumnModel().getColumn(10).setPreferredWidth(80);

    //jTable13.getColumnModel().getColumn(0).setPreferredWidth(30);
    //jTable13.getColumnModel().getColumn(1).setPreferredWidth(90);
    //jTable13.getColumnModel().getColumn(2).setPreferredWidth(250);
    //jTable13.getColumnModel().getColumn(3).setPreferredWidth(130);
    //jTable13.getColumnModel().getColumn(4).setPreferredWidth(130);
    //jTable13.getColumnModel().getColumn(5).setPreferredWidth(90);
    //jTable13.getColumnModel().getColumn(6).setPreferredWidth(120);
    //jTable13.getColumnModel().getColumn(7).setPreferredWidth(130);
    //jTable13.getColumnModel().getColumn(8).setPreferredWidth(130);
    //jTable13.getColumnModel().getColumn(9).setPreferredWidth(130);
    //jTable13.getColumnModel().getColumn(10).setPreferredWidth(130);

    jTable13.getColumnModel().getColumn(11).setMaxWidth(0);
    jTable13.getColumnModel().getColumn(12).setMaxWidth(0);
    jTable13.getColumnModel().getColumn(11).setMinWidth(0);
    jTable13.getColumnModel().getColumn(12).setMinWidth(0);
    // Progress bar renderer
    renderer = new IndicatorCellRenderer(MIN, MAX);
    renderer.setStringPainted(true);
    renderer.setBackground(jTable13.getBackground());

    // set limit value and fill color
    limitColors = new Hashtable();
    limitColors.put(new Integer(0), Color.green);
    limitColors.put(new Integer(60), Color.yellow);
    limitColors.put(new Integer(80), Color.red);
    renderer.setLimits(limitColors);
    jTable13.getColumnModel().getColumn(7).setCellRenderer(renderer);

    jScrollPane13= new FrozenTablePane(jTable13, 3);
    jComboBox2.setSelectedIndex(jComboBox2.getItemCount()-1);
    SimpleDateFormat fm = new SimpleDateFormat("MM");
    SimpleDateFormat fm2 = new SimpleDateFormat("yyyy");
    try {
        fillmonthsummtable(Integer.valueOf(fm2.format(today)).intValue(), Integer.valueOf(fm.format(today)).intValue());
    } catch (SQLException ex) {
        Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ParseException ex) {
        Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Exception ex) {
        Logger.getLogger(TontineSynthese.class.getName()).log(Level.SEVERE, null, ex);
    }
    jScrollPane13.setViewportView(jTable13);

    org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
    jPanel15.setLayout(jPanel15Layout);
    jPanel15Layout.setHorizontalGroup(
        jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
    );
    jPanel15Layout.setVerticalGroup(
        jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jScrollPane13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("DEC", jPanel15);

    jLabel3.setText("Année :");

    System.out.println("minyear"+minyear);
    if (minyear !=0 && maxyear !=0) {
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        JPanel jPanel5 = new JPanel();
        jComboBox2.removeAllItems();
        for (int i=minyear; i<=maxyear; i++) {
            jComboBox2.addItem("Exercice "+i);
        }
    }
    //jTabbedPane2.add("tab 3", jPanel4);
    //jTabbedPane2.add("tab 4", jPanel4);
    //jTabbedPane2.add("tab 5", jPanel4);
    jComboBox2.setSelectedIndex(jComboBox2.getItemCount()-1);
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
                    .add(31, 31, 31)
                    .add(jLabel3)
                    .add(33, 33, 33)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 256, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
            .add(18, 18, 18)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel3)
                .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jTabbedPane2)
            .addContainerGap())
    );

    jTabbedPane1.addTab("Versements", jPanel2);

    //if (jComboBox1.getSelectedIndex() != -1) setChart((ChartPanel) jPanel3);
    jComboBox1.setSelectedIndex(jComboBox1.getItemCount()-1);
    //jScrollPane2= new FrozenTablePane(jTable2, 1);
    //jScrollPane2.setViewportView(jTable2);

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 823, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 477, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("Progression annuelle", jPanel3);
    for (int i= Integer.valueOf(fm.format(today)).intValue(); i<12; i++) {
        jTabbedPane2.setEnabledAt(i, false);
    }

    jTabbedPane2.setSelectedIndex(Integer.valueOf(fm.format(today)).intValue()-1);

    jTable14.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
        },
        new String [] {
            "ID", "Date", "Noms & Prénoms/Rais. Soc", "Mise", "Nbre de cot.", "Montant", "Libellé"
        }
    ) {
        Class[] types = new Class [] {
            java.lang.Integer.class, java.util.Date.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, String.class
        };
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    jScrollPane14.setViewportView(jTable14);

    jLabel4.setText("Date début");

    jLabel5.setText("Date fin");

    demoDateField1.setYearDigitsAmount(4);

    demoDateField2.setYearDigitsAmount(4);

    jButton1.setText("Valider");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jLabel6.setText("Total:");

    org.jdesktop.layout.GroupLayout panel1Layout = new org.jdesktop.layout.GroupLayout(panel1);
    panel1.setLayout(panel1Layout);
    panel1Layout.setHorizontalGroup(
        panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(panel1Layout.createSequentialGroup()
            .addContainerGap()
            .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jScrollPane14)
                .add(panel1Layout.createSequentialGroup()
                    .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jLabel4)
                        .add(jLabel5))
                    .add(41, 41, 41)
                    .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(demoDateField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                        .add(demoDateField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(62, 62, 62)
                    .add(jLabel6)
                    .add(77, 77, 77)
                    .add(jLabel7)
                    .add(0, 322, Short.MAX_VALUE))
                .add(org.jdesktop.layout.GroupLayout.TRAILING, panel1Layout.createSequentialGroup()
                    .add(0, 0, Short.MAX_VALUE)
                    .add(jButton1)
                    .add(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    panel1Layout.setVerticalGroup(
        panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(panel1Layout.createSequentialGroup()
            .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(panel1Layout.createSequentialGroup()
                    .add(21, 21, 21)
                    .add(jLabel4)
                    .add(11, 11, 11))
                .add(org.jdesktop.layout.GroupLayout.TRAILING, panel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6)
                            .add(jLabel7))
                        .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
            .add(panel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel5))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jScrollPane14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
            .add(18, 18, 18)
            .add(jButton1)
            .add(29, 29, 29))
    );

    jButton1.getAccessibleContext().setAccessibleName("JButton1");
    jButton1.getAccessibleContext().setAccessibleDescription("");

    jTabbedPane1.addTab("Historique des mouvements", panel1);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(jLabel1)
            .add(0, 0, Short.MAX_VALUE))
        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jTabbedPane1))
    );

    jTabbedPane1.getAccessibleContext().setAccessibleName("");

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(((String) jComboBox1.getSelectedItem().toString()).substring(jComboBox1.getSelectedItem().toString().lastIndexOf(" ") + 1), 0);
        sorter.setRowFilter(rf);
        
        Double cmtsum = 0.0;
        Double cotrsum = 0.0;
        Double retrsum = 0.0;
        Double soldesum = 0.0;
        System.out.println("jTabe row count"+ jTable1.getRowCount());
        for (int i=0; i < jTable1.getRowCount() - 1;  i++ ){
            cmtsum = cmtsum+ (double) jTable1.getValueAt(i, 1);
           
            
        
            
        }
        
         for (int i=0; i < jTable1.getRowCount() - 1;  i++ ){
            cotrsum = cotrsum+ (double) jTable1.getValueAt(i, 2);
           
            
        
            
        }
        
         for (int i=0; i < jTable1.getRowCount() - 1;  i++ ){
            retrsum = retrsum + (double) jTable1.getValueAt(i, 3);
           
            
        
            
        }
         
         for (int i=0; i < jTable1.getRowCount() - 1;  i++ ){
            soldesum = soldesum + (double) jTable1.getValueAt(i, 4);
           
            
        
            
        }
         
        jTable1.setValueAt(cmtsum, jTable1.getRowCount()-1, 1);
        jTable1.setValueAt(cotrsum, jTable1.getRowCount()-1, 2);
        jTable1.setValueAt(retrsum, jTable1.getRowCount()-1, 3);
        jTable1.setValueAt(soldesum, jTable1.getRowCount()-1, 4);
        

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jPanel2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel2FocusGained
        // TODO add your handling code here:


    }//GEN-LAST:event_jPanel2FocusGained

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        JTabbedPane sourceTabbedPane = (JTabbedPane) evt.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        if (index == 2 && jComboBox1.getSelectedIndex() != -1) {
            setChart((ChartPanel) jPanel3);
        }

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged

// TODO add your handling code here:
        if (jComboBox2.getSelectedItem() != null && visitedmth[jTabbedPane2.getSelectedIndex()] != Integer.valueOf(((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length() - 4, ((String) jComboBox2.getSelectedItem()).length()))) {

            try {

                fillmonthsummtable(Integer.valueOf((((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length() - 4, ((String) jComboBox2.getSelectedItem()).length()))).intValue(), jTabbedPane2.getSelectedIndex() + 1);
            } catch (ParseException ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (jComboBox2.getSelectedItem() != null) {
            System.out.println("value string" + (((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length() - 4, ((String) jComboBox2.getSelectedItem()).length())));
        }
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (jComboBox2.getSelectedIndex() == 0) {
            for (int i = 0; i < originmonth - 1; i++) {
                jTabbedPane2.setEnabledAt(i, false);
            }

            for (int i = originmonth - 1; i < 12; i++) {
                jTabbedPane2.setEnabledAt(i, true);
            }

            if (jTabbedPane2.getSelectedIndex() < originmonth - 1) {
                jTabbedPane2.setSelectedIndex(originmonth - 1);
            }

        }

        if (jComboBox2.getSelectedIndex() == (jComboBox2.getItemCount() - 1)) {  // Si c'est le dernier
            System.out.println("it is true");
            SimpleDateFormat fm = new SimpleDateFormat("MM");
            int nowmonth = Integer.valueOf(fm.format(today)).intValue();
            for (int i = nowmonth; i < 12; i++) {
                jTabbedPane2.setEnabledAt(i, false);
                System.out.println("setting false" + i);
            }

            for (int i = 0; i < nowmonth; i++) {
                jTabbedPane2.setEnabledAt(i, true);
            }

            if (jTabbedPane2.getSelectedIndex() >= nowmonth) {
                jTabbedPane2.setSelectedIndex(nowmonth - 1);
            }
        }

        if (jComboBox2.getSelectedIndex() != 0 && jComboBox2.getSelectedIndex() != (jComboBox2.getItemCount() - 1)) {
            for (int i = 0; i < 12; i++) {
                jTabbedPane2.setEnabledAt(i, true);

            }
        }

        // update table 
        if (jComboBox2.getSelectedItem() != null && visitedmth[jTabbedPane2.getSelectedIndex()] != Integer.valueOf(((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length() - 4, ((String) jComboBox2.getSelectedItem()).length()))) {
            try {
                System.out.println("filling");
                fillmonthsummtable(Integer.valueOf((((String) jComboBox2.getSelectedItem()).substring(((String) jComboBox2.getSelectedItem()).length() - 4, ((String) jComboBox2.getSelectedItem()).length()))).intValue(), jTabbedPane2.getSelectedIndex() + 1);
            } catch (ParseException ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Epargneview2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    public Vector getMembres() throws Exception {
        sumtontineplg =0;
        conn = Connect.ConnectDb();
        int i = 1;
        java.sql.Date sqlDate1 = new java.sql.Date(demoDateField1.getDate().getTime());
        java.sql.Date sqlDate2 = new java.sql.Date(demoDateField2.getDate().getTime());
        PreparedStatement pre;
        System.out.println("requête" + "SELECT * FROM ((SELECT DateTontine, Mise, JoursTontine, IdEpargnant, TypeEpargnant, 'Dépôt mensuel' as Libelle FROM Tontine WHERE DateTontine between '" + sqlDate1 + "' AND '" + sqlDate2 + "') UNION (SELECT DateRet as DateTontine, '0' as Mise, '0' as JoursTontine, IdEpargnant, TypeEpargnant FROM retraits_tontine  WHERE DateRet between '" + sqlDate1 + "' AND '" + sqlDate2 + " ;))  ORDER BY DateTontine ");
       // pre = conn.prepareStatement("SELECT * FROM Tontine WHERE DateTontine between '" + sqlDate1 + "' AND '" + sqlDate2 + "' ORDER BY DateTontine;");
        pre = conn.prepareStatement("SELECT * FROM ((SELECT DateTontine, Mise, JoursTontine, IdEpargnant, TypeEpargnant, 'Dépôt mensuel' as Libelle, '0' as Montant FROM Tontine WHERE DateTontine between '" + sqlDate1 + "' AND '" + sqlDate2 + "') UNION (SELECT DateRet as DateTontine, '0' as Mise, '0' as JoursTontine, IdEpargnant, TypeEpargnant, Libelle, Montant FROM retraits_tontine  WHERE DateRet between '" + sqlDate1 + "' AND '" + sqlDate2 + "')) tbl2 ORDER BY DateTontine");

        ResultSet rs = pre.executeQuery();
        // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();
        Vector<Vector> membreVector = new Vector<Vector>();
        while (rs.next()) {
     //   Vector<String> membre = new Vector<String>();
            //   membre.add(String.valueOf(i)); 
            Vector<Object> membre = new Vector<Object>();
            membre.add(i);
            membre.add(rs.getDate("DateTontine")); 
            String type = rs.getString("TypeEpargnant");
            int id = rs.getInt("IdEpargnant");
            String epargnant = getNamefromId(id, type);
            membre.add(epargnant);
            membre.add(rs.getInt("Mise"));
            int nbcot=0;
            if ( (rs.getString("JoursTontine") != null) && (rs.getString("Libelle") != null) && (rs.getString("Libelle").equals("Dépôt mensuel")) && ((rs.getString("JoursTontine").length()==1) || (rs.getString("JoursTontine").length()==1)))  { nbcot =1; }
            else if ((rs.getString("JoursTontine") != null) && (rs.getString("Libelle") != null) && rs.getString("Libelle").equals("Dépôt mensuel")) {
                  nbcot =Integer.valueOf(rs.getString("JoursTontine").substring(rs.getString("JoursTontine").lastIndexOf(",")+1, rs.getString("JoursTontine").length()));

            }
            
            
           
if (rs.getString("JoursTontine") !=null) {
    System.out.println("Libelle frfrfrf"+ rs.getString("Libelle"));
    if (rs.getString("Libelle") != null && rs.getString("Libelle").equals("Dépôt mensuel")) {
        System.out.println("we are in the case");
    membre.add(nbcot);
   
    membre.add(rs.getInt("Mise") * nbcot); 
    sumtontineplg = sumtontineplg + rs.getInt("Mise") * nbcot;
    } else {
     membre.add(0); 
     membre.add(-rs.getDouble("Montant")); 
     sumtontineplg = sumtontineplg -rs.getDouble("Montant");
    }
    
} else {
    membre.add(0);
    membre.add(0.0);
}
            

            //
 membre.add(rs.getString("Libelle"));
            membreVector.add(membre);
            i++;
        }

        /*Close the connection after use (MUST)*/
        if (conn != null) {
            conn.close();
        }

        return membreVector;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (demoDateField1.getDate() == null || demoDateField2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner des dates valides");
        } else {  
        try {
            // TODO add your handling code here:
            data2 = getMembres();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
         Vector<String> header = new Vector<String>();
         header.add("ID");
         header.add("Date");
         header.add("Noms & Prénoms/Rais. Soc");
         header.add("Mise");
         header.add("Nbre de cot");
         header.add("Montant");
         header.add("Libellé");
         jTable14.setModel(new javax.swing.table.DefaultTableModel(data2, header) {

       Class[] types = new Class [] {
        java.lang.Integer.class, java.util.Date.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class
    };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            
               boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false, false
           };      
         
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
        });
         
   
         
         
         
         
         
         
         
         
         
         
         
         
        jTable14.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable14.setFillsViewportHeight(true);
        jTable14.setPreferredScrollableViewportSize(new Dimension(1000, 70));
        jTable14.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTable14.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable14.getColumnModel().getColumn(2).setPreferredWidth(185);
        jTable14.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable14.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTable14.getColumnModel().getColumn(5).setPreferredWidth(150);
        jTable14.getColumnModel().getColumn(6).setPreferredWidth(195);
     
        jScrollPane1.setViewportView(jTable1);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("fr", "TG"));
        jLabel7.setText(formatter.format((sumtontineplg)));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(TontineSynthese.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TontineSynthese.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TontineSynthese.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TontineSynthese.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TontineSynthese().setVisible(true);
            }
        });
    }

    public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
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
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables

}
