package com.example;

//import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class EmployeeManagerAlternativeTest {

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
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testExampleOfArgumentCaptor() {
		when(employeeRepository.findAll()).thenReturn(asList(new Employee("1", 1000),new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		verify(bankService,times(2)).pay(idCaptor.capture(), amountCaptor.capture());
		assertThat(idCaptor.getAllValues()).containsExactly("1","2");
		assertThat(amountCaptor.getAllValues()).containsExactly(1000.0,2000.0);
		verifyNoMoreInteractions(bankService);
	}
	
	@Test
	public void testOtherEmployeesArePaidWhenBankServiceThrowsException() {
		when(employeeRepository.findAll()).thenReturn(asList(notToBePaid,toBePaid));
		doThrow(new RuntimeException()).doNothing().when(bankService).pay(anyString(), anyDouble());
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}

}
