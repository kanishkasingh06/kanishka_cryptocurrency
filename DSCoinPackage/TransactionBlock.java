package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
	  trarray=t.clone();
	  previous=null;
	  MerkleTree tree=new MerkleTree();
	  tree.Build(t);
	  trsummary=tree.rootnode.val;
	  Tree=tree;
	  dgst=null;
	  
  }

  public boolean checkTransaction (Transaction t) {
	  if(t.coinsrc_block==null) {
		  return true;
	  }
	  Transaction[] k=t.coinsrc_block.trarray;
	  boolean m=false;
	  for(int i=0;i<k.length;i++) {
		  if(k[i].coinID.equals(t.coinID)&&(k[i].Destination==t.Source)) {
			  m=true;
			  break;
		  }
	  }
	  if(m) {
		  TransactionBlock curr=this;
		  if(this==t.coinsrc_block) {
			  return m;
		  }
		  curr=this.previous;
		  while(curr!=t.coinsrc_block) {
			  k=curr.trarray;
			  for(int i=0;i<k.length;i++) {
				  if(k[i].coinID.equals(t.coinID)) {
					  return false;
				  }
			  }
			  curr=curr.previous;
		  }
	  }
	  return m;
  }
}
