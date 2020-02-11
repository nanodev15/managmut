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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PhoneDocument extends PlainDocument {
    //(XX) XXXX-XXXX
    //(XX) XXXXX-XXXX

    @Override
    public void insertString(int i, String string, AttributeSet as) throws BadLocationException {

        if (string != null) {
    //        if ((string.matches("\\d+") && getLength() < 15) || string.matches("\\([0-9]{2}\\) [0-9]{4,5}-[0-9]{4}")) {
              System.out.println("string"+string);
              // (string.matches("^\\+"))
            if (true) {
                System.out.println("match");
            super.insertString(i, string, as);
                insertNumeric();
            }
        }

    }

    private void insertNumeric() throws BadLocationException {
        String numericText = "";
        int i=0;
//        for (char character : super.getText(0, super.getLength()).toCharArray()) {
//            
//            if (Character.isDigit(character) || character =='+') {
//                numericText += character;
//            }
//        }
        for (char character : super.getText(0, super.getLength()).toCharArray()) {
            
            if (i==0 && character =='+') {
                numericText += character;
            } else if (i != 0 && Character.isDigit(character)){
                 numericText += character;
            }
            i++;
        }
        System.out.println("numeric"+numericText);
        if (getLength() > 0) {
            super.remove(0, getLength());
           // super.insertString(0, formatPhoneNumber(numericText), null);
            super.insertString(0, numericText, null);
        }
    }

    private String formatPhoneNumber(String number) {
        String formatted = "";
        int numberLength = number.length();

        char[] numberCharSet = number.toCharArray();
        for (int i = 0; (i < numberLength && i < 11); i++) {
            formatted += before(i, numberLength);
            formatted += numberCharSet[i];
        }
        return formatted;
    }

    private String before(int index, int length) {
        String result = "";
        
        if (index == 0) {
            result = "(";
        }
        if (index == 2) {
            result = ") ";
        }
        if (index == 6 && length < 11) {
            result = "-";
        }
        if (index == 7 && length == 11) {
            result = "-";
        }
        return result;
    }

    @Override
    public void remove(int i, int i1) throws BadLocationException {
        super.remove(i, i1);
        insertNumeric();
    }

}
 