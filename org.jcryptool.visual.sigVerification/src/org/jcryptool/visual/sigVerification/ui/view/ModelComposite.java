// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.view;

import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.visual.sigVerification.Messages;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
import org.jcryptool.visual.sigVerification.cert.CertGeneration;

/**
 * This class contains all the code required for the design and functionality of the verification
 * model view.
 *
 */
public class ModelComposite extends Composite {
    private Text lblGeneralDescription;
    private Text lblHeader;
    private Label lblRoot;
    private Label lbllevel2;
    private Label lbllevel3;
    private Text lblrootChooseEnd;
    private Text lblrootChooseStart;
    private Text lbllevel2ChooseEnd;
    private Text lbllevel2ChooseStart;
    private Text lbllevel3ChooseEnd;
    private Text lbllevel3ChooseStart;
    private Button btnNewResult;
    private Text lblChooseStartDate;
    private Text lblChooseEndDate;
    private CertGeneration certificates;
    private Text textValidDate;
	private String now;
	private String dateRootStart;
	private String dateRootEnd;
	private String dateLevel2Start;
	private String dateLevel2End;
	private String dateUserStart;
	private String dateUserEnd;
	private Date temp;
	private Date changeTest;
	private Date changeRootStart;
	private Date changeRootEnd;
	private Date changeLevel2Start;
	private Date changeLevel2End;
	private Date changeUserStart;
	private Date changeUserEnd;
	private Group mainGroup;
	private int status = 0; //0: not checked yet, 1: valid, 2: not valid
	private Composite resultComp;
	private SigVerView sigVerView;

    public ModelComposite(final Composite parent, final int style, final SigVerView sigVerView) {
        super(parent, SWT.NONE);
        this.sigVerView = sigVerView;
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        createContents(parent);
        createActions();
    }
    
    protected void addResetIconHandler() {
        // Adds reset button to the Toolbar
        AbstractHandler resetHandler = new AbstractHandler() {
	       	@Override
			public Object execute(ExecutionEvent event) {
	       		reset();
	       		return null;
	       	}
        };
        defineCommand(sigVerView.resetCommandId, "Reset", resetHandler);
    }

