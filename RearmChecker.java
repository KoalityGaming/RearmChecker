public class RearmChecker {
	public static void main(String[] args) {
	
		ViewListener vl = new RearmCheckModel();
		ModelListener ml = new RearmCheckView(args[0]);
		
		vl.setView(ml);
		ml.setModel(vl);
	
	}
}