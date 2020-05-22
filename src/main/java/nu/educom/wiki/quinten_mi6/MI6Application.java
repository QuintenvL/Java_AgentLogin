package nu.educom.wiki.quinten_mi6;
import nu.educom.wiki.quinten_mi6.controllers.AppController;
import nu.educom.wiki.quinten_mi6.cruds.Crud;

public class MI6Application {

	public static void main(String[] args) {
		Crud crud = new Crud();
		AppController controller = new AppController(crud);
		controller.actionPerformed(null);
	}
}
