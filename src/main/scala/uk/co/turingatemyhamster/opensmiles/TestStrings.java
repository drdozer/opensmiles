package uk.co.turingatemyhamster.opensmiles;

public class TestStrings {

  public static void main(String[] args) {
    OpenSmilesParser parser = new OpenSmilesParser();

    String smiles = "[K+4]";

    System.out.println(parser.check(smiles));
    parser.validate(smiles);
  }

}
