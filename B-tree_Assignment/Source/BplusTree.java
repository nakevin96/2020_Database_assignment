
import java.util.*;

public class BplusTree{

	/*
	 * @param degree
	 * �� BplusTree�� ����
	 * 
	 * @param root
	 * �� BplusTree�� ��Ʈ���
	 */
		private int degree;
		private Node root;
		
		public void setRoot(Node r) {
			root =r;
		}
		public Node getRoot() {
			return root;
		}
		public int getDegree() {
			return degree;
		}
		
		public BplusTree(int idegree) {
			degree=idegree;
			root=null;
		}
		
		/*
		 *  insert BplusTree�� �۵��������� key�� value�� �޾� ��忡 �����ϱ����� �޼ҵ�
		 */
		public void insert(int key, int value) {
			//���� �ƹ� ���� �������� ���� ���¶��
			if(this.root==null) {
				Node newNode = new Node();
				newNode.getKeys().add(new Key(key,value));
				newNode.setN(1);
				
				this.root=newNode;
			}else if(this.root.getChildNodes().isEmpty() &&  this.root.getN()<this.degree-1) {
				//root Node �� �����ϰ� �߰��Ͽ��� overFlow���� ���� ���
				insertInLeafNode(this.root,key,value);
			}else {
				Node sNode = this.root;
				//sNode�� �̿��Ͽ� ���������� ��������
				//���� ���� leafNode�� ��Ʈ��常 ���� ���� childNode�� �����Ƿ� isEmpty()�� üũ �������� ���� ��带 ����Ű�Ƿ� 
				//childNode�� ����� 1�̴�. 
				while(!sNode.getChildNodes().isEmpty()) {
					sNode= sNode.getChildNodes().get(binSearch(key,sNode.getKeys()));
				}
				insertInLeafNode(sNode,key,value);
				if(sNode.getN()==this.degree) {
					splitLeafNode(sNode);
				}
			}
		}
		
		/*
		 * leafNode�� key�� value insert
		 */
		private void insertInLeafNode(Node node, int key, int value) {
			int insertIndex = binSearch(key,node.getKeys());
			if(insertIndex==0 && node.getKeys().get(0).getKey()==key) {
				System.out.println(key+"is already existed (no duplicated)");
				return;
			}if(insertIndex !=0) {
				if(node.getKeys().get(insertIndex-1).getKey()==key){
					System.out.println(key+"is already existed (no duplicated)");
					return;
				}
			}
			Key insertKey = new Key(key,value);
			node.getKeys().add(insertIndex,insertKey);
			node.setN(node.getN()+1);
			return;
		}
		/*
		 * key���� key�迭�� ������ ���� �����ϴ��� index�� ��ȯ
		 */
		public int binSearch(int key, List<Key> keys) {
			int start =0;
			int end = keys.size()-1;
			int mid;
			int index=-1;
			//if key�� keys�� ���� ù° ������ �۴ٸ� 0
			//���� ���ٸ� ���������ΰ����Ѵ�.
			if(key<keys.get(start).getKey()) {
				return 0;
			}
			//���� keys�� ���� ���� ū������ ũ�ų� ���ٸ� end������ �ϳ� ũ��
			if(key>=keys.get(end).getKey()) {
				return end+1;
			}
			
			//�߰��� ��������
			while(start<=end) {
				mid=(start+end)/2;
				//start�� 0�̰� end�� 1�� mid�� 0�� ���ͼ� ���ʳ�� ������ ���� �з�
				if(mid !=0) {
					if((keys.get(mid-1).getKey()<=key) && (key<keys.get(mid).getKey())) {
						index=mid;
						break;
					}else if(key<keys.get(mid).getKey()) {
						end=mid-1;
					}else {
						start=mid+1;
					}
				}else {
					if(key<keys.get(mid).getKey()) {
						index=mid;
						break;
					}else {
						start=mid+1;
					}
				}
			}
			return index;
		}
		/*
		 * leafNode�� �����ؾ� �� ���  ����ϴ� �Լ�
		 */
		private void splitLeafNode(Node mNode) {
			int midN = this.degree/2;
			
			Node parentNode=findParent(mNode);
			
			Node upNode = new Node();
			Node rightNode = new Node();
			
			//mNode�� midN index�� ������ key�� value������ rightNode�� �����Ѵ�.
			rightNode.setKeys(mNode.getKeys().subList(midN, mNode.getN()));
			rightNode.setN(rightNode.getKeys().size());
			rightNode.setR(mNode.getR());
			
			//nonleafNode�� �� upNode����
			upNode.getKeys().add(new Key(mNode.getKeys().get(midN).getKey()));
			upNode.getChildNodes().add(mNode);
			upNode.getChildNodes().add(rightNode);
			upNode.setN(1);
			
			//mNode�� upNode�� ���� childNode�� �ɰ��̴� ����
			mNode.getKeys().subList(midN, mNode.getN()).clear();
			//���� Ű �޾Ƽ� �ѱ��
			
			
			mNode.setN(mNode.getKeys().size());
			mNode.setR(rightNode);
			
			mergeNonLeafNode(upNode,parentNode);
		}
		/*
		 * parentNode�� �߰����� ����� �Ͽ��� ���� �θ��� ��Ī
		 */
		private Node findParent(Node mNode) {
			Node parentNode=null;
			Node tmpNode=this.root;
			boolean find =false;
			if(mNode==this.root) {
				parentNode =null;
			}else {
				while(!find) {
					parentNode=tmpNode;
					tmpNode= tmpNode.getChildNodes().get(binSearch(mNode.getKeys().get(0).getKey(),tmpNode.getKeys()));
					if(tmpNode==mNode) {
						find=true;
					}
				}
			}
			return parentNode;
		}
		
