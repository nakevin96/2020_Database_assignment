import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Admin {
	private String inputname;
	private String inputpass;
	private String url = "jdbc:mariadb://localhost:3307/musicstreaming";
	Connection admincon;
	
	static {
		try {Class.forName("org.mariadb.jdbc.Driver");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private Admin() {};
	public Admin(String name , String pass) {
		this.inputname=name;
		this.inputpass =pass;
	}
	
	public static String GetAdminSsn() {
		String admin=null;
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"root","with7742I@");
			Statement st = admincon.createStatement();
			String sql = "SELECT 관리자주민번호 FROM 음원관리자";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				admin=rs.getString(1);
			}
			st.close();
			admincon.close();
			return admin;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return admin;
	}
	
	
	public static void Withdrawl(String ssn) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"root","with7742I@");
			Statement st = admincon.createStatement();
			String sql = "DELETE FROM musicstreaming.스트리밍구독자 WHERE 구독자주민번호='"+ssn+"'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()==false) {
				System.out.println("가입되어있지 않은 사용자입니다.");
			}else {
				System.out.println("삭제되었습니다.");	
			}
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ShowAllUserSimple() {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"root","with7742I@");
			Statement st = admincon.createStatement();
			String sql = "SELECT ID, 성명, 구독자주민번호 FROM 스트리밍구독자 ORDER BY 성명";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				String id = rs.getString(1);
				String name =rs.getString(2);
				String ssn = rs.getString(3);
				System.out.println("성명:"+name+"  ID: "+id+"   주민등록번호:"+ssn);
			}
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ShowAllUserDetail() {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"root","with7742I@");
			Statement st = admincon.createStatement();
			String sql = "SELECT 성명, 구독자주민번호, 주소, 핸드폰번호, 구독개월수, 구독시작날짜, ID FROM 스트리밍구독자 ORDER BY 구독시작날짜";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString(1);
				String ssn = rs.getString(2);
				String addr = rs.getString(3);
				String phonenum=rs.getString(4);
				int subscribeMonth = rs.getInt(5);
				String start = rs.getString(6);
				String id = rs.getString(7);
				String[] sstart = start.split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(sstart[0]), Integer.parseInt(sstart[1]), Integer.parseInt(sstart[2]));
				LocalDate sdate = date.plusMonths(subscribeMonth);
				LocalDate ndate = LocalDate.now();
				long days = ChronoUnit.DAYS.between(ndate, sdate);
				System.out.println("성명:"+name+"  ID:  "+id+"  주민등록번호:"+ssn+
						"  주소:"+addr+"\t핸드폰번호:"+phonenum+"  구독시작날짜:"+start+"  남은날짜:"+days);
			}
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void SearchUser(String n) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"root","with7742I@");
			Statement st = admincon.createStatement();
			String sql = "SELECT 성명, 구독자주민번호, 구독개월수, 구독시작날짜, ID  FROM 스트리밍구독자 WHERE 성명='"+n+"'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString(1);
				String ssn = rs.getString(2);
				int subscribeMonth = rs.getInt(3);
				String start = rs.getString(4);
				String id = rs.getString(5);
				String[] sstart = start.split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(sstart[0]), Integer.parseInt(sstart[1]), Integer.parseInt(sstart[2]));
				LocalDate sdate = date.plusMonths(subscribeMonth);
				LocalDate ndate = LocalDate.now();
				long days = ChronoUnit.DAYS.between(ndate, sdate);
				System.out.println("성명:"+name+"  ID:"+id+"   주민등록번호:"+ssn+"  남은 날짜:"+days+"일");
			}
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isAdmin() {
		try {
			admincon = DriverManager.getConnection(this.url,this.inputname,this.inputpass);
			System.out.println("로그인성공");
			return true;
		}catch(Exception e) {
			System.out.println("Wrong admin... get out...");
			System.gc();
			System.exit(-1);
			return false;
		}
	}
	
}
