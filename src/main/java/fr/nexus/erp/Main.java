package fr.nexus.erp;

public class Main {

    public static void main(String[] args){

        int NiveauAccrementation = 6;
        String[] taches = {"initialisation", "Connexion DB", "Nettoyage", "Export"};

        if(SystemBoot.checkAccess(NiveauAccrementation)){
            SystemBoot.progTache(taches);
            DataEngine.runPipeline();
        }else {
            return;
        }
    }
}
