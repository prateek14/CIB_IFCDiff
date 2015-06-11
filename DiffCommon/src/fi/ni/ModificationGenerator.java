package fi.ni;


public class ModificationGenerator{
	

	public ModificationGenerator(String directory, String filename, String type, String setname) {
		ModelHandler first = new ModelHandler();
		first.handle(directory,filename, type, setname);		
		
	}

	
	static public void test(String directory,String filename, String type,String setname) {
		new ModificationGenerator(directory,filename, type,setname);
	}


	static public void testset() {
		test("c:/2014/a_testset/","Drum_A.ifc", "IFC","50");
	}

	public static void main(String[] args) {

		testset();
		System.out.println("Done");
	}
}
