package com.example;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import org.mockito.InOrder;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.VerificationCollector;
import org.mockito.ArgumentCaptor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EmployeeManagerTest {

	private EmployeeManager employeeManager;
	private EmployeeRepository employeeRepository;
	private BankService bankService;
	
	
	/*
	 * This rule allow to execute all tests without halting the execution due to a test failure.
	 * The collector gather all test reports.
	 */
	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();
	
	@Before
	public void setup() {
		employeeRepository = mock(EmployeeRepository.class);
		bankService = mock(BankService.class);
		employeeManager = new EmployeeManager(employeeRepository,bankService);		
	}

	@Test
	public void testPayEmployeesWhenNoEmployessArePresent() {
		when(employeeRepository.findAll()).thenReturn(emptyList());
		assertThat(employeeManager.payEmployees()).isEqualTo(0);
	}
	
	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 1000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		verify(bankService).pay("1", 1000);
	}

	@Test
	public void testPayEmployeesWhenSeveralEmployeesArePresent() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		verify(bankService,times(2)).pay(anyString(), anyDouble());
	}
	
	@Test
	public void testPayEmployeesWhenSeveralEmployeesArePresentV2() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		// we intentionally invert the order of invocations to show that we're not concerned with the order.
		verify(bankService).pay("2", 2000);
		verify(bankService).pay("1", 1000);
		verifyNoMoreInteractions(bankService);
	}
	
	/*
	 * This test check the order of the invocation using Mockito InOrder.
	 * Otherwise we could use "containsExactly" from AssertJ 
	 */
	@Test
	public void testPayEmployeesWhenSeveralEmployeesArePresentV3() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		// an example of invocation order verification
		InOrder inOrder = inOrder(bankService);
		inOrder.verify(bankService).pay("1", 1000);
		inOrder.verify(bankService).pay("2", 2000);
		verifyNoMoreInteractions(bankService);
	}
	
	// Just an example of ArgumentCaptor to check passed arguments
	@Test
	public void testExampleOfArgumentCaptor() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
		verify(bankService,times(2)).pay(idCaptor.capture(), amountCaptor.capture());
		assertThat(idCaptor.getAllValues()).containsExactly("1","2");
		assertThat(amountCaptor.getAllValues()).containsExactly(1000.0,2000.0);
		verifyNoMoreInteractions(bankService);
	}
	
	@Test
	public void testEmployeeSetPaidIsCalledAfterPaying() {
		Employee employee = spy(new Employee("1", 1000));
		when(employeeRepository.findAll()).thenReturn(asList(employee));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		InOrder inOrder = inOrder(bankService,employee);
		inOrder.verify(bankService).pay("1", 1000);
		inOrder.verify(employee).setPaid(true);
	}
	
	@Test
	public void testPayEmployeesWhenBankServiceThrowsException() {
		Employee employee = spy(new Employee("1", 1000));
		when(employeeRepository.findAll()).thenReturn(asList(employee));
		doThrow(new RuntimeException()).when(bankService).pay(anyString(),anyDouble());
		// number of payments must be 0
		assertThat(employeeManager.payEmployees()).isEqualTo(0);
		// make sure that Employee.paid is updated accordingly
		verify(employee).setPaid(false);
	}
	
	/*
	 * the following test has nothing to do with our case-study
	 * it's just a demonstration of spy and stubbing 
	 */
	@Test
	public void testSubsequentStubbing() {
		when(employeeRepository.findAll())
			//this is only for the first invocation
			.thenReturn(Collections.emptyList())
			// this is for all the subsequent invocations
			.thenReturn(asList(new Employee("1", 1000)));
		assertThat(employeeRepository.findAll()).isEmpty();
		assertThat(employeeRepository.findAll()).hasSize(1);
		assertThat(employeeRepository.findAll()).hasSize(1);
	}
	
	@Test
	public void testOtherEmployeesArePaidWhenBankServiceThrowsException() {
		Employee notToBePaid = spy(new Employee("1", 1000));
		Employee toBePaid = spy(new Employee("2", 2000));
		when(employeeRepository.findAll()).thenReturn(asList(notToBePaid,toBePaid));
		doThrow(new RuntimeException()).doNothing().when(bankService).pay(anyString(), anyDouble());
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}
	
	/*
	 * The following test is functionally the same as the previous
	 * but use the argument matcher as an example.
	 */
	@Test
	public void testArgumentMatcherExample() {
		Employee notToBePaid = spy(new Employee("1", 1000));
		Employee toBePaid = spy(new Employee("2", 2000));
		when(employeeRepository.findAll()).thenReturn(asList(notToBePaid,toBePaid));
		doThrow(new RuntimeException()).when(bankService).pay(argThat(s -> s.equals("1")), anyDouble());
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}
}
