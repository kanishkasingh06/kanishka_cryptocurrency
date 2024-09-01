package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;

public class BlockChain_Malicious {
  private int j=0;	
  private int mnc=0;
  public boolean initialization=false;
  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList=new TransactionBlock[100];
  public static boolean checkTransactionBlock (TransactionBlock tB) {
	  boolean m=false;
	  Transaction[] e=tB.trarray.clone();
	  if((tB.dgst.substring(0, 4)).equals("0000")) {
		  if(tB.previous==null) {
			  if((tB.dgst.equals((new CRF(64)).Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce)))){
				  m=true;
			  }
		  }
		  else {
			  if((tB.dgst.equals((new CRF(64)).Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce)))){
				  m=true;
			  }
		  }
	  }
	  if(m) {
		  MerkleTree tree=new MerkleTree();
		  if(!(tB.trsummary.equals(tree.Build(tB.trarray)))) {
			  return false;
		  }
	  }
	  if(m) {
		  for(int i=0;i<e.length;i++) {
			  if(!tB.checkTransaction(e[i])) {
				  return false;
			  }
		  }
	  }
	  return m;
  }

  public TransactionBlock FindLongestValidChain () {
	  int n=mnc;
	  TransactionBlock[] e=lastBlocksList.clone();
	  int track=0;
	  j=0;
	  for(int i=0;i<n;i++) {
		  int k=0;
		  TransactionBlock curr=lastBlocksList[i];
		  while(curr!=null) {
			 if(!(curr.trarray[0].Source.UID.equals("Moderator"))) { 
			  if(checkTransactionBlock(curr)) {
				  k++;
			  }
			  else {
				  k=0;
				  e[i]=curr.previous;
			  }
			  curr=curr.previous;}
			 else {
				 break;
			 }
		  }
		  if(k>track) {
			  track=k;
			  j=i;
		  }
	  }
    return e[j];
  }
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
  public void InsertBlock_Malicious (TransactionBlock newBlock) {
	  TransactionBlock lastBlock;
	  if(!initialization) {
	  lastBlock=FindLongestValidChain();}
	  else {
		  lastBlock=lastBlocksList[j];
	  }
	  boolean ccd=false;
	  if(lastBlocksList[j]==lastBlock&& lastBlock!=null) {
		  ccd=true;
	  }
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
	 if(ccd) {
		 lastBlocksList[j]=lastBlock;
	 }
	 else {
		 lastBlocksList[mnc]=lastBlock;
		 mnc++;
	 }
  }
}
