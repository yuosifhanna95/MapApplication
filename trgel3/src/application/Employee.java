package application;

public class Employee extends User{

	Employee(String firstName, String lastName, String email, String userName, String password, String phoneNumber,
			String payment,String type) {
		super(firstName, lastName, email, userName, password, phoneNumber, payment,type);
		// TODO Auto-generated constructor stub
	}
	
	public Employee(String firstName, String lastName, String email, String userName, String password, String phoneNumber) {
		super(firstName,lastName, email, userName, password, phoneNumber,"","employee");
		
		// TODO Auto-generated constructor stub
	}

}
