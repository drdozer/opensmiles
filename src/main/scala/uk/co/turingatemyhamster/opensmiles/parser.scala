package uk.co.turingatemyhamster.opensmiles

import fastparse.all._

object Parser {

  // http://www.opensmiles.org/spec/open-smiles-2-grammar.html
  lazy val star = P ( "*" )

  // ATOMS
  /* 3.1   */ lazy val atom = P( bracket_atom | aliphatic_organic | aromatic_organic | star )

  // ORGANIC SUBSET ATOMS
  /* 3.1.5 */ lazy val aliphatic_organic = P( "B" | "C" | "N" | "O" | "S" | "P" | "F" | "Cl" | "Br" | "I" )
  /* 3.5   */ lazy val aromatic_organic = P( "b" | "c" | "n" | "o" | "s" | "p" )

  // BRACKET ATOMS
  /* 3.1.1 */ lazy val bracket_atom = P( "[" ~/ isoSym ~/ chiral.? ~ hcount.? ~ charge.? ~ `class`.? ~ "]" )
  lazy val isoSym = P( (isotope ~/ symbol) | symbol )
  /* 3.1.1 */ lazy val symbol = P( element_symbols | aromatic_symbols | star )
  /* 3.1.4 */ lazy val isotope = P( NUMBER )
  
  /* 3.1.1 */ lazy val element_symbols = P( element_s1 | element_s2 | element_s3 | element_s4 | element_s5 | element_s6 | element_s7 |
                                            element_t1 | element_t2 )
  lazy val element_s1 = "H" |                                                                                "He"
  lazy val element_s2 = "Li"|"Be"|                                                  "B" |"C" |"N" |"O" |"F" |"Ne"
  lazy val element_s3 = "Na"|"Mg"|                                                  "Al"|"Si"|"p" |"S" |"Cl"|"Ar"
  lazy val element_s4 = "K" |"Ca"|"Sc"|"Ti"|"V" |"Cr"|"Mn"|"Fe"|"Co"|"Ni"|"Cu"|"Zn"|"Ga"|"Ge"|"As"|"Se"|"Br"|"Kr"
  lazy val element_s5 = "Rb"|"Sr"|"Y" |"Zr"|"Nb"|"Mo"|"Tc"|"Ru"|"Rh"|"Pb"|"Ag"|"Cd"|"In"|"Sn"|"Sb"|"Te"|"I" |"Xe"
  lazy val element_s6 = "Cs"|"Ba"|     "Hf"|"Ta"|"W" |"Re"|"Os"|"Ir"|"Pt"|"Au"|"Hg"|"Ti"|"Pb"|"Bi"|"Po"|"At"|"Rn"
  lazy val element_s7 = "Fr"|"Ra"|     "Rf"|"Db"|"Sg"|"Hs"|"Mt"|"Ds"|"Rg"
  lazy val element_t1 = "La"|"Ce"|"Pr"|"Nd"|"Pm"|"Sm"|"Eu"|"Gd"|"Tb"|"Dy"|"Ho"|"Er"|"Tm"|"Yb"|"Lu"
  lazy val element_t2 = "Ac"|"Th"|"Pa"|"U" |"Np"|"Pu"|"Am"|"Cm"|"Bk"|"Cf"|"Es"|"Fm"|"Md"|"No"|"Lr"
  /* 3.5  */ lazy val aromatic_symbols = P( "c" | "n" | "o" | "p" | "s" | "se" | "as" )

  // CHIRALITY
  lazy val chiral = P(
    "@" ~/ (
      "@" |
      ("TH" ~/ oneToTwo) |
      ("AL" ~/ oneToTwo) |
      ("SP" ~/ oneToThirty) |
      ("TB" ~/ oneToThirty) ).? )
  lazy val oneToTwo = CharIn('1' to '2')
  lazy val oneToThirty = oneToNine | tenToTwentyNine | thirty
  lazy val oneToNine = CharIn('1' to '9')
  lazy val tenToTwentyNine = oneToTwo ~ DIGIT
  lazy val thirty = "30"

  // HYDROGENS
  /* 3.1.2 */ lazy val hcount = P( "H" ~ DIGIT.? )

  // CHARGE
  /* 3.1.3 */ lazy val charge = P( ("-" ~ DIGIT.?) |
                                   ("+" ~ DIGIT.?) |
                                   "--" | "++" )

  // ATOM_CLASS
  /* 3.1.7 */ lazy val `class` = P( ":" ~ NUMBER )

  // BONDS AND CHAINS
  /* 3.2
     3.9.3 */ lazy val bond = P( "-" | "=" | "#" | "$" | ":" | "/" | "\\" )
  /* 3.4   */ lazy val ringbond = P( (bond.? ~ DIGIT) |
                                     (bond.? ~ "%" ~ DIGIT ~ DIGIT) )
  /* 3.3   */ lazy val branched_atom = P( atom ~ ringbond.rep ~ branch.rep )
              lazy val branch: Parser[Unit] = P( "(" ~ (bond | dot).? ~ chain ~ ")" )
              lazy val chain = P( branched_atom ~ (branched_atom.rep(0) ~ (bond | dot).? ~ branched_atom).? )
  /* 3.7   */ lazy val dot = P( "." )

  // SMILES STRINGS
  /* 3.10  */ lazy val smiles = P( chain ~ terminator )
  lazy val terminator = " \t" | 0x000A.toString | 0x000D.toString | End

  // SUNDRY
  lazy val DIGIT = P( CharIn('0' to '9') )
  lazy val NUMBER = P( DIGIT.rep(1) )

}

class OpenSmilesParser {
  // Horrible API for Java
  def validate(smiles: String): Unit = Parser.smiles.parse(smiles) match {
    case f : Parsed.Failure =>
      throw new IllegalArgumentException(f.extra.traced.trace)
    case _ =>
  }

  def check(smiles: String): Boolean = Parser.smiles.parse(smiles) match {
    case _ : Parsed.Success[Unit] =>
      true
    case _ =>
      false
  }
}
