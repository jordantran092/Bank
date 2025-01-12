package cs.mylab.bank.model;

public class Bank {
	
	private Client[] clients;
	private final int MAX_CLIENTS = 6;
	private boolean error;
	private String errorStr;
	private int noc; // number of clients
	
	public Bank() {
		this.clients = new Client[this.MAX_CLIENTS];
		this.noc = 0;
		this.error = false;
		this.errorStr = "";
	}
	
	public String getStatus() { 
		String result = "";
		
		
		/*
		 * If no error occurs with the services used e.g. deposit, then a list of all client's statuses will be returned. Otherwise, will return the recent error reason
		 */
		if(!error) {
			
			for(int i = 0; i < this.noc; i++) {
				result += String.format("%s", this.clients[i].getStatus());
				if(i < this.noc-1) {
					result += ", ";
				}
			}
			result = String.format("Accounts: {%s}", result);
			
		}
		else {
			result = errorStr;
		}
		return result; 
	}
	
	/*
	 * Print statement service. Will search through clients list, and if exists, will get the client's statement and return that. Otherwise, will give an error saying account does not exist
	 */
	public String[] getStatement(String name) { 
		
		String[] result = null;
		
		
		Client client = getClient(name);
		
		if(client != null) {
			result = client.getStatement();
		}
		else { //result will stay null
			turnOnError(String.format("Error: From-Account %s does not exist", name));

		}
		
		return result;
		
	}
	
	/*
	 *  Will check if any of the errors will occur, if not then will deposit the amount into that account and clear any previous bank service errors. Errors are prioritized in a way where the lowest ranked error is outputted if multiple errors occur, thus the higher in the if chain, the more priority, as required by the lab assignment. Error checks are if account exists, and if non-positive amount.
	 */
	public void deposit(String name, double amount) { 

		Client client = getClient(name);
		
		if(client == null) { //rank1
			turnOnError(String.format("Error: To-Account %s does not exist", name));
		}
		else if(amount <= 0) { //rank 2
			turnOnError("Error: Non-Positive Amount");
		}
		else {
			client.deposit(amount);
			
			turnOffError();
		}
		
		
	}
	
	/*
	 *  Will check if any of the errors will occur, if not then will withdraw the amount from that account and clear any previous bank service errors. Errors are prioritized in a way where the lowest ranked error is outputted if multiple errors occur, thus the higher in the if chain, the more priority, as required by the lab assignment. Error checks are if account exists, if non-positive amount, and if amount is too large too withdraw. 
	 */
	public void withdraw(String name, double amount) { 
		Client client = getClient(name);
		
		if(client == null) {
			turnOnError(String.format("Error: From-Account %s does not exist", name));
		}
		else if(amount <= 0) {
			turnOnError("Error: Non-Positive Amount");
		}
		else if(amount > client.getBalance()) {
			turnOnError("Error: Amount too large to withdraw");
		}
		else {
			client.withdraw(amount);
			turnOffError();
		}
	
		
	}
	
	/*
	 *  Will check if any of the errors will occur, if not then will withdraw from fromName and deposit to toName, and clear any previous bank service errors. Errors are prioritized in a way where the lowest ranked error is outputted if multiple errors occur, thus the higher in the if chain, the more priority, as required by the lab assignment. Error checks are if accounts exists, and if non-positive amount, and if amount is too large too withdraw. 
	 */
	public void transfer(String fromName, String toName, double amount) { //FIXME
		Client fromClient = getClient(fromName);
		Client toClient = getClient(toName);
		
		if(fromClient == null) {
			turnOnError(String.format("Error: From-Account %s does not exist", fromName));
		}
		else if(toClient == null) {
			turnOnError(String.format("Error: To-Account %s does not exist", toName));
		}
		else if(amount <= 0) {
			turnOnError("Error: Non-Positive Amount");
		}
		else if(amount > fromClient.getBalance()) {
			turnOnError("Error: Amount too large to transfer");

		}
		else {
			fromClient.withdraw(amount);
			toClient.deposit(amount);
			turnOffError();
		}
	}
	
	
	/*
	 *  Will check if any of the errors will occur, if not then will create a new client with the name and initial balance and clear any previous bank service errors. Errors are prioritized in a way where the lowest ranked error is outputted if multiple errors occur, thus the higher in the if chain, the more priority, as required by the lab assignment. Error checks are if max number of accounts reached, if client already exists, and if non-positive initial balance
	 */
	public void addClient(String name, double amount) {
		/*
		 * higher prio is higher in the if chain, then just do else if.... as you go down, to create the priority 
		 * 
		 */
		if(this.noc == this.MAX_CLIENTS) { //rank1
			turnOnError("Error: Maximum Number of Accounts Reached");
		}
		else if(getClient(name) != null) { //rank 2
			turnOnError(String.format("Error: Client %s already exists", name));
		}
		else if(amount <= 0) { //rank 3
			turnOnError("Error: Non-Positive Initial Balance");
		}
		else {
			this.clients[this.noc] = new Client(name, amount);
			this.noc++;
			
			turnOffError();
		}
		
	}
	
	/*
	 * Retrieves client from clients array with given name. Will immediately stop searching array once found, and return that, otherwise return null
	 */
	public Client getClient(String name) {
		Client client = null;
		
		boolean foundClient = false;
		for(int i = 0; !foundClient && i < this.noc; i++) {
			client = this.clients[i];
			foundClient = client.getName().equals(name);	
		}
		
		//if not found account will return null, otherwise will mean account was found successfully and will be returned eventually
		if(!foundClient) { 
			client = null;
		}
		
		return client;
	}
	
	/*
	 * Sets error status to true and sets its error message to given argument
	 */
	public void turnOnError(String errorStr) {
		this.error = true;
		this.errorStr = errorStr;
	}
	
	/*
	 * Sets error status to false and clears error message
	 */
	public void turnOffError() {
		this.error = false;
		this.errorStr = "";
	}
	
	
	
}
