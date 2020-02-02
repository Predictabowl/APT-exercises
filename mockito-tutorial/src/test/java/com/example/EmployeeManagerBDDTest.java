package com.example;

//import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static java.util.Arrays.asList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

// we use mockito runner in place of initMock in the Before method
@RunWith(MockitoJUnitRunner.class)
public class EmployeeManagerBDDTest {

	@Mock
	private EmployeeRepository employeeRepository;
	
	@Mock
	private BankService bankService;
	
	@Spy
	private Employee notToBePaid = new Employee("1", 1000);
	
	@Spy
	private Employee toBePaid = new Employee("2", 2000);
	
	@Captor
	private ArgumentCaptor<String> idCaptor;
	
	@Captor
	private ArgumentCaptor<Double> amountCaptor;
	
	@InjectMocks
	private EmployeeManager employeeManager;
	
	@Test
	public void testPayEmployeesWhenSeveralEmployeeArePresent() {
		given(employeeRepository.findAll()).willReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		InOrder inOrder = inOrder(bankService,employeeRepository);
		then(employeeRepository).should(inOrder).findAll();
		then(bankService).should(inOrder).pay("1", 1000);
		then(bankService).should(inOrder).pay("2", 2000);
		then(bankService).shouldHaveNoMoreInteractions();
	}
	
	@Test
	public void testExampleOfArgumentCaptor() {
		given(employeeRepository.findAll()).willReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		then(bankService).should(times(2)).pay(idCaptor.capture(), amountCaptor.capture());
		assertThat(idCaptor.getAllValues()).containsExactly("1","2");
		assertThat(amountCaptor.getAllValues()).containsExactly(1000.0,2000.0);
		then(bankService).shouldHaveNoMoreInteractions();
	}
	
	@Test
	public void testOtherEmployeesArePaidWhenBankServiceThrowsException() {
		given(employeeRepository.findAll()).willReturn(asList(notToBePaid,toBePaid));
		willThrow(new RuntimeException()).willDoNothing().given(bankService).pay(anyString(), anyDouble());
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		then(notToBePaid).should().setPaid(false);
		then(toBePaid).should().setPaid(true);
	}

}
