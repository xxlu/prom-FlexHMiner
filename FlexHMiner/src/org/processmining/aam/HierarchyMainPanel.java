package org.processmining.aam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.processmining.aam.model.ActTreeNode;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMiner;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMiner.InductiveVisualMinerLauncher;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.DfgMiner;
import org.processmining.processtree.ProcessTree;
import org.processmining.xlu.experiment.discovery.MarkedPetrinet;
import org.processmining.xlu.relabel.plugin.hyber.RelabelHyberApproachIM;
import org.processmining.xlu.relabel.visualization.GraphvizPetriNet;

import com.fluxicon.slickerbox.factory.SlickerDecorator;

import info.clearthought.layout.TableLayout;

public class HierarchyMainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7233392679672455924L;

	private PluginContext context;
	//	private InductiveVisualMiner miner = new InductiveVisualMiner();
	private JComponent inductiveVisMinerPanel;

	private Map<JSlider, JLabel> mapSlider2Label;

	private	HierarchyControllerModel controller;

	private static Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	private static Border paddingWidthBorder = BorderFactory.createEmptyBorder();

	
	ActTreeNode model;
	public HierarchyMainPanel(ActTreeNode model) {
		this.model = model;
		controller = new HierarchyControllerModel(model);
		/* ---------------------------------
		 * Create controller
		 * --------------------------------- */
//		if (model instanceof RelabelControllerModel) {
//			this.controller = (RelabelControllerModel) model;
//		} else {
//			this.controller = new RelabelControllerModel(model.getRelabelModel(),
//					model.getRelabelScope(), model.getRelabelParam());
//		}

//		this.mapSlider2Label = new THashMap<JSlider, JLabel>();		
	}

	public HierarchyMainPanel(PluginContext context, ActTreeNode model) {
		this(model);
		this.context = context;
		initLayout();
		addComponentControllerPanel();
		addComponentInductiveVisualMinerPanel();
	}

	private void initLayout() {
		TableLayout mainLayout = new TableLayout(
				new double[][] { { 300, TableLayout.FILL }, { TableLayout.FILL } });
		setLayout(mainLayout);
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(Color.DARK_GRAY);
		setForeground(Color.WHITE);
	}

	private void addComponentInductiveVisualMinerPanel() {
		inductiveVisMinerPanel = new JPanel();
		// Add layout, otherwise panel not maximized to parent container
		inductiveVisMinerPanel.setLayout(new BorderLayout());
		inductiveVisMinerPanel.setBackground(Color.GRAY);
		inductiveVisMinerPanel.setForeground(Color.WHITE);

//		RelabelHyberApproachIM miner = new RelabelHyberApproachIM();
//
//		MarkedPetrinet net = miner.minePetrinet(context, model.getOrigLog());
//
//		Dot dot = GraphvizPetriNet.convert(net.getPetrinet(), net.getInitialMarking(),
//				net.getFinalMarking());
//		NavigableSVGPanel panel = new AnimatableSVGPanel(DotPanel.dot2svg(dot));
//		DotPanel paneldot = getPanelOfModel(model.getOrigLog());
//		inductiveVisMinerPanel.add(paneldot);
		updateNewModel();
		this.add(inductiveVisMinerPanel, "1,0");
	}

	private void addComponentControllerPanel() {
		JComponent panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.WHITE);

		/* ---------------------------------
		 * add choice for local or global
		 * --------------------------------- */
//		JRadioButton b1 = new JRadioButton("Global");
//		b1.setBackground(Color.DARK_GRAY);
//		b1.setForeground(Color.WHITE);
//		b1.setActionCommand("Global");
//		b1.setSelected(true);
//		b1.addActionListener(getRadioButtonListener());
//		JRadioButton b2 = new JRadioButton("Local");
//		b2.setBackground(Color.DARK_GRAY);
//		b2.setForeground(Color.WHITE);
//		b2.setActionCommand("Local");
//		b2.addActionListener(getRadioButtonListener());
//		//Group the radio buttons.
//		ButtonGroup group = new ButtonGroup();
//		group.add(b1);
//		group.add(b2);
//		Box boxContainer = Box.createHorizontalBox();
//		JLabel l = createLabel(panel, "Choose Mode: ");
//		boxContainer.add(l);
//		boxContainer.add(b1);
//		boxContainer.add(b2);
//		boxContainer.add(Box.createHorizontalGlue());
//		boxContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.add(boxContainer);

