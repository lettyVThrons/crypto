package org.jcryptool.visual.merkletree.ui;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.MultiTree;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite with the Seed in Tabpage 1
 * 
 * @author Fabian Mayer
 * @author <i>revised by</i>
 * @author Maximilian Lindpointner
 * 
 *
 */
public class MerkleTreeGeneration extends Composite {

	private ViewPart masterView;
	private SUIT mode;

	private byte[] seedarray;
	private byte[] bitmaskSeedarray;

	private Composite groupMaster;
	private Button buttonCreateSeed;
	private Label randomgenerator;
	private Text textSeed;
	private int wParameter = 16;

	private int spinnerValue;

	// w Parameter Box
	private Button buttonSet4;
	private Button buttonSet16;
	private Label titleLabel;
	private StyledText wParamDescription;

	// Generate Keys Box
	private Button buttonCreateKeys;
	private Label createLabel;
	private Label createdKey;
	private StyledText generateKeyDescription;
	private Spinner keypairSpinner;

	private MessageBox successBox;
	private int allowedIndex;
	private Label trees;
	private Combo multiTreeCombo;
	private int[][] multiTreeArguments;
	private int multiTreeArgumentIndex;
	private Label multitreeAmountLabel;

	private Group bitmaskGroup;
	private Button bitmaskButton;
	private Label bitmaskLabel;
	private StyledText bitmaskDescription;
	private Text bitmaskText;
	private Label bitmaskRandomgenerator;

	/**
	 * Create the composite. Including Seed content and KeyPairComposite
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeGeneration(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.mode = mode;
		this.masterView = masterView;

		groupMaster = new Composite(this, SWT.NONE);
		groupMaster.setLayout(new GridLayout(8, true));
		groupMaster.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		/**
		 * (non-javadoc)
		 * 
		 * Seed Box
		 * 
		 */
		Group group = new Group(groupMaster, SWT.NONE);
		group.setText(Descriptions.Tab0_Head1);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		group.setLayout(new GridLayout(10, true));
		group.setFont(FontService.getNormalBoldFont());

		/*
		 * Seed Label
		 */
		randomgenerator = new Label(group, SWT.NONE);
		randomgenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		// randomgenerator.setText(Descriptions.Tab0_Head1);

		/*
		 * Textbox for seed initiates textbox with a seed
		 */
		textSeed = new Text(group, SWT.BORDER | SWT.CENTER);
		textSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		seedarray = generateNewSeed();
		textSeed.setText(Converter._byteToHex(seedarray));

		/*
		 * Button generate new Seed
		 */
		buttonCreateSeed = new Button(group, SWT.NONE);
		buttonCreateSeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		buttonCreateSeed.setText(Descriptions.Tab0_Button0);

		buttonCreateSeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				seedarray = generateNewSeed();
				textSeed.setText(Converter._byteToHex(seedarray));
				((MerkleTreeView) masterView).updateElement();
			}
		});

		/*
		 * if XMSS or XMSS_MT is selected, also the Bitmask is requiered
		 * therefore the Bitmask Box is injected
		 */

		/**
		 * (non-javadoc)
		 * 
		 * Bitmask Seed
		 * 
		 */
		if (mode == SUIT.XMSS || mode == SUIT.XMSS_MT) {
			bitmaskGroup = new Group(groupMaster, SWT.NONE);
			bitmaskGroup.setText(Descriptions.Tab0_Head5);
			bitmaskGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			bitmaskGroup.setLayout(new GridLayout(10, true));
			bitmaskGroup.setText(Descriptions.Tab0_Head3);
			bitmaskGroup.setFont(FontService.getNormalBoldFont());

			bitmaskDescription = new StyledText(bitmaskGroup, SWT.WRAP | SWT.BORDER);
			bitmaskDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 2));
			bitmaskDescription.setText(Descriptions.Tab0_Txt3);
			bitmaskDescription.setEditable(false);
			bitmaskDescription.setCaret(null);

			/*
			 * Bitmask Seed Label
			 */
			bitmaskRandomgenerator = new Label(bitmaskGroup, SWT.NONE);
			bitmaskRandomgenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			bitmaskRandomgenerator.setText(Descriptions.Tab0_Head4);

			/*
			 * Textbox for seed
			 */
			bitmaskText = new Text(bitmaskGroup, SWT.BORDER | SWT.CENTER);
			bitmaskText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

			/*
			 * Button generate new Seed
			 */
			bitmaskButton = new Button(bitmaskGroup, SWT.NONE);
			bitmaskButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
			bitmaskButton.setText(Descriptions.Tab0_Button1);

			bitmaskSeedarray = generateNewSeed();
			bitmaskText.setText(Converter._byteToHex(bitmaskSeedarray));

			bitmaskButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					bitmaskSeedarray = generateNewSeed();
					bitmaskText.setText(Converter._byteToHex(bitmaskSeedarray));
					((MerkleTreeView) masterView).updateElement();
				}
			});
		}

		/**
		 * (non-javadoc)
		 * 
		 * Winternitz Parameter Box
		 * 
		 */

		Group wParameterGroup = new Group(groupMaster, SWT.NONE);
		wParameterGroup.setText(Descriptions.Tab0_Head5);
		wParameterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		GridLayout wParameterGroupLayout = new GridLayout(8, true);
		wParameterGroupLayout.verticalSpacing = 13;
		wParameterGroup.setLayout(wParameterGroupLayout);
		wParameterGroup.setFont(FontService.getNormalBoldFont());

		// text box with Description
		wParamDescription = new StyledText(wParameterGroup, SWT.WRAP | SWT.BORDER);
		wParamDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		wParamDescription.setEditable(false);
		wParamDescription.setCaret(null);

		// Radio Buttons 4/16
		buttonSet4 = new Button(wParameterGroup, SWT.RADIO);
		buttonSet4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet4.setText("4");

		buttonSet16 = new Button(wParameterGroup, SWT.RADIO);
		buttonSet16.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet16.setText("16");
		buttonSet16.setSelection(true);

		buttonSet4.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 4;
				((MerkleTreeView) masterView).updateElement();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		buttonSet16.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 16;
				((MerkleTreeView) masterView).updateElement();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		/**
		 * (non-javadoc)
		 * 
		 * Key Generation Box
		 * 
		 */

		Group generateKeyGroup = new Group(groupMaster, SWT.NONE);
		generateKeyGroup.setText(Descriptions.Tab0_Head2);
		generateKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		GridLayout generateKeyGroupLayout = new GridLayout(1, true);
		generateKeyGroupLayout.verticalSpacing = 13;
		generateKeyGroup.setLayout(generateKeyGroupLayout);
		generateKeyGroup.setFont(FontService.getNormalBoldFont());

		// text
		generateKeyDescription = new StyledText(generateKeyGroup, SWT.WRAP | SWT.BORDER);
		generateKeyDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));
		generateKeyDescription.setCaret(null);
		generateKeyDescription.setEditable(false);
		//
		Composite keyRow = new Composite(generateKeyGroup, SWT.NONE);
		GridLayout keyRowLayout = new GridLayout(16, true);
		keyRowLayout.verticalSpacing = 13;
		keyRow.setLayout(keyRowLayout);
		keyRow.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 8, 1));

		// text - for the spinner

		// spinner for the key-ammount
		if (mode != SUIT.XMSS_MT) {
			Label keysum = new Label(keyRow, SWT.NONE);
			keysum.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
			keysum.setText(Descriptions.Tab0_Lable1);

			keypairSpinner = new Spinner(keyRow, SWT.BORDER);
			keypairSpinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			keypairSpinner.setMinimum(2);
			keypairSpinner.setMaximum(64);
			keypairSpinner.setSelection(8);
			spinnerValue = 8;

			keypairSpinner.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					int selection = keypairSpinner.getSelection();
					//
					if (selection < spinnerValue) {
						keypairSpinner.setSelection(spinnerValue / 2);
					} else {
						keypairSpinner.setSelection(spinnerValue * 2);
					}
					spinnerValue = keypairSpinner.getSelection();
					((MerkleTreeView) masterView).updateElement();

				}
			});
		} else {
			multiTreeArguments = new int[][] { /* { 1, 16 }, */ { 3, 16 },
					/* { 1, 64 }, */ { 3, 64 }, { 4, 64 } };

			Label multiTreeAmountDescription = new Label(keyRow, SWT.NONE);
			multiTreeAmountDescription.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
			multiTreeAmountDescription.setText(Descriptions.Tab0_Lable1);

			multitreeAmountLabel = new Label(keyRow, SWT.NONE);
			multitreeAmountLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			multitreeAmountLabel.setText(String.valueOf(multiTreeArguments[0][1]));

		}

		// Text box with generated key info
		createdKey = new Label(keyRow, SWT.NONE);
		createdKey.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 5, 2));
		createdKey.setText(Descriptions.MerkleTreeKey_1);

		// 'create button'
		buttonCreateKeys = new Button(keyRow, SWT.NONE);
		buttonCreateKeys.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 5, 2));
		buttonCreateKeys.setFont(FontService.getNormalBoldFont());

		if (mode == SUIT.XMSS_MT) {
			Label keysum = new Label(keyRow, SWT.NONE);
			keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			keysum.setText(Descriptions.Tab0_Head6);
			multiTreeCombo = new Combo(keyRow, SWT.READ_ONLY);
			multiTreeCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));

			// multiTreeCombo.add(Descriptions.Tab0_MT_1);
			multiTreeCombo.add(Descriptions.Tab0_MT_2);
			// multiTreeCombo.add(Descriptions.Tab0_MT_3);
			multiTreeCombo.add(Descriptions.Tab0_MT_4);
			multiTreeCombo.add(Descriptions.Tab0_MT_5);

			multiTreeCombo.select(0);

			multiTreeCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					multiTreeArgumentIndex = multiTreeCombo.getSelectionIndex();
					multitreeAmountLabel.setText(String.valueOf(multiTreeArguments[multiTreeArgumentIndex][1]));
					((MerkleTreeView) masterView).updateElement();
				}
			});
		}

		// setting the text's depending on the actual suite

		// createLabel.setText(Descriptions.Tab0_Head2);

		// MessageBox when successfully creating a Key
		successBox = new MessageBox(this.getShell(), SWT.ICON_WORKING | SWT.OK);
		successBox.setMessage(Descriptions.MerkleTreeKey_3);

		switch (mode) {
		case XMSS:
			generateKeyDescription.setText(Descriptions.XMSS.Tab0_Txt2);
			wParamDescription.setText(Descriptions.XMSS.Tab0_Txt1);
			buttonCreateKeys.setText(Descriptions.XMSS.Tab0_Key_Button);
			successBox.setText(Descriptions.XMSS.Tab0_MessageBox0);
			break;
		case XMSS_MT:
			generateKeyDescription.setText(Descriptions.XMSS_MT.Tab0_Txt2);
			wParamDescription.setText(Descriptions.XMSS_MT.Tab0_Txt1);
			group.setText(Descriptions.XMSS_MT.Tab0_Seed0);
			bitmaskGroup.setText(Descriptions.XMSS_MT.Tab0_Seed1);
			bitmaskButton.setText(Descriptions.XMSS_MT.Tab0_Seed2);
			buttonCreateSeed.setText(Descriptions.XMSS_MT.Tab0_Seed3);
			bitmaskRandomgenerator.setText("");
			bitmaskDescription.dispose();
			buttonCreateKeys.setText(Descriptions.XMSS_MT.Tab0_Key_Button);
			successBox.setText(Descriptions.XMSS_MT.Tab0_MessageBox0);

			this.layout();
			break;
		case MSS:
		default:
			generateKeyDescription.setText(Descriptions.MSS.Tab0_Txt2);
			wParamDescription.setText(Descriptions.MSS.Tab0_Txt1);
			buttonCreateKeys.setText(Descriptions.MSS.Tab0_Key_Button);
			successBox.setText(Descriptions.MSS.Tab0_MessageBox0);
			break;
		}

		/**
		 * Event Listener for the generate keys button if this button is pressed
		 * a new merkle tree is generated
		 */
		buttonCreateKeys.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc) generates the MerkleTree and the KeyPairs
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateMerkleTree();
				successBox.open();
				((MerkleTreeView) masterView).removeFocus();
			}

		});

		// Listener to set "neutral" focus on current view, where you can scroll
		// and click everything
		Listener setLocalFocus = new Listener() {
			@Override
			public void handleEvent(Event event) {
				((MerkleTreeView) masterView).removeFocus();
			}
		};
		// Gets all groups in this tab and adds above listener
		Control groupChildren[] = groupMaster.getChildren();

		for (int i = 0; i < groupChildren.length; ++i) {
			if (groupChildren[i] instanceof Group) {
				groupChildren[i].addListener(SWT.MouseUp, setLocalFocus);
			}
		}

	}

	ISimpleMerkle merkle;

	public ISimpleMerkle generateMerkleTree() {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {

				/*
				 * select the type of suite
				 */
				switch (mode) {
				case XMSS:
					merkle = new XMSSTree();
					break;
				case XMSS_MT:
					merkle = new MultiTree();
					break;
				case MSS:
				default:
					merkle = new SimpleMerkleTree();
					break;
				}

				/*
				 * create the merkle tree with the chosen values
				 */
				// if the generated Tree is a XMSSTree -> the
				// bitmaskseed is also needed
				if (mode == SUIT.XMSS) {
					((XMSSTree) merkle).setBitmaskSeed(bitmaskSeedarray);
				}
				if (mode == SUIT.XMSS_MT) {
					merkle.setLeafCount(multiTreeArguments[multiTreeArgumentIndex][1]);
					((MultiTree) merkle).setSingleTreeHeight(multiTreeArguments[multiTreeArgumentIndex][0]);
					((MultiTree) merkle).setSKSeed(seedarray);
					((MultiTree) merkle).setPKSeed(bitmaskSeedarray);
				} else {
					merkle.setLeafCount(spinnerValue);
					merkle.setSeed(seedarray);
				}

				// merkle.setLeafCount(spinnerValue);
				merkle.setWinternitzParameter(wParameter);
				merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
				merkle.generateKeyPairsAndLeaves();
				merkle.generateMerkleTree();
				((MerkleTreeView) masterView).setAlgorithm(merkle, mode);

				// set or update the key information
				createdKey.setText(Descriptions.MerkleTreeKey_2 + " " + Converter._numberToPrefix(merkle.getKeyLength()));

			}
		});
		return merkle;
	}

	/**
	 * generates a new random seed
	 * 
	 * @return random seed
	 */
	private byte[] generateNewSeed() {
		SecureRandom secureRandomGenerator = new SecureRandom();
		byte[] randomBytes = new byte[128];
		secureRandomGenerator.nextBytes(randomBytes);
		// set the seed length
		int seedByteCount = 16;
		byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
		return seed;
	}

}
