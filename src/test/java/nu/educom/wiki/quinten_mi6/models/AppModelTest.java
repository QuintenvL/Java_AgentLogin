package nu.educom.wiki.quinten_mi6.models;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import nu.educom.wiki.quinten_mi6.ValidationException;
import nu.educom.wiki.quinten_mi6.objects.Agent;


class AppModelTest {
	
	Agent dbAgent;
	
	AppModelTest() {
		Timestamp expire = Timestamp.valueOf("2030-01-10 10:10:10");
		Timestamp latestCool = Timestamp.valueOf("2020-01-08 10:10:10");
		dbAgent = new Agent(1, (short) 1, "Code", true, true, expire, (long) 2, latestCool);
	}
	
	@Test
	void testVerifyNumber_LongInput() {
		AppModel model = new AppModel();
		model.numberField.setText("0001");
		try {
			model.verifyNumber();
			fail();
		}
		catch (Exception e) {
			assertEquals("Invalid agent number for :\"0001\".", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertEquals((short) 1, model.inputNumber, "AgentNumber is wrong.");
		}
	}
	@Test
	void testVerifyNumber_NegativeInput() {
		AppModel model = new AppModel();
		model.numberField.setText("-2");
		try {
			model.verifyNumber();
			fail();
		}
		catch (Exception e) {
			assertEquals("Invalid agent number for :\"-2\".", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertEquals((short) -2, model.inputNumber, "AgentNumber is wrong.");
		}
	}
	@Test
	void testVerifyNumber_OutRangeInput() {
		AppModel model = new AppModel();
		model.numberField.setText("999");
		try {
			model.verifyNumber();
			fail();
		}
		catch (Exception e) {
			assertEquals("Invalid agent number for :\"999\".",e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertEquals((short) 999, model.inputNumber, "AgentNumber is wrong.");
		}
	}
	@Test
	void testVerifyNumber_StringInput() {
		AppModel model = new AppModel();
		model.numberField.setText("bla");
		try {
			model.verifyNumber();
			fail();
		}
		catch (Exception e) {
			assertEquals("Invalid input for :\"bla\".",e.getMessage(),"Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertEquals((short) 0, model.inputNumber, "AgentNumber is wrong.");
		}
	}
	
		
	@Test
	void testVerifyNumber_CorrectInput() {
		AppModel model = new AppModel();
		model.numberField.setText("002");
		try {
			model.verifyNumber();
			assertEquals((short) 2, model.inputNumber);
		}
		catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testVerifyCode_valid() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		model.codeField.setText("Code");
		try {
			model.verifyCode();
			assertFalse(model.saveAttempt);
		}
		catch (Exception e){
			fail();
		}
	}
	
	@Test
	void testVerifyCode_invalidValue() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		model.codeField.setText("test");
		try {
			model.verifyCode();
			fail();
		}
		catch (ValidationException e){
			assertEquals("Invalid code for :\"test\".", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertTrue(model.saveAttempt, "Save attempt is wrong");
		}
	}
	@Test
	void testVerifyCode_invalidLength() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		model.codeField.setText("test34");
		try {
			model.verifyCode();
			fail();
		}
		catch (ValidationException e){
			assertEquals("Invalid code for :\"test34\".", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertTrue(model.saveAttempt, "Save attempt is wrong");
		}
	}
	
	@Test
	void testTestString() {
		AppModel model = new AppModel();
		String string = "  123  ";
		
		String result = model.testString(string);
		
		assertEquals("123", result);
		
	}
	
	@Test
	void testResetFields() {
		AppModel model = new AppModel();
		model.numberField.setText("123");
		model.codeField.setText("code");
		model.saveAttempt = true;
		
		model.resetFields();
		
		assertEquals("", model.numberField.getText(), "NumberField is wrong.");
		assertTrue(model.codeField.getPassword().length == 0, "CodeField is wrong.");
		assertFalse(model.saveAttempt, "Save attempt is wrong");
	}
	
	@Test
	void testAgentExist_invalid() {
		AppModel model = new AppModel();
		model.dbAgent = null;
		model.inputNumber = 1;
		try {
			model.agentExist();
			fail();
		}
		catch (ValidationException e) {
			assertEquals("Non existing agent.", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
		}
	}
	
	@Test
	void testVerifyActive_invalid() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		model.dbAgent.active = false;
		try {
			model.verifyActive();
			fail();
		}
		catch (ValidationException e) {
			assertEquals("Inactive agent.", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertTrue(model.saveAttempt);
		}
	}
	
	@Test
	void testVerifyActive_valid() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		try {
			model.verifyActive();
			assertFalse(model.saveAttempt);
		}
		catch (ValidationException e) {
			fail();
		}
	}
	
	@Test
	void testVerifyCoolDown_invalid() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.parse("2020-01-08 10:11:09", formatter);
		try {
			model.verifyCoolDown(now);
			fail();
		}
		catch (ValidationException e) {
			assertEquals("Cool off still active for 61 second(s).", e.getMessage(), "Message is wrong.");
			assertTrue(e instanceof ValidationException, "Instance is wrong.");
			assertEquals((long) 1, model.remainingCoolOffMinutes, "Remaining minutes is wrong.");
			assertEquals((long) 1, model.remainingCoolOffSeconds, "Remaining seconds is wrong.");
			
		}
	}
	
	@Test
	void testVerifyCoolDown_valid() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.parse("2020-01-08 11:11:09", formatter);
		try {
			model.verifyCoolDown(now);
			assertEquals((long) 0, model.remainingCoolOffMinutes, "Remaining minutes is wrong.");
			assertEquals((long) 0, model.remainingCoolOffSeconds, "Remaining seconds is wrong.");
		}
		catch (ValidationException e) {
			fail();
			
		}
	}
	
	@Test
	void testcalculateNewCoolOff_increaseCoolOff() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		
		model.calculateNewCoolOff();
		assertEquals((long) 4, model.newCoolOff);
		assertEquals((long) 4, model.remainingCoolOffMinutes);
	}
	
	@Test
	void testcalculateNewCoolOff_setCoolOff() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		model.dbAgent.coolOff = 0;
		
		model.calculateNewCoolOff();
		assertEquals((long) 1, model.newCoolOff);
		assertEquals((long) 1, model.remainingCoolOffMinutes);
	}
	
	@Test
	void testSetLicenseMessage_license() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		
		model.setLicenseMessage();
		assertEquals("<html>You have a license to kill which expires on <b><font color=navy>10-01-2030 10:10:10</font></b></html>", model.licenseMessage);
	}
	
	@Test
	void testSetLicenseMessage_notLicense() {
		AppModel model = new AppModel();
		model.dbAgent = dbAgent;
		model.inputNumber = 1;
		model.dbAgent.license = false;
		
		model.setLicenseMessage();
		
		assertEquals("You don't have a license to kill", model.licenseMessage);
	}

}
