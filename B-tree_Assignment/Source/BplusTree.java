
import java.util.*;

public class BplusTree{

	/*
	 * @param degree
	 * 이 BplusTree의 차수
	 * 
	 * @param root
	 * 이 BplusTree의 루트노드
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
		 *  insert BplusTree를 작동시켰을때 key와 value를 받아 노드에 저장하기위한 메소드
		 */
		public void insert(int key, int value) {
			//만약 아무 값도 들어와있지 않은 상태라면
			if(this.root==null) {
				Node newNode = new Node();
				newNode.getKeys().add(new Key(key,value));
				newNode.setN(1);
				
				this.root=newNode;
			}else if(this.root.getChildNodes().isEmpty() &&  this.root.getN()<this.degree-1) {
				//root Node 만 존재하고 추가하여도 overFlow되지 않을 경우
				insertInLeafNode(this.root,key,value);
			}else {
				Node sNode = this.root;
				//sNode를 이용하여 리프노드까지 내려간다
				//제일 우측 leafNode와 루트노드만 있을 경우는 childNode가 없으므로 isEmpty()로 체크 나머지는 우측 노드를 가리키므로 
				//childNode의 사이즈가 1이다. 
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
		 * leafNode에 key와 value insert
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
		 * key값과 key배열을 받으면 어디로 가야하는지 index값 반환
		 */
		public int binSearch(int key, List<Key> keys) {
			int start =0;
			int end = keys.size()-1;
			int mid;
			int index=-1;
			//if key가 keys에 속한 첫째 값보다 작다면 0
			//값이 같다면 오른쪽으로가야한다.
			if(key<keys.get(start).getKey()) {
				return 0;
			}
			//값이 keys에 속한 제일 큰값보다 크거나 같다면 end값보다 하나 크게
			if(key>=keys.get(end).getKey()) {
				return end+1;
			}
			
			//중간에 속했을때
			while(start<=end) {
				mid=(start+end)/2;
				//start가 0이고 end가 1면 mid가 0이 나와서 왼쪽노드 에러가 나니 분류
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
		 * leafNode가 분할해야 할 경우  사용하는 함수
		 */
		private void splitLeafNode(Node mNode) {
			int midN = this.degree/2;
			
			Node parentNode=findParent(mNode);
			
			Node upNode = new Node();
			Node rightNode = new Node();
			
			//mNode의 midN index를 포함한 key와 value묶음을 rightNode에 저장한다.
			rightNode.setKeys(mNode.getKeys().subList(midN, mNode.getN()));
			rightNode.setN(rightNode.getKeys().size());
			rightNode.setR(mNode.getR());
			
			//nonleafNode가 될 upNode설정
			upNode.getKeys().add(new Key(mNode.getKeys().get(midN).getKey()));
			upNode.getChildNodes().add(mNode);
			upNode.getChildNodes().add(rightNode);
			upNode.setN(1);
			
			//mNode가 upNode의 좌측 childNode가 될것이니 설정
			mNode.getKeys().subList(midN, mNode.getN()).clear();
			//우측 키 받아서 넘기기
			
			
			mNode.setN(mNode.getKeys().size());
			mNode.setR(rightNode);
			
			mergeNonLeafNode(upNode,parentNode);
		}
		/*
		 * parentNode를 추가하지 말라고 하여서 만든 부모노드 서칭
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
				//existedNode에 삽입될 인덱스 값과 키값
				//insert에서 일어나는 모든 mergeNonLeafNode의 insertedNode는 key값이 하나이므로 아래와 같이 짠다.
				int indexKeyToENode = binSearch(insertedNode.getKeys().get(0).getKey(),existedNode.getKeys());
				Key insertedKey = insertedNode.getKeys().get(0);
				/*
				 * 합병과정
				 * 1. index값에  Key값 삽입 index+1에 chlidNode삽입
				 * 2. existedNode의 n값 재설정
				 * 3. 만약 childNode가 leafNode라면 r설정
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
		 * nonLeafNode의 overflow시 split method
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
			 * 1.mid index에 위치한 key값을 가질 노드 midNode와 rightNode 생성
			 * 2.rightNode에 key,childNode삽입 (currNode의 mid+1 ~key size / child size)
			 * 3.rightNode N 설정
			 * 4.midNode에 key값 삽입
			 * 5.currNode와 rightNode 자식노드에 추가
			 * 6.n설정
			 * 7.currNode 조정하기
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
		 * main에서 search시 불려오는 method
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
		 * 1.노드는 자식노드를 최대 degree갯수만큼가진다
		 * 2.노드는 최대 degree-1개 만큼의 키값을 가진다
		 * 3.노드는 적어도 ceil(m/2)만큼의 자식을 가지고 있어야한다
		 * 4.루트를 제외한 노드들은 적어도 ceil(m-1/2)만큼의 키를 가지고 있어야한다.
		 */
		
		public void delete(int deletedKey) {
			Node currNode;
			Node parentNode=null;//nonLeafNode에서 deletedKey와 일치하는 값을 가진 노드가 있다면 저장
			int index=-1;
			int leafIndex=-1; //leafNode에서 발견된 값
			int pfindKeyIndex=-1;//parentNode에 key값이 존재한다면 그 키가 해당되어있는 index
			boolean nonLeafFind=false;
			boolean leafFind=false;
			
			currNode=this.root;
			//nonLeafNode에서 LeafNode로 탐색하면서 deletedKey가 있는지 확인
			while(!currNode.getChildNodes().isEmpty()) {
				index=binSearch(deletedKey,currNode.getKeys());
				if(index==0) {
					if(deletedKey==currNode.getKeys().get(index).getKey()) {
						//만약 현재 노드에 index에 해당하는 키값이 deletedKey와 동일하면
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
		 * leafNode에서key를 제거
		 */
		private void deleteFromLeafNode(boolean nonLeafFind, boolean leafFind, int parentIndex, int leafIndex, Node pFindNode, int pfindKeyIndex, Node leafNode) {
			int minOfKey =(int)Math.ceil((this.degree-1)/2.0);
			//만약 아무런 키값을 찾지 못했다면
			if(leafNode==this.root) {
				//해당 leafNode 가 rootNode라면 그냥 키값제거한다.
				leafNode.getKeys().remove(leafIndex);
				leafNode.setN(leafNode.getN()-1);
				if(leafNode.getN()==0) {
					this.root=null;
				}
				return;
			}else {
				//키값을 찾았고 leafNode가 root가 아닐때
				Node parentNode=findParent(leafNode);
				Key tmpKey;
				
				if(leafNode.getN()>minOfKey) {
					//만약 리프노드의 key값이 충분하다면 그냥 삭제
					leafNode.getKeys().remove(leafIndex);
					leafNode.setN(leafNode.getN()-1);
					if(nonLeafFind==true) {
						//만약 해당 값이 nonleafNode에도 존재할경우 leafNode의 최소값과 교체해준다.
						tmpKey=new Key(leafNode.getKeys().get(0).getKey());
						pFindNode.getKeys().remove(pfindKeyIndex);
						pFindNode.getKeys().add(pfindKeyIndex,tmpKey);
					}
				}else {
					//오른쪽 왼쪽 빌려올수 있는지 확인
					boolean ifRsiblingisLandable =false;
					boolean ifLsiblingisLandable = false;
					if(parentIndex ==0) {
						//제일 왼쪽노드라면 오른쪽 노드 확인
						if(leafNode.getR().getN()>minOfKey) {
							ifRsiblingisLandable=true;
						}//아니라면 그대로 false
					}else if(leafNode.getR()==null) {
						
						if(parentNode.getChildNodes().get(parentIndex-1).getN()>minOfKey) {
							ifLsiblingisLandable=true;
						}
					}else {
						//가운데 값들일경우
						if(parentNode.getChildNodes().get(parentIndex-1).getN()>minOfKey) {
							ifLsiblingisLandable=true;
						}
						if(leafNode.getR().getN()>minOfKey) {
							ifRsiblingisLandable=true;
						}
					}
					if(ifLsiblingisLandable) {
						//왼쪽에서빌려올수 있다면
						Key tmp;
						//tmpNode는 leafNode의 leftNode
						Node tmpNode=parentNode.getChildNodes().get(parentIndex-1);
						//tmp는 가져올 키값
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
						//만약 양쪽에서 둘다 빌려올수없을때
						leafNode.getKeys().remove(leafIndex);
						leafNode.setN(leafNode.getN()-1);
						if(nonLeafFind==true) {
							//상위노드에 key값이 있다면
							if(leafNode.getN()==0) {
								//만약 삭제후에 leafNode의 키값이 0개있다면
								if(parentIndex==parentNode.getN()) {
									//제일 우측노드일경우
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
			 * 1.node1에 node2의 모든 키값 추가 , node1의 N변경
			 * 2.부모노드의 childNodes의 Leftchildindex+1위치의 노드 삭제
			 * 3.부모노드 key에서 leftchildindex위치 키 삭제하고 N변경
			 * 4.node1의 우측노드 재설정
			 */
			int minOfKey =(int)Math.ceil((this.degree-1)/2.0);
			boolean upper=false; //계속해서 위로 올라가서 balancing해야하는지 여부파악
			if(parentNode.getN()<=minOfKey && parentNode !=this.root) {
				//만약 부모노드가 여유가 없고 부모노드가 루트가 아니라면 위로 머징을 계속한다
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
				//parent노드가 사이즈가 0이되고 그 상위노드가 null이라면
				this.root=node1;
				return;
			}
			if(upper) {
				int index;
				Node ppNode=findParent(parentNode);
				if(parentNode.getKeys().size()==0) {
					//만약 부모노드의 사이즈가 0이면 텅비엇다면 node1의 첫번째 값으로 index구하기
					index=binSearch(node1.getKeys().get(0).getKey(),ppNode.getKeys());
				}else {
					index=binSearch(parentNode.getKeys().get(0).getKey(),ppNode.getKeys());
				}
				if(index==0) {
					//만약 제일 왼쪽에 위치한 노드라면
					balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
				}else {
					if((ppNode.getChildNodes().get(index-1).getN()>minOfKey) || (index==ppNode.getN())) {
						//만약 왼쪽노드에서 빌려올수 있거나 제일 우측 노드라면
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}else if(ppNode.getChildNodes().get(index+1).getN()>minOfKey) {
						//만약 우측노드에서 빌려올수 있다면
						balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
					}else {
						//양쪽다 빌려올수없다면
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
			 * 노드 둘중하나가 여유가 있다면 옮겨주고
			 * 여유가 없다면 부모노드에서 leftChildIndex위치의 키와 NodeR의 모든키를 NodeL에 넣는다
			 * 만약 그결과 루트노드 키 수가 0이라면 NodeL이 새로운 root가 된다
			 */
			if((parentNode.getN()<=minOfKey) && parentNode != this.root) {
				upper=true;
			}
			if(nodeL.getN()>minOfKey) {
				//좌측노드가 여유가 있다면
				Node tmp;
				tmp = nodeL.getChildNodes().get(nodeL.getN());
				//tmp는 nodeL의 제일 우측 노드
				nodeR.getKeys().add(0,parentNode.getKeys().get(leftChildIndex));
				nodeR.setN(nodeR.getKeys().size());
				
				parentNode.getKeys().remove(leftChildIndex);
				parentNode.getKeys().add(leftChildIndex,nodeL.getKeys().get(nodeL.getN()-1));
				
				nodeR.getChildNodes().add(0,tmp);
				
				nodeL.getChildNodes().remove(nodeL.getN());
				nodeL.getKeys().remove(nodeL.getN()-1);
				nodeL.setN(nodeL.getN()-1);
			}else if(nodeR.getN()>minOfKey) {
				//우측노드가 여유가 있다면
				nodeL.getKeys().add(parentNode.getKeys().get(leftChildIndex));
				nodeL.setN(nodeL.getKeys().size());
				
				parentNode.getKeys().remove(leftChildIndex);
				parentNode.getKeys().add(leftChildIndex,nodeR.getKeys().get(0));
				
				nodeL.getChildNodes().add(nodeR.getChildNodes().get(0));
				
				nodeR.getChildNodes().remove(0);
				nodeR.getKeys().remove(0);
				nodeR.setN(nodeR.getN()-1);
			}else {
				//둘다 여유가 없다면
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
					//만약 parent노드의 모든 키를 끌어다 썼다면
					this.root=nodeL;
					return;
				}
			}
			if(upper) {
				//만약 또 합쳐야한다면
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
						//만약 왼쪽노드에서 빌려올수 있거나 제일 우측 노드라면
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}else if(ppNode.getChildNodes().get(index+1).getN()>minOfKey) {
						//만약 우측노드에서 빌려올수 있다면
						balanceNonLeafNode(parentNode,ppNode,ppNode.getChildNodes().get(index+1),index);
					}else {
						//양쪽다 빌려올수없다면
						balanceNonLeafNode(ppNode.getChildNodes().get(index-1),ppNode,parentNode,index-1);
					}
				}
			}
			
		}
	}
