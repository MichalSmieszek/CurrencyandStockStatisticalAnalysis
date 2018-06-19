

public class Main {
    public static void main (String[] args) {

       Currency2.Write("USD");
        Currency2.Write("JPY");
        Currency2.Write("GBP");
        Currency2.Write("EUR");
        Currency2.Write("CHF");
        Currency2.PairToJson("USD","JPY");
        Currency2.PairToJson("USD","NOK");
        Currency2.PairToJson("USD","CAD");
        Currency2.PairToJson("CAD","NOK");
        Currency2.PairToJson("CAD","JPY");
        Stock.getData("Oil");
        Stock.getData("Gold");
        Stock.getData("Aluminium");
        Stock.getData("Silver");

    }
}