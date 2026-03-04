package fr.nexus.erp;

public class SystemBoot {

    public static boolean checkAccess(int accreLvl){
        if(accreLvl < 5){
            System.out.println("Niveau pas suffisant");
            return false;
        }else{
            System.out.println("Niveau suffisant | Acces autorisée");
            return true;
        }
    }

    public static void progTache(String[] taches){
        for(String tache : taches){
            System.out.println("Les Taches : " + tache);
        }
    }
}
