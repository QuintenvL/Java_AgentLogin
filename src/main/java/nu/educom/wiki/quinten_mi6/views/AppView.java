package nu.educom.wiki.quinten_mi6.views;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import nu.educom.wiki.quinten_mi6.models.AppModel;

public class AppView {
	
	protected AppModel model;
	private JFrame frame;
	public ActionListener controller;
	
	/**
	 * Construct the AppView
	 * 
	 * @param AppModel model The model of the application
	 * @param JFrame frame the only view frame.
	 */
	public AppView (AppModel model, JFrame frame, ActionListener controller) {
		this.model = model;
		this.frame = frame;
		this.controller = controller;
		setupFrame();
	}
	
	private void setupFrame() {
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(FrameVariables.WIDTH, FrameVariables.HEIGHT));
		frame.setTitle("MI6 Application");
		frame.setBounds(100,100,FrameVariables.WIDTH, FrameVariables.HEIGHT);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private void rebuildFrame() {
		frame.revalidate();
		frame.repaint();
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Show the login screen with number and code inputs.
	 */
	public void loginScreen() {
		
		frame.getContentPane().removeAll();
		
		JLabel mainLabel = new JLabel("Enter your number and code:");
		mainLabel.setBounds(10, 25, 514, 36);
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainLabel.setFont(FrameVariables.MAIN_FONT);
		frame.getContentPane().add(mainLabel);
		
		model.numberField.setFont(FrameVariables.MAIN_FIELD_FONT);
		model.numberField.setBounds(186, 98, 234, 30);
		model.numberField.setColumns(FrameVariables.NUMBER_OF_COLUMNS);
		frame.getContentPane().add(model.numberField);
		
		model.codeField.setFont(FrameVariables.MAIN_FIELD_FONT);
		model.codeField.setBounds(186, 139, 234, 30);
		frame.getContentPane().add(model.codeField);
		
		JLabel numberLabel = new JLabel("Number:");
		numberLabel.setFont(FrameVariables.MAIN_FIELD_FONT);
		numberLabel.setBounds(93, 106, 83, 14);
		frame.getContentPane().add(numberLabel);
		
		JLabel codeLabel = new JLabel("Code:");
		codeLabel.setFont(FrameVariables.MAIN_FIELD_FONT);
		codeLabel.setBounds(118, 147, 58, 14);
		frame.getContentPane().add(codeLabel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(10, 215, 514, 37);
		frame.getContentPane().add(buttonsPanel);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		submitButton.setFont(FrameVariables.MAIN_BUTTON_FONT);
		submitButton.addActionListener(controller);
		buttonsPanel.add(submitButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		exitButton.setFont(FrameVariables.MAIN_BUTTON_FONT);
		exitButton.addActionListener(controller);
		buttonsPanel.add(exitButton);
		
		rebuildFrame();
	}
	
	/**
	 * Show the successful log in message.
	 */
	public void loggedIn() {
		frame.getContentPane().removeAll();
		
		JLabel mainLabel = new JLabel(String.format("Welcome agent %03d!", model.dbAgent.agentNumber));
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainLabel.setFont(FrameVariables.BOLD_MAIN_FONT);
		mainLabel.setBounds(10, 11, 514, 33);
		frame.getContentPane().add(mainLabel);
		
		JLabel licenseLabel = new JLabel(model.licenseMessage);
		licenseLabel.setBounds(20, 55, 504, 17);
		licenseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		licenseLabel.setForeground(FrameVariables.INFO_COLOR);
		licenseLabel.setFont(FrameVariables.NORMAL_FONT);
		frame.getContentPane().add(licenseLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 92, 514, 161);
		frame.getContentPane().add(scrollPane);
		
		JLabel listHeaderLabel = new JLabel("Your latest login attempt(s):");
		listHeaderLabel.setFont(FrameVariables.ITALIC_NORMAL_FONT);
		listHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(listHeaderLabel);
		
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		JList<String> attemptList = new JList<String>(dlm);
		attemptList.setEnabled(false);
		attemptList.setFont(FrameVariables.NORMAL_FONT);
		attemptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListCellRenderer renderer =  (DefaultListCellRenderer)attemptList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		for (LocalDateTime i:model.latestAttempts) {
			dlm.addElement(i.format(formatter));
		}
		scrollPane.setViewportView(attemptList);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(10, 266, 514, 34);
		frame.getContentPane().add(buttonPanel);
		
		JButton exitButton = new JButton("Log out");
		exitButton.setFont(FrameVariables.MAIN_BUTTON_FONT);
		exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		exitButton.addActionListener(controller);
		buttonPanel.add(exitButton);
		
		rebuildFrame();
	}
	
	/**
	 * Show the error message to the user.
	 * 
	 */
	public void error() {
		
		frame.getContentPane().removeAll();
		
		JLabel mainLabel = new JLabel("ACCESS DENIED");
		mainLabel.setFont(FrameVariables.MAIN_FONT);
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainLabel.setForeground(FrameVariables.ERROR_COLOR);
		mainLabel.setBounds(10, 37, 514, 49);
		frame.getContentPane().add(mainLabel);
		
		String labelText = "Try again in "+ model.remainingCoolOffMinutes+" minute(s) and "+ model.remainingCoolOffSeconds+" second(s).";
		JLabel coolOffLabel = new JLabel(labelText);
		coolOffLabel.setFont(FrameVariables.NORMAL_FONT);
		coolOffLabel.setHorizontalAlignment(SwingConstants.CENTER);
		coolOffLabel.setBounds(10, 110, 514, 26);
		if (model.remainingCoolOffMinutes != 0 || model.remainingCoolOffSeconds != 0) {
			frame.getContentPane().add(coolOffLabel);
		}
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(10, 169, 514, 33);
		frame.getContentPane().add(buttonsPanel);
		
		JButton retryButton = new JButton("Retry");
		retryButton.setFont(FrameVariables.MAIN_BUTTON_FONT);
		retryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		retryButton.addActionListener(controller);
		buttonsPanel.add(retryButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.setFont(FrameVariables.MAIN_BUTTON_FONT);
		exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		exitButton.addActionListener(controller);
		buttonsPanel.add(exitButton);
		
		rebuildFrame();
	}
	
}
