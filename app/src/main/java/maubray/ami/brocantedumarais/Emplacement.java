package maubray.ami.brocantedumarais;

public class Emplacement {

    private String number;
    private String entry;
    private String scan;
    private String code;
    private int refus;

    public Emplacement (){
        this.number = null;
        this.code = null;
        this.entry = null;
        this.scan = null;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    public int getRefus() {
        return refus;
    }

    public void setRefus(int refus) {
        this.refus = refus;
    }

    @Override
    public String toString() {
        return "Emplacement{" +
                "number='" + number + '\'' +
                ", entry='" + entry + '\'' +
                ", scan='" + scan + '\'' +
                ", code='" + code + '\'' +
                ", refus=" + refus +
                '}';
    }
}