		private void mergeNonLeafNode(Node insertedNode, Node existedNode) {
			if(existedNode==null) {
				this.root=insertedNode;
				return;
			}else {
				//existedNode�� ���Ե� �ε��� ���� Ű��
				//insert���� �Ͼ�� ��� mergeNonLeafNode�� insertedNode�� key���� �ϳ��̹Ƿ� �Ʒ��� ���� §��.
				int indexKeyToENode = binSearch(insertedNode.getKeys().get(0).getKey(),existedNode.getKeys());
				Key insertedKey = insertedNode.getKeys().get(0);
				/*
				 * �պ�����
				 * 1. index����  Key�� ���� index+1�� chlidNode����
				 * 2. existedNode�� n�� �缳��
				 * 3. ���� childNode�� leafNode��� r����
				 */
				existedNode.getKeys().add(indexKeyToENode,insertedKey);
				existedNode.getChildNodes().add(indexKeyToENode+1,insertedNode.getChildNodes().get(1));
				
				existedNode.setN(existedNode.getKeys().size());
				
			}
			if(existedNode.getN()==this.degree) {
				splitNonLeafNode(existedNode,findParent(existedNode));
			}
		}
		
		/*
		 * nonLeafNode�� overflow�� split method
		 * @param currNode
		 * overflow node
		 * 
		 * @param parenNode
		 * currNode's parentNode
		 */
		private void splitNonLeafNode(Node currNode, Node parentNode) {
			int mid=this.degree/2;
			
			Node midNode=new Node();
			Node rightNode=new Node();
			/*
			 * 1.mid index�� ��ġ�� key���� ���� ��� midNode�� rightNode ����
			 * 2.rightNode�� key,childNode���� (currNode�� mid+1 ~key size / child size)
			 * 3.rightNode N ����
			 * 4.midNode�� key�� ����
			 * 5.currNode�� rightNode �ڽĳ�忡 �߰�
			 * 6.n����
			 * 7.currNode �����ϱ�
			 */
			
			rightNode.setKeys(currNode.getKeys().subList(mid+1, currNode.getN()));
			rightNode.setChildNodes(currNode.getChildNodes().subList(mid+1, currNode.getChildNodes().size()));
			rightNode.setN(rightNode.getKeys().size());
			
			midNode.getKeys().add(new Key(currNode.getKeys().get(mid).getKey()));
			
			currNode.getKeys().subList(mid, currNode.getN()).clear();
			currNode.getChildNodes().subList(mid+1, currNode.getChildNodes().size()).clear();
			currNode.setN(currNode.getKeys().size());
			
			midNode.getChildNodes().add(currNode);
			midNode.getChildNodes().add(rightNode);
			midNode.setN(1);
			
			if(parentNode==null) {
				this.root=midNode;
				return;
			}else {
				mergeNonLeafNode(midNode,parentNode);
			}
		}
		
