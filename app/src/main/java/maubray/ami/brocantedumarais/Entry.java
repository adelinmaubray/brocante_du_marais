package maubray.ami.brocantedumarais;

public class Entry {

    private String id;
    private String libelle;
    private int value;

    public Entry() {
        this.id = null;
        this.libelle = null;
        this.value = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", libelle='" + libelle + '\'' +
                ", value=" + value +
                '}';
    }
}
