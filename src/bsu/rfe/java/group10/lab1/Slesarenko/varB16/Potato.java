package bsu.rfe.java.group10.lab1.Slesarenko.varB16;

public class Potato extends Food{
    private String type;
    public Potato(String type) {
        super("Potato");
        this.type = type;
    }
    public void consume() {
        System.out.println(this + " eaten");
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
//    public boolean equals(Object arg0) {
//        if (super.equals(arg0)) { // Шаг 1
//            if (!(arg0 instanceof Potato)) return false; // Шаг 2
//            return type.equals(((Potato)arg0).type); // Шаг 3
//        } else
//            return false;
//    }
    public String toString() {
        return super.toString() + " type '" + type.toUpperCase() + "'"; //функция, выз.родительский конструктор(объект)
    }
}