		/*
		 * main���� search�� �ҷ����� method
		 */
		public List<String> search(int key){
			List<String> lastPrint = new ArrayList<>();
			int index;
			boolean find =false;
			
			Node node=this.root;
			while(!node.getChildNodes().isEmpty()) {
				index=binSearch(key,node.getKeys());
				if(index==0) {
					lastPrint.add("<"+node.getKeys().get(index).getKey()+"> left");
				}else {
					lastPrint.add("<"+node.getKeys().get(index-1).getKey()+"> right");
				}
				node=node.getChildNodes().get(index);
			}
			List<Key> tmpKeyList = node.getKeys();
			for(int i=0;i<node.getN();i++) {
				if(key==tmpKeyList.get(i).getKey()) {
					lastPrint.add("<"+key+","+node.getKeys().get(i).getValue()+">");
					find =true;
				}
				if(key<tmpKeyList.get(i).getKey()) {
					break;
				}
			}
			
			if(find==false) {
				lastPrint.add("Not Found");
			}
			return lastPrint;
		}
		
		public List<Key> search(int startKey, int endKey){
			List<Key> findkeys = new ArrayList<>();
			Node node=this.root;
			while(!node.getChildNodes().isEmpty()) {
				node= node.getChildNodes().get(binSearch(startKey,node.getKeys()));
			}
			boolean find=false;
			while(node != null && !find) {
				for(int i=0;i<node.getN();i++) {
					if(startKey<=node.getKeys().get(i).getKey() && endKey>=node.getKeys().get(i).getKey()) {
						findkeys.add(node.getKeys().get(i));
					}
					if(node.getKeys().get(i).getKey()>endKey) {
						find=true;
					}
				}
				node = node.getR();
			}
			return findkeys;
		}
		
		
		/*
		 * deletion opreation
		 * 1.���� �ڽĳ�带 �ִ� degree������ŭ������
		 * 2.���� �ִ� degree-1�� ��ŭ�� Ű���� ������
		 * 3.���� ��� ceil(m/2)��ŭ�� �ڽ��� ������ �־���Ѵ�
		 * 4.��Ʈ�� ������ ������ ��� ceil(m-1/2)��ŭ�� Ű�� ������ �־���Ѵ�.
		 */
		
		public void delete(int deletedKey) {
			Node currNode;
			Node parentNode=null;//nonLeafNode���� deletedKey�� ��ġ�ϴ� ���� ���� ��尡 �ִٸ� ����
			int index=-1;
			int leafIndex=-1; //leafNode���� �߰ߵ� ��
			int pfindKeyIndex=-1;//parentNode�� key���� �����Ѵٸ� �� Ű�� �ش�Ǿ��ִ� index
			boolean nonLeafFind=false;
			boolean leafFind=false;
			
			currNode=this.root;
			//nonLeafNode���� LeafNode�� Ž���ϸ鼭 deletedKey�� �ִ��� Ȯ��
			while(!currNode.getChildNodes().isEmpty()) {
				index=binSearch(deletedKey,currNode.getKeys());
				if(index==0) {
					if(deletedKey==currNode.getKeys().get(index).getKey()) {
						//���� ���� ��忡 index�� �ش��ϴ� Ű���� deletedKey�� �����ϸ�
						nonLeafFind=true;
						parentNode=currNode;
						pfindKeyIndex=index;
					}
				}else {
					if(deletedKey==currNode.getKeys().get(index-1).getKey()) {
						nonLeafFind=true;
						parentNode=currNode;
						pfindKeyIndex=index-1;
					}
				}
				currNode=currNode.getChildNodes().get(index);
			}
			for(int i=0;i<currNode.getN();i++) {
				if(deletedKey==currNode.getKeys().get(i).getKey()) {
					leafIndex=i;
					leafFind=true;
					break;
				}
			}
			if(nonLeafFind==false && leafFind==false) {
				System.out.println("ERROR: DON'T HAVE THAT KEY : "+deletedKey);
				return;
			}else {
				deleteFromLeafNode(nonLeafFind,leafFind,index,leafIndex,parentNode,pfindKeyIndex,currNode);
			}
			return;
		}
		
