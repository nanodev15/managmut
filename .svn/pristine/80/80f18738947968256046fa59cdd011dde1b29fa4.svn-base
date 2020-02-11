/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author elommarcarnold
 */
 class SeparatorComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer
{
   public SeparatorComboBoxRenderer() {
      super();
   }
    
   public Component getListCellRendererComponent( JList list,
           Object value, int index, boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
          setBackground(list.getSelectionBackground());
          setForeground(list.getSelectionForeground());
      }
      else {
          setBackground(list.getBackground());
          setForeground(list.getForeground());
      }
  
      setFont(list.getFont());
      if (value instanceof Icon) {
         setIcon((Icon)value);
      }
      if (value instanceof JSeparator) {
         return (Component) value;
      }
      else {
         setText((value == null) ? "" : value.toString());
      }
  
      return this;
  } 
}