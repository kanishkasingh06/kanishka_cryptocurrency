package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;
  private String generate(String a,String b) {
	  int i=1000000001;
	  CRF obj=new CRF(64);
	  while(true) {
		  if((obj.Fn(a+"#"+b+"#"+(Integer.toString(i))).substring(0, 4)).equals("0000")){
			  return Integer.toString(i);
		  }
		  i++;
	  }
  }
  public void InsertBlock_Honest (TransactionBlock newBlock) {
	 if(lastBlock==null) {
		  newBlock.nonce=generate(start_string,newBlock.trsummary);
		  newBlock.dgst=(new CRF(64)).Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
		  lastBlock=newBlock;
	 }
	 else {
		  newBlock.nonce=generate(lastBlock.dgst,newBlock.trsummary);
		  newBlock.dgst=(new CRF(64)).Fn(lastBlock.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
		  newBlock.previous=lastBlock;
		  lastBlock=newBlock;
	 }
  }
}