		/*
		 * leafNode����key�� ����
		 */
		private void deleteFromLeafNode(boolean nonLeafFind, boolean leafFind, int parentIndex, int leafIndex, Node pFindNode, int pfindKeyIndex, Node leafNode) {
			int minOfKey =(int)Math.ceil((this.degree-1)/2.0);
			//���� �ƹ��� Ű���� ã�� ���ߴٸ�
			if(leafNode==this.root) {
				//�ش� leafNode �� rootNode��� �׳� Ű�������Ѵ�.
				leafNode.getKeys().remove(leafIndex);
				leafNode.setN(leafNode.getN()-1);
				if(leafNode.getN()==0) {
					this.root=null;
				}
				return;
			}else {
				//Ű���� ã�Ұ� leafNode�� root�� �ƴҶ�
				Node parentNode=findParent(leafNode);
				Key tmpKey;
				
				if(leafNode.getN()>minOfKey) {
					//���� ��������� key���� ����ϴٸ� �׳� ����
					leafNode.getKeys().remove(leafIndex);
					leafNode.setN(leafNode.getN()-1);
					if(nonLeafFind==true) {
						//���� �ش� ���� nonleafNode���� �����Ұ�� leafNode�� �ּҰ��� ��ü���ش�.
						tmpKey=new Key(leafNode.getKeys().get(0).getKey());
						pFindNode.getKeys().remove(pfindKeyIndex);
						pFindNode.getKeys().add(pfindKeyIndex,tmpKey);
					}
				}else {
					//������ ���� �����ü� �ִ��� Ȯ��
					boolean ifRsiblingisLandable =false;
					boolean ifLsiblingisLandable = false;
					if(parentIndex ==0) {
						//���� ���ʳ���� ������ ��� Ȯ��
						if(leafNode.getR().getN()>minOfKey) {
							ifRsiblingisLandable=true;
						}//�ƴ϶�� �״�� false
					}else if(leafNode.getR()==null) {
						
						if(parentNode.getChildNodes().get(parentIndex-1).getN()>minOfKey) {
							ifLsiblingisLandable=true;
						}
					}else {
						//��� �����ϰ��
						if(parentNode.getChildNodes().get(parentIndex-1).getN()>minOfKey) {
							ifLsiblingisLandable=true;
						}
						if(leafNode.getR().getN()>minOfKey) {
							ifRsiblingisLandable=true;
						}
					}
					if(ifLsiblingisLandable) {
						//���ʿ��������ü� �ִٸ�
						Key tmp;
						//tmpNode�� leafNode�� leftNode
						Node tmpNode=parentNode.getChildNodes().get(parentIndex-1);
						//tmp�� ������ Ű��
						tmp=tmpNode.getKeys().get(tmpNode.getN()-1);
						
						leafNode.getKeys().remove(leafIndex);
						leafNode.getKeys().add(0,tmp);
						
						tmpNode.getKeys().remove(tmpNode.getN()-1);
						tmpNode.setN(tmpNode.getN()-1);
						
						parentNode.getKeys().remove(parentIndex-1);
						parentNode.getKeys().add(parentIndex-1,tmp);
						if(nonLeafFind==true) {
							tmpKey=new Key(leafNode.getKeys().get(0).getKey());
							pFindNode.getKeys().remove(pfindKeyIndex);
							pFindNode.getKeys().add(pfindKeyIndex,tmpKey);
						}
					}else if(ifRsiblingisLandable) {
						Key tmp;
						Node tmpNode=leafNode.getR();
						tmp=tmpNode.getKeys().get(0);
						leafNode.getKeys().remove(leafIndex);
						leafNode.getKeys().add(tmp);
						tmpNode.getKeys().remove(0);
						tmpNode.setN(tmpNode.getN()-1);
						
						parentNode.getKeys().remove(parentIndex);
						parentNode.getKeys().add(parentIndex,new Key(tmp.getKey()));
						if(nonLeafFind==true) {
							tmpKey=new Key(leafNode.getKeys().get(0).getKey());
							pFindNode.getKeys().remove(pfindKeyIndex);
							pFindNode.getKeys().add(pfindKeyIndex,tmpKey);
						}
					}else {
						//���� ���ʿ��� �Ѵ� �����ü�������
						leafNode.getKeys().remove(leafIndex);
						leafNode.setN(leafNode.getN()-1);
						if(nonLeafFind==true) {
							//������忡 key���� �ִٸ�
							if(leafNode.getN()==0) {
								//���� �����Ŀ� leafNode�� Ű���� 0���ִٸ�
								if(parentIndex==parentNode.getN()) {
									//���� ��������ϰ��
									tmpKey=null;
								}else {
									tmpKey=new Key(leafNode.getR().getKeys().get(0).getKey());
								}
							}else {
								tmpKey=new Key(leafNode.getKeys().get(0).getKey());
							}
							pFindNode.getKeys().remove(pfindKeyIndex);
							pFindNode.getKeys().add(pfindKeyIndex,tmpKey);
						}
						if(parentIndex != 0) {
							balanceLeafNode(parentNode.getChildNodes().get(parentIndex-1),leafNode,parentNode,parentIndex-1);
						}else {
							balanceLeafNode(leafNode,leafNode.getR(),parentNode,parentIndex);
						}
					}
				}
			}
			return;
		}
		
