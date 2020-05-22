package nu.educom.wiki.quinten_mi6.controllers;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.JFrame;

import nu.educom.wiki.quinten_mi6.ValidationException;
import nu.educom.wiki.quinten_mi6.models.AppModel;
import nu.educom.wiki.quinten_mi6.cruds.AgentCrud;
import nu.educom.wiki.quinten_mi6.cruds.Crud;
import nu.educom.wiki.quinten_mi6.views.AppView;
import nu.educom.wiki.quinten_mi6.views.ConsoleLogger;

public class AppController implements ActionListener {

	public AppModel model;
	public JFrame frame;
	public Crud crud;
		
	/**
	 * The construct of the AppController when it is created for the first time.
	 */
	public AppController(Crud c) {
		this.model = new AppModel();
		this.frame = new JFrame();
		this.crud = c;
	}
	
	@Override
	/**
	 * Handles which action should be performed in the model and which view should be shown.
	 * By default the login screen will be shown.
	 * 
	 * @param ActionEvent e The event triggered by hitting a button. 
	 */
	public void actionPerformed(ActionEvent e) {
		AppView view = new AppView(model, frame, this);
		String action = e != null ?e.getActionCommand(): "";
		try {
			switch (action) {
				case "Submit":
					verifyAgent();
					prepareLoginScreen();
					view.loggedIn();
					break;
				case "Exit":
					System.exit(0);
					break;
				default:
					model.resetCoolOffVariables();
					model.resetFields();
					view.loginScreen();
					break;
			}
		}
		catch (ValidationException ex){
			handleException(ex);
			view.error();
		} 
	}
	/**
	 * Verify the agent.
	 * To verify the agent will be get from the database.
	 * 
	 * @throws ValidationException
	 */
	private void verifyAgent() throws ValidationException {
		AgentCrud aCrud = new AgentCrud(crud);
		model.verifyNumber();
		model.dbAgent = aCrud.getSingleAgent(model.inputNumber);
		model.agentExist();
		model.verifyCoolDown(LocalDateTime.now());
		model.verifyActive();
		model.verifyCode();
	}
	
	/**
	 * Prepare the logged in screen by getting the latest login attempts.
	 * Also resets the cool off and sets the license message.
	 * 
	 * @throws ValidationException
	 */
	private void prepareLoginScreen() throws ValidationException {
		AgentCrud aCrud = new AgentCrud(crud);		
		model.latestAttempts = aCrud.readLoginAttempts(model.dbAgent.agentNumber);
		aCrud.createLoginAttempt(model.dbAgent.agentNumber, false);
		aCrud.updateCoolOff(0, model.dbAgent.agentNumber);
		model.setLicenseMessage();
	}
	/**
	 * Handle the exception by preparing the error screen.
	 * Logs the exception message in the console.
	 * Stores the login attempt in the database when needed.
	 * When needed it also updates the cool off in the database.
	 * In the end it resets the input fields.
	 * 
	 * @param ValidationException ex the caught exception.
	 */
	private void handleException(ValidationException ex) {
		AgentCrud aCrud = new AgentCrud(crud);
		ConsoleLogger logger = new ConsoleLogger();
		logger.debugLog(ex.getMessage());
		if (model.saveAttempt) {
			aCrud.createLoginAttempt(model.dbAgent.agentNumber, true);
			model.calculateNewCoolOff();
			aCrud.updateCoolOff(model.newCoolOff, model.dbAgent.agentNumber);
		}
		model.resetFields();
	} 
}