//		/* ---------------------------------------------------------------------
//		 *  Add label and slider for number of clusters. 
//		 * -------------------------------------------------------------------- */
////		final String description = "Set Unfolding Threshold: ";
//		int initValue = param.getClusterParam().getNumOfClusters();
//		final JLabel numClsLabel = createLabel(panel, "Set num of Cls.");
//		Box boxContainer = Box.createHorizontalBox();
//		boxContainer.add(numClsLabel);
//		JLabel labelValue = createLabel(panel, String.valueOf(initValue));
//		boxContainer.add(labelValue);
//		boxContainer.add(Box.createHorizontalGlue());
//		boxContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.add(boxContainer);
//
//		ChangeListener c = getNumClsChangeListener(labelValue);
//
//		// Add slider
//		JSlider sliderNumClsThreshold = createSlider(panel, c, 1, 100, initValue);
//		sliderNumClsThreshold.setBorder(paddingBorder);
//		panel.add(sliderNumClsThreshold);

		/* ---------------------------------------------------------------------
		 *  Add imprecise label selector. 
		 * -------------------------------------------------------------------- */

		/* ---------------------------------------------------------------------
		 *  Add label and slider for max cost of edges between events. 
		 * -------------------------------------------------------------------- */

//		RelabelImpreciseLabelCandidateView view =  getVisualPanel()
		panel.add( getVisualPanel());
		
		

////		final String description = "Set Unfolding Threshold: ";
//		int initValue = controller.getMaxCostOfEdge();
//		final JLabel maxNumEdgeCost = createLabel(panel, "Set Max. Cost of Edges: ");
//		Box boxContainer = Box.createHorizontalBox();
//		boxContainer.add(maxNumEdgeCost);
//		JLabel labelCost = createLabel(panel, String.valueOf(initValue));
//		boxContainer.add(labelCost);
//		boxContainer.add(Box.createHorizontalGlue());
//		boxContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.add(boxContainer);
//
//		final JLabel maxNumEdgeCostExplain = createHelpLabel(panel,
//				"(Left: more choices | Right: less choices.)");
////		boxContainer = Box.createHorizontalBox();
////		boxContainer.add(maxNumEdgeCostExplain);
////		boxContainer.add(Box.createHorizontalGlue());
////		boxContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.add(maxNumEdgeCostExplain);
//
//		ChangeListener c = getCostChangeListener(labelCost);
//		// Add slider
//		JSlider sliderMaxCostThreshold = createSlider(panel, c, 0, 101, initValue);
//		sliderMaxCostThreshold.setBorder(paddingBorder);
//		panel.add(sliderMaxCostThreshold);


		/* ---------------------------------------------------------------------
		 *  Add separator
		 * -------------------------------------------------------------------- */

//		final JLabel advancedConfig = createLabel(panel,
//				"--- For Advanced User (temp) ---");
//		boxContainer = Box.createHorizontalBox();
//		boxContainer.add(advancedConfig);
//		boxContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.add(boxContainer);
//		/* ---------------------------------------------------------------------
//		 *  Add label and slider for each event class. 
//		 * -------------------------------------------------------------------- */
//		for (final XEventClass evc : data.model.getMapLabel2Graph().keySet()) {
//			final String textlabel = "Select for " + evc.toString() + " : ";
//			int currentValue = 0;
//
//			JLabel description1 = createLabel(panel, textlabel);
//			Box boxContainer1 = Box.createHorizontalBox();
//			boxContainer1.add(description1);
//			JLabel label3 = createLabel(panel, String.valueOf(currentValue));
//			boxContainer1.add(label3);
//			boxContainer1.add(Box.createHorizontalGlue());
//			boxContainer1.setAlignmentX(Component.LEFT_ALIGNMENT);
//			panel.add(boxContainer1);
//
//			ChangeListener cc = getEventClassChangeListener(evc);
//			JSlider slider = createSlider(panel, cc, 0, 10, currentValue);
//			panel.add(slider);
//			slider.setBorder(paddingBorder);
//			this.mapSlider2Label.put(slider, label3);
//		}

//		RelabelVisSetting settings = new RelabelVisSetting(controller, this);
//		panel.add(settings.getPanel());
		this.add(panel, "0,0");
	}