		private void balanceLeafNode(Node node1, Node node2, Node parentNode, int leftChildIndex) {
			/*
			 * 1.node1�� node2�� ��� Ű�� �߰� , node1�� N����
			 * 2.�θ����� childNodes�� Leftchildindex+1��ġ�� ��� ����
			 * 3.�θ��� key���� leftchildindex��ġ Ű �����ϰ� N����
			 * 4.node1�� ������� �缳��
			 */
			int minOfKey =(int)Math.ceil((this.degree-1)/2.0);
			boolean upper=false; //����ؼ� ���� �ö󰡼� balancing�ؾ��ϴ��� �����ľ�
			if(parentNode.getN()<=minOfKey && parentNode !=this.root) {
				//���� �θ��尡 ������ ���� �θ��尡 ��Ʈ�� �ƴ϶�� ���� ��¡�� ����Ѵ�
				upper=true;
			}
			
			for(int i=0;i<node2.getKeys().size();i++) {
				node1.getKeys().add(node2.getKeys().get(i));
			}
			node1.setN(node1.getKeys().size());
			
			parentNode.getChildNodes().remove(leftChildIndex+1);
			parentNode.getKeys().remove(leftChildIndex);
			parentNode.setN(parentNode.getN()-1);
			node1.setR(node2.getR());
			
			if(parentNode.getKeys().size()==0 && findParent(parentNode)==null) {
				//parent��尡 ����� 0�̵ǰ� �� ������尡 null�̶��
				this.root=node1;
				return;
			}
			if(upper) {
				int index;
				Node ppNode=findParent(parentNode);
				if(parentNode.getKeys().size()==0) {
					//���� �θ����� ����� 0�̸� �ֺ���ٸ� node1�� ù��° ������ index���ϱ�
					index=binSearch(node1.getKeys().get(0).getKey(),ppNode.getKeys());
				}else {
					index=binSearch(parentNode.getKeys().get(0).getKey(),ppNode.getKeys());
				}
				if(index==0) {
					//���� ���� ���ʿ� ��ġ�� �����
					balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
				}else {
					if((ppNode.getChildNodes().get(index-1).getN()>minOfKey) || (index==ppNode.getN())) {
						//���� ���ʳ�忡�� �����ü� �ְų� ���� ���� �����
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}else if(ppNode.getChildNodes().get(index+1).getN()>minOfKey) {
						//���� ������忡�� �����ü� �ִٸ�
						balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
					}else {
						//���ʴ� �����ü����ٸ�
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}
				}
			}
			return;
		}
		
