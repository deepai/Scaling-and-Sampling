
public class hk {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tmp="abc\\def";
		String[] ep=tmp.split("\\\\");
		System.out.println(ep[1]);
	}

}