    private void defineCommand(final String commandId, final String name, AbstractHandler handler) {
        ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
    	Command command = commandService.getCommand(commandId);
    	command.define(name,  null, commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID));
    	command.setHandler(handler);
    }

    /**
     * Create contents of the application window.
     *
     * @param parent
     */
    private void createContents(final Composite parent) {
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        Color white = SWTResourceManager.getColor(255, 255, 255);
        setLayout(new GridLayout());

        certificates = new CertGeneration();
        try {//create default certificates
			certificates.setRoot(certificates.createCertificate(1));
			certificates.setLevel2(certificates.createCertificate(2));
			certificates.setUser(certificates.createCertificate(3));
		} catch (Exception e) {
            LogUtil.logError(e);
		}
        
        //HEADER AND DESCRPITION (TOP)
        Composite introComposite = new Composite(this, SWT.NONE);
        introComposite.setBackground(white);
        introComposite.setLayout(new GridLayout());
        introComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
		lblHeader = new Text(introComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblHeader.setEditable(false);
        lblHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
        lblHeader.setText(Messages.ModelComposite_lblHeader);
        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));

        lblGeneralDescription = new Text(introComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblGeneralDescription.setEditable(false);
        GridData gd_lblGeneralDescription = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_lblGeneralDescription.widthHint = 600;
		lblGeneralDescription.setLayoutData(gd_lblGeneralDescription);
		lblGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lblGeneralDescription.setText(Messages.ModelComposite_description);

		//MAIN GROUP
        mainGroup = new Group(this, SWT.NONE);
        mainGroup.setText(Messages.ModelComposite_lblTitle);
		mainGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_mainGroup = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_mainGroup.widthHint = 800;
		mainGroup.setLayoutData(gd_mainGroup);
		GridLayout gl_mainGroup = new GridLayout(2, true);
		gl_mainGroup.horizontalSpacing = 20;
		mainGroup.setLayout(gl_mainGroup);
	    
		//Headings for the date table
	    Label lblCertificate = new Label(mainGroup, SWT.READ_ONLY);
	    GridData gd_lblCertificate = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
	    gd_lblCertificate.widthHint = 150;
	    lblCertificate.setLayoutData(gd_lblCertificate);
	    lblCertificate.setText(Messages.ModelComposite_certLayer);
	    
	    Composite compositeChooseDateLabels = new Composite(mainGroup, SWT.NONE);
	    GridData gd_compositeChooseDateLabels = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    compositeChooseDateLabels.setLayoutData(gd_compositeChooseDateLabels);
		GridLayout gl_compositeChooseDateLabels = new GridLayout(2, true);
		gl_compositeChooseDateLabels.marginHeight = 0;
		compositeChooseDateLabels.setLayout(gl_compositeChooseDateLabels);
	    
	    lblChooseStartDate = new Text(compositeChooseDateLabels, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
	    lblChooseStartDate.setEditable(false);
	    lblChooseStartDate.setText(Messages.ModelComposite_ChooseStart);
	    GridData gd_lblChooseStartDate = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lblChooseStartDate.widthHint = 155;
	    gd_lblChooseStartDate.verticalIndent = 20;
	    lblChooseStartDate.setLayoutData(gd_lblChooseStartDate);
	    
	    lblChooseEndDate = new Text(compositeChooseDateLabels, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
	    lblChooseEndDate.setEditable(false);
	    lblChooseEndDate.setText(Messages.ModelComposite_ChooseEnd);
	    GridData gd_lblChooseEndDate = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lblChooseEndDate.widthHint = 155;
	    gd_lblChooseEndDate.verticalIndent = 20;
	    lblChooseEndDate.setLayoutData(gd_lblChooseEndDate);

		//ROOT CERT row
	    lblRoot = new Label(mainGroup, SWT.NONE);
	    lblRoot.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lblRoot = new GridData(SWT.RIGHT, SWT.FILL, false, false);
	    gd_lblRoot.widthHint = 340;
	    lblRoot.setLayoutData(gd_lblRoot);
	    lblRoot.setText(Messages.ModelComposite_lblroot);
	    
		temp = certificates.getRoot().getNotBefore();
		dateRootStart = setFormat(temp);
		temp = certificates.getRoot().getNotAfter();
		dateRootEnd = setFormat(temp);
	   
		Composite compRootTimes = new Composite(mainGroup, SWT.NONE);
		GridData gd_compRootTimes = new GridData(SWT.LEFT, SWT.FILL, false, false);
		compRootTimes.setLayoutData(gd_compRootTimes);
		GridLayout gl_compRootTimes = new GridLayout(2, true);
		gl_compRootTimes.marginHeight = 0;
		compRootTimes.setLayout(gl_compRootTimes);
		
	    lblrootChooseStart = new Text(compRootTimes, SWT.BORDER);
	    lblrootChooseStart.setEditable(true);
	    lblrootChooseStart.setText(dateRootStart);
	    lblrootChooseStart.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lblrootChooseStart = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lblrootChooseStart.widthHint = 150;
	    lblrootChooseStart.setLayoutData(gd_lblrootChooseStart);
		
	    lblrootChooseEnd = new Text(compRootTimes, SWT.BORDER);
	    lblrootChooseEnd.setEditable(true);
	    lblrootChooseEnd.setText(dateRootEnd);
	    lblrootChooseEnd.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lblrootChooseEnd = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lblrootChooseEnd.widthHint = 150;
	    lblrootChooseEnd.setLayoutData(gd_lblrootChooseEnd);

	    //LEVEL 2 CERT row   
	    lbllevel2 = new Label(mainGroup, SWT.NONE);
	    lbllevel2.setText(Messages.ModelComposite_lbllevel2);
	    lbllevel2.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lbllevel2 = new GridData(SWT.RIGHT, SWT.FILL, false, false);
	    gd_lbllevel2.widthHint = 280;
	    gd_lbllevel2.verticalIndent = 20;
	    lbllevel2.setLayoutData(gd_lbllevel2);
	    
		temp = certificates.getLevel2().getNotBefore();
		dateLevel2Start = setFormat(temp);
		temp=certificates.getLevel2().getNotAfter();
		dateLevel2End=setFormat(temp);
		
		Composite compLevel2Times = new Composite(mainGroup, SWT.NONE);
		GridData gd_compLevel2Times = new GridData(SWT.LEFT, SWT.FILL, false, false);
		compLevel2Times.setLayoutData(gd_compLevel2Times);
		GridLayout gl_compLevel2Times = new GridLayout(2, true);
		gl_compLevel2Times.marginHeight = 0;
		compLevel2Times.setLayout(gl_compLevel2Times);
	    
	    lbllevel2ChooseStart = new Text(compLevel2Times, SWT.BORDER);
	    lbllevel2ChooseStart.setEditable(true);
	    lbllevel2ChooseStart.setText(dateLevel2Start);
	    lbllevel2ChooseStart.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lbllevel2ChooseStart = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lbllevel2ChooseStart.widthHint = 150;
	    gd_lbllevel2ChooseStart.verticalIndent = 20; 
	    lbllevel2ChooseStart.setLayoutData(gd_lbllevel2ChooseStart);
	    
	    lbllevel2ChooseEnd = new Text(compLevel2Times, SWT.BORDER);
	    lbllevel2ChooseEnd.setEditable(true);
	    lbllevel2ChooseEnd.setText(dateLevel2End);
	    lbllevel2ChooseEnd.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lbllevel2ChooseEnd = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lbllevel2ChooseEnd.widthHint = 150;
	    gd_lbllevel2ChooseEnd.verticalIndent = 20; 
	    lbllevel2ChooseEnd.setLayoutData(gd_lbllevel2ChooseEnd);
		
	    //LEVEL 3 CERT row
	    lbllevel3 = new Label(mainGroup, SWT.NONE);
	    lbllevel3.setText(Messages.ModelComposite_lbllevel3);
	    lbllevel3.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lbllevel3 = new GridData(SWT.RIGHT, SWT.FILL, false, false);
	    gd_lbllevel3.widthHint = 220;
	    gd_lbllevel3.verticalIndent = 20;
	    lbllevel3.setLayoutData(gd_lbllevel3);

		temp = certificates.getUser().getNotBefore();
		dateUserStart = setFormat(temp);
		temp=certificates.getUser().getNotAfter();
		dateUserEnd=setFormat(temp);
		
		Composite compLevel3Times = new Composite(mainGroup, SWT.NONE);
		GridData gd_compLevel3Times = new GridData(SWT.LEFT, SWT.FILL, false, false);
		compLevel3Times.setLayoutData(gd_compLevel3Times);
		GridLayout gl_compLevel3Times = new GridLayout(2, true);
		gl_compLevel3Times.marginHeight = 0;
		compLevel3Times.setLayout(gl_compLevel3Times);
		
	    lbllevel3ChooseStart = new Text(compLevel3Times, SWT.BORDER);
	    lbllevel3ChooseStart.setEditable(true);
	    lbllevel3ChooseStart.setText(dateUserStart);
	    lbllevel3ChooseStart.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lbllevel3ChooseStart = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lbllevel3ChooseStart.widthHint = 150;
	    gd_lbllevel3ChooseStart.verticalIndent = 20; 
	    lbllevel3ChooseStart.setLayoutData(gd_lbllevel3ChooseStart);

	    lbllevel3ChooseEnd = new Text(compLevel3Times, SWT.BORDER);
	    lbllevel3ChooseEnd.setEditable(true);
	    lbllevel3ChooseEnd.setText(dateUserEnd);
	    lbllevel3ChooseEnd.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
	    GridData gd_lbllevel3ChooseEnd = new GridData(SWT.LEFT, SWT.FILL, false, false);
	    gd_lbllevel3ChooseEnd.widthHint = 150;
	    gd_lbllevel3ChooseEnd.verticalIndent = 20; 
	    lbllevel3ChooseEnd.setLayoutData(gd_lbllevel3ChooseEnd);
	    
	    //Result, validation date and buttons Area
	    resultComp = new Composite(mainGroup, SWT.NONE);
	    GridData gd_resultComp = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
	    gd_resultComp.verticalIndent = 20;
	    gd_resultComp.horizontalIndent = 20;
	    //gd_resultComp.heightHint = 50;
	    resultComp.setLayoutData(gd_resultComp);
	    resultComp.setLayout(new GridLayout(2, true));
	    resultComp.addPaintListener(new PaintListener() {
	    	@Override
			public void paintControl(PaintEvent e) {
	    		ImageDescriptor idIcon = null;
				if (status == 1) {
                	idIcon = ImageService.getImageDescriptor(SigVerificationPlugin.PLUGIN_ID, "icons/gruenerHacken.png");
                }
				else if (status == 2) {
                    idIcon = ImageService.getImageDescriptor(SigVerificationPlugin.PLUGIN_ID, "icons/rotesKreuz.png");
                }
				if (idIcon != null) {
		    		GC gc = e.gc;
		    		Rectangle area = resultComp.getClientArea(); // Get the size of the canvas
	                ImageData imdIcon = idIcon.getImageData(100);
	                Image imgIcon = new Image(Display.getCurrent(), imdIcon);
	            	gc.drawImage(imgIcon, (area.width / 2) - 125, -45);
				}
	    	}
	    });

		temp=certificates.getNow();//default date for validity checks
		now=setFormat(temp); //to string
		
		Label lblDummy = new Label(resultComp, SWT.NONE);
		lblDummy.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
		
		Label lblValidDate = new Label(resultComp, SWT.NONE);
		lblValidDate.setText(Messages.ModelComposite_validDate);
		GridData gd_lblValidDate = new GridData(SWT.LEFT, SWT.BOTTOM, true, false);
		gd_lblValidDate.horizontalIndent = 10;
		lblValidDate.setLayoutData(gd_lblValidDate);
		
		Label lblDummy2 = new Label(resultComp, SWT.NONE);
		lblDummy2.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));

		textValidDate = new Text(resultComp, SWT.BORDER);
		GridData gd_textValidDate = new GridData(SWT.LEFT, SWT.TOP, true, false);
		gd_textValidDate.widthHint = 150;
		gd_textValidDate.heightHint = 25;
		gd_textValidDate.horizontalIndent = 5;
		textValidDate.setLayoutData(gd_textValidDate);
		textValidDate.setEditable(true);
		textValidDate.setText(now);
		textValidDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		
		Composite btnComposite2 = new Composite(mainGroup, SWT.NONE);
		btnComposite2.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 2, 1));
		btnComposite2.setLayout(new GridLayout());

	    btnNewResult = new Button(btnComposite2, SWT.NONE);
	    btnNewResult.setEnabled(true);
	    btnNewResult.setText(Messages.ModelComposite_btnNewResult);
	    btnNewResult.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));

		Button btnReset = new Button(btnComposite2, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	reset();
		    }
		});
		btnReset.setText(Messages.ModelComposite_btnReset);
		btnReset.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));
		
		Composite hintComposite = new Composite(mainGroup, SWT.NONE);
		hintComposite.setLayout(new GridLayout());
		GridData gd_hintComposite = new GridData(SWT.CENTER, SWT.FILL, true, true, 2, 1);
		gd_hintComposite.widthHint = 500;
		hintComposite.setLayoutData(gd_hintComposite);
		
		Text certificateVerificationHint = new Text(hintComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.CENTER);
		certificateVerificationHint.setText(Messages.ModelComposite_lblCertificateVerification);
        GridData gd_certificateVerificationHint = new GridData(SWT.CENTER, SWT.FILL, true, true);
        certificateVerificationHint.setLayoutData(gd_certificateVerificationHint);
    }

    private void createActions() {
    	//Listener for the new date to valid at
    	 textValidDate.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(textValidDate.getText());
             	changeTest=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });
    	 
    	 //listener for the root validity
    	 lblrootChooseStart.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(lblrootChooseStart.getText());
             	changeRootStart=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });

    	 //listener for the root validity
    	 lblrootChooseEnd.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(lblrootChooseEnd.getText());
             	changeRootEnd=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });
    	 
     	//listener for the level2 validity
    	 lbllevel2ChooseStart.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(lbllevel2ChooseStart.getText());
             	changeLevel2Start=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });

    	//listener for the level2 validity
    	 lbllevel2ChooseEnd.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(lbllevel2ChooseEnd.getText());
             	changeLevel2End=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });
    	 
     	//listener for the user validity
    	 lbllevel3ChooseStart.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(lbllevel3ChooseStart.getText());
             	changeUserStart=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });

    	//listener for the user validity
    	 lbllevel3ChooseEnd.addModifyListener(new ModifyListener() {
             @Override
			public void modifyText(final ModifyEvent e) {
             	String temp=new String(lbllevel3ChooseEnd.getText());
             	changeUserEnd=toDate(temp);
             	status = 0;
             	resultComp.redraw();
             }
         });


     	btnNewResult.addSelectionListener(new SelectionAdapter(){
     		@Override
			public void widgetSelected(SelectionEvent e) {
     			boolean result = false;

                try {
                    if(changeRootStart != null){//if root has a new validity
                    	certificates.setNow(changeRootStart);
                    	certificates.setRoot(certificates.createCertificateNew(1,certificates.getRoot().getNotAfter()));
                    }
                    if(changeLevel2Start != null){//if level2 has a new validity
                    	certificates.setNow(changeLevel2Start);
                    	certificates.setLevel2(certificates.createCertificateNew(2,certificates.getLevel2().getNotAfter()));
                    }
                    if(changeUserStart != null){//if user has a new validity
                    	certificates.setNow(changeUserStart);
                    	certificates.setUser(certificates.createCertificateNew(3, certificates.getUser().getNotAfter()));
                    }
                    
                    if(changeRootEnd != null){//if root has a new validity
                    	certificates.setNow(certificates.getRoot().getNotBefore());
                    	certificates.setRoot(certificates.createCertificateNew(1,changeRootEnd));
                    }
                    if(changeLevel2End != null){//if level2 has a new validity
                    	certificates.setNow(certificates.getLevel2().getNotBefore());
              			certificates.setLevel2(certificates.createCertificateNew(2,changeLevel2End));
                    }
                    if(changeUserEnd != null){//if user has a new validity
                    	certificates.setNow(certificates.getUser().getNotBefore());
               			certificates.setUser(certificates.createCertificateNew(3,changeUserEnd));
                    }

                    if(changeTest==null && textValidDate.getText().length() > 0){
                    	changeTest = toDate(textValidDate.getText());
                    }
                    result=certificates.verify(changeTest);
                    
                    if (result) status = 1; 
                    else status = 2;
                    resultComp.redraw();

                 } catch (Exception ex) {
                     LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                 }

             }
         });
    }

    /**
     * Converts a date from a String to a Date and requests for the date to be checked
     * @param string - the date (in form of a string) to save
     * @return the string as a date
     */
    @SuppressWarnings("deprecation")
 	private Date toDate(final String string){
     	Date date=new Date();
     	
     	boolean empty = string.length() == 0;
     	
     	char[] temp=string.toCharArray();
     	int i=1;
     	int day=0,month = 0,year=0;

     	for(;i<string.length();i++){
     		if(i<=1){//day
     			day=((temp[i-1]-48)*10)+(temp[i]-48);
     			date.setDate(day);
     		}else if(i==4){//month
     			month=(((temp[i-1]-48)*10)+(temp[i]-48)-1);
     			date.setMonth(month);
     		}else if(i==9){//year
     			year=(((temp[i-3]-48)*1000)+((temp[i-2]-48)*100)+((temp[i-1]-48)*10)+(temp[i]-48)-1900);
     			date.setYear(year);
     		}
     	}
     	if(checkDate(day,(month+1),(year+1990))!=true || empty){
     		btnNewResult.setEnabled(false);
     	}else{
     		btnNewResult.setEnabled(true);
     	}
     	return date;
     }


    /**
     * checks whether a date is a valid date
     * @param day
     * @param month
     * @param year
     * @return true - if the given date exists
     */
    private boolean checkDate(int day, int month, int year) {
    	boolean result=true;

		if(month<1 || month>12){
			result=false;
		}
		/*months with 31 days*/
		if(month == 1 || month == 3 ||month == 5 ||month == 7 ||month == 8 ||month == 10 ||month == 12){
			if(day <1 || day>31){
				result=false;
			}
		}
		/*months with 30 days*/
		if(month == 4 || month == 6 ||month == 9 ||month == 11){
			if(day <1 || day >30){
				result=false;
			}
		}
		if(month==2 && leapyear(year)==true){
			if(day <1 || day >29){
				result=false;
				}
			}
		if(month==2 && leapyear(year)==false){
			if(day<1 || day >28){
				result=false;
			}
		}
		return result;
	}

    /**
     * checks whether a given year is a leap year
     * @param year
     * @return true if it's a leap year
     */
    public static boolean leapyear(int year){
		if(year%4 == 0 && (year%100 != 0 || year%400 == 0)){
			return true;
		}else{
			return false;
		}
	}

    /**
     * Converts a Date to a String (in order to be displayed on the user interface)
     * @param date to convert
     * @return the String
     */
    @SuppressWarnings("deprecation")
 	public String setFormat(Date date){
     	String result;

     	if(date.getDate()<=9){
     		result="0"+date.getDate()+".";
     	}else{
     		result=date.getDate()+".";
     	}
     	if(date.getMonth()<=9){
     		result+="0"+(date.getMonth()+1)+"."+(date.getYear()+1900)+" ";
     	}else{
     		result+=(date.getMonth()+1)+"."+(date.getYear()+1900)+" ";
     	}

   	  return result;
     }

	/**
	 * resets the certificates to the default certificates
	 * and resets the shown dates
	 */
	private void reset() {
    	btnNewResult.setEnabled(true);
        try {
        	certificates = new CertGeneration();
        	
			certificates.setRoot(certificates.createCertificate(1));
			certificates.setLevel2(certificates.createCertificate(2));
	 		certificates.setUser(certificates.createCertificate(3));

	 		temp=certificates.getEnd();
	 		
			dateRootEnd=setFormat(temp);
			lblrootChooseEnd.setText(dateRootEnd);

			dateLevel2End=setFormat(temp);
			lbllevel2ChooseEnd.setText(dateLevel2End);

			dateUserEnd=setFormat(temp);
			lbllevel3ChooseEnd.setText(dateUserEnd);
			
			temp = certificates.getNow();
			
			dateRootStart = setFormat(temp);
			lblrootChooseStart.setText(dateRootStart);
			
			dateLevel2Start = setFormat(temp);
			lbllevel2ChooseStart.setText(dateLevel2Start);
			
			dateUserStart = setFormat(temp);
			lbllevel3ChooseStart.setText(dateUserStart);
		} catch (Exception e) {
            LogUtil.logError(e);
		}
    }



    /**
     * Create the menu manager.
     *
     * @return the menu manager
     */
    /*
     * @Override protected MenuManager createMenuManager() { MenuManager menuManager = new
     * MenuManager("menu"); return menuManager; }
     */

    /**
     * Create the status line manager.
     *
     * @return the status line manager
     */
    /*
     * @Override protected StatusLineManager createStatusLineManager() { StatusLineManager
     * statusLineManager = new StatusLineManager(); return statusLineManager; } /** Return the
     * initial size of the window.
     */
    /*
     * @Override protected Point getInitialSize() { return new Point(1270, 750); }
     */

}
