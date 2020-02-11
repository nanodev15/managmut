/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

/**
 *
 * @author elommarcarnold
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.KeyEvent;
import java.text.*;
import java.util.Date;
import java.util.GregorianCalendar;
 
public class DateDocument extends PlainDocument
{
  public String initString = "yyyy MM dd";
  private int ivorher = 0;
 
  Date datum = new Date();
  private String str;
  private static int sep1 = 4, sep2 = 7;
  private JTextComponent textComponent;
  private int newOffset;
 
  /**
   * Überprüft eingegebenes Zeichen auf seine Gültigkeit->
   * Buchstaben oder sonstige Zeichen sind nicht zulässig
   * @param tc JTextComponent
   */
  public DateDocument(JTextComponent tc)
  {
    textComponent = tc;
    try{
         insertString(0,initString,null);
       }
       catch(Exception ex) {
          ex.printStackTrace();
       }
  }
 
 
  /**
   * Versucht das eingegebene Zeichen auf einen Integer zu parsen. Zusätzlich wird
   * überprüft, wo sich der Cursor befindet (womöglich vor einem Trennzeichen, das
   * übersprungen werden soll). Tage und Monate werden bereits während der Eingabe
   * auf ihre Gültigkeit (z.B. Tag: 32 -> falsch, Monat: 13 -> falsch) überprüft.
   * @param offset  Position im String (ist zu vergleichem mit dem Initialisierungsstring)
   * @param s       Zeichen, das überprüft wird
   * @param attributeSet  AttributeSet wird nur an die Vaterklasse weitergeleitet
   * @throws BadLocationException  wird geworfen, wenn das Zeichen nicht geparst werden kann
   */
  public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException
  {
     if(s.equals(initString))
        super.insertString(offset,s,attributeSet);
     else
     {
         try
         {
            //DateFormat format = DateFormat.getDateInstance();
            SimpleDateFormat format = new SimpleDateFormat(initString);
            format.setLenient(false);       //ßberprüfung auf korrektes Datum
            Date dDatum = format.parse(s);
            super.remove(0,textComponent.getText().length());
            super.insertString(offset,s,attributeSet);
         }
         catch(Exception ex)
         {
             try
             {
               Integer.parseInt(s);
 
               newOffset = offset;
               if(atSeparator(offset))
               {
                 newOffset++;
                 textComponent.setCaretPosition(newOffset);
               }
               if(!dateIsOk(//textComponent.getText(0,newOffset)+
               s,newOffset))
               {
                  Toolkit.getDefaultToolkit().beep();
                  return;
               }
 
               super.remove(newOffset,1);
               super.insertString(newOffset,s,attributeSet);
             }
 
             catch(Exception ex2)
             {
               return; //only allow integer values
             }
         }
     }
  }
 
  /**
   *
   * @param offset  Position im String (ist zu vergleichen mit dem Inititialisierungsstring)
   * @param length  Länge des Strings
   * @throws BadLocationException  Wenn eine Position nicht verwendet bzw. ermittelt werden kann
   */
  public void remove(int offset, int length) throws BadLocationException
  {
     if(atSeparator(offset))
       textComponent.setCaretPosition(offset-1);
     else textComponent.setCaretPosition(offset);
  }
 
  /**
   * @param offset Position im String (ist zu vergleichen mit dem Initialisierungsstring)
   * @return boolean, true für Separater ist am offset, false für kein Separator
   */
  public boolean atSeparator(int offset)
  {
     return offset == sep1 || offset == sep2; //false
  }
 
  /**
   * Überprüft das eingegebene Datum auf seine Richtigkeit
   * @param txtDate  Zeichen (als String), das überprüft wird
   * @param off      Offset des Zeichens
   * @return         true für Datum ist ok, false für nicht möglich
   */
 private boolean dateIsOk(String txtDate,int off)
 {
   boolean ret = false;
   int curScan = 0;
   String cur = "";
   int len = 0;
   len = off+1;
   String svorher ="";
   String s ="";
   if(len < 1)
     curScan = 0;
   else curScan = len -1;
 
   try{
       s = initString.substring(len-1, len);
      if(curScan != 0)
         svorher = initString.substring(curScan-1, len-1);
      else
         svorher = "F";
      int izahl = Integer.parseInt(txtDate);
 
      if (s.compareTo("M")== 0)
      {
        if (svorher.compareTo("M") != 0)
        {
          if (izahl > 1)
            ret = false;
          else
            ret = true;
          ivorher = izahl;
        }
        else {
            if (ivorher == 1 && izahl > 2)
              ret = false;
            else if(ivorher == 0 && izahl == 0)
              ret = false;
            else
              ret = true;
            }
      }
      if (s.compareTo("d")==0 || s.compareTo("D")==0)
      {
        if (svorher.compareTo("d") !=0 && svorher.compareTo("D") !=0 )
        {
          if (izahl > 3)
            ret = false;
          else
            ret = true;
        ivorher = izahl;
        }
        else {
          if (ivorher == 3 && izahl > 1)
            ret = false;
          else if(ivorher == 0 && izahl == 0)
            ret = false;
          else
            ret = true;
 
        }
      }
      if(s.compareTo("y") == 0 || s.compareTo("Y")==0)
      {
        ret = true;
      }
 
    }catch(Exception ex){
      //ex.printStackTrace(System.out);   //soll nicht geworfen werden, weil Jahre noch nicht überprüft werden
                                //bringt Error, wenn an letzter Stelle noch Ziffer eingegeben wird.
    }
    finally{
      return ret;
    }
  }
}