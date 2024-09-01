package DSCoinPackage;

import java.util.*;

import HelperClasses.MerkleTree;
import HelperClasses.Pair;
import HelperClasses.TreeNode;

public class Members
 {	
  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans=new Transaction[100];
  private int c=0;
  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
	  Transaction tobj=new Transaction();
	  Pair<String, TransactionBlock> e=mycoins.remove(0);
	  tobj.coinID=e.get_first();
	  tobj.coinsrc_block=e.get_second();
	  tobj.Source=this;
	  for(int i=0;i<DSobj.memberlist.length;i++) {
		  if(destUID.equals(DSobj.memberlist[i].UID)) {
			  tobj.Destination=DSobj.memberlist[i];
			  break;
		  }
	  }
	  in_process_trans[c]=tobj;
	  c++;
	  DSobj.pendingTransactions.AddTransactions(tobj);
  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
	  Transaction tobj=new Transaction();
	  Pair<String, TransactionBlock> e=mycoins.remove(0);
	  tobj.coinID=e.get_first();
	  tobj.coinsrc_block=e.get_second();
	  tobj.Source=this;
	  for(int i=0;i<DSobj.memberlist.length;i++) {
		  if(destUID.equals(DSobj.memberlist[i].UID)) {
			  tobj.Destination=DSobj.memberlist[i];
			  break;
		  }
	  }
	  in_process_trans[c]=tobj;
	  c++;
	  DSobj.pendingTransactions.AddTransactions(tobj);
  }
  
  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
	  TransactionBlock lastBlock=DSObj.bChain.lastBlock;
	  TransactionBlock curr=lastBlock;
	  int i=0;
	  boolean m=false;
	  List<Pair<String,String>> d=new ArrayList<Pair<String,String>>();
	  while(curr!=null) {
		  for(i=0;i<curr.trarray.length;i++) {
			  if(tobj==curr.trarray[i]) {
				  m=true;
				  break;
			  }
		  }
		  if(curr.previous!=null) {
		  d.add(new Pair<String, String>(curr.dgst,curr.previous.dgst+"#"+curr.trsummary+"#"+curr.nonce));
		  }
		  if(m) {
			  break;
		  }
		  curr=curr.previous;
	  }
	  if(curr==null) {
		  throw new MissingTransactionException();
	  }
	  d.add(new Pair<String,String>(curr.previous.dgst,null));
	  List<Pair<String,String>> r=new ArrayList<Pair<String,String>>();
	  for(int jcb=d.size()-1;jcb>-1;jcb--) {
		  r.add(d.get(jcb));
	  }
	  MerkleTree tree=curr.Tree;
	  List<Pair<String,String>> e=new ArrayList<Pair<String,String>>();
	  TreeNode mcd=tree.g(0, curr.trarray.length-1, tree.rootnode, i);
	  e=tree.sib(mcd.parent, e);
	  for(int j=0;j<=c;j++) {
		  if(tobj==in_process_trans[j]) {
			  in_process_trans[j]=null;
			  int lmd=0;
			  for(lmd=0;lmd<tobj.Destination.mycoins.size();lmd++) {
				  if(tobj.coinID.compareTo(tobj.Destination.mycoins.get(lmd).get_first())<0) {
					  break;
				  }
			  }
			  tobj.Destination.mycoins.add(lmd, new Pair<String,TransactionBlock>(tobj.coinID,curr));
			  break;
		  }
	  }
	  return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(e,r);
  }

  public void MineCoin(DSCoin_Honest DSObj) throws EmptyQueueException {
	  int n=DSObj.bChain.tr_count -1;
	  List<String> a=new ArrayList<String>();
	  Transaction[] tb=new Transaction[n+1];
	  for(int i=0;i<n;i++) {
		  boolean m=true;
		  Transaction s=DSObj.pendingTransactions.RemoveTransaction();
		  TransactionBlock ad=DSObj.bChain.lastBlock;
		  boolean k=ad.checkTransaction(s);
		  if(k&&(s.coinsrc_block!=ad)) {
			  Transaction[] md=ad.trarray;
			  for(int bde=0;bde<md.length;bde++) {
				  if(md[bde].coinID.equals(s.coinID)) {
					  k=false;
				  }
			  }
		  }
		  if(!k) {
			  i--;
			  continue;
		  }
		  for(int j=0;j<a.size();j++) {
			  if(s.coinID.equals(a.get(j))) {
				  m=false;
				  i--;
				  break;
			  }
		  }
		  if(m&&k) {
			  a.add(s.coinID);
			  tb[i]=s;
		  }
	  }
	  Transaction mined=new Transaction();
	  DSObj.latestCoinID=Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
	  mined.coinID=DSObj.latestCoinID;
	  mined.coinsrc_block=null;
	  mined.Destination=this;
	  mined.Source=null;
	  tb[n]=mined;
	  TransactionBlock z=new TransactionBlock(tb);
	  DSObj.bChain.InsertBlock_Honest(z);
	  mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID,z));
  }  

  public void MineCoin(DSCoin_Malicious DSObj) throws EmptyQueueException {
	  int n=DSObj.bChain.tr_count -1;
	  List<String> a=new ArrayList<String>();
	  Transaction[] tb=new Transaction[n+1];
	  for(int i=0;i<n;i++) {
		  boolean m=true;
		  Transaction s=DSObj.pendingTransactions.RemoveTransaction();
		  TransactionBlock ad=DSObj.bChain.FindLongestValidChain();
		  boolean k=ad.checkTransaction(s);
		  if(k&&(s.coinsrc_block!=ad)) {
			  Transaction[] md=ad.trarray;
			  for(int bde=0;bde<md.length;bde++) {
				  if(md[bde].coinID.equals(s.coinID)) {
					  k=false;
				  }
			  }
		  }
		  if(!k) {
			  i--;
			  continue;
		  }
		  for(int j=0;j<a.size();j++) {
			  if(s.coinID.equals(a.get(j))) {
				  m=false;
				  i--;
				  break;
			  }
		  }
		  if(m&&k) {
			  a.add(s.coinID);
			  tb[i]=s;
		  }
	  }
	  Transaction mined=new Transaction();
	  DSObj.latestCoinID=Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
	  mined.coinID=DSObj.latestCoinID;
	  mined.coinsrc_block=null;
	  mined.Destination=this;
	  mined.Source=null;
	  tb[n]=mined;
	  TransactionBlock z=new TransactionBlock(tb);
	  DSObj.bChain.InsertBlock_Malicious(z);
	  mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID,z));
  }  
}