//	protected static JComponent packLeftAligned(JComponent component) {
//		JPanel packed = new JPanel();
//		packed.setOpaque(false);
//		packed.setBorder(paddingBorder);
//		packed.setLayout(new BoxLayout(packed, BoxLayout.X_AXIS));
//		packed.add(component);
//		packed.add(Box.createHorizontalGlue());
//		return packed;
//	}

//	private ActionListener getRadioButtonListener() {
//		return new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				type = e.getActionCommand();
//			}
//		};
//	}

//	private ChangeListener getCostChangeListener(final JLabel labelCost) {
//		return new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				JSlider source = (JSlider) e.getSource();
//				int threshold = source.getValue();
//				labelCost.setText(String.valueOf(threshold));
//				if (!source.getValueIsAdjusting()) {
//					// ApplyThreshold
//					controller.setMaxCostOfEdge(threshold);
//					updateNewModel();
//				}
//			}
//		};
//	}

//	private ChangeListener getNumClsChangeListener(final JLabel labelValue) {
//		return new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				JSlider source = (JSlider) e.getSource();
//				if (!source.getValueIsAdjusting()) {
//					int threshold = source.getValue();
//					labelValue.setText(String.valueOf(threshold));
//
//					// ApplyThreshold
//					controller.setNumOfClusters(threshold);
//					updateNewModel();
//				}
//			}
//		};
//	}



