package java4project;


public class Java4Project {

        public static void main(String[] args) {
        EtudiantRepository repo =
            new EtudiantRepository("data/universite.xml");
        MediaRepository medRepo = new MediaRepository();

        new LoginFrame(repo,medRepo);
  }
}
