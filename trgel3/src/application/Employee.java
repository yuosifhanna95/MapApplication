package application;

public class Employee extends User {

	Employee(String firstName, String lastName, String email, String userName, String password, String phoneNumber,
			String payment, String type, String history) {
		super(firstName, lastName, email, userName, password, phoneNumber, payment, type, history);
		// TODO Auto-generated constructor stub
	}

	public Employee(String firstName, String lastName, String email, String userName, String password,
			String phoneNumber, String history) {
		super(firstName, lastName, email, userName, password, phoneNumber, "", "employee", history);

		// TODO Auto-generated constructor stub
	}

}
