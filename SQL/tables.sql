
-- for testing
DROP TABLE IF EXISTS bank_transaction;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS bank;


CREATE TABLE bank (

	bank_id INT NOT NULL,

	error BOOLEAN NOT NULL, -- default false

	error_str TEXT NOT NULL, -- default empty string

	num_of_clients INT CHECK (num_of_clients >= 0) NOT NULL, -- default 0


	PRIMARY KEY (bank_id)
	


);



CREATE TABLE client (

	client_id INT NOT NULL,

	num_of_transactions INT CHECK (num_of_transactions >= 0) NOT NULL, -- default 0

	balance NUMERIC(15,2) CHECK (balance >= 0) NOT NULL, -- default 0.00

	name VARCHAR(50) NOT NULL, -- default empty str

	bank_id INT NOT NULL,

	FOREIGN KEY (bank_id) REFERENCES BANK(bank_id),



	PRIMARY KEY (client_id)
	
);

CREATE TABLE bank_transaction (

	transaction_id INT NOT NULL,

	transaction_type VARCHAR(50) NOT NULL, -- default 0

	amount NUMERIC(15,2) CHECK (amount >= 0) NOT NULL, -- default 0.00

	client_id INT NOT NULL,


	-- ON delete, set null, if client deleted keep the transactions still
	FOREIGN KEY (client_id) REFERENCES CLIENT(client_id) ON DELETE SET NULL,


	PRIMARY KEY (transaction_id)
	

);



/*

- Client
    - ON DELETE restrict default, because don’t want bank to delete if it has client records. Handle it manually
    - ON update restrict default, the PK value of bank should never really change (can cause problems like FK references breaking) so prob an accident at that point
- Transaction
    - ON update restrict default, the PK value of client should never really change (can cause problems like FK references breaking) so prob an accident at that point

*/









/* ----- Testing */

/* Expect Pass */
-- INSERT INTO bank VALUES(0, FALSE, '', 0);
-- INSERT INTO client VALUES(0, 0, 0.00, 'Jordan', 0);
-- INSERT INTO client VALUES(1, 0, 0.23, 'John', 0);
-- INSERT INTO bank_transaction VALUES(0, 'DEPOSIT', 0.00, 0);
-- INSERT INTO bank_transaction VALUES(1, 'DEPOSIT', 2.23, 0);


/* Expect Fail */
-- INSERT INTO bank_transaction VALUES(0, 'DEPOSIT', -2, 0);

-- SELECT * FROM bank;
-- SELECT * FROM client;
-- SELECT * FROM bank_transaction;