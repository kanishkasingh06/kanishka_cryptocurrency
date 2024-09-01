package DSCoinPackage;

public class TransactionQueue {
  public Transaction firstTransaction=null;
  public Transaction lastTransaction=null;
  public int numTransactions=0;
  public void AddTransactions (Transaction transaction) {
	  if(firstTransaction==null) {
		  firstTransaction=transaction;
		  lastTransaction=firstTransaction;
		  numTransactions=1;
	  }
	  else if(lastTransaction==firstTransaction) {
		  lastTransaction=transaction;
		  firstTransaction.next=lastTransaction;
		  numTransactions=2;
	  }
	  else {
		  Transaction k=lastTransaction;
		  lastTransaction=transaction;
		  k.next=lastTransaction;
		  numTransactions+=1;
	  }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
	  if(firstTransaction==null) {
		  throw new EmptyQueueException();
	  }
	  Transaction k=firstTransaction;
	  if(numTransactions==1) {
		  firstTransaction=null;
		  lastTransaction=null;
		  numTransactions=0;
		  return k;
	  }
    
    firstTransaction=k.next;
    numTransactions-=1;
    return k;
  }

  public int size() {
    return numTransactions;
  }
}