		private void balanceNonLeafNode(Node nodeL, Node parentNode, Node nodeR, int leftChildIndex) {
			boolean upper =false;
			int minOfKey =(int)Math.ceil((this.degree-1)/2.0);
			/*
			 * ��� �����ϳ��� ������ �ִٸ� �Ű��ְ�
			 * ������ ���ٸ� �θ��忡�� leftChildIndex��ġ�� Ű�� NodeR�� ���Ű�� NodeL�� �ִ´�
			 * ���� �װ�� ��Ʈ��� Ű ���� 0�̶�� NodeL�� ���ο� root�� �ȴ�
			 */
			if((parentNode.getN()<=minOfKey) && parentNode != this.root) {
				upper=true;
			}
			if(nodeL.getN()>minOfKey) {
				//������尡 ������ �ִٸ�
				Node tmp;
				tmp = nodeL.getChildNodes().get(nodeL.getN());
				//tmp�� nodeL�� ���� ���� ���
				nodeR.getKeys().add(0,parentNode.getKeys().get(leftChildIndex));
				nodeR.setN(nodeR.getKeys().size());
				
				parentNode.getKeys().remove(leftChildIndex);
				parentNode.getKeys().add(leftChildIndex,nodeL.getKeys().get(nodeL.getN()-1));
				
				nodeR.getChildNodes().add(0,tmp);
				
				nodeL.getChildNodes().remove(nodeL.getN());
				nodeL.getKeys().remove(nodeL.getN()-1);
				nodeL.setN(nodeL.getN()-1);
			}else if(nodeR.getN()>minOfKey) {
				//������尡 ������ �ִٸ�
				nodeL.getKeys().add(parentNode.getKeys().get(leftChildIndex));
				nodeL.setN(nodeL.getKeys().size());
				
				parentNode.getKeys().remove(leftChildIndex);
				parentNode.getKeys().add(leftChildIndex,nodeR.getKeys().get(0));
				
				nodeL.getChildNodes().add(nodeR.getChildNodes().get(0));
				
				nodeR.getChildNodes().remove(0);
				nodeR.getKeys().remove(0);
				nodeR.setN(nodeR.getN()-1);
			}else {
				//�Ѵ� ������ ���ٸ�
				nodeL.getKeys().add(parentNode.getKeys().get(leftChildIndex));
				for(int i=0;i<nodeR.getN();i++) {
					nodeL.getKeys().add(nodeR.getKeys().get(i));
				}
				for(int i=0;i<nodeR.getChildNodes().size();i++) {
					nodeL.getChildNodes().add(nodeR.getChildNodes().get(i));
				}
				parentNode.getChildNodes().remove(leftChildIndex+1);
				parentNode.getKeys().remove(leftChildIndex);
				
				nodeL.setN(nodeL.getKeys().size());
				parentNode.setN(parentNode.getKeys().size());
				if(parentNode.getN()==0 && parentNode ==this.root) {
					//���� parent����� ��� Ű�� ����� ��ٸ�
					this.root=nodeL;
					return;
				}
			}
			if(upper) {
				//���� �� ���ľ��Ѵٸ�
				int index;
				Node ppNode= findParent(parentNode);
				if(parentNode.getKeys().size()==0) {
					index=binSearch(nodeL.getKeys().get(0).getKey(),ppNode.getKeys());
				}else {
					index=binSearch(parentNode.getKeys().get(0).getKey(),ppNode.getKeys());
				}
				if(index==0) {
					balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
				}else {
					if((ppNode.getChildNodes().get(index-1).getN()>minOfKey) || (index==ppNode.getN())) {
						//���� ���ʳ�忡�� �����ü� �ְų� ���� ���� �����
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}else if(ppNode.getChildNodes().get(index+1).getN()>minOfKey) {
						//���� ������忡�� �����ü� �ִٸ�
						balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
					}else {
						//���ʴ� �����ü����ٸ�
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}
				}
			}
			
		}
	}
