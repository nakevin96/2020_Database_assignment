
import java.util.*;

public class ShowInterface {
	private Admin admin;
	private User user;
	private boolean isEnd=false;
	private boolean isRootEnd=false;
	private boolean isUserEnd=false;
	static Scanner in;
	public boolean playlistExist=false;
	private void ShowEnd() {
		System.out.println();
		System.out.println();
		System.out.println("Thank You For Using Our Application");
		System.out.println("Have a nice day! :)");
		System.out.println();
		System.out.println();
	}
	
	private char HomeMenu() {
		char select;
		System.out.println("======================");
		System.out.println("0. Exit");
		System.out.println("1. Login by Administrator");
		System.out.println("2. Login by User");
		System.out.println("3. Sign UP");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	
	private boolean LoginAdmin(ShowInterface s) {
		String name= new String();
		String pw= new String();
		System.out.print("Please Put Your ID : ");
		name = in.nextLine();
		System.out.print("Please Put Your PW : ");
		pw = in.nextLine();
		System.out.println("OK.. Wait a minute..");
		s.setAdmin(new Admin(name,pw));
		return true;
	}
	
	private char showAdminMenu() {
		char select;
		System.out.println("======================");
		System.out.println("0. exit program");
		System.out.println("1. Show and Modify User");
		System.out.println("2. Show and Modify Music");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;	
	}
	private char showAdmin_User() {
		char select;
		System.out.println("======================");
		System.out.println("0. Back");
		System.out.println("1. Retrieve User");
		System.out.println("2. Delete User");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	private char showAdmin_User_Search() {
		char select;
		System.out.println("======================");
		System.out.println("1. Show All Users(name,ssn)");
		System.out.println("2. Search User by name(name,ssn,remainDay)");
		System.out.println("3. Show All Users in detail(name,ssn,addr,phone-num,remainDay)");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	private char showAdmin_Music() {
		char select;
		System.out.println("======================");
		System.out.println("0. Back");
		System.out.println("1. Retrieve Music");
		System.out.println("2. ADD Music");
		System.out.println("3. Delete Music");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	private char showAdmin_Music_Search() {
		char select;
		System.out.println("======================");
		System.out.println("1. Show All Music Simple(title,singer,MusicNumber)");
		System.out.println("2. Search Music (all information)");
		System.out.println("3. Show All Music in Detail(all information)");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	private char showUser_Music_Search() {
		char select;
		System.out.println("======================");
		System.out.println("0. Back");
		System.out.println("1. Show All Music Simple(title,singer,MusicNumber)");
		System.out.println("2. Search Music (all information)");
		System.out.println("3. Show All Music in Detail(all information)");
		System.out.println("4. Play the music");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	private char showUser_Playlist() {
		char select;
		System.out.println("======================");
		System.out.println("0. Back");
		System.out.println("1. Show My playlist List");
		System.out.println("2. Use playlist");
		System.out.println("3. Add playlist");
		System.out.println("4. Delete playlist");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	private char add_del_playlist() {
		char select;
		System.out.println("======================");
		System.out.println("0. Back");
		System.out.println("1. play music");
		System.out.println("2. add music");
		System.out.println("3. delete music");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;
	}
	
	private boolean LoginUser(ShowInterface s) {
		String name= new String();
		String pw= new String();
		System.out.print("Please Put Your ID : ");
		name = in.nextLine();
		System.out.print("Please Put Your PW : ");
		pw = in.nextLine();
		System.out.println("OK.. Wait a minute..");
		s.setUser(new User(name,pw));
		return s.getUser().isInDatabase();	
	}
	
	private char showUserMenu() {
		char select;
		System.out.println("======================");
		System.out.println("0. exit program");
		System.out.println("1. Search Music");
		System.out.println("2. My playList");
		System.out.println("3. My MusicRanking");
		System.out.println("4. My Information");
		System.out.println("5. WithDrawl");
		System.out.print("SELECT: ");
		select = in.next().charAt(0);
		in.nextLine();
		System.out.println("======================");
		return select;	
	}
	
	private void mainMenu(ShowInterface program) {
		while(!program.isEnd) {
			char n = program.HomeMenu();
			switch(n) {
			//exit Program
			case '0':
				program.ShowEnd();
				program.isEnd=true;
				in.close();
				return ;
			//Login admin	
			case '1':
				if(program.LoginAdmin(program)) {
					program.isEnd=true;
					while(!program.isRootEnd) {
						char r_select = program.showAdminMenu();
						switch(r_select) {
						//exit program
						case '0':
							program.ShowEnd();
							program.isRootEnd=true;
							break;
						//maintain user
						case '1':
							boolean muS=false;
							while(!muS) {
								char mu = program.showAdmin_User();
								switch(mu) {
								case '0':
									muS=true;
									break;
								case '1':
									char su= program.showAdmin_User_Search();
									switch(su) {
									case '1':
										Admin.ShowAllUserSimple();
										break;
									case '2':
										String usn;
										System.out.print("검색하고자하는 유저의 이름을 입력해주세요:");
										usn=in.nextLine();
										Admin.SearchUser(usn);
										break;
									case '3':
										Admin.ShowAllUserDetail();
										break;
									default:
										System.out.println("Wrong Select");
										break;
									}
									break;
								case '2':
									String userSsn;
									System.out.print("삭제시키고 싶은 사용자의 주민번호를 입력하여 주세요:");
									userSsn=in.nextLine();
									Admin.Withdrawl(userSsn);
									break;
								}
							}
							
							break;
						//maintain music	
						case '2':
							boolean muM=false;
							while(!muM) {
								char mm = program.showAdmin_Music();
								switch(mm) {
								case '0':
									muM=true;
									break;
								case '1':
									char sm= program.showAdmin_Music_Search();
									switch(sm) {
									case '1':
										Music.ShowAllMusicSimple();
										break;
									case '2':
										char searchSelect;
										String input;
										System.out.println("======================");
										System.out.println("1.타이틀로 검색");
										System.out.println("2.가수이름으로 검색");
										System.out.println("3.장르명으로 검색");
										System.out.println("4.앨범명으로 검색");
										System.out.println("선택:");
										System.out.println("======================");
										searchSelect=in.next().charAt(0);
										in.nextLine();
										System.out.print("검색어를 입력:");
										input=in.nextLine();
										Music.SearchMusic(searchSelect,input);
										break;
									case '3':
										char c;
										System.out.println("======================");
										System.out.println("1.타이틀순으로 보기");
										System.out.println("2.랭킹(플레이회수순) 보기");
										System.out.print("선택:");
										c=in.next().charAt(0);
										in.nextLine();
										System.out.println("======================");
										
										if(c=='1') {
											c='7';
										}else if(c=='2') {
											c='8';
										}else {
											System.out.println("잘못입력했어요.");
										}
										Music.SearchMusic(c, "allInstancePrint");
										break;
									default:
										System.out.println("Wrong Select");
										break;
									}
									break;
								case '2':
									//add music
									String[] musicInfo = program.takeMusicInfo();
									Music.UploadMusic(musicInfo[0], musicInfo[1], musicInfo[2], musicInfo[3]);
									break;
								case '3'://delete music
									String musicControlNum;
									System.out.print("삭제시키고 싶은 음악의 관리번호를 입력하여 주세요:");
									musicControlNum=in.nextLine();
									Music.WithdrawlMusic(musicControlNum);
									
									break;
								default:
									System.out.println("잘못 선택하셨습니다. 다시 제대로 입력해주세요");
								}
							}
						default:
							System.out.println("You put wrong select number, select again");
							break;
						}
					}
				}
				break;
			//login user	
			case '2':
				if(program.LoginUser(program)) {
					program.isEnd=true;
					while(!program.isUserEnd) {
						char u_select = program.showUserMenu();
						switch(u_select) {
						case '0':
							program.ShowEnd();
							program.isUserEnd=true;
							break;
						case '1':
							//음악검색기능 -> 나갈지 아니면 플레이할지
							boolean mse=false;
							while(!mse) {
								char sm= program.showUser_Music_Search();
								switch(sm) {
								case '0':
									mse=true;
									break;
								case '1':
									Music.ShowAllMusicSimple();
									break;
								case '2':
									char searchSelect;
									String input;
									System.out.println("======================");
									System.out.println("1.타이틀로 검색");
									System.out.println("2.가수이름으로 검색");
									System.out.println("3.장르명으로 검색");
									System.out.println("4.앨범명으로 검색");
									System.out.println("선택:");
									searchSelect=in.next().charAt(0);
									in.nextLine();
									System.out.println("======================");
									
									System.out.print("검색어를 입력:");
									input=in.nextLine();
									Music.SearchMusic(searchSelect,input);
									break;
								case '3':
									char c;
									System.out.println("======================");
									System.out.println("1.타이틀순으로 보기");
									System.out.println("2.랭킹(플레이회수순) 보기");
									System.out.print("선택:");
									c=in.next().charAt(0);
									in.nextLine();
									System.out.println("======================");
									
									if(c=='1') {
										c='7';
									}else if(c=='2') {
										c='8';
									}else {
										System.out.println("잘못입력했어요.");
									}
									Music.SearchMusic(c, "allInstancePrint");
									break;
								case '4':
									//음악 플레이
									System.out.print("플레이를 원하는 곡의 관리번호를 입력하여주세요:");
									int playnum=in.nextInt();
									in.nextLine();
									Music.PlayMusic(playnum,program.getUser().getId());
									break;
								default:
									System.out.println("Wrong Select");
									break;
								}
							}
							break;
						case '2':
							boolean ple=false;
							while(!ple) {
								char sp= program.showUser_Playlist();
								switch(sp) {
								case '0':
									ple=true;
									break;
								case '1':
									//플레이리스트 목차
									program.getUser().showMyPlayList();
									break;
								case '2':
									//플레이리스트 선택
									String p_name;
									System.out.print("사용하고싶은 플레이리스트 이름 입력:");
									p_name=in.nextLine();
									program.playlistExist=false;
									program.getUser().usePlayList(p_name,program);
									if(program.playlistExist) {
										boolean plue=false;
										while(!plue) {
											//플레이리스트에서 값 추가 및 제거기능
											
											char ap = program.add_del_playlist();
											switch(ap) {
											case '0':
												plue=true;
												break;
											case '1':
												System.out.print("플레이를 원하는 곡의 관리번호를 입력하여주세요:");
												int playnum=in.nextInt();
												in.nextLine();
												Music.PlayMusicPlaylist(playnum,program.getUser().getId(),p_name);
												break;
											case '2':
												System.out.print("플레이리스트에 추가하고자하는 곡의 관리번호를 입력해주세요:");
												int addnum=in.nextInt();
												in.nextLine();
												program.getUser().AddTiltleToPlayList(p_name,addnum);
												break;
											case '3':
												System.out.print("플레이리스트에서 삭제하고자하는 곡의 관리번호를 입력해주세요:");
												int delnum=in.nextInt();
												in.nextLine();
												program.getUser().DelTiltleToPlayList(p_name,delnum);
												break;
											default:
												System.out.println("잘못 입력하셨습니다.");
											}
										}
									}
									program.playlistExist=false;
									break;
								case '3':
									//playlist 만들기
									System.out.println("플레이리스트를 생성합니다.");
									System.out.print("생성할 플레이리스트 명을 입력해주세요:");
									String addListname=in.nextLine();
									program.getUser().AddPlayList(addListname);
									break;
								case '4':
									//playlist 없애기
									
									System.out.println("플레이리스트를 삭제합니다.");
									System.out.print("삭제할 플레이리스트 명을 입력해주세요:");
									String delListname=in.nextLine();
									program.getUser().DelPlayList(delListname);
									
									break;
								default :
									System.out.println("메뉴를 확인하시고 다시 입력해주세요.");
								}
							}
							//내 재생목록 확인 -> 선택 -> 나갈지 아니면 플레이할지
							break;
						case '3':
							program.getUser().GetUserTitleRanking();
							break;
						case '4':
							//내 개인정보확인
							String myId;
							String mypass;
							System.out.println("정보확인을 위해 본인 아이디와 비밀번호를 입력해야합니다.");
							System.out.print("ID:");
							myId=in.nextLine();
							System.out.print("PW:");
							mypass=in.nextLine();
							program.getUser().CheckMyInformation(myId,mypass);
							break;
						case '5':
							//회원탈퇴
							String userSsn;
							System.out.print("회원 탈퇴를 위하여 본인 주민번호를 입력하여 주세요:");
							userSsn=in.nextLine();
							Admin.Withdrawl(userSsn);
							break;
						}
					}
				}
				program.getUser().userLogout();
				break;
			case '3':
				//회원가입
				String[] newUser = takeUserInfo();
				User.SignUp(newUser[0], newUser[1], newUser[2], newUser[3], Integer.parseInt(newUser[4]),newUser[5], newUser[6],newUser[7]);
				break;
			default:
				System.out.println("You put wrong select number");
				break;
			}
			
		}
	}
	
	private String[] takeUserInfo() {
		String[] s = new String[8];
		System.out.println("사용자 정보를 정확하게 입력해주세요");
		//(String gender,String address,String Phone,String ssn,int subscribe, String pw,String id)
		System.out.print("사용자 이름:");
		s[6]=in.nextLine();
		System.out.print("사용하려고 하는 ID:");
		s[7]=in.nextLine();
		System.out.print("사용하고자 하는 비밀번호: ");
		s[5]=in.nextLine();
		System.out.print("성별(남자면 M 여자면 F):");
		s[0]=in.nextLine();
		System.out.print("주소:");
		s[1]=in.nextLine();
		System.out.print("핸드폰번호:");
		s[2]=in.nextLine();
		System.out.print("주민등록번호:");
		s[3]=in.nextLine();
		System.out.print("구독하고자 하는 개월 수:");
		s[4]=in.nextLine();
		
		return s;
	}
	
	private String[] takeMusicInfo() {
		String[] s = new String[4];
		System.out.println("음악 정보를 정확하게 입력해주세요");
		//(String title,String singer,String genre,String Alubum)
		System.out.print("음악 이름(타이틀):");
		s[0]=in.nextLine();
		System.out.print("가수 이름:");
		s[1]=in.nextLine();
		System.out.print("장르:");
		s[2]=in.nextLine();
		System.out.print("앨범명:");
		s[3]=in.nextLine();
		return s;
	}
	
	public static void main(String[] args){
		in = new Scanner(System.in);
		ShowInterface program = new ShowInterface();
		program.mainMenu(program);
		in.close();
		System.gc();
		return;
	}
	
	public Admin getAdmin() {
		return this.admin;
	}
	public void setAdmin(Admin a) {
		this.admin=a;
	}
	public User getUser() {
		return this.user;
	}
	public void setUser(User u) {
		this.user =u;
	}
	
}