//	private ChangeListener getEventClassChangeListener(final XEventClass evc) {
//		return new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				JSlider source = (JSlider) e.getSource();
//				if (!source.getValueIsAdjusting()) {
//					JLabel label = mapSlider2Label.get(source);
//					int threshold = source.getValue();
//					label.setText(String.valueOf(threshold));
//
//					// ApplyThreshold
//					controller.setEventClassNumb(evc, threshold);
//					updateNewModel();
//				}
//			}
//		};
//	}
	
	JTree visTree;
	
	public JComponent getVisualPanel() {
		//		Tree<RepEGraph> hierarchy = graphs.getHierarchy();
		//		if(hierarchy != null){
		
//		JTree visTree = new JTree(this.model);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.WHITE);

//		JLabel labelCost = RelabelMainPanel.createLabel(panel,
//				"Select the cluster to view: ");
//		labelCost.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.add(labelCost);

		if (visTree == null) {
			visTree = new JTree(this.model);

			//			visTree.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			visTree.setBackground(Color.GRAY);
			visTree.setForeground(Color.WHITE);

			visTree.getSelectionModel()
					.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

			//Listen for when the selection changes.
			visTree.addTreeSelectionListener(new TreeSelectionListener() {

				@SuppressWarnings("unchecked")
				public void valueChanged(TreeSelectionEvent e) {
					//Returns the last path element of the selection.
					//This method is useful only when the selection model allows a single selection.
					ActTreeNode node = (ActTreeNode) visTree
							.getLastSelectedPathComponent();

//					updateSelectedGraphs(node, isShowSubcluster);
					controller.setLog(node.getSubLog());
					updateNewModel();
				}
			});

//			visTree.setCellRenderer(new MyTreeCellRenderer());
			
//			ClusterExpanderBlockStructure expander = new ClusterExpanderBlockStructure(log);
//			expander.expandTree(visTree,hierarchy);
		}

		visTree.setAlignmentX(Component.LEFT_ALIGNMENT);
		//		visTree.setBorder(BorderFactory.createEmptyBorder());
		JScrollPane treeView = new JScrollPane(visTree);
		//		treeView.setMaximumSize(new Dimension(1000, 300));
		treeView.setBorder(BorderFactory.createEmptyBorder());
		treeView.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(treeView);

//		SlickerButton button = new SlickerButton("Export Selected Cluster");
//		button.setAlignmentX(Component.LEFT_ALIGNMENT);
//		//		SlickerDecorator.instance().decorate(button);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				XLog newLog = getFilteredLog();
//				controller.getContext().getProvidedObjectManager().createProvidedObject(
//						"Cluster Log", newLog, XLog.class, controller.getContext());
//			}
//		});

//		SlickerButton bMineModel = new SlickerButton("Show mining model with relabel");
//		bMineModel.setAlignmentX(Component.LEFT_ALIGNMENT);
//		bMineModel.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				XLog newLog = getFilteredLog();
//
//				controller.updateRelabel(newLog);
//			}
//		});

		//		JButton dfgbutton = new JButton("Show cluster as DFG");
		//		button.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		////				XLog newLog = getFilteredLog();
		////				XLog2Dfg.log2Dfg();
		//			}
		//		});
		//		

//		final JCheckBox box = new JCheckBox("Show child-clusters");
//		box.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				Tree<RepEGraph> node = (Tree<RepEGraph>) visTree
//						.getLastSelectedPathComponent();
//
//				if (((JCheckBox) e.getSource()).isSelected()) {
//					isShowSubcluster = true;
//				} else {
//					isShowSubcluster = false;
//				}
//				updateSelectedGraphs(node, isShowSubcluster);
//				controller.update();
//			}
//		});
//		box.setSelected(isShowSubcluster);
//		SlickerDecorator.instance().decorate(box);
//		box.setBackground(Color.DARK_GRAY);
//		box.setForeground(Color.WHITE);
//		box.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//		panel.add(button);
////		panel.add(bMineModel);
//		panel.add(box);
		//		panel.add(dfgbutton, BorderLayout.PAGE_END);

		return panel;
	}

	public void updateNewModel() {
		if (inductiveVisMinerPanel != null) {

			inductiveVisMinerPanel.removeAll();

			Object object = controller.computeNewProcessModel(context);
			JComponent component = null;
			if (object instanceof InductiveVisualMinerLauncher) {
				InductiveVisualMiner ivm = new InductiveVisualMiner();
				
				InductiveVisualMinerLauncher launcher = (InductiveVisualMinerLauncher) object;
//				launcher.setMiner(VisualMinerWrapperPluginFinder.);
				launcher.setMiner(new DfgMiner());
				if(context != null){
					context.getProvidedObjectManager().createProvidedObject("laucher", launcher, InductiveVisualMinerLauncher.class, context);
				}
				component = ivm.visualise(context, launcher,
						ProMCanceller.NEVER_CANCEL);
				
			} else {
				component = getPanelOfModel(this.context, object);
			}
			component.setPreferredSize(new Dimension(500, 500));
			component.setMaximumSize(new Dimension(500, 500));
			inductiveVisMinerPanel.add(component);
			inductiveVisMinerPanel.revalidate();
			inductiveVisMinerPanel.repaint();
		}
	}

	public static DotPanel getPanelOfModel(PluginContext context, Object object) {

		MarkedPetrinet net = null;
		if (object instanceof ProcessTree) {
			if (context == null) {
				net = RelabelHyberApproachIM.convertTree((ProcessTree) object);
			} else {
				net = RelabelHyberApproachIM.convertTreeInContext(context,
						(ProcessTree) object);
			}
		} else if (object instanceof MarkedPetrinet) {
			net = (MarkedPetrinet) object;
		}
		Dot dot = GraphvizPetriNet.convert(net.getPetrinet(), net.getInitialMarking(),
				net.getFinalMarking());
//		NavigableSVGPanel panel = new AnimatableSVGPanel(DotPanel.dot2svg(dot));
		DotPanel paneldot = new DotPanel(dot);
		return paneldot;
	}

	public static JLabel createLabel(JComponent panel, final String textlabel) {
		final JLabel labelUnfoldingThreshold = new JLabel(textlabel);
		SlickerDecorator.instance().decorate(labelUnfoldingThreshold);
		labelUnfoldingThreshold.setForeground(panel.getForeground());
		labelUnfoldingThreshold.setBorder(paddingBorder);
		labelUnfoldingThreshold.setAlignmentX(Component.LEFT_ALIGNMENT);
		return labelUnfoldingThreshold;
	}

	public static JLabel createHelpLabel(JComponent panel, final String textlabel) {
		final JLabel labelUnfoldingThreshold = new JLabel(textlabel);
		SlickerDecorator.instance().decorate(labelUnfoldingThreshold);
		labelUnfoldingThreshold.setForeground(Color.GRAY);
		labelUnfoldingThreshold.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		labelUnfoldingThreshold.setAlignmentX(Component.LEFT_ALIGNMENT);
		return labelUnfoldingThreshold;
	}

	private static JSlider createSlider(JComponent panel, ChangeListener c, int min,
			int max, int init) {
		JSlider slider = new JSlider(min, max, init);
		slider.addChangeListener(c);
//		SlickerDecorator.instance().decorate(sliderUnfoldingThreshold);
		slider.setForeground(panel.getForeground());
		slider.setBackground(panel.getBackground());
		slider.setBorder(paddingBorder);
		slider.setAlignmentX(Component.LEFT_ALIGNMENT);
		return slider;
	}

}
