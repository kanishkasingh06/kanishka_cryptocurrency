package DSCoinPackage;

import HelperClasses.Pair;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
	  int iC=100000;
	  int j=0;
	  Members mo=new Members();
	  mo.UID="Moderator";
	  Transaction[] t=new Transaction[coinCount];
	  for(int i=0;i<coinCount;i++) {
		  t[i]=new Transaction();
		  t[i].coinID=Integer.toString(iC);
		  iC++;
		  t[i].Source=mo;
		  t[i].Destination=DSObj.memberlist[j];
		  t[i].coinsrc_block=null;
		  j++;
		  if(j==DSObj.memberlist.length) {
			  j=0;
		  }
	  }
	  DSObj.latestCoinID=Integer.toString(iC-1);
	  for(int i=0;i<coinCount;) {
		  Transaction[] m=new Transaction[DSObj.bChain.tr_count];
		  for(int d=0;d<DSObj.bChain.tr_count;d++) {
			  m[d]=t[i];
			  i++;
		  }
		  TransactionBlock lds=new TransactionBlock(m);
		  for(int d=0;d<DSObj.bChain.tr_count;d++) {
			  m[d].Destination.mycoins.add(new Pair<String,TransactionBlock>(m[d].coinID,lds));
		  }
		  DSObj.bChain.InsertBlock_Honest(lds);
	  }
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
	  int iC=100000;
	  int j=0;
	  Members mo=new Members();
	  mo.UID="Moderator";
	  Transaction[] t=new Transaction[coinCount];
	  for(int i=0;i<coinCount;i++) {
		  t[i]=new Transaction();
		  t[i].coinID=Integer.toString(iC);
		  iC++;
		  t[i].Source=mo;
		  t[i].Destination=DSObj.memberlist[j];
		  t[i].coinsrc_block=null;
		  j++;
		  if(j==DSObj.memberlist.length) {
			  j=0;
		  }
	  }
	  DSObj.latestCoinID=Integer.toString(iC-1);
	  for(int i=0;i<coinCount;) {
		  Transaction[] m=new Transaction[DSObj.bChain.tr_count];
		  for(int d=0;d<DSObj.bChain.tr_count;d++) {
			  m[d]=t[i];
			  i++;
		  }
		  TransactionBlock lds=new TransactionBlock(m);
		  for(int d=0;d<DSObj.bChain.tr_count;d++) {
			  m[d].Destination.mycoins.add(new Pair<String,TransactionBlock>(m[d].coinID,lds));
		  }
		  DSObj.bChain.initialization=true;
		  DSObj.bChain.InsertBlock_Malicious(lds);
		  DSObj.bChain.initialization=false;
	  }
  }
}
