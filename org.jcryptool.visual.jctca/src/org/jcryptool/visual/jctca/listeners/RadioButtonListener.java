package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RadioButtonListener implements SelectionListener {
Combo cmb_select;
Label lbl_selected_file;
Text txt_text;
Color black = Display.getDefault().getSystemColor(
		SWT.COLOR_BLACK);
Color dark_gray = Display.getDefault().getSystemColor(
		SWT.COLOR_DARK_GRAY);
Button btn_select_file;
Boolean enabled;
Group select;
Button btn1;
Button btn2;
Button btn3;
Label selected_file;
	
	public RadioButtonListener(Combo cmb_select) {
		this.cmb_select = cmb_select;
	}
	
	public RadioButtonListener(Label lbl_selected_file, Text txt_text, Button btn_select_file) {
		this.lbl_selected_file = lbl_selected_file;
		this.txt_text = txt_text;
		this.btn_select_file = btn_select_file;

	}
	
	public RadioButtonListener(boolean enabled, Button btn1, Button btn2, Button btn3, Label selected_file) {
		this.enabled = enabled;
		this.select = select;
		this.btn1 = btn1;
		this.btn2 = btn2;
		this.btn3= btn3;
		this.selected_file = selected_file;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Button btn_selected= (Button) e.getSource();
		if(btn_selected.getData().equals("generate")) {
			cmb_select.setEnabled(false);
		}else if(btn_selected.getData().equals("select")){
			cmb_select.setEnabled(true);
		}else if(btn_selected.getData().equals("file")) {
			lbl_selected_file.setEnabled(true);
			txt_text.setEnabled(false);
			txt_text.setForeground(dark_gray);
			btn_select_file.setEnabled(true);
		}else if(btn_selected.getData().equals("text")) {
			lbl_selected_file.setEnabled(false);
			txt_text.setEnabled(true);
			//set fontcolor to black - looks like enabled
			txt_text.setForeground(black);
			btn_select_file.setEnabled(false);
		}else if(btn_selected.getData().equals("detail")) {
			btn1.setEnabled(enabled);
			btn2.setEnabled(enabled);
			btn3.setEnabled(enabled);
			selected_file.setEnabled(enabled);
		}
		

		
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
