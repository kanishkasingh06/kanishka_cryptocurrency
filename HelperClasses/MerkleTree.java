package HelperClasses;

import DSCoinPackage.Transaction;
import java.util.*;

public class MerkleTree {

  // Check the TreeNode.java file for more details
  public TreeNode rootnode;
  public int numdocs;

  void nodeinit(TreeNode node, TreeNode l, TreeNode r, TreeNode p, String val) {
    node.left = l;
    node.right = r;
    node.parent = p;
    node.val = val;
  }

  public String get_str(Transaction tr) {
    CRF obj = new CRF(64);
    String val = tr.coinID;
    if (tr.Source == null)
      val = val + "#" + "Genesis"; 
    else
      val = val + "#" + tr.Source.UID;

    val = val + "#" + tr.Destination.UID;

    if (tr.coinsrc_block == null)
      val = val + "#" + "Genesis";
    else
      val = val + "#" + tr.coinsrc_block.dgst;

    return obj.Fn(val);
  }

  public String Build(Transaction[] tr) {
    CRF obj = new CRF(64);
    int num_trans = tr.length;
    List<TreeNode> q = new ArrayList<TreeNode>();
    for (int i = 0; i < num_trans; i++) {
      TreeNode nd = new TreeNode();
      String val = get_str(tr[i]);
      nodeinit(nd, null, null, null, val);
      q.add(nd);
    }
    TreeNode l, r;
    while (q.size() > 1) {
      l = q.get(0);
      q.remove(0);
      r = q.get(0);
      q.remove(0);
      TreeNode nd = new TreeNode();
      String l_val = l.val;
      String r_val = r.val;
      String data = obj.Fn(l_val + "#" + r_val);
      nodeinit(nd, l, r, null, data);
      l.parent = nd;
      r.parent = nd;
      q.add(nd);
    }
    rootnode = q.get(0);

    return rootnode.val;
  }
  public TreeNode g(int l,int r,TreeNode n,int id) {
		if(l==r||n.left==null) {
			return n;
		}
		else {TreeNode v;
			if(id<=(l+r)/2) {
				v=g(l,(l+r)/2,n.left,id);
			}
			else {
				v=g((l+r)/2,r,n.right,id);
			}return v;
		}
	}
  public List<Pair<String,String>> sib(TreeNode k,List<Pair<String,String>> nd){
	  if(k.parent==null) {
		  nd.add(new Pair<String,String>(k.left.val,k.right.val));
		  nd.add(new Pair<String,String>(k.val,null));
		  return nd;
	  }
	  else {
		  nd.add(new Pair<String,String>(k.left.val,k.right.val));
		  nd=sib(k.parent,nd);
		  return nd;
	  }
  }
}
